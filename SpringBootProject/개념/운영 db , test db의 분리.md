# 운영 DB와 Test DB의 분리
우여곡절 끝에 EC2에 배포를 마쳤다. 그런데 변동사항이 있어 ```api``` 서버를 수정하였고 다시 서버를 build했다. 그런데 그때, build 단계에서 ```test```오류가 발생하는 것이었다. 오류가 뜨는 것을 가만 보니,, 운영 db에 저장되어 있는 내용으로 인해 test가 방해되고 있는 것이었다. 문제를 발견한 순간 운영 db, test db와 분리를 시도하였다.

### 1. test directory에 resources값 주기
처음에는 단순하게, test directory에 로컬에서 사용하던 ```application.properties```를 넣으면 되겠지?라고 생각하였다. 그래서 다음과 같이 파일을 추가하였다.
```properties
spring.datasource.url=jdbc:h2:tcp://localhost/~/palette
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.jpa.hibernate.ddl-auto=create
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.profiles.include=aws
spring.jpa.properties.hibernate.default_batch_fetch_size=1000 
```
당연히 ```local```에서는 ```test```가 모두 통과했고, ```github```에 push 한 뒤, 다시 build를 시작했다.
그런데,, 알 수 없는 에러들이 뜨기 시작했다.
![image](https://user-images.githubusercontent.com/87312401/141483877-34db8008-6e05-46ab-95c6-1058b5c7bf7f.png)
모든 테스트가 다 실패하는 것이었다. ```--stacktrace``` 를 찍어봐도 더이상 에러코드가 나오지 않아 에러 내용을 하나씩 보기 시작했다. 

**지켜본 결과**
1. test database 설정에 문제가 있다. java compile까지는 잘 통과했기 때문이었고, ```HibernateException```이 떴다.
2. Bean 생성에 문제가 있다. 이 또한 test db 때문이라고 판단했다.

하지만 로컬에서는 잘 돌아갔다. 그 이유를 이미 아는 사람도 있겠지만, 나는 꽤나 오랜시간 삽질을 하고 발견했다.
```properties
#localhost가 보이는가?
spring.datasource.url=jdbc:h2:tcp://localhost/~/palette 
```
```ec2```에 배포해놓고, ```local```의 h2만 주구장창 찾고 있으니 당연히 에러가 날 수 밖에, 그래서 다시 제대로 ```test```와 운영 db를 분리하기로 하였다. 그 key는 바로 ```@ActiveProfiles("test")``` 이다.

### 2. @ActiveProfiles("test")
이 문제를 해결하기 위해서 ```github```를 돌아다니며 배포를 한 많은 프로젝트를 살펴보았다. 대부분 ```resources```디렉토리에 ```application-test.properties```파일을 갖고 있더라. 일반적으로 설정 파일은 ```application-{내용}.properties```로 사용하고, ```test```같은 경우는 test 코드 클래스 상단에 ```@ActiveProfiles("test")```를 달아서, 해당 설정파일을 기반으로 test를 돌린다.

### test 돌릴때 다음과 같이 실행되면 성공이다.
![image](https://user-images.githubusercontent.com/87312401/141484955-ddbabf9e-b729-45ec-986e-c0f078180f64.png)
이제 설정 파일에 대해 조금 더 살펴보자.

### 3. application-test.properties
> 이 부분은 설명보다는 코드위에 주석으로 달아보겠다.
```properties
spring.datasource.url=jdbc:h2:mem:test  # h2에 test용 db 생성
spring.datasource.driver-class-name=org.h2.Driver # h2 db 사용  
spring.datasource.name=sa  
spring.datasource.password=  
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect  # H2 방언 사용
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect  # H2 방언 사용  

# 기본 설정, 아직 테스트를 마치지 않아 show sql 관련 내용이 있지만
# test 마치고 정식버전 release 되면 sql 콘솔 출력 관련 내용은 모두 제거 예정이다.
spring.jpa.hibernate.ddl-auto=create  
spring.jpa.show-sql=true  
spring.jpa.properties.hibernate.format_sql=true  
spring.jpa.properties.hibernate.default_batch_fetch_size=1000
```
이렇게 ec2 환경에서도 build가 잘될 수 있게 setting 을 해준다.
그리고 다시 배포를 해주면?
![image](https://user-images.githubusercontent.com/87312401/141485571-1e97c5dc-a901-49d6-bceb-dfcdf2eaf0a2.png)  

성공적으로 build가 완료되고, 다시 배포 상태가 유지된다~!

### EC2에 배포하면 , 운영 db와 test db분리가 필수이다. 또 운영 db에서 기존 local에서 사용하던 설정 (generate-ddl, ddl-auto)을 바꾸고 배포해야 한다는 것을 꼭 기억하자!

