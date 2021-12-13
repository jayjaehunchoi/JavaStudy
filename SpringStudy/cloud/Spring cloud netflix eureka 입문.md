# Spring Cloud Netflix Eureka
> ```Spring Cloud Eureka```를 이용하여 ```Service Discovery``` 를 도전해보자

```MSA```에 대해 공부하던 중, ```Spring cloud```를 이용한 ```Service Discovery``` 경험이 필요하다고 생각했다.
```Service Discovery```는 ```Spring Eureka```에 포트가 다른 동일 서비스를 등록하여 ```Api gateway```를 통해 접근 요청이 들어왔을때,
```key, value```형태의 저장소에서, 적절한 포트를 찾아 반환해줌으로써 원활하게 서비스를 이용할 수 있게 돕는 서비스이다.

### 구조

![image](https://user-images.githubusercontent.com/87312401/145758995-bfa589aa-b0b1-40b1-b78c-e99d1b213134.png)


## 시작하기
```Eureka server```와 ```Eureka client```로 나뉘어 찾는역할, 등록되어 포워딩되는 역할로 작성할 예정이다.

```spring cloud``` ```2021.0.0``` 버전을 이용할 것이며, 이는 ```spring boot```가장 최신 버전인 ```2.6.1```버전에 적합하다.
![image](https://user-images.githubusercontent.com/87312401/145758702-39c6db50-d56d-4a17-93f2-b873fe6ebccd.png)


## Eureka Server

```Spring initializer```에 접속하여 ```gradle``` , ```2.6.1```, ```java 11``` 을 선택하고 ```dependencies```에 ```Eureka Server```를 선택해준다.

![image](https://user-images.githubusercontent.com/87312401/145758886-734b99df-7885-477a-89fe-808d7766639d.png)

프로젝트를 불러온 뒤, ```application.yml``` 파일을 생성해준다.

```yml
# 1
server:
  port: 8761

# 2
spring:
  application:
    name: discoveryservice

# 3
eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
```

1. ```port``` 번호는 사용하지 않는 포트로 자유롭게 등록하면 된다.
2. micro service architecture 를 구분하기 위한 id이다. 자유롭게 등록해주면 된다.
3. 현재 작동되는 것은 ```eureka server```인데 3번 옵션을 지정하지 않으면 ```server```의 클라이언트로 자기 자신을 등록하게 된다.


spring boot 실행 클래스에 접근하여 ```@EnableEurekaServer```애노테이션을 추가하고 애플리케이션을 실행한다.  
아래와 등록한 포트로 접근하였을 때, 아래와 같은 화면이 나온다면 성공이다.

![image](https://user-images.githubusercontent.com/87312401/145759329-fdbe7482-a5e6-4d22-8001-e0ff0b2ebedb.png)


## Eureka client


위 ```Eureka server``` 설정과 동일하게 설정한 뒤, ```dependencies```에 아래 설정을 추가해준다.
> lombok은 추후 개발 편의를 위함

![image](https://user-images.githubusercontent.com/87312401/145759443-bfbca5c5-2577-4859-aa15-7c01b8cf1892.png)

```application.yml``` 파일을 생성한다.

```yml
# 1
server:
  port: 0

spring:
  application:
    name: user-service

# 2
eureka:
  client:
    register-with-eureka: true
    fetch-registry: true
    service-url:
      defaultZone: http://127.0.0.1:8761/eureka
  instance:
    instance-id: ${spring.cloud.client.hostname}:${spring.application.instance_id:${random.value}}

```

1. 포트를 0으로 설정하게 되면 , ```spring boot```가 랜덤 포트로 실행시켜준다. 만약 포트를 지정한다면 새 인스턴스를 생성할 때마다 일일히 포트 설정을 해줘야 한다.
```
vm 옵션 작성 후 실행
./gradlew bootrun --args='--server.port=8081'
java -jar -Dserver.port=8081 ./build/eurekaclient-0.0.1-SNAPSHOT.jar
```
2. ```register-with-eureka``` : 유레카 서버로부터 인스턴스 정보를 주기적으로 가져옴ㄴ
```service-url``` : 유레카 서버 주소
```instance-id``` : 랜덤 포트로 생성될 시 인스턴스 이름 설정

spring boot 실행 클래스에 접근하여 ```@EnableDiscoveryClient```애노테이션을 추가하고 애플리케이션을 실행한다. 


이제 여러개의 인스턴스를 생성하여 실행해보면, 아래와 같이 각 인스턴스가 등록된다.
해당 인스턴스에 접근했을 때, ```Spring boot```에서 제공하는 ```white label error```가 뜨면 성공!

![image](https://user-images.githubusercontent.com/87312401/145760090-c19ca474-b6d9-4b8f-a566-2b1956f372c6.png)


### 정리
```MSA``` 에 대해 공부하고 있다. 최근에는 코드를 작성하는 것 보다 아키텍처, 클린코드, 리팩토링 쪽에 관심이 많이 생긴다.
다양하게 경험해보고 최고의 선택을 할 수 있는 개발자가 되고싶다.
