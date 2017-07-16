package ru.innopolis.rinatgumarov.telegramtestingplatform.utils;

import org.telegram.telegrambots.api.methods.GetFile;
import org.telegram.telegrambots.api.objects.PhotoSize;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Rinat on 12.07.17.
 */
public final class Utils {

    public static final String getToken(){
        return Long.toHexString(ThreadLocalRandom.current().nextLong());
    }

}
