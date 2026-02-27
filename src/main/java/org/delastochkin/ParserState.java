package org.delastochkin;

public interface ParserState {
    ParserState handleCharacter(Character character);
}
