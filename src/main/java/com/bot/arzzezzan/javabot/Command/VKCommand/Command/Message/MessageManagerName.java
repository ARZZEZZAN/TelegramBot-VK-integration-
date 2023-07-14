package com.bot.arzzezzan.javabot.Command.VKCommand.Command.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum MessageManagerName {
    ONLINE("Online"),
    RECENT("Recent"),
    SUGGEST("Suggest")
    ;
    private final String commandName;
    private List<String> listofEnum;
    MessageManagerName(String commandName) {
        this.commandName = commandName;
        listofEnum = new ArrayList<>();
        listofEnum.add(commandName);
    }
    public String getCommandName() {
        return commandName;
    }
    public static List<String> getListOfEnum() {
        return Arrays.stream(values()).
                map(MessageManagerName::getCommandName).
                collect(Collectors.toList());
    }
}
