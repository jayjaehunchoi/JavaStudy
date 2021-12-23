# Fetch Join

## Fetching
```Fetching```은 db의 데이터를 application에서 이용할 수 있게끔 가져오는 과정이다. 애플리케이션에서 ```Fetching```을 어떻게 하느냐에 따라 성능에 큰 요인이 될 수 있다.
너무 많은 row, column이 fetching되면 overhead가 발생할 수 있고, 너무 적은 fetching이 이뤄지면 추가적인 fetching이 필요할 수 있다.
즉 얼마나 fetching을 잘 튜닝하는가에 따라 성능 개선에 크게 기여할 수 있다.

```Fetching```은 두가지 컨셉을 갖고 진행되어야 한다.
1. 언제 데이터가 fetch 되어야 하는가? Lazy or Eager
2. 어떻게 fetch 할 것인가?


## SELECT Vs JOIN
fetch를 하는 데 여러가지 관점이 있지만 그 중에서도 ```SELECT``` 와 ```JOIN``` 개념에 대해 건드려 보고 Fetch Join을 해보고자 한다.

### SELECT
말그대로 SQL Select 해오는 것, Eager or Lazy 설정에 따라 proxy로 가져올지 말지를 결정한다. 
proxy로 영속성 컨텍스트에 저장된 객체가 존재할 때 N + 1 이슈를 야기한다.

### JOIN
SQL의 Outer Join을 사용하여 Eager 형태로 모든 엔티티 그래프 내 값을 대입한다.

> join fetch를 사용하지 않으면 N+1 이슈를 야기할 수 있고, 그것이 바로 LAZY를 사용해야 하는 이유이다.

간단하게 fetch에 대해 알게 됐으니 Join Fetch를 사용해보자

## Fetch Join
```Member```와 ```Team``` 객체를 만들고, Member는 Team을 ```@ManyToOne``` 연관관계로 가진다고 가정해보자.
기본 FetchType으로 ```LAZY```를 선언하고 일반 조회 쿼리 vs  ```join fetch```를 비교해보자.

### 일반 조회 쿼리
```java
String query = "select m from Member m";
List<Member> members = em.createQuery(query, Member.class).getResultList();

for (Member member1 : members) {
    System.out.println("member1 = " + member1.getName() + " , " + member1.getTeam().getName());
}

tx.commit();
```

![image](https://user-images.githubusercontent.com/87312401/147173131-6e9fa7a6-86e8-48b1-9b1a-e469015bc89c.png)

위 사진에서 볼 수 있듯, Lazy에서  ```Join```을 사용하지 않으면 ```Team```객체는 Proxy로 존재하기 때문에,
실제로 조회되는 시점인 ```for loop```에서 조회가 이뤄진다. 최초에 ```팀A```를 가져올 때 1회 SQL 쿼리가 나가고 그 다음은 영속성 컨텍스트에 캐시된 값을 가져온다.

이후 ```팀B```를 조회할 때 다시 쿼리가 나간다. ```N+1```이슈가 발생하는 것이다.

### 일반 join
그렇다면 쿼리를 일반 join으로 바꾸면 어떨까?
```java
String queryA = "select m, t from Member m left outer join m.team t";
List<Member> resultList = em.createQuery(queryA, Member.class).getResultList();

for (Member member1 : resultList) {
    System.out.println("member1.getTeam().getName() = " + member1.getTeam().getName());
    System.out.println("member1.getName() = " + member1.getName());
}
```

![image](https://user-images.githubusercontent.com/87312401/147198557-f71d5e98-206f-47f9-8392-5e85cf724e3f.png)
에러가 발생한다. 에러 메시지의 의미를 보니, 조회한 값을 ```Member``` 엔티티에 담을 수 없다는 의미이다.
추측이지만 일반 join을 사용하면 ```Entity Graph```를 타지 못하는 것 같다. 
따라서 일반 ```join```을 사용하기 위해서는 조회할 ```Column```만 객체에 담은 뒤, 해당 내용들을 join으로 최적화하여 가져오면 된다.

### Fetch Join

쿼리를 아래와 같이 바꿔보자.
```java
String query = "select m from Member m join fetch m.team";
```

![image](https://user-images.githubusercontent.com/87312401/147173335-0c128197-786b-4087-bff1-62f9e1f63ff0.png)

한 방 쿼리로 Team 객체까지 가져오고 ```Entity Graph```를 타서 Member, Team 객체를 채워준다.
한 번의 조회를 마치면 다시 Team을 query를 날려 가져올 일이 없다.

## 주의사항
- ```1:N``` 연관관계에서 조회시 중복이 발생할 수 있다. 이 문제는 ```distinct```예약어를 붙임으로써 애플리케이션에서 동일 엔티티를 제거하여 해결한다.
- ```1:N``` 에서 페이징이 불가하다. Reference에서는 추천하지 않는다고 표시하지만 거의 불가능하다고 보면 될 것 같다. 위의 주의사항 때문에 그렇다. 이 문제는 ```@BatchSize```를 선언하거나 전역으로 ```default_batch_fetch_size```를 선언해주면 in query로 해결할 수 있다.
- sub query로 유효하지 않다. join fetch한 객체를 alias로 받아서 추가 작업을 할 생각은 하지말자..

간단하게 Reference를 보며 Fetch와 Fetch Join에 대해 학습할 수 있었다. fetch join은 정말 유용하다. 특히 JPA의 N+1 문제를 해결할 주요 key이다. ```@EntityGraph```도 fetch Join을 사용하더라.
JPA를 사용한다면 잘 이해하고 넘어가야 하는 내용이다.


