package com.bot.arzzezzan.javabot.Command;


import com.bot.arzzezzan.javabot.Service.SendBotMessageService;
import com.bot.arzzezzan.javabot.Service.TelegramUserService;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.users.UserFull;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.objects.users.responses.GetResponse;
import com.vk.api.sdk.queries.friends.FriendsGetOnlineQuery;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Component
public class AuthCommand implements Command {
    private final static String USER_ID = "429866479";
    private final static String CLIENT_ID = "51700494";
    private final static String REDIRECT_URI = "https://oauth.vk.com/blank.html";
    private final static String VK_API_VERSION = "5.131";
    private final SendBotMessageService sendBotMessageService;
    private final TelegramUserService telegramUserService;
    private Update update;

    public AuthCommand(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public void execute(Update update) {
        this.update = update;
        String authUrl = getAuthorizationUrl();
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), authUrl);
        handleAuthorizationResponse("vk1.a.iHndOb3lmKv1aiwcvkQhsT5kdOwf5QJZEy_opf-KYOmMqt4q3vqXX1u7mLcSVt_3LEVOxWaXDRGAmzUkQZF8b-jvFnjV8GSXBN2XG1HIJ0IixANcPBHyGHbv63aRxqN3DNZn0YdA70MC5TAW9yqllyb4sWMeKig8Gb1QLXRMqf3driQJIqrqOkSPfovh2GufzJoAzJxUWjMLMVwYG9mKBg");
    }

    private String getAuthorizationUrl() {
        return "https://oauth.vk.com/authorize?client_id=" + CLIENT_ID +
                "&display=page&redirect_uri=" + REDIRECT_URI +
                "&scope=offline,friends&response_type=token&v=" + VK_API_VERSION;
    }

    // Метод, вызываемый при получении ответа на авторизацию VK
    public void handleAuthorizationResponse(String token) {
        VkApiClient vk = new VkApiClient(HttpTransportClient.getInstance());

        // Создайте экземпляр UserActor, используя полученный токен доступа и ID пользователя
        UserActor userActor = new UserActor(Integer.parseInt(CLIENT_ID), token);
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), getSuggests(vk, userActor));
    }
    public String getOnlineFriends(VkApiClient vk, UserActor userActor) {
        StringBuilder onlineFriends = new StringBuilder();
        int count = 0;
        try {
            List<Integer> onlineFriendIds = vk.friends().getOnline(userActor).execute();
            onlineFriends.append("<b>Online friends:</b>\n");

            outputOfIds(vk, userActor, onlineFriends, count, onlineFriendIds);
        } catch (ClientException | ApiException e) {
            e.printStackTrace();
        }

        return onlineFriends.toString();
    }
    public String getRecent(VkApiClient vk, UserActor userActor) {
        StringBuilder recentFriends = new StringBuilder();
        int count = 0;
        try {
            List<Integer> recentFriendIds = vk.friends().getRecent(userActor).execute();
            recentFriends.append("<b>Recent friends:</b>\n");

            outputOfIds(vk, userActor, recentFriends, count, recentFriendIds);
        } catch (ClientException | ApiException e) {
            e.printStackTrace();
        }

        return recentFriends.toString();
    }
    public String getSuggests(VkApiClient vk, UserActor userActor) {
        StringBuilder suggFriends = new StringBuilder();
        int count = 0;
        try {
            List<UserFull> suggestionsResponse = vk.friends().getSuggestions(userActor).execute().getItems();
            List<Integer> suggIds = new ArrayList<>(10);
            for(UserFull userFull : suggestionsResponse) {
                suggIds.add(userFull.getId());
            }
            suggFriends.append("<b>Suggestions:</b>\n");
            outputOfIds(vk, userActor, suggFriends, count, suggIds);
        } catch (ClientException | ApiException e) {
            e.printStackTrace();
        }

        return suggFriends.toString();
    }

    private void outputOfIds(VkApiClient vk, UserActor userActor, StringBuilder strFriends, int count, List<Integer> friends) throws ApiException, ClientException {
        for (Integer friendId : friends) {
            GetResponse friend = vk.users().get(userActor).userIds(String.valueOf(friendId)).execute().get(0);
            strFriends.append(++count + ". ");
            String friendLink = "<a href=\"https://vk.com/id" + friend.getId() + "\">" + friend.getFirstName() + " " + friend.getLastName() + "</a>";
            strFriends.append(friendLink).append("\n");
        }
    }
}