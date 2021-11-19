# Querydsl로 페이징 구현하기(4)
offset을 이용하여 페이징을 구현할 때, 1만번째 데이터부터 조회를 원한다면 1만개의 데이터를 스쳐지나가기때문에 인덱스를 태우지 않으면 그 성능이 굉장히 느려진다. 그렇다면 방법이 있다, 인덱스를 태우고 인덱스로 태운 값을 ```where``` 조건문으로 받아 페이징을 구현하는 것이다. 바로 코드로 구현해보자.

### index로 조회하기
간단한 조회조건과, ```pageNo(offset)```, ```pageSize(limit)```를 매개변수로 index를 조회하는 쿼리를 작성한다.
```java
// pk로 select를 돌린다.
queryFactory.select(post.id)
		.from(post)
		.where(memberNameEq(condition.getName()),  
		  regionEq(condition.getRegion()),  
		  titleContain(condition.getTitle()))
		 .orderBy(post.id.desc())  
		.offset((pageNo-1)*pageSize)  
		.limit(pageSize)  
		.fetch();
```
위와같이 코드를 작성하면 쿼리가 인덱스를 타게되어 성능이 말도 안되게 개선된다. 하지만 약간의 불편한점이 있다. 당연히 비즈니스 요구사항 상 id값만 받는 것이 아니라 , 여러가지 전달값이 필요하다. 이 전달값을 조회하는 쿼리를 다시 한 번 작성한다.

### Where in
```offset```의 단점은 ```offset``` 의 작성으로 인해 발생하는 불필요한 데이터를 확인하는 과정이었다. 하지만 앞서 ```id```값을 조회한 결과를 ```where in``` 으로 조회하여 특정 ```index```를 가진 값만 데이터를 조회한다. 조회를 더욱 최적화하기위해 적절한 dto를 만들어 쿼리를 작성해보자.
```java
return queryFactory.select(new QStoryListWithoutPathLikeResponseDto(post.member.id.as("memberId"),  
	  post.member.uname,  
	  post.id.as("postId"),  
	  post.title,  
	  post.period.startDate,  
	  post.period.endDate))  
                .from(post)  
                .where(post.id.in(postIds))  
                .orderBy(post.id.desc())  
                .fetch();
```
> in 쿼리는 순서가 보장되지 않기 때문에 orderby를 꼭 작성해줘야한다.
> 추가적으로 1대다관계를 가지는 조회쿼리는 함께 fetch join하면 절대 페이징처리를 할 수 없다. 따로 쿼리를 작성하고 서비스 로직에서 하나의 dto로 합치는 방법을 선택한다.

### test
Test를 돌려보면 다음과 같이 쿼리가 발생한다.
1 + N 이슈도 없고, 원하는대로 쿼리가 잘 나간 것을 확인할 수 있다.
```
Hibernate: 
    select
        post0_.post_id as col_0_0_ 
    from
        post post0_ 
    where
        post0_.region=? 
    order by
        post0_.post_id desc limit ?
Hibernate: 
    select
        post0_.member_id as col_0_0_,
        member1_.uname as col_1_0_,
        post0_.post_id as col_2_0_,
        post0_.title as col_3_0_,
        post0_.start_date as col_4_0_,
        post0_.end_date as col_5_0_ 
    from
        post post0_ cross 
    join
        members member1_ 
    where
        post0_.member_id=member1_.member_id 
        and (
            post0_.post_id in (
                ? , ? , ? , ? , ? , ? , ? , ? , ? , ?
            )
        ) 
    order by
        post0_.post_id desc

```
