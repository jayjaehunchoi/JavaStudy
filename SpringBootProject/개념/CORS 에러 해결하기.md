# CORS 에러
프로젝트 도중 빠르게 프론트엔드와 확인해 볼 사항이 있어 간단한 ```api``` 를 작성하여 데이터를 주고 받고자하였다. 그런데 ```CORS```에러가 발생했고, 결국 테스트를 진행하지 못했다. 분한 나머지 ```CORS```에 대해 알아보게 되었다. 

>CORS는 크로스 오리진 리소스 셰어링의 약자로, 처음 리소스를 제공한 도메인이 현재 요청하려는 도메인과 다르더라도 요청을 허락해주는 웹 보안 방침이다. 

어떠한 설정도 존재하지 않는다면 CORS 방침으로 인해 local환경에서 ```3000```번 포트를 이용하는 프론트엔드 서버가 , ```8080``` 포트를 이용하는 백엔드 서버에  ```request```를 보낼 때 요청이 거절된다. 
요청이 거절될 수 있는 이유는 ```HTTP``` 요청 ```header```를 보면 쉽게 알 수 있다. 요청 헤더를 요약하면 다음과 같다.
```
GET /login HTTP/1.1
Host: localhost:8080
...
Origin : http://localhost:3000
```
위 요청헤더에서 보이듯, 프론트 서버에서는 ```api```서버에 요청을 보낼때, ```origin```에 프론트 엔드 리소스의 서버를 함께 보낸다. 백엔드 서버에서는 이 ```origin```을 확인하고, 만약 같지 않을 때 다음과 같이 ```response```를 날린다.

```
HTTP/1.1 403
```
바로 ```403, Forbidden```이다. 그렇다면 프론트엔드 서버와 api 서버가 완전 분리되어 있을때 (ex. ```CSR```) 어떻게 이 문제를 해결할 수있을까?
이는 ```Spring Boot```에서 ```Configure``` 클래스를 만들어 ```CORS``` 설정을 넣음으로써 해결할 수 있다.

## WebMvcConfig
```Spring Boot``` 환경에서 웹 개발을 해보았다면 이 설정파일을 만든 경험이 있을 것이다. ```WebMvcConfigurer```를 상속받아  ```argumentresolver```나 ```interceptort```를 추가할 때 주로 사용하는데, 여기서 ```CORS``` 관련 설정 또한 해결할 수 있다.
```java
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	private final long MAX_AGE_SECS = 3600; // CORS 성능 효율 위해, 60분마다 OPTION을 보냄

	@Override
	public void addCorsMappings(CorsRegistry registry){
		registry.addMapping("/**")
			.allowedOrigins("http://localhost:3000") // 만약 배포한다면 당연히 프론트 서버 주소 입력
			.allowedMethods("GET","POST","PUT","PATCH","DELETE","OPTIONS")
			.allowedHeaders("*")
			.allowCredential(true)
			.maxAge(MAX_AGE_SECS);
	}
}
```
> 모든 경로(/**)에 대해 Origin값인 경우, 상기 메서드를 이용한 요청을 허용하고, 모든 헤더와 인증에 관한 Credential도 허용한다는 의미이다.
