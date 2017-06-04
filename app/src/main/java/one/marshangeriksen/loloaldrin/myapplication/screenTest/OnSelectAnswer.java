package one.marshangeriksen.loloaldrin.myapplication.screenTest;

interface OnSelectAnswer {
    void onAnswerChange(int questionPosition, int answerPosition);

    int getSaveAnswer(int questionPosition);
}
