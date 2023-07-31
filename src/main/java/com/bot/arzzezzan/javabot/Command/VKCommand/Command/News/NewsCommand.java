package com.bot.arzzezzan.javabot.Command.VKCommand.Command.News;

import com.bot.arzzezzan.javabot.Command.Command;
import com.bot.arzzezzan.javabot.Service.SendBotMessageService;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.docs.Doc;
import com.vk.api.sdk.objects.groups.Group;
import com.vk.api.sdk.objects.newsfeed.Filters;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.video.responses.GetResponse;
import com.vk.api.sdk.objects.wall.WallpostAttachment;
import com.vk.api.sdk.oneofs.NewsfeedNewsfeedItemOneOf;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.bot.arzzezzan.javabot.Command.VKCommand.Command.News.NewsManagerName.*;

public class NewsCommand implements Command {
    private SendBotMessageService sendBotMessageService;
    private VkApiClient vk;
    private UserActor userActor;
    private int messageId;
    private Update update;
    private boolean isEdit;
    com.vk.api.sdk.objects.newsfeed.responses.GetResponse posts;

    public NewsCommand(SendBotMessageService sendBotMessageService, VkApiClient vk, UserActor userActor) {
        this.userActor = userActor;
        this.vk = vk;
        this.sendBotMessageService = sendBotMessageService;
    }
    @Override
    public void execute(Update update) {
        this.update = update;

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
        message.setText("Управляйте своими нвостями!");

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        InlineKeyboardButton listButton = new InlineKeyboardButton();

        listButton.setText("Список");
        listButton.setCallbackData(LIST.getCommandName()); // Здесь вы можете задать новое значение для коллбэк-данных кнопки

        rowInLine.add(listButton);
        rowsInLine.add(rowInLine);

        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);


        sendBotMessageService.sendMessageMarkup(message);
    }
    public void callbackHandler(Update update, String callbackData) {
        this.update = update;
        if(callbackData.equals(LIST.getCommandName()) || callbackData.equals(NEXT.getCommandName())){
            getNews();
        } else if (callbackData.equals(BACK.getCommandName())) {
            execute(update);
        }
    }
    private void getNews() {
        StringBuilder newsBuilder = new StringBuilder();
        String startFrom = null;
        try {
            if(posts != null) {
                startFrom = posts.getNextFrom();
            }
            posts = vk.newsfeed().get(userActor).count(1).startFrom(startFrom).filters(Filters.POST).execute();
            for(NewsfeedNewsfeedItemOneOf item : posts.getItems()) {
                Group group;
                String text = item.getOneOf0().getText();
                group = vk.groups().getByIdObjectLegacy(userActor).
                        groupId(String.valueOf(Math.abs(item.getOneOf0().getSourceId()))).execute().get(0);
                String groupName = group.getName();
                if(text != null) {
                    text = "Группа: " + groupName + "\n\n" + text;
                } else {
                    text = "Группа: " + groupName + "\n\n";
                }
                newsBuilder.append(text).append("\n");
                List<WallpostAttachment> attachments = item.getOneOf0().getAttachments();
                if (attachments != null && !attachments.isEmpty()) {
                    for (WallpostAttachment attachment : attachments) {
                        if (attachment.getPhoto() != null) {
                            photoAttachmentHandler(newsBuilder, item, group, text, attachment);
                        } else if (attachment.getVideo() != null) {
                            videoAttachmentHandler(newsBuilder, attachment);
                        } else if (attachment.getLink() != null) {
                            linkAttachmentHandler(newsBuilder, attachment);
                        } else if(attachment.getDoc() != null) {
                            docAttachmentHandler(newsBuilder, attachment);
                        }
                    }
                } else {
                    sendBotMessageService.sendMessagePost(update.getCallbackQuery().getMessage().getChatId().toString(),
                            newsBuilder.toString());
                }
                newsBuilder = new StringBuilder();
            }
        } catch (ClientException | ApiException e) {
            e.printStackTrace();
        }
    }

    private void linkAttachmentHandler(StringBuilder newsBuilder, WallpostAttachment attachment) {
        newsBuilder.append(attachment.getLink().getUrl());
        sendBotMessageService.sendMessagePost(update.getCallbackQuery().getMessage().getChatId().toString(),
                newsBuilder.toString());
    }

    private void videoAttachmentHandler(StringBuilder newsBuilder, WallpostAttachment attachment) {
        try {
            GetResponse response = vk.videos().get(userActor).videos(attachment.getVideo().getOwnerId().toString() + "_" +
                    attachment.getVideo().getId() + "_" +
                    attachment.getVideo().getAccessKey()).execute();
            sendBotMessageService.sendVideo(update.getCallbackQuery().getMessage().getChatId().toString(),
                    response, newsBuilder.toString());
        } catch (ClientException | ApiException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void photoAttachmentHandler(StringBuilder newsBuilder, NewsfeedNewsfeedItemOneOf item, Group group, String text, WallpostAttachment attachment) {
        try {
            if (text.length() > 1024) {
                newsBuilder = new StringBuilder(newsBuilder.substring(0, 940) + "...\nОзнакомиться со всей записью можно по ссылке: " + String.format("https://vk.com/wall-%d_%d",
                        group.getId(),
                        item.getOneOf0().getPostId()));
            }
            Photo photo = attachment.getPhoto();
            String photoUrl = photo.getSizes().get(photo.getSizes().size() - 1).getUrl().toString();
            sendBotMessageService.sendPhoto(update.getCallbackQuery().getMessage().getChatId().toString(),
                        photoUrl, newsBuilder.toString());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
    private void docAttachmentHandler(StringBuilder newsBuilder, WallpostAttachment attachment) {
        Doc doc = attachment.getDoc();
        sendBotMessageService.sendDoc(update.getCallbackQuery().getMessage().getChatId().toString(),
                doc, newsBuilder.toString());
    }
}
