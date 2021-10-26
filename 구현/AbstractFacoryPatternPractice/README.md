## Abstract Factory 패턴
Abstract Factory 패턴은 인터페이스를 이용하여 구현 클래스에 직접 의존하지 않고 객체를 생성하는 패턴이다.
Spring을 하면서 의존성 역전원칙을 준수하기 위해, 구현클래스보다, interface에 의존성을 놓아서 익숙해진 패턴이었으나 , 마냥 이 방식으로만 구현하려고 하니 설계하는 게 쉽지는 않았다.
결론적으로 이 패턴은 , 패키지간 양방향 의존성 주입을 예방하기 위해, 복잡한 개발을 조금 단순하기 위해 부분적으로 사용하는 것이 맞을 것 같다고 판단하였다.

### 과연 올바르게 짠 것일까?
코드를 작성하면서도 과연 올바르게 Abstract Factory 패턴을 구현한 것인가에 대한 의문이 들었다. 
사실상 조건문으로 구현클래스에 직접 의존하고 있는 부분이 꽤나 있었기 때문이다.  이게, 어쩔 수 없는 부분인지, 아니면 잘못 짠것인지는 약간 헷갈린다.
Spring에서는 의존성을 주입해주는 클래스를 따로 생성하여 해당 클래스에서 구현 클래스와 연결이 됐는데, 이 부분은 조금 더 공부가 필요해보인다.

![image](https://user-images.githubusercontent.com/87312401/135263587-fb85b994-e61d-4df7-9b87-952fe1ba7470.png)

### code

구조를 짜는 연습이었기때문에, 로직은 최소화, 단순화하였다.
가장 의문이 드는 코드를 적어두겟다.

```java
// 아래에 보면 CarStore에서 car assembling하는 로직을 호출한다. 과연 이게 맞는 설계인가?
public abstract class CarStore {
	
	public Car orderCar(String name) {
		Car car = makeOrder(name);
		car.assembling();
		return car;
	}
	
	protected abstract Car makeOrder(String name);
	
}
```

분명 나는 공장을 만들었는데, 정작 로직은 store에서 돌아간다. 조금 더 고민해서 짠다면 : orderCar > makeOrder > factory > assembling > car > orderCar > return 이 돼야 맞는 것 같다.
우선 이 추상 공장 패턴을 작성해봤다는 것에 의의를 둔다...
