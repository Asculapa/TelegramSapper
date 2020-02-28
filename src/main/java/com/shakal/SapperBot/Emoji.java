package com.shakal.SapperBot;

import com.shakal.BotConfig;

import java.util.HashMap;

abstract class Emoji {
    static final String closedCell = BotConfig.getStringProperty("sapperBot.game.emoji.closedCell");
    static final String flag = BotConfig.getStringProperty("sapperBot.game.emoji.flag");
    static final String bomb = BotConfig.getStringProperty("sapperBot.game.emoji.bomb");
    static final String magnifier = BotConfig.getStringProperty("sapperBot.game.emoji.magnifier");
    static final HashMap<String, String> numbers = new HashMap<String, String>(){{
        put("0", BotConfig.getStringProperty("sapperBot.game.emoji.zero"));
        put("1", BotConfig.getStringProperty("sapperBot.game.emoji.one"));
        put("2", BotConfig.getStringProperty("sapperBot.game.emoji.two"));
        put("3", BotConfig.getStringProperty("sapperBot.game.emoji.three"));
        put("4", BotConfig.getStringProperty("sapperBot.game.emoji.four"));
        put("5", BotConfig.getStringProperty("sapperBot.game.emoji.five"));
        put("6", BotConfig.getStringProperty("sapperBot.game.emoji.six"));
        put("7", BotConfig.getStringProperty("sapperBot.game.emoji.seven"));
        put("8", BotConfig.getStringProperty("sapperBot.game.emoji.eight"));
        put("9", BotConfig.getStringProperty("sapperBot.game.emoji.nine"));

    }};
}
