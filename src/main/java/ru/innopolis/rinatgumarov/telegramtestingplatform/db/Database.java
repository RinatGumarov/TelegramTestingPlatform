package ru.innopolis.rinatgumarov.telegramtestingplatform.db;

import org.telegram.telegrambots.logging.BotLogger;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Created by Rinat on 16/07/2017.
 */
public class Database {
    private static Database INSTANCE;

    private static Statement stmt;

    public static Database getINSTANCE() {
        if (INSTANCE == null)
            INSTANCE = new Database();
        return INSTANCE;
    }

    private Database() {
        try {
            Properties props = new Properties();
            FileInputStream in = new FileInputStream("src/main/resources/db.properties");
            props.load(in);
            in.close();

            String driver = props.getProperty("jdbc.driver");
            if (driver != null) {
                Class.forName(driver);
            }

            String url = props.getProperty("jdbc.url");
            String username = props.getProperty("jdbc.username");
            String password = props.getProperty("jdbc.password");

            Connection con = DriverManager.getConnection(url, username, password);
            stmt = con.createStatement();
            BotLogger.info(this.getClass().getName(), "Connection to database successful");
        } catch (SQLException | IOException | ClassNotFoundException e) {
            BotLogger.info(this.getClass().getName(), "Connection to database failed with message: " + e.getMessage());
        } catch (NullPointerException e) {
            BotLogger.info(this.getClass().getName(), "Wrong properties");
        }
    }

    public void write(String query) {
        try {
            stmt.execute(query);
        } catch (SQLException e) {
            BotLogger.info(this.getClass().getName(), "Writing to database failed: " + e.getMessage());
        }
    }
}
