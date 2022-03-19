package state_pattern.state;

import state_pattern.Card;
import state_pattern.Cards;

public interface State {

    State draw(final Card card);

    State stay();

    boolean isFinished();

    Cards cards();

    double profit(int amount);
}
