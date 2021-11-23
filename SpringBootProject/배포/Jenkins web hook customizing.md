# Jenkins web hook customizing
현재 프로젝트에서 누구든 ```main```브랜치에 ```push```하면 자동 빌드가 시작되게끔 세팅되어 있었다. 하지만 서버 배포를 담당하는 나 이외의 누군가가 푸시할 때 굳이 빌드될 이유가 없을 것 같다는 생각이 들었다.
그래서 내 아이디인 ```jayjaehunchoi```로 ```main```브랜치에 ```push```할 때만 자동 배포되게 세팅하고자 한다.

1. Jenkins plugin 설치
![image](https://user-images.githubusercontent.com/87312401/142973195-deb360f7-5cc2-4d9f-af7d-402b575a7876.png)
> Generic Webhook Trigger 를 설치해준다. Webhook을 커스터마이징할 수 있게끔 도와주는 플러그인이다.

2. github webhook 수정
![image](https://user-images.githubusercontent.com/87312401/142973433-7ed420af-c51a-42f9-a2a0-15e06ab2ce24.png)
> http://{Jenkins 주소}/generic-webhook-trigger/invoke?token={임의 토큰}을 입력하고 Content type을 json으로 선택하여 update한다.

3. Jenkins 설정
![image](https://user-images.githubusercontent.com/87312401/142974819-18bc95e7-67b2-4f01-869b-01ecb31051a1.png)
> Generic Webhooke Trigger를 선택한다.

![image](https://user-images.githubusercontent.com/87312401/142975068-49e56f67-ef86-40a9-a42c-75ee99dd0a25.png)
> ```refs/heads/```를 필터로 걸어 ```main```값만 받아와 branch 변수로 추가한다. 

![image](https://user-images.githubusercontent.com/87312401/142974957-014c4002-1f09-4c9f-8143-9ad146e8dc87.png)

> ```pusher.name``` 으로 전달되는 변수를 ```pusher```변수로 지정한다.

![image](https://user-images.githubusercontent.com/87312401/142975301-3572433c-e6a7-4817-9251-14b0a02a04ef.png)
> 내가 지정한 Token값을 입력한다.

![image](https://user-images.githubusercontent.com/87312401/142975238-941b2fe7-3c41-4d30-9d16-5813b1c8ef47.png)
> ```main```-```jayjaehunchoi```를 필터로 걸어 main에 내가 푸시했을때만 빌드되게 세팅한다.

이렇게 세팅을 마무리 하면 이제부터 내가 푸시할때만 빌드가 된다!
