package com.bot.arzzezzan.javabot.Command.VKCommand;

import com.bot.arzzezzan.javabot.Command.Command;
import com.bot.arzzezzan.javabot.Command.VKCommand.Command.FriendCommand;
import com.bot.arzzezzan.javabot.Service.SendBotMessageService;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.bot.arzzezzan.javabot.Command.VKCommand.AuthCommand.CLIENT_ID;
import static com.bot.arzzezzan.javabot.Command.VKCommand.AuthCommand.FRIEND_BUTTON;

public class AccountManager {
    private Update update;
    private SendBotMessageService sendBotMessageService;
    private static final String TOKEN = "vk1.a.iHndOb3lmKv1aiwcvkQhsT5kdOwf5QJZEy_opf-KYOmMqt4q3vqXX1u7mLcSVt_3LEVOxWaXDRGAmzUkQZF8b-jvFnjV8GSXBN2XG1HIJ0IixANcPBHyGHbv63aRxqN3DNZn0YdA70MC5TAW9yqllyb4sWMeKig8Gb1QLXRMqf3driQJIqrqOkSPfovh2GufzJoAzJxUWjMLMVwYG9mKBg";
    public AccountManager(Update update, SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
        this.update = update;
    }
    public void commandManagerHandler() {
        VkApiClient vk = new VkApiClient(HttpTransportClient.getInstance());
        UserActor userActor = new UserActor(Integer.parseInt(CLIENT_ID), TOKEN);

        String callbackData = update.getCallbackQuery().getData();

        if(callbackData.equals(FRIEND_BUTTON)){
            Command command = new FriendCommand(sendBotMessageService, vk, userActor);
            command.execute(update);
        }
    }
}
