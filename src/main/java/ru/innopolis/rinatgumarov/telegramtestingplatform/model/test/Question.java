package ru.innopolis.rinatgumarov.telegramtestingplatform.model.test;

import ru.innopolis.rinatgumarov.telegramtestingplatform.model.testgivening.TestGiver;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Rinat on 12.07.17.
 */
public class Question{

    private int id;
    private String question;
    private String photo;
    private List<Answer> answers;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Question() {
        this.answers = new LinkedList<>();
    }

    public void addAnswer(Answer answer){
        answers.add(answer);
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "Question=\"" + question + "\n; answers=" + answers.toString();
    }

}
