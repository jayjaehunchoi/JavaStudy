package coupon.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Coupons {
    private List<Coupon> couponList = new ArrayList<>();

    public List<Coupon> getNotUsedCouponList() {
        return couponList.stream()
                .filter(coupon -> coupon.getCouponStatus().equals(CouponStatus.NOT_USED))
                .collect(Collectors.toList());
    }

    public List<Coupon> getUsedCouponList() {
        return couponList.stream()
                .filter(coupon -> coupon.getCouponStatus().equals(CouponStatus.USED))
                .collect(Collectors.toList());
    }

    public void addCoupon(Coupon coupon) {
        if(couponList.contains(coupon)) {
            throw new IllegalArgumentException("쿠폰 추가 실패");
        }

        validateCouponSize();

        couponList.add(coupon);
    }

    public Coupon useCoupon(String serialNumber) {
        Coupon findCoupon = getNotUsedCouponList().stream().
                filter(coupon -> coupon.getSerialNumber().equals(serialNumber))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰"));

        findCoupon.useCoupon();
        return findCoupon;
    }

    private void validateCouponSize(){
        if(getNotUsedCouponList().size() + 1 > 10) {
            throw new IllegalArgumentException("더이상 발급받을 수 없습니다.");
        }
    }
}
