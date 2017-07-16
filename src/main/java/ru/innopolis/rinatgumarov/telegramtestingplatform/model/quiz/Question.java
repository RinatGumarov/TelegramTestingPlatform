package ru.innopolis.rinatgumarov.telegramtestingplatform.model.quiz;

import java.io.File;
import java.util.List;

/**
 * Created by Rinat on 12.07.17.
 */
public class Question {

    private final String QUESTION_TOKEN;

    private String questionText;
    private File questionPhoto;
    private List<String> answers;

    public Question(String QUESTION_TOKEN) {
        this.QUESTION_TOKEN = QUESTION_TOKEN;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public void addAnswer(String answer){
        answers.add(answer);
    }
}
