package one.marshangeriksen.loloaldrin.myapplication.Topic;

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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import one.marshangeriksen.loloaldrin.myapplication.R;
import one.marshangeriksen.loloaldrin.myapplication.SquareImageView;
import one.marshangeriksen.loloaldrin.myapplication.Models.Topic;
import one.marshangeriksen.loloaldrin.myapplication.Models.Word;
import one.marshangeriksen.loloaldrin.myapplication.WordAdapter;

import static one.marshangeriksen.loloaldrin.myapplication.Constant.TOPIC_BUNDLE;
import static one.marshangeriksen.loloaldrin.myapplication.Constant.TOPIC_ID_BUNDLE;

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
    private TopicController controller;

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
        tvTopic.setText(topic.getName().split("[.] ")[1]);
        Glide.with(this)
                .load(R.drawable.img_topic)
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
        controller = new TopicController(this);
        int topicId = getIntent().getIntExtra(TOPIC_ID_BUNDLE, 0);
        wordList = controller.getAllWordsBaseOnTopic(topicId);
        adapter = new WordAdapter(this,wordList,controller);
        rcvWord.setAdapter(adapter);
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
        controller.close();
    }
}
