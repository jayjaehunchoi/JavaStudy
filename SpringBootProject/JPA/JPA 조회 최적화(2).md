# JPA 조회 최적화(2)
앞선 포스팅에서는 ```XToOne``` 형태인 단건 조회에서의 N+1 문제를 해결하는 방법에 대해 알아봤다. JPA의 ```fetch join``` 을 이용하여 쉽게 단건 조회에서의 N+1 문제를 해결할 수 있었다. 하지만 문제는 ```XToMany``` 형태이다. 지금부터 ```XToMany``` 형태에서의 조회를 최적화해보자.

### fetch join
앞서 사용했던 방식인 ```fetch join```을 그대로 이용해보자.
```java
public List<Order> selectXToMany(){
	return em.createQuery("select o from Order o"
		+" join fetch o.member m" 
		+" join fetch o.delivery d"
		+" join fetch o.orderItems oi"
		+" join fetch oi.item i", Order.class).getResultList();
}
```
위와같이 일반적인 형태로 코드를 작성하였다. 그런데 문제가 있다. 데이터가 중복해서 발생하는 것이다. 왜일까?
간단하게 ```inner join``` 후 테이블을 그려보자.
|orderId|order_item_id|내용|
|--|--|--|
|4|1|아이템1|
|4|2|아이템2|
|5|3|아이템3|
|5|4|아이템4|
위에서 보이다시피, ```join```의 기준인 ```orderId```가 ```fk``` 개수에 맞게 증가한 것을 볼 수 있다. 이제 데이터가 중복해서 발생하는 이유를 알았다. 중복 해결의 key 또한 ```sql```이 갖고 있다.
### distinct
```java
public List<Order> selectXToMany(){
	return em.createQuery("select distinct o from Order o"
		+" join fetch o.member m" 
		+" join fetch o.delivery d"
		+" join fetch o.orderItems oi"
		+" join fetch oi.item i", Order.class).getResultList();
}
```
간단하다. ```distinct```를 추가해주면 된다. 그런데 잘 생각해보면 , ```sql```에서의 ```distinct```는 모든 컬럼의 값이 같을 때 중복 처리를 해준다. 하지만 JPA에서의 ```distinct```는 다르다. ```Pk```인 ```orderId```가 같으면 자동으로 중복처리를 진행해준다. 자 그러면 가져온 값을 기반으로 페이징을 해보자.
간단하게 ```offset = 0``` ,```limit =3```으로 둔다고 가정하자. 원하는 값이 나오지 않는 것을 알 수 있다. JPA에서는 중복을 처리했지만, ```sql```에 쿼리를 날려보면 여전히 중복된 상태로 존재하기 때문이다. 그렇다면 이 문제는 어떻게 해결할 수 있을까?

### 1+1
다시 처음으로 돌아가서 **orderItems를 조회하지 말아보자**.
```java
public List<Order> oneNProblem(int offset, int page){
	return em.createQuery("select o from Order o" +
	" join fetch o.member m" +
	" join fetch o.delivery d", Order.class)
	.setFirstResult(offset)
	.setMaxResults(limit)
	.getResultList();
}
```
이렇게 작성하면 ```orderItems```호출 시 N+1 이슈가 발생할 것이다. 하지만 이를 해결할 정말 간단한 방법이 있다.

### application.properties
```properties
jpa.properties.hibernate.default_batch_fetch_size:500
```
위 코드를 작성한 뒤 쿼리를 돌려보면 놀라운 변화가 있다. 기존에 ```where id = ?``` 방식의 단 건 조회가 ```where in (?,?,? )``` 으로 찍히는 것을 볼 수 있을 것이다. 이렇게 되면 , 중복없이 페이징도 가능하고, 1+N문제도 동시에 해결할 수 있다.
> 물론 distinct를 사용한 복수 row join보다 쿼리가 많이 나가는 것은 사실이나, 성능에 큰 차이도 없고 특수한 상황에는 성능이 더 잘나올 수 도 있다.하지만 페이징을 구현해야 한다면, 필연적으로 사용해야 하는 기능이라고 생각한다.
