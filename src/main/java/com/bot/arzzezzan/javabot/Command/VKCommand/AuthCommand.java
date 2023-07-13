package com.bot.arzzezzan.javabot.Command.VKCommand;


import com.bot.arzzezzan.javabot.Command.Command;
import com.bot.arzzezzan.javabot.Service.SendBotMessageService;
import com.bot.arzzezzan.javabot.Service.TelegramUserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthCommand implements Command {
    public final static String FRIEND_BUTTON = "Friend";
    public final static String CLIENT_ID = "51700494";
    private final static String USER_ID = "429866479";
    private final static String REDIRECT_URI = "https://oauth.vk.com/blank.html";
    private final static String VK_API_VERSION = "5.131";
    private final SendBotMessageService sendBotMessageService;
    private final TelegramUserService telegramUserService;
    private Update update;
    private String chatId;

    public AuthCommand(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public void execute(Update update) {
        this.update = update;
        chatId = update.getMessage().getChatId().toString();
        sendBotMessageService.sendMessage(chatId, getAuthorizationUrl());
        handleAuthorizationResponse();
    }

    private String getAuthorizationUrl() {
        return "Для регистрации перейдите по ссылке: <a>https://oauth.vk.com/authorize?client_id=" + CLIENT_ID +
                "&display=page&redirect_uri=" + REDIRECT_URI +
                "&scope=offline,friends&response_type=token&v=" + VK_API_VERSION +
                "</a>";
    }

    public void handleAuthorizationResponse() {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(update.getMessage().getChatId()));
        message.setText("Регистрация прошла успешно, можете управлять своим аккаунтом!");

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        InlineKeyboardButton friendButton = new InlineKeyboardButton();

        friendButton.setText("Friends");
        friendButton.setCallbackData(FRIEND_BUTTON);

        rowInLine.add(friendButton);
        rowsInLine.add(rowInLine);

        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);

        sendBotMessageService.sendMessageMarkup(message);
    }
}