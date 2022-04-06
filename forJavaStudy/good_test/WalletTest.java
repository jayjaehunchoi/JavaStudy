package good_test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class WalletTest {

    @Test
    void addMoney() {
        Wallet wallet = createDefaultWallet(9000);
        wallet.addMoney(100);
        assertThat(wallet.getMoney()).isEqualTo(9100);
    }

    @Test
    void pay() {
        Wallet wallet = createDefaultWallet(9000);
        wallet.pay(1000);
        assertThat(wallet.getMoney()).isEqualTo(8000);
    }

    @Test
    void payException() {
        Wallet wallet = createDefaultWallet(9000);
        assertThatThrownBy(() -> wallet.pay(100000))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("돈이 부족합니다.");
    }

    private Wallet createDefaultWallet(int money) {
        Wallet wallet = new Wallet();
        wallet.addMoney(money);
        return wallet;
    }

/*
    @Test
    @Disabled
    void addCard() {
        wallet.addCard("신한카드");
        assertThat(wallet.getCards()).hasSize(2);
    }

    @Test
    void throwCard() {
        wallet.throwCard("토스카드");
        assertThat(wallet.getCards()).isEmpty();
    }

    @Test
    void throwCardExeption() {
        assertThatThrownBy(() -> wallet.throwCard("신한카드"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("카드가 없는데 어떻게 버려");
    }*/

}
