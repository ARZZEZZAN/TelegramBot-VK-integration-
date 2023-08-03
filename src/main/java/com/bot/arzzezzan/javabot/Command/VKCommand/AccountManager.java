package com.bot.arzzezzan.javabot.Command.VKCommand;

import com.bot.arzzezzan.javabot.Command.VKCommand.Command.Friend.FriendCommand;
import com.bot.arzzezzan.javabot.Command.VKCommand.Command.Friend.FriendManagerName;
import com.bot.arzzezzan.javabot.Command.VKCommand.Command.News.NewsCommand;
import com.bot.arzzezzan.javabot.Command.VKCommand.Command.News.NewsManagerName;
import com.bot.arzzezzan.javabot.Service.SendBotMessageService;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiAuthException;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;

import com.vk.api.sdk.objects.wall.responses.GetResponse;
import com.vk.api.sdk.queries.account.AccountGetInfoQuery;
import com.vk.api.sdk.queries.wall.WallGetQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.bot.arzzezzan.javabot.Command.VKCommand.AuthCommand.CLIENT_ID;
import static com.bot.arzzezzan.javabot.Command.VKCommand.CommandManagerName.FRIEND;
import static com.bot.arzzezzan.javabot.Command.VKCommand.CommandManagerName.NEWS;


public class AccountManager {
    private Update update;
    private SendBotMessageService sendBotMessageService;
    private FriendCommand friendCommand;
    private NewsCommand newsCommand;
    private static String TOKEN = null;
    private static boolean isValid = false;
    public AccountManager(Update update, SendBotMessageService sendBotMessageService, String token) {
        this.sendBotMessageService = sendBotMessageService;
        this.update = update;
        TOKEN = token;

        VkApiClient vk = new VkApiClient(HttpTransportClient.getInstance());
        UserActor userActor = new UserActor(Integer.parseInt(CLIENT_ID), TOKEN);

        isValidToken(update, sendBotMessageService, vk, userActor);

        friendCommand = new FriendCommand(sendBotMessageService, vk, userActor);
        newsCommand = new NewsCommand(sendBotMessageService, vk, userActor);
    }

    private static void isValidToken(Update update, SendBotMessageService sendBotMessageService, VkApiClient vk, UserActor userActor) {
        try {
            GetResponse info = vk.wall().get(userActor).execute();
            if(info != null) {
                isValid = true;
                sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(),
                        "Регистрация прошла успешно, используйте команду '/menu' для управления аккаунтом!");
            }
        } catch(ClientException | ApiException e) {
            sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(),
                    e.getMessage());
        }
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
    public static boolean isIsValid() {
        return isValid;
    }

}
