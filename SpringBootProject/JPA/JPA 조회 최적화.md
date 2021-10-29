# JPA 조회 최적화
개인적으로 JPA를 이용하여 api를 만들고 있다. 열심히 코드를 작성하고 포스트맨으로 테스트를 하던 중,, 포스트맨 Body result에 무한대로 Json이 작성되는 모습을 봐버렸다. 그 이유는 무엇이었을까?

### Entity를 Json으로 return하지 말 것
나름대로 ```ResponseDto```를 만들어 api 스펙을 정확하게 return해줬다고 생각했으나, 바보같이 ```dto```내부에 Entity를 직접 사용했었다.  양방향 연관관계로 매핑되는 Entity였고, 때문에 무한대로 반복하면서 장애가 발생한 것이다. 간단하게 이런 상황을 예시 코드로 작성해보자.
> Order table, Delivery table, Member table은 xTox로 양방향 연관관계 설정되어있다고 가정한다.
```java
@GetMapping("/api/v1/orders")  
public List<Order> getOrders(){  
    List<Order> all = orderRepository.findAllByString(new OrderSearch());  
    return all;  
}
```
위와 같은 코드를 작성하면, Json Message Converter가 Entity 객체에 있는 양방향 매핑을 계속 참조하다가 결국 장애를 발생시킨다. 해결방법이 아예 없는 것은 아니다.  양방향 연관관계 중 하나에 ```@JsonIgnore```를 달아주는 방법이 있다. 하지만 추천하지는 않는다. 

### response해주는 dto를 만들것
그렇다면 Entity를 반환하지 않고 어떻게 해야할까? 바로 api 스펙에 완전 일치하는 **응답 dto**를 만들어야한다. 
```java
@Data  
public class SimpleOrderDto{  
    private Long orderId;  
	private String name;  
	private LocalDateTime orderDate;  
	private OrderStatus orderStatus;  
	private Address address;  
  
 public SimpleOrderDto(Order order){  
      orderId = order.getId();  
	  name = order.getMember().getName(); 
	  orderDate = order.getOrderDate();  
	  orderStatus =order.getStatus();  
	  address = order.getDelivery().getAddress();
  }  
}
```
> Address 는  Embedded Entity이기 때문에 entity 그대로 api 스펙에 노출해도 크게 상관 없다.
```java
@GetMapping("/api/v2/orders")  
public List<SimpleOrderDto> getOrders(){  
    return orderRepository.findAllByString(new OrderSearch()).stream()
					  .map(SimpleOrderDto::new) 
					  .collect(toList());  
}
```
이제 Dto도 만들었으니, api 스펙대로 return을 해준다. 하지만, 과연 이 코드는 성능 측면에서 좋다고 할 수 있을까? 절대 아니다. 여기서 바로 ```JPA N+1``` 문제가 발생한다.

### N+1
코드를 한 줄 씩 분해해가며 왜 N+1 문제가 발생하는지 살펴보자.
> 모든 연관관계는 FetchType.LAZY로 선언되어있다.
```java
List<Order> orders = orderRepository.findAllByString(new OrderSearch())
```
위 코드에서, ```OrderSearch``` (검색 조건)에 맞게 ```Order``` entity의 모든 내용을 ```select``` 한다. 이때 지연로딩이 걸려있기때문에, 연관관계 매핑되어있는 Entity는 조회되지 않는다. **1**

```java
orders.stream.map(SimpleOrderDto::new).collect(Collectors.toList);
```
위 코드는, 조회한 내용을 반복문을 돌며, 하나씩 ```SimpleOrderDto``` 로 전환해준다. 이 전환 과정에서 문제가 발생한다. 문제의 코드는 다음과 같다.
```java
public SimpleOrderDto(Order order){  
	  //(생략)
	  name = order.getMember().getName(); 
      //(생략)
	  address = order.getDelivery().getAddress();
  } 
```
바로 이 두 가지 변수를 생성하는 과정에서 발생한다. 현재 ```Member```와 ```Delivery```의 값은 영속성 컨텍스트에 존재하지 않는다. 따라서 JPA는 반복문을 1회 돌때마다 새 쿼리를 두 개씩 발생시킨다. 
> 반복 1회 후 영속성 컨텍스트에서 Member, Delivery를 가져오지 못하는 이유는 바로 , id값을 기반으로 딱 하나의 row만 가져왔기 때문이다.

이렇게될 시, 만약 Order가 10000개라면 ? 최악의 경우 1 + 10000 + 10000 개의 쿼리가 발생할 것이다. 이것은 성능을 최악으로 가져가는 문제이다. ```FetchType```을 ```Eager```로 발생시키면 되지 않는가? 라는 질문이 생길 수 있다. 직접 ```Eager``` 로 코드를 돌려보면 왜 하지 않는지 알 수 있다.
그렇다면 이 N+1 문제를 해결해보자.

### fetch Join
fetch join은 join문을 이용해 단건 쿼리를 날려 영속성 컨텍스트에 모든 값을 저장해놓고 필요한 순간에만 사용할 수 있는 JPA만의 아주 좋은 기능이다.
```java
//repository
public List<Order> findAllWithMemberDelivery(){
	return em.createQuery("select o from Order o" +
	" join fetch o.member m"+
	" join fetch o.delivery d").getResultList();
}
```
이처럼 join fetch를 이용하여 조회하게되면 간단하게 N+1문제를 해결할 수 있다. 하지만 이는 **단건 조회** 만의 방법이다. ```OneToMany``` 상황에서는 fetch join의 문제점을 어떻게 해결할 수 있을지는 다음 글에 이어서 작성하고자 한다.
