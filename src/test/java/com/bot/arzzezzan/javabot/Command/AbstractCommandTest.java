package com.bot.arzzezzan.javabot.Command;

import com.bot.arzzezzan.javabot.Bot.TelegramBot;
import com.bot.arzzezzan.javabot.Service.SendBotMessageService;
import com.bot.arzzezzan.javabot.Service.SendBotMessageServiceImpl;
import com.bot.arzzezzan.javabot.Service.TelegramUserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
/**
 * Abstract class for testing {@link Command}s.
 */
abstract class AbstractCommandTest {

    protected TelegramBot telegramBot = Mockito.mock(TelegramBot.class);
    protected SendBotMessageService sendBotMessageService = new SendBotMessageServiceImpl(telegramBot);
    protected TelegramUserService telegramUserService = Mockito.mock(TelegramUserService.class);

    abstract String getCommandName();

    abstract String getCommandMessage();
    abstract Command getCommand();
    @Test
    public void shouldProperlyExecuteCommand() throws TelegramApiException {
        //given
        Long chatId = 1234567824356L;
        Update update = new Update();
        Message message = Mockito.mock(Message.class);
        Mockito.when(message.getChatId()).thenReturn(chatId);
        Mockito.when(message.getText()).thenReturn(getCommandName());
        update.setMessage(message);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(getCommandMessage());
        sendMessage.enableHtml(true);
        //when
        getCommand().execute(update);
        //then
        Mockito.verify(telegramBot).execute(sendMessage);
    }
}
