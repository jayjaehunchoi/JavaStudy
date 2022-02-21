package low_coupling_high_cohension;

public class Main {

    public static void main(String[] args) {
        Company developerCompany = new Company(new Developer());
        developerCompany.work();
    }
}
