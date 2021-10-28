# Querydsl로 페이징 구현하기(3)
지난 게시물에는 ```offset``` 기반 페이징 기법을 사용하여 ```querydsl```로 페이징 코드를 작성해보았다. 하지만 ```offset```기반 페이징 기법은 성능에 큰 이슈가 있다. 왜 그런걸까?
> offset으로 시작 지점을 지정하면, 시작 지점 전까지의 row들을 모두 지나친다. 즉 offset 100,000, limit 10이라고  가정했을 때, 10개의 게시물을 출력하기 위해 10만개의 데이터들을 모두 지나가는 것이다. 데이터가 적을 때에는 상관없겠지만,  데이터가 많아지면 성능상 정말 큰 이슈가 발생하는 것이다. 특히 원페이지 애플리케이션 같은 경우에 offset으로 페이징을 구현하게 되면 , 사용자는 정말 큰 불편함을 느낄 것이다.

위와 같은 이유로 , 대규모 데이터를 다루는 원페이지 애플리케이션은 주로 **커서 페이징**을 사용한다.
> 하지만 비즈니스 요구사항에 꼭 1,2,3,4 형태의 페이지를 사용해야 한다고 하면 커서페이징은 사용이 불가하다.

## 구현
우리 프로젝트에서는 1,2,3,4 형태의 페이징 기법을 사용하기로 하였으나, 아직 무한 스크롤에 대한 사용 고민이 있기 때문에, Querydsl 을 이용한 커서 페이징을 직접 코드로 작성해보고자 한다. 
먼저 동적으로 쿼리를 작성하기위해 ```BooleanExpression``` 을 return 하는 메서드를 하나 만들어준다.
### 현재 boardId
```java
private BooleanExpression ltBoardId(Long boardId){  
    return boardId != null? board.id.lt(boardId) : null; // board.id < 입력 board Id
}
```
위 메서드를 이용하여, 현재 ```boardId```를 확인하고, ```boardId``` 보다 작은 ```Id```를 조건에 걸어줄 수 있게 만들어준다. 만약 처음 조회한다면, ```null``` 값을 넣어 맨 위부터 조회될 수 있도록 동적 쿼리를 작성한다. **lt를 사용한 이유**는 **게시판은 최신순이기 때문이다** (``desc()`` 사용)
> 이때 , PrimaryKey인 Id를 사용한 것을 볼 수 있다. 클러스터 Index를 이용해 조회함으로써 성능을 최적화 할 수 있다.

### PaginationByCursor
```java
@Override  
public List<MemberBoardDto> searchByCursor(Long boardId, int pageSize, BoardSearchCond cond) {
	//사실 dto가 Querydsl에 종속되는 것은 별로 좋지 않으나, 
	//단순 예제이기 때문에 구현함. 대체로, Projection.fields 등을 사용할 수 있다.
	return queryFactory.select(new QMemberBoardDto(member.id.as("memberId"), 
											member.name,
											board.id.as("boardId"),
											board.title,
											board.content))
					  .from(board)
					  .leftJoin(board.member,member)
					  .where(memberNameEq(cond.getName()),  
							  regionEq(cond.getRegion()),  
							  likesGoe(cond.getLikes()),  
							  ltBoardId(boardId))
					  .orderBy(board.id.desc())
					  .limit(pageSize)
					  .fetch();
}
```
쿼리를 작성하고 Test코드를 돌려봤다.
### Test
```java
@TestInstance(TestInstance.Lifecycle.PER_CLASS) //default Method 단위
@ExtendWith(SpringExtension.class)  
@SpringBootTest  
@Transactional  
public class BoardTest {
...
// BeforeAll 을 void로 사용하기위해 TestInstance Lifecycle을 Class단위로 주었다.
// 하지만 영속성 issue가 있었다.또, 테스트는 각 메서드가 완전히 분리되는 것이 좋다고 생각한다. 
// 따라서 본 프로젝트에서 테스트 코드를 짤 때는 각 메서드에서 값을 세팅해주는 방식을 사용하고자 한다.
@BeforeAll  
void setUp(){...}
}
@DisplayName("커서 페이징 첫번째 조회")  
@Test  
void searchPageCursor_first(){  
    BoardSearchCond boardSearchCond = new BoardSearchCond();  
	List<MemberBoardDto> dtos = boardRepository.searchByCursor(null, 10, boardSearchCond);  
	assertThat(dtos.size()).isEqualTo(10);  
	assertThat(dtos.get(9).getBoardId()).isEqualTo(22L);  
}
@DisplayName("커서 페이징 viewMore 클릭시")  
@Test  
void searchPageCursor_whenClick(){  
    BoardSearchCond boardSearchCond = new BoardSearchCond();  
	List<MemberBoardDto> dtos = boardRepository.searchByCursor(22L, 10, boardSearchCond);  
	assertThat(dtos.size()).isEqualTo(10);  
	assertThat(dtos.get(9).getBoardId()).isEqualTo(12L);  
  
}  
@DisplayName("커서 페이징 남은 글이 10개 이하일때")  
@Test  
void searchPageCursor_whenLeftContentUnder10(){  
    BoardSearchCond boardSearchCond = new BoardSearchCond();  
	List<MemberBoardDto> dtos = boardRepository.searchByCursor(5L, 10, boardSearchCond);  
	assertThat(dtos.size()).isEqualTo(4);  
	assertThat(dtos.get(3).getBoardId()).isEqualTo(1L);  
  
}
```
이로써 Cursor 페이징을 모두 구현했다. 하지만 역시 우리 프로젝트의 요구사항을 고려했을 때는, Offset기반의 페이징을 사용해야한다. Offset 기반의 페이징 성능 개선 방법을 고민해보자.. (생각해보면 구글도 페이징을 사용하니까... 방법은 있을 것이다.)
