package state_pattern.state;

import state_pattern.Cards;

public class BlackJack extends Finished {

    public BlackJack(Cards cards) {
        super(cards);
    }

    @Override
    protected double rate() {
        return 1.5;
    }
}
