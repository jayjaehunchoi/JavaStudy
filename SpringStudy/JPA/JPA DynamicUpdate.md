# @DynamicUpdate

일반적인 ```SQL``` ```update```쿼리를 작성할 때, 다음과 같은 요청사항이 있으면 어떻게 쿼리를 작성하는가?

> User 테이블에서 이름을 변경해주세요.

```sql
update user set username = '변경할 이름' where id = '변경할 테이블의 식별자'
```
위와 같이 변경할 컬럼의 데이터만 입력하여 변경시켜준다.
그렇다면 JPA에서 데이터를 업데이트할 때는 어떨까?

### Service
```java
@Transactional
public void updateName(Long id,String name){
    userRepository.findById(id).ifPresent(user -> user.updateName(name));
}
```
> 더티체킹을 이용하여, ```username``` 컬럼을 업데이트 한다.

### Test
```java
@Test
void 이름을_업데이트한다(){
    User user = userService.findAll().get(0);
    userService.updateName(user.getId(),"최재순");
    String updateUsername = userService.findAll().get(0).getUsername();
    Assertions.assertThat(updateUsername).isEqualTo("최재순");
}
```
이 테스트를 돌리고, ```update```가 발생하는 시점의 쿼리를 확인해보자.

```
Hibernate: 
    update
        user 
    set
        age=?,
        username=? 
    where
        id=?
```
```username``` 뿐 아니라 ```age```도 함께 쿼리에 나간 것을 확인할 수 있다. 이는 하이버네이트에서 기본으로 제공하는 스펙이라고 보면 된다.
이렇게 쿼리가 나갈때의 이점과, 단점을 하이버네이트에서 설명하고 있다.

>The default UPDATE statement containing all columns has two advantages:  
>* it allows you to better benefit from JDBC Statement caching.
>* it allows you to enable batch updates even if multiple entities modify different properties.  
> 
> However, there is also one downside to including all columns in the SQL UPDATE statement. If you have multiple indexes, the database might update those redundantly even if you don’t actually modify all column values.

장점으로는, ```JDBC``` 쿼리 캐싱하는데 좋고, 여러 엔티티가 서로 다른 컬럼을 수정하는 경우에도 일괄 업데이트하기 좋다고 한다.
하지만 큰 단점이 있다.  
여러 인덱스를 업데이트 하는 경우 실제로 모든 열을 수정하지 않더라도, 데이터베이스에서 인덱스를 중복 업데이트 할 가능성이 있다고 한다.

그렇다면 이 문제를 어떻게 해결할까?
> To fix this issue, you can use dynamic updates.

```@DynamicUpdate```애노테이션을 entity에 달아주면 된다.

> DynamicUpdate 애노테이션을 달아준 후

```
Hibernate: 
    update
        user 
    set
        username=? 
    where
        id=?
```
이제는 수정한 열만 쿼리로 나가는 것을 확인할 수 있다!!

```java
@Test
void 나이를_업데이트한다(){
    User user = userService.findAll().get(0);
    userService.updateAge(user.getId(), 26);
    int age = userService.findAll().get(0).getAge();
    Assertions.assertThat(age).isEqualTo(26);
}
```
이렇게 나이를 업데이트 하는 경우에도, 쿼리는 다음과 같이 나간다.

```
Hibernate: 
    update
        user 
    set
        age=? 
    where
        id=?
```

여러 인덱스를 수정할 때, 혹은 성능 개선에 도움이 필요할 때는 ```@DynamicUpdate```를 붙여
```update```를 최적화하자.
