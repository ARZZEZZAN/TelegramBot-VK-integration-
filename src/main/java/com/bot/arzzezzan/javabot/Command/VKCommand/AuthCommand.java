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

import static com.bot.arzzezzan.javabot.Command.VKCommand.CommandManagerName.FRIEND;


@Component
public class AuthCommand implements Command {
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
        return "Для регистрации перейдите по ссылке: <a href=\"https://oauth.vk.com/authorize?client_id=" + CLIENT_ID + "&display=page&redirect_uri=" + REDIRECT_URI +
                "&scope=likes,newsfeed,groups,friends,wall,messages&response_type=token&v=" + VK_API_VERSION + "\">ссылка на авторизацию</a>";
    }

    public void handleAuthorizationResponse() {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(update.getMessage().getChatId()));
        message.setText("Регистрация прошла успешно, можете управлять своим аккаунтом!");

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        InlineKeyboardButton friendButton = new InlineKeyboardButton();
        InlineKeyboardButton messageButton = new InlineKeyboardButton();

        friendButton.setText("Друзья");
        friendButton.setCallbackData(FRIEND.getCommandName());
        messageButton.setText("Сообщения");
        messageButton.setCallbackData(FRIEND.getCommandName());

        rowInLine.add(friendButton);
        rowInLine.add(messageButton);
        rowsInLine.add(rowInLine);

        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);

        sendBotMessageService.sendMessageMarkup(message);
    }
}