package one.marshangeriksen.loloaldrin.myapplication.screenSpecificWord;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import one.marshangeriksen.loloaldrin.myapplication.R;
import one.marshangeriksen.loloaldrin.myapplication.Utils;
import one.marshangeriksen.loloaldrin.myapplication.adapters.ViewPagerAdapter;
import one.marshangeriksen.loloaldrin.myapplication.objectModels.Word;

import static one.marshangeriksen.loloaldrin.myapplication.Constant.KEY_ID;

public class WordActivity extends AppCompatActivity implements WordDetailFragment.OnFavoriteChangeListener {
    @BindView(R.id.toolbarWord)
    Toolbar toolbarWord;
    @BindView(R.id.viewpagerWord)
    ViewPager viewPagerWord;
    @BindView(R.id.fabStart)
    FloatingActionButton fabStart;
    @BindView(R.id.fabStop)
    FloatingActionButton fabStop;
    @BindView(R.id.tabLayoutWord)
    TabLayout tabLayoutWord;
    RecordFragment recordFragment;
    private Word word;
    private WordDatabaseModel model;
    private MediaRecorder mediaRecorder = null;
    private View.OnClickListener btnClick = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fabStart: {
                    startRecording();
                    enableButtons(true);
                    break;
                }
                case R.id.fabStop: {
                    stopRecording();
                    enableButtons(false);
                    break;
                }
            }
        }
    };

    private void stopRecording() {
        mediaRecorder.stop();
        mediaRecorder.release();
        recordFragment.onResume();
    }

    private void startRecording() {
        try {
            File file = Utils.getOutputFile(this, word.getId(), "record_" + System.currentTimeMillis());
            MediaRecorderReady(file.getAbsolutePath());
            mediaRecorder.prepare();
            mediaRecorder.start();
            Thread.sleep(1000);
        } catch (IllegalStateException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);
        ButterKnife.bind(this);
        int id = getIntent().getIntExtra(KEY_ID, 0);
        model = new WordDatabaseModel(this);
        word = model.getWordBaseOnId(id);
        setSupportActionBar(toolbarWord);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbarWord.setTitle(word.getVocabulary());
        tabLayoutWord.setupWithViewPager(viewPagerWord);
        setupViewPager(viewPagerWord);
        setButtonHandlers();
        enableButtons(false);
        initPermission();
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
        model.close();
        super.onDestroy();
    }

    @Override
    public void onFavoriteChange() {
        model.updateFavorite(word);
    }

    private void setButtonHandlers() {
        fabStart.setOnClickListener(btnClick);
        fabStop.setOnClickListener(btnClick);
    }

    private void enableButtons(boolean isRecording) {
        fabStart.setEnabled(!isRecording);
        fabStop.setEnabled(isRecording);
    }

    public void MediaRecorderReady(String filePath) {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(filePath);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(WordDetailFragment.newInstance(word), "Word");
        recordFragment = RecordFragment.newInstance(word);
        adapter.addFrag(recordFragment, "Records");
        viewPager.setAdapter(adapter);
    }

    public void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.RECORD_AUDIO) !=
                            PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO}, 1);
            }
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
            if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 1);
            }
        }
    }
}
