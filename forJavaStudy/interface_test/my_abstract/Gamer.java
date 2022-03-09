package interface_test.my_abstract;

import java.util.ArrayList;
import java.util.List;

public abstract class Gamer {

    private List<String> cards = new ArrayList<>();

    public void addCard(String card) {
        cards.add(card);
    }

    public List<String> getResult() {
        return List.copyOf(cards);
    }

    public abstract boolean isReceivable();


}
