package one.marshangeriksen.loloaldrin.myapplication.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
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
import one.marshangeriksen.loloaldrin.myapplication.ViewPagerAdapter;
import one.marshangeriksen.loloaldrin.myapplication.favorite.FavoriteActivity;
import one.marshangeriksen.loloaldrin.myapplication.main.dictionary.DictionaryFragment;
import one.marshangeriksen.loloaldrin.myapplication.main.test.TestFragment;
import one.marshangeriksen.loloaldrin.myapplication.main.topicList.TopicFragment;

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
                invalidateOptionsMenu();
                if(position==0)
                    fabFav.show();
                else
                    fabFav.hide();
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (viewPager.getCurrentItem() != 2) {
            menu.clear();
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void processCopyDatabase() {
        File dbFile = getDatabasePath(DB_NAME);
        if (!dbFile.exists()) {
            try {
                copyDataBaseFromAsset();
                Toast.makeText(this, "Copying success from Assets folder", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
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
        adapter.addFrag(TopicFragment.newInstance(), "Topic");
        adapter.addFrag(TestFragment.newInstance(), "Test");
        adapter.addFrag(DictionaryFragment.newInstance(), "Dictionary");
        viewPager.setAdapter(adapter);
    }
}
