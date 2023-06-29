package com.bot.arzzezzan.javabot.Command;

import com.bot.arzzezzan.javabot.Service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class UnknownCommand implements Command  {
    private SendBotMessageService sendBotMessageService;
    public static final String NO_MESSAGE = "Unknown command, please check correction. For more information write /help";
    public UnknownCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), NO_MESSAGE);
    }
}
