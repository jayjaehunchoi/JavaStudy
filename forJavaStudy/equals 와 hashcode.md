# Equals 와 hashCode의 관계

```java
class Card{
	
	String kind;
	int number;
	
	public Card(String kind, int number) {
		this.kind = kind;
		this.number = number;
	}
 }
 ```
 
 위와 같은 클래스가 생성되어 있을때, 아래와 같이 객체를 비교하면 같은 객체라고 나올까?
 
 ```java
Card c1 = new  Card("신용카드", 1);
		Card c2 = new  Card("신용카드", 1);
		
		System.out.println(c1.toString());
		System.out.println(c2.toString());
		System.out.println(c1.equals(c2));
 ```
 
 #### 당연히 아니다. 주소값이 다르기 때문이다(내부적으로 같고있는 hashCode값이 다름)
 
 하지만 분명, 객체끼리 같은 값을 가지고 있을 때, 그 값이 동일한지, 아닌지 판별하고 싶을 때가 있을 것이다.
 그때, 바로 Equals와 hashCode를(꼭 둘다 모두) 오버라이딩 해주면 된다. (내부적으로 equals가 되면 해시코드가 동일해지고, 주소값이 같아진다)
 
 ```java
 class Card{
	
	String kind;
	int number;
	
	public Card(String kind, int number) {
		this.kind = kind;
		this.number = number;
	}

	@Override
	public String toString() {
		return "Card [kind=" + kind + ", number=" + number + "]";
	}
	
	public boolean equals(Object obj) {
		if(!(obj instanceof Card)) {
			return false;
		}
		Card c = (Card)obj;
		return this.kind.equals(c.kind)&& this.number == c.number;
	}
	
	public int hashCode() {
		return Objects.hash(kind, number);
	}
	
}
```

## 꼭 둘다 재정의해줘야 하는 이유는?

이는 동등성 비교의 특징과 이어진다, 기본적으로 동등성 비교의 과정은 hashCode hashfunction을 통해 생성된 객체 내부값을 먼저 비교하고, 
이후 equals로 그 값이 같은지 확인한다.

만약 둘 중 하나만 재정의 하게 된다면? 당연히 제대로 같은 값을 찾지 못할 것이다.

input값이 다르더라도 hash 충돌이 발생하여 같은 버킷에 값이 저장될 수 있다. 이 경우에는 hashCode값은 같지만, equals 로 비교했을 때 틀린 값이 나오게 된다.
이런 경우를 예방하기 위해 같은 객체를 찾고 싶을 때는 hashCode와 equals의 overriding이 꼭 필요하다.
