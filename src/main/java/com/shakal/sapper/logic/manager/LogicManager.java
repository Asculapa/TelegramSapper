package com.shakal.sapper.logic.manager;

import com.shakal.BotConfig;
import com.shakal.EnumUtils;
import com.shakal.sapper.Emoji;
import com.shakal.sapper.logic.entity.Cell;
import com.shakal.sapper.logic.entity.Field;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;
import java.util.logging.Logger;


class LogicManager {

    private final static Logger logger = Logger.getLogger(LogicManager.class.getSimpleName());

    private static String messageText;
    private static Message message;
    private static List<List<InlineKeyboardButton>> keyboard;
    private static InlineKeyboardButton toolButton;
    private static User user;
    private static int bombCount;

    private enum Command {
        START, CHANGE_TOOL, DEFEAT, NULL, OPEN
    }

    static InlineKeyboardMarkup buildEmptyField(Field field) {

        List<List<InlineKeyboardButton>> inlineKeyboard = new ArrayList<>();
        for (int y = 0; y < field.getHeight(); y++) {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            for (int x = 0; x < field.getWidth(); x++) {
                rowInline.add(new InlineKeyboardButton()
                        .setText(Emoji.closedCell)
                        .setCallbackData(y + " " + x + " " + Command.START.name() + " " + field.getBombs()));
            }
            inlineKeyboard.add(rowInline);
        }

        inlineKeyboard.add(Collections.singletonList(new InlineKeyboardButton()
                .setText(Emoji.magnifier)
                .setCallbackData(Command.CHANGE_TOOL.name() + " " + field.getBombs()))); //Review
        return new InlineKeyboardMarkup().setKeyboard(inlineKeyboard);
    }


    static EditMessageText analyze(Message message, String callbackQueryData, User user) {
        if (isOpened(callbackQueryData)) {
            changeMessageText(BotConfig.getStringProperty("sapperBot.message.clickOnOpenedCell"));
        } else {
            initVars(message, user);

            if (isTool(callbackQueryData)) {
                changeTool();
            } else {
                int y = getY(callbackQueryData);
                int x = getX(callbackQueryData);

                if (flagSelected()) {
                    changeIcon(y, x);// Set number of flags + check whole field if count == 0
                } else {
                    Command command = getCommand(callbackQueryData);
                    List<List<InlineKeyboardButton>> gameKeyboard = getGameKeyboard();
                    executeCommand(command, gameKeyboard, y, x);
                }
            }
        }

        return new EditMessageText()
                .setMessageId(message.getMessageId())
                .setChatId(message.getChatId())
                .setText(messageText)
                .setReplyMarkup(message.getReplyMarkup());
    }

    private static void changeMessageText(String newMessageText) {
        String invisibleText = "\u200E";

        if (messageText.contains(invisibleText)) {
            messageText = messageText.replace(invisibleText, "");
        } else {
            messageText = messageText.equals(newMessageText) ? messageText + invisibleText : newMessageText;
        }
    }

    private static void initVars(Message message, User user) {
        LogicManager.message = message;
        LogicManager.keyboard = message.getReplyMarkup().getKeyboard();
        LogicManager.messageText = message.getText();
        LogicManager.toolButton = getToolButton();
        LogicManager.user = user;
    }

    private static int getX(String callbackQueryData) {
        String x = callbackQueryData.split(" ")[1];
        return Integer.valueOf(x);
    }

    private static void changeIcon(int y, int x) {
        String icon = keyboard.get(y).get(x).getText();
        InlineKeyboardButton toolButton = getToolButton();
        int flagCount = getFlagCount(toolButton.getCallbackData());

        String newIcon = Emoji.closedCell;
        if (icon.equals(Emoji.flag)) {
            flagCount++;
        } else {
            newIcon = Emoji.flag;
            flagCount--;
        }

        if (flagCount >= 0) {
            setFlagCount(flagCount);
        } else {
            changeMessageText(BotConfig.getStringProperty("sapperBot.message.outOfFlags"));
            return;
        }

        keyboard.get(y).get(x).setText(newIcon);

        if (isWin(keyboard)) {
            win();
        }

    }

    private static void incrementCountOfFlags() {
        setFlagCount(getFlagCount(toolButton.getCallbackData()) + 1);
    }


    private static void win() {
        removeKeyboard();
        messageText = BotConfig.getStringProperty("sapperBot.message.win");
        logger.info(getUserName() + " won a game!");
    }

    private static void removeKeyboard() {
        message.getReplyMarkup().getKeyboard().clear();
    }

    private static boolean isWin(List<List<InlineKeyboardButton>> keyboard) {
        for (List<InlineKeyboardButton> line : keyboard) {
            for (InlineKeyboardButton button : line) {
                if (button.getText().equals(Emoji.closedCell)) {
                    return false;
                }
            }
        }

        return true;
    }

    private static boolean flagSelected() {
        return toolButton.getText().equals(Emoji.flag);
    }

    private static void changeTool() {
        String tool = toolButton.getText();
        toolButton.setText(tool.equals(Emoji.flag) ? Emoji.magnifier : Emoji.flag);
    }

    private static int getFlagCount(String callbackQueryData) {
        return Integer.valueOf(callbackQueryData.split(" ")[1]);
    }

    private static void setFlagCount(int value) {
        toolButton.setCallbackData(toolButton.getCallbackData().split(" ")[0] + " " + value);
    }

    private static boolean isTool(String callbackQueryData) {
        return callbackQueryData.split(" ")[0].equals(Command.CHANGE_TOOL.name());
    }

    private static InlineKeyboardButton getToolButton() {
        return keyboard.get(keyboard.size() - 1).get(0);
    }

    private static int getY(String callbackQueryData) {
        String y = callbackQueryData.split(" ")[0];
        return Integer.valueOf(y);
    }

    private static String getValue(String callbackQueryData) {
        return callbackQueryData.split(" ")[3];
    }

    private static boolean isOpened(String callbackQueryData) {
        return callbackQueryData.equals(Command.NULL.name());
    }

    private static List<List<InlineKeyboardButton>> getGameKeyboard() {
        List<List<InlineKeyboardButton>> gameKeyboard = new ArrayList<>(keyboard);
        gameKeyboard.remove(gameKeyboard.size() - 1);

        return gameKeyboard;
    }

    private static void executeCommand(Command comm, List<List<InlineKeyboardButton>> gameKeyboard, int y, int x) {
        switch (comm) {
            case START:
                startGame(y, x, gameKeyboard);
                openCell(y, x, gameKeyboard);
                break;
            case DEFEAT:
                endGame(gameKeyboard);
                break;
            case OPEN:
                openCell(y, x, gameKeyboard); // ?only value?
                break;
        }
    }

    private static void endGame(List<List<InlineKeyboardButton>> gameKeyboard) {
        disableKeyboard(gameKeyboard);
        messageText = BotConfig.getStringProperty("sapperBot.message.lose");
        logger.info(getUserName() + " lost the game =(");
    }

    private static String getUserName(){
        return user.getUserName() + " (" + user.getFirstName() + " " + user.getLastName() + ")";
    }

    private static void openCell(int y, int x, List<List<InlineKeyboardButton>> gameKeyboard) {
        InlineKeyboardButton cell = gameKeyboard.get(y).get(x);
        String callbackQueryData = cell.getCallbackData();

        if (isOpened(callbackQueryData)) {
            return;
        }

        if (hasFlag(cell.getText())) {
            incrementCountOfFlags();
        }

        String value = getValue(callbackQueryData);
        gameKeyboard.get(y).get(x).setCallbackData(Command.NULL.name());
        gameKeyboard.get(y).get(x).setText(Emoji.numbers.get(value));

        if (isWin(gameKeyboard)) {
            win();
            return;
        }

        if (value.equals("0")) {
            getCellsAround(y, x, getHeight(gameKeyboard), getWidth(gameKeyboard))
                    .forEach(k -> {
                        String buttonText = gameKeyboard.get(k.getY()).get(k.getX()).getText();
                        if (!hasFlag(buttonText)) {
                            openCell(k.getY(), k.getX(), gameKeyboard);
                        }
                    });
        }

    }

    private static boolean hasFlag(String buttonText) {
        return buttonText.equals(Emoji.flag);
    }


    private static void startGame(int y, int x, List<List<InlineKeyboardButton>> gameKeyboard) {
        fillGameField(y, x, gameKeyboard);
    }

    private static Command getCommand(String callbackQueryData) {
        return EnumUtils.lookupMap(Command.class, Enum::name).apply(callbackQueryData.split(" ")[2]);
    }

    private static void setCommand(int y, int x, List<List<InlineKeyboardButton>> gameKeyboard, Command command, Integer value) {
        String callbackQueryData = gameKeyboard.get(y).get(x).getCallbackData();
        String[] elements = callbackQueryData.split(" ");
        assert elements.length >= 4;
        elements[2] = command != null ? command.name() : elements[2];
        elements[3] = value != null ? String.valueOf(value) : elements[3];
        String newCallbackQueryData = String.join(" ", elements);
        gameKeyboard.get(y).get(x).setCallbackData(newCallbackQueryData);
    }

    private static void fillGameField(int y1, int x1, List<List<InlineKeyboardButton>> gameKeyboard) {
        int keyboardHeight = getHeight(gameKeyboard);
        int keyboardWidth = getWidth(gameKeyboard);
        LogicManager.bombCount = getBombCount();

        List<Cell> exclusiveCells = getCellsAroundAndCenter(y1, x1, keyboardHeight, keyboardWidth);

        for (int y = 0; y < keyboardHeight; y++) {
            for (int x = 0; x < keyboardWidth; x++) {
                setCommand(y, x, gameKeyboard, Command.OPEN, 0);
            }
        }

        setBombs(gameKeyboard, exclusiveCells);
    }

    private static int getBombCount() {
        return Integer.valueOf(keyboard.get(0).get(0).getCallbackData().split(" ")[3]);
    }

    private static List<Cell> getCellsAroundAndCenter(int y1, int x1, int gameFieldHeight, int gameFieldWidth) {
        List<Cell> result = new ArrayList<>();
        for (int y = y1 - 1; y <= y1 + 1; y++) {
            for (int x = x1 - 1; x <= x1 + 1; x++) {
                if (y < 0 || y >= gameFieldHeight) {
                    continue;
                }

                if (x < 0 || x >= gameFieldWidth) {
                    continue;
                }

                result.add(Cell.of(y, x));
            }
        }

        return result;
    }

    private static int getHeight(List<List<InlineKeyboardButton>> gameKeyboard) {
        return gameKeyboard.size();
    }

    private static int getWidth(List<List<InlineKeyboardButton>> gameKeyboard) {
        return gameKeyboard.get(0).size();
    }

    private static void setBombs(List<List<InlineKeyboardButton>> gameField, List<Cell> exclusiveCell) {
        int gameFieldHeight = getHeight(gameField);
        int gameFieldWidth = getWidth(gameField);
        Map<Cell, Integer> cellsAround = new HashMap<>();

        for (Cell cell : getBombCells(gameFieldHeight, gameFieldWidth, exclusiveCell)) {
            setCommand(cell.getY(), cell.getX(), gameField, Command.DEFEAT, null);
            List<Cell> cells = getCellsAround(cell.getY(), cell.getX(), gameFieldHeight, gameFieldWidth);
            incrementPut(cellsAround, cells);
        }

        setAroundCells(cellsAround, gameField);
    }

    private static List<Cell> getBombCells(int gameFieldHeight, int gameFieldWidth, List<Cell> exclusiveCells) {
        List<Cell> cells = getAppropriateCells(gameFieldHeight, gameFieldWidth, exclusiveCells);
        Collections.shuffle(cells);
        return cells.subList(0, bombCount);
    }

    private static List<Cell> getAppropriateCells(int gameFieldHeight, int gameFieldWidth, List<Cell> exclusiveCells) {
        List<Cell> cells = new ArrayList<>();
        for (int y = 0; y < gameFieldHeight; y++) {
            for (int x = 0; x < gameFieldWidth; x++) {
                if (exclusiveCells.contains(Cell.of(y, x))) {
                    continue;
                }
                cells.add(Cell.of(y, x));
            }
        }
        return cells;
    }

    private static <T> void incrementPut(Map<T, Integer> map, List<T> keys) {
        for (T key : keys) {
            Integer mapValue = map.get(key);
            if (mapValue == null) {
                map.put(key, 1);
            } else {
                map.put(key, ++mapValue);
            }
        }
    }

    private static List<Cell> getCellsAround(int y, int x, int gameFieldHeight, int gameFieldWidth) {
        List<Cell> aroundCells = getCellsAroundAndCenter(y, x, gameFieldHeight, gameFieldWidth);
        aroundCells.remove(Cell.of(y, x));
        return aroundCells;
    }

    private static void setAroundCells(Map<Cell, Integer> cellsAround, List<List<InlineKeyboardButton>> gameField) {
        cellsAround.forEach((k, v) -> setCommand(k.getY(), k.getX(), gameField, null, v));
    }

    private static void disableKeyboard(List<List<InlineKeyboardButton>> gameKeyboard) {
        for (List<InlineKeyboardButton> y : gameKeyboard) {
            for (InlineKeyboardButton x : y) {
                if (isOpened(x.getCallbackData())) {
                    continue;
                }

                if (getCommand(x.getCallbackData()) == Command.DEFEAT) {
                    x.setText(Emoji.bomb);
                }
                x.setCallbackData(Command.NULL.name());
            }
        }
        toolButton.setCallbackData(Command.NULL.name());
    }
}
