# @Bean vs @Component
스프링은 열심히 코드를 작성해서 스프링 컨텍스트에 올려줘야 의존성이 주입되어 작성한 코드를 잘 활용할 수 있다. 그때마다 ```@Component vs @Bean``` 사이에서 고민이 된다. 어떤걸 사용해야 하는가... 쉽고 편한 것은 ```@Component``` 한 곳에 모아서 관리하고 싶으면 ```@Bean```을 사용해야 한다고 알고 있겠지만 이 두 애노테이션에는 차이점이 있다.

### @Bean
```java
@Configuration
public class 설정클래스{
	@Bean
	public ObjectMapper objectMapper(){
		return new ObjectMapper;
	}
}
```
1. ```@Bean``` 을 주입하기 위해서는 Class에서 ```@Configuration``` annotation을 달아 component scan이 이뤄져야 한다.
2. ```@Bean```클래스를 보면, ```@Target(ElementType.~~~)``` 에 ```TYPE```이 없다. 즉 클래스 단위에서 사용하지 못한다는 의미이다. 대신 ``` @Target(ElementType.METHOD)```가 있어 외부 라이브러리를 의존성 주입하기 위해서는 ```@Bean``` annotation 의 사용이 필요하다. 

### @Component
```java
@Component
public class 내가구현한클래스{
}
```
1. ```@Component```는 개발자가 직접 구현한 클래스에 선언하여 의존성을 주입해준다.
2. ```@Service```, ```@Repository```, ```@Controller```, ```@Configuration```은 모두 내부적으로 ```@Component```를 갖고있으며 당연히 해당 클래스는 자동으로 의존성을 주입받을 수 있는 것이다.

 ```@Bean```과 ```@Component``` 역할은 유사하지만 사용하는 상황은 전혀 다르다. 글의 서두에 설명한 관례와 각 annotation의 정확한 사용 시기를 알고 개발을 한다면 더 좋은 코드를 작성할 수 있을 것이다!!
