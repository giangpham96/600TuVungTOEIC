package one.marshangeriksen.loloaldrin.myapplication.Test;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import one.marshangeriksen.loloaldrin.myapplication.R;

public class TestFragment extends Fragment {


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
        return view;
    }

}
