package com.bot.arzzezzan.javabot.Command.VKCommand.Command.Friend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum FriendManagerName {
    ONLINE("Online"),
    RECENT("Recent"),
    SUGGEST("Suggest")
    ;
    private final String commandName;
    private List<String> listofEnum;
    FriendManagerName(String commandName) {
        this.commandName = commandName;
        listofEnum = new ArrayList<>();
        listofEnum.add(commandName);
    }
    public String getCommandName() {
        return commandName;
    }
    public static List<String> getListOfEnum() {
        return Arrays.stream(values()).
                map(FriendManagerName::getCommandName).
                collect(Collectors.toList());
    }
}
