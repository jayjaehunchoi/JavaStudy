package state_pattern.state;

import state_pattern.Card;
import state_pattern.Cards;

public class Hit extends Running {

    public Hit(Cards cards) {
        super(cards);
    }

    @Override
    public State draw(final Card card) {
        Cards cards = this.cards.add(card);
        if (cards.sum() < 21) {
            return new Hit(cards);
        }
        return new Bust(cards);
    }

    @Override
    public State stay() {
        return new Stay(cards);
    }

}
