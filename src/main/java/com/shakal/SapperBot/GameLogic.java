package com.shakal.SapperBot;

import com.shakal.BotConfig;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;


 class GameLogic {

    private final static Logger logger = Logger.getLogger(GameLogic.class.getSimpleName());

    private static final int fieldWidth = BotConfig.getIntProperty("sapperBot.game.fieldWidth");
    private static final int fieldHeight = BotConfig.getIntProperty("sapperBot.game.fieldHeight");
    private static final int bombCount = BotConfig.getIntProperty("sapperBot.game.bombCount");

    private enum Command{
        START, CHANGE_TOOL
    }

     static InlineKeyboardMarkup buildField(){

        List<List<InlineKeyboardButton>> field = new ArrayList<>();
        for (int k = 0; k < fieldHeight; k++) {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            for (int m = 0; m < fieldWidth; m++) {
                rowInline.add(new InlineKeyboardButton().setText(Emoji.closedCell).setCallbackData(Command.START.name()));
            }
            field.add(rowInline);
        }
        field.add(Collections.singletonList(new InlineKeyboardButton().setText(Emoji.flag).setCallbackData(Command.CHANGE_TOOL.name())));

        return new InlineKeyboardMarkup().setKeyboard(field);
    }
}
