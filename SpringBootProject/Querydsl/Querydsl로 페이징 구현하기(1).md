#  Querydsl로 페이징 구현하기 (1)
이번 프로젝트에서 JPA를 사용하기로 하였다. ```JPA```는 기존에 사용한 경험이 있어 간단한 ```CRUD``` 정도는 구현할 수 있지만 ```JPA```를 이용하여 페이징을 구현해본 경험이 없어, 페이징 구현에 대해 학습하고자 한다.  
  
```JPQL``` 을 이용한 페이징 구현도 가능한데, 왜 ```Querydsl``` 로 페이징(게시판)을 구현할까? 지금 우리 프로젝트 상황에 빗대어보면 다음과 같은 이유가 있다. 
  
바로 **복잡한 동적 쿼리**의 사용이다. 프로젝트 기획안 중 **조회수 순**, **인기 순**, **최신 순** 등 다양한 필터를 넣어 조회하는 로직을 작성해야 하는데, 이 필터와 페이징 쿼리를 같이 편하게 만드는 방법이 ```Querydsl```이라고 생각하였다.

## Querydsl 시작하기
```Queydsl```은 세팅이 중요하다. ```build.gradle``` 로 세팅을 시작해보자.
starter를 이용하여 일반적으로 세팅되는 build는 제외하고, ```Querydsl```을 사용하기 위한 코드만 작성해본다.
```groovy
plugins{
	id "com.ewerk.gradle.plugins.querydsl" version "1.1.3" // docs 확인 결과 1.1.3이 가장 최신 버전으로 확인 됨.
}
dependencies{
	implementation 'org.querydsl:querydsl-jpa'
}

def querydslDir = "$buildDir/gernerated/querydsl" // 해당 경로에 querydsl 폴더 생성
querydsl {
	jpa = true;
	querydslSourcesDir = querydslDir
}
sourceSets{
	main.java.srcDir querydslDir
}
configurations {
	querydsl.extendsFrom compileClasspath
}
compileQuerydsl {
	options.annotationProcessorPath = configurations.querydsl
}
```
> querydsl을 만들면 Qclass를 담은 폴더가 생긴다.  위와 같이 생성하면 ```buildDir/gernerated/querydsl``` 경로에 생성되는데, 자동 생성되는 파일이니 .gitignore에 추가하자. (일반적으로 build는 .gitignore로 추가한다.)

## JPA에 Querydsl
먼저 ```JPA``` repository를 만들고,  기본 ```EntityManager```와 ```JPQL```을 사용하여 간단한 생성, 조회 메서드를 만들어보자.

### 순수 jpa
```java
@Repository
public class BoardJpaRepository{
	private final EntityManager em;
	private final JPAQueryFactory queryFactory;
	
	// queryFactory는 parameter로 em 이 필요 , 생성자 주입 명시 
	public MemberJpaRepository(EntityManager em){
		this.em = em;
		this.queryFactory = new JPAQueryFactory(em);
	}

	public void save(Board board){
		em.persist(board);
	}
	// null 로 return 가능
	public Optional<Board> findById(Long id){
		Board findBoard = em.find(Board.class, id);
		return Optional.ofNullable(findBoard);
	}
	// jpql을 이용한 전체 조회
	public List<Board> findAll(){
		return em.createQuery("select b from Board b", Board.class)
			.getResultList();
	}
	
	// 타이틀을 이용한 조회
	public List<Board> findByBoardTitle(String boardTitle){
		return em.createQuery("select b from Board b where b.boardTitle = :boardTitle", Board.class)
			.setParameter("boardTitle", boardTitle)
			.getResultList();
	}
}
```
위에서 작성한 메서드를 ```Querydsl```로 전환해준다.
### Querydsl로 전환
```java
public List<Board> findAll(){
	return queryFactory
			.selectFrom(QBoard.board) // static import해줘도 됨.
			.fetch();
}

public List<Board> findByBoardTitle(String boardTitle){
	return queryFactory
			.selectFrom(board)
			.where(board.boardTitle.eq(boardTitle))
			.fetch();
}
```
```querydsl``` 을 이용하면 ```IDE```에서 code assistant를 제공하기 때문에 굉장히 편하게 쿼리를 작성할 수 있다. ```JPQL```로 코드를 작성할 때는 오타가 발생해도 , 실행은 되지만 runtime시점에 오류가 난다.

