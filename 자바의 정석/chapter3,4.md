## Chapter 3
### 쉬프트연산자
쉬프트 연산자는 곱셈/ 나눗셈보다 빠른 우선순위를 가진 연산자이다.
이 연산자를 이용하여 2^n 을 나누거나 , 곱해줄 수 있다

```java
System.out.println(10<<5); // 10 * 2^5
System.out.println(10>>5); // 10 / 2^5
```

### 비트연산자
비트 연산자는 이진법으로 변환할 때, 사용되는 연산자이다.
과거 코딩테스트 문제를 풀때 비트연산자를 사용한 기억이 난다.

```java
System.out.println(Integer.toBinaryString(1|10)); // 1, 1010 --> 1011 
System.out.println(Integer.toBinaryString(8^10)); // 1000, 1010 --> 0010 --> 10
System.out.println(Integer.toBinaryString(1&10)); //1, 1010 --> 0
```

### 삼항연산자
조건문을 쉽게 주는 용도로 사용한다.
지금껏이게 삼항연산자인지 모른상태로 손에 익숙해져서 써왔었는데, 이제 확실히 알게됐다

```java
// 삼항연산자를 이용한 간단한 recursion 구현
public static int fibonacci(int n) {
		if(n == 1 || n == 0) {
			return 1;
		}
		return n == 1 || n == 0 ? 1 : fibonacci(n-1)+fibonacci(n-2); 
	}
```
## Chapter 4
### do-while
개념은 알고 있지만 나는 잘 사용하지 않아서 손에 익숙지는 않다.
do 구문을 먼저 수행하고 while에 달린 조건을 바탕으로 반복문이 돌아간다.

```java
// input값으로 1이 들어오면 반복문 종료
int input = 0;
do{
  input = sc.nextInt();
}while(input == 1);
```

### Loop 명을 명시하여 break를 걸어보자
평소에 알고리즘문제를 풀다보면 중첩된 조건문 내에서 조건문 전체를 끝내고 싶은 경우가 있다.
그럴때 이 Loop명을 명시적으로 선언하여 , break에 달아주면 편하다

```java
// 내부 조건문에서 1이걸리면 전체 loop가 break
Loop1 : for(int i = 0 ; i < 10 ; i++){
          for(int j = 0 ; j < 10 ; j++){
            int s = sc.nextInt();
            if(s == 1) break Loop1;
          }
}
```
