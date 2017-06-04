package one.marshangeriksen.loloaldrin.myapplication.screenTest;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import one.marshangeriksen.loloaldrin.myapplication.R;
import one.marshangeriksen.loloaldrin.myapplication.objectModels.Question;

public class QuestionFragment extends Fragment {
    private static final String POSITION = "param1";
    private static final String QUESTION = "param2";
    @BindView(R.id.tvQuestion)
    TextView tvQuestion;
    @BindView(R.id.radioGroupAnswer)
    RadioGroup radioGroupAnswer;
    @BindView(R.id.rb1)
    RadioButton rb1;
    @BindView(R.id.rb2)
    RadioButton rb2;
    @BindView(R.id.rb3)
    RadioButton rb3;
    @BindView(R.id.rb4)
    RadioButton rb4;
    private OnSelectAnswer onSelectAnswer;
    private int questionPosition;
    private Question question;
    private View view;

    public QuestionFragment() {
        // Required empty public constructor
    }

    public static QuestionFragment newInstance(int position, Question question) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION, position);
        args.putSerializable(QUESTION, question);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSelectAnswer)
            onSelectAnswer = (OnSelectAnswer) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            questionPosition = getArguments().getInt(POSITION);
            question = (Question) getArguments().getSerializable(QUESTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_question, container, false);
        ButterKnife.bind(this, view);
        initQuestion();
        radioGroupAnswer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (onSelectAnswer != null)
                    switch (group.getCheckedRadioButtonId()) {
                        case R.id.rb1:
                            onSelectAnswer.onAnswerChange(questionPosition, 0);
                            break;
                        case R.id.rb2:
                            onSelectAnswer.onAnswerChange(questionPosition, 1);
                            break;
                        case R.id.rb3:
                            onSelectAnswer.onAnswerChange(questionPosition, 2);
                            break;
                        case R.id.rb4:
                            onSelectAnswer.onAnswerChange(questionPosition, 3);
                            break;
                    }
            }
        });
        return view;
    }

    private void initQuestion() {
        switch (question.getType()) {
            case Question.EXPLANATION_QUESTION:
            case Question.MEANING_QUESTION:
                tvQuestion.setText(question.getWord().getVocabulary());
                break;
            case Question.VOCAB_QUESTION:
                tvQuestion.setText(question.getWord().getTranslation());
                break;
        }
        rb1.setText(question.getAnswer().get(0));
        rb2.setText(question.getAnswer().get(1));
        rb3.setText(question.getAnswer().get(2));
        rb4.setText(question.getAnswer().get(3));
        if (onSelectAnswer != null)
            switch (onSelectAnswer.getSaveAnswer(questionPosition)) {
                case 4:
                    radioGroupAnswer.clearCheck();
                    break;
                case 0:
                    rb1.setChecked(true);
                    break;
                case 1:
                    rb2.setChecked(true);
                    break;
                case 2:
                    rb3.setChecked(true);
                    break;
                case 3:
                    rb4.setChecked(true);
                    break;
            }
    }
}
