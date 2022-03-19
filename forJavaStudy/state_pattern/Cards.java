package state_pattern;

import java.util.ArrayList;
import java.util.List;

public class Cards {

    private final List<Card> values;

    public Cards(final List<Card> values) {
        this.values = values;
    }

    public int sum() {
        return values.stream()
                .mapToInt(Card::point)
                .sum() + adjustAce();
    }

    private int adjustAce() {
        if (values.stream()
                .anyMatch(Card::isAce)) {
            return 10;
        }
        return 0;
    }

    public Cards add(final Card card) {
        List<Card> cards = new ArrayList<>(values);
        cards.add(card);
        return new Cards(cards);
    }
}
