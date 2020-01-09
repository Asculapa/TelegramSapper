package com.shakal.SapperBot;

import com.shakal.BotConfig;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.logging.Logger;

public class SapperBot extends TelegramLongPollingBot {

    private final static Logger logger = Logger.getLogger(SapperBot.class.getSimpleName());

    public void onUpdateReceived(Update update) {
        if (hasCommand(update, BotConfig.getStringProperty("sapperBot.commands.start"))){
            SendMessage message = new SendMessage().setText("SAPPER").setChatId(update.getMessage().getChatId()).setReplyMarkup(GameLogic.buildField()).enableMarkdown(true);
            sendMessage(message);
        }else if (update.hasCallbackQuery()){
            List<List<InlineKeyboardButton>> buttons = update
                    .getCallbackQuery()
                    .getMessage()
                    .getReplyMarkup()
                    .getKeyboard();

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

    private void sendMessage(SendMessage message){
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
