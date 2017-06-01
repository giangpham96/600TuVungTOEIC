package one.marshangeriksen.loloaldrin.myapplication.favorite;


import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import one.marshangeriksen.loloaldrin.myapplication.R;
import one.marshangeriksen.loloaldrin.myapplication.objectModels.Word;


public class FavoriteFragment extends Fragment {

    @BindView(R.id.rcvFavWord)
    public RecyclerView rcvFavWord;
    @BindView(R.id.noFavFound)
    public View noFav;
    private View view;
    private FavoriteModel model;
    private List<Word> favoriteWordList;
    private FavoriteAdapter adapter;
    public FavoriteFragment() {
        // Required empty public constructor
    }

    public static FavoriteFragment newInstance() {
        return new FavoriteFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_favorite, container, false);
        ButterKnife.bind(this, view);
        model = new FavoriteModel(getActivity());
        favoriteWordList = new ArrayList<>();
        adapter = new FavoriteAdapter(favoriteWordList);
        rcvFavWord.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        favoriteWordList.clear();
        favoriteWordList.addAll(model.getAllFavoriteWords());
        adapter.notifyDataSetChanged();
        processNotifyNoFav();
    }

    private void processNotifyNoFav() {
        if (favoriteWordList.size() == 0) {
            noFav.setVisibility(View.VISIBLE);
            noFav.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fav_check_appear));
        } else
            noFav.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        model.close();
    }

    class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {
        private List<Word> words;

        FavoriteAdapter(List<Word> words) {
            this.words = words;
        }

        @Override
        public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_word, parent, false);
            return new FavoriteViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final FavoriteViewHolder holder, int position) {
            final Word word = words.get(position);
            holder.tvVocab.setText(word.getVocabulary());
            holder.tvVocal.setText(word.getVocalization());
            holder.tvExplanation.setText(word.getExplanation());
            holder.tvTranslation.setText(word.getTranslation());
            holder.imgFav.setImageResource(R.drawable.icon_fav);
            holder.imgFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    word.setFavorite(false);
                    model.updateFavorite(word);
                    words.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                    processNotifyNoFav();
                }
            });
            holder.imgSound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        AssetFileDescriptor afd =FavoriteFragment.this.getActivity().getAssets()
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

        class FavoriteViewHolder extends RecyclerView.ViewHolder {
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

            FavoriteViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
