package ru.innopolis.rinatgumarov.telegramtestingplatform;


import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;
import ru.innopolis.rinatgumarov.telegramtestingplatform.controller.TestingPlatformBot;

/**
 * Created by Rinat on 12.07.17.
 */
public class Main {
    public static void main(String[] args) {

        ApiContextInitializer.init();
        BotLogger.info(Main.class.getName(), "initialized");
        TelegramBotsApi botsApi = new TelegramBotsApi();
        BotLogger.info(Main.class.getName(), "generated");
        try {
            botsApi.registerBot(new TestingPlatformBot());
            BotLogger.info(Main.class.getName(), "registered");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}