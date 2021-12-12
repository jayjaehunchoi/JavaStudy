package coupon.domain;

import camp.nextstep.edu.missionutils.Randoms;

import java.util.Objects;

public class Coupon {
    private String serialNumber;
    private CouponStatus couponStatus;

    public Coupon() {
        serialNumber = createCouponSerialNumber();
        couponStatus = CouponStatus.NOT_USED;

    }

    private String createCouponSerialNumber(){
        String val = "";
        for (int i = 0; i < 10; i++) {
            val += Randoms.pickNumberInRange(0, 9);
        }
        return val;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public CouponStatus getCouponStatus() {
        return couponStatus;
    }

    public void useCoupon(){
        couponStatus = CouponStatus.USED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coupon coupon = (Coupon) o;
        return Objects.equals(serialNumber, coupon.serialNumber) && couponStatus == coupon.couponStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(serialNumber, couponStatus);
    }

    @Override
    public String toString() {
        return "쿠폰번호 = " + serialNumber;
    }
}
