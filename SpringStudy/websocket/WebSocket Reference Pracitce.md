# WebSocket

> 스프링 레퍼런스에 나와있는 WebSocket을 정리한다.


## Introduction to WebSocket

웹소켓 프로토콜은 ```Single TCP Connection```을 통해 클라이언트 - 서버 간에 전이중 양방향 통신채널을 설정하는
표준화된 방법을 제공한다. 

```Http```와는 다른 ```TCP Protocol```이지만, 포트 ```80```, ```443```을 사용하고
기존 방화벽 규칙을 재사용할 수 있도록 ```Http```를 통해 작동하도록 설계되었다.

![image](https://user-images.githubusercontent.com/87312401/144804245-35fe5fee-889c-4af6-ab1a-4b3495db5c6d.png)

```Upgrade```헤더와 ```Upgrade``` Connection을 사용해서 ```Http```요청을 ```WebSocket```으로 전환시킨다.


![image](https://user-images.githubusercontent.com/87312401/144804393-6b79c90b-7bc9-44ef-b3e1-bb7aa018cdd7.png)

```200``` response 대신 ```101``` response를 던져준다. 이제부터 ```TCP Protocol```이 열려 클라이언트 - 서버간에 메시지가 전달될 수 있도록 열린 상태를
유지한다.

깊게 작동원리를 확인할 필요가 있다면 [링크](https://datatracker.ietf.org/doc/html/rfc6455) 를 확인하자

> WebSocket 서버를 웹 서버에서 사용한다면 , Upgrade 요청이 잘 전달되도록 세팅해줘야한다.


## HTTP Versus WebSocket
```WebSocket```이 ```Http``` 요청으로부터 시작하는 것은 맞지만, 두 프로토콜은 완전히 다르다.
 
* HTTP : ```REST```에서 ```HTTP```는 많은 ```URL```로 이뤄져있다. 애플리케이션과 상호작용하기 위해 클라이언트는 ```URL```, ```Request-Response```스타일에 맞게 요청을 보내야하고 서버는 전달받은 ```HTTP URL, Method, headers```에 맞게 적절한 핸들러를 호출한다.
* WebSocket : 최초 연결 시 단 하나의 ```URL```만 사용된다. 그리고 이 ```TCP Protocol```내에서 메시지가 전달된다.

## When to use WebSockets
대부분의 경우 ```Ajax```나 스트리밍, 폴링 조합을 사용하는게 효과적인 솔루션을 제공할 수 있다.
하지만 ```Collaboration, games, and financial apps``` 과 같은 경우에는 ```real-time```에 가까워야 하기 때문에, 이 때 사용하면 좋다.

단순히 대기시간만으로 ```WebSocket```사용을 결정해서는 안된다. 적은 대기시간, 높은 사용 빈도, 많은 양이 함께 수반될 때 ```WebSocket``` 사용이 최고의 선택이다.

## WebSocket Practice

먼저 ```websocket```라이브러리를 받아온다.
```groovy
implementation 'org.springframework.boot:spring-boot-starter-websocket'
```

### WebSocketHandler
> WebSocketHandler을 상속받으면서 WebSocket Server를 만들 수 있다.
> 예시에서는 TextWebSocketHandler를 이용하여 WebSocket을 만들어준다.

```java
@Slf4j
public class MyHandler extends TextWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("afterConnectionEstablished : {} ", session.toString()); // 연결 수립된 직후 행위
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println(message); // 웹소켓 연결 수립 후 해당 연결을 이용하는 세션과 메시지
        System.out.println(message.getPayload()); 

    }
}
```

### WebSocketConfig
> Handler 의존성을 주입하고, 엔드포인트를 등록시켜준다.

```java
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myHandler(), "/myHandler"); // 엔드포인트에 등록
    }

    @Bean
    public WebSocketHandler myHandler(){
        return new MyHandler();
    }
}
```

## PostMan test

#### 먼저 PostMan을 이용하여 WebSocket에 연결해보자

![image](https://user-images.githubusercontent.com/87312401/144809528-26a8123d-0c50-48a3-8af8-3113c8fc440b.png)
> 먼저 Home -> Create New -> WebSocket Request 를 클릭한다.

![image](https://user-images.githubusercontent.com/87312401/144809655-6ac1789d-fa36-49da-86c5-04be28cedf74.png)
> 엔드포인트 주소를 입력하고 연결해준다. 아래와 같이 상태가 변경되고 메시지가 입력된다. 

![image](https://user-images.githubusercontent.com/87312401/144809733-e6da84af-a55d-4dbb-8f4d-5cd1416dd379.png)

> 로그 메시지도 잘 나오는 것을 볼 수 있다. (afterConnectionEstablished)

![image](https://user-images.githubusercontent.com/87312401/144809845-087af1db-d679-48ab-a8c0-b85a997bad83.png)

#### 이제 메시지를 하나 보내보자

![image](https://user-images.githubusercontent.com/87312401/144809944-ffd6544c-8724-49ce-ae9d-97b7a1abeebe.png)
> 메시지를 입력하고 Send 버튼을 누르면 메시지가 보내진 것을 확인할 수 있다.

![image](https://user-images.githubusercontent.com/87312401/144810053-a47dba7d-98d4-42ee-a6b5-702681fbc7e9.png)
> 로그에도 내용이 잘 전달된 것을 확인할 수 있다.

이로써 ```client - server``` 간 ```WebSocket``` 연결이 성공적으로 된 것을 확인할 수 있다.

## 마무리
앞의 예제는 ```Spring MVC``` 애플리케이션에서 사용하기 위한 것으로 ```DispatcherServlet```의 구성에 포함되어야 한다. 그러나 ```Spring```의 ```WebSocket``` 지원은 ```Spring MVC```에 의존하지 않는다.

웹 환경에서 사용하기 위해서는 뭔가 추가적인 요구사항이 필요한 것 같다. 다음 포스팅에 추가 작성해보겠다..

참고 : https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#websocket  
https://tecoble.techcourse.co.kr/post/2021-08-14-web-socket/
