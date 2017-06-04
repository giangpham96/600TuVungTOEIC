package one.marshangeriksen.loloaldrin.myapplication.screenHighscore;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import one.marshangeriksen.loloaldrin.myapplication.BaseDatabaseModel;
import one.marshangeriksen.loloaldrin.myapplication.R;
import one.marshangeriksen.loloaldrin.myapplication.objectModels.HighScore;

public class HighScoreActivity extends AppCompatActivity {
    @BindView(R.id.rcvHighScore)
    RecyclerView rcvHighScore;
    @BindView(R.id.toolbarHigh)
    Toolbar toolbar;
    List<HighScore> highScores;
    BaseDatabaseModel baseDatabaseModel;
    HighScoreAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        baseDatabaseModel = new BaseDatabaseModel(this);
        highScores = baseDatabaseModel.getHighScoreList();
        adapter = new HighScoreAdapter(highScores);
        rcvHighScore.setAdapter(adapter);
    }

    @OnClick(R.id.btnReset)
    void resetHighScore() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_confirm_submit);
        dialog.setCancelable(false);

        ((TextView) dialog.findViewById(R.id.tv_title)).setText(R.string.reset_high_score);
        ((TextView) dialog.findViewById(R.id.tv_mess)).setText(R.string.confirm_reset);
        dialog.findViewById(R.id.btn_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseDatabaseModel.resetHighScore();
                highScores.clear();
                highScores.addAll(baseDatabaseModel.getHighScoreList());
                adapter.notifyItemRangeChanged(0, highScores.size());
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.btn_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
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
        baseDatabaseModel.close();
    }

    class HighScoreAdapter extends RecyclerView.Adapter<HighScoreAdapter.FavoriteViewHolder> {
        private List<HighScore> highScores;

        HighScoreAdapter(List<HighScore> highScores) {
            this.highScores = highScores;
        }

        @Override
        public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_highscore, parent, false);
            return new FavoriteViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(FavoriteViewHolder holder, int position) {
            HighScore highScore = highScores.get(position);
            holder.bind(highScore);
        }

        @Override
        public int getItemCount() {
            return highScores.size();
        }

        class FavoriteViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tvNo)
            TextView tvNo;
            @BindView(R.id.tvName)
            TextView tvName;
            @BindView(R.id.tvScore)
            TextView tvScore;

            FavoriteViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            public void bind(HighScore highScore) {
                tvNo.setText(highScores.indexOf(highScore) + 1 + "");
                tvName.setText(highScore.getName_highscore());
                tvScore.setText(highScore.getPassnumber_highscore() + " " + getString(R.string.points));
            }
        }
    }
}
