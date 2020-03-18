package com.shakal.sapper;

import com.shakal.BotConfig;

import java.util.HashMap;

public abstract class Emoji {
    public static final String closedCell = BotConfig.getStringProperty("sapperBot.game.emoji.closedCell");
    public static final String flag = BotConfig.getStringProperty("sapperBot.game.emoji.flag");
    public static final String bomb = BotConfig.getStringProperty("sapperBot.game.emoji.bomb");
    public static final String magnifier = BotConfig.getStringProperty("sapperBot.game.emoji.magnifier");
    public static final HashMap<String, String> numbers = new HashMap<String, String>(){{
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
