package com.bot.arzzezzan.javabot.Command.VKCommand;

import com.bot.arzzezzan.javabot.Command.Command;
import com.bot.arzzezzan.javabot.Service.SendBotMessageService;
import com.bot.arzzezzan.javabot.Service.TelegramUserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;


@Component
public class AuthCommand implements Command {
        public final static String CLIENT_ID = "51700494";
        private final static String REDIRECT_URI = "https://oauth.vk.com/blank.html";
        private final static String VK_API_VERSION = "5.131";
        private final SendBotMessageService sendBotMessageService;
        private final TelegramUserService telegramUserService;
        private Update update;
        private String chatId;

        public AuthCommand(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService) {
            this.sendBotMessageService = sendBotMessageService;
            this.telegramUserService = telegramUserService;
        }

        @Override
        public void execute(Update update) {
            this.update = update;
            chatId = update.getMessage().getChatId().toString();
            sendBotMessageService.sendMessage(chatId, getAuthorizationUrl());
        }

        private String getAuthorizationUrl() {
            return "Для регистрации перейдите по ссылке: <a href=\"https://oauth.vk.com/authorize?client_id=" + CLIENT_ID + "&display=page&redirect_uri=" + REDIRECT_URI +
                    "&scope=video,photos,likes,groups,friends,wall,stats,offline&response_type=token&v=" + VK_API_VERSION + "\">ссылка на авторизацию</a>\n" +
                    "Пришлите токен после регистрации в чат, токен находится в адрессной строке, в аргументе access_token.";
        }
}
