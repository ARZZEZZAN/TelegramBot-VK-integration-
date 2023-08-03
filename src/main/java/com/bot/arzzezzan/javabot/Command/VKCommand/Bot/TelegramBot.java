package com.bot.arzzezzan.javabot.Command.VKCommand.Bot;

import com.bot.arzzezzan.javabot.Command.CommandContainer;
import com.bot.arzzezzan.javabot.Command.VKCommand.AccountManager;
import com.bot.arzzezzan.javabot.Service.SendBotMessageService;
import com.bot.arzzezzan.javabot.Service.SendBotMessageServiceImpl;
import com.bot.arzzezzan.javabot.Service.TelegramUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;


import java.io.IOException;

import static com.bot.arzzezzan.javabot.Command.CommandName.AUTH;
import static com.bot.arzzezzan.javabot.Command.CommandName.UNKNOWN;


@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {
    private static final String COMMAND_PREFIX = "/";
    @Value("${bot.token}")
    private String token;
    @Value("${bot.username}")
    private String username;

    private CommandContainer commandContainer;
    private AccountManager accountManager;
    private SendBotMessageService sendBotMessageService;
    private boolean isAuth;
    private static String ACC_TOKEN;
    private String commandIdentifier = "unknown";

    @Autowired
    public TelegramBot(TelegramUserService telegramUserService) {
        sendBotMessageService = new SendBotMessageServiceImpl(this);
        commandContainer = new CommandContainer(sendBotMessageService, telegramUserService);
    }
    @Override
    public void onUpdateReceived(Update update) {
        if(accountManager != null) {
            accountManager.setUpdate(update);
        }
        if(update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText().trim();
            if(message.startsWith("vk") && message.length() > 2) {
                checkToken(update, message);
            } else if (message.startsWith(COMMAND_PREFIX) && isAuth) {
                callCommand(update, message);
            } else if(message.equals("/auth")) {
                if(!isAuth) {
                    callCommand(update, message);
                } else {
                    sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(),
                            "Отправь правильный \"access_token\"!");
                }
            } else if(!isAuth) {
                sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(),
                        "Пожалуйста зарегистрируйся для начала пользования ботом!");
            } else {
                try {
                    commandContainer.getCommand(UNKNOWN.getCommandName()).execute(update);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (update.hasCallbackQuery()) {
            accountManager.commandManagerHandler();
        }
    }

    private void callCommand(Update update, String message) {
        commandIdentifier = message.split(" ")[0].toLowerCase();
        try {
            commandContainer.getCommand(commandIdentifier).execute(update);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkToken(Update update, String message) {
        if(commandIdentifier.equals("/auth")) {
            ACC_TOKEN = message;
            accountManager = new AccountManager(update, sendBotMessageService, ACC_TOKEN);
            isAuth = AccountManager.isIsValid();
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
