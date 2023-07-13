package com.bot.arzzezzan.javabot.Command.VKCommand.Command;

import com.bot.arzzezzan.javabot.Command.Command;
import com.bot.arzzezzan.javabot.Service.SendBotMessageService;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.objects.users.UserFull;
import com.vk.api.sdk.objects.users.responses.GetResponse;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

public class FriendCommand implements Command {
    private SendBotMessageService sendBotMessageService;
    private VkApiClient vk;
    private UserActor userActor;
    private static final String friendMessage = "You can manage your list of friends!";

    public FriendCommand(SendBotMessageService sendBotMessageService, VkApiClient vk, UserActor userActor) {
        this.userActor = userActor;
        this.vk = vk;
        this.sendBotMessageService = sendBotMessageService;
    }
    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getCallbackQuery().getMessage().getChatId().toString(), getOnlineFriends());
    }
    public String getOnlineFriends() {
        StringBuilder onlineFriends = new StringBuilder();
        int count = 0;
        try {
            List<Integer> onlineFriendIds = vk.friends().getOnline(userActor).execute();
            onlineFriends.append("<b>Online friends:</b>\n");

            outputOfIds(onlineFriends, count, onlineFriendIds);
        } catch (ClientException | ApiException e) {
            e.printStackTrace();
        }

        return onlineFriends.toString();
    }
    public String getRecent() {
        StringBuilder recentFriends = new StringBuilder();
        int count = 0;
        try {
            List<Integer> recentFriendIds = vk.friends().getRecent(userActor).execute();
            recentFriends.append("<b>Recent friends:</b>\n");

            outputOfIds(recentFriends, count, recentFriendIds);
        } catch (ClientException | ApiException e) {
            e.printStackTrace();
        }

        return recentFriends.toString();
    }
    public String getSuggests() {
        StringBuilder suggFriends = new StringBuilder();
        int count = 0;
        try {
            List<UserFull> suggestionsResponse = vk.friends().getSuggestions(userActor).execute().getItems();
            List<Integer> suggIds = new ArrayList<>(10);
            for(UserFull userFull : suggestionsResponse) {
                suggIds.add(userFull.getId());
            }
            suggFriends.append("<b>Suggestions:</b>\n");
            outputOfIds(suggFriends, count, suggIds);
        } catch (ClientException | ApiException e) {
            e.printStackTrace();
        }

        return suggFriends.toString();
    }

    public void outputOfIds(StringBuilder strFriends, int count, List<Integer> friends) throws ApiException, ClientException {
        List<GetResponse> friendInfo = vk.users().get(userActor)
                .userIds(friends.stream().map(String::valueOf).toArray(String[]::new))
                .fields(Fields.FIRST_NAME_ABL, Fields.LAST_NAME_ABL)
                .execute();

        for (GetResponse friend : friendInfo) {
            strFriends.append(++count).append(". ");
            String friendLink = "<a href=\"https://vk.com/id" + friend.getId() + "\">" + friend.getFirstName() + " " + friend.getLastName() + "</a>";
            strFriends.append(friendLink).append("\n");
        }
    }
}
