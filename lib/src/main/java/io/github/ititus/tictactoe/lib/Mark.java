package io.github.ititus.tictactoe.lib;

import java.util.List;

public enum Mark {

    NONE, CROSS, CIRCLE;

    public static final List<Mark> VALUES = List.of(values());

    public boolean empty() {
        return this == NONE;
    }

    public boolean notEmpty() {
        return this != NONE;
    }

    public Mark enemy() {
        return switch (this) {
            case CROSS -> CIRCLE;
            case CIRCLE -> CROSS;
            default -> throw new RuntimeException("none has no enemy");
        };
    }
}
