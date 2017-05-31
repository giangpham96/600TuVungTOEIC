package one.marshangeriksen.loloaldrin.myapplication.Dictionary;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import one.marshangeriksen.loloaldrin.myapplication.Models.Word;
import one.marshangeriksen.loloaldrin.myapplication.R;
import one.marshangeriksen.loloaldrin.myapplication.WordAdapter;


public class DictionaryFragment extends Fragment {

    private View view;
    @BindView(R.id.rcvAll)
    public RecyclerView rcvAll;
    private List<Word> words;
    private WordAdapter adapter;
    private DictionaryController controller;
    public DictionaryFragment() {
        // Required empty public constructor
    }

    public static DictionaryFragment newInstance() {
        return new DictionaryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_all_word, container, false);
        ButterKnife.bind(this,view);
        controller = new DictionaryController(getActivity());
        words = new ArrayList<>();
        adapter = new WordAdapter(getActivity(),words,controller);
        rcvAll.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        words.clear();
        words.addAll(controller.getAllWords());
        adapter.notifyDataSetChanged();
    }
}
