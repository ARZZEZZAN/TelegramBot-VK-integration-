package com.bot.arzzezzan.javabot.Command;

import static com.bot.arzzezzan.javabot.Command.CommandName.STAT;
import static com.bot.arzzezzan.javabot.Command.StatCommand.STAT_MESSAGE;

public class StatCommandTest extends AbstractCommandTest {
    @Override
    String getCommandName() {
        return STAT.getCommandName();
    }

    @Override
    String getCommandMessage() {
        return String.format(STAT_MESSAGE, 0);
    }

    @Override
    Command getCommand() {
        return new StatCommand(sendBotMessageService, telegramUserService);
    }
}
