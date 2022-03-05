package synchronized_practice;

public class ContributionStatic {

    private static int amount = 0;

    public static synchronized void donate() {
        amount++;
    }

    public int getAmount() {
        return amount;
    }
}
