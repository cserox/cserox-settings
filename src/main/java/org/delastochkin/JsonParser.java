package org.delastochkin;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Stack;

public class JsonParser {

    private final String input;
    private final StringBuilder currentWord;
    private final Settings rootSettings;
    private final Stack<Settings> settings;
    private final EscapingState escaping;
    private ParserState state;
    private String currentSettingName;

    public JsonParser(String input) {
        this.input = input;
        this.currentWord = new StringBuilder();
        this.state = this::readingSettingName;
        this.escaping = new EscapingState();
        this.rootSettings = new Settings();
        this.settings = new Stack<>();
        this.settings.push(rootSettings);
    }

    public Settings getSettings() throws IOException {
        Reader inputReader = new StringReader(input);
        int currentSymbol;
        while ((currentSymbol = inputReader.read()) != -1) {
            state = state.handleCharacter((char) currentSymbol);
        }
        return rootSettings;
    }

    private ParserState readingSettingName(Character character) {
        if (character != ':') {
            return defaultAction(character);
        }
        currentSettingName = getAndClear(currentWord);
        return this::readingSettingValue;
    }

    private ParserState readingSettingValue(Character character) {
        Settings currentSettings = settings.peek();
        switch(character) {
            case '{':
                settings.push(currentSettings.newElement(currentSettingName));
                break;
            case '}':
                settings.pop();
            case ',':
                currentSettings.setAttribute(currentSettingName, getAndClear(currentWord));
                break;
            default:
                return defaultAction(character);
        }
        currentSettingName = null;
        return this::readingSettingName;
    }

    private ParserState defaultAction(Character character) {
        switch(character) {
            case '\"':
                escaping.returnState = state;
                return escaping;
            case '}':
                settings.pop();
            case '{':
            case ' ':
            case '\t':
            case '\n':
            case ',':
                break;
            default:
                currentWord.append(character);
        }
        return state;
    }

    private String getAndClear(StringBuilder builder) {
        String value = builder.toString();
        builder.setLength(0);
        return value;
    }

    private class EscapingState implements ParserState {
        ParserState returnState;

        @Override
        public ParserState handleCharacter(Character character) {
            if(character == '\"') {
                return returnState;
            }
            currentWord.append(character);
            return this;
        }
    }
}

