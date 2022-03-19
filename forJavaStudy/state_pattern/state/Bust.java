package state_pattern.state;

import state_pattern.Cards;

public class Bust extends Finished {

    public Bust(Cards cards) {
        super(cards);
    }

    @Override
    protected double rate() {
        return -1;
    }

}
