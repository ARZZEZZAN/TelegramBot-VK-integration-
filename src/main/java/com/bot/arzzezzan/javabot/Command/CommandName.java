package com.bot.arzzezzan.javabot.Command;

public enum CommandName {
    START("/start"),
    STOP("/stop"),
    HELP("/help"),
    STAT("/stat"),
    UNKNOWN("")
    ;

    private final String commandName;
    CommandName(String commandName) {
        this.commandName = commandName;
    }
    public String getCommandName() {
        return commandName;
    }
}
