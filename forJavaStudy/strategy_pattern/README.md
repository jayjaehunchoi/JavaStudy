# 전략패턴

## 디자인 패턴

**디자인 패턴**이란 객체 지향 설계 과정에서 빈번하게 사용되는 설계를 모아 패턴화 한 것입니다.
즉, 재설계를 최소화 하며 요구사항의 변화를 수용할 수 있는 패턴을 정리한 것이라고 생각하면 좋습니다.

아래 예시를 보겠습니다.

```java
// 만약 Grade가 z까지 있다면...?? 후..;;
public String calculateGrade(final int score) {
    if (score < 50) {
        return "F";
    } else if (score < 60) {
        return "D";
    } else if (score < 70) {
        return "C";
    } else if (score < 80) {
        return "B";
    } else if (score < 90) {
        return "A";
    } else {
        return "S";
    }
}
```

일반적인 상황에서 상태를 기반으로 동작을 변경할 때 이런 코드를 작성합니다.  
위 코드처럼 ```score```의 상태에 따라 다른 작업을 수행할 때, **if-else** 블록이 생깁니다.

이를 조금 개선하기 위해서는 **enum** 타입을 사용하기도 하죠.

```java
public enum Score {
    
    A("A", score -> score > 90),
    B("B", score -> score > 80),
    C("C", score -> score > 70),
    F("F", score -> score < 50);

    private final String name;
    private final Predicate<Integer> predicate;
    
    Score(String name, Predicate<Integer> predicate) {
        this.name = name;
        this.predicate = predicate;
    }

    public static Score valueOf(int score) {
        return Arrays.stream(values())
                .filter(value -> value.predicate.test(score))
                .findFirst()
                .orElse(F);
    }
}
```

하지만 사실상 **Score**의 개수가 다른 알파벳으로 추가된다면, 분기문을 추가하는 것과 큰 차이가 없습니다.
게다가 **Stream**을 사용하지 않는 레거시에서는 분기문이 존재하기에 기능 변경에 따라 분기문을 수정해야하는 불상사가 발생합니다. (OCP 원칙 미준수)

오늘은 이 문제를 해결할 수 있는 전략패턴을 사용해보고자 합니다.

## 전략패턴

**전략패턴**은 런타임에 알고리즘을 선택할 수 있기 위한 **How**기반의 행위 소프트웨어 디자인 패턴입니다.

![image](https://user-images.githubusercontent.com/87312401/158318810-598a4a24-26e6-4e72-afb6-b23e3a1f6740.png)

**전략**을 담을 객체를 하나 생성합니다. 이 객체는 클라이언트의 요청에 따라 얼마든지 전략을 변경할 수 있지만 
전략패턴의 특징은 상태 패턴과 달리 각 **ConcreteClass**의 상태를 서로 알고있지 않을 뿐더러 전략의 변화가 많이 없는 경우에 사용합니다.

위 사진에서 볼 수 있다시피 구현된 전략을 갈아끼며 다르게 캡슐화 되어 있는 로직을 마음껏 조립할 수 있습니다.

### 전략패턴 구현
베팅금을 통해 수익금을 계산해야 하는 클래스입니다.
현재 수익금 계산의 경우의 수는 **BlackJack, Win, Draw, Lose**가 존재합니다.

만약 전략패턴이 없다면 어떻게 구현할 수 있을까요?

```java
public void calculateBenefitNoStrategy(String benefitStrategy) {
    if (benefitStrategy.equals(BlackJack)) {
        amount = (int) (amount * 1.5);
    } else if (benefitStrategy.equals(Win)) {
        amount = amount;
    } else if (benefitStrategy.equals(Draw)) {
        amount = 0;
    } else {
        amount = - amount;
    }
}
```

처음 소개한 내용처럼 분기문을 일일히 넣어주던가, enum을 사용하게 되겠죠.  
계산 전략이 많지 않다면 사용할 수도 있겠지만 기능이 추가된다면 유지보수성이 굉장히 안좋아질 것이라는 것을 직감할 수 있습니다.

그럼 이제 전략패턴을 사용하여 구현해보겠습니다.
먼저 **Strategy**를 갖는 객체를 하나 만들어보겠습니다.
```java
public class Benefit {

    private int amount;
    private BenefitStrategy benefitStrategy; // 수익금 계산 전략

    public Benefit(int amount, BenefitStrategy benefitStrategy) {
        this.amount = amount;
        this.benefitStrategy = benefitStrategy;
    }

    public void calculateBenefit() {
        // 전략에 따른 계산 방식 변화
        amount = benefitStrategy.calculate(amount);
    }

    public int getAmount() {
        return amount;
    }
}

```

위 코드에서는 **BenefitStrategy** 인터페이스를 참조하여 전략을 하나 갖고 있습니다.

이 전략을 상속하여 전략에 따라 계산 로직이 변경되게끔 만들어보겠습니다.

#### BenefitStrategy - ConcreteClasses
```java
public interface BenefitStrategy {

    int calculate(int amount);
}

public class BlackJack implements BenefitStrategy{

    @Override
    public int calculate(int amount) {
        return (int) (amount * 1.5);
    }
}

public class Win implements BenefitStrategy{

    @Override
    public int calculate(int amount) {
        return amount;
    }
}

public class Draw implements BenefitStrategy{

    @Override
    public int calculate(int amount) {
        return 0;
    }
}

public class Lose implements BenefitStrategy{

    @Override
    public int calculate(int amount) {
        return - amount;
    }
}
```
각 새로운 전략이 생길때마다 **Strategy**를 상속받아 새로운 클래스를 생성하여 활용하게 됩니다.  
관리해야 할 클래스가 많아지기는 하지만 오히려 공통적인 계산상황이 존재할 때 반복되는 코드를 줄일 수 있습니다.
또, 하나의 클래스에서 모든 전략이 관리되는 것보다 유지 보수성이 좋을 수 있습니다.

이렇게 구현된 내용을 직접 사용해봅시다.

```java
public class Main {
    public static void main(String[] args) {
        Benefit benefit = new Benefit(1000, new BlackJack());
        benefit.calculateBenefit();
        System.out.println("블랙잭: " + benefit.getAmount());

        Benefit benefit1 = new Benefit(1000, new Win());
        benefit1.calculateBenefit();
        System.out.println("승리: " + benefit1.getAmount());

        Benefit benefit2 = new Benefit(1000, new Draw());
        benefit2.calculateBenefit();
        System.out.println("무승부: " +benefit2.getAmount());

        Benefit benefit3 = new Benefit(1000, new Lose());
        benefit3.calculateBenefit();
        System.out.println("패배: " + benefit3.getAmount());
    }
}
```

결과는 아래와 같습니다.

```
블랙잭: 1500
승리: 1000
무승부: 0
패배: -1000
```

고수준 코드는 그대로 변경이 전혀 없음에도 사용한 전략에 따라 계산결과가 달리 출력되는 것을 확인할 수 있습니다.

## 정리

이번 포스팅에서는 전략패턴을 사용해보았습니다. 
블랙잭 미션을 하면서 상태패턴을 사용하려다 우연히 함께 알게된 디자인 패턴인데 사실 아직 실제 상황에서는 전략 패턴과 상태 패턴이 조금 헷갈리긴 합니다.

하지만 리뷰를 받고 수정해보면서 이 차이점을 확실히 이해하고 필요한 상황에 각 디자인패턴을 사용할 수 있도록 연습하는 게 중요할 것 같습니다.