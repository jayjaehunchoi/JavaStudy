package state_pattern.state;

import state_pattern.Cards;

public class Stay extends Finished {

    public Stay(Cards cards) {
        super(cards);
    }

    @Override
    protected double rate() {
        return 1;
    }

}
