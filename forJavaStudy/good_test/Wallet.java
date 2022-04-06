package good_test;

import java.util.ArrayList;
import java.util.List;

public class Wallet {

    private int money;
    private List<String> cards;

    public Wallet() {
        money = 0;
        cards = new ArrayList<>();
    }

    public void addMoney(int money) {
        this.money += money;
    }

    public void pay(int money) {
        if (money > this.money) {
            throw new IllegalArgumentException("돈이 부족합니다.");
        }
        this.money -= money;
    }

    public void addCard(String card) {
        cards.add(card);
    }

    public void throwCard(String card) {
        if (!cards.contains(card)) {
            throw new IllegalArgumentException("카드가 없는데 어떻게 버려");
        }
        cards.remove(card);
    }

    public int getMoney() {
        return money;
    }

    public List<String> getCards() {
        return cards;
    }
}
