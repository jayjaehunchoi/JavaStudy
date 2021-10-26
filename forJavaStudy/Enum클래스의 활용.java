package com.test;

import java.util.Arrays;
import java.util.function.Function;

// ENUM 클래스를 이용하여 원금액, 공급가액, 부가세를 계산해보자
enum SalesAmountType{
	// Function 인터페이스를 통해 람다식으로 구현체 표현
	ORIGIN_AMOUNT("원금액", amount -> amount),
	SUPPLY_AMOUNT("공급가액", amount -> Math.round(amount.doubleValue() * 10)/11),
	VAT_AMOUNT("부가세", amount -> Math.round(amount.doubleValue()/11)),
	NOT_USED("사용 안함", amount -> 0L);
	
	private String viewName;
	private Function<Long,Long> expression;
	
	SalesAmountType(String viewName, Function<Long, Long> expression) {
		this.viewName = viewName;
		this.expression = expression;
	}
	
	// function 인터페이스의 apply(주어진 람다식 수행해줌)
	public long calculate(long amount) {
		return expression.apply(amount);
	}
	
	public String getViewName() {
		return viewName;
	}
}

// 지불 수단을 그루핑하여 각 수수료를 계산해보자.
enum PaymentOption{
	MOBILE("휴대폰"),
	CREDIT_CARD("신용카드"),
	CASH("현금"),
	SIMPLE_PAY("간편결제"),
	DEPOSITLESS("무통장입금"),
	BANK_TRANSFER("계좌이체"),
	FIELD_PAYMENT("현장결제"),
	TOSS("토스"),
	POINT("포인트"),
	COUPON("쿠폰");
	
	private String viewName;
	PaymentOption(String viewName){
		this.viewName = viewName;
	}
	
	public String getViewName() {
		return viewName;
	}
}

enum PaymentGroup{
	
	CASH("현금", new PaymentOption[]{PaymentOption.CASH, PaymentOption.DEPOSITLESS,PaymentOption.BANK_TRANSFER,PaymentOption.FIELD_PAYMENT, PaymentOption.TOSS}, amount -> 0L),
	PG("결제 대행사", new PaymentOption[] {PaymentOption.MOBILE, PaymentOption.CREDIT_CARD,PaymentOption.SIMPLE_PAY}, amount -> Math.round(amount.doubleValue()*0.001)),
	ETC("기타", new PaymentOption[] {PaymentOption.POINT, PaymentOption.COUPON}, amount -> Math.round(amount.doubleValue()*0.002)),
	EMPTY("없음", new PaymentOption[] {},amount -> 0L);
	
	private String viewName;
	private PaymentOption[] options;
	private Function<Long, Long> amount;
	
	PaymentGroup(String viewName, PaymentOption[] options, Function<Long,Long> amount){
		this.viewName = viewName;
		this.options = options;
		this.amount = amount;
	}
	
	
	public static PaymentGroup findGroup(PaymentOption option) {
		return Arrays.stream(PaymentGroup.values()) // [CASH, PG, ETC, EMPTY]
				.filter(group -> hasPaymentOption(group, option)) // 해당 그룹의 Options중 찾고자하는 option이 있는경우 필터 
				.findAny() // 조건에 부합하는 것 출력 직렬일땐 첫번째, 병렬일땐 매번 달라짐
				.orElse(PaymentGroup.EMPTY); // 없으면 empty로
	}
	public static boolean hasPaymentOption(PaymentGroup group, PaymentOption option) {
		return Arrays.stream(group.options)
				.anyMatch(pay -> pay == option); // bool값 반환, group.options 을 돌며 option과 같은값 나오면 true
	}
	
	public Long calculate(long price) { 
		return amount.apply(price);
	}
	public String getViewName() {
		return viewName;
	}
}

public class Enumtest2 {
	public static void main(String[] args) {
		
		// ENUM 클래스를 이용하여 원금액, 공급가액, 부가세 계산하기
		System.out.println("=======================================================");
		System.out.println("======ENUM 클래스를 이용하여 원금액, 공급가액, 부가세 계산하기=======");
		System.out.println("=======================================================");
		SalesAmountType origin = SalesAmountType.ORIGIN_AMOUNT;
		SalesAmountType supp = SalesAmountType.SUPPLY_AMOUNT;
		SalesAmountType vat = SalesAmountType.VAT_AMOUNT;
		SalesAmountType notUsed = SalesAmountType.NOT_USED;
		
		System.out.println(origin.getViewName()+" = " + origin.calculate(100000));
		System.out.println(supp.getViewName()+" = " +supp.calculate(100000));
		System.out.println(vat.getViewName()+" = " +vat.calculate(100000));
		System.out.println(notUsed.getViewName()+" = " +notUsed.calculate(100000));
		
		System.out.println("=======================================================");
		System.out.println("==ENUM 클래스 이용하여 지불 수단 구분하고 지불수단에 맞는 수수료 계산하기==");
		System.out.println("=======================================================");
		
		// ENUM 클래스 이용하여 지불 수단 구분하고 지불수단에 맞는 수수료 계산하기
		PaymentOption toss = PaymentOption.TOSS;
		PaymentOption mob = PaymentOption.MOBILE;
		PaymentOption cou = PaymentOption.COUPON;
		Long amount = 200000L;
		
		
		System.out.println(toss.getViewName() +" "+ PaymentGroup.findGroup(toss) + " " + amount + " " + PaymentGroup.findGroup(toss).calculate(amount));
		System.out.println(mob.getViewName() +" "+ PaymentGroup.findGroup(mob) + " " + amount + " " +  PaymentGroup.findGroup(mob).calculate(amount));
		System.out.println(cou.getViewName() +" "+ PaymentGroup.findGroup(cou) + " " + amount + " " +  PaymentGroup.findGroup(cou).calculate(amount));
		
	}

}
