# Stream docs 파헤치기

로또 미션을 진행하면서 의도적으로 **Stream API**를 사용하려고 노력했는데요.
지금까지 개념적 정립이 되지 않은 상태에서 **Stream**을 사용했다보니 어려움을 많이 겪었습니다.

오늘 포스팅에서는 Stream docs를 파헤쳐서 Stream에 대해 이해하고자 합니다.

### Stream이란?
**Stream**은 컬렉션에서 ```map, reduce``` 처럼 요소의 스트림에 기능적 작업을 지원해주는 클래스입니다.

```java
int sum = widgets.stream()
                  .filter(b -> b.getColor() == RED)
                  .mapToInt(b -> b.getWeight())
                  .sum();
```

위 코드를 보면, ```Collection<Widget>```의 인스턴스를 ```stream``` 소스로 변경하여 빨간색을 갖는
내부 객체의 무게 합을 구하는 **reduction** 과정입니다.

이처럼 일반 반복문으로 작성하면 indent가 깊어지는 코드를 간소화 할 수 있는 기능이 있습니다.

### Stream은 Collections가 아니다

**Stream**은 **Collections**와 명백한 차이가 있습니다.

* 먼저, storage가 없습니다. **Stream**은 자료를 저장하는 자료구조가 아니고 자료구조처럼 소스의 요소를 전달하는 역할을 합니다.
* **Stream**은 연산에 따른 결과는 생성하지만 그 값을 변경시키지는 않습니다. ```RED``` 색을 필터링 한다고 해서 자료구조 내 값이 수정되는 것은 아니죠.
* 지연실행을 통해 최적화를 하고자 합니다. **Stream**연산은 중간연산(```map)과 종료연산(```forEach(), count()``` 등)으로 나뉘어지는데, 중간 연산은 항상 Lazy를 유지합니다.
* **Stream**은 무제한인 연산이 가능합니다. ```limit(), findFirst```와 같은 연산이 무제한 stream을 유한한 시간에 완료되도록 만듭니다.
* **Stream**은 소모품입니다. 다시 이전 요소로 방문하려면 새로운 stream을 열어야 합니다.

### Stream 파이프라인과 작업

![이름 없는 노트북 (1)-18](https://user-images.githubusercontent.com/87312401/155453697-3a2c00d6-c15c-496e-a8a9-22fcfdb0137b.jpg)

**Stream**작업은 크게 중간, 종료 작업으로 나뉩니다. 모든 중간 작업은 **lazy** 하게 진행되기 때문에 종료작업이 호출될 때 까지 실제
파이프라인이 실행되지 않습니다. 종료 작업 같은 경우에는 ```Iterator, spliterator```를 제외하고 **eager**하게 작업이 진행되기 때문에 이 때 파이프라인이 실행되고 결과를 반환합니다.
종료 작업이 실행되면 **Stream**은 종료되고 다시 파이프라인을 반복할 수 없습니다.

대부분의 중간 작업이 **Stateless** 하기 때문에 이전 Stream의 상태를 모른다면 ```distict, sorted```와 같이 이전 상태를 기억해야 하는 **StateFul**한 작업도 존재합니다.
이 작업들은 종료 작업이 호출되기 전에 실행되어 병렬상태에서 상태 저장, 데이터 버퍼링 등이 필요합니다.

### 병렬성

본질적으로 **for-loop**는 직렬적입니다. 하지만 **Stream**은 개별 요소에 대한 명령 작업이 아닌 파이프라인 작업이기 때문에 병렬작업이 용이합니다.

아래 코드를 통해 ```isParallel()```이 **true**인 **Stream**을 받아 작업을 실행시킬 수 있습니다.
```java
int sumOfWeights = widgets.parallelStream()
                       .filter(b -> b.getColor() == RED)
                       .mapToInt(b -> b.getWeight())
                        .sum();  
```

### 무상태, 무간섭

**Stream**을 사용하면 **ArrayList**처럼 thread-safe하지 않은 컬렉션에도 병렬 작업을 할 수 있습니다.
하지만 하나의 조건이 있습니다. 파이프라인이 진행되는 동안 기존 소스의 변환이 없어야 합니다.

아래 코드를 통해 문제를 확인해봅시다.

```java
List<String> l = new ArrayList(Arrays.asList("one", "two"));
Stream<String> sl = l.stream();
l.add("three");
String s = sl.collect(joining(" "));
```

과연 **s** 값은 무엇일까요? 바로 **one two three**입니다. 종료작업이 진행되기 전에 **Stream**하기 전 자료구조가 수정되었으니
종료시점에 실행되는 stream이 three를 포함하여 ```collect```메서드를 실행한 것입니다.

무상태를 유지해야 하는 이유도 이와 유사합니다. 병렬 환경에서 실행되는 람다가 상태를 저장한다면 어떤 상태가 저장될지 누구도 예상 못하게 되는 것이죠.

### 부작용

병렬성 보장으로 인한 부작용도 역시 존재하는데요 아래 코드를 통해 확인해봅시다.

```java
IntStream.range(0,5).parallel().map(x -> x*2).forEach(System.out::println)
```

이 코드의 출력 내용은 어떨까요? 병렬로 진행되기 때문에 순서가 뒤죽박죽 나올 것입니다.

또 하나 더 보시죠.
```java
ArrayList<String> results = new ArrayList<>();
stream.filter(s -> pattern.matcher(s).matches())
   .forEach(s -> results.add(s));  // Unnecessary use of side-effects!
```
이 코드가 병렬로 실행된다면 어떨까요? ArrayList는 thread-safe하지 않기 때문에 forEach가 실행되면서 이상한 값들이 들어갈 수 있게 되는 것이죠.

아래처럼 개선하면 불필요한 부작용 없이 문제를 해결할 수 있습니다.

```java
List<String>results = stream.filter(s -> pattern.matcher(s).matches())
                            .collect(Collectors.toList());
```

