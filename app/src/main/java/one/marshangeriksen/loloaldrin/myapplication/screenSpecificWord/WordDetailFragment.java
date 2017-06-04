package one.marshangeriksen.loloaldrin.myapplication.screenSpecificWord;


import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import one.marshangeriksen.loloaldrin.myapplication.R;
import one.marshangeriksen.loloaldrin.myapplication.objectModels.Word;

import static android.provider.UserDictionary.Words.WORD;

public class WordDetailFragment extends Fragment {
    @BindView(R.id.tvVocab)
    public TextView tvVocab;
    @BindView(R.id.tvVocal)
    public TextView tvVocal;
    @BindView(R.id.tvExplanation)
    public TextView tvExplanation;
    @BindView(R.id.tvMeaning)
    public TextView tvMeaning;
    @BindView(R.id.tvExample)
    public TextView tvExample;
    @BindView(R.id.tvExampleTrans)
    public TextView tvExampleTrans;
    @BindView(R.id.tvExampleLabel)
    public TextView tvExampleLabel;
    @BindView(R.id.btnFav)
    public ImageButton btnFav;
    @BindView(R.id.btnSound)
    public ImageButton btnSound;
    OnFavoriteChangeListener listener;
    private Word word;
    private View view;

    public WordDetailFragment() {
        // Required empty public constructor
    }

    public static WordDetailFragment newInstance(Word word) {
        WordDetailFragment fragment = new WordDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(WORD, word);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFavoriteChangeListener)
            listener = (OnFavoriteChangeListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            word = (Word) getArguments().getSerializable(WORD);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_word_detail, container, false);
        ButterKnife.bind(this, view);
        SpannableString content = new SpannableString(getString(R.string.example));
        content.setSpan(new UnderlineSpan(), 0, getString(R.string.example).length(), 0);
        tvExampleLabel.setText(content);
        tvVocab.setText(word.getVocabulary());
        tvVocal.setText(word.getVocalization());
        tvMeaning.setText(word.getTranslation());
        tvExample.setText(word.getExample());
        tvExplanation.setText(word.getExplanation());
        tvExampleTrans.setText(word.getExampleTranslation());
        setBtnFavBackground(word.isFavorite());
        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                word.setFavorite(!word.isFavorite());
                if (listener != null)
                    listener.onFavoriteChange();
                setBtnFavBackground(word.isFavorite());
            }
        });

        btnSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AssetFileDescriptor pronunciation = getActivity().getAssets()
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
        return view;
    }

    @Override
    public void onDestroy() {
        listener = null;
        super.onDestroy();
    }

    private void setBtnFavBackground(boolean favorite) {
        if (favorite)
            btnFav.setBackgroundResource(R.drawable.icon_fav);
        else
            btnFav.setBackgroundResource(R.drawable.icon_fav_border);
    }

    interface OnFavoriteChangeListener {
        void onFavoriteChange();
    }
}
