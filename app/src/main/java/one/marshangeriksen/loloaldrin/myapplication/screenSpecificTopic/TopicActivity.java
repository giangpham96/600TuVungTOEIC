package one.marshangeriksen.loloaldrin.myapplication.screenSpecificTopic;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import one.marshangeriksen.loloaldrin.myapplication.R;
import one.marshangeriksen.loloaldrin.myapplication.adapters.WordAdapter;
import one.marshangeriksen.loloaldrin.myapplication.customViews.SquareImageView;
import one.marshangeriksen.loloaldrin.myapplication.objectModels.Topic;
import one.marshangeriksen.loloaldrin.myapplication.objectModels.Word;

import static one.marshangeriksen.loloaldrin.myapplication.Constant.TOPIC_BUNDLE;
import static one.marshangeriksen.loloaldrin.myapplication.Constant.TOPIC_ID_BUNDLE;
import static one.marshangeriksen.loloaldrin.myapplication.Constant.TOPIC_RES_ID_BUNDLE;

public class TopicActivity extends AppCompatActivity {
    @BindView(R.id.toolbarTopic)
    public Toolbar toolbarTopic;
    @BindView(R.id.rcvWord)
    public RecyclerView rcvWord;
    @BindView(R.id.imgThumbnail)
    public SquareImageView imgThumbnail;
    @BindView(R.id.tvTopic)
    public TextView tvTopic;
    private List<Word> wordList;
    private WordAdapter adapter;
    private TopicDatabaseModel model;
    private int topicId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        ButterKnife.bind(this);
        setSupportActionBar(toolbarTopic);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Topic topic = (Topic) getIntent().getSerializableExtra(TOPIC_BUNDLE);
        int resId = getIntent().getIntExtra(TOPIC_RES_ID_BUNDLE, 0);
        tvTopic.setText(topic.getName().split("[.] ")[1]);
        Glide.with(this)
                .load(resId)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(R.drawable.img_topic)
                .placeholder(R.drawable.img_topic)
                .atMost()
                .animate(android.R.anim.fade_in)
                .approximate()
                .into(new BitmapImageViewTarget(imgThumbnail) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory
                                .create(getResources(), resource);
                        drawable.setCircular(true);
                        imgThumbnail.setImageDrawable(drawable);
                    }
                });
        model = new TopicDatabaseModel(this);
        topicId = getIntent().getIntExtra(TOPIC_ID_BUNDLE, 0);
        wordList = new ArrayList<>();
        adapter = new WordAdapter(this, wordList, model);
        rcvWord.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        wordList.clear();
        wordList.addAll(model.getAllWordsBaseOnTopic(topicId));
        adapter.setWords(wordList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        model.close();
    }
}
