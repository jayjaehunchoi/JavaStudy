package state_pattern;

import java.util.HashMap;
import java.util.Map;

public class Card {

    private static Map<String, Card> CACHE = new HashMap<>();

    private final Suit suit;
    private final Denomination denomination;

    private Card(final Suit suit, final Denomination denomination) {
        this.suit = suit;
        this.denomination = denomination;
    }

    public static Card valueOf(final Suit suit, final Denomination denomination) {
        return CACHE.computeIfAbsent(denomination.getPoint() + suit.getName(), (key) -> new Card(suit, denomination));
    }

    public int point() {
        return denomination.getPoint();
    }

    public boolean isAce() {
        return denomination == Denomination.ACE;
    }

}
