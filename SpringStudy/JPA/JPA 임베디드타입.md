# JPA 임베디드 타입

> Hibernate ORM Reference의 ```Embeddable type```을 보고 정리한다.

값을 조합해서, 내장형으로 저장해두는 타입을 ```Embeddable type```이라고 부른다. 간편하게 ```@Embeddable```애노테이션을 달아줌으로써 임베디드 타입임을 정의할 수 있다.

```Publisher```라는 불변 객체가 있다. ```name```과 ```location```을 갖고 있고,  이 객체를 특정 ```Entity```의 내장형으로 사용하고자한다면 다음과 같이 클래스를 생성해준다.

```java
@Embeddable
public static class Publisher {

	private String name;

	private Location location;

	public Publisher(String name, Location location) {
		this.name = name;
		this.location = location;
	}

	private Publisher() {} // 빈생성자는 필수이다.

	//Getters and setters are omitted for brevity
}

```

하지만 또다시, 불변객체 ```Location```이 존재한다. ```Embeddable``` 은 또 다른 임베디드 타입 객체를 내장할 수 있다.

```java
@Embeddable
public static class Location {

	private String country;

	private String city;

	public Location(String country, String city) {
		this.country = country;
		this.city = city;
	}

	private Location() {}

	//Getters and setters are omitted for brevity
}
```

임베디드 타입의 생명 주기는 부모 엔티티의 생명주기를 따르며 속성정보를 엔티티로부터 상속받는다.

또 같은 속성끼리 높은 응집도를 가지며, 의미 있는 메서드를 만들어 활용할 수 있다.  ```isCountryInAsia()``` 등등..



가령 하나의 임베디드 타입을 두개의 경우로 나눠서 사용하는 경우도 있다.
그럴땐 ```@AttributeOverride```를 사용하여 해결할 수 있다.

```java
@Embedded
@AttributeOverrides({
        @AttributeOverride(name = "city", column = @Column(name = "home_city")),
        @AttributeOverride(name = "street", column = @Column(name = "home_street")),
        @AttributeOverride(name = "zipcode", column = @Column(name = "home_zipcode"))
})
private Address homeAddress;

@Embedded
private Address workSpaceAddress;
```

```sql
Hibernate: 
    create table Member (
       member_id bigint not null,
        createdAt timestamp,
        createdBy varchar(255),
        modifiedAt timestamp,
        home_city varchar(255), // home
        home_street varchar(255), // home
        home_zipcode varchar(255), // home
        member_name varchar(255),
        city varchar(255), // work
        street varchar(255), // work
        zipcode varchar(255), // work
        team_id bigint,
        primary key (member_id)
    )
```
