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
    private static final String TOKEN = "vk1.a.VpM9CXNdNKxn4HHPeEajABrf6cgf49g6fWXwgcgnSgZCQqJE9NJz5G1uqOTURVdbjaVrwGCwrVpG55rFCFlUGcPmHSCFwP-7NTe20XpJoje2697wwbwQUhNPy8ESKW7pGS7SYXM2D86BqEQe7DsJIq8yskGA1aJWlCpzrfE6mazIo5kTHfBz5j7sSP1p5do64Ounh-q7qdUnHpvw90Ha-A";
    public AccountManager(Update update, SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
        this.update = update;
    }
    public void commandManagerHandler() {
        VkApiClient vk = new VkApiClient(HttpTransportClient.getInstance());
        UserActor userActor = new UserActor(Integer.parseInt(CLIENT_ID), TOKEN);

        String callbackData = update.getCallbackQuery().getData();

        FriendCommand friendCommand = new FriendCommand(sendBotMessageService, vk, userActor);
        NewsCommand newsCommand = new NewsCommand(sendBotMessageService, vk, userActor);
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
}
