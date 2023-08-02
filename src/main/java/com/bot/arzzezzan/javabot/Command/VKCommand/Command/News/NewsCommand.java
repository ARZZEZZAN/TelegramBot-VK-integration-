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

import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Video;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaVideo;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import static com.bot.arzzezzan.javabot.Command.VKCommand.Command.News.NewsManagerName.*;

public class NewsCommand implements Command {
    private SendBotMessageService sendBotMessageService;
    private VkApiClient vk;
    private UserActor userActor;
    private Update update;
    private List<InputMedia> inputMediaPhotos = new ArrayList<>();
    private List<InputMedia> inputMediaVideos = new ArrayList<>();
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
        message.setText("Управляйте своими новостями!");

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
        inputMediaPhotos = new ArrayList<>();
        StringBuilder newsBuilder = new StringBuilder();
        String startFrom = null;
        boolean isPhoto = false;
        boolean isVideo = false;
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
                            isPhoto = true;
                            addPhotoToList(newsBuilder, attachment, item, group);
                        } else if (attachment.getVideo() != null) {
                            isVideo = true;
                            addVideoToList(newsBuilder, attachment);
                        } else if (attachment.getLink() != null) {
                            linkAttachmentHandler(newsBuilder, attachment);
                        } else if(attachment.getDoc() != null) {
                            docAttachmentHandler(newsBuilder, attachment);
                        }
                    }
                    if(isPhoto) {
                        mediaAttachmentHandler(newsBuilder, item, group, text);
                    } else if (isVideo) {
                        videoAttachmentHandler(newsBuilder);
                    }
                } else {
                    sendBotMessageService.sendMessagePost(update.getCallbackQuery().getMessage().getChatId().toString(),
                            newsBuilder.toString());
                }
                newsBuilder = new StringBuilder();
            }
        } catch (ClientException | ApiException | IOException e) {
            e.printStackTrace();
        }
    }

    private void addPhotoToList(StringBuilder newsBuilder, WallpostAttachment attachment, NewsfeedNewsfeedItemOneOf item, Group group) throws IOException {
        if (newsBuilder.toString().length() > 1024) {
            newsBuilder = new StringBuilder(newsBuilder.substring(0, 940) + "...\nОзнакомиться с полной записью тут: " + String.format("https://vk.com/wall-%d_%d",
                    group.getId(),
                    item.getOneOf0().getPostId()));
        }
        Photo photo = attachment.getPhoto();
        URL url = new URL(photo.getSizes().get(photo.getSizes().size() - 1).getUrl().toString());
        try(InputStream inputStream = url.openStream()) {
            byte[] photoBytes = inputStream.readAllBytes();
            InputFile file = new InputFile(new ByteArrayInputStream(photoBytes), photo.getAccessKey() + ".jpg");
            InputMediaPhoto inputMediaPhoto = new InputMediaPhoto();
            inputMediaPhoto.setMedia(file.getNewMediaStream(), photo.getAccessKey() + ".jpg");
            inputMediaPhoto.setCaption(newsBuilder.toString());
            inputMediaPhotos.add(inputMediaPhoto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void addVideoToList(StringBuilder newsBuilder, WallpostAttachment attachment)  {
        try {
            GetResponse response = vk.videos().get(userActor).videos(attachment.getVideo().getOwnerId().toString() + "_" +
                    attachment.getVideo().getId() + "_" +
                    attachment.getVideo().getAccessKey()).execute();
            URL url = new URL(response.getItems().get(0).getPlayer().toString());
            try (InputStream inputStream = url.openStream()) {
                byte[] videoBytes = inputStream.readAllBytes();
                InputFile file = new InputFile(new ByteArrayInputStream(videoBytes), response.hashCode() + ".mp4");

                InputMediaVideo inputMediaVideo = new InputMediaVideo();
                inputMediaVideo.setMedia(file.getNewMediaStream(), "video.mp4");
                inputMediaVideo.setCaption(newsBuilder.toString());
                inputMediaVideo.setNewMediaFile(file.getNewMediaFile());
                inputMediaVideos.add(inputMediaVideo);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (ClientException | ApiException | MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private void linkAttachmentHandler(StringBuilder newsBuilder, WallpostAttachment attachment) {
        newsBuilder.append(attachment.getLink().getUrl());
        sendBotMessageService.sendMessagePost(update.getCallbackQuery().getMessage().getChatId().toString(),
                newsBuilder.toString());
    }

    private void videoAttachmentHandler(StringBuilder newsBuilder) {
        try {
            sendBotMessageService.sendVideo(update.getCallbackQuery().getMessage().getChatId().toString(),
                    inputMediaVideos, newsBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void mediaAttachmentHandler(StringBuilder newsBuilder, NewsfeedNewsfeedItemOneOf item, Group group, String text) {
        try {
            if (text.length() > 1024) {
                newsBuilder = new StringBuilder(newsBuilder.substring(0, 940) + "...\nОзнакомиться с полной записью тут: " + String.format("https://vk.com/wall-%d_%d",
                        group.getId(),
                        item.getOneOf0().getPostId()));
            }
            inputMediaPhotos.get(0).setCaption(newsBuilder.toString());
            sendBotMessageService.sendMedia(update.getCallbackQuery().getMessage().getChatId().toString(),
                        inputMediaPhotos, newsBuilder.toString());
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
