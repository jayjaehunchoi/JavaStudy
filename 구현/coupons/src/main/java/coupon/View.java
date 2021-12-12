package coupon;

import camp.nextstep.edu.missionutils.Console;
import coupon.domain.Coupon;
import coupon.domain.Coupons;
import coupon.domain.User;

public class View {

    public static String inputUserName(){
        System.out.println("유저 명을 입력하세요");
        return Console.readLine();
    }

    public static String inputMenu(){
        System.out.println("## 사용할 기능을 골라주세요");
        System.out.println("1. 쿠폰 생성");
        System.out.println("2. 사용 전 쿠폰 관리");
        System.out.println("3. 사용된 쿠폰 관리");
        System.out.println("4. 쿠폰 사용하기");
        System.out.println("Q. 종료");

        return Console.readLine();
    }

    public static String inputCouponNumber(){
        System.out.println("쿠폰 번호를 입력하세요.");
        return Console.readLine();
    }

    public static void printCreateCoupon(Coupon coupon){
        System.out.println("쿠폰이 생성되었습니다. " + coupon);
    }

    public static void printNotUsedCoupon(Coupons coupons) {
        System.out.println("사용 가능한 쿠폰");
        coupons.getNotUsedCouponList().forEach(coupon -> System.out.println(coupon));
    }

    public static void printUsedCoupon(Coupons coupons) {
        System.out.println("사용 한 쿠폰");
        coupons.getUsedCouponList().forEach(coupon -> System.out.println(coupon));
    }

    public static String insertCouponNumber() {
        System.out.println("사용할 쿠폰 넘버를 입력하세요");
        return Console.readLine();
    }
}
