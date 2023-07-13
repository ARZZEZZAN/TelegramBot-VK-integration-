package com.bot.arzzezzan.javabot.Service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface SendBotMessageService {
    public void sendMessage(String chatId, String message);
    public void sendMessageMarkup(SendMessage message);
}
