package state_pattern.state;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import state_pattern.Card;
import state_pattern.Cards;
import state_pattern.Denomination;
import state_pattern.Suit;

class HitTest {

    @Test
    @DisplayName("카드를 뽑아 21보다 낮으면 Hit를 유지한다")
    void drawCardHit() {
        Cards cards = new Cards(List.of(Card.valueOf(Suit.DIAMOND, Denomination.JACK),
                Card.valueOf(Suit.DIAMOND, Denomination.TWO)));

        Hit hit = new Hit(cards);
        assertThat(hit.draw(Card.valueOf(Suit.DIAMOND, Denomination.FOUR))).isInstanceOf(Hit.class);
    }

    @Test
    @DisplayName("카드를 뽑아 21이 넘으면 Bust로 변한다.")
    void drawCardBust() {
        Cards cards = new Cards(List.of(Card.valueOf(Suit.DIAMOND, Denomination.JACK),
                Card.valueOf(Suit.DIAMOND, Denomination.TWO)));

        Hit hit = new Hit(cards);
        assertThat(hit.draw(Card.valueOf(Suit.DIAMOND, Denomination.TEN))).isInstanceOf(Bust.class);
    }

    @Test
    @DisplayName("stay하면 Stay로 변한다.")
    void stay() {
        Cards cards = new Cards(List.of(Card.valueOf(Suit.DIAMOND, Denomination.JACK),
                Card.valueOf(Suit.DIAMOND, Denomination.TWO)));

        Hit hit = new Hit(cards);
        assertThat(hit.stay()).isInstanceOf(Stay.class);
    }

    @Test
    @DisplayName("게임이 종료되지 않았다.")
    void running() {
        Cards cards = new Cards(List.of(Card.valueOf(Suit.DIAMOND, Denomination.JACK),
                Card.valueOf(Suit.DIAMOND, Denomination.TWO)));

        Hit hit = new Hit(cards);
        assertThat(hit.isFinished()).isFalse();
    }

    @Test
    @DisplayName("profit을 계산할 수 없다")
    void profitException() {
        Cards cards = new Cards(List.of(Card.valueOf(Suit.DIAMOND, Denomination.JACK),
                Card.valueOf(Suit.DIAMOND, Denomination.TWO)));

        Hit hit = new Hit(cards);
        assertThatThrownBy(() -> hit.profit(100)).isInstanceOf(IllegalStateException.class);
    }

}