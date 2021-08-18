package io.github.ititus.tictactoe.lib.player;

public record AICapabilities(
        boolean win,
        boolean block,
        boolean fork,
        boolean blockFork
) {

    public static final int RANDOM = 0;
    public static final int WIN = 1;
    public static final int BLOCK = 2;
    public static final int FORK = 3;
    public static final int PERFECT = 4;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private int level = RANDOM;

        private Builder() {
        }

        public Builder level(int level) {
            this.level = level;
            return this;
        }

        public AICapabilities build() {
            return new AICapabilities(level >= WIN, level >= BLOCK, level >= FORK, level >= PERFECT);
        }
    }
}
