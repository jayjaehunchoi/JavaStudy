package state_pattern.state;

import state_pattern.Cards;

public abstract class Running extends Started {

    public Running(Cards cards) {
        super(cards);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public double profit(final int amount) {
        throw new IllegalStateException();
    }
}
