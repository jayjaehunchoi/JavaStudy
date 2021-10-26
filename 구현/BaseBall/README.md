# 자바 코드 작성 연습
작년 우아한테크코스 1주차 java 구현 문제를 코드로 작성해보았다.

## 기능 구현
### Validator.java
3가지 숫자를 조건에 부합하게 만들기 위해서는 
1. Computer가 생성하는 난수에 **동일한 숫자가 여러번 들어갔는지** 확인해야한다.
2. Player가 입력한 수에 **3자리 수인지, 0이 포함되어있는지, 동일한 숫자가 여러번 들어갔는지** 확인해야한다.

따라서 아래와 같이 Validator 클래스에 3가지 메서드를 추가하였다.
```java
// 체크해야하는 순서를 나름대로 선정하여 해당 순서대로 메서드를 작성하였다.
public static void isLengthNotThree(List<Integer> list) {
  if(list.size() != INPUT_LENGTH) {
    throw new IllegalArgumentException("3자리 숫자를 입력해주세요.");
  }
}

public static void isZeroExist(List<Integer> list) {
  if(list.contains(0)) {
    throw new IllegalArgumentException("0을 제외하고 입력해주세요.");
  }
}

public static void isDuplicatedNum(List<Integer> list) {
  Set<Integer> set = new HashSet<>();
  for(int i = 0 ; i < list.size(); i++) {
    set.add(list.get(i));
  }
  if(set.size() != INPUT_LENGTH) {
    throw new IllegalArgumentException("반복된 숫자가 있습니다.");
  }
}
```
### Computer.java
컴퓨터는 랜덤으로 난수를 발생시켜 3자리 수를 만든다. 이때 **Constant.java**에 선언해둔 final 상수 덕분에 0이 나오는 경우는 고려하지 않아도 된다. Computer에서 고려한 점은 다음과 같다.
1. 3회 반복문을 돌려 3자리 수를 만든다.
2. **isDuplicatedNum** 메서드를 실행시켜 오류가 발생하면 Computer의 computerTurn 메서드에서 다시 난수를 발생시키도록 코드를 작성하였다.

Computer.java : <https://github.com/jayjaehunchoi/JavaStudy/blob/main/BaseBall/src/main/java/baseball/Computer.java>

### Player.java
위에서 언급했듯이 player는 직접 수를 입력하기 때문에 Validator에서 선언한 메서드를 모두 고려해야 한다.
1. error가 catch되면 player가 수정해야 할 내용을 보낸 뒤 다시한번 새로운 수 입력 요청을 보내고, scanner 를 통해 입력값을 input받는다.

Player.java : <https://github.com/jayjaehunchoi/JavaStudy/blob/main/BaseBall/src/main/java/baseball/Player.java>

### BaseBallGame.java
Baseball game이 작동하는 메서드가 포함되어 있다. 이 클래스에는 게임의 핵심 로직인 정답 확인, 3스트라이크, 4볼 여부, 재시작 or 종료 여부, 재시작 시 초기화 메서드가 포함되어 있다.
1. checkAnswer 메서드를 선언하여, 정답을 확인하고 동적으로 return문을 발생시켜준다. 이때, index - element가 동시에 같은것이 우선순위이다. 코드는 아래와 같다.
```java
public String checkAnswer(List<Integer> playerList, List<Integer> computerList) {
  // 동적으로 String을 return해주기 위한 bool타입 변수
  boolean strikeCheck = false;
  boolean ballCheck = false;
  
  for(int i = 0 ; i < INPUT_LENGTH; i++ ) {
    if(playerList.get(i) == computerList.get(i)) { //스트라이크 확인이 우선순위
      Player.strike++;
      strikeCheck = true;
    }else if(computerList.contains(playerList.get(i))){ // 스트라이크가 아닐경우 볼을 확인한다.
      Player.ball++;
      ballCheck = true;
    }
  }
  // bool 타입 변수를 활용하여 동적으로 String 생성
  if(strikeCheck && ballCheck) {
    return Player.ball+BALL_HAPPEND+" "+Player.strike+STRIKE_HAPPEND;
  }else if(strikeCheck) {
    return Player.strike+STRIKE_HAPPEND;
  }else if(ballCheck){
    return Player.ball+BALL_HAPPEND;
  }
  return NOTHING;
}
```

2. 조건에는 Strike가 3이 되는 순간 게임을 종료하지만, 통념상 4볼이 될때 게임이 종료되는 것이 맞다고 판단하여 4볼의 경우를 추가하였다.
3. 이때, 동시에 3스트라이크 이상, 4볼 이상이 체크되는 경우가 있어 OverFlow를 막는 메서드를 추가하였다.

```java
public void blockOverFlow() {
		if(Player.strike >= 3) {
			Player.strike = 3;
		}
		if(Player.ball >= 4) {
			Player.ball = 4;
		}
	}
```
**추가적인 기능은 아래 링크 참조**
BaseBallGame.java : <https://github.com/jayjaehunchoi/JavaStudy/blob/main/BaseBall/src/main/java/baseball/BaseBallGame.java>




