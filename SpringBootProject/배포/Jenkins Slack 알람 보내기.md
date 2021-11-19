# Jenkins Slack 알람

Jenkins를 이용한 CI/CD 를 성공했다. 하지만 빌드 수행 관련 알람이 없다면 진정한 CI라고 할 수 있을까? 나는 아니라고 생각한다.
따라서 빌드 과정 중 알람은 필수이고, 성공했는지 실패했는지 알려주는 것도 필수이다.
그렇기때문에 우리 프로젝트 **Jenkins pipeline**에 슬랙알림 기능을 추가하였고 이 방법을 기록하고자 한다.

## Slack에 app 추가
![image](https://user-images.githubusercontent.com/87312401/142582498-a87332df-1650-457a-8c0e-5b16d937dad8.png)
> 원하는 슬랙 채널에 들어가 슬랙 app 추가 버튼을 누른다

![image](https://user-images.githubusercontent.com/87312401/142582594-c45e3c32-1968-4515-98cf-7028e8f567f6.png)
> Jenkins를 검색하고 ```app 추가``` 버튼을 누른다. (나는 이미 추가되어 있어 app 추가 버튼이 없다.)

![image](https://user-images.githubusercontent.com/87312401/142582721-de01564c-c70b-4c2b-a0bf-1c8069ccd90b.png)
> Slack에 추가 버튼을 누른다.

![image](https://user-images.githubusercontent.com/87312401/142582779-a35fa772-1209-40ab-be1a-89ad2193a507.png)
> 원하는 채널을 선택해준다.

![image](https://user-images.githubusercontent.com/87312401/142582903-e28fa44f-2194-4415-8afb-9cf30ab8789e.png)
> 추가하게 되면 Jenkins에 등록하는 과정이 친절하게 나옵니다. 이 등록과정을 자연스럽게 따라가면 된다.
> 이 3단계에서 제공되는 workspace와 key값을 잘 복사해두자

## Jenkins에 Slack관련 환경변수 추가
![image](https://user-images.githubusercontent.com/87312401/142583272-f7c33dc7-5652-4531-8fa3-b4a1e761617c.png)
> Jenkins관리 > 시스템 설정 > Slack 을 찾고 다음 정보를 입력한다.
> Credential은 새로 추가하여 SecretText > 본인이 원하는 id > credential key를 추가해준다.

이제 기본적인 설정은 끝났고, ```freestyle project```를 사용한다면, slack의 안내사항을 따라가면 된다.
파이프라인은 조금 다릅니다. build가 시작되는 stage, step에서 시작한다는 명령어를 작성해주고, build가 끝날 때 성공, 실패에 따라 다른 메시지를 보내는 명령어를 작성해준다.
추가 할 명령어는 다음과 같다.
```groovy
// 빌드 시작 시점 step 사이에 추가
slackSend (channel: '#Slack Channel', color: '#FFFF00', message: "STARTED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")

// 모든 stage가 종료되고 pipeLine 종료 이전에 추가
post {
        success {
            slackSend (channel: '#Slack Channel', color: '#00FF00', message: "SUCCESSFUL: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
        }
        failure {
            slackSend (channel: '#Slack Channel', color: '#FF0000', message: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
        }
    }
```

이렇게 ```pipeLine```에 명령어를 작성하고 ```readme``` 를 바꿔 push 해본다.
![image](https://user-images.githubusercontent.com/87312401/142584135-df079400-0e59-435b-b076-670de14aea0c.png)
![image](https://user-images.githubusercontent.com/87312401/142584163-cd615bf6-9ed3-4003-b4d8-011311f2cb1f.png)

그러면 알림이 친절하게 나가는 것을 확인할 수 있다!!



