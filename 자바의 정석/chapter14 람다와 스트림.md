## 람다와 스트림
#### 람다식이란
* 함수를 간단한 식으로 표현하는 방법
* 익명 함수

#### 람다식 활용
Comparator를 람다식으로 받아서 편리하게 sort하는 방법을 코드로 작성해보았다!

```java
// Comparator 람다식 사용 사례 (정수형)
List<Integer> aList = Arrays.asList(1,6,2,8,3,4,11,5,100,58,30);
Collections.sort(aList,(a,b) -> a-b);
System.out.println(aList);

// Comparator 람다식 사용 사례 - 역순 (문자형)
List<String> sList = Arrays.asList("aa","bdb","csc","ascx","grge","s","a","bb","cdc");
Collections.sort(sList, (a,b)->b.compareTo(a));
System.out.println(sList);
```

### 람다식과 스트림을 활용한 계산

간단하게 람다식과 스트림을 이용하여 배열의 문자열을 대문자로 변경하고, List에 넣어준다.

```java
List<String> sList = Arrays.asList("aa","bb","cc","dd","ee");
System.out.println(sList.stream().map(String::toUpperCase).collect(Collectors.toList())); // 1. 출력 [AA, BB, CC, DD, EE]
System.out.println(sList.stream().map(x -> x.toUpperCase()).collect(Collectors.toList())); // 2. 출력 [AA, BB, CC, DD, EE], 위 작성문과 동일
```
1. sList.stream - 리스트를 표준화된 방법으로 다루기위해 stream으로 변경시켜준다.
2. .map - 위 스트림 계산식에서 중간연산이라고 보면 된다. 람다를 이용하여 String 클래스의 toUpperCase를 적용하여 변경시켜준다. 2번으로 변경하여 사용이 가능하다.
3. .collect(Collectors.toList()) - 최종적으로 List형태로 변경시켜 반환해준다. 

* **스트림은 1회용이다**

### 람다식과 스트림 실전 예제

개인적으로 주문 시스템을 클론코딩하던 와중, Stream과 람다를 활용하여 모든 장바구니 혹은 옵션의 가격을 더하여 최종 가격을 출력해주는 코드를 참고하였다.

```java
// Money 클래스의 핵심 변수와 메서드
public static final Money ZERO = Money.wons(0);
private final BigDecimal amount;
public static <T> Money sum(Collection<T> bags, Function<T, Money> monetary) {
    return bags.stream().map(bag -> monetary.apply(bag)).reduce(Money.ZERO, Money::plus);
}
public Money plus(Money amount) {
    return new Money(this.amount.add(amount.amount));
}
```

**위 Money 클래스에서 핵심적으로 활용할 내용은 sum 메서드이다**
```java
public static <T> Money sum(Collection<T> bags, Function<T, Money> monetary) {
    return bags.stream().map(bag -> monetary.apply(bag)).reduce(Money.ZERO, Money::plus);
}
```

1. 제너릭 타입의 사용으로 활용성을 높인다, 
 * bags 의 자료형은 옵션의 리스트이든 장바구니 리스트이든 관계가 없다. 
 * monetary 또한, 어떤 클래스든 받아서 클래스의 Money 자료형을 참조할 수 있다.

2. Function 함수의 기본 메서드 apply
 * 매개변수로 들어온 lambda식을 수행해준다, 이때 bags 각 요소의 Money값을 apply의 매개변수로 받는다. (T자리 product, return으로 Money)

3. stream 함수의 reduce
 * reduce(기본값, 수행할 람다식) 형태를 받아 0값에 plus 메서드를 참조하여 apply로 받은 Money값을 계속 더해준다.
 * 최종적으로 Money 값으로 리턴한다.

이제 자료형으로 받을 클래스와 실행을 해보겠다!

**Product 클래스**
```java
class Product{
	String name;
	int price;
	Money money;
	
	public String getName() {
		return name;
	}
	public int getPrice() {
		return price;
	}
	public Money getMoney() {
		return money;
	}
	public int plus(Product p) {
		return this.price+p.price;
	}
	public Product(String name, int price, Money money) {
		this.name = name;
		this.price = price;
		this.money = money;
	}
	public int cal(int price, Function<Integer,Integer> exp) {
		return exp.apply(price);
	}
}
```

**main 메서드**
```java
Product p = new Product("aa", 1000, Money.wons(1000));
Product p1 = new Product("bb", 2000, Money.wons(2000));
Product p2 = new Product("cc", 3000, Money.wons(3000));

List<Product> pList = Arrays.asList(p,p1,p2); // Collections<T> bags에 해당하는 값
System.out.println(Money.sum(pList, Product::getMoney)); //계산결과 6000원이 나온다.
```
* bags에 해당하는 매개변수는 Product 자료형을 각 요소로 받는 List가 들어왔다. 제너릭 덕분에 어떤 클래스든 활용이 가능하다.
* Product의 getMoney메서드를 사용하여 각 리스트 내 각 요소의 Money값을 뽑아온다.(apply)
* 이를 0값에 모두 더한다.(reduce)

제너릭, 컬렉션, 람다식, 스트림은 그 내용이 너무 방대하고 어려운 부분이 있어 이렇게 매번 새로운 코드를 만날때마다 경험적으로, 자바의 정석을 보고 개념적으로 정리할 생각이다. 
		


