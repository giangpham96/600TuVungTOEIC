package one.marshangeriksen.loloaldrin.myapplication.test;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseIntArray;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import one.marshangeriksen.loloaldrin.myapplication.HighScoreActivity;
import one.marshangeriksen.loloaldrin.myapplication.R;
import one.marshangeriksen.loloaldrin.myapplication.objectModels.Question;

import static one.marshangeriksen.loloaldrin.myapplication.Constant.TOPICS;

public class TestActivity extends AppCompatActivity {

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
    TestModel model;
    List<Question> questions;
    SparseIntArray answers;
    int currentPosition = 0;

    @Override
    protected void onDestroy() {
        model.close();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        ArrayList<Integer> topicIds = getIntent().getIntegerArrayListExtra(TOPICS);
        model = new TestModel(this);
        questions = model.getQuestionList(topicIds);
        answers = new SparseIntArray();
        for (int i = 0; i < questions.size(); i++) {
            answers.put(i, 4);
        }
        radioGroupAnswer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.rb1:
                        answers.put(currentPosition, 0);
                        break;
                    case R.id.rb2:
                        answers.put(currentPosition, 1);
                        break;
                    case R.id.rb3:
                        answers.put(currentPosition, 2);
                        break;
                    case R.id.rb4:
                        answers.put(currentPosition, 3);
                        break;
                }
            }
        });
        initQuestion();
    }

    @OnClick(R.id.btnNext)
    void btnNextClick() {
        if (currentPosition != questions.size() - 1)
            currentPosition += 1;
        else
            currentPosition = 0;
        initQuestion();
    }

    @OnClick(R.id.btnPrev)
    void btnPrevClick() {
        if (currentPosition != 0)
            currentPosition -= 1;
        else
            currentPosition = questions.size() - 1;
        initQuestion();
    }

    @OnClick(R.id.btnSubmit)
    void btnSubmitClick() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_confirm_submit);
        dialog.setCancelable(false);
        Button btn_yes = (Button) dialog.findViewById(R.id.btn_yes);
        Button btn_no = (Button) dialog.findViewById(R.id.btn_no);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int noOfCorrect = getNumberOfRightAnswer();
                if (model.isHighScore(noOfCorrect * 100 / questions.size())) {
                    openTypeNameDialog(noOfCorrect);
                } else
                    openScoreDialog(noOfCorrect);
                dialog.dismiss();
            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void openScoreDialog(int noOfCorrect) {
        final int score = noOfCorrect * 100 / questions.size();
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_type_name);
        dialog.setCancelable(false);
        dialog.findViewById(R.id.btnCancel).setVisibility(View.GONE);
        dialog.findViewById(R.id.til_dialog).setVisibility(View.GONE);
        dialog.findViewById(R.id.tvTitle).setVisibility(View.GONE);
        TextView tvCorrect = (TextView) dialog.findViewById(R.id.tvCorrectAnswer);
        tvCorrect.setText(noOfCorrect + "/" + questions.size());
        TextView tvPoint = (TextView) dialog.findViewById(R.id.tvPoint);
        tvPoint.setText(score + "");
        final Button btnClose = (Button) dialog.findViewById(R.id.btnConfirm);
        btnClose.setText(R.string.close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestActivity.this, HighScoreActivity.class);
                startActivity(intent);
                dialog.dismiss();
                finish();
            }
        });
        dialog.show();
    }

    private void openTypeNameDialog(int noOfCorrect) {
        final int newHighScore = noOfCorrect * 100 / questions.size();
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_type_name);
        dialog.setCancelable(false);
        final Button btn_yes = (Button) dialog.findViewById(R.id.btnConfirm);
        btn_yes.setEnabled(false);
        btn_yes.setAlpha(0.5f);
        Button btn_no = (Button) dialog.findViewById(R.id.btnCancel);
        final EditText et_name = (EditText) dialog.findViewById(R.id.etName);
        TextView tvCorrect = (TextView) dialog.findViewById(R.id.tvCorrectAnswer);
        tvCorrect.setText(noOfCorrect + "/" + questions.size());
        TextView tvPoint = (TextView) dialog.findViewById(R.id.tvPoint);
        tvPoint.setText(newHighScore + "");
        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_name.getText().toString().length() == 0) {
                    btn_yes.setEnabled(false);
                    btn_yes.setAlpha(0.5f);
                } else {
                    btn_yes.setEnabled(true);
                    btn_yes.setAlpha(1f);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.updateHighScore(et_name.getText().toString(), newHighScore);
                dialog.dismiss();
                Intent intent = new Intent(TestActivity.this, HighScoreActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.show();
    }

    private int getNumberOfRightAnswer() {
        int counter = 0;
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            for (int j = 0; j < question.getAnswer().size(); j++) {
                String answer = question.getAnswer().get(j);
                if (answer.equals(question.getWord().getVocabulary())
                        || answer.equals(question.getWord().getTranslation())
                        || answer.equals(question.getWord().getExplanation())) {
                    if (answers.get(i) == j)
                        counter++;
                }
            }
        }
        return counter;
    }

    private void initQuestion() {
        Question question = questions.get(currentPosition);
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
        int saveAnswer = answers.get(currentPosition);
        switch (saveAnswer) {
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
