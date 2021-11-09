# JSON으로 ENUM 타입 파싱하기
이번 프로젝트에서 편리하게 특정 카테고리를 관리하기 위해 ```Enum``` 타입을 활용하였다. 하지만 ```dto```를 만들던 도중, **```Enum``` 타입은 어떻게 파싱되지?** 라는 궁금증이 생겼다. 그래서 테스트를 진행하였고 문제를 발견했다. 이번 포스팅은 코드를 통해 문제점을 발견하고, 해결하는 과정을 보여주고자 한다.

### Member
> 테스트 용 Member class를 만들어 json으로 값을 전달받을 수 있는 dto용도로 사용한다.
```java
@NoArgsConstructor  
@Getter  
public class Member {  
    private String name;  
    private Role role; // role이라는 enum 타입을 생성하였다.  
}
```  

### Role
> 초 간단 enum 타입을 만든다.  브라우저에서 user가 입력할 값을 변수로 갖는다.
```java
public enum Role {  
    ADMIN("관리자"),  
    MEMBER("이용자");    
	private String roleName;    
	Role(String roleName) {  
	     this.roleName = roleName;  
	}  
	public String getRoleName(){  
	     return roleName;  
    }  
}
```

### Controller
> Member data를 전달받을 수 있는 controller를 세팅한다.
```java
@RestController  
public class MemberController {  
    @PostMapping("/member")  
    public Member getMember(@RequestBody Member member){  
        return member;  
  }  
}
```
전체 과정은 다음과 같다.  
 ```user```는 Member의 정보를 입력한다. 이때, ```관리자```인지 ```사용자```인지 구분하여 입력될 예정이고, 입력된다면 해당 정보를 다시 browser에 출력해준다. 이 과정을 ```Postman```을 이용하여 테스트 해보자.

### 1. 데이터 입력
![image](https://user-images.githubusercontent.com/87312401/140856508-9d2a9d72-f49a-42a5-86bf-8e174b44ee3a.png)
 
 > name 과 role을 입력하고 Post방식으로 데이터를 전송한다.

### 2. 에러
```
2021-11-09 12:24:06.805  WARN 25384 --- [nio-8080-exec-7] .w.s.m.s.DefaultHandlerExceptionResolver : Resolved [org.springframework.http.converter.HttpMessageNotReadableException: JSON parse error: Cannot deserialize value of type `test.ajaxserver.testdomain.Role` from String "관리자": not one of the values accepted for Enum class: [MEMBER, ADMIN]; nested exception is com.fasterxml.jackson.databind.exc.InvalidFormatException: Cannot deserialize value of type `test.ajaxserver.testdomain.Role` from String "관리자": not one of the values accepted for Enum class: [MEMBER, ADMIN]<LF> at [Source: (PushbackInputStream); line: 3, column: 12] (through reference chain: test.ajaxserver.testdomain.Member["role"])]
```
> 해석해보면,  ```관리자```라는 이름의 String 값을 role enum타입으로 ```deserialize``` 할 수 없다고 나온다. 분명 ADMIN 변수에 우리가 원하는 한글명을 줬지만, 이렇게 입력하면 enum타입으로 시리얼라이즈 할 수 없는 것이다. 물론 프론트에서 관리자 ->  ```ADMIN```, 사용자 -> ```MEMBER``` 로 전환 할 수 있지만 우리는 서버에서 해결하는 방법도 알고 있어야 한다.

### @JsonCreator
> jackson 라이브러리에서 제공해주는 ```@JsonCreator```를 이용하여 이 문제를 쉽게 해결할 수 있다. 
```java
@JsonCreator  
public static Role getRoleFromRoleName(String roleName){  
    for (Role role : Role.values()) {  
        if(role.getRoleName().equals(roleName)){  
            return role;  
	    }  
    }  
    return null;  
}
```
```enum``` 클래스에 해당 내용을 추가 한 뒤 ```postman``` 테스트를 돌리면? 아래와 같이 문제가 해결된 것을 확인할 수 있다.

![image](https://user-images.githubusercontent.com/87312401/140858347-20425b1d-6f26-4622-b76d-539ad26648cd.png)

이렇게 ```Jackson``` 라이브러리에서 제공하는 ```@JsonCreator``` 를 사용하여 간편하게 browser -> server로 넘어오는 enum 데이터를 parsing 해줄 수 있다.
