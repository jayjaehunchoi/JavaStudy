# Spring Cloud Config

프로그램마다 설정파일(application.yml)이 존재한다. 당연히 분산시스템에서도 그렇다.
```MSA```를 구성할 때, 하나의 Micro Service마다 설정 파일을 셋팅하다 보면 같은 설정 내용이 반복되는 순간이 있다.
```dev```, ```prod```, ```test```, ```local``` 등 다양한 설정 파일이 존재한다고 가정했을 때, 반복되는 설정을 매번 작성하는 것은 비효율이다.

```Spring Cloud Config```는 이 문제를 해결해준다. ```Config```서버를 구축하여 설정을 변경할 수 있고, 매번 Micro Service를 재기동하지 않아도 Config Server만 재기동하여 설정을 모두 변경할 수 있다.

![image](https://user-images.githubusercontent.com/87312401/146754076-52fee150-b6e0-44d6-a2da-fdfaa8842c28.png)

위 사진과 같이 ```git repository```에 존재하는 ```yml``` 파일을 ```Config Sever```에서 참조하고, 각 Micro Service는 ```Config Server```의 설정 파일을 참조하여 해당 설정 내용을 가져온다.


## Config Server

> Config Server를 직접 만들어보자.

먼저 폴더를 하나 생성하고 ```Local```에 Git Repository를 만든다. 
그리고 아래와 같이 원하는 설정 내용을 ```yaml``` 파일에 넣고 ```commit```을 날린다.

![image](https://user-images.githubusercontent.com/87312401/146757039-6e8c5e81-8c15-4d3d-b461-d150988594b4.png)

이제 설정 파일을 받아올 수 있는 ```Repository```를 생성하였으니, 이를 불러올 서버를 하나 생성한다.


Spring Initializer를 켜고, Dependency에 ```Config Server``` 를 받아온다.
![image](https://user-images.githubusercontent.com/87312401/146754710-0c1b8a93-2f47-468c-b83d-8c4754bfaf2e.png)


Spring application이 실행되면서 Config Server가 실행될 수 있도록 ```@EnableConfigServer``` 애노테이션을 추가한다.
![image](https://user-images.githubusercontent.com/87312401/146755036-fee74df8-79e3-484c-a2ad-13b3098ab4ee.png)


그리고 ```application.yml``` 을 수정하여, 8888포트 (default), server git 디렉토리를 선언한다.

```yml
server:
  port: 8888

spring:
  application:
    name: config-service
  cloud:
    config:
      server:
        git:
          uri: file:///Users/JaehunChoi/Desktop/2021/Developer/git-local-repo
```

이제 ```Config server```를 실행하고, default 설정을확인해보자.

![image](https://user-images.githubusercontent.com/87312401/146756222-5d7ffb6d-caec-43ac-ba34-8eb172e90c4f.png)

설정 내용이 잘 반영된 것을 확인할 수 있다.

그리고 해당 설정을 반영할 ```Micro Service```에서 config 내용을 받아올 수 있도록 세팅한다.

먼저, dependency에 Config와 cloud bootstrap을 추가한다.
```groovy
implementation 'org.springframework.cloud:spring-cloud-starter-config'
implementation 'org.springframework.cloud:spring-cloud-starter-bootstrap'
```

그리고 application.yml보다 더 먼저 설정 내용을 읽어오도록 ```bootstrap.yml``` 파일을 생성한다.

```yml
spring:
  cloud:
    config:
      uri: http://127.0.0.1:8888
      name: ecommerce
```

uri에 ```Config Server```주소를, name에 읽어 올```yaml``` 파일의 이름을 작성한다.
그리고 Controller에 관련 내용을 추가하여 콘솔에 찍어보자.

아래와 같이 원하는 내용이 잘 찍힌 것을 확인할 수 있다.
![image](https://user-images.githubusercontent.com/87312401/146756779-3d1d5344-0ff2-4d19-9cda-9e7dd071b47b.png)
