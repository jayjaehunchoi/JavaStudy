package synchronized_practice;

public class Contributor extends Thread {

    private final Contribution contribution;
    private final String name;

    public Contributor (final Contribution contribution, final String name) {
        this.contribution = contribution;
        this.name = name;
    }

    public void run() {
        for (int i = 0; i < 1000; i++) {
            contribution.donate();
        }

        System.out.printf("%s total = %d\n", name, contribution.getAmount());
    }
}
