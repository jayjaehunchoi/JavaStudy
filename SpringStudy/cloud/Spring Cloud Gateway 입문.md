# Spring Cloud Gateway

## API Gateway
```API Gateway```는 클라이언트가 요청을 보내면, 프록시로 클라이언트 대신 요청을 보내고, 적절한 응답을 해주는 기능이다.
인증과 권한 부여, 서비스 검색 통합, 응답 캐싱, 부하 분산, 로깅 등 다양한 기능을 통합적으로 수행할 수 있게 돕는다.

과거에는 ```netflix zuul```을 이용해서 동기 방식으로 게이트웨이 역할을 수행했는데, 현재 버전에서는 ```Maintenance``` 상태이다.
따라서 이제부터는 ```Spring Cloud Gateway```를 사용해야한다. 

> ```SCG```는  ```zuul``` 보다 성능이 좋으며, ```Predicate```와 ```filter```가 조합되어 동작한다.
> 또, 비동기를 지원하기 때문에 ```Tomcat```이 아닌 ```Netty``` 서버가 실행된다.
> ```Servlet```이 아니고 ```ServerHttp~```를 사용하더라. 

![image](https://user-images.githubusercontent.com/87312401/145935733-4bab8f40-423f-432c-b898-d66dcce7b740.png)


## 예제
코드를 통해 ```MicroService```와 매핑되는 과정을 직접 확인해보자.

![image](https://user-images.githubusercontent.com/87312401/145936382-41a6580f-e14b-4962-aacc-5c761701dca9.png)

### MicroService

먼저 다른 역할을 수행하는 두 가지 ```Service```를 생성한다.
추후 ```Eureka```에 등록하여 로드밸런싱하기 위해 관련 의존성을 추가하고 기본적인 의존성 추가해준다.

각 서비스마다 ```port```번호와 ```name```을 다르게 설정해주고, ```Controller```를 아주 기본적인 형태로 생성한다.
```yml
server:
  port: 8081 # 다른 마이크로 서비스의 경우 포트번호 변경

spring:
  application:
    name: my-first-service # 다른 마이크로 서비스의 경우 이름 변경

eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
```

```java
@RestController
@RequestMapping("/first-service") // 다른 마이크로 서비스는 second 입력
public class FirstServiceController {

    @GetMapping("/welcome")
    public String welcome() {
        return "welcome to the First service"; // 다른 마이크로 서비스는 Second로 추가
    }
}
```

### Gateway

```eureka```에 등록할 예정이기 때문에, 관련 의존성을 추가해주고 ```gateway```의존성 추가해준다.

```yml
server:
  port: 8000

eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
    service-url:
      defaultZone: http://localhost:8761/eureka

spring:
  application:
    name: apigateway-service
  cloud:
    gateway:
      routes:
        - id: first-service
          uri: http://localhost:8081/
          predicates:
            - Path=/first-service/**
        - id: second-service
          uri: http://localhost:8082/
          predicates:
            - Path=/second-service/**
```

```route```에 ```[id, uri, prediacates]``` 형태로 값을 넣어준다.
위와 같이 넣어주면 ```http://localhost:8000/second-service/**``` 가 입력될 때, ```http://localhost:8082/second-service/**```로 매핑된다.  

![image](https://user-images.githubusercontent.com/87312401/145937188-16bcb9fe-e6f5-4748-b80a-104fc3e950e7.png)

위 구조에서 볼 수 있다시피 게이트웨이를 통과할 때 필터를 걸어줄 수 있다.
```Custom Filter```를 걸어줄 수 있고 ```Global Filter```를 걸어줄 수 있다.

### Custom Filter

```java

@Component
@Slf4j
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {

    @Data
    public static class Config {
    }

    public CustomFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(CustomFilter.Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Custom PRE filter: request id -> {}", request.getId());

            // Custom PostFilter
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                log.info("Custom POST filter: response code -> {}", response.getStatusCode());
            }));
        });
    }
}
```
```Netty```환경에서 ```http```관련 요청, 응답 정보를 가져오기 위해 ```exchange```를 사용한다.
값이 요청으로 넘어올 때, 위의 ```pre``` 내용을 로깅하고, 응답으로 나가야 할 때, ```Spring webflux```의 ```Mono```를 이용해 로깅을 남겨준다.

> yml 설정

```yml
# routes 하단에 추가한다. 깊이레벨은 predicates와 동일
      filters:
            - CustomFilter
```

### Global Filter
자바 코드 자체는 ```custom filter```와 거의 동일하다. ```config``` 내용을 추가해보자.

```java
package com.example.cloud;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {

    @Data
    public static class Config {
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }

    public GlobalFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(GlobalFilter.Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Global Filter baseMessage : {} ", config.getBaseMessage());

            if(config.isPreLogger()) {
                log.info("Global filter start: request id -> {}", request.getId());
            }
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                if(config.isPostLogger()) {
                    log.info("Global filter end: response code -> {}", response.getStatusCode());
                }
            }));
        });
    }
}
```

```yml
# spring.cloud.gateway 하단에 추가, routes와 같은 깊이

      default-filters:
        - name : GlobalFilter
          args :
            baseMessage: Spring Cloud Gateway Gloabla Filter
            preLogger: true
            postLogger: true
```

이제 다시 한 번 값을 넘겨보자.  

![image](https://user-images.githubusercontent.com/87312401/145937949-d4e6d979-5bae-405e-9882-744dc648000a.png)

이처럼 로그가 잘 남은 것을 확인할 수 있다. (로깅 순서도 자바 코드를 통해 설정할 수 있다.)

### 마무리
```MSA```를 사용하는 데 있어서 ```Gateway```는 필수적인 듯하다.
지금은 단순히 로깅을 남겼지만, ```header```에 ```auth```정보를 남기거나 , ```session```을 공통으로 사용해야 할 떄 그 활용도가 높을 것 같다.
우선은 기능을 구현하는 데 자체에 집중했지만, 이에 익숙해지면 조금 더 깊이있게 파볼 생각이다.
