package com.shakal.sapper.logic.manager;

import com.shakal.BotConfig;
import com.shakal.sapper.logic.entity.Field;
import com.shakal.sapper.logic.exception.MenuException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


class MenuManager {

    private static final String easyMode = "Easy";
    private static final String normalMode = "Normal";
    private static final String hardMode = "Hard";
    private static final String initialText = BotConfig.getStringProperty("sapperBot.menu.initial_text");

    static SendMessage buildMenuMessage(long chatId) {
        return new SendMessage()
                .setChatId(chatId)
                .setText(initialText)
                .setReplyMarkup(buildMenuInlineKeyboard())
                .enableMarkdown(true);
    }

    static Field buildField(String callBackQuery) {
        Field field;

        switch (callBackQuery) {
            case easyMode:
                field = new Field(Field.Level.EASY);
                break;
            case normalMode:
                field = new Field(Field.Level.NORMAL);
                break;
            case hardMode:
                field = new Field(Field.Level.HARD);
                break;
            default:
                throw new MenuException("Incorrect mode!");
        }
        return field;
    }

    static boolean isMenuMessage(Message message){
        return message.getText().equals(initialText);
    }

    private static InlineKeyboardMarkup buildMenuInlineKeyboard() {

        List<InlineKeyboardButton> easy = Collections.singletonList(buildButton(easyMode));
        List<InlineKeyboardButton> normal = Collections.singletonList(buildButton(normalMode));
        List<InlineKeyboardButton> hard = Collections.singletonList(buildButton(hardMode));

        List<List<InlineKeyboardButton>> menuInlineKeyboard = Arrays.asList(easy, normal, hard);

        return new InlineKeyboardMarkup()
                .setKeyboard(menuInlineKeyboard);
    }

    private static InlineKeyboardButton buildButton(String text) {
        return new InlineKeyboardButton()
                .setText(text)
                .setCallbackData(text);
    }

}
