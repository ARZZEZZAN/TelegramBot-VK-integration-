package com.bot.arzzezzan.javabot.Command.VKCommand;

public enum CommandManagerName {
    FRIEND("Friend"),
    NEWS("News");

    private final String commandName;
    CommandManagerName(String commandName) {
        this.commandName = commandName;
    }
    public String getCommandName() {
        return commandName;
    }
}
