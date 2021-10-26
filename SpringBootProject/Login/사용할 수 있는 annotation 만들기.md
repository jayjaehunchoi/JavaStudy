# 사용할 수 있는 annotation 만들기
로그인이 되어있는 상태라면, 로그인 session이 필요할 때 매번 ```@SessionAttribute(required = false)``` 와 같은 애노테이션을 길게 작성하곤 한다. 그러지 말고 사용할만한 annotation을 만들어 위 annotation을 대체해보자.

##  구현
#### Login
```java
@Retention(RetentionPolicy.RUNTIME) // runtime 시점에도 annotation이 살아있음
@Target(ElementType.METHOD) // 메서드에 달아줄 수 있음
public @interface Login{
}
```
annotation의 형태를 만드는 것은 정말 간단하다.  
 ```@Retention``` 은 annotation의 생명 주기를 설정해준다. 대표적으로 ```lombok```의 annotation들이 컴파일 시점에 삭제된다. ```Ctrl+click``` 하여 두 클래스의 차이를 비교해보도록!   
 ```@Target```은 annotation의 위치이다.  

 이렇게 annotation만 만든다고해서 바로 활용이 가능한 것은 아니다. 한 번 ```@Login``` 을 사용할 수 있는 annotation으로 만들어보자.

#### LoginArgumentResolver
```java
public class LoginArgumentResolver implements HandlerMethodArgumentResolver{
	// Login Annotation이 사용가능한 여건
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class); // Login annotation이 붙어있는지 
		boolean isParameterClassMember = Member.class.isAssignableFrom(parameter.getParameterType()); // Member 클래스인지 확인
		return hasLoginAnnotation && isParameterClassMember;
	}
	// 실제 기능
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
		HttpSession session = request.getSession(false);
		if(session == null){
			return null;
		}
		return session.getAttribute(SessionConst.LOGIN_MEMBER);
	}
}
``` 

이제 ```@Login``` annotation은 기능이 확실하게 생겼다. 하지만 아직 사용할 수 없다. 
```argumentResolver```를 사용하기 위해서는 등록이 필요하다.

#### WebConfig
이전에 계속 작성해오던 WebConfig에 argumentResolver를 추가해주자.
```java
@Override
public void addArgumentResolver(List<HandlerMethodArgumentResolver> resolvers){
	resolvers.add(new LoginArgumentResolver());
}
```
설정은 모두 끝났다. 이제 이 annoation을 session Login 값 사용시 편하게 사용하면 된다 ^^!
