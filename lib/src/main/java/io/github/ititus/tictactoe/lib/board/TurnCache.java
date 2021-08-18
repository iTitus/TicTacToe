package io.github.ititus.tictactoe.lib.board;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

abstract class TurnCache {

    protected int cachedTurn;

    private TurnCache() {
        this.cachedTurn = -1;
    }

    static class Obj<T> extends TurnCache {

        private final Supplier<? extends T> supplier;
        private T value;

        Obj(Supplier<? extends T> supplier) {
            this.supplier = supplier;
        }

        T get(int turn) {
            if (cachedTurn != turn) {
                value = supplier.get();
                cachedTurn = turn;
            }

            return value;
        }
    }

    static class Int extends TurnCache {

        private final IntSupplier supplier;
        private int value;

        Int(IntSupplier supplier) {
            this.supplier = supplier;
        }

        int get(int turn) {
            if (cachedTurn != turn) {
                value = supplier.getAsInt();
                cachedTurn = turn;
            }

            return value;
        }
    }
}
