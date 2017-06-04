package one.marshangeriksen.loloaldrin.myapplication.adapters;

import android.app.Activity;
import android.content.Intent;
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
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import one.marshangeriksen.loloaldrin.myapplication.BaseDatabaseModel;
import one.marshangeriksen.loloaldrin.myapplication.R;
import one.marshangeriksen.loloaldrin.myapplication.objectModels.Word;
import one.marshangeriksen.loloaldrin.myapplication.screenSpecificWord.WordActivity;

import static one.marshangeriksen.loloaldrin.myapplication.Constant.KEY_ID;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> {
    private Activity activity;
    private List<Word> words;
    private BaseDatabaseModel model;

    public WordAdapter(Activity activity, List<Word> words, BaseDatabaseModel model) {
        this.activity = activity;
        this.words = new ArrayList<>(words);
        this.model = model;
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_word, parent, false);
        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        Word word = words.get(position);
        holder.bind(word);
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    public void setWords(List<Word> words) {
        this.words = new ArrayList<>(words);
    }

    private Word removeItem(int position) {
        final Word word = words.remove(position);
        notifyItemRemoved(position);
        return word;
    }

    private void addItem(int position, Word word) {
        words.add(position, word);
        notifyItemInserted(position);
    }

    private void moveItem(int fromPosition, int toPosition) {
        final Word word = words.remove(fromPosition);
        words.add(toPosition, word);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<Word> words) {
        applyAndAnimateRemovals(words);
        applyAndAnimateAdditions(words);
        applyAndAnimateMovedItems(words);
    }

    private void applyAndAnimateRemovals(List<Word> newWords) {
        for (int i = words.size() - 1; i >= 0; i--) {
            final Word model = words.get(i);
            if (!newWords.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Word> newWords) {
        for (int i = 0, count = newWords.size(); i < count; i++) {
            final Word word = newWords.get(i);
            if (!words.contains(word)) {
                addItem(i, word);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Word> newWords) {
        for (int toPosition = newWords.size() - 1; toPosition >= 0; toPosition--) {
            final Word word = newWords.get(toPosition);
            final int fromPosition = words.indexOf(word);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
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
        @BindView(R.id.container)
        View view;

        WordViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(final Word word) {
            tvVocab.setText(word.getVocabulary());
            tvVocal.setText(word.getVocalization());
            tvExplanation.setText(word.getExplanation());
            tvTranslation.setText(word.getTranslation());
            imgFav.startAnimation(
                    AnimationUtils.loadAnimation(activity, R.anim.fav_check_appear));
            if (!word.isFavorite())
                imgFav.setImageResource(R.drawable.icon_fav_border);
            else
                imgFav.setImageResource(R.drawable.icon_fav);
            imgFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = words.indexOf(word);
                    word.setFavorite(!word.isFavorite());
                    model.updateFavorite(word);
                    notifyItemChanged(position);
                }
            });

            imgSound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        AssetFileDescriptor pronunciation = activity.getAssets()
                                .openFd(word.getVocabulary() + ".mp3");
                        MediaPlayer player = new MediaPlayer();
                        player.setDataSource(pronunciation.getFileDescriptor(), pronunciation.getStartOffset(), pronunciation.getLength());
                        player.prepare();
                        player.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, WordActivity.class);
                    intent.putExtra(KEY_ID, word.getId());
                    activity.startActivity(intent);
                }
            });
        }
    }
}