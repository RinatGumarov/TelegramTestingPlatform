package ru.innopolis.rinatgumarov.telegramtestingplatform.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.api.methods.GetFile;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.PhotoSize;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;
import ru.innopolis.rinatgumarov.telegramtestingplatform.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static ru.innopolis.rinatgumarov.telegramtestingplatform.model.commands.Commands.COMMAND_CREATE;
import static ru.innopolis.rinatgumarov.telegramtestingplatform.model.commands.Commands.COMMAND_TAKE;


/**
 * Created by Rinat on 12.07.17.
 */

public class TestingPlatformBot extends TelegramLongPollingBot {

    private final String TOKEN;
    private Map<Integer, JSONObject> stateMap = new HashMap<>();

    public TestingPlatformBot() {
        TOKEN = "";
    }

    public void onUpdateReceived(Update update) {

        BotLogger.info(this.getClass().getName(), update.getMessage().toString());
        System.out.println(update.getMessage().getDate());
        try {
            if (update.hasMessage()) {
                if (update.getMessage().getText().equals("/gimmetits") ||
                        update.getMessage().hasPhoto() && update.getMessage().getCaption().equals("tits")) {
                    Message message = update.getMessage();
                    if (message.hasPhoto()) {
                        savePhoto(update);
                    }
                    if (message.hasText()) {
                        generateAnswer(message);
                    }
                } else handleMessage(update);
            }
        } catch (Exception e) {
            BotLogger.error("smth happened", e);
        }
    }

    private void handleMessage(Update update) {
        if (!stateMap.containsKey(update.getMessage().getFrom().getId())) {
            stateMap.put(update.getMessage().getFrom().getId(), new JSONObject().put("state", BotState.INITIAL_STATE));
        }
        switch ((BotState) stateMap.get(update.getMessage().getFrom().getId()).get("state")) {
            case INITIAL_STATE:
                handleInitial(update.getMessage());
                break;
            case START_STATE:
                handleStart(update);
                break;
            case CREATION_START:
                handleTestCreation(update);
                break;
            case CREATION_END:
                handleCreationEnd(update);
                break;
            case ADDING_QUESTION:
                handleAddingQuestion(update);
                break;
            case ADDING_ANSWER:
                handleAddingAnswer(update);
                break;
            case FINISH:
                handleFinish(update);
        }
    }

    private void handleFinish(Update update) {
        if (update.getMessage().hasText()){
            switch (update.getMessage().getText()){
                case "/submit":

            }
        }
    }

    private void handleAddingAnswer(Update update) {
        if (update.getMessage().hasText()){
            switch (update.getMessage().getText()){
                default:
                    if (update.getMessage().getText().trim().startsWith("/true")) {

                        ((JSONObject) ((JSONArray) getTestJSONObject(update).get("questions"))
                                .get(((JSONArray) getTestJSONObject(update).get("questions")).length() - 1))
                                .append("answers", new JSONObject().put("istrue", true).put("answer", update.getMessage().getText()
                                        .substring(update.getMessage().getText().indexOf('e'))));
                        try {
                            sendMessage(new SendMessage()
                            .setChatId(update.getMessage().getChatId())
                            .setText("/addanswer - to add answer for last question" +
                                    "\n/addquestion - to add new question" +
                                    "\n/submit - to submit test"));
                        } catch (TelegramApiException e) {
                            error(e);
                        }
                    }
                    else if (update.getMessage().getText().trim().startsWith("/false")){
                        ((JSONObject) ((JSONArray) getTestJSONObject(update).get("questions"))
                                .get(((JSONArray) getTestJSONObject(update).get("questions")).length() - 1))
                                .append("answers", new JSONObject().put("istrue", false).put("answer", update.getMessage().getText()
                                        .substring(update.getMessage().getText().indexOf('e'))));
                    }
                    else
                        try {
                            sendMessage(new SendMessage()
                            .setChatId(update.getMessage().getChatId())
                            .setText("This is incorrect answer" +
                                    "\nSee the example and try again"));
                        } catch (TelegramApiException e) {
                            error(e);
                        }
            }
        }
    }

    private void handleAddingQuestion(Update update) {
        if (update.getMessage().hasText()){
            switch (update.getMessage().getText()){
                default:
                    ((JSONObject)((JSONArray)getTestJSONObject(update).get("questions"))
                            .get(((JSONArray) getTestJSONObject(update).get("questions")).length()-1))
                            .put("text", update.getMessage().getText());
                    getJSONObject(update).put("state", BotState.ADDING_ANSWER);
                    try {
                        sendMessage(new SendMessage()
                                .setChatId(update.getMessage().getChatId())
                                .setText("send me answer to the question\nIf it is a true answer start it with /true\n" +
                                        "otherwise start with /false." +
                                        "\nExample:" +
                                        "\n\"/true Putin\"" +
                                        "\nThat means that answer \"Putin\" is correct answer"));
                    } catch (TelegramApiException e) {
                        error(e);
                    }
            }
        }
    }

    private void handleCreationEnd(Update update) {
        if (!getTestJSONObject(update).has("questions")) {
            if (update.getMessage().getText().equals("/add")){
                try {
                    sendMessage(new SendMessage()
                    .setChatId(update.getMessage().getChatId())
                    .setText("send me your question"));
                    getJSONObject(update).put("state", BotState.ADDING_QUESTION);
                    getTestJSONObject(update).append("questions", new JSONObject().put("text", update.getMessage().getText()));
                } catch (TelegramApiException e) {
                    BotLogger.error(this.getClass().getName(), e.getMessage());
                }
            }
        }
    }

    private void handleStart(Update update) {
        if (update.getMessage().getText().equals(COMMAND_CREATE)) {
            getJSONObject(update).put("state", BotState.CREATION_START);
            getJSONObject(update).put("test", new JSONObject().put("token", Utils.getToken()));

            getJSONObject(update).put("lastmodtest", getTestJSONObject(update).get("token"));

            SendMessage msg = new SendMessage()
                    .setChatId(update.getMessage().getChatId())
                    .setText("Send me name of new test");
            try {
                sendMessage(msg);
            } catch (TelegramApiException e) {
                BotLogger.error(this.getClass().getName(), e.getMessage());
            }
        }
    }

    private void handleTestCreation(Update update) {
        switch (update.getMessage().getText()) {
            default:
                try {
                    sendMessage(new SendMessage()
                            .setChatId(update.getMessage().getChatId())
                            .setText("The name of your test is " + update.getMessage().getText() + "" +
                                    "\nType /add to add question"));
                    getTestJSONObject(update).put("name", update.getMessage().getText());
                    getJSONObject(update).put("state", BotState.CREATION_END);
                } catch (TelegramApiException e) {
                    BotLogger.error(this.getClass().getName(), e.getMessage());
                }
        }
    }

    private void handleInitial(Message message) {
        SendMessage msg = new SendMessage()
                .setChatId(message.getChatId())
                .setText("Do you want to " + COMMAND_CREATE + " new test or to " + COMMAND_TAKE + " the test?");
        getJSONObject(message).put("state", BotState.START_STATE);
        try {
            sendMessage(msg);
        } catch (TelegramApiException e) {
            BotLogger.error(this.getClass().getName(), e.getMessage());
        }
    }

    private void handleDivision(Update update) {

    }

    private void generateAnswer(Message message) {
        // Create send method
        SendPhoto sendPhotoRequest = new SendPhoto();
        // Set destination chat id
        sendPhotoRequest.setChatId(message.getChatId());
        // Set the photo file as a new photo (You can also use InputStream with a method overload)
        File d = new File("/Users/Rinat/work/girls/");
        List<File> files = Arrays.stream(d.listFiles())
                .filter(file -> file.getName().endsWith(".jpg"))
                .collect(Collectors.toList());
        BotLogger.info(this.getClass().getName(), Integer.toString(files.size()));
        sendPhotoRequest.setCaption("ololo").setNewPhoto(files.get(ThreadLocalRandom.current().nextInt(files.size())));
        try {
            // Execute the method
            sendPhoto(sendPhotoRequest);
        } catch (TelegramApiException e) {
            BotLogger.error(this.getClass().getName(), e.getMessage());
        }
    }

    public PhotoSize getPhoto(Update update) {
        // Check that the update contains a message and the message has a photo
        if (update.hasMessage() && update.getMessage().hasPhoto()) {
            // When receiving a photo, you usually get different sizes of it
            List<PhotoSize> photos = update.getMessage().getPhoto();

            // We fetch the bigger photo
            return photos.stream()
                    .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                    .findFirst()
                    .orElse(null);
        }

        // Return null if not found
        return null;
    }


    public String getFilePath(PhotoSize photo) {
        Objects.requireNonNull(photo);

        if (photo.hasFilePath()) { // If the file_path is already present, we are done!
            return photo.getFilePath();
        } else { // If not, let find it
            // We create a GetFile method and set the file_id from the photo
            GetFile getFileMethod = new GetFile();
            getFileMethod.setFileId(photo.getFileId());
            try {
                // We execute the method using AbsSender::getFile method.
                org.telegram.telegrambots.api.objects.File file = getFile(getFileMethod);
                // We now have the file_path
                return file.getFilePath();
            } catch (TelegramApiException e) {
                BotLogger.error(this.getClass().getName(), e.getMessage());
            }
        }

        return null; // Just in case
    }

    public File downloadPhotoByFilePath(String filePath) {
        try {
            // Download the file calling AbsSender::downloadFile method
            return downloadFile(filePath);
        } catch (TelegramApiException e) {
            BotLogger.error(this.getClass().getName(), e.getMessage());
        }

        return null;
    }

    public void savePhoto(Update update) {
        System.out.println();
        File file = downloadPhotoByFilePath(getFilePath(getPhoto(update)));
        System.out.println(file.getPath());
        try {
            Files.move(Paths.get(file.getAbsolutePath()), Paths.get("bar.jpg"));
        } catch (IOException e) {
            System.out.println("couldn't move file " + file.getName() + ": " + e.getMessage());
        }
        if (file.delete())
            System.out.println("deleted");
    }

    public String getBotUsername() {
        return "TestingPlatformBot";
    }

    public String getBotToken() {
        return TOKEN;
    }

    private JSONObject getTestJSONObject(Update update) {
        return ((JSONObject) stateMap.get(update.getMessage().getFrom().getId()).get("test"));
    }

    private void error(Exception e){
        BotLogger.error(this.getClass().getName(), e.getMessage());
    }

    private void info(Exception e){
        BotLogger.info(this.getClass().getName(), e.getMessage());
    }

    private JSONObject getJSONObject(Update update) {
        return stateMap.get(update.getMessage().getFrom().getId());
    }

    private JSONObject getJSONObject(Message message) {
        return stateMap.get(message.getFrom().getId());
    }
}
