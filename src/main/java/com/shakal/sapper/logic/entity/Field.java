package com.shakal.sapper.logic.entity;

import com.shakal.BotConfig;

public final class Field {
    private int height;
    private int width;
    private int bombs;

    public enum Level{
     EASY, NORMAL, HARD
    }

    public Field(Level level){
        setAttributes(level.name().toLowerCase());
    }


    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getBombs() {
        return bombs;
    }

    private void setAttributes(String level){
        height = BotConfig.getIntProperty("sapperBot.level." + level + ".height");
        width = BotConfig.getIntProperty("sapperBot.level." + level + ".width");
        bombs = BotConfig.getIntProperty("sapperBot.level." + level + ".bombs");
    }
}
