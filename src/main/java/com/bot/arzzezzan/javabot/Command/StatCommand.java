package com.bot.arzzezzan.javabot.Command;

import com.bot.arzzezzan.javabot.Service.SendBotMessageService;
import com.bot.arzzezzan.javabot.Service.TelegramUserService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StatCommand implements Command {
    private SendBotMessageService sendBotMessageService;
    private TelegramUserService telegramUserService;
    @Override
    public void execute(Update update) {

    }
}
