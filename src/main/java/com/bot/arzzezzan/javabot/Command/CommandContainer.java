package com.bot.arzzezzan.javabot.Command;

import com.bot.arzzezzan.javabot.Command.VKCommand.AuthCommand;
import com.bot.arzzezzan.javabot.Command.VKCommand.MenuCommand;
import com.bot.arzzezzan.javabot.Service.SendBotMessageService;
import com.bot.arzzezzan.javabot.Service.TelegramUserService;

import java.util.HashMap;

import static com.bot.arzzezzan.javabot.Command.CommandName.*;

public class CommandContainer {
    private HashMap<String, Command> commandHashMap;
    private SendBotMessageService sendBotMessageService;
    private TelegramUserService telegramUserService;

    public CommandContainer(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
        commandHashMap = new HashMap<>();
        {
            commandHashMap.put(START.getCommandName(), new StartCommand(sendBotMessageService, telegramUserService));
            commandHashMap.put(STOP.getCommandName(), new StopCommand(sendBotMessageService, telegramUserService));
            commandHashMap.put(HELP.getCommandName(), new HelpCommand(sendBotMessageService));
            commandHashMap.put(STAT.getCommandName(), new StatCommand(sendBotMessageService, telegramUserService));
            commandHashMap.put(MENU.getCommandName(), new MenuCommand(sendBotMessageService, telegramUserService));
            commandHashMap.put(AUTH.getCommandName(), new AuthCommand(sendBotMessageService, telegramUserService));
        };
    }
    public Command getCommand(String commandName) {
        return commandHashMap.getOrDefault(commandName, new UnknownCommand(sendBotMessageService));
    }
}
