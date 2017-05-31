package one.marshangeriksen.loloaldrin.myapplication;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import one.marshangeriksen.loloaldrin.myapplication.Models.Word;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> {
    private Activity activity;
    private List<Word> words;
    private BaseController controller;
    public WordAdapter(Activity activity, List<Word> words,BaseController controller) {
        this.activity = activity;
        this.words = words;
        this.controller = controller;
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_word, parent, false);
        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final WordViewHolder holder, int position) {
        final Word word = words.get(position);
        holder.tvVocab.setText(word.getVocabulary());
        holder.tvVocal.setText(word.getVocalization());
        holder.tvExplanation.setText(word.getExplanation());
        holder.tvTranslation.setText(word.getTranslation());
        holder.imgFav.startAnimation(
                AnimationUtils.loadAnimation(activity, R.anim.fav_check_appear));
        if (!word.isFavorite())
            holder.imgFav.setImageResource(R.drawable.icon_fav_border);
        else
            holder.imgFav.setImageResource(R.drawable.icon_fav);
        holder.imgFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                word.setFavorite(!word.isFavorite());
                controller.updateFavorite(word);
                onBindViewHolder(holder,holder.getAdapterPosition());
            }
        });

        holder.imgSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AssetFileDescriptor afd =activity.getAssets()
                            .openFd(word.getVocabulary()+".mp3");
                    MediaPlayer player = new MediaPlayer();
                    player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                    player.prepare();
                    player.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    class WordViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvVocab)
        TextView tvVocab;
        @BindView(R.id.tvVocal)
        TextView tvVocal;
        @BindView(R.id.tvExplanation)
        TextView tvExplanation;
        @BindView(R.id.tvTranslation)
        TextView tvTranslation;
        @BindView(R.id.imgFav)
        ImageView imgFav;
        @BindView(R.id.imgSound)
        ImageView imgSound;

        WordViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}