package io.github.ititus.tictactoe.lib;

import java.util.List;

import static io.github.ititus.tictactoe.lib.board.Board.SIZE;

public enum Pos {

    TOP_LEFT, TOP, TOP_RIGHT,
    LEFT, CENTER, RIGHT,
    BOTTOM_LEFT, BOTTOM, BOTTOM_RIGHT;

    public static final List<Pos> ALL = List.of(TOP_LEFT, TOP, TOP_RIGHT, LEFT, CENTER, RIGHT, BOTTOM_LEFT, BOTTOM, BOTTOM_RIGHT);
    public static final List<Pos> CORNERS = List.of(TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT);
    public static final List<Pos> SIDES = List.of(TOP, LEFT, RIGHT, BOTTOM);

    public static final List<Pos> HORIZONTAL_1 = List.of(TOP_LEFT, TOP, TOP_RIGHT);
    public static final List<Pos> HORIZONTAL_2 = List.of(LEFT, CENTER, RIGHT);
    public static final List<Pos> HORIZONTAL_3 = List.of(BOTTOM_LEFT, BOTTOM, BOTTOM_RIGHT);
    public static final List<List<Pos>> ALL_HORIZONTALS = List.of(HORIZONTAL_1, HORIZONTAL_2, HORIZONTAL_3);
    public static final List<Pos> VERTICAL_1 = List.of(TOP_LEFT, LEFT, BOTTOM_LEFT);
    public static final List<Pos> VERTICAL_2 = List.of(TOP, CENTER, BOTTOM);
    public static final List<Pos> VERTICAL_3 = List.of(TOP_RIGHT, RIGHT, BOTTOM_RIGHT);
    public static final List<Pos> DIAGONAL_1 = List.of(TOP_LEFT, CENTER, BOTTOM_RIGHT);
    public static final List<Pos> DIAGONAL_2 = List.of(TOP_RIGHT, CENTER, BOTTOM_LEFT);
    public static final List<List<Pos>> LINES = List.of(HORIZONTAL_1, HORIZONTAL_2, HORIZONTAL_3, VERTICAL_1, VERTICAL_2, VERTICAL_3, DIAGONAL_1, DIAGONAL_2);

    public static Pos of(int index) {
        if (index < 0 || index >= SIZE * SIZE) {
            throw new IllegalArgumentException();
        }

        return ALL.get(index);
    }

    public static Pos of(int x, int y) {
        if (x < 0 || x >= SIZE || y < 0 || y >= SIZE) {
            throw new IllegalArgumentException();
        }

        return of(x + y * SIZE);
    }

    public int x() {
        return index() % SIZE;
    }

    public int y() {
        return index() / SIZE;
    }

    public int index() {
        return ordinal();
    }

    public boolean isCenter() {
        return this == CENTER;
    }

    public boolean isCorner() {
        return this == TOP_LEFT || this == TOP_RIGHT || this == BOTTOM_LEFT || this == BOTTOM_RIGHT;
    }

    public boolean isSide() {
        return this == TOP || this == LEFT || this == RIGHT || this == BOTTOM;
    }

    public Pos rotateCW() {
        return switch (this) {
            case TOP_LEFT -> TOP_RIGHT;
            case TOP -> RIGHT;
            case TOP_RIGHT -> BOTTOM_RIGHT;
            case LEFT -> TOP;
            case CENTER -> CENTER;
            case RIGHT -> BOTTOM;
            case BOTTOM_LEFT -> TOP_LEFT;
            case BOTTOM -> LEFT;
            case BOTTOM_RIGHT -> BOTTOM_LEFT;
        };
    }

    public Pos rotateCCW() {
        return switch (this) {
            case TOP_LEFT -> BOTTOM_LEFT;
            case TOP -> LEFT;
            case TOP_RIGHT -> TOP_LEFT;
            case LEFT -> BOTTOM;
            case CENTER -> CENTER;
            case RIGHT -> TOP;
            case BOTTOM_LEFT -> BOTTOM_RIGHT;
            case BOTTOM -> RIGHT;
            case BOTTOM_RIGHT -> TOP_RIGHT;
        };
    }

    public Pos mirrorCenter() {
        return rotateCCW().rotateCCW();
    }

    public Pos mirrorX() {
        return switch (this) {
            case TOP_LEFT -> BOTTOM_LEFT;
            case TOP -> BOTTOM;
            case TOP_RIGHT -> BOTTOM_RIGHT;
            case LEFT -> LEFT;
            case CENTER -> CENTER;
            case RIGHT -> RIGHT;
            case BOTTOM_LEFT -> TOP_LEFT;
            case BOTTOM -> TOP;
            case BOTTOM_RIGHT -> TOP_RIGHT;
        };
    }

    public Pos mirrorY() {
        return switch (this) {
            case TOP_LEFT -> TOP_RIGHT;
            case TOP -> TOP;
            case TOP_RIGHT -> TOP_LEFT;
            case LEFT -> RIGHT;
            case CENTER -> CENTER;
            case RIGHT -> LEFT;
            case BOTTOM_LEFT -> BOTTOM_RIGHT;
            case BOTTOM -> BOTTOM;
            case BOTTOM_RIGHT -> BOTTOM_LEFT;
        };
    }

    public Pos mirrorDiag1() {
        return mirrorY().rotateCCW();
    }

    public Pos mirrorDiag2() {
        return mirrorX().rotateCCW();
    }
}
