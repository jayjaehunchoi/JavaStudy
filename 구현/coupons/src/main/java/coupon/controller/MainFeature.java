package coupon.controller;

import coupon.View;
import coupon.domain.Coupon;
import coupon.domain.User;

import java.util.Arrays;
import java.util.function.Consumer;

public enum MainFeature {
    CREATE_COUPON("1", user -> {
        Coupon coupon = new Coupon();
        user.getCoupons().addCoupon(coupon);
        View.printCreateCoupon(coupon);
    }),
    MANAGE_COUPON("2", user -> {
        View.printNotUsedCoupon(user.getCoupons());
    }),
    MANAGE_USED_COUPON("3", user -> {
        View.printUsedCoupon(user.getCoupons());
    }),
    USE_COUPON("4", user -> {
        String number = View.inputCouponNumber();
        user.getCoupons().useCoupon(number);
    }),
    QUIT("Q", user -> {});

    private String number;
    private Consumer<User> consumer;

    MainFeature(String number, Consumer<User> consumer) {
        this.number = number;
        this.consumer = consumer;
    }

    public static MainFeature getMenu(String input) {
        return Arrays.stream(MainFeature.values())
                .filter(value -> value.number.equals(input))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("선택지 없음"));
    }

    public String getNumber() {
        return number;
    }

    public void accept(User user){
        consumer.accept(user);
    }
}
