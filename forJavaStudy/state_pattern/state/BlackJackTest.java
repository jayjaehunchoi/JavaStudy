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

public class BlackJackTest {

    @Test
    @DisplayName("카드를 더이상 뽑을 수 없다.")
    void cannotDrawCard() {
        Cards cards = new Cards(List.of(Card.valueOf(Suit.DIAMOND, Denomination.JACK),
                Card.valueOf(Suit.DIAMOND, Denomination.ACE)));

        BlackJack blackJack = new BlackJack(cards);
        assertThatThrownBy(() -> blackJack.draw(Card.valueOf(Suit.DIAMOND, Denomination.JACK)))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("stay를 선택할 수 없다")
    void noStay() {
        Cards cards = new Cards(List.of(Card.valueOf(Suit.DIAMOND, Denomination.JACK),
                Card.valueOf(Suit.DIAMOND, Denomination.ACE)));

        BlackJack blackJack = new BlackJack(cards);
        assertThatThrownBy(blackJack::stay)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("게임이 종료됐다.")
    void finished() {
        Cards cards = new Cards(List.of(Card.valueOf(Suit.DIAMOND, Denomination.JACK),
                Card.valueOf(Suit.DIAMOND, Denomination.ACE)));

        BlackJack blackJack = new BlackJack(cards);
        assertThat(blackJack.isFinished()).isTrue();
    }

    @Test
    @DisplayName("cards 정보를 가져온다.")
    void cards() {
        Cards cards = new Cards(List.of(Card.valueOf(Suit.DIAMOND, Denomination.JACK),
                Card.valueOf(Suit.DIAMOND, Denomination.ACE)));

        BlackJack blackJack = new BlackJack(cards);
        assertThat(blackJack.cards()).isEqualTo(cards);
    }

    @Test
    @DisplayName("profit을 1.5배로 계산한다.")
    void profitException() {
        Cards cards = new Cards(List.of(Card.valueOf(Suit.DIAMOND, Denomination.JACK),
                Card.valueOf(Suit.DIAMOND, Denomination.ACE)));

        BlackJack blackJack = new BlackJack(cards);
        assertThat(blackJack.profit(100)).isEqualTo(150);
    }

}
