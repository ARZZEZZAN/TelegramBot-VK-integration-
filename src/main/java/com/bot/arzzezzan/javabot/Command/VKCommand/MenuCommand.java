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
import static com.bot.arzzezzan.javabot.Command.VKCommand.CommandManagerName.NEWS;


@Component
public class MenuCommand implements Command {
    private final SendBotMessageService sendBotMessageService;
    private final TelegramUserService telegramUserService;
    private Update update;

    public MenuCommand(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public void execute(Update update) {
        this.update = update;
        handleAuthorizationResponse();
    }


    public void handleAuthorizationResponse() {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(update.getMessage().getChatId()));
        message.setText("Управляйте своим аккаунтом!");

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        InlineKeyboardButton friendButton = new InlineKeyboardButton();
        InlineKeyboardButton newsButton = new InlineKeyboardButton();

        friendButton.setText("Друзья");
        friendButton.setCallbackData(FRIEND.getCommandName());
        newsButton.setText("Новости");
        newsButton.setCallbackData(NEWS.getCommandName());

        rowInLine.add(friendButton);
        rowInLine.add(newsButton);
        rowsInLine.add(rowInLine);

        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);

        sendBotMessageService.sendMessageMarkup(message);
    }
}