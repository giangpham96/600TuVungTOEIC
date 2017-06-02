package one.marshangeriksen.loloaldrin.myapplication.test;

interface OnSelectAnswer {
    void onAnswerChange(int questionPosition, int answerPosition);

    int getSaveAnswer(int questionPosition);
}
