package com.bot.arzzezzan.javabot.Command;

import com.bot.arzzezzan.javabot.Service.SendBotMessageService;

import java.util.HashMap;

import static com.bot.arzzezzan.javabot.Command.CommandName.*;

public class CommandContainer {
    private HashMap<String, Command> commandHashMap;
    private SendBotMessageService sendBotMessageService;

    public CommandContainer(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
        commandHashMap = new HashMap<>();
        {
            commandHashMap.put(START.getCommandName(), new StartCommand(sendBotMessageService));
            commandHashMap.put(STOP.getCommandName(), new StopCommand(sendBotMessageService));
            commandHashMap.put(HELP.getCommandName(), new HelpCommand(sendBotMessageService));
        };
    }
    public Command getCommand(String commandName) {
        return commandHashMap.getOrDefault(commandName, new UnknownCommand(sendBotMessageService));
    }
}
