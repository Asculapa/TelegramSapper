package com.shakal.SapperBot;

import com.shakal.BotConfig;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;
import java.util.logging.Logger;

public class SapperBot extends TelegramLongPollingBot {

    private final static Logger logger = Logger.getLogger(SapperBot.class.getSimpleName());
    private final static String ERROR_MESSAGE = BotConfig.getStringProperty("sapperBot.message.error");

    public void onUpdateReceived(final Update update) {
        if (hasCommand(update, BotConfig.getStringProperty("sapperBot.commands.start"))) {
            SendMessage message = new SendMessage().setText("SAPPER").setChatId(update.getMessage().getChatId()).setReplyMarkup(GameLogic.buildEmptyField()).enableMarkdown(true);
            sendMessage(message);
        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            EditMessageText editMessageText;
            logger.info("Get callback query. Data = " + callbackQuery.getData());
            try {
                editMessageText = GameLogic.analyze(callbackQuery.getMessage(), callbackQuery.getData()).get();
            }catch (Exception ex){
                ex.printStackTrace();
                editMessageText = makeErrorEditMessage(callbackQuery.getMessage());
            }
            sendMessage(editMessageText);
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

    private EditMessageText makeErrorEditMessage(Message message){
        return new EditMessageText()
                .setText(ERROR_MESSAGE)
                .setChatId(message.getChatId())
                .setMessageId(message.getMessageId());
    }

}
