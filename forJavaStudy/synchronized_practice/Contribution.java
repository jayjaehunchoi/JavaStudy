package synchronized_practice;

public class Contribution {

    private int amount = 0;

    public synchronized void donate() {
        amount++;
    }

    public int getAmount() {
        return amount;
    }
}
