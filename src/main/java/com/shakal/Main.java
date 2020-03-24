package com.shakal;

import com.shakal.sapper.SapperBot;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.logging.Logger;

public class Main {

    private final static Logger logger = Logger.getLogger(Main.class.getSimpleName());

    public static void main(String[] args) {

        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        logger.info("Starting....");
        try {
            telegramBotsApi.registerBot(new SapperBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        logger.info("Bot successfully launched");

    }
}
