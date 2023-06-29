package com.bot.arzzezzan.javabot.Command;

import org.junit.jupiter.api.DisplayName;

import static com.bot.arzzezzan.javabot.Command.CommandName.STOP;
import static com.bot.arzzezzan.javabot.Command.StopCommand.STOP_MESSAGE;

@DisplayName("Unit-level testing for StopCommand")
public class StopCommandTest extends AbstractCommandTest {
    @Override
    String getCommandName() {
        return STOP.getCommandName();
    }
    @Override
    String getCommandMessage() {
        return STOP_MESSAGE;
    }

    @Override
    Command getCommand() {
        return new StopCommand(sendBotMessageService, telegramUserService);
    }
}
