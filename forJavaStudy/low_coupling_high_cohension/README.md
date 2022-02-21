# 의존성 응집도 결합도

객체 지향 공부를 하게되면 필연적으로 **의존성, 응집도, 결합도**에 대해 듣게됩니다.

직접 자바 코드로 이 내용들을 학습하고 객체지향 코드를 작성할 때 이들이 왜 중요한지 알아봅시다.

## 의존성

의존성은 코드레벨에서는 확인할 수 없지만 코드가 실행되는 지점에 객체간 의존관계가 존재하는 경우 이를 동적 의존성으로 분류합니다.

아래 코드를 보겠습니다.
```java
public class Developer implements Employee{
    @Override
    public void doWork() {
        System.out.println("개발자가 일을 합니다.");
    }
}

class Designer implements Employee {
    @Override
    public void doWork() {
        System.out.println("디자이너가 일을 합니다.");
    }
}

class Company {
    private Employee employee;

    public Company(Employee employee) {
        this.employee = employee;
    }

    public void work() {
        employee.doWork();
    }
}
```

위 코드에서는 **Employee** 라는 인터페이스에 의존성이 있습니다.
즉, 구현체와는 객체 관계를 맺지 않고 있다는 뜻이죠.

덕분에 **Company**는 아래 코드처럼 언제든 개발자 회사가 될 수도, 디자인 회사가 될 수도 있습니다.

```java
public class Main {

    public static void main(String[] args) {
        Company developerCompany = new Company(new Developer());
        developerCompany.work(); // 개발자가 일을 합니다. 출력

        Company designerCompany = new Company(new Designer());
        designerCompany.work(); // 디자이너가 일을 합니다. 출력
    }
}
```

위 코드처럼 인터페이스를 매개인자로 두고 실제 실행환경에서 원하는 구현체를 넣어 코드를 조금 더 유연하게 설계할 수 있습니다.

## 높은 응집도와 낮은 결합도

객체지향적으로 좋은 코드는 무엇인가 물었을 때 흔히 **높은 응집도와 낮은 결합도**에 대해 이야기합니다.

도대체 응집도와 결합도가 무엇일까요?

### 응집도

응집도는 간단히 말하자면 **SRP** 입니다.
**SRP**는 Single Responsibility Principle의 약자로 하나의 객체는 하나의 역할을 수행해야 한다는 의미입니다.
아래 코드를 통해 설명을 구체화 해보겠습니다.

```java
public class AnyCall {
    
    public void call() {
        System.out.println("전화");
    }
    
    // 아니 애니콜에서 웹서핑을?
    public void webSurfing() {
        System.out.println("웹서핑");
    }
    
}
```

예시가 예쁜 것 같지는 않지만... 과거 휴대폰에는 웹서핑 기능이 존재하지 않습니다.

하지만 해당 클래스에는 전혀 다른 책임을 갖고 있는 웹서핑 메서드가 존재하여 응집도를 낮추고 있습니다.

이를 개선해본다면

```java
public class Mobile {
    
    public void call() {
        System.out.println("전화");
    }
}

class Computer {
    
    public void webSurfing() {
        System.out.println("웹 서");
    }
}
```

**Mobile**과 **Computer** 객체로 분리하여 각각 하나의 책임만 수행할 수 있도록 만들어 사용합니다.
이를 통해 응집도를 높일 수 있죠.

### 결합도

다른 객체지향원칙도 물론 중요하지만 그중에서도 저는 결합도를 낮추는 것이 단연 중요하다고 생각합니다.

결합도는 객체간 의존하는 정도를 의미합니다. 만약 객체간 의존하는 정도가 높다면 어떨까요?

**리팩토링 할 때마다 관련없는 객체를 변경해야 한다.** 라는 결론이 나옵니다.

아래 코드로 확인해볼까요

```java
public class WooTeChoCrew {

    public void wakeUp() {
        System.out.println("09시 55분 기상");
    }

    public void turnOnComputer() {
        System.out.println("컴퓨터를 켠다");
    }

    public void startDaily() {
        System.out.println("데일리 미팅을 한다");
    }
}

class WooTeCho {
    public void workAtMorning() {
        WooTeChoCrew wooTeChoCrew = new WooTeChoCrew();
        wooTeChoCrew.wakeUp();
        wooTeChoCrew.turnOnComputer();
        wooTeChoCrew.startDaily();
    }
}
```

위 코드에서 보시다시피 **WooTeCho**객체에서 **WooTeChoCrew** 객체의 메서드를 하나하나 가져와 참조하고 있습니다.

하지만 이는 결합도를 굉장히 높인 사례로 만약 메서드 명이 변경되거나 로직이 바뀌어야 하는 경우 이렇게 결합도 높게 참조한 모든 객체를
탐색하며 수정해야 합니다.

그렇다면 어떻게 결합도를 낮출 수 있을까요?

```java
public class WooTeChoCrew {

    public void startMorning() {
        wakeUp();
        turnOnComputer();
        startDaily();
    }
    
    private void wakeUp() {
        System.out.println("09시 55분 기상");
    }

    private void turnOnComputer() {
        System.out.println("컴퓨터를 켠다");
    }

    private void startDaily() {
        System.out.println("데일리 미팅을 한다");
    }
}

class WooTeCho {
    public void workAtMorning() {
        WooTeChoCrew wooTeChoCrew = new WooTeChoCrew();
        wooTeChoCrew.startMorning();
    }
}
```

굳이 다른 객체에 공개하지 않아도 되는 메서드는 **private**으로 묶고 외부로 노출할 내용에 대해 캡슐화를 진행합니다.

이처럼 의존성, 응집도, 결합도에 대해 학습해보았습니다. 간단한 규칙이지만 잘 지키지 않았던 것은 아닌지 고민해보고 이를 활용하여 객체지향적인 코드를 작성해 봅시다.