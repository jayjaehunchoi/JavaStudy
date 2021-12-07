# Logging

> [링크](https://goddaehee.tistory.com/206) 를 참고하여 로깅에 대해 알아보자.


## Logback
### Logback이란?
- Java 오픈소스 로깅 프레임워크로, ```Slf4j```의 구현체이다.
- ```spring-boot-starter-web```에 ```spring-boot-starter-logging``` 으로 구현체가 있어, 따로 라이브러리를 추가하지 않아도 된다.  
  ![image](https://user-images.githubusercontent.com/87312401/144972523-d8bffc8f-0d3c-48d4-b756-a46484f03e82.png)
- Logback을 이용하여 로깅을 수행하기 위해 필요한 주요 설정 요소로는 ```Logger``` ```Appender``` ```encoder``` 3가지가 있다.
  - 참고 : [Architecture](http://logback.qos.ch/manual/architecture.html#LoggerContext)

### Spring boot logback 설정
> logback-spring.xml 파일을 resources 디렉토리에 만들어 참조한다. 스프링부트에서는
> 위 파일을 사용해서 설정한 profile 별로 logback을 구동할 수 있게 해준다.

> ex)  
> spring.profiles.active=local  
logback-local.properties => console-logging  
logback-dev.properties => file-logging  
logback-prod.properties => file-logging,remote-logging


### log level

![image](https://user-images.githubusercontent.com/87312401/144973769-11fa445e-6073-4c93-a9ec-2c4c6c7e2c94.png)

> ERROR -> TRACE 순으로 에러 -> 가장 상세한 수준 으로 정리된다.

- ERROR : 요청을 처리하는 중 오류가 발생한 경우 (개발자가 예상치 못한 에러)
- WARN : 처리 가능한 문제 시스템 에러의 원인이 될 수 있는 경고성 메시지
- INFO : 상태변경과 같은 정보성 로그 표시
- DEBUG : 프로그램 디버깅 위한 정보 표시
- TRACE : DEBUG보다 훨씬 상세한 정보

로그 레벨의 특징은 로그 레벨을 설정하여 설정한 로그 레벨보다 낮은 레벨은 무시하는 것이다.

#### 예시
```properties
logging.level.root=info # root는 info 레벨부터 로깅 남김
logging.level.chat.socket.config=debug # config 패키지는 debug 레벨 부터 로깅 남김 (상세 내용이기에 우선순위가 root보다 앞)
```

> config 패키지에서는 debug 레벨 log가 남는다.

![image](https://user-images.githubusercontent.com/87312401/144974532-cc2fc15a-0075-4d14-91fa-273dff2a9bc8.png)


> 다른 패키지에서는 config와 debug 모두 선언해도 info만 log가 남는다

```java
log.debug("message 패키지에서 디버그");
log.info("message 패키지에서 인포");
```

![image](https://user-images.githubusercontent.com/87312401/144974735-3a0a0985-ff29-49de-9a59-0966b66109ec.png)


### logback-spring.xml
위에서 말했다시피 해당 파일을 통해 로깅을 커스텀할 수있다.
- 어디에 출력할지
- 어느 레벨로 출력할지
- 환경별로 달리 설정할지

#### 1. appender
> log의 형태를 성정, 로그 메시지가 출력될 대상을 결정

1. ```ch.qos.logback.core.ConsoleAppender``` : 로그를 ```OutputStream```에 작성하여 콘솔에 출력시킴
2. ```ch.qos.logback.core.FileAppender``` : 파일에 로그를 찍어 최대 보관일, 최대 보관 용량 등을 커스텀할 수 있음
3. ```ch.qos.logback.core.rolling.RollingFileAppender``` : 여러개의 파일을 롤링하며 로그를 찍는다. 지정 용량이 넘어간 로그 파일을 넘버링하여 나눠서 저장할 수 있다.
4. ```ch.qos.logback.classic.net.SMTPAppender``` : 로그를 메일로 보낸다.
5. ```ch.qos.logback.classic.db.DBAppender``` : 로그를 디비에 남긴다.

#### 2. root, logger
> 설정한 appender를 참조하여 package와 level을 설정한다.

- root
  - 전역 설정
  - 지역적으로 설정된 ```logger```가 있다면 해당 ```logger``` 내용은 default로 적용
- logger
  - 지역 설정
  - additivity 값은 root설정 상속 유무 설정

#### 3. property
> 설정 파일에서 사용될 변수값 설정

#### 4. layout, encoder
> log4j에서는 layout을 많이 사용함, appender 사용을 위해서는 encoder 사용이 필수

#### 5. pattern

`````%Logger{length}````` : Logger name을 축약할 수 있다. {length}는 최대 자리 수, ex)logger{35}   
```%-5level``` : 로그 레벨, -5는 출력의 고정폭 값(5글자)  
```%msg``` : - 로그 메시지 (=%message)  
```${PID:-}``` : 프로세스 아이디  
```%d``` : 로그 기록시간  
```%p``` : 로깅 레벨  
```%F``` : 로깅이 발생한 프로그램 파일명  
```%M``` : 로깅일 발생한 메소드의 명  
```%l``` : 로깅이 발생한 호출지의 정보  
```%L``` : 로깅이 발생한 호출지의 라인 수  
`````%thread````` : 현재 Thread 명  
```%t``` : 로깅이 발생한 Thread 명  
```%c``` : 로깅이 발생한 카테고리  
```%C``` : 로깅이 발생한 클래스 명  
```%m``` : 로그 메시지  
```%n``` : 줄바꿈(new line)  
```%%``` : %를 출력  
```%r``` : 애플리케이션 시작 이후부터 로깅이 발생한 시점까지의 시간(ms)  


### logback xml 작성
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- 60초마다 설정 파일의 변경을 확인 하여 변경시 갱신 -->
<configuration scan="true" scanPeriod="60 seconds">
    <!--springProfile 태그를 사용하면 logback 설정파일에서 복수개의 프로파일을 설정할 수 있다.-->
    <springProfile name="local">
        <property resource="logback-local.properties"/>
    </springProfile>
    <springProfile name="dev">
        <property resource="logback-dev.properties"/>
    </springProfile>
    <!--Environment 내의 프로퍼티들을 개별적으로 설정할 수도 있다.-->
    <springProperty scope="context" name="LOG_LEVEL" source="logging.level.root"/>
    <!-- log file path -->
    <property name="LOG_PATH" value="${log.config.path}"/>
    <!-- log file name -->
    <property name="LOG_FILE_NAME" value="${log.config.filename}"/>
    <!-- err log file name -->
    <property name="ERR_LOG_FILE_NAME" value="err_log"/>
    <!-- pattern -->
    <property name="LOG_PATTERN" value="%d{yy-MM-dd HH:mm:ss} %highlight(%-5level) [%thread] %cyan([%logger{0}:%line]) - %msg%n"/>
    <!-- Console Appender -->
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <!-- color -->
        <withJansi>true</withJansi>
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <pattern>${LOG_PATTERN}</pattern>
        </encoder>
    </appender>
    <!-- File Appender -->
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!-- 파일경로 설정 -->
        <file>${LOG_PATH}/${LOG_FILE_NAME}.log</file>
        <!-- 출력패턴 설정-->
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <pattern>${LOG_PATTERN}</pattern>
        </encoder>
        <!-- Rolling 정책 -->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!-- .gz,.zip 등을 넣으면 자동 일자별 로그파일 압축 -->
            <fileNamePattern>${LOG_PATH}/${LOG_FILE_NAME}.%d{yyyy-MM-dd}_%i.log</fileNamePattern>
            <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <!-- 파일당 최고 용량 kb, mb, gb -->
                <maxFileSize>10MB</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
            <!-- 일자별 로그파일 최대 보관주기(~일), 해당 설정일 이상된 파일은 자동으로 제거-->
            <maxHistory>30</maxHistory>
            <!--<MinIndex>1</MinIndex> <MaxIndex>10</MaxIndex>-->
        </rollingPolicy>
    </appender> <!-- 에러의 경우 파일에 로그 처리 -->
    <appender name="Error" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>error</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
        <file>${LOG_PATH}/${ERR_LOG_FILE_NAME}.log</file>
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <pattern>${LOG_PATTERN}</pattern>
        </encoder>
        <!-- Rolling 정책 -->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!-- .gz,.zip 등을 넣으면 자동 일자별 로그파일 압축 -->
            <fileNamePattern>${LOG_PATH}/${ERR_LOG_FILE_NAME}.%d{yyyy-MM-dd}_%i.log</fileNamePattern>
            <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <!-- 파일당 최고 용량 kb, mb, gb -->
                <maxFileSize>10MB</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
            <!-- 일자별 로그파일 최대 보관주기(~일), 해당 설정일 이상된 파일은 자동으로 제거-->
            <maxHistory>60</maxHistory>
        </rollingPolicy>
    </appender>
    <!-- root레벨 설정 -->
    <root level="${LOG_LEVEL}">
        <appender-ref ref="CONSOLE"/>
        <appender-ref ref="FILE"/>
        <appender-ref ref="Error"/>
    </root>
    <!-- 특정패키지 로깅레벨 설정 -->
    <logger name="org.apache.ibatis" level="DEBUG" additivity="false">
        <appender-ref ref="CONSOLE"/>
        <appender-ref ref="FILE"/>
        <appender-ref ref="Error"/>
    </logger>
</configuration>
```

