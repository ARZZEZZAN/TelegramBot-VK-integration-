package com.bot.arzzezzan.javabot.Command.VKCommand;

import com.bot.arzzezzan.javabot.Command.VKCommand.Command.Friend.FriendCommand;
import com.bot.arzzezzan.javabot.Command.VKCommand.Command.Friend.FriendManagerName;
import com.bot.arzzezzan.javabot.Command.VKCommand.Command.News.NewsCommand;
import com.bot.arzzezzan.javabot.Command.VKCommand.Command.News.NewsManagerName;
import com.bot.arzzezzan.javabot.Service.SendBotMessageService;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.bot.arzzezzan.javabot.Command.VKCommand.AuthCommand.CLIENT_ID;
import static com.bot.arzzezzan.javabot.Command.VKCommand.CommandManagerName.FRIEND;
import static com.bot.arzzezzan.javabot.Command.VKCommand.CommandManagerName.NEWS;

public class AccountManager {
    private Update update;
    private SendBotMessageService sendBotMessageService;
    FriendCommand friendCommand;
    NewsCommand newsCommand;
    private static final String TOKEN = "vk1.a.q407rkDM1GB6-6pZxwu3t_dtqYX40BfWyXfSVtYyofkyFfYOcS16xn6VocxPjATuR6zLecu8pw3Jo-ujxCJTUU6K-4hSbOFF7-kgdE8UoEimEk0lQu4ZlFKshMeMnOHfDHT2p1o9vwPspE-wrhnXj4H4Ge4K2xoiFDDESsdvIcbzEnNmDDjPLD-ae1eUGbph3YHjj7TYJFeOeXLpzLl91A";
    public AccountManager(Update update, SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
        this.update = update;

        VkApiClient vk = new VkApiClient(HttpTransportClient.getInstance());
        UserActor userActor = new UserActor(Integer.parseInt(CLIENT_ID), TOKEN);

        friendCommand = new FriendCommand(sendBotMessageService, vk, userActor);
        newsCommand = new NewsCommand(sendBotMessageService, vk, userActor);
    }
    public void commandManagerHandler() {

        String callbackData = update.getCallbackQuery().getData();

        if(callbackData.equals(FRIEND.getCommandName())){
            friendCommand.execute(update);
        } else if (FriendManagerName.getListOfEnum().contains(callbackData)) {
            friendCommand.callbackHandler(update, callbackData);
        } else if (callbackData.equals(NEWS.getCommandName())){
            newsCommand.execute(update);
        } else if (NewsManagerName.getListOfEnum().contains(callbackData)) {
            newsCommand.callbackHandler(update, callbackData);
        }
    }
    public void setUpdate(Update update) {
        this.update = update;
    }
}
