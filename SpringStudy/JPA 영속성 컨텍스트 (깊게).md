# 영속성 컨텍스트 1차 캐시
JPA를 이용하여 프로젝트도 진행해봤고 나름 공부도 열심히하려고 노력해서 영속성 컨텍스트에 대해서는
간단히 이해하고 있다. db에서 데이터를 가져오면 영속성 컨텍스트 1차 캐시에 ```key```, ```value```
형태로 캐싱되어 트랜잭션 내에서 다시 값을 찾아올 때, 똑같은 값을 탐색한다면 쿼리를 안날리고, 
캐싱되어있는 값을 가져와 그대로 활용한다.

하지만 이정도만 알고 프로젝트를 진행하다보니, 쿼리가 왜 안나가는지, 왜 원하는대로 작동을 하지 않는지
알 수가 없었다. 이번 JPA 포스팅 시리즈로 한 번 영속성 컨텍스트에 대해 상세히 알아보고자 한다.

## 기본 셋팅
### domain
```java
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
@ToString
@Getter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    public User(String username) {
        this.username = username;
    }

    public void updateName(String username){
        this.username = username;
    }
}
```

### repository
```java
public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);
}

```

### init
```java
// main 클래스 내부에 생성했다.
@Component
@RequiredArgsConstructor
static class init{

    private final UserRepository userRepository;

    @PostConstruct
    public void init(){
        User user = new User("최재훈");
        userRepository.save(user);
    }
}
```

> 기본 셋팅으로 User Entity와 repository 그리고 기본 값으로 ```최재훈``` 이라는 이름을 가진 
> 데이터를 저장해둔다.

이제 서비스 로직을 짜보면서 쿼리를 확인해보자

### Service
```java
@Transactional(readOnly = true)
public User findById(Long id){
    userRepository.findById(id).orElse(null);
    return userRepository.findById(id).orElse(null);
}
```
먼저 이 로직을 수행하기전 이 메서드에 대해 해석해보자,
간단하게 똑같은 식별자 값으로 두 번 조회를 실시한다. 과연 두 번 쿼리가 나갈까?

```java
Hibernate:
    select
        user0_.id as id1_2_0_,
        user0_.username as username2_2_0_ 
    from
        user user0_ 
    where
        user0_.id=?
```

쿼리는 한 번 나간다. 처음 설명했듯, 두 번째 조회 시점에 영속성 컨텍스트에 1차 캐시된 값을 가져오기 때문에
다시 쿼리가 나가지 않는 것이다. 그렇다면 이런 메서드는 어떨까?

```java
@Transactional(readOnly = true)
public User findByTest(Long id, String username){
    User findUser = userRepository.findById(id).orElse(null);
    User findMember = userRepository.findByUsername(username);

    log.info("동일한 엔티티를 가져왔는지 확인한다 = {},{}",findUser, findMember);
    return findMember;
}
```
id값으로 한 번, username으로 한 번 조회한다. 이때 동일한 엔티티를 가져오기 위해 다음과 같이 세팅한다.

![image](https://user-images.githubusercontent.com/87312401/144089271-dd727fb6-77c1-4b76-acc1-0473a2354b6c.png)

로그를 통해 동일한 값을 가져왔는지 먼저 확인하자
```
동일한 엔티티를 가져왔는지 확인한다 = User(id=1, username=최재훈),User(id=1, username=최재훈)
```
같은 값을 가져왔다. 그렇다면 당연히 쿼리도 한 번 나갔을까? 

```
Hibernate: 
    select
        user0_.id as id1_2_0_,
        user0_.username as username2_2_0_ 
    from
        user user0_ 
    where
        user0_.id=?
Hibernate: 
    select
        user0_.id as id1_2_,
        user0_.username as username2_2_ 
    from
        user user0_ 
    where
        user0_.username=?
```

답은 **아니다** 쿼리는 두 번 나갔다.  
이는 영속성 컨텍스트 1차 캐시의 형태 때문이다.  
영속성 컨텍스트 내에 저장되어있는 값은 ```key, value``` 형태라고 처음에 말했다.
이 떄 ```key```는 식별자 (pk) 값이다. 즉, username으로는 map에서 정보를 찾을 수 없는 것이다.

그렇다면 순서가 바뀐다면 어떨까?

```java
@Transactional(readOnly = true)
public User findByTest2(Long id, String username){
    User findMember = userRepository.findByUsername(username);
    User findUser = userRepository.findById(id).orElse(null);
    log.info("동일한 엔티티를 가져왔는지 확인한다 = {},{}",findUser, findMember);
    return findMember;
}
```

```
Hibernate: 
    select
        user0_.id as id1_2_,
        user0_.username as username2_2_ 
    from
        user user0_ 
    where
        user0_.username=?
```

이 때 쿼리는 단 한 번 나간다. 방금 전 설명을 되새겨보자.
```username```으로 조회한 엔티티는 ```User(id=1, username=최재훈)``` 과 같은 형태로 저장된다.
이후 ```id```값으로 조회한다면, key값 1에 걸려 1차 캐시에서 Entity를 가져올 수 있다.

또 이런 경우도 있다.
```getBy```와 ```findBy```의 차이이다.
```getBy```는 내부 동작원리상 1차 캐시에 값이 존재하지 않으면 프록시를 호출한다. 
```java
@Transactional(readOnly = true)
public User findGetByTest2(Long id){
    User getUser = userRepository.getById(id);
    User findUser = userRepository.findById(id).orElse(null);

    log.info("동일한 엔티티? = {},{},{}",findUser, getUser, findUser == getUser);
    return findUser;
}
```
하지만 이런 경우에 ```findBy```는 프록시를 그대로 반환해준다. 만약 DB쿼리에서 직접 값을 가져오면
저 둘의 비교가 틀리겠지만 프록시를 그대로 반환해주기 때문에 로그는 이렇게 남는다.

```
동일한 엔티티? = test.ajaxserver.jpatest.User@f230a4f,test.ajaxserver.jpatest.User@f230a4f,true
```

> 하지만 get은 lazyload 대상이다 따라서 hibernate lazy관련 세팅을 해주지 않으면 ```InvalidaDefinitionException```이 터진다.
> 굳이 get을 이용할 이유는 없어보인다.

## 마무리
JPA의 기본은 영속성 컨텍스트라고 생각한다. 이 내용을 잘 알아야, 더티 체킹이 왜 발생하는지, 어떤 코드가
왜 안티 패턴인지 알 수 있게된다. 다음 시간에는 ```update```관련하여 포스팅을 해보겠다!
