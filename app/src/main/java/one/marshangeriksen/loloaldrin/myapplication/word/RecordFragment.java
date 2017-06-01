package one.marshangeriksen.loloaldrin.myapplication.word;


import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import one.marshangeriksen.loloaldrin.myapplication.R;
import one.marshangeriksen.loloaldrin.myapplication.Utils;
import one.marshangeriksen.loloaldrin.myapplication.objectModels.Word;

import static android.provider.UserDictionary.Words.WORD;
import static one.marshangeriksen.loloaldrin.myapplication.Utils.sdf;

public class RecordFragment extends Fragment {

    @BindView(R.id.rcvRecord)
    RecyclerView rcvRecord;
    @BindView(R.id.noRecFound)
    View noRec;
    List<File> files;
    RecordAdapter adapter;
    private Word word;
    private View view;

    public RecordFragment() {
        // Required empty public constructor
    }

    public static RecordFragment newInstance(Word word) {
        RecordFragment fragment = new RecordFragment();
        Bundle args = new Bundle();
        args.putSerializable(WORD, word);
        fragment.setArguments(args);
        return fragment;
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
        view = inflater.inflate(R.layout.fragment_record, container, false);
        ButterKnife.bind(this, view);
        files = new ArrayList<>();
        adapter = new RecordAdapter(files);
        rcvRecord.setAdapter(adapter);
        return view;
        // Inflate the layout for this fragment
    }

    @Override
    public void onResume() {
        super.onResume();
        files.clear();
        files.addAll(Utils.listFilesInFolder(Utils.getWordFolder(getActivity(), word.getId())));
        processNotifyNoRecord();
        adapter.notifyDataSetChanged();
    }


    private void processNotifyNoRecord() {
        if (files.size() == 0) {
            noRec.setVisibility(View.VISIBLE);
            noRec.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fav_check_appear));
        } else
            noRec.setVisibility(View.GONE);
    }

    class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {

        List<File> files;

        RecordAdapter(List<File> files) {
            this.files = files;
        }

        @Override
        public RecordAdapter.RecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_record, parent, false);
            return new RecordViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecordAdapter.RecordViewHolder holder, int position) {
            File file = files.get(position);
            holder.bind(file);
        }

        @Override
        public int getItemCount() {
            return files.size();
        }

        class RecordViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.btnSound)
            ImageButton btnSound;
            @BindView(R.id.tvFileName)
            TextView tvFileName;
            @BindView(R.id.tvDate)
            TextView tvDate;
            @BindView(R.id.tvLength)
            TextView tvLength;
            @BindView(R.id.container)
            View container;

            RecordViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            public void bind(final File file) {
                tvFileName.setText(file.getName());
                tvDate.setText(sdf.format(file.lastModified()));
                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                mmr.setDataSource(file.getAbsolutePath());
                String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                Long second = Long.parseLong(durationStr) / 1000L;
                int secondPart = (int) (second % 60);
                if (secondPart < 1)
                    secondPart = 1;
                if (secondPart < 10)
                    tvLength.setText(second / 60 + ":0" + secondPart);
                else
                    tvLength.setText(second / 60 + ":" + secondPart);
                btnSound.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MediaPlayer mp = new MediaPlayer();

                        try {
                            mp.setDataSource(file.getAbsolutePath());
                            mp.prepare();
                            mp.start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                container.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (file.delete()) {
                            int position = files.indexOf(file);
                            files.remove(position);
                            notifyItemRemoved(position);
                            processNotifyNoRecord();
                        } else {
                            Toast.makeText(getActivity(), "Unable to delete this record!", Toast.LENGTH_LONG).show();
                        }
                        return false;
                    }
                });
            }
        }
    }
}
