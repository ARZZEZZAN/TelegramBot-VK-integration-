package com.bot.arzzezzan.javabot.Command.VKCommand;

public enum CommandManagerName {
    FRIEND("Friend");

    private final String commandName;
    CommandManagerName(String commandName) {
        this.commandName = commandName;
    }
    public String getCommandName() {
        return commandName;
    }
}
