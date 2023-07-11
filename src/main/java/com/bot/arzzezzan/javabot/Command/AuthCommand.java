package com.bot.arzzezzan.javabot.Command;

import com.bot.arzzezzan.javabot.Service.SendBotMessageService;
import com.bot.arzzezzan.javabot.Service.TelegramUserService;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.newsfeed.responses.GetResponse;
import com.vk.api.sdk.oneofs.NewsfeedNewsfeedItemOneOf;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class AuthCommand implements Command {
    private final static String VK_APP_ID = "51700494";
    private final static String VK_CLIENT_SECRET = "3200dbc13200dbc13200dbc1f8311438cf332003200dbc156add03e023dfbb215f947b5";
    private final static String REDIRECT_URI = "https://www.youtube.com/";
    SendBotMessageService sendBotMessageService;
    TelegramUserService telegramUserService;
    private VkApiClient vkApiClient;
    private UserActor userActor;
    public static final String AUTH_MESSAGE = "Let's go!";

    public AuthCommand(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
        vkApiClient = new VkApiClient(HttpTransportClient.getInstance());
        userActor = new UserActor(Integer.parseInt(VK_APP_ID), VK_CLIENT_SECRET);
    }
    @Override
    public void execute(Update update) {
        try {
            GetResponse response = vkApiClient.newsfeed().get(userActor)
                    .count(10)
                    .execute();

            List<NewsfeedNewsfeedItemOneOf> items = response.getItems();

            for (NewsfeedNewsfeedItemOneOf item : items) {
                // Выводим текст каждой новости
                sendBotMessageService.sendMessage(
                        update.getMessage().getChatId().toString(),
                        item.toString());
            }
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }
}