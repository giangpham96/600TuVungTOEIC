package one.marshangeriksen.loloaldrin.myapplication.main.test;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import one.marshangeriksen.loloaldrin.myapplication.R;
import one.marshangeriksen.loloaldrin.myapplication.SquareImageView;
import one.marshangeriksen.loloaldrin.myapplication.main.TopicModel;
import one.marshangeriksen.loloaldrin.myapplication.objectModels.Topic;
import one.marshangeriksen.loloaldrin.myapplication.test.TestActivity;

import static one.marshangeriksen.loloaldrin.myapplication.Constant.TOPICS;

public class TestFragment extends Fragment {


    @BindView(R.id.rcvTopic)
    RecyclerView rcvTopic;
    @BindView(R.id.btnStart)
    Button btnStart;
    @BindView(R.id.btnSelectAll)
    Button btnSelectAll;
    TopicAdapter adapter;
    List<Topic> topics;
    boolean isSelectAll = false;
    private View view;
    public TestFragment() {
        // Required empty public constructor
    }


    public static TestFragment newInstance() {
        return new TestFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_test, container, false);
        ButterKnife.bind(this, view);
        topics = TopicModel.getTopicList();
        adapter = new TopicAdapter(topics);
        rcvTopic.setAdapter(adapter);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Integer> topics = adapter.getCheckedItems();
                if (topics.size() == 0)
                    Toast.makeText(getActivity(), R.string.choose_one, Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent(getActivity(), TestActivity.class);
                    intent.putIntegerArrayListExtra(TOPICS, topics);
                    startActivity(intent);
                }
            }
        });
        displaySelectAllButtonText();
        btnSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelectAll)
                    adapter.deselectAll();
                else
                    adapter.selectAll();
                isSelectAll = !isSelectAll;
                displaySelectAllButtonText();
            }
        });
        return view;
    }

    private void displaySelectAllButtonText() {
        if (isSelectAll)
            btnSelectAll.setText(R.string.deselect_all);
        else btnSelectAll.setText(R.string.select_all);
    }

    class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {

        SparseBooleanArray mSparseBooleanArray;
        CompoundButton.OnCheckedChangeListener mCheckedChangeListener
                = new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);
            }
        };
        private List<Topic> topics;

        TopicAdapter(List<Topic> topics) {
            this.topics = topics;
            mSparseBooleanArray = new SparseBooleanArray();
        }

        @Override
        public TopicAdapter.TopicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_topic_selection, parent, false);
            return new TopicViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final TopicAdapter.TopicViewHolder holder, int position) {
            Topic topic = topics.get(position);
            holder.bind(topic);
        }

        @Override
        public int getItemCount() {
            return topics.size();
        }

        ArrayList<Integer> getCheckedItems() {
            ArrayList<Integer> mTempArry = new ArrayList<>();
            for (int i = 0; i < topics.size(); i++) {
                if (mSparseBooleanArray.get(i)) {
                    mTempArry.add(i + 1);
                }
            }
            return mTempArry;
        }

        void deselectAll() {
            for (int i = 0; i < topics.size(); i++) {
                mSparseBooleanArray.put(i, false);
            }
            notifyItemRangeChanged(0, topics.size());
        }

        void selectAll() {
            for (int i = 0; i < topics.size(); i++) {
                mSparseBooleanArray.put(i, true);
            }
            notifyItemRangeChanged(0, topics.size());
        }

        class TopicViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tvTopic)
            public TextView tvTopic;
            @BindView(R.id.container)
            public View container;
            @BindView(R.id.imgThumbnail)
            SquareImageView imgThumbnail;
            @BindView(R.id.checkbox)
            CheckBox checkBox;

            TopicViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            public void bind(final Topic topic) {
                tvTopic.setText(topic.getName());
                Glide.with(TestFragment.this)
                        .load(R.drawable.img_topic)
                        .into(imgThumbnail);
                container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkBox.setChecked(!checkBox.isChecked());
                        int position = topics.indexOf(topic);
                    }
                });
                checkBox.setTag(topics.indexOf(topic));
                checkBox.setChecked(mSparseBooleanArray.get(topics.indexOf(topic)));
                checkBox.setOnCheckedChangeListener(mCheckedChangeListener);
            }
        }
    }
}
