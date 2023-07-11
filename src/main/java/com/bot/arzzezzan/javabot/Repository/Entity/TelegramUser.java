package com.bot.arzzezzan.javabot.Repository.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;



/**
 * Telegram User entity.
 */
@Data
@Entity
@Table(name = "tg_user", schema = "bot")
public class TelegramUser {

    @Id
    @Column(name = "chat_id")
    private String chatId;

    @Column(name = "active")
    private boolean active;
}
