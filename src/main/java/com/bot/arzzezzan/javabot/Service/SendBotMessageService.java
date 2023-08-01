package com.bot.arzzezzan.javabot.Service;

import com.vk.api.sdk.objects.docs.Doc;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.video.responses.GetResponse;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;

import java.net.MalformedURLException;
import java.util.List;

public interface SendBotMessageService {
    public void sendMessage(String chatId, String message);
    public void sendMessage(EditMessageText editMessageText);
    public void sendMessagePost(String chatId, String message);
    public void sendMessageMarkup(EditMessageReplyMarkup message);
    public void sendMessageMarkup(SendMessage message);
    public void sendPhoto(String chatId, String photoUrl, String text) throws MalformedURLException ;
    public void sendMedia(String chatId, List<InputMedia> mediaPhotos, String text) throws MalformedURLException;
    public void sendVideo(String chatId, GetResponse response, String text) throws MalformedURLException;
    public void sendDoc(String chatId, Doc doc, String text);

}
