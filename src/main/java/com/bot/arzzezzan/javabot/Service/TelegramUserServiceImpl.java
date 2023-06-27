package com.bot.arzzezzan.javabot.Service;

import com.bot.arzzezzan.javabot.Repository.Entity.TelegramUser;
import com.bot.arzzezzan.javabot.Repository.TelegramUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TelegramUserServiceImpl implements TelegramUserService {
    private TelegramUsersRepository telegramUsersRepository;

    @Autowired
    public TelegramUserServiceImpl(TelegramUsersRepository telegramUsersRepository) {
        this.telegramUsersRepository = telegramUsersRepository;
    }
    @Override
    public void save(TelegramUser telegramUser) {
        telegramUsersRepository.save(telegramUser);
    }

    @Override
    public List<TelegramUser> getAllUsers() {
        return telegramUsersRepository.findAllByActiveTrue();
    }

    @Override
    public Optional<TelegramUser> findByChatId(String chatId) {
        return telegramUsersRepository.findById(chatId);
    }
}
