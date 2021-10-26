# Interceptor
Login 기능을 구현하기 위해 Spring의 Interceptor를 이용할 가능성이 생겨 직접 구현해보았다.
> ** 참고 **  
> Interceptor는 스프링 컨테이너 내부 (디스패처 서블릿 이후)에서 작동한다.
> Filter는 서블릿에서 작동한다. 즉, Filter -> dispatcher servlet -> interceptor의 작동 순서를 가진다.
> filter 보다 interceptor가 코드 작성이 더 간단하기 때문에 servletRequest를 커스터마이징하여 사용할 것이 아니라면 interceptor를 쓰는게 더 편할 듯 하다.

## 구현
### HandlerInterceptor
interceptor를 구현하기 위해서는 ```HandlerInterceptor``` 클래스를 상속받아야 한다.
```HandlerInterceptor```는 ```preHandler()```, ```postHandler()```, ```afterCompletion()``` 메서드를 상속받을 수 있고 모두 ```default```로 생성되어 있기 때문에 필요한 메서드만 상속받으면 된다.

로그인 인터셉터를 구현하기 위해서는 ```preHandle()``` 정도만 필요할 것 같아 해당 메서드만 참조 받았다.
#### LoginCheckInterceptor
```java
@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor{
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
		String requestURI = request.getRequestURI();
		log.info("[{}] Start Login Check ", requestURI);
		HttpSession session = request.getSession(false);
		if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER){
			response.sendRedirect("/login?redirectURL="+requestURI);
			return false;
		}
		return true;
	}
}
```

이렇게 코드를 작성하면 과연 ```Interceptor```가 작동할까? 당연히 작동하지 않는다.
설정 파일을 만들어 ```@bean``` 주입을 해줘야한다.

#### WebConfig
```java
@Configuration
public class WebConfig implements WebMvcConfigurer{
	
	@Override
	public void addInterceptors(InterceptorRegistry registry){
		registry.addInterceptor(new LoginCheckInterceptor())
				.order(1)
				.addPathPatterns("/**") // 전부 검증
				.excludePathPatterns("/login","/logout","/css/**","/"); // 제외 URI
	}			
}
```

```Config``` 클래스를 만들어 Interceptor를 위와같이 추가해주면, 설정한 URL의 컨트롤러에서 Interceptor가 작동하여 로그인 상태를 확인해준다.
