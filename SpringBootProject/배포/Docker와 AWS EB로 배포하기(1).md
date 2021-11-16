# Docker와 AWS EB로 배포하기(1)
## 포스트 작성 계기
이번 프로젝트 중 ```EC2```로 백엔드 서버 배포를 마치고, 프론트 서버를 어떻게 배포하면 좋을까 고민이 생겼다. 동일한 서버에서 프론트, 백엔드 환경을 동시에 배포할 수 없을까라는 생각에 ```Docker```를 떠올렸고 이제 막 공부를 시작했다. 

또, 도커를 이용해서 동일한 환경에 배포할 때는 ```Elastic Beanstalk```를 많이 추천하기에, EC2에 배포한 백엔드 서버는 프론트엔드  팀원을 위한 테스트 서버로 두고, 실제 운영 서버는 Elastic Beanstalk에 ```nginx```, ```backend```, ```frontend``` 컨테이너를 동작시켜 구동시킬 예정이다.

목표 기한이 며칠 남지 않았기에 가능할지는 모르지만 도전해보려 한다.

### 1. EB란?
AWS Elastic Beanstalk의 약자로 Apach, Nginx같은 웹서버에서 Java, Docker 등과 함께 개발된 웹 응용 프로그램, 서비스를 배포하고 확장하기 쉬운 서비스입니다.

> 아래 도표와 같이 EC2 인스턴스, 데이터베이스 등을 포함한 환경을 자동으로 관리해줌.
![image](https://user-images.githubusercontent.com/87312401/141946312-266b1797-b4c7-4ce5-8039-e5224085451a.png)

### 2. EB 환경 세팅
#### 1. Elastic Beanstalk create
![image](https://user-images.githubusercontent.com/87312401/141944931-174a37f9-6c85-480f-867c-42528c6af268.png)
> 먼저 AWS 에 들어가 Elastic Beanstalk를 검색한 뒤 ```create Application``` 버튼을 클릭한다.

#### 2.  Elastic Beanstalk 관리 콘솔
![image](https://user-images.githubusercontent.com/87312401/141946663-9885f2b8-43e8-4d59-a194-2a283e6c92d7.png)
> 본인이 생성하고자 하는 애플리케이션의 이름으로 설정한다. 나는 이번 프로젝트의 이름인 Palette로 설정하였다.  

![image](https://user-images.githubusercontent.com/87312401/141946802-f7fbb7aa-2631-4447-a461-ce5a70c3b9d1.png)
> Docker를 이용하여 하나의 EB 환경에 frontend, backend 컨테이너를 올려 배포를 진행할 예정이다. 더하여 도커 컨테이너 80번 포트로 Nginx를 두어 reverse-proxy를 만들어 로드 밸런서 역할을 수행시킨다.

이후 환경 생성 버튼을 클릭하면, 다음과 같은 화면이 나오고 환경이 생성된다.
![image](https://user-images.githubusercontent.com/87312401/141947896-2fdbe054-50aa-42ac-8b72-d6bd211dbf24.png)


#### 번외 : VPC와 보안그룹
1. VPC

이렇게 하나의 EB인스턴스가 생성되면, AWS에서 ```RDS```를 이용해 데이터베이스도 열어줘야 한다(서버와 db 연결을 위해). 하지만 아무 설정 없이 EB환경과 RDS 환경이 통신할 수 있을까? 당연히 불가능하다. 

VPC는 고객이 정의하는 가상 네트워크에서 AWS 리소스를 시작할 수 있게 해준다. 인스턴스를 개인 아이디에서만 접근이 가능하게 논리적으로 격리된 네트워크에서 생성되게 해준다. 다른 아이디로는 접근이 불가하고 같은 아이디이더라도 지역이 달라져도 접근이 불가능하다.
> EB인스턴스를 생성하면 VPC가 현재 region기반으로 하나 생성된다.

2. 보안그룹

```Inbound``` : 외부에서 EC2 혹은 EB로 요청을 보내는 트래픽. HTTP, HTTPS, SSH가 있고 접근하는 포트, IP가 열려있어야 원활하게 서버로 접근이 가능하다.
```Outbound``` : EC2, EB 등에서 바깥으로 나가는 경우를 outbound라고 한다.

> 이제 RDS를 만들어 EB와 RDS를 동일한 VPC 환경에 두고, 같은 Security Group으로 관리하여 EB, RDS간 원활한 통신이 이뤄질 수 있게끔 세팅할 예정이다.

#### 3. RDS 생성
> AWS 에서 RDS를 검색한 후 데이터 베이스 생성버튼을 누르면 생성과정이 시작된다.
> 
![image](https://user-images.githubusercontent.com/87312401/141951662-6557102c-1a37-4aea-9aa1-011c02e17942.png)
> Mysql을 사용할 예정이기 때문에 Mysql을 선택하고 내 Mysql과 같은 버전으로 선택한다.

![image](https://user-images.githubusercontent.com/87312401/141951781-5b5db034-f743-4905-9b8d-cdefed1303d4.png)
> 현재 프리티어 계정으로 사용 중이기도 하고, 비용이슈때문에 프리 티어를 선택해준다.

![image](https://user-images.githubusercontent.com/87312401/141951989-2e0ccad1-71ac-4883-b7a5-c3d675e1a05e.png)
> 1번 : AWS RDS 대시보드에 생성될 데이터베이스 이름이다.
> 2번 : 추후  MySQL에서 RDS 접근할 때, 사용할 username
> 3번 : 추후 MySQL에서 RDS 접근할 떄, 사용할 password 

![image](https://user-images.githubusercontent.com/87312401/141953225-5da4f3e9-312b-4b80-8593-be296a00a6f8.png)
> 기본 VPC를 사용한다. 퍼블릭 액세스를 먼저 열어두고, 추후 보안그룹을 통해 인바운드 제한을 둘 예정이다.
> 
![image](https://user-images.githubusercontent.com/87312401/141956995-12cf4c0c-993b-4ef4-9b07-9576fc42b2b4.png)
> 추가 구성을 누르고 초기 데이터베이스 이름을 세팅해준다.
> 
![image](https://user-images.githubusercontent.com/87312401/141957149-4a1558b8-b05c-450b-ba59-c2543bc5d6b3.png)
> 위 사진과 같이 대시보드에 DB가 생성되면 성공적으로 RDS를 만든 것이다.

하지만 지금처럼 퍼블릭액세스를 열어둔 상태로 RDS를 사용하면 보안상 정말 위험하다. 보안그룹에 EB 인스턴스를 추가하여 접근의 제한을 둬야한다.

#### 4. 보안그룹 생성 및 적용 
> VPC에 접속하여 보안그룹 생성 버튼을 누른다.
> 
![image](https://user-images.githubusercontent.com/87312401/141958350-c88881d5-a5d1-4def-b45c-34c5af6a3954.png)
> 보안그룹을 생성한 뒤 , 인바운드 편집을 해서 MySQL 포트를 열어준다.

![image](https://user-images.githubusercontent.com/87312401/141958488-26721712-0e14-48cb-827c-739ed75cd806.png)
> 사용중인 MySQL 포트인 3306 포트를 입력하고 소스에 방금 만든 보안그룹을 추가해준다. 설명은 선택사항이다.

#### 이제 생성한 보안그룹을 적용해보자.
> 만든 EB 환경에 들어가 구성 > 인스턴스의 편집버튼을 누른다.

![image](https://user-images.githubusercontent.com/87312401/141959086-82f0bfd7-e7a7-4494-bb68-727d8f5aadf0.png)
> 방금 위에서 생성한 보안그룹을 추가해준다. 체크 후 적용 , 경고가 나오는데 무시하고 확인버튼 눌러주면 된다. 이어서 RDS에도 생성한 보안그룹을 등록해주자. RDS > DB 선택 > 수정

![image](https://user-images.githubusercontent.com/87312401/141959457-8932e410-f0a0-4e67-91d8-a241bf83d151.png)
> 방금 생성한 보안그룹을 넣어주고, 수정, 즉시적용을 선택해 바로 RDS 보안그룹이 수정될 수 있도록 세팅한다.
> 이제 보안그룹까지 모두 세팅을 완료했다. 하지만 EB의 컨테이너들이 아직 MySQL 환경변수를 인식하지 못하는 상태이다. EB에 MySQL 환경변수를 추가해보자.

#### 5. 환경 변수 설정
> 만든 EB에 들어가 구성 > 소프트웨어의 편집 버튼을 눌러준다.
> 
![image](https://user-images.githubusercontent.com/87312401/141960294-f8dd656f-f61f-4181-91c9-751345530bbc.png)

> 이름과 값을 RDS 설정에 맞게 채워준다.
> 
![image](https://user-images.githubusercontent.com/87312401/141960696-ebaa0ee0-805a-4e0e-8a81-430208016ed2.png)
> 1번 : 생성한 RDS에 들어가 endpoint를 복사하여 입력한다.
> 2번 : RDS를 생성할 때 작성했던 이름을 입력한다.
> 3번 : RDS를 생성할 때 작성했던 비밀번호를 입력한다.
> 4번 : RDS를 생성할 때 작성했던 초기 database이름을 입력한다.
> 5번 : PORT 번호를 입력한다.

이렇게 환경변수를 모두 입력하면 ```EB```의 컨테이너들이 ```RDS```를 인식할 수 있게 된다.

이번 포스팅에서는 ```AWS EB```와 ```RDS```를 생성하고 둘을 연결시켜주는 작업을 진행했다. 다음 포스팅부터 본격적으로 Docker를 통해 배포하는 법에 대해 공부하고 삽질한 뒤 작성해보겠다!
