package ru.innopolis.rinatgumarov.telegramtestingplatform.model.testgivening;

import ru.innopolis.rinatgumarov.telegramtestingplatform.model.test.Question;
import ru.innopolis.rinatgumarov.telegramtestingplatform.model.test.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Rinat on 25/07/2017.
 */
public class TestGiver implements Iterator<Question> {
    private Question[] questions;
    private int i = 0;

    public TestGiver(Test test) {
        questions = new Question[test.getQuestions().size()];
        for (int i = 0; i < questions.length; i++) {
            int j = ThreadLocalRandom.current().nextInt(i + 1);
            if (j != i)
                questions[i] = questions[j];
            questions[j] = test.getQuestions().get(i);
        }
    }

    @Override
    public boolean hasNext() {
        return i < questions.length;
    }

    @Override
    public Question next() {
        if (hasNext())
            return questions[i++];
        throw new NullPointerException("use hasNext() to prevent this moment");
    }
}
