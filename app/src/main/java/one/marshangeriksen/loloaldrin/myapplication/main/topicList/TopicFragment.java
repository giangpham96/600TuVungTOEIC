package one.marshangeriksen.loloaldrin.myapplication.main.topicList;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import one.marshangeriksen.loloaldrin.myapplication.R;
import one.marshangeriksen.loloaldrin.myapplication.SquareImageView;
import one.marshangeriksen.loloaldrin.myapplication.main.MainActivity;
import one.marshangeriksen.loloaldrin.myapplication.main.TopicModel;
import one.marshangeriksen.loloaldrin.myapplication.objectModels.Topic;
import one.marshangeriksen.loloaldrin.myapplication.topic.TopicActivity;

import static one.marshangeriksen.loloaldrin.myapplication.Constant.TOPIC_BUNDLE;
import static one.marshangeriksen.loloaldrin.myapplication.Constant.TOPIC_ID_BUNDLE;


public class TopicFragment extends Fragment {

    @BindView(R.id.rcvTopic)
    public RecyclerView rcvTopic;
    private View view;
    private TopicAdapter adapter;
    private FloatingActionButton fabFav;

    public TopicFragment() {
        // Required empty public constructor
    }

    public static TopicFragment newInstance() {
        return new TopicFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_topic, container, false);
        ButterKnife.bind(this, view);
        adapter = new TopicAdapter(TopicModel.getTopicList());
        rcvTopic.setAdapter(adapter);
        fabFav = ((MainActivity)getActivity()).fabFav;
        rcvTopic.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0)
                    fabFav.hide();
                else if (dy < 0)
                    fabFav.show();
            }
        });
        return view;
    }

    class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {

        private List<Topic> topics;

        TopicAdapter(List<Topic> topics) {
            this.topics = topics;
        }

        @Override

        public TopicAdapter.TopicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_topic, parent, false);
            return new TopicViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final TopicAdapter.TopicViewHolder holder, int position) {
            final Topic topic = topics.get(position);
            holder.tvTopic.setText(topic.getName());
            holder.tvTopic.setSelected(true);
            Glide.with(TopicFragment.this)
                    .load(R.drawable.img_topic)
                    .into(holder.imgTopic);
            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TopicFragment.this.getActivity(), TopicActivity.class);
                    intent.putExtra(TOPIC_ID_BUNDLE,holder.getAdapterPosition()+1);
                    intent.putExtra(TOPIC_BUNDLE,topic);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return topics.size();
        }

        class TopicViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tvTopic)
            public TextView tvTopic;
            @BindView(R.id.item)
            public View item;
            @BindView(R.id.imgTopic)
            SquareImageView imgTopic;

            TopicViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

}