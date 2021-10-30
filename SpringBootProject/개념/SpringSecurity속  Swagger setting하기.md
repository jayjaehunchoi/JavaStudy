# SpringSecurity속  Swagger setting하기
이번 글은 직접 ```Spring Security```로 ```jwt``` 인증을 걸어놓은 상태에서 Swagger를 적용해보고 이를 공유하기 위해 작성하였다.

먼저 현재 구현되어 있는 ```api``` 서버 정보를 공유하자면, ```jwt```로 로그인을 구현하고, 인증이 되었을때만, ```todo```리스트를 작성할 수 있는 간단한 crud api이다.

#### 이번 글에서 주의 깊게 살펴볼 패키지는 다음과 같다. 
```
|__controller
	|__TodoController
	|__UserController
|__config
	|__WebSecurityConfig
	|__WebMvcConfig
```

## build.gradle
```swagger```를 사용하기 위해서는 ```dependecy```를 가져와야한다.
```maven Repository```에 접속하여 ```springfox-swagger2```와 ```springfox-swagger-ui```를 가져온다. 
> swagger는 2.xx 버전과 3.xx 버전의 사용 방법이 완전히 다르다. 이번 글에서는 사람들이 가장 많이 사용하고 레퍼런스도 많은 2.9.2 버전을 가져왔다.

###  dependency
```groovy
implementation 'io.springfox:springfox-swagger2:2.9.2'  
implementation 'io.springfox:springfox-swagger-ui:2.9.2'
```
## SwaggerConfig
```build```가 완료되었으면 , ```swagger```를 사용할 수 있는 설정을 세팅해주자. 최대한 간단하게 설정을 하였으며 설정 내용은 코드에 주석으로 설명하겠다.
```java
@Configuration
@EnableSwagger2
public class SwaggerConfig{
	@Bean //swagger 사용을 위해 Docket 의존성 주입
	public Docket apiAdmin(){ 
		ApiInfoBuilder apiInfo = new ApiInfoBuilder();
		apiInfo.title("Todo Api 서버 문서"); // swagger ui의 타이틍
		apiInfo.description("Todo API 서버 문서입니다.") // swagger ui api문서 요약
		Docket docket = new Docket(DocumentationType.Swagger_2);
		return docket.apiInfo(apiInfo.build())
				.select()
				.apis(RequestHandlerSelectors.basePackage("todo.todo.controller"))
				.paths(PathSelectors.ant("/**"));
		// apis : 패키지 지정, @ApiOperation이 달려있는 메서드만 설정 가능
		// paths : apis에 해당하는 패키지의 path에 해당하는 api swagger에 노출			
	}
}
```
```Spring security```가 적용되지 않는 상황에서는 이렇게 설정하고 실행하면 ```swagger-ui```에 접속이 가능하다. 일반적으로는 ```security```로 인증이 구현되어있기때문에, 추가적인 설정이 필요하다.

## WebConfig
먼저 ```swagger-ui```에 접근할 수 있도록 ```resourcehandler```에 ```swagger-ui```를 구성하는 ```js, css, html``` 등을 등록해줘야 한다.
기존에 작성해두었던 ```WebMvcConfigurer```를 상속받는 클래스에 ```addResourceHandler```  메서드를 추가하여 경로를 등록해준다.

```java
@Override
public void addResourceHandler(ResourceHandlerRegistry registry){
	registry.addResourceHandler("swagger-ui.html")  
        .addResourceLocations("classpath:/META-INF/resources/");  
	registry.addResourceHandler("/webjars/**")  
        .addResourceLocations("classpath:/META-INF/resources/webjars/");
}
```

## WebSecurityConfig
만약 ```Security```에 접근 경로를 허가하지 않으면 어떻게될까? 당연히 ```Spring Boot```에서 제공하는 ```WhiteLabelError```가 나오며 ```403```에러가 뜰 것이다. HTTP 상태 코드에 익숙한 사람은 바로 눈치챌 것이다. ```permit```경로에 추가해줘야하는구나! 나 또한 그랬기때문에 ```swagger-ui.html```을 허가경로로 추가해줬다.

```java
http.cors ....(생략).antMatchers(.."swagger-ui.html**"..);
``` 
추가하고 접근하면?
![image](https://user-images.githubusercontent.com/87312401/139519936-9d0cf905-7d7e-4ecd-8b73-a8ce601d3087.png)
**시원하게 빈화면이 등장한다.**
분명 나와야 할 화면이 있는데, 나오지 않는다면 개발자 도구를 켜보자.

![image](https://user-images.githubusercontent.com/87312401/139519962-15d901e4-b67b-4bf6-8602-f715e9fb8093.png)
html만 permit처리하니 당연히 나머지는 ```403```에러가 뜬다. 이제 문제를 깨달았으니, 바로 ```SecurityConfig```에 403이 뜬 경로들을 무시해달라고 추가해보자.

```java
@Override  
public void configure(WebSecurity web) throws Exception {  
  
   web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui",  
  "/swagger-resources/**", "/webjars/**", "/swagger/**",  
  "/swagger-ui.html**", "/configuration/security");   
}
```
경로 무시 code를 작성하고 다시 서버를 돌려보자.
![image](https://user-images.githubusercontent.com/87312401/139520021-d3518493-8b04-4d56-98b1-5607e24d9af6.png)
화면이 모두 잘 나오는 것을 볼 수 있다. 이제부터 ```Spring security```가 걸려있는 상황에도 어떻게 하면 ```swagger-ui```를 띄울 수 있는지 알게됐다.
이제부터는 ```Controller```를 다니면서 ```@Api``` , ```@ApiOperation``` 등 원하는대로 설명을 달면서 ```API```문서를 완성하면 된다.
