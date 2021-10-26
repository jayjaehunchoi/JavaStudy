# Mockito
Mockito는 모의 객체 생성, 검증, 스텁을 지원하는 프레임워크이다. Mockito를 이용한다면 대역을 원활하게 사용할 수 있을 것이다.
## 의존 추가
```gradle
# gradle
testImplementation 'org.mockito:mockito-core:[version]'
```

## 모의 객체 생성
```Mockito.mock()``` 메서드를 이용하여 모의 객체를 생성할 수 있다.
#### 예시 인터페이스
```java
public interface GameNumGen{
	String generate(GameLevel level);
}
```

#### 모의 객체 생성
```java
@Test
void mockTest(){
	// GameNumGen 인터페이스의 모의 객체를 만들었다.
	GameNumGen genMock = mock(GameNumGen.class);
}
```
모의 객체를 생성했다면 ```BDDMockito.given()``` 메서드를 이용하여 스텁을 구성할 수 있다. 모의 객체에 스텁을 구성하여 특정값을 리턴하도록 만들어보자.

#### Stub 생성
```java
@Test
void mockTest(){
	GameNumGen genMock = mock(GameNumGen.class);
	given(genMock.generate(GameLevel.EASY)).willReturn("123"); //willThrow도 가능
	String num = genMock.generate(GameLevel.EASY);
	assertEquals("123",num);
}
@Test
void mockThrowTest(){
	GameNumGen genMock = mock(GameNumGen.class);
	given(genMock.generate(GameLevel.HARD)).willThrow(IllegalArgumentException.class);
	
	assertThrows(IllegalArgumentException.class, () -> genMock.generate(GameLevel.HARD));
}
```
> Mockito는 일치하는 스텁 설정이 없는 경우 타입의 기본 값을 리턴한다.
> int 인 경우 0, boolean 은 false, 기본 데이터 타입이 아니라면 null 값을 리턴한다.

```ArgumentMatchers``` 클래스에는 임의의 값에 일치하게 만들어주는  메서드도 존재한다.
```java
@Test
void anyMatchTest(){
	GameNumGen genMock = mock(GameNumGen.class);
	given(genMock.generate(any())).willReturn("123"); // any + [type] + () ex) anyString()
	int num = genMock.generate(GameLevel.EASY);
	assertEquals("123",num); // pass
	int num2 = genMock.generate(GameLevel.NORMAL);
	assertEquals("123",num2); // pass
}
```
이처럼 ```any()``` 메서드를 이용하여 임의 값 모두 동일하게 return 해주는 방법이 있다.
두 개 이상의 인자를 적을 때, 하나는 임의 , 하나는 구체 값을 matching 시키기 위해서는 ``` generate(any(),"123"); ``` 이 아닌 ```generate(any(), eq("123"));``` 과 같이 ```eq()``` 메서드를 사용해야 한다.

#### 행위 검증
모의 객체의 역할 중 하나는 실제로 모의 객체가 불렸는지 검증하는 것이다.
```java
@Test  
void init(){  
  GameNumGen genMock = mock(GameNumGen.class);  
  Game game = new Game(genMock);  
  game.init(GameLevel.EASY);  
  
  then(genMock).should().generate(GameLevel.EASY);
  // then -> 검증할 모의 객체
  // should -> 모의객체의 메서드가 불려야 함.불릴 메서드 
}
```

아직  mock에 대한 이해가 조금 부족하지만 반복적으로 test를 작성하면서 mock에 대한 이해를 높여보자

