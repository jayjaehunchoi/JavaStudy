# JUnit 기초
## JUnit 모듈
* Junit jupiter : Junit 5 환경
* Junit vintage : Junit 3, 4를 5에서 돌릴 수 있게끔 환경 제공
## @Test
### Junit 기본 구조
1. Class + suffix(Test)
2. @Test
3. not private
### 예시
```java
public class sumTest{
	@Test
	void sum(){
		int result = 2+3;
		assertEquals(5,result)
	}
}
```

## 주요 메서드
|메서드|설명|
|--------|---------|
|```assertEquals(expected,actual)```|if actual is same with expected|
|```assertNotEquals(expected,actual)```|if actual is not same with expected|
|```assertSame(Object expected, Object actual)```|if objects are same(```check object equals```)|
|```assertNotSame(Object expected, Object actual)```|not same|
|```assertTrue(boolean condition)```|true?|
|```assertFalse(boolean condition)```|false?|
|```assertNull(Object actual)```|null?|
|```assertNotNull(Object actual)```|not null?|
|```fail()```|테스트 실패처리|

### 사용 상황
#### assertEquals
```java
@Test
void 같은_날짜인지_확인(){
	// LocalDateTime은 mutable 내부 equals 사용
	LocalDateTime t1 = LocalDateTime.now();
	LocalDateTime t2 = LocalDateTime.now();
	assertEquals(t1,t2);
}
```
```assertEquals```메서드는 object의 equals 메서드를 호출하여 값을 비교한다. 따라서 ```immutable```한 객체를 가진 값을 test하기 위해서는 ```equals``` 와 ```hashcode```의 오버라이딩이 필요하다.

#### fail
```java
try{
	AuthService authService = new AuthService();
	// if null, exception occured 
	authService.authenticate(null, null);
	fail() 	
}catch(Exception e){
	e.printstacktrace();
}
```
굳이 ```fail``` 메서드를 사용한다면 , 위처럼 ```exception``` 예정 로직에서 정상적으로 예외처리가 되지 않을 때 테스트 실패처리를 하는 방법이 있다.
하지만 이 방법보다 명확하게 ```assertThrows``` 메서드를 사용하는 것이 더 효과적이다.
#### assertThrows
```java
assertThrows(IllegalArgumentException.class, () -> authService.authenticate(null,null));
``` 
```execute``` 메서드를 받는 ```assertThrows```메서드를 활용하여 바로 예외처리를 터트려서 test를 통과시킬 수 있다.

### assertAll
```java
assertAll(
	() -> assertEquals(3,6-2), // test failed 하지만 멈추지 않고 다음 test가 진행된다.
	() -> assertEquals(3, 5-2)
)
```
한번쯤은 테스트코드 돌리다가 첫 테스트부터 실패해서 다음 테스트는 성공인지 아닌지 알지 못한 경우가 있었을 것이다. 그런 문제를 해결할 수 있는 메서드가 바로 ```assertAll``` 메서드 이다.

## 테스트 라이프사이클
### Junit의 라이프사이클은 다음과 같다.
1. 테스트 메서드를 포함한 객체 생성
2. @BeforeEach 애노테이션이 붙은 메서드 생성 (if exist)
3. @Test 매노테이션이 붙은 메서드 생성 (순서 보장 x)
4. @AfterEach 애노테이션이 붙은 메서드 생성 (if exist)
```java
public class LifeCycleTest{
	public LifeCycleTest(){
		System.out.println("Test start");
	}
	@BeforeEach //각 test 메서드 시작시 실행 (기본 값 set up, 테스트간 의존도 x) 
	void setUp(){
		System.out.println("set up");
	}

	@Test
	void a(){
		System.out.println("a");
	}

	@AfterEach //각 test를 마치고 실행 (테스트간 의존도 x)
	void tearDown(){
		System.out.println("tear down");
	}
}
```
### 출력 결과
```java
console-----------------------------------
Test start
set up
a
teardown
------------------------------------------
```
테스트는 절대 필드를 공유해서는 안된다. ```@Test``` 의 순서가 보장되지 않을 뿐더러 특정 test로 인해 또 다른 test 코드가 error가 날 수 있기 때문이다. 따라서 필드를 공유한다거나, 순서를 예상하고 test코드를 작성하는 일은 지양하도록 하자.

## @DisplayName , @Disabled
한번에 모든 테스트코드를 돌릴 경우 **이테스트가 뭐였지?** 하는 의문이 생길때도 있다. 혹은 모든 테스트를 돌리고 싶으나 몇몇 테스트는 제외하고 싶다! 할때 ```@DisplayName , @Disabled``` 애노테이션을 사용할 수 있다.
```java
@DisplayName("멤버 서비스 로직 테스트") // Test result에 멤버 서비스 로직 테스트라고 뜬다.
public MemberServiceTest(){
	@DisplayName("멤버생성")
	@Test
	void create(){
	}
	
	@Disabled // 해당 테스트 메서드 실행 x
	@Test
	void delete(){
	}
}
```

Junit에도 좋은 메서드들이 많다. 다만 ```assertJ``` 가 더 다양하고 직관적이다는 느낌은 든다.
