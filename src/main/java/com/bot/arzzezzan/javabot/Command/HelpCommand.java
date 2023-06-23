package com.bot.arzzezzan.javabot.Command;

import com.bot.arzzezzan.javabot.Service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.bot.arzzezzan.javabot.Command.CommandName.*;

public class HelpCommand implements Command {
    private SendBotMessageService sendBotMessageService;
    private final String commandMessage = String.format("✨<b>Available commands</b>✨\n"
                    + "%s - Start working with bot\n"
                    + "%s - Stop working with bot\n"
                    + "%s - Take help about my skills\n",
            START.getCommandName(), STOP.getCommandName(), HELP.getCommandName());
    public HelpCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), commandMessage);
    }
}
