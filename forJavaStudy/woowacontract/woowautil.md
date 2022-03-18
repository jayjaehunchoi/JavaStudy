# 핵심 정리

### **Console**

### **`readLine()`**

줄 바꿈 문자를 제외하고 현재 줄의 나머지 부분을 반환한다.

`Console.readLine();`  → nextLine을 사용함으로써 스트림에 존재하는 개행 해결

- 새로운 테스트가 실행될 때 readInput()에서 IOException이 잡힌다.  실제 객체를 close시키지 않고 sourceClosed를 close하여 source를 제대로 읽지 못하는 현상이 생김

따라서 Reflection을 열어 해당 필드를 확인하고 객체를 재생성함.
- reflection은 잘사용하지 못할 때 사용하지 않는 것.

### **Randoms**

- ThreadLocal.current() 를 사용함으로써 ThreadSafe보장

### **`pickNumberInList(List)`**

목록에 있는 숫자 중 하나를 반환한다.

`Randoms.pickNumberInList(Arrays.asList(1, 3, 10)); // 1
Randoms.pickNumberInList(Arrays.asList(1, 3, 10)); // 10
Randoms.pickNumberInList(Arrays.asList(1, 3, 10)); // 3`

### **`pickNumberInRange(int, int)`**

숫자 범위를 지정하면 시작 또는 끝 숫자를 포함하여 범위의 숫자를 반환한다.

`Randoms.pickNumberInRange(1, 10); // 1
Randoms.pickNumberInRange(1, 10); // 10
Randoms.pickNumberInRange(1, 10); // 4
Randoms.pickNumberInRange(1, 10); // 5`

### **`pickUniqueNumbersInRange(int, int, int)`**

숫자 범위 내에서 지정된 개수만큼 겹치지 않는 숫자를 반환한다.

`Randoms.pickUniqueNumbersInRange(1, 10, 2); // [1, 2]
Randoms.pickUniqueNumbersInRange(1, 10, 5); // [1, 10, 7, 8, 5]`

### **`shuffle(List)`**

무작위로 섞인 새 목록을 반환한다.

`Randoms.shuffle(Arrays.asList(1, 2, 3, 4, 5)); // [2, 4, 1, 3, 5]`

### NsTest

1. 테스트 픽스쳐의 유연함, BeforeEach,  AfterEach를 사용함으로서 독립적인 테스트 환경 유지
2. 자바의 System.out의 스트림을 `ByteArrayOutputStream` 로 변경, 종료 후 콘솔에 output을 볼 수 있게 기존 default System.out 스트림으로 변경
3. runException이 따로 존재하는 이유는 ? 에러를 catch하기 때문에 다음 input들에 대한 대비가 필요하다. `NoSuchElementException` 을 무시해야 하기 떄문!
4. 들어온 args를 개행문자로 join하여 바트어레이인풋스트림에 담아둔다. → 개행 Console.readLine 과의 연관성

runMain을 재정의함으로써 원하는 main 을 실행

### Assertions

*`assertTimeoutPreemptively` - 시간초과가 되는 순간 터짐 (assertTimeout은 시간 초과가 되더라도 종료될 때 까지) 새로운 Thread에서 테스트를 진행함, 독립된 테스트 유지* 

`MockedStatic` - 현재 스레드와, try블럭 내부에서만 모킹, 독립된 테스트 유지
