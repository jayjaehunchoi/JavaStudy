# Synchronized 이해하기

자바에서는 멀티 쓰레드 환경에서 안정성을 부여하기 위해 **synchronized**라는 예약어를 제공합니다.  
하지만 일반적으로 순수 자바로 프로그래밍을 하다보면 멀티 쓰레드를 경험할 일이 없어 해당 예약어에 대해 이해하는 것이 어려운데요.

이번 포스팅에는 **synchronized**예약어를 이해하고 간단하게 실습하는 시간을 갖고자 합니다.

## synchronized란?

**synchronized**는 사전적으로 ```동시에 일어나다.``` 라는 의미를 갖고 있습니다.
그 의미처럼 자바에서도 동시에 일어나는 일에 대해 안정적으로 처리하기 위해 해당 키워드가 생겼습니다.

동시에 하나의 객체에 접근하는 여러 쓰레드에 대해 , **하나씩 들어오세요**하며 안내하는 존재라고 생각하면 쉬울 것 같습니다.

**synchronized**는 메서드와 블록으로 사용할 수 있고, 생성자의 식별자로는 사용이 불가합니다.

```java
// 메서드에서 사용하는 경우
public synchronized void sample() {
    //...
}

// 블록으로 사용하는 경우
public Object obj = new Object();
public void sampleBlock() {
    synchronized(obj) {
        /...
    }    
}
```

단순한 방법으로 사용이 가능하죠? 하지만 해당 식별자를 언제 사용할지에 대해서는 감이 오지 않습니다.
**synchronized**는 일반적으로 아래와 같은 상황들에서 사용됩니다.

1. 하나의 객체를 여러 스레드에서 동시에 사용하는 경우
2. static으로 선언한 객체를 여러 스레드에서 동시에 사용하는 경우

## synchronized 사용하기

주로 사용되는 두가지 상황을 예시로 **synchronized**를 사용해봅시다.

```Contributor``` 가 ```Contribution``` 에 기부를 하는 클래스를 먼저 구현해 보겠습니다.

```java
public class Contribution {

    private int amount = 0;

    public void donate() {
        amount++;
    }

    public int getAmount() {
        return amount;
    }
}
```
기부금을 기부받는 ```donate``` 메서드와 기부금을 확인하는 ```getAmount``` 가 존재합니다.

```java
package synchronized_practice;

public class Contributor extends Thread {

    private final Contribution contribution;
    private final String name;

    public Contributor (final Contribution contribution, final String name) {
        this.contribution = contribution;
        this.name = name;
    }

    public void run() {
        for (int i = 0; i < 1000; i++) {
            contribution.donate();
        }

        System.out.printf("%s total = %d\n", name, contribution.getAmount());
    }
}
```
**Contributor**는 멀티 Thread로 구현하기 위해 **Thread**를 상속받고 필드로 기부단체와 기부자 이름을 갖고 있습니다.
```run```메서드를 상속받아 멀티 스레드 환경에서 **1000원을 기부하고 이를 출력합니다**.

먼저 각기 다른 기부 단체에 기부하는 상황을 가정해봅시다.

```java
public static void donateEachContributorToEachContribution() {
    Contributor[] contributors = new Contributor[10];

    for (int i = 0; i < 10; i++) {
        Contribution group = new Contribution(); // 기부자마다 다른 단체에 기부한다.
        contributors[i] = new Contributor(group, "contributor " + i);
    }

    for (int i = 0; i < 10; i++) {
        contributors[i].start(); // 멀티 스레드 환경에서 기각
    }
}
```

각 스레드마다 다른 객체에 접근하기 때문에 동시성 이슈가 발생하지 않고 1000원씩 잘 기부되는 것을 확인할 수 있습니다.

![스크린샷 2022-03-05 오후 1 30 02](https://user-images.githubusercontent.com/87312401/156867775-ba21a91b-8bb5-4297-bef0-8f0a40a20c4b.png)

그렇다면 하나의 객체에 접근할 때는 어떨까요?

```java
public static void donateEachContributorToOneContribution() {
    Contributor[] contributors = new Contributor[10];
    Contribution group = new Contribution(); // 기부할 하나의 그룹
    for (int i = 0; i < 10; i++) {
        contributors[i] = new Contributor(group, "contributor " + i);
    }

    for (int i = 0; i < 10; i++) {
        contributors[i].start(); // 멀티 스레드 환경에서 동일 객체에 접근
    }
}
```
실행 결과는 아래와 같습니다.

![스크린샷 2022-03-05 오후 1 31 44](https://user-images.githubusercontent.com/87312401/156867840-5fb6f557-e1f1-4735-afeb-4414d5cecb37.png)

1000원씩 10명의 사람이 기부를 함에도 불구하고 최종금액이 10000원이 되지 않고 8993원이 되는 것을 확인할 수 있습니다.
간혹 10000원이 되는 경우도 있겠지만 항상 10000원이 되는 상황에서 간혹은 올바르지 않은 프로그램임을 뜻합니다.

동시에 10개의 객체가 1개의 객체에 접근하려다 보니 이런 문제가 발생한 것이죠.

그렇다면 **Contributor**객체가 **Contribution**객체에 접근하는 지점인 ```donate```에 **synchronized**식별자를 추가해보겠습니다.

```java
public synchronized void donate() {
    amount++;
}
```

contributor 3을 보면 최종 금액으로 10000원이 출력된 것을 볼 수 있습니다.

![스크린샷 2022-03-05 오후 1 35 46](https://user-images.githubusercontent.com/87312401/156867950-bec57165-5f11-4cea-81a3-9d6b0f109cb9.png)

성능을 비교해봐도 큰 차이는 존재하지 않습니다. (물론 메서드가 커질 수록 성능 차이가 커집니다.)

그렇다면 **static**을 사용하는 경우에는 어떻게 동기화를 할 수 있을까요?

```java
public class ContributionStatic {

    private static int amount = 0;

    public void donate() {
        amount++;
    }

    public int getAmount() {
        return amount;
    }
}
```

당연히 **amount**는 **ContributionStatic**의 클래스 객체이기 때문에 각 다른 단체에 기부를 하는 모양새가 안나옵니다.
게다가 동기화도 되지 않습니다.

![스크린샷 2022-03-05 오후 1 46 18](https://user-images.githubusercontent.com/87312401/156868303-8de8df47-7529-4d9b-8ff9-f63d53a41e97.png)

그럼 ```donate``` 메서드에 **synchronized** 식별자를 추가하고 실행볼까요?

![스크린샷 2022-03-05 오후 1 48 37](https://user-images.githubusercontent.com/87312401/156868362-57c93bfd-f4f0-487b-a722-68537f36d262.png)

여전히 동기화가 되지 않습니다.

바로 **synchronized**의 특성 때문입니다. 이 식별자는 **각각의 객체**에 대해 동기화를 해줍니다.
지금처럼 필드가 클래스 변수일 경우 해당 필드에 대해서는 동기화를 해주지 못하는 것이죠.

하지만 해결방법은 존재합니다. 메서드도 **인스턴스 메서드**가 아닌 **클래스 메서드**로 변경해주면 됩니다.

```java
public static synchronized void donate() {
    amount++;
}
```

항상 변하는 값에 대해 static으로 선언하는 상황은 많이 없겠지만, 어쨌든 이런 방법으로 **static** 필드를 동기화 해줄 수 있습니다.

## 정리

이번 포스팅을 통해 간단하게 **synchronized**를 이용한 동기화에 대해 알아봤습니다.

동기화는 **여러 객체에서 동시에 하나의 객체에 접근할 때** , **static으로 선언된 필드에 대해 여러 객체가 동시에 접근할 때** 필요합니다.
간단하게 식별자를 추가해줌으로써 동기화를 진행할 수 있지만, 성능상의 이슈는 분명 존재하기에 사용에 대해서는 많은 고민이 필요하다고 합니다.


