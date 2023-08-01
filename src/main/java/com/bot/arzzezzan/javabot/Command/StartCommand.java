package com.bot.arzzezzan.javabot.Command;

import com.bot.arzzezzan.javabot.Repository.Entity.TelegramUser;
import com.bot.arzzezzan.javabot.Service.SendBotMessageService;
import com.bot.arzzezzan.javabot.Service.TelegramUserService;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class StartCommand implements Command {
    private SendBotMessageService sendBotMessageService;
    private TelegramUserService telegramUserService;
    public static final String START_MESSAGE = "Hello! I'm Telergam bot that will help you to be aware of" +
            " recent news in VK, please use command '/auth' for registration";
    public StartCommand(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public void execute(Update update) throws IOException {
        String chatId = update.getMessage().getChatId().toString();

        telegramUserService.findByChatId(chatId).ifPresentOrElse(
                telegramUser -> {
                    telegramUser.setActive(true);
                    telegramUserService.save(telegramUser);
                },
                () -> {
                    TelegramUser telegramUser = new TelegramUser();
                    telegramUser.setChatId(chatId);
                    telegramUser.setActive(true);
                    telegramUserService.save(telegramUser);
                }
        );

        sendBotMessageService.sendMessage(chatId, START_MESSAGE);
    }
}
