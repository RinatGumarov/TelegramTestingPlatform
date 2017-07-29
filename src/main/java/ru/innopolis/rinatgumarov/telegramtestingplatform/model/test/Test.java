package ru.innopolis.rinatgumarov.telegramtestingplatform.model.test;

import ru.innopolis.rinatgumarov.telegramtestingplatform.model.testgivening.TestGiver;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Rinat on 12.07.17.
 */
public class Test implements Iterable<Question>{

    private final String TOKEN;

    private String name;
    private int user_id;
    List<Question> questions;

    public void addQuestion(Question question){
        questions.add(question);
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Test(String TOKEN) {
        this.TOKEN = TOKEN;
        questions = new LinkedList<>();
    }

    @Override
    public String toString() {
        return "name=\"" + name + "\"; questions=" + questions.toString();
    }

    @Override
    public Iterator<Question> iterator() {
        return new TestGiver(this);
    }
}
