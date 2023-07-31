package com.bot.arzzezzan.javabot.Command;

import com.bot.arzzezzan.javabot.Service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.bot.arzzezzan.javabot.Command.CommandName.*;

public class HelpCommand implements Command {
    private SendBotMessageService sendBotMessageService;
    public static final String HELP_MESSAGE = String.format("✨<b>Доступные комманды</b>✨\n"
                    + "%s - Начать работу с ботом!\n"
                    + "%s - Остановить работу с ботом\n"
                    + "%s - Количество активных пользователей\n"
                    + "%s - Авторизация в ВКонтакте\n"
                    + "%s - Take help about my skills\n",
            START.getCommandName(), STOP.getCommandName(), HELP.getCommandName(), STAT.getCommandName(), MENU.getCommandName());
    public HelpCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), HELP_MESSAGE);
    }
}
