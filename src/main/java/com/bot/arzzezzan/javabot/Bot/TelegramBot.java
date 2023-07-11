package com.bot.arzzezzan.javabot.Bot;

import com.bot.arzzezzan.javabot.Command.CommandContainer;
import com.bot.arzzezzan.javabot.Service.SendBotMessageServiceImpl;
import com.bot.arzzezzan.javabot.Service.TelegramUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;


import static com.bot.arzzezzan.javabot.Command.CommandName.UNKNOWN;


@Component
public class TelegramBot extends TelegramLongPollingBot {
    private static final String COMMAND_PREFIX = "/";
    @Value("${bot.token}")
    private String token;
    @Value("${bot.username}")
    private String username;

    private CommandContainer commandContainer;

    @Autowired
    public TelegramBot(TelegramUserService telegramUserService) {
        commandContainer = new CommandContainer(new SendBotMessageServiceImpl(this), telegramUserService);
    }
    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText().trim();
            if(message.startsWith(COMMAND_PREFIX)) {
                String commandIdentifier = message.split(" ")[0].toLowerCase();
                commandContainer.getCommand(commandIdentifier).execute(update);
            } else {
                commandContainer.getCommand(UNKNOWN.getCommandName()).execute(update);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
