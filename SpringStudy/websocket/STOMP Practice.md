# STOMP
> Spring Reference의 STOMP를 보고 정리한다.

## OverView
```STOMP``` (Simple Text Oriented Messaging Protocol)은 스크립트 언어에서 엔터프라이즈용 메시지 브로커에 연결하기위해 만들어진 프로토콜이다.

```STOMP```는 ```TCP```, ```WebSocket``` 같은 신뢰성이 보장된 양방향 네트워크에서 사용이 가능하고, ```text-oriented protocol```이지만, message payload는 ```binary```형태여도 된다.

```STOMP```는 ```Http```에서 모델링되는 프레임 기반 프로토콜이다.

### subscribe
> topic/greetings 인사할 수 있는 곳을 구독하여 해당 url을 구독한 사람들끼리 메시지를 주고받을 수 있다.

![image](https://user-images.githubusercontent.com/87312401/144839913-2bf0818a-0638-4a44-bbc0-10a57e314e62.png)

### send
> MessageMapping된 Controller에 JSON으로 데이터를 보내고, 이는 ```DestinationPrefix```로 선언된 ```/app```과 합쳐져 메시지가 잘 전달되게끔 세팅되어있다.
>> -> SEND는 MessageBrokerConfigurer에서 선언된 ```DestinationPrefix``` + 수행하고픈 ```Controller``` 로보낸다.
>>
>> -> MESSAGE는 전달받은 Controller에서 ```SimpMessagingTemplate```, ```@SendTo```에 선언된 subscribe url 타고 해당 구독 방에 메시지를 전달한다. 

![image](https://user-images.githubusercontent.com/87312401/144840036-a4365020-eb40-43f2-92b0-db8e3a88f288.png)

위에서 보이듯, Client는 ```Send``` ```Subscribe``` 명령어를 통해 특정 주소를 ```subscribe```하고, ```subscribe```한 모든 이들에게 message를 ```Send```한다.

```destination```은 자유롭게 설정할 수 있지만 일반적으로 ```/topic/..``` 형태가 ```1:N``` , ```/queue/..``` 형태가 ```1:1``` 공간으로 사용된다고 한다.

## Benefits
```Spring Framework```에서 `STOMP```를 사용하는 것이 순수 ```WebSocket```을 사용하는 것보다 훨씬 풍부한 프로그래밍 모델을 제공한다.

* 메시지 프로토콜과 메시지 포맷을 커스터마이징할 필요가 없다.
* Java 클라이언트를 포함한 STOMP 클라이언트 사용 가능
* RabbitMQ나 ActiveMQ같은 메시지 브로커를 통해 구독과 메시지 브로드캐스팅을 관리할 수 있다.
* STOMP 헤더를 기반으로 애플리케이션 로직을 Controller에서 정리하고 라우팅할 수 있다.
* Spring Security를 사용할 수 있다.

## Message Flow

![image](https://user-images.githubusercontent.com/87312401/144842578-ca2cddb9-d0b4-4739-9aa7-36a8a0f04cd6.png)

1. ```WebSocket``` 사용자가 메시지를 보낸다.
2. ```ClientInboundChannel```을 타고, ```simpleBroker```인지, ```DestinationPrefix```인지 확인하여 각각 매핑해준다. (STOMP -> Spring message)
3. ```SimpleBroker```의 경우 바로 ```ClientOutboundChannel```을 타고 구독한 사용자에게 메시지를 전달한다.
4. ```SimpAnnotationMethod```의 경우 ```Controller```의 로직을 수행하고 ```brokerChannel```을 탄 뒤, ```MessageBroker```로 전달된다.

## 예제
> Getting Started WebSocket을 약간 변경하여 예제로 사용해보자.

### build.gradle
```groovy
// Jqurey, bootstrap 사용과 spring boot 환경, websocket, sockJS를 사용함 
dependencies {
	implementation 'org.webjars:webjars-locator-core'
	implementation 'org.webjars:sockjs-client:1.0.2'
	implementation 'org.webjars:stomp-websocket:2.3.3'
	implementation 'org.webjars:bootstrap:3.3.7'
	implementation 'org.webjars:jquery:3.1.1-1'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	implementation 'org.springframework.boot:spring-boot-starter-websocket'
	compileOnly 'org.projectlombok:lombok'
	annotationProcessor 'org.projectlombok:lombok'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```
> enabling SockJS fallback options so that alternate transports can be used if WebSocket is not available. The SockJS client will attempt to connect to /gs-guide-websocket and use the best available transport (websocket, xhr-streaming, xhr-polling, and so on).
> > SockJS 클라이언트는 등록된 endpoint에 연결하여 websocket, streaming, polling 중 최상의 선택을 해준다고 함.

### MessageDto
```java
@Getter
@NoArgsConstructor
public class MessageDto {
    private String name;
    private String content;

    public MessageDto(String name, String content) {
        this.name = name;
        this.content = content;
    }
}
```
JSON 타입으로 ```STOMP```프로토콜을 타고 들어올 메시지를 객체로 전환하기 위해 사용

### WebSocketConfig
```java
@Configuration
@EnableWebSocketMessageBroker // WebSocketMessageBroker를 통해 message를 핸들링 해줌
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // SockJS로 endPoint에 접근할 수 있음
        registry.addEndpoint("/gs-guide-websocket").withSockJS(); 
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // simple Broker -> message broker 작동 -> out bound -> 구독자에게 전달
        registry.enableSimpleBroker("/topic");
        
        // /app/..을 타고 들어와 @MessageMapping("/..") 위치의 로직을 수행하고, Message Broker를 타게끔 세팅 
        registry.setApplicationDestinationPrefixes("/app"); 
    }
}
```

### GreetingController

```java
@Controller
public class GreetingController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public MessageDto greeting(MessageDto messageDto) throws Exception{
        Thread.sleep(500);
        return messageDto;
    }
}
```
```/app/hello```를 타고 들어온 message를 500ms 쉬었다가 ```/topic/greetings```를 구독하는 사용자에게 ```messageDto```를 전달해준다.
아래와 같이 변경해서 사용할 수도 있다.

```java
@RequiredArgsConstructor
@Controller
public class GreetingController {

    private final SimpMessagingTemplate template;

    @MessageMapping("/hello")
    public void greeting(MessageDto messageDto) throws Exception{
        Thread.sleep(500);
        template.convertAndSend("/topic/greetings", messageDto);
    }
}
```
두 번째처럼 ```SimpMessagingTemplate```을 사용하면 구독 주소를 동적으로 커스터마이징해서 사용할 수 있어 채팅방 구현할 때는 두 번째 방법이 편할 듯 하다.

### 프론트 html, JS는 [링크](https://spring.io/guides/gs/messaging-stomp-websocket/) 참고
> 이 부분은 링크 내용 복붙하고 서버에 맞게 조금 수정했음 
### app.js

#### connect()
```javascript
function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/greetings', function (greeting) {
            showGreeting(JSON.parse(greeting.body).name, JSON.parse(greeting.body).content);
        });
    });
}
```
```SockJS```를 이용하여 서버에서 선언한 ```STOMP```엔드 포인트에 접근한다.
이때, ```stompClient.subscribe```에 구독할 주소를 입력한다.
만약 실제로 채팅방을 구현한다면 ```규정한 구독 prefix + api + {number}``` 정도의 ```url```을 구독하면 된다.

아래와 같이 연결을 끊어줄 수도 있다.
```javascript
function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}
```

#### sendMessage()
```javascript

function sendMessage() {
    stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val() , 'content': $("#content").val()}));
}
```

```stompClient.send```의 파라미터를 보면 ```/app/hello```로 ```JSON```을 전송한다.
서버 ```Controller```의 ```@MessageMapping("/hello")``` 애노테이션을 타고 로직을 수행한뒤, ```MessageBroker```를 타고 구독한 인원들에게 메시지가 전달된다.


정상적으로 코드를 작성한뒤, 서버를 실행하여 ```localhost:8080/index.html``` 로 접속하여 메시지를 보내면 아래처럼 메시지를 주고받는 모습을 확인할 수 있다.  

![image](https://user-images.githubusercontent.com/87312401/144847447-39853d8b-9701-4b67-9b16-d13c48633361.png)
![image](https://user-images.githubusercontent.com/87312401/144847536-919d77a8-ffd6-42e5-afbf-7895f5f1a9ce.png)


#### 참고 
https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#websocket-stomp-overview  
https://spring.io/guides/gs/messaging-stomp-websocket/
