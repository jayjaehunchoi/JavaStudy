# 당신의 더티체킹이 실패하는 이유

프로젝트를 하면서 update 쿼리가 안나간다고 도움을 요청하는 경우가 꽤나 있었다.
하지만 하나의 ```update```메서드에 ```update``` 기능만 잘 수행시켜 ```transaction```이 종료되면 업데이트 쿼리가 안나가는 일은
거의 없다고 보면된다.

그렇다면 update 쿼리가 안나가는 상황을 모두 살펴보고 개선해보자

1. @Transaction(readOnly = true)
```java
@Transactional(readOnly = true)
public User updateName(Long id,String name){
    User user = userRepository.findById(id).orElse(null);
    log.info("업데이트 전 user 상태 = {} ", user);
    user.updateName(name);
    log.info("업데이트 후 user 상태 = {} ", user);
    User notUpdated = userRepository.findByUsername("최재순");
    log.info("데이터에 있는값 ? {}", notUpdated);

    return user;
    }
```
설정 이름만 봐도, update쿼리가 안나갈 것 같다 ```spring data jpa``` spec에서 readOnly 부분의 일부를 발췌해봤다.
> when used with Hibernate, the flush mode is set to NEVER when you configure a transaction as readOnly, which causes Hibernate to skip dirty checks (a noticeable improvement on large object trees).
> > readOnly 설정을 하면, flushMode 는 NEVER로 선언된다. 즉 액션 큐에 담기는 쿼리가 안나간다.
> > 따라서 더티체킹이 스킵된다 (데이터를 조회할 때만 사용해야 한다, 하나하나 더티체킹하지 않기 떄문에 성능에 큰 이득이 있다.)

```jpa```를 조금이라도 사용해봤다면 이런식의 실수는 잘 하지 않을 것이다.
또, 이런 경우를 보기도 했다.

2. Repository Test 에서의 dirty checking
```java
@Rollback(false)
@Transactional
@Test
void update(){
    System.out.println("===");
    User user = userRepository.findAll().get(0);
    System.out.println("===");
    user.updateName("최재순");
    System.out.println("===");
    User findUser = userRepository.findById(user.getId()).orElse(null);
    assertThat(findUser.getUsername()).isEqualTo("최재순");
    System.out.println(findUser.getUsername());
}
```
> 메서드 종료 후 flush를 확인하기 위해 Rollback 옵션을 꺼두자.

콘솔에 출력되는 로그를 확인해보자  
![image](https://user-images.githubusercontent.com/87312401/144169173-28e0ba73-d37b-46c3-a9e4-b894bbee0f8f.png)

이렇게 쿼리가 발생하는 이유를 알기 위해 ```flushMode.AUTO```의 설정을 알아야 한다.
기본적으로 ```hibernate```를 사용하면 ```flushMode.AUTO```로 세팅된다. 
```update```, ```delete```, ```insert```등의 쿼리는 ```ActionQueue```순서에 맞게 메서드가
종료된 뒤 호출된다. (이 때도, 여러개의 쿼리가 복합적으로 들어있으면 flush가 되지 않는다.)

즉, ```Repository```단계에서 굳이 업데이트를 테스트 하지 않으면 이 문제는 해결된다.
하지만 ```TDD```를 하고 있다면
1. ```update```로직 수행 후 , ```flush()```를 명시적으로 날린다.
2. ```update``` 로직 수행 후 다음 조건에 맞는 로직을 추가 수행시켜준다.
> 2번 참고
> * prior to committing a Transaction
> * **prior to executing a JPQL/HQL query that overlaps with the queued entity actions** (추천)
> * before executing any native SQL query that has no registered synchronization

### 마무리
왜 쿼리가 안나가지? 라고 고민을 많이 했었다. 감으로 ```flush``` 문제임은 예측했기때문에,
그럴때마다 명시적으로 ```flush```를 날려줬다. 애초에, ```flush```를 작성하지 않아도
쿼리가 잘 나가는 코드를 짜야, ```JPA```를 완벽 이해하고 사용한다고 볼 수 있겠다.

참고 :
https://docs.jboss.org/hibernate/orm/5.3/userguide/html_single/Hibernate_User_Guide.html#flushing-order  
https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#dependencies  
https://stackoverflow.com/questions/56812221/jpa-auto-flush-before-any-query
