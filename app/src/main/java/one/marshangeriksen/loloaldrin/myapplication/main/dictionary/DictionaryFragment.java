package one.marshangeriksen.loloaldrin.myapplication.main.dictionary;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import one.marshangeriksen.loloaldrin.myapplication.R;
import one.marshangeriksen.loloaldrin.myapplication.WordAdapter;
import one.marshangeriksen.loloaldrin.myapplication.main.MainActivity;
import one.marshangeriksen.loloaldrin.myapplication.objectModels.Word;


public class DictionaryFragment extends Fragment implements SearchView.OnQueryTextListener {

    @BindView(R.id.rcvAll)
    public RecyclerView rcvAll;
    private View view;
    private List<Word> words;
    private WordAdapter adapter;
    private DictionaryModel model;
    private SearchView searchView;

    public DictionaryFragment() {
        // Required empty public constructor
    }

    public static DictionaryFragment newInstance() {
        return new DictionaryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_all_word, container, false);
        ButterKnife.bind(this,view);
        model = new DictionaryModel(getActivity());
        words = new ArrayList<>();
        adapter = new WordAdapter(getActivity(), words, model);
        rcvAll.setAdapter(adapter);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        model.close();
    }

    @Override
    public void onResume() {
        super.onResume();
        words.clear();
        words.addAll(model.getAllWords());
        adapter.setWords(words);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        searchView = ((MainActivity) getActivity()).searchView;
        if (searchView != null) {
            searchView.setQuery("", false);
            searchView.clearFocus();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        query = query.toLowerCase();

        final List<Word> filteredWordList = new ArrayList<>();
        for (Word word : words) {
            final String vocab = word.getVocabulary().toLowerCase();
            final String trans = word.getTranslation().toLowerCase();
            if (vocab.contains(query) || trans.contains(query)) {
                filteredWordList.add(word);
            }
        }
        adapter.animateTo(filteredWordList);
        rcvAll.scrollToPosition(0);
        return true;
    }
}
