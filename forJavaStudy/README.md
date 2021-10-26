# TCP/IP (MultiThread) - Object Transfer

## image
![tcp](https://user-images.githubusercontent.com/87312401/133274809-4ffebbb6-d1a8-476e-a5f2-f58d766f7d50.jpg)

## Why?
* Clients must not access to DB directly.
* Building a multi-threaded environment.
* Easy to deploy (separate Server / Client).

### to send an object, Client and server use the same serial key

```java
public class PlantsGrowingVo implements Serializable {
	private static final long serialVersionUID = 1234567890L;
 }
``` 
###  Use Object io

```java
public void receiveData() {
		
		Runnable thread = new Runnable() {

			@Override
			public void run() {
				
				try {
					while(true) {
            printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(dataSocket.getOutputStream())));
            objectInputStream = new ObjectInputStream(dataSocket.getInputStream());
            System.out.println(objectInputStream.toString());
            PlantsGrowingVo pgVo = (PlantsGrowingVo) objectInputStream.readObject();
            }
         } catch (Exception e) {
         }
      }
   };
    // to block thread explosive increase, use submit to decrease overheader
    PlantsGrowingServer.threadPool.submit(thread); 
 }
```

# UUID
uuid란 난수생성기이다. 경우의수가 엄청나므로 거의 일치할 일이 없다.

해당 난수 생성기의 특징을 바탕으로 서버에서 클라이언트에게 ****-****-****-**** 구조의 쿠폰번호를 생성하여 제공하는 코드를 간단하게 구현하였다.
이메일 양식에 어긋나는 클라이언트, 이미 지급된 클라이언트는 다시 쿠폰을 받지 못하도록 간단한 예외처리도 만들어 두었다.

간단하고 빠르게, 이해하기 쉽게 한 페이지에 코드를 구현해놓았다.

# Enum
## Enum 타입 구분
enum 클래스를 이용해 , 객체의 상태를 구분할 수 있다. 단, enum 으로 관리되는 데이터는 빈번하게 추가, 제거가 발생하면 안되고 
enum 타입이 여러 테이블에서 활용될 때 enum이 비로소 제 역할을 해낸다.

## 타입별 다른 연산식 처리
특정한 타입에 따라 다른 연산식을 처리해야 할 경우가 있다. 이를테면 vip 등급과 gold 등급의 할인율? 등의 차이가 있을 듯하다.
또, 부가세, 공급가액 등 같은 지불액임에도 그 계산식에 차이가 있다. 이를 Enum 타입으로 해결할 수 있다.

### 핵심 코드
Enum 타입으로 각 계산 방법을 분리하고 , Function 함수를 통해 구현체를 람다식으로 생성해준다.
추가적으로 람다식을 계산해주는 메서드를 만들어 이를 계산한다.
```java
enum SalesAmountType{
	ORIGIN_AMOUNT("원금액", amount -> amount),
	SUPPLY_AMOUNT("공급가액", amount -> Math.round(amount.doubleValue() * 10)/11),
	VAT_AMOUNT("부가세", amount -> Math.round(amount.doubleValue()/11)),
	NOT_USED("사용 안함", amount -> 0L);
	
	private String viewName;
	private Function<Long,Long> expression;
	.
	.
	.
}
```

## 타입을 그룹화하여 연산식 처리하기
타입에도 하나로 묶을 수 있는 그룹이 있을 것이고, 그 그룹에 따라 다른 연산식이 필요한 경우가 있다.
예를 들면 지불수단, 지불 그룹이 있다. 현금 지불 수단에는 현금, 무통장 입금 , 계좌이체, 카카오페이, 토스 등등이 있다. 이들은 각 타입은 다르지만 수수료를 계산할 때 동일한 계산식(0)이 적용된다.

### 핵심 코드

먼저 각 지불 수단을 나누는 enum 클래스를 생성한다.
```java
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
```

지불 수단을 그루핑할 수 있는 enum 클래스를 생성한다.
이때, 각 enum에 PaymentOption의 배열을 두고, stream을 이용하여 값을 찾는다.

```java
enum PaymentGroup{
	
	CASH("현금", new PaymentOption[]{PaymentOption.CASH, PaymentOption.DEPOSITLESS,PaymentOption.BANK_TRANSFER,PaymentOption.FIELD_PAYMENT, PaymentOption.TOSS}, amount -> 0L),
	PG("결제 대행사", new PaymentOption[] {PaymentOption.MOBILE, PaymentOption.CREDIT_CARD,PaymentOption.SIMPLE_PAY}, amount -> Math.round(amount.doubleValue()*0.001));
	
	private String viewName;
	private PaymentOption[] options;
	private Function<Long, Long> amount;
	
	PaymentGroup(String viewName, PaymentOption[] options, Function<Long,Long> amount){
		this.viewName = viewName;
		this.options = options;
		this.amount = amount;
	}
}
```

stream 계산식
stream 의 filter, findAny, orElse 등을 이용하여 원하는 값을 출력한다
이때, anyMatch를 통해 bool값을 반환받아 원하는 값을 확인할 수 있다.

```java
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
```
    

