package state_pattern;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CardTest {

    @Test
    @DisplayName("카드를 생성한다.")
    void constructor() {
        assertThat(Card.valueOf(Suit.DIAMOND, Denomination.EIGHT)).isInstanceOf(Card.class);
    }


}
