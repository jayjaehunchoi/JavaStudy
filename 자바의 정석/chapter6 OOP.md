## 객체지향언어
### 객체지향언어란 ?
* 프로그래밍 언어 + 객체지향 개방방식
* 프로그램 재사용성이 높고, 코드 관리가 쉬우며, 제어자와 메서드를 통해 데이터를 보호하고 중복코드를 예방하여 신뢰성이 높다.

### 클래스와 객체
간단하게 클래스는 객체를 만들기 위해, 객체는 객체를 만들어서 기능과 속성을 사용하기 위해 작성한다.

```java
// 노드의 위치와 값을 저장하는 클래스
// 인스턴스를 생성하여 Node의 x좌표, y좌표, val값을 사용할 수 있다.
class Node{
  int x;
  int y;
  int val;
  
  public Node(int x, int y, int val){
    this.x = x;
    this.y = y;
    this.val = val;
  }
}
```

### 클래스, 객체의 생성과 사용
어제 킥보드를 탔다. 간단하게 킥보드 클래스를 만들고 , 이 클래스가 작동할때, 그리고 작동할때 할당되는 메모리를 간단하게 그림으로 표현해보았다.

#### Singsing 킥보드 클래스
정적 변수로 int값 하나를 갖고 있고, price, minutes 를 변수로 갖고 있다.
메서드도 하나 포함하고 있다.

```java
class Singsing{
	static int limitSpeed = 25;
	int price;
	int minutes;
	
	public int pricePerMinutes(int price, int minutes) {
		return price * minutes;
	}
}
```

#### Oop 클래스와 main 메서드
Oop 클래스가 클래스 로더에서 불러와 메모리(JVM)에 할당되고, 이 내부적으로 main메서드가 먼저 JVM 스택에 쌓인다.
main 메서드 스택 내부에서 메서드의 내용이 동작한다.

```java
public class Oop {
	public static void main(String[] args) {
		// Singsing 의 인스턴스 s 생성
    // stack 에 참조변수 s가 쌓이고 heap 메모리에 new Singsing()을 참조한다.
    Singsing s = new Singsing();
    // 참조된 힙 메모리 Singsing내부의 int값을 각각 선언한다.
		s.price = 100;
		s.minutes = 5;
    // 메서드가 실행될때 , JVM 스택에 pricePerMinutes가 하나 쌓이고 , return 값을 주며 gc에 의해 제거된다.
    // 이때 생성된 값이 Stack 의 totPrice에 할당된다
		int totPrice = s.pricePerMinutes(s.price, s.minutes);
    
    // Singsing의 인스턴스 k 생성
		Singsing k = new Singsing();
		k.price = 100;
		k.minutes = 50;
		int totKPrice = k.pricePerMinutes(k.price, k.minutes);
    
    // 메서드 영역의 정적변수 값을 100으로 변동시켰다.
		k.limitSpeed = 100;
		System.out.println(s.limitSpeed == k.limitSpeed); //true 값 반환 , 메서드 영역의 limitSpeed Singsing 클래스 모두가 공유하는 메서드이다.
	}
}
```

### 참고 이미지
![image](https://user-images.githubusercontent.com/87312401/134776810-579a7490-5932-4acb-92ea-218ec284fcc4.png)
