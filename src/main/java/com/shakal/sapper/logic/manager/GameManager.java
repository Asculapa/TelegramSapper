package com.shakal.sapper.logic.manager;

import com.shakal.sapper.logic.entity.Field;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public class GameManager {

    public static SendMessage getInitialMessage(long chatId) {
        return MenuManager.buildMenuMessage(chatId);
    }

    public static EditMessageText editMessage(Update update, User user) {
        String callbackQueryData = update.getCallbackQuery().getData();
        Message message = update.getCallbackQuery().getMessage();
        EditMessageText editMessage = new EditMessageText();

        if (MenuManager.isMenuMessage(message)) {
            Field gameField = MenuManager.buildField(callbackQueryData);
            InlineKeyboardMarkup inlineKeyboardMarkup = LogicManager.buildEmptyField(gameField);
            editMessage.setReplyMarkup(inlineKeyboardMarkup);
            editMessage.setText("Let's start!");
        } else {
            editMessage = LogicManager.analyze(message, callbackQueryData, user);
        }

        return editMessage
                .setChatId(message.getChatId())
                .setMessageId(message.getMessageId());
    }
}
