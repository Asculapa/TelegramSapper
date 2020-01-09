package com.shakal.bots.SuppeBot;


import com.shakal.BotConfig;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.logging.Logger;

public class SupperBot extends TelegramLongPollingBot {

    private final static Logger logger = Logger.getLogger(SupperBot.class.getSimpleName());

    public void onUpdateReceived(Update update) {
        if (hasCommand(update, BotConfig.getProperty("supperBot.commands.start"))){
            logger.info("=)");
        }
    }

    public String getBotUsername() {
        return BotConfig.getProperty("supperBot.name");
    }

    public String getBotToken() {
        return BotConfig.getProperty("supperBot.token");
    }

    private boolean hasCommand(Update update, String command) {
        Message message = update.getMessage();
        return update.hasMessage() &&
                message.hasText() &&
                message.getText().equals(command);
    }
}
