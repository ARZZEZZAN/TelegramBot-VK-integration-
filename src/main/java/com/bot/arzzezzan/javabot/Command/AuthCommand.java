package com.bot.arzzezzan.javabot.Command;

import com.bot.arzzezzan.javabot.Service.SendBotMessageService;
import com.bot.arzzezzan.javabot.Service.TelegramUserService;
import com.vk.api.sdk.actions.Auth;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.UserAuthResponse;
import com.vk.api.sdk.objects.newsfeed.responses.GetResponse;
import com.vk.api.sdk.oneofs.NewsfeedNewsfeedItemOneOf;
import com.vk.api.sdk.queries.friends.FriendsGetAppUsersQuery;
import com.vk.api.sdk.queries.friends.FriendsGetOnlineQuery;
import com.vk.api.sdk.queries.friends.FriendsGetQuery;
import com.vk.api.sdk.queries.oauth.OAuthUserAuthorizationCodeFlowQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

    public class AuthCommand implements Command {
        private final static String VK_APP_ID = "51700494";
        private final static String VK_CLIENT_SECRET = "3200dbc13200dbc13200dbc1f8311438cf332003200dbc156add03e023dfbb215f947b5";
        private final static String REDIRECT_URI = "https://oauth.vk.com/authorize?client_id=51700494&display=page&redirect_uri=https://oauth.vk.com/blank.html&scope=friends,offline,groups&response_type=code&v=5.131";
        private final static String SCOPE = "offline"; // Другие разрешения VK API, если необходимо

        private final SendBotMessageService sendBotMessageService;
        private final TelegramUserService telegramUserService;

        public AuthCommand(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService) {
            this.sendBotMessageService = sendBotMessageService;
            this.telegramUserService = telegramUserService;
        }

        @Override
        public void execute(Update update) {
//            String authorizationUrl = getAuthorizationUrl();
//            sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), authorizationUrl);
            handleAuthorizationResponse(update);
        }

        private String getAuthorizationUrl() {
            VkApiClient vk = new VkApiClient(HttpTransportClient.getInstance());

            OAuthUserAuthorizationCodeFlowQuery auth = vk.oAuth().userAuthorizationCodeFlow(Integer.parseInt(VK_APP_ID), VK_CLIENT_SECRET, REDIRECT_URI, SCOPE);
            return auth.getUrl();
        }
        public void handleAuthorizationResponse(Update update) {
            VkApiClient vk = new VkApiClient(HttpTransportClient.getInstance());
            try {
//                UserAuthResponse authResponse = vk.oauth()
//                        .client(VK_APP_ID, VK_CLIENT_SECRET)
//                        .redirectUri(REDIRECT_URI)
//                        .code(code)
//                        .execute();

                // Получаем токен доступа и ID пользователя
                String accessToken = "vk1.a.iHndOb3lmKv1aiwcvkQhsT5kdOwf5QJZEy_opf-KYOmMqt4q3vqXX1u7mLcSVt_3LEVOxWaXDRGAmzUkQZF8b-jvFnjV8GSXBN2XG1HIJ0IixANcPBHyGHbv63aRxqN3DNZn0YdA70MC5TAW9yqllyb4sWMeKig8Gb1QLXRMqf3driQJIqrqOkSPfovh2GufzJoAzJxUWjMLMVwYG9mKBg";

                // Создаем актора пользователя VK
                UserActor userActor = new UserActor(Integer.parseInt("429866479"), accessToken);

                FriendsGetOnlineQuery response = vk.friends().getOnline(userActor);

                // Отправляем сообщение с новостями в чат
                sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), response.executeAsString());

            } catch (Exception e) {
                e.printStackTrace();
                // Обработка ошибки авторизации или получения новостей
            }
        }
    }
