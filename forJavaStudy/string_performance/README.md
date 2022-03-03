StringBuilder와 String의 연산

최근 미션을 진행하면서 output을 출력할 때 **String 연산을 사용한다** vs **StringBuilder**를 사용한다로 선택지가 나뉘고 있습니다.

지난 백엔드 **String** 강의에서 네오가 ByteCode를 비교해준 것을 토대로 앞으로의 미션동안 제가 직접 사용할 output 연산에 대한 의사결정을 해보고자 합니다.

## String 연산의 발전

### JDK 5
JDK 5 버전부터 **String** 연산은 JVM에서 **StringBuilder**로 최적화 되어 연산되었습니다.

아래 코드를 실행시켜봅시다.
```java
public String concat(String s, int i) {
    return s + i;
}
```

**javap -c** 명령어를 통해 바이트 코드를 간소화해서 확인해보면 아래와 같습니다.

```java
java.lang.String concat(java.lang.String, int);
  Code:
     0: new           #2      // class StringBuilder
     3: dup
     4: invokespecial #3      // Method StringBuilder."<init>":()V
     7: aload_0
     8: invokevirtual #4      // Method StringBuilder.append:(LString;)LStringBuilder;
    11: iload_1
    12: invokevirtual #5      // Method StringBuilder.append:(I)LStringBuilder;
    15: invokevirtual #6      // Method StringBuilder.toString:()LString;
```

> 0 ~ 4: StringBuilder를 초기화 합니다.  
> 7 : 0번째 매개변수로 받은 String s 를 stack에 로드합니다.  
> 8 : invokeVirtual로 StringBuilder에 s를 append합니다.  
> 11 : 1번째 매개변수로 받은 int i 를 stack에 로드합니다.  
> 12 ~ 15: append하고 toString을 작동시킵니다.

실제로 바이트 코드를 뜯어보면 StringBuilder를 생성하여 **invokevirtual** translation strategy로 인자를 append하는 것을 확인할 수 있습니다.

아래 사진이 **invokevirtual**이 작동하는 원리인데요 바이트코드에서도 볼 수 있듯이 대부분의 작업이 컴파일 시점에 이뤄집니다.

<img width="307" alt="스크린샷 2022-03-03 오후 2 47 22" src="https://user-images.githubusercontent.com/87312401/156504169-c7a91224-38fc-44db-adb9-f3a13ae8d711.png">

하지만 **JDK 9** 버전 부터 **String 연산**의 전략이 변경됩니다.

### JDK 9

위에서 작성한 ```concat``` 메서드를 다시 실행시켜 봅시다.

```java
java.lang.String concat(java.lang.String, int);
  Code:
     0: aload_0
     1: iload_1
     2: invokedynamic #7,  0   // InvokeDynamic #0:makeConcatWithConstants:(LString;I)LString;
     7: areturn
```

JDK 9 버전 이하의 바이트코드와는 전혀 다른 모습을 볼 수 있습니다.

2번에서 사용한 **invokedynamic** 때문입니다.

**StringConcatFactory**클래스에는 ```makeConcatWithConstants``` bootstrap 메서드가 존재합니다.  
**invokedynamic**은 컴파일 시점에 정해진 스펙에 따라 런타임에 부트스트랩 메서드를 최적화하여 호출합니다.
부트스트랩 메서드는 **CallSite**객체를 통해 최종적으로 **String 연산** 작업을 수행하는 것이죠.

버전이 업그레이드 되면서 더 안전하고 동적으로 변경하게 됐습니다.

## 그럼에도 불구하고 String 연산은?

**String 연산**이 많이 최적화 되었음에도, 여전히 성능은 **StringBuilder**에 비해 떨어집니다.

main 메서드에서 아래 두 메서드를 실행시켜 보겠습니다. (10,000회 반복)
```java
public class StringBuilderTest {

    private final static String ADDER = "abc";

    public void stringOperation() {
        String a = "";
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            a += ADDER;
        }
        long end = System.currentTimeMillis();
        System.out.println("string operation : " + (end - start) + " ms");
    }

    public void stringBuilderAppend() {
        StringBuilder sb = new StringBuilder();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            sb.append(ADDER);
        }
        long end = System.currentTimeMillis();
        System.out.println("stringBuilder append : " + (end - start) + " ms");
    }
}
```

실행 결과는 아래와 같습니다.
```
stringBuilder append : 12 ms
string operation : 1450 ms
```

분명히 최적화가 되었음에도 그 성능에서 차이를 보이는데요, 이유는 무엇일까요?

반복문이 실행되는 지점의 바이트 코드를 확인해봅시다.
```java
  public void stringOperation();
    Code:
        ...
    6: ldc           #3                  // int 100000
    8: if_icmpge     24
    11: aload_1
    12: invokedynamic #4,  0              // InvokeDynamic #0:makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;
    17: astore_1
        ...
```

위 코드를 보면 17번 지점에서 계속해서 11번 지점에 stack에 올라간 값을 지역변수로 저장하는 것을 볼 수 있습니다.

즉, 메서드가 종료될 때 까지 stack에 존재하는 String은 제거되지 않습니다.

반면에 **StringBuilder**로 연산된 시점의 바이트 코드를 보시죠.
```java
 public void stringBuilderAppend();
    Code:
        ...
      11: ldc           #3                  // int 100000
      13: if_icmpge     29
      16: aload_1
      17: ldc           #8                  // String abc
      19: invokevirtual #9                  // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
      22: pop
        ...
```
22번 지점에서 반복문이 돌 때마다 stack 의 top value를 pop해주고 있습니다.
덕분에 여전히 **StringBuilder**의 성능이 더 좋습니다.

## 그래서 뭘 사용해야 할까?

```java
public class SimpleStringTest {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        String a = "ab";
        a += "c";
        long end = System.currentTimeMillis();
        System.out.println("string operation : " + (end - start) + " ms");


        start = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        sb.append("ab").append("c");
        end = System.currentTimeMillis();
        System.out.println("stringBuilder append : " + (end - start) + " ms");
    }
}
```

실행결과는 아래와 같습니다.

```
string operation : 35 ms
stringBuilder append : 0 ms
```

한 마디로 **얼마 차이 안납니다.** 실제로 미션에서 사용하는 문자열 덧셈 정도 수준은 성능상 크게 차이가 나지 않습니다.

그럼에도 불구하고 극한으로 최적화를 하고싶다면 **StringBuilder**를 , 그냥 편한게 쓰고 싶다면 **String 연산**을 사용하면 될 것 같습니다.  
다만 언제 **StringBuilder**를 사용할지에 대해서는 이번 포스팅을 통해 조금 확실해 진 것 같습니다.

## 정리

우테코 코치 네오의 String 명강의를 들었지만 뭔가 직접 이 과정을 체험해보고 싶어서 작성해봤습니다.

결론적으로 짧은 문자열을 연산할 때는 **String 연산**을 사용하는 것도 큰 문제는 없을 것 같습니다.
다만 JDBC를 사용하여 복잡한 동적 쿼리를 작성할 때 등 여러 연산이 필요한 경우에는 **StringBuilder**를 사용하는 게 좋겠죠?



