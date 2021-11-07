# Validator
개발을 할 때 검증은 필수적이다. ```javax.validation``` 라이브러리는 ```Http body``` 에서 데이터가 넘어올 때 , 쉽게 검증을 할 수 있도록 ```annotation``` 기반의 ```validate```기능을 갖고 있다. [자바 8 기준 validate annotation](https://javaee.github.io/javaee-spec/javadocs/javax/validation/constraints/package-summary.html) 
하지만 내가 사용해보기로는 이 애노테이션으로는 하나의 필드 변수씩 검증하는 기능을 가지고 있고, 만약 복잡한 계산로직이 들어간 내용을 검증하려 한다면 ```controller```에서 추가적인 로직을 수행하여, ```controller```가 굉장히 복잡해지는 부분이 있었다. 이를 해결하기 위해 ```spring framework``` 에서 제공하는 ```Validator```를 사용해보고자 한다.  
[validation 참고](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#validation)

## 예시 상황
> 정말 간단한 곱하기 계산기를 만들고자 한다. 하지만 인터페이스상 100 이상의 숫자를 표현하지 못한다. 입력값과 출력값이 모두 100을 초과하지 않게 계산기를 만들어야 하며, 만약 100을 초과할 경우 ```500``` error를 리턴한다. (단순하게 서버 콘솔에만 띄우면 됨.)

이 지문을 보면 다음과 같은 설계를 할 수 있을 것이다.

1. ```dto```를 만들어 ```@Max``` 값을 100으로 두어 입력값을 100으로 제한시킨다.
2. 곱하기 후의 값이 100을 초과한다면 error를 날려준다. ```controller```에서 작업 수행.

자 이제 하나하나 이 문제점을 해결해보자.

### NumberDto
```java
@Data  
public class NumberDto {  
    @Max(100)  
    private int numberL;  
    @Max(100)  
    private int numberR;  
}
```
> 간단하게 Max 값을 두어 크기를 100으로 제한시켰다.

### NumberController
```java
@Slf4j
@RestController  
public class NumberController {  
    @PostMapping("/multiple")  
    public int multiple(@RequestBody @Valid NumberDto numberDto, BindingResult bindingResult){  
        log.info("number L = {}, number R = {}", numberDto.getNumberL(), numberDto.getNumberR());  
	    if(bindingResult.hasErrors()){  
            throw new IllegalArgumentException("100 초과");  
	    }  
        return numberDto.getNumberL() * numberDto.getNumberR();  
  }   
}
```
> dto 에 validation 걸어놓은 내용을 controller에서 @Valid 애노테이션과 bindingResult를 받아 error 를 처리해줬다.

자 이제, 테스트를 한 번 돌려보자.
### request JSON
```
{
	"numberL":101,
	"numberR":95
}
```
### errorMessage
```
java.lang.IllegalArgumentException: 100 초과
```
**하지만 95,95 를 넣어서 돌려본다면?**
![image](https://user-images.githubusercontent.com/87312401/140632317-a6c241ca-1829-47a3-946c-c8ef2815b063.png)
> 분명 비즈니스 요구사항에서 결과값이 100 초과가 나오면 안된다고 하였으나 100 이 초과하여 나왔다. (당연히 controller에서 처리해주지 않았으니,,,) 이제, Controller는 Controller의 역할만 수행하면서 복잡한 validation을 할 수 있도록 validator 클래스를 만들어보자.

### NumberDtoValidator
```java
@Slf4j  
@Component  
public class NumberDtoValidator implements Validator {  
    @Override  
  public boolean supports(Class<?> clazz) {  
        log.info("class {}",clazz.getName());  
		return NumberDto.class.isAssignableFrom(clazz);  
  }  
  
    @Override  
  public void validate(Object target, Errors errors) {  
      log.info("validate 시작");  
	  NumberDto numberDto = (NumberDto) target;  
	  if(numberDto.getNumberL() * numberDto.getNumberR() >100){  
	     errors.reject(null);  
	  }  
  }  
}
```
> target은 NumberDto가 들어갈 것이고, Error에는 bindingresult가 들어간다. (부모 클래스임) 하지만 단순히 이 Validator가 있다고 validation이 돌아가는 것은 아니다. 이 validation을 수행시키는 데에는 두가지 방법이 있지만 편리하게 annotation 기반으로 이 validation을 수행해보자

### NumberController
```java
@Slf4j  
@RequiredArgsConstructor  
@RestController  
public class NumberController {  
      private final NumberDtoValidator numberDtoValidator;  
	  @InitBinder  
	  public void init(WebDataBinder webDataBinder){  
	        webDataBinder.addValidators(numberDtoValidator);  
	  }
``` 
> 이제부터 NumberController가 실행될때마다 WebDataBinder에 우리가 만들어서 의존성주입을 받은 numberDto를 실행시킬 것이다. 그리고 기존의 @Valid 애노테이션을 @Validated로 바꿔준다. (Validated가 Valid기능을 포함함) 

### 이제 결과값을 돌려보면 ? 
![image](https://user-images.githubusercontent.com/87312401/140632424-f22774bb-13a9-4184-ba2a-890c5d7c5b2f.png)

500 에러가 뜨는 것을 확인할 수 있다. ```500```에러 메시지로는 다음과 같이 우리가 원하는 내용이 정상 출력되는 것을 확인할 수 있다.
![image](https://user-images.githubusercontent.com/87312401/140632431-55cd3df4-e76c-4fde-ba37-09556db86545.png)
이처럼 복잡한 ```validate```로직이 ```controller```에 들어가는 것을 막기 위해서는 ,  ```springframework```에서 제공하는 ```Validator```인터페이스를 사용할 수 있다.
