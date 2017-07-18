package ru.innopolis.rinatgumarov.telegramtestingplatform.db;

import org.telegram.telegrambots.logging.BotLogger;
import ru.innopolis.rinatgumarov.telegramtestingplatform.model.test.Answer;
import ru.innopolis.rinatgumarov.telegramtestingplatform.model.test.Question;
import ru.innopolis.rinatgumarov.telegramtestingplatform.model.test.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Rinat on 17/07/2017.
 */
public class TestFetcher {
    public static synchronized Test fetchTest(String testToken) {
        synchronized (Database.getINSTANCE()) {
            Test t = new Test(testToken);
            ResultSet rs = Database.getINSTANCE().fetch("select user_id, name from tests where token = '" + testToken + "';");
            try {
                rs.next();
                t.setName(rs.getString("name"));
                t.setUser_id(rs.getInt("user_id"));
                rs = Database.getINSTANCE()
                        .fetch("select q.id, q.question, a.answer, a.istrue" +
                                " from questions q, answers a where q.test_token = '"
                                + testToken + "' and a.test_token = '"
                                + testToken + "' and a.id_question = q.id order by q.id;");
                rs.next();
                boolean b = true;
                while (b) {
                    Question q = new Question();
                    q.setQuestion(rs.getString("question"));
                    Answer answer = new Answer();
                    answer.setAnswer(rs.getString("answer"));
                    answer.setTrue(rs.getInt("istrue") == 1);
                    q.addAnswer(answer);
                    while ((b = rs.next()) && rs.getInt(1) == (t.getQuestions().size())) {
                        answer = new Answer();
                        answer.setAnswer(rs.getString("answer"));
                        answer.setTrue(rs.getInt("istrue") == 1);
                        q.addAnswer(answer);
                    }
                    t.addQuestion(q);
                }

            } catch (SQLException e) {
                BotLogger.error(TestFetcher.class.getName(), e);
            }
            return t;
        }
    }
}
