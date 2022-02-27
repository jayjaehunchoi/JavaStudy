# 코드로 객체 지향 기본 용어 알아보기

객체지향을 처음 공부할 때 관련 내용을 검색 하면 **책임**, **협력**, **다형성** 등 알 수 없는 용어들이 잔뜩 등장하는 것을 본 경험이 있을 것입니다.

오늘 포스팅에서는 코드를 통해 객체 지향 기본 용어들을 정리하고 앞으로 코드를 작성할 때 의도적으로 이 내용을 떠올릴 수 있도록 도움을 주는 글을 작성하고자 합니다.

## 책임

**책임** 이란 객체가 맡아야하는 역할입니다.

한 **User**가 하나은행에 돈을 입금한다고 가정해봅시다.  
비즈니스 상 **User**는 ```돈을 입금한다``` 는 역할을 갖게 됩니다.

```java
public class User {

    public void depositMoney() {
        //...
    }
}
```

다음으로는 **HanaBank**라는 은행 객체를 만들겠습니다.  
은행은 **입금**과 **출금**에 책임을 갖게 됩니다.

```java
public class HanaBank {
    public void deposit() {
        //...
    }
    public void withdraw() {
        //...
    }
}
```

이로써 **User**와 **HanaBank** 는 각각의 역할을 갖게 되었습니다. 이를 **책임**이라고 합니다.

## 협력

위에서 구현한 코드를 잘 협력하여 동작시켜 봅시다.

### User
```java
public class User {

    private final HanaBank hanaBank;
    
    public User(HanaBank hanaBank) {
        this.hanaBank = hanaBank; // 하나은행을 상태로 갖습니다.
    }

    public void depositMoney(final int money) {
        hanaBank.deposit(money); // 입금하는 로직을 수행하면 하나은행의 deposit을 호출합니다.
    }
}
```

### HanaBank

```java
public class HanaBank {

    private int balance;

    public HanaBank() {
        this.balance = 0;
    }

    public void deposit(final int money) {
        balance += money; // 입금
    }

    public void withdraw(final int money) {
        if (money < balance) {
            throw new IllegalArgumentException();
        }
        balance -= money; // 출금
    }
}
```

이로써 ```사용자가 하나은행에 돈을 입금하는 상황```을 협력을 통해 구현할 수 있게 됐습니다.

이를 바로 **책임 주도 설계**라고 부릅니다.
책임 주도 설계는 **객체**가 하나의 책임을 갖도록 하여 각 책임을 연결함으로써 높은 응집도와 낮은 결합도를 유지하는 코드를 만들 수 있게 됩니다.

이때, 신흥 은행 각자 **TossBank**가 등장합니다. 입금할 때마다 100원을 더 얹어준다는 파격적인 정책을 제시하였습니다.
당연히 TossBank에도 가입을 해야겠죠? 이를 원활하게 만들어주는 것이 바로 **추상화**와 **다형성**입니다.

## 추상화

**HanaBank**와 **TossBank**의 공통점은 **Bank**라는 것입니다.  
이들의 공통적인 기능을 추상화하여 **Bank**를 생성해봅니다.

```java
public interface Bank {
    public void deposit(final int money);
    public void withdraw(final int money);
}
```

인터페이스와 추상 메서드를 생성하였으니 **HanaBank**와 **TossBank**는 이를 상속 받을 수 있습니다.

```java
public class TossBank implements Bank{

    private int balance;

    public TossBank() {
        this.balance = 0;
    }

    public void deposit(final int money) {
        balance += (money + 100);
    }

    public void withdraw(final int money) {
        if (money < balance) {
            throw new IllegalArgumentException();
        }
        balance -= money;
    }
}
```

이제 추상화를 완료했으니 다형성을 활용해서 코드를 수정할 수 있습니다.

## 다형성

```java
public class User {

    private final Bank bank; // 부모 클래스로 변경

    public User(Bank bank) {
        this.bank = bank;
    }
}
```

```java
User user1 = new User(new HanaBank());
user1.depositMoney(1000);

User user2 = new User(new TossBank());
user2.depositMoney(1000);
```
**User** 생성자에서 인터페이스 **Bank**을 매개변수로 받고 있기 때문에 그 하위 클래스인 **HanaBank**와 **TossBank** 를 매개변수로 받을 수 있습니다.

이처럼 외부에서 객체를 만들어 **User**에 주입해주는 것을 **의존성 주입**이라고 부르며 이는 객체지향의 핵심이자 확장성을 높이는 key같은 존재입니다.

## 캡슐화

마지막으로 **캡슐화**입니다.
캡슐화를 통해 내부 상태를 공개하지 않고 필요한 내용만 외부 객체가 알게하여 유지보수에 용이한 코드를 만들 수 있습니다.

가령 **User 가 현재 잔고를 확인한다** 는 기능을 아래와 같이 구현한다고 가정해봅시다.

```java
TossBank bank = new TossBank();
User user2 = new User(bank);
user2.depositMoney(1000);
System.out.println(bank.balance);
```

만약 이렇게 **balance**가 직접 open된다면 아래와 같은 문제가 발생할지도 모릅니다.

```java
bank.balance += 50000000; // 이제부터 나는 부자~!
```

이런 문제를 해결하기 위해 우리는 내부 상태를 **private**으로 막고 필요한 기능을 캡슐화 하여 제공합니다.

```java

public class User {

    private final Bank bank;

    public User(Bank bank) {
        this.bank = bank;
    }

    public void depositMoney(int money) {
        bank.deposit(money);
    }

    public void showBalance() {
        bank.checkBalance(); // 잔고를 출력하는 메서드
    }
}
```

이렇게 코드가 작성되면 **User**는 bank에 직접 접근하지 않고도 잔고를 출력할 수 있습니다.

# 정리
객체 지향은 객체들의 책임과 협력으로 이루어집니다.
추상화를 통해 구현체에 의존하지 않는 코드를 만들 수 있고, 캡슐화를 통해 수정과 확장에 유연한 코드를 만들 수 있습니다.
항상 책임 주도 개발을 기억하며 객체지향 코드의 장인이 되어 봅시다!