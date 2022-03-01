# Optional

자바 8 이전에는 메서드가 특정 조건에서 값을 반환할 수 없을 때 취할 수 있는 선택지가 두가지 있었습니다.

1. null 반환
2. Exception 호출

하지만 **null**을 반환하는 코드는 null을 처리하기 위한 추가 로직히 필요할 뿐 아니라, 만약 null이 제대로 처리되지 않을 경우 **NullPointerException** 이 발생할 가능성이 커지게 되죠.

또, 예외를 던지는 것은 JVM이 비정상적인 상황이라고 판단하고 최적화를 해주지 않기 때문에 그 비용이 꽤 큽니다.

그러나 자바 8 버전이 등장하면서 **Optional**이라는 새로운 선택지가 생겼습니다.

## Optional

![스크린샷 2022-03-01 오후 9 48 59](https://user-images.githubusercontent.com/87312401/156171800-2685e18f-fff2-43c4-a49a-e29f446ce14b.png)

자바 docs에서 제공하는 설명에 따르면 **Optional**은 null이 아닌 값을 포함하거나, 포함하지 않을 수 있는 컨테이너 개체라고 합니다.
**Optional**이 생성된 의도는 null을 사용하면 문제가 생길 가능성이 있는 상황에 **"결과 없음"** 을 반환하기 위함입니다.

즉, null을 발생시키는 것이 아닌 **정해진 객체** 혹은 **유연하게 exception**을 발생시키는 것이죠.

> docs를 읽어보면 Optional은 설계 자체가 null을 반환하지 않기 위해 사용됩니다.
> Optional.ofNullable() 를 사용하면 null값을 허용하기는 하지만 Optional의 개념과는 완전히 상반됩니다.

그렇다면 **Optional**을 어떻게 활용할 수 있을까요?

## Optional 활용하기

### Optional 생성
```java
// 만약 Optional.of(null); 이면 nullPointerExaception
Optional<String> opt = Optional.of("val");

// null 가능한 생성
Optional<String> optNullable = Optional.ofNullable(null);
```

### Optional 값 가져오기
```java
Optional<String> opt = Optional.of("val");
opt.get(); // return "val"

Optional<String> optEmpty = Optional.empty();
optEmpty.get() // return NoSuchElementException
```

```NoSuchElementException```을 방지하기 위해 값이 있는지, 없는지 확인해야합니다.

```java
opt.isPresent(); // 존재?
opt.isEmpty(); // 자바 11부터, 존재하지 않음?
```

존재하는 것만 확인하지 않고, 존재하면 특정 로직을 수행하게끔 만들어줄 수 있습니다.
```java
opt.isPresent(val -> print(val));
opt.isPresentOrElse(val -> print(val), () -> throw new IllegalArgumentException());
```

존재하지 않으면 특정 로직을 수행시켜 주는 예약어도 존재합니다.
```java
opt.orElse("default"); // 기본값 자체
opt.orElseGet(() -> "default") // 기본값
opt.orElseThrow(() -> new IllegalArgumentException()); // 없을때 예외처리
```

> isPresent를 사용하다보면 느끼겠지만 사실상 "isPresent가 false 라면 ~~ 다 " 라는 식의 추가 로직이 구현되어야 합니다.
> 즉, null을 그대로 사용하는 것과 큰 차이가 없는 것이죠. 그렇기 때문에 Optional에서 제공하는 orElse~ 메서드를 잘 활용해야 합니다.


또, **Optional**을 **Stream**처럼 사용할 수도 있습니다.

```java
public class OptionalTest {

    public static void main(String[] args) {
        Optional<User> user = Optional.of(new User("최재훈", LocalDate.of(1998, 1, 8)));
        Integer age = user.map(User::calculateAge)
                .filter(tempAge -> tempAge > 20)
                .orElseThrow(NoSuchElementException::new);

        System.out.println(age); // 24세가 출력됩니다
    }

    static class User {
        private final String name;
        private final LocalDate birthDay;

        public User(String name, LocalDate birthDay) {
            this.name = name;
            this.birthDay = birthDay;
        }

        public int calculateAge() {
            return LocalDateTime.now().getYear() - birthDay.getYear();
        }
    }
}
```

## Optional 주의사항

**Optional**을 사용하는 것이 굉장히 편리할 것 같지만 때로는 득이되지 않을 때도 있습니다. 이런 상황들은 유의해서 사용해야 하는데요.

#### 1. 컬렉션, 스트림, 배열, 옵셔널 같은 컨테이너 타입은 Optional로 감싸면 안됩니다.

말 그대로 이중으로 깜사게 되는 것이죠, 만약 컬렉션에서 null이 나오는 것이 두렵다면 쉽게 ```Collections.emptyList()```를 반환하면 됩니다. 그러면 따로 Optional을 처리하는 코드 없이 Optional을 사용해야 할 이유를 해결할 수 있죠

#### 2. primitive type을 감쌀 때 사용하면 안됩니다.

**Optional**은 null을 반환하지 않을 수 있습니다. 원시 타입을 굳이 박싱하지 않아도 되는 것이죠. 이 때는 굳이 무겁게 **Optional**을 사용하기 보다는 아래처럼 문제를 해결하면 됩니다.
```java
int i = OptionalInt.of(1).orElseGet(() -> 2);
long l = OptionalLong.of(1L).orElseThrow(IllegalAccessError::new);
double v = OptionalDouble.of(1.1).orElse(2.2);
```

#### 3. 웬만하면 반환값을 대체하는 용도로만 사용합시다.

**Optional**의 근본 자체는 null을 반환하면 안되는 상황에 이를 감싸는 용도입니다. **Optional** 자체를 키, 값, 원소로 사용하는 일은 지양해야 합니다.

```java
List<OptionalInt> optionalIntList = new ArrayList<>(); // 말도 안되는 사용법입니다..
```

## 정리
Optional을 사용할 때는 **Optional을 왜 사용해야하지**에 대해 항상 생각하며 사용해야 합니다.
이 주의사항만 머릿속에 유념하면 **Optional**을 똑똑하게 사용할 수 있을 것입니다!