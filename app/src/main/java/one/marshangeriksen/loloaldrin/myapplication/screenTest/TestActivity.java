package one.marshangeriksen.loloaldrin.myapplication.screenTest;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseIntArray;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import one.marshangeriksen.loloaldrin.myapplication.R;
import one.marshangeriksen.loloaldrin.myapplication.adapters.ViewPagerAdapter;
import one.marshangeriksen.loloaldrin.myapplication.objectModels.Question;
import one.marshangeriksen.loloaldrin.myapplication.screenHighscore.HighScoreActivity;

import static one.marshangeriksen.loloaldrin.myapplication.Constant.TOPICS;

public class TestActivity extends AppCompatActivity implements OnSelectAnswer {

    @BindView(R.id.tabLayoutTest)
    public TabLayout tabLayout;
    @BindView(R.id.viewpagerTest)
    public ViewPager viewPager;
    @BindView(R.id.toolbarTest)
    public Toolbar toolbar;
    TestDatabaseModel model;
    List<Question> questions;
    SparseIntArray answers;

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
        model = new TestDatabaseModel(this);
        questions = model.getQuestionList(topicIds);
        answers = new SparseIntArray();
        for (int i = 0; i < questions.size(); i++) {
            answers.put(i, 4);
        }
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setSupportActionBar(toolbar);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        for (int i = 0; i < questions.size(); i++)
            adapter.addFrag(QuestionFragment.newInstance(i, questions.get(i)),
                    getString(R.string.question) + " " + (i + 1));
        viewPager.setAdapter(adapter);
    }

    @OnClick(R.id.btnNext)
    void btnNextClick() {
        if (viewPager.getCurrentItem() < questions.size())
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
    }

    @OnClick(R.id.btnPrev)
    void btnPrevClick() {
        if (viewPager.getCurrentItem() > 0)
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
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


    @Override
    public void onAnswerChange(int questionPosition, int answerPosition) {
        answers.put(questionPosition, answerPosition);
    }

    @Override
    public int getSaveAnswer(int questionPosition) {
        return answers.get(questionPosition);
    }

    @Override
    public void onBackPressed() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_confirm_submit);
        dialog.setCancelable(false);

        ((TextView) dialog.findViewById(R.id.tv_title)).setText(R.string.quit);
        ((TextView) dialog.findViewById(R.id.tv_mess)).setText(R.string.confirm_quit_test);
        dialog.findViewById(R.id.btn_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.btn_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
