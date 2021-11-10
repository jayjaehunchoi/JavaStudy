# Spring Security
프로젝트를 하면서 무작정 ```Security``` 설정에 구글링한 내용을 복사 붙여넣기 했기 때문에 우리의 프로젝트에 맞게 ```customizing``` 하는 과정이 굉장히 어려웠다. 그래서 이번에는 ```Spring Security Reference```를 읽고 정리하여 ```Spring Security```에 대한 이해도를 높이고자 한다.

### Spring Security는 Filter에서 돌아간다.
```Spring Security```는 ```Spring```이라는 이름이 붙어있기 때문에, ```Spring``` 사용시에만 적용 가능한 내용이라고 생각한다. 하지만 그렇지 않다. ```Servlet Filter```에서 작동하기 때문에, ```Spring```환경이 아니더라도 ```Security```를 사용할 수 있다.

### dependency, gradle
```groovy
dependencies { 
implementation "org.springframework.boot:spring-boot-starter-security" 
}
```

### Spring Boot 자동 설정
* ```Spring Security``` 의존성을 받아오면 자동으로 ```SpringSecurityFilterChain```을 받아온다. 이 filterChain은 ```url```, ```validate```, ```redirect``` 를 담당한다.
* 다음과 같이 ```INFO```레벨 로그로 user, password를 생성한다.```Using generated security password: 8e557245-73e2-4286-969a-ff57fe326336``` 
* 모든 요청에 ```SpringSecurityFilterChain```를 걸어놓는다.
* 그 외
  * 기본 로그인 폼 제공
  * Bcrypt 인코더 제공
  * CSRF 공격 보호
  * 다양한 보안 위협으로부터 보호

## Security Exception 처리과정
Security config를 다루면서 "이 오류가 왜나지?" 하는 경험을 많이 했다. 이런 오류의 문제점을 알기 위해서는 Security Exception의 처리 과정을 정확히 이해해야 한다. Exception 처리 과정은 다음 그림과 같다.
![image](https://user-images.githubusercontent.com/87312401/141035929-a194b46d-be56-44cd-a50b-3df852348f7c.png)

1. Filter chain 중 한 과정인 ```ExceptionTranslationFilter```가 ```doFilter```를 호출하여, application을 동작시킨다.
2. 만약 인증되지 않은 사용자라면? 
   1. 인증된 유저가 저장되어 있는 ```Security Context Holder```를 모두 비운다 
   2. 그리고 요청 정보를 cache에 저장하여, 이후 인증되었을때 최초 요청을 다시 발생시켜준다.
   3. 마지막으로 ```AuthenticationEntryPoint```가 login 페이지로 redirect 해준다.
3. 만약 접근 거부 에러가 발생하면, 접근을 금지시킨다.
> 만약 ```AccessDeniedException``` , ```AuthenticationException```에러가 발생하지 않는다면, ```ExceptionTranslationFilter```가 작동하지 않은 상황이라고 생각하면 된다. (오류 없음)
> 이외에도 정말 많은 filter chain이 돌아간다. 다른 경우들은 양이 너무 방대하여 에러가 날 때마다 확인하는 것이 좋겠다..

## HttpSecurity
Spring Security는 ```WebSecurityConfigurerAdapter```를 이용하여 설정을 불러온다. 이때 ```configure```메서드를 활용한다. 다음 예시를 통해 ```HttpSecurity```를 이해해보자.
```java
protected void configure(HttpSecurity http) throws Exception{
	http.authorizeRequests(authorize -> authorize
	.anyRequest().authenticated())
	.formLogin(withDefaults())
	.httpBasic(withDefaults());
}
```
위 설정을 추가함으로써 ```Spring Security```에서는 다음과 같은 인증을 진행한다.
* 모든 요청에 대한 유저 정보 인증 필요
* 기본 로그인 폼 제공
* 기본 HTTP 인증 적용

### Multiple HttpSecurity
그렇다면 여러가지 ```HttpSecurity``` 가 필요한 상황에는 어떻게 하면 좋을까? 간단하게 ```WebSecurityConfigureAdapter```를 상속받는 ```Config``` 클래스를 만들어, ```configure```메서드에 필요한 내용을 작성하면 된다.

```java
@Configuration  
@Order(1) // 1번 순서
public  static  class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter { 
// api로 시작하는 url은 admin만 접근 가능
	protected void configure(HttpSecurity http) throws Exception {
		 http.antMatcher("/api/**")
		 .authorizeRequests(authorize -> authorize .anyRequest().hasRole("ADMIN"))
		 .httpBasic(withDefaults()); 
		 } 
 } 
@Configuration  
public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter { 
	@Override  
	protected void configure(HttpSecurity http) throws Exception {
		 http .authorizeRequests(authorize -> authorize .anyRequest().authenticated())
		 .formLogin(withDefaults()); 
	 } 
 }
```
이번 포스팅으로 간단하게나마 ```Security``` 작동원리를 알게 되었고, 어떻게 ```authentication``` 을 설정하면 좋을지도 고민할 수 있었다. rest api에서 사용하기 위해 조금 더 많은 노력이 필요하겠지만, 하나하나 알아가면 될 것 같다. 
