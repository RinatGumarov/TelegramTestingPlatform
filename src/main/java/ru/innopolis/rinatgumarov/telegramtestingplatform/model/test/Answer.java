package ru.innopolis.rinatgumarov.telegramtestingplatform.model.test;

/**
 * Created by Rinat on 17/07/2017.
 */
public class Answer {
    private String answer;
    private boolean isTrue;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isTrue() {
        return isTrue;
    }

    public void setTrue(boolean aTrue) {
        isTrue = aTrue;
    }

    @Override
    public String toString() {
        return "(" + answer + ":" + Boolean.toString(isTrue) + ")";
    }
}
