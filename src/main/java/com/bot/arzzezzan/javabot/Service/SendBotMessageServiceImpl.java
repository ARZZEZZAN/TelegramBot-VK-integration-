package com.bot.arzzezzan.javabot.Service;

import com.bot.arzzezzan.javabot.Command.VKCommand.Bot.TelegramBot;
import com.vk.api.sdk.objects.docs.Doc;
import com.vk.api.sdk.objects.video.VideoFull;
import com.vk.api.sdk.objects.video.responses.GetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.bot.arzzezzan.javabot.Command.VKCommand.Command.News.NewsManagerName.*;

@Service
public class SendBotMessageServiceImpl implements SendBotMessageService {
    private TelegramBot telegramBot;
    @Autowired
    public SendBotMessageServiceImpl(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }
    @Override
    public void sendMessage(String chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableHtml(true);
        sendMessage.setText(message);

        try {
            telegramBot.execute(sendMessage);
        } catch(TelegramApiException telegramApiException) {
            telegramApiException.printStackTrace();
        }
    }

    @Override
    public void sendMessage(EditMessageText editMessageText) {
        try {
            telegramBot.execute(editMessageText);
        } catch(TelegramApiException telegramApiException) {
            telegramApiException.printStackTrace();
        }
    }

    @Override
    public void sendMessagePost(String chatId, String message) {
        InlineKeyboardMarkup markupInLine = postControl();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableHtml(true);
        sendMessage.setText(message);
        sendMessage.setReplyMarkup(markupInLine);

        try {
            telegramBot.execute(sendMessage);
        } catch(TelegramApiException telegramApiException) {
            telegramApiException.printStackTrace();
        }
    }
    @Override
    public void sendMessageMarkup(EditMessageReplyMarkup sendMessage) {
        try {
            telegramBot.execute(sendMessage);
        } catch(TelegramApiException telegramApiException) {
            telegramApiException.printStackTrace();
        }
    }
    @Override
    public void sendMessageMarkup(SendMessage sendMessage) {
        try {
            telegramBot.execute(sendMessage);
        } catch(TelegramApiException telegramApiException) {
            telegramApiException.printStackTrace();
        }
    }

    @Override
    public void sendPhoto(String chatId, String photoUrl, String text) throws MalformedURLException {
        InlineKeyboardMarkup markupInLine = postControl();

        URL url = new URL(photoUrl);
        try(InputStream inputStream = url.openStream()) {
            byte[] photoBytes = inputStream.readAllBytes();
            InputFile file = new InputFile(new ByteArrayInputStream(photoBytes), "photo.jpg");
            SendPhoto sendPhoto = new SendPhoto(chatId, file);
            sendPhoto.setCaption(text);
            sendPhoto.setReplyMarkup(markupInLine);
            telegramBot.execute(sendPhoto);
        } catch (IOException | TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendVideo(String chatId, GetResponse response, String text) {
        InlineKeyboardMarkup markupInLine = postControl();

        VideoFull vi = response.getItems().get(0);
        if(vi.getFiles() != null) {
            try {
                SendVideo sendVideo = new SendVideo(chatId, new InputFile(new URL(vi.getFiles().getMp4720().toString()).openStream(), "video.mp4"));
                sendVideo.setCaption(text);
                sendVideo.setReplyMarkup(markupInLine);
                telegramBot.execute(sendVideo);
            } catch (IOException | TelegramApiException e) {
                e.printStackTrace();
            }
        } else {
            String videoUrl = String.valueOf(vi.getPlayer());
            sendMessagePost(chatId, text + "Ссылка на видео: " + videoUrl);
        }
    }

    @Override
    public void sendDoc(String chatId, Doc doc, String text) {
        InlineKeyboardMarkup markupInLine = postControl();

        try {
            SendDocument sendDocument = new SendDocument(chatId, new InputFile(new URL(doc.getUrl().toString()).openStream(), "doc.pdf"));
            sendDocument.setCaption(text);
            sendDocument.setReplyMarkup(markupInLine);
            telegramBot.execute(sendDocument);
        } catch(IOException | TelegramApiException e) {
            e.printStackTrace();
        }
    }


    private static InlineKeyboardMarkup postControl() {
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        InlineKeyboardButton nextButton = new InlineKeyboardButton();
        InlineKeyboardButton backButton = new InlineKeyboardButton();

        nextButton.setText("->");
        nextButton.setCallbackData(NEXT.getCommandName());
        backButton.setText("Назад");
        backButton.setCallbackData(BACK.getCommandName());

        rowInLine.add(backButton);
        rowInLine.add(nextButton);
        rowsInLine.add(rowInLine);

        markupInLine.setKeyboard(rowsInLine);
        return markupInLine;
    }

}
