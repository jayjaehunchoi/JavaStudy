# 간단한 코드를 캡슐화하여 객체지향적 코드를 짜보자!

## Timer
#### 기존 코드
```java
public class PracticeCapsule{
	public static void main(String[] args) {

		long startTime = System.currentTimeMillis(); // 시작시간
		long stopTime = System.currentTimeMillis(); // 멈춘시간
 		long elapsedTime = stopTime - startTime; // 총 걸린 시간
		System.out.println(elapsedTime);
	}
}
```
#### 위와 같은 코드의 문제점은 무엇일까?
내가생각하는 문제점은 다음과 같다.
1. 만약 시간계산하는 단위가, miili초에서 변한다면? 해당 코드를 사용한 모든 곳을 돌며 단위를 바꿔줘야한다.(시간적 낭비가 비용적 낭비로 이어진다.)

#### 캡슐화 후 코드
```java
class Timer{
    private long startTime;
    private long stopTime;
    
    public void start() {
    	this.startTime = System.currentTimeMillis();
    }
    public void stop() {
    	this.stopTime = System.currentTimeMillis();
    }
    public long elapsedTime(TimeUnit unit) {
    	switch(unit) {
    	case MILLISECONDS:
    		return stopTime - startTime;
    	case NANOSECONDS:
    		return (long) (((stopTime/Math.pow(10, 6)) - (startTime/Math.pow(10, 6)))/Math.pow(10, 6));
    	default:
    		return -1;
    	}
    }
    public long getElapsedTime(TimeUnit unit) {
    	long elapsedTime = elapsedTime(unit);
    	if(!isInputTimeUnitSupport(elapsedTime)){
    		throw new IllegalStateException("지원하지 않는 시간 단위입니다.");
    	}
    	return elapsedTime;
    }
    public boolean isInputTimeUnitSupport(long elapsedTime) {
    	if(elapsedTime == -1) {
    		return false;
    	}
    	return true;
    }
    
}
public class PracticeCapsule{
	public static void main(String[] args) {
		
		Timer t = new Timer();
		t.start();
		t.stop();
		System.out.println(t.getElapsedTime(TimeUnit.MILLISECONDS));
		
	}
}
```
#### 해결 방법
1. Time 클래스를 만들어 start, stop 타임을 메서드 하나로 기능 구현을 한다.
2. 추가적으로 언제든 단위 변경이 되어도 이를 사용할 수 있게끔한다. 또다른 방법은 Timer클래스 내부에서 단위를 결정짓는 방법이있다.
