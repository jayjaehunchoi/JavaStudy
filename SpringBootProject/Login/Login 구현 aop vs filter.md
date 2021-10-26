# Login 구현
##  Session Login
우리 프로젝트에서는 Session Login을 사용할 예정이다. 
Session Login을 사용하기 앞서 로그인 공통 로직을 어떻게 해결할지에 대해 고민이 생겼다.
###  1. AOP
```@LoginCheck``` 애노테이션을 만들어 스프링 AOP를 이용해, ```@Around("annotation(~~)")``` 를 받는 메서드를 만들어 로그인을 체크하고, 로그인 성공 시 ```OK```, 실패시 ```Unathorized```를 리턴하는 로직을 짜는 방법이 있을 것 같다. 

#### 간단하게 구현한 AOP 로그인 체크 코드 
```java
@Around("annotation(com.aop.LoginCheck)")
public Object memberLoginCheck(ProceedingJoinPoint proceedingJoinPoint){
	HttpSession session = ((ServletRequestAttribute)(RequestContextHolder.currentRequestAttribute())).getRequest().getSession();
	String id = session.getAttribute(MEMBER_LOGIN_SESSION);
	if(id == null){
		throw new HttpStatusCodeException(HttpStatus.UNAUTHORIZED, "로그인 확인");
	}
	Object[] params = proceedingJoingPoint.getArgs();
	if(params.length != 0){
		params[0] = id; // 매번 첫 parameter를 id 값으로 처리 해야함 
	}
	return proceedingJoinPoint.proceed(params);
}
//@LoginCheck annotation=========================================================
@Retention(RetentionPolicy.RUNTIME) //런타임에도 annotation 유지
@Target(ElementType.METHOD) // 메서드 단위로 annotation 달 수 있음
public @interface LoginCheck{
}
```
하지만 웹 관련한 확인에 조금 불편함이 느껴지긴 한다. 그래서 서블릿 필터 활용을 고려해보았다.

### 2. 서블릿 필터 / 스프링 인터셉터
둘 중 하나를 사용하면 웹과 관련한 기능들을 제공해준다 ```request, response``` 등.
> **참고**
> 서블릿 필터와 스프링 인터셉터는 엄연히 다른 기능이다. 
> 서플릿 필터는 DispatcherServlet 작동 이전에 WAS와 가장 가까운 위치에서 실행되고
> 인터셉터는 스프링 컨테이너 내부에서 작동하는 필터(?)이다.

#### 간단하게 구현한 서블릿 필터 로그인 체크 코드 
```java
// 로그인 안해도 ok (Spring security에서 andMatcher,permitAll 느낌)
private static final String[] whiteList = {"/","/signin","/signup","/css/*"};

@Override // Filter 클래스 오버라이드  
public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {  
	 HttpServletRequest httpRequest = (HttpServletRequest) request;  
	 HttpServletResponse httpResponse = (HttpServletResponse) response;  
	 String requestURI = httpRequest.getRequestURI();  
	 try {  
	        log.info("인증 체크 필터 시작",requestURI);  
			if(isLoginCheckPath(requestURI)){  
	            log.info("인증 체크 로직 실행 {}" , requestURI);  
				HttpSession session = httpRequest.getSession(false);  
			 if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null){  
	           log.info("미인증 사용자 요청 {}", requestURI);  
			  //로그인으로 redirect  
			  httpResponse.sendRedirect("/login?redirectURL="+requestURI);  
			  return;  
			   }  
	        }  
	        chain.doFilter(request,response);  
	  }catch (Exception e){  
	        throw e;  
	  }finally {  
	        log.info("인증 체크 필터 종료 {}", requestURI);  
	  }  
}
//@Bean 등록==============================================================
@Bean
public FilterRegistrationBean loginCheckFilter(){
	FilterRegistrationBean f = new FilterRegistrationBean();
	f.setFilter(new LoginCheckFilter());
	f.setOrder(//원하는 순서);
	f.addUrlPatterns("/*"); // 위의 화이트리스트 여기서 처리해줘도 되긴함

	return f;
}
```

### 의사결정
두 방법 모두 장점이 있으나, API 서버를 만들어서 활용하는데 있어서는 aop를 활용하는게 조금 더 나을 수도 있겠다는 생각이 들고, 만약 filter 방식을 사용할 것이라면 Spring security 활용을 고민해야 할 것 같다. 
소셜 로그인 기능을 고려하고 있기때문에 Spring security 사용이 거의 필수적인 상황에서 우선 나는 ```filter or interceptor```에서 로그인 공통 처리를 진행하는 것이 더 나을 것 같다고 생각했다.
