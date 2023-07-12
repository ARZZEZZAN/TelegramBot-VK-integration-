package com.bot.arzzezzan.javabot.Command;

import com.bot.arzzezzan.javabot.Service.SendBotMessageService;
import com.bot.arzzezzan.javabot.Service.TelegramUserService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StopCommand implements Command  {
    private SendBotMessageService sendBotMessageService;
    private TelegramUserService telegramUserService;
    public static final String STOP_MESSAGE = "Have deactivated your account";
    public StopCommand(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public void execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        telegramUserService.findByChatId(chatId).ifPresent(
                telegramUser -> {
                    telegramUser.setActive(false);
                    telegramUserService.save(telegramUser);
                }
        );
        sendBotMessageService.sendMessage(chatId, STOP_MESSAGE);
    }
}
