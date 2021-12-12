package coupon.domain;

public class User {
    private String name;
    private Coupons coupons;

    public User(String name) {
        this.name = name;
        coupons = new Coupons();
    }

    public String getName() {
        return name;
    }

    public Coupons getCoupons() {
        return coupons;
    }
}
