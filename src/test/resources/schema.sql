create schema if not exists bot;

CREATE TABLE bot.tg_user (
                         chat_id VARCHAR(100),
                         active BOOLEAN
);