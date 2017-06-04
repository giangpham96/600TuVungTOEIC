package one.marshangeriksen.loloaldrin.myapplication.screenMain;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import one.marshangeriksen.loloaldrin.myapplication.R;
import one.marshangeriksen.loloaldrin.myapplication.ReminderBroadcastReceiver;
import one.marshangeriksen.loloaldrin.myapplication.adapters.ViewPagerAdapter;
import one.marshangeriksen.loloaldrin.myapplication.screenFavorite.FavoriteActivity;
import one.marshangeriksen.loloaldrin.myapplication.screenHighscore.HighScoreActivity;
import one.marshangeriksen.loloaldrin.myapplication.screenMain.dictionary.DictionaryFragment;
import one.marshangeriksen.loloaldrin.myapplication.screenMain.test.TestFragment;
import one.marshangeriksen.loloaldrin.myapplication.screenMain.topicList.TopicFragment;

public class MainActivity extends AppCompatActivity {
    private static final String DB_NAME = "toeic600.db";
    private static final String DB_PATH_SUFFIX = "/databases/";
    @BindView(R.id.tabLayoutMain)
    public TabLayout tabLayout;
    @BindView(R.id.viewpagerMain)
    public ViewPager viewPager;
    @BindView(R.id.toolbarMain)
    public Toolbar toolbar;
    @BindView(R.id.fabFav)
    public FloatingActionButton fabFav;
    public SearchView searchView;
    private DictionaryFragment dictionaryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        processCopyDatabase();
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    fabFav.show();
                } else {
                    fabFav.hide();
                }
                if (searchView != null)
                    searchView.setQuery("", false);
                invalidateOptionsMenu();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        fabFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
                startActivity(intent);
            }
        });
        startServiceIfNeeded();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        switch (viewPager.getCurrentItem()) {
            case 2:
                getMenuInflater().inflate(R.menu.search_menu, menu);
                final MenuItem mnuSearch = menu.findItem(R.id.mnu_search);
                searchView = (SearchView) mnuSearch.getActionView();
                searchView.setQueryHint(getString(R.string.search));
                searchView.setIconifiedByDefault(false);
                searchView.setOnQueryTextListener(dictionaryFragment);
                break;
            case 1:
                getMenuInflater().inflate(R.menu.high_score_menu, menu);
                break;
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void processCopyDatabase() {
        File dbFile = getDatabasePath(DB_NAME);
        if (!dbFile.exists()) {
            try {
                copyDataBaseFromAsset();
            } catch (Exception e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnu_high:
                Intent intent = new Intent(this, HighScoreActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void copyDataBaseFromAsset() {
        try {
            InputStream myInput;

            myInput = getAssets().open(DB_NAME);
            String outFileName = getDatabaseDirectory();
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists())
                f.mkdir();
            OutputStream myOutput = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getDatabaseDirectory() {
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX + DB_NAME;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(TopicFragment.newInstance(), getString(R.string.topic));
        adapter.addFrag(TestFragment.newInstance(), getString(R.string.test));
        dictionaryFragment = DictionaryFragment.newInstance();
        adapter.addFrag(dictionaryFragment, getString(R.string.dictionary));
        viewPager.setAdapter(adapter);
    }

    private void startServiceIfNeeded() {
        if (PendingIntent.getBroadcast(
                this,
                ReminderBroadcastReceiver.REQUEST_CODE,
                new Intent(getApplicationContext(), ReminderBroadcastReceiver.class),
                PendingIntent.FLAG_NO_CREATE) == null) {
            scheduleAlarm();
        }
    }

    public void scheduleAlarm() {
        Intent intent = new Intent(getApplicationContext(), ReminderBroadcastReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(
                this,
                ReminderBroadcastReceiver.REQUEST_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        long firstAlarm = System.currentTimeMillis() + 3 * 60 * 60 * 1000;
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                firstAlarm,
                3 * 60 * 60 * 1000,
                pIntent);
    }
}
