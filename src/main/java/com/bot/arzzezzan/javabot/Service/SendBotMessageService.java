package com.bot.arzzezzan.javabot.Service;

import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.video.Video;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.net.MalformedURLException;

public interface SendBotMessageService {
    public void sendMessage(String chatId, String message);
    public void sendMessageMarkup(SendMessage message);
    public void sendPhoto(String chatId, Photo photo, String text) throws MalformedURLException;
    public void sendVideo(String chatId, Video video, String text) throws MalformedURLException;
}
