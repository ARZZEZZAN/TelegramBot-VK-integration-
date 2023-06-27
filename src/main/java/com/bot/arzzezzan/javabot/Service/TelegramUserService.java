package com.bot.arzzezzan.javabot.Service;

import com.bot.arzzezzan.javabot.Repository.Entity.TelegramUser;

import java.util.List;
import java.util.Optional;

public interface TelegramUserService {
    void save(TelegramUser telegramUser);
    List<TelegramUser> getAllUsers();
    Optional<TelegramUser> findByChatId(String chatId);
}
