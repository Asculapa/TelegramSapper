package com.shakal.sapper;

import com.shakal.BotConfig;
import com.shakal.sapper.logic.manager.GameManager;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;
import java.util.logging.Logger;

public class SapperBot extends TelegramLongPollingBot {

    private final static Logger logger = Logger.getLogger(SapperBot.class.getSimpleName());
    private final static String startCommand = BotConfig.getStringProperty("sapperBot.commands.start");

    public void onUpdateReceived(final Update update) {
        if (hasCommand(update, startCommand)) {
            long chatId = update.getMessage().getChatId();
            User user = update.getMessage().getFrom();
            SendMessage initialMessage = GameManager.getInitialMessage(chatId);

            sendMessage(initialMessage);
            logger.info(user.getUserName() + " (" + user.getFirstName() + " " + user.getLastName() + ") started play game!");
        } else if (update.hasCallbackQuery()) {
            update.getCallbackQuery().getFrom();
            User user = update.getCallbackQuery().getFrom();
            EditMessageText editMessage = GameManager.editMessage(update, user);
            sendMessage(editMessage);
        }
    }

    public String getBotUsername() {
        return BotConfig.getStringProperty("sapperBot.name");
    }

    public String getBotToken() {
        return BotConfig.getStringProperty("sapperBot.token");
    }

    private boolean hasCommand(Update update, String command) {
        Message message = update.getMessage();
        return update.hasMessage() &&
                message.hasText() &&
                message.getText().equals(command);
    }

    private void sendMessage(BotApiMethod<? extends Serializable> message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
           e.printStackTrace();
        }
    }
}
