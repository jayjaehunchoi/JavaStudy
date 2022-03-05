package synchronized_practice;

import java.util.concurrent.ConcurrentHashMap;

public class Contribute {

    public static void donateEachContributorToEachContribution() {
        Contributor[] contributors = new Contributor[10];

        for (int i = 0; i < 10; i++) {
            Contribution group = new Contribution();
            contributors[i] = new Contributor(group, "contributor " + i);
        }

        for (int i = 0; i < 10; i++) {
            contributors[i].start();
        }
    }

    public static void donateEachContributorToOneContribution() {
        Contributor[] contributors = new Contributor[10];
        Contribution group = new Contribution();
        for (int i = 0; i < 10; i++) {
            contributors[i] = new Contributor(group, "contributor " + i);
        }

        for (int i = 0; i < 10; i++) {
            contributors[i].start();
        }
    }

    public static void donateEachContributorToEachContributionStatic() {
        ContributorStatic[] contributors = new ContributorStatic[10];

        for (int i = 0; i < 10; i++) {
            ContributionStatic group = new ContributionStatic();
            contributors[i] = new ContributorStatic(group, "contributor " + i);
        }

        for (int i = 0; i < 10; i++) {
            contributors[i].start();
        }
    }

    public static void main(String[] args) {
        //donateEachContributorToEachContribution();
        //donateEachContributorToOneContribution();
        donateEachContributorToEachContributionStatic();

    }
}
