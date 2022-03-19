package state_pattern;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import state_pattern.state.BlackJack;
import state_pattern.state.Hit;

class GameTest {

    @Test
    @DisplayName("게임을 시작하고 카드 두장의 합이 21이면 블랙잭이다.")
    void startGameBlackJack() {
        Game game = new Game();

        List<Card> cards = List.of(Card.valueOf(Suit.DIAMOND, Denomination.JACK),
                Card.valueOf(Suit.DIAMOND, Denomination.ACE));


        assertThat(game.start(new Cards(cards))).isInstanceOf(BlackJack.class);
    }

    @Test
    @DisplayName("게임을 시작하고 카드 두장의 합이 21이 아닐경우 히트이다.")
    void startGameHit() {
        Game game = new Game();
        List<Card> cards = List.of(Card.valueOf(Suit.DIAMOND, Denomination.JACK),
                Card.valueOf(Suit.DIAMOND, Denomination.TEN));

        assertThat(game.start(new Cards(cards))).isInstanceOf(Hit.class);

    }

}
