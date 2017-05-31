package one.marshangeriksen.loloaldrin.myapplication.TopicList;


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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import one.marshangeriksen.loloaldrin.myapplication.MainActivity;
import one.marshangeriksen.loloaldrin.myapplication.Models.Topic;
import one.marshangeriksen.loloaldrin.myapplication.R;
import one.marshangeriksen.loloaldrin.myapplication.SquareImageView;
import one.marshangeriksen.loloaldrin.myapplication.Topic.TopicActivity;

import static one.marshangeriksen.loloaldrin.myapplication.Constant.TOPIC_BUNDLE;
import static one.marshangeriksen.loloaldrin.myapplication.Constant.TOPIC_ID_BUNDLE;


public class TopicFragment extends Fragment {

    private View view;
    @BindView(R.id.rcvTopic)
    public RecyclerView rcvTopic;
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
        adapter = new TopicAdapter(getTopicList());
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

    List<Topic> getTopicList() {
        List<Topic> topics = new ArrayList<>();
        topics.add(new Topic("1. Contracts", "1.jpg"));
        topics.add(new Topic("2. Marketing", "2.jpg"));
        topics.add(new Topic("3. Warranties", "3.jpg"));
        topics.add(new Topic("4. Business Planning", "4.jpg"));
        topics.add(new Topic("5. Conferences", "5.jpg"));
        topics.add(new Topic("6. Computers and the Internet", "6.jpg"));
        topics.add(new Topic("7. Office Technology", "7.jpg"));
        topics.add(new Topic("8. Office Procedures", "8.jpg"));
        topics.add(new Topic("9. Electronics", "9.jpg"));
        topics.add(new Topic("10. Correspondence", "10.jpg"));
        topics.add(new Topic("11. Job Ads & Recruitment", "11.jpg"));
        topics.add(new Topic("12. Apply and Interviewing", "12.jpg"));
        topics.add(new Topic("13. Hiring and Training", "13.jpg"));
        topics.add(new Topic("14. Salaries & Benefits", "14.jpg"));
        topics.add(new Topic("15. Promotions, Pensions & Awards", "15.jpg"));
        topics.add(new Topic("16. Shopping", "16.jpg"));
        topics.add(new Topic("17. Ordering Supplies", "17.jpg"));
        topics.add(new Topic("18. Shipping", "18.jpg"));
        topics.add(new Topic("19. Invoices", "19.jpg"));
        topics.add(new Topic("20. Inventory", "20.jpg"));
        topics.add(new Topic("21. Banking", "21.jpg"));
        topics.add(new Topic("22. Accounting", "22.jpg"));
        topics.add(new Topic("23. Investments", "23.jpg"));
        topics.add(new Topic("24. Taxes", "24.jpg"));
        topics.add(new Topic("25. Financial Statements", "25.jpg"));
        topics.add(new Topic("26. Property & Departments", "26.jpg"));
        topics.add(new Topic("27. Board Meeting & Committees", "27.jpg"));
        topics.add(new Topic("28. Quality Control", "28.jpg"));
        topics.add(new Topic("29. Product Development", "29.jpg"));
        topics.add(new Topic("30. Renting & Leasing", "30.jpg"));
        topics.add(new Topic("31. Selecting A Restaurant", "31.jpg"));
        topics.add(new Topic("32. Eating Out", "32.jpg"));
        topics.add(new Topic("33. Ordering Lunch", "33.jpg"));
        topics.add(new Topic("34. Cooking As A Career", "34.jpg"));
        topics.add(new Topic("35. Events", "35.jpg"));
        topics.add(new Topic("36. General Travel", "36.jpg"));
        topics.add(new Topic("37. Airlines", "37.jpg"));
        topics.add(new Topic("38. Trains", "38.jpg"));
        topics.add(new Topic("39. Hotels", "39.jpg"));
        topics.add(new Topic("40. Car Rentals", "40.jpg"));
        topics.add(new Topic("41. Movies", "41.jpg"));
        topics.add(new Topic("42. Theater", "42.jpg"));
        topics.add(new Topic("43. Music", "43.jpg"));
        topics.add(new Topic("44. Museums", "44.jpg"));
        topics.add(new Topic("45. Media", "45.jpg"));
        topics.add(new Topic("46. Doctor's Office", "46.jpg"));
        topics.add(new Topic("47. Dentist's Office", "47.jpg"));
        topics.add(new Topic("48. Health Insurance", "48.jpg"));
        topics.add(new Topic("49. Hopitals", "49.jpg"));
        topics.add(new Topic("50. Pharmacy", "50.jpg"));
        return topics;
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
            @BindView(R.id.imgTopic)
            public SquareImageView imgTopic;
            @BindView(R.id.tvTopic)
            public TextView tvTopic;
            @BindView(R.id.item)
            public View item;

            TopicViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

}
