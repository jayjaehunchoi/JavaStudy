package state_pattern.state;

import state_pattern.Cards;

public abstract class Started implements State {

    protected final Cards cards;

    public Started(Cards cards) {
        this.cards = cards;
    }

    @Override
    public Cards cards() {
        return cards;
    }
}
