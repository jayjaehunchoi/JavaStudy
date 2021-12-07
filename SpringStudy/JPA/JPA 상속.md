# JPA 상속관계 매핑

> Hibernate docs 2.11 Inheritance를 읽고 정리한다.

```RDS```는 상속을 지원하지 않는다. 하지만 엔티티를 객체지향으로 관리하는 ```ORM``` 하이버네이트는 상속관계를 테이블로 전환하는 몇가지 전략이 있다.

* MappedSuperclass
* Single table
* Joined table
* Table per class


## MappedSuperclass

```@MappedSuperclass```를 사용하면, 상속은 ```domain model```에서만 확인할 수 있고, 각 데이터베이스 테이블에서 ```base class```를 포함하고 있다.

> 예시 : 아래 관계를 코드로 바꿔본다. 

![image](https://user-images.githubusercontent.com/87312401/144954860-76a89d06-5107-46ac-8cad-ad831320bdf0.png)


#### Account
```MappedSuperclass```로 선언하여 ```Account```를 상속받는 ```Entity```테이블에 속성값을 넣어준다. 
주로 ```EntityListner```를 사용하여 ```속성```을 넣을 때 사용하고, 솔직히 아래 예시는 상속을 사용하는게 더 맞는 것 같다.
```java
@MappedSuperclass
public abstract class Account {

	@Id
	private Long id;

	private String owner;

	private BigDecimal balance;

	private BigDecimal interestRate;

	//Getters and setters are omitted for brevity
}
```

#### SubClass
```Account```를 상속받아 테이블에 속성값을 주입해준다.
```java

@Entity(name = "DebitAccount")
public class DebitAccount extends Account {

	private BigDecimal overdraftFee;

	//Getters and setters are omitted for brevity
}

@Entity(name = "CreditAccount")
public class CreditAccount extends Account {

	private BigDecimal creditLimit;

	//Getters and setters are omitted for brevity
}
```

이렇게 코드를 작성하고 프로젝트를 실행해보면 다음과 같이 쿼리가 생성된다.

```sql
CREATE TABLE DebitAccount (
    id BIGINT NOT NULL ,
    balance NUMERIC(19, 2) ,
    interestRate NUMERIC(19, 2) ,
    owner VARCHAR(255) ,
    overdraftFee NUMERIC(19, 2) ,
    PRIMARY KEY ( id )
)

CREATE TABLE CreditAccount (
    id BIGINT NOT NULL ,
    balance NUMERIC(19, 2) ,
    interestRate NUMERIC(19, 2) ,
    owner VARCHAR(255) ,
    creditLimit NUMERIC(19, 2) ,
    PRIMARY KEY ( id )
)
```

```Account```가 없어지고, ```Account```에서 선언된 변수가 각 테이블의 ```Column```으로 들어간 것을 확인할 수 있다.

> Because the @MappedSuperclass inheritance model is not mirrored at the database level, it’s not possible to use polymorphic queries (fetching subclasses by their base class).
> > 하이버네이트 스펙에 따르면, ```@MappedSuperclass```는 db 레벨로 미러링되지 않는다고 한다. 따라서 다형성 사용이 불가하다. 즉 객체를 직접 구현하여 사용할 일이 없다.

따라서, 아래와 같이 추상 클래스로 선언해주고 조회, 검색, 직접 구현 등 JPA가 실행될 때 발생할 수 있는 오류를 막아준다. (테이블이 없기 때문에 무조건 오류 난다.)
```java
public abstract class Account {}
```

## Single table
```single table``` 상속은 모든 ```subclass```를 한 테이블에 저장한다. 각 ```subclass```는 속성값을 ```root```테이블에 선언한다.

> 상속을 사용하면 default 설정이 Single table 전략이다.

위 코드에서, 부모 클래스만 살짝 바꿔보자.

#### Account
```default```전략으로 ```Single table```이 잡히지만, 명시적으로 ```@Inheritance``` 애노테이션을 달아 전략을 명시해준다.
```java
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Account {

    @Id
    private Long id;

    private String owner;

    private BigDecimal balance;

    private BigDecimal interestRate;

    //Getters and setters are omitted for brevity
}
```

위에서 설명했듯이, 하나의 테이블에 모든 하위클래스의 속성이 선언된다.
```sql
CREATE TABLE Account (
    DTYPE VARCHAR(31) NOT NULL ,
    id BIGINT NOT NULL ,
    balance NUMERIC(19, 2) ,
    interestRate NUMERIC(19, 2) , 
    owner VARCHAR(255) ,
    overdraftFee NUMERIC(19, 2) ,
    creditLimit NUMERIC(19, 2) ,
    PRIMARY KEY ( id )
)
```
> Account 테이블의 subclass인 ```DebitAccount```, ```CreditAccount``` 의 각 속성값 ```overdraftFee```와 ```creditLimit```가 한, 테이블에 선언됐다.

> 모든 상속 전략 중 , 테이블 하나만 생성되는 ```single table```전략의 성능이 가장 좋다. ```not null``` 제약조건을 사용할 수 없기에, 무결성 검사시 ```Trigger```나 ```Check``` 제약 조건을 통해 실행되어야 한다. 

그런데 아래와 같이 선언하지도 않은 컬럼이 생겼다! 
```sql
DTYPE VARCHAR(31) NOT NULL 
```
과연 ```DTYPE```은 무엇이고 왜 생긴 것일까? 다음 상속 유형을 알아보기 전 ```DTYPE```에 대해 간단히 알아보자.

## Discriminator
```Discriminator column```은 말그대로 판별자 열이다. 이 행이 ```CreditAccount```  ```DebitAccount``` 중 어떤 ```Entity```로 생성되었는지 확인할 수 있다.
```type``` ```name``` 등 자세한 전략은 ```@DiscriminatorColumn```을 통해 작성할 수 있고, ```single table```전략 사용시 기본 컬럼인 ```DTYPE```이 생성된다

```DTYPE```에 들어가는 값은 subclass에 ```@DiscriminationValue("{name}")```를 통해 선언할 수 있다. 만약 선언하지 않을 시 기본으로 ```Entity```명이 들어간다.

추가적인 내용은 [링크](https://docs.jboss.org/hibernate/orm/5.3/userguide/html_single/Hibernate_User_Guide.html#entity-inheritance-discriminator-formula) 를 참고하자.

## Joined Table
각 subclass들은 그들의 테이블로 매핑된다(각 테이블이 생성됨). subclass는 조회시 부모 클래스와 함께 join되어 조회된다.
적용 방법은 간단하다. superclass에 ```@Inheritance(strategy = InheritanceType.JOINED)``` 를 선언해주면 된다.

쿼리는 다음과 같이 발생한다.

```sql
CREATE TABLE Account (
    id BIGINT NOT NULL ,
    balance NUMERIC(19, 2) ,
    interestRate NUMERIC(19, 2) ,
    owner VARCHAR(255) ,
    PRIMARY KEY ( id )
)

CREATE TABLE CreditAccount (
    creditLimit NUMERIC(19, 2) ,
    id BIGINT NOT NULL ,
    PRIMARY KEY ( id )
)

ALTER TABLE CreditAccount
ADD CONSTRAINT FKihw8h3j1k0w31cnyu7jcl7n7n
FOREIGN KEY (id) REFERENCES Account

```
각 테이블마다 ```join```할 수 있는 ```id```(pk 이자 fk) 값이 생긴다. 하지만 문제점 두 가지가 보인다.

1. ```DTYPE```이 없다.
2. constraint name이 선언되지 않았다.

```1```번 이슈는 간단하게 ```@DiscriminatorColumn```을 선언하여 해결할 수 있고,
```2```번 이슈는 ```@PrimaryKeyJoinColumn(name = "ACCOUNT_ID", foreignKey = @ForeignKey(name = "FK_ACCOUNT_CREDIT"))``` 을 subclass에 선언하여 해결할 수 있다.

> joined table의 PK는 FK이기도 하다. 이는 @PrimaryKeyJoinColumn 을 이용하여 조작이 가능하다.
> @PrimaryKeyJoinColumn 설정이 없으면 자식 테이블 pk / fk는 부모 테이블의 pk 이름을 따라가고 fk constraint name은 랜덤으로 선언된다.

> Join table 전략에서 다형성을 이용하면 여러 Join이 발생하여 성능이슈가 생길 수 있다.
> 다형성을 안쓰는게 맞지않을까... 생각이 든다. 필요한 subclass만 조회해서 1회 join으로 끝내는 게 좋은 것 같다.

## Table per class
구현된 클래스가 각각 테이블로 생성된다. 즉 테이블만 보고는 상속관계를 알 수가 없다.
아래와 같이 sql 쿼리가 발생한다.

```sql
CREATE TABLE Account (
    id BIGINT NOT NULL ,
    balance NUMERIC(19, 2) ,
    interestRate NUMERIC(19, 2) ,
    owner VARCHAR(255) ,
    PRIMARY KEY ( id )
)

CREATE TABLE CreditAccount (
    id BIGINT NOT NULL ,
    balance NUMERIC(19, 2) ,
    interestRate NUMERIC(19, 2) ,
    owner VARCHAR(255) ,
    creditLimit NUMERIC(19, 2) ,
    PRIMARY KEY ( id )
)

CREATE TABLE DebitAccount (
    id BIGINT NOT NULL ,
    balance NUMERIC(19, 2) ,
    interestRate NUMERIC(19, 2) ,
    owner VARCHAR(255) ,
    overdraftFee NUMERIC(19, 2) ,
    PRIMARY KEY ( id )
)
```
```FK``` 가 선언되지 않는다. 만약 ```Account```를 조회하려 하면, ```Union All```을 사용하여 모든 테이블을 다 뒤져서
조회를 시도한다. 즉 성능상 이슈가 엄청나다. 국내 ``JPA``의 아버지 김영한님의 말에 따르면 그냥 쓰지 말라신다. 따라서 이 전략은 사용하지 말자.

## 마무리
상속은 ```rds```에서 제공되지 않지만 ```hibernate```를 통해 다양한 전략으로 구성할 수 있다.
개인적으로 ```join```전략이 가장 효과적일 것이라 생각이 든다. 테이블을 봤을 때도 명쾌하고, 객체를 사용할 때도 성능이 크게 떨어지지 않을 것 같다는 생각이 든다.




