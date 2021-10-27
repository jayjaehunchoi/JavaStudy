#  Querydsl로 페이징 구현하기 (2)
지난 시간에는 Querydsl 세팅과 순수 ```Jpa```를 이용하여 기본적인 ```Querydsl``` 코드를 작성해보았다. 이번 시간에는 ```SpringDataJpa``` 로 전환하고, 동시에 분리한 ```Querydsl``` 을 어떻게 ```JpaRepository```에서 활용하는지 작성해 보고자 한다.

### BoardRepository
우선 코드를 작성하기 앞서, 이제부터 ```class``` 명에 주목해야 한다.
```java
public interface BoardRepository extends JpaRepository<Board,Long>, BoardRepositoryCustom{
}
```
먼저 ```JpaRepository```를 상속받는 클래스를 만들어준다.

### BoardRepositoryCustom
```Querydsl``` 쿼리를 작성할 구현 클래스를 만들기 전에 부모 클래스를 생성해준다.
이때, ```Custom``` 이라는 단어를 꼭 기억하자.
```java
public interface BoardRepositoryCustom{
	Page<MemberBoardDto> search(BoardSearchCondition condition, Pageable pageable);
}
```
```springframework```에서 지원하는 ```Page``` 클래스를 리턴값으로, 매개변수에는 검색 조건을 구현해주는 ```dto``` 클래스와 ```pageable```을 받는다.

### BoardRepositoryImpl
이제 실제 ```Querydsl``` 코드를 작성할 구현 클래스를 만든다. 여기서도 ```Impl``` 이라는 단어를 꼭 기억하자.
```java
public class BoardRepositoryImpl implements BoardRepositoryCustom{
	private final JPAQueryFactory queryFactory;
	public BoardRepositoryImpl(EntityManager em){
		this.queryFactory = new JPAQueryFactory(em);
	}
	public Page<MemberBoardDto> search(BoardSearchCondition condition, Pageable pageable){
		QueryResults<MemberBoardDto> results = queryFactory.select(new MemberBoardDto(
									member.id.as("memberId"),
									member.name,
									board.id.as("boardId"),
									board.title,
									board.content))
							.from(board)
							.leftJoin(board.member, member)
							.where(memberNameEq(condition.getName()),
									regionEq(condition.getRegion()),
									likesGoe(condition.getLikes()))
							.orderBy(board.id.desc())
							.offset(pageable.getOffset())
							.limit(pageable.getPageSize())
							.fetchResults(); // count 쿼리가 함께 나감
		List<MemberBoardDto> contents = results.getResults();
		long count = results.getTotal();
		return new PageImpl<>(content, pageable, count);
	} 
}
```

위 코드상에는 등장하지 않았지만 , ```where``` 조건문을 달아 편리하게 조회 & 페이징 기능을 구현하였다. ``Test``` 코드 작성하기 앞서 이름의 중요성에 대해 이야기하겠다.
> JPA에는 Impl 네이밍 규칙이 있다. 이 규칙은 RepositoryInterface + Impl 으로 만약 이 규칙이 지켜지지 않는다면 사용자가 정의한 구현 클래스를 인식하지 못하고 **BeanCreationError**를 발생시킨다. 따라서 이름 규칙을 철저하게 지키며 코드를 작성해야 한다.

### Test
```java
@BeforeEach  
void setUp(){
	...// 데이터 셋업
}
  
@DisplayName("페이징")  
@Test  
void searchPageSimple(){  
      BoardSearchCond boardSearchCond = new BoardSearchCond();  
	  boardSearchCond.setName("wogns0108");  // wogns0108 아이디를 모두 가져와라
	  PageRequest pageRequest = PageRequest.of(0,5);  // offset은 0부터 시작, 1 page size = 5
	  Page<MemberBoardDto> memberBoardDtos = boardRepository.searchPageSimple(boardSearchCond, pageRequest);  
	  assertThat(memberBoardDtos.getSize()).isEqualTo(5);  // 1페이지에 다섯개의 게시물이 출력된다.(@BeforeEach에 데이터 넣어두었음.)
}

```
자 이렇게 간단하게 ```Querydsl```을 사용하여 페이징을 구현하였다. 
하지만 ```offset```기반의 페이징 기법에는 큰 이슈가 있다. 바로 성능이다.
이번 프로젝트에서는 무한 스크롤을 사용하지 않을 것으로 예상되지만, 다음 시간에는 커서기반의 페이징과 우리 프로젝트에서 ```offset```을 사용하고도 성능을 개선할 수 있는 페이징(커버링 인덱스)에 대해 실험해보고자 한다.
