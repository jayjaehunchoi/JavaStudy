## 자바 코드 작성 연습
작년 우아한테크코스 racing car 자바 구현 문제를 코드로 작성해보았다.
### 구조
![image](https://user-images.githubusercontent.com/87312401/135377045-43788f34-d452-4f4c-a0ea-d5690cbeca9e.png)  
단방향으로 참조하는 구조를 만들어보았다.

### 기능 구현
#### Validator
validating 해야 할 내용이 출전할 자동차 이름과 시도 회수였다. 
먼저 어떤 Exception을 줘야할지 고민하고 Exception 패키지를 만들어 각 클래스로 관리했다.
Exception Link : <https://github.com/jayjaehunchoi/JavaStudy/tree/main/RacingGame/src/exception>

**validation시 고려한 점은 다음과 같다**
* 아래 validation을 boolean 형태로 반환하여, false시 다시 이름, 시도회수 입력이 가능하게 설계하였다.
1. Racing Car이름 입력에 넣어준 validation은 **Comma가 존재하는지 , 이름이 비어있지 않은지, 이름의 길이가 5자 초과인지, 중복된 이름이 존재하는지** 여부였다.
2. 시도 회수 입력에 넣어준 validation은 **숫자 값인지, 시도회수가 0 초과인지** 여부 였다.

```java
// 예시 코드 : 숫자값인지, 아닌지 확인하기 위해 Character의 isDigit() 메서드를 사용하였다, 정수형인지 아닌지 확인하여 아니면 false를 반환한다.
	public static boolean isTryTimeNumber(String times) {
		for(int i  = 0 ; i < times.length(); i++) {
			if(!Character.isDigit(times.charAt(i))) {
				try {
					throw new TryTimesNotNumberException("[ERROR] 시도회수는 숫자여야 합니다.");
				}catch (Exception e) {
					System.out.println(e.getMessage());
					return false;
				}
			}
		}
		return true;
	}
```

#### Service
하나의 패키지로 관리하기에는 생각보다 로직이 길어지고 많아져서 service 패키지를 만들어 nameService, timesService, RacingService를 만들어 로직을 작성했다.
service Link : <https://github.com/jayjaehunchoi/JavaStudy/tree/main/RacingGame/src/service>

**nameService, timeService** 클래스에는 반복문을 돌며 validating을 하는 과정이 상세하게 담겨있다.
**racingService** 클래스에는 random값으로 이동인지 아닌지 확인하고 이를 print해주는 로직이 담겨있다.

**else 사용을 지양하라고 하였으나 , 단 한 번 else를 사용했다**
```java
// 복수의 최종 승리자를 확인하는 과정을 else를 사용하지 않고 할 수 있을까에 대해 고민을 많이 하였다, 하지만 잘 떠오르지는 않았다.
private void findWinner(){
		List<String> winners = new ArrayList<String>();
		int max = 0;
    
		for(int i = 0 ; i < Racingcar.cars.size(); i++) {
			Car car = Racingcar.cars.get(i);
			if(car.getPosition() > max) {
				max = car.getPosition();
				winners.clear();
				winners.add(car.getName());
			}else if(car.getPosition() == max) {
				winners.add(car.getName());
			}
		}
		System.out.print(FINAL_WINNER_IS);
		printWinner(winners);
		
	}
```

### racingCar
해당 패키지에는 주어진 Application 클래스와 Car 클래스가 있었고, 또 자체적으로 Racingcar라는 클래스를 만들어 게임을 작동하는 메서드를 만들었다.
Car 클래스에는 **setPosition 사용 제한**이라는 지문이 있었기 때문에 아래와 같이 position 이동 메서드를 만들었다. set보다는 명확하게 의미를 담은 메서드를 만들라는 의미로 받아들였다.

```java
 public void goForward() {
    	this.position++;
    }
```
**Racingcar** 클래스에는 Scanner를 받는 생성자를 하나 만들어주고 각 Service들을 참조하였다, 사실 의존성을 주입받는 형태로 사용할 수 도 있으나
그렇게 되면 매개변수의 길이가 다소 길어질 수 있어 우선 사용하지 않았다. 문제의 코드는 다음과 같다.

```java
// 의존성을 생성자에 넣어 주입을 받을지, 이 방법을 사용해야할지, 어떤게 좋은지 잘 모르겠다..
  NameService nameService = new NameService();
	TryTimesService tryTimesService = new TryTimesService();
	RacingService racingService = new RacingService();
	Scanner scanner;
	
	
	public Racingcar(Scanner scanner) {
		this.scanner = scanner;
	}
```

### 마치며
재밌는 경험이었다. 약 1시간 30분정도 걸렸는데, 초기에 잘 설계를 했으면 시간을 훨씬 줄였을 것 같다. 만들다가 마음에 안들면 바꾸고, 이런 방식이 좋지는 않은 것 같다.
좋은 코드인지는 모르겠으나 우선 자바 코드 작성 완료



