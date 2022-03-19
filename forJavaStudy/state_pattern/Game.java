package state_pattern;

import state_pattern.state.BlackJack;
import state_pattern.state.Hit;
import state_pattern.state.State;

public class Game {


    public State start(final Cards cards) {
        if (cards.sum() == 21) {
            return new BlackJack(cards);
        }
        return new Hit(cards);
    }
}
