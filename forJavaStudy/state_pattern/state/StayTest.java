package state_pattern.state;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import state_pattern.Card;
import state_pattern.Cards;
import state_pattern.Denomination;
import state_pattern.Suit;

class StayTest {

    @Test
    @DisplayName("profit을 1.0배로 계산한다.")
    void profitException() {
        Cards cards = new Cards(List.of(Card.valueOf(Suit.DIAMOND, Denomination.JACK),
                Card.valueOf(Suit.DIAMOND, Denomination.TEN)));

        Stay stay = new Stay(cards);
        assertThat(stay.profit(100)).isEqualTo(100);
    }

}