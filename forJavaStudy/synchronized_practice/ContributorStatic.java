package synchronized_practice;

public class ContributorStatic extends Thread {

    private final ContributionStatic contribution;
    private final String name;

    public ContributorStatic(final ContributionStatic contribution, final String name) {
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
