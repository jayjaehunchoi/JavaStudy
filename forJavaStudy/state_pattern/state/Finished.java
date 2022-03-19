package state_pattern.state;

import state_pattern.Card;
import state_pattern.Cards;

public abstract class Finished extends Started {

    public Finished(Cards cards) {
        super(cards);
    }

    @Override
    public State draw(final Card card) {
        throw new IllegalStateException();
    }

    @Override
    public State stay() {
        throw new IllegalStateException();
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public double profit(final int amount) {
        return rate() * amount;
    }

    protected abstract double rate();
}
