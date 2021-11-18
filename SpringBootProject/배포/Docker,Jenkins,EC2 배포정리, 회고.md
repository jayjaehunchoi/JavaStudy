# Docker, Jenkins, EC2에 배포하기
기존에는 EB에 배포를 진행할 예정이었으나, EC2 정도로도 충분히 애플리케이션 서버를 감당할 수 있을 것이라 생각하여 EC2에 Jenkins, Docker를 올려 배포하였다. 
과정들은 블로그나 reference보면 상세하게 나와있으나, 명령어, 권한 등은 똑같이 따라가면 오류가 많다. 그 내용을 기록하기 위해 이번 포스팅을 작성한다.

## 명령어
### Linux AMI 2 Jenkins install
```
sudo wget -O /etc/yum.repos.d/jenkins.repo \ https://pkg.jenkins.io/redhat-stable/jenkins.repo  
sudo rpm --import https://pkg.jenkins.io/redhat-stable/jenkins.io.key  
sudo yum upgrade  
sudo yum install epel-release java-11-openjdk-devel
sudo systemctl daemon-reload
```
> Jenkins 실행

```
sudo systemctl start jenkins
```

> Jenkins 상태확인

```
sudo systmctl status jenkins
```
> Jenkins 포트 변경

```
sudo vi /etc/sysconfig/jenkins
```

> Jenkins 최초 unlock password

```
sudo cat /var/lib/jenkins/secrets/initialAdminPassword
```
### Linux AMI2 Docker 설치
> yum update

```
sudo yum update -y
```

> docker 설치

```
sudo amazon-linux-extras install docker
```

> docker 시작

```
sudo service docker start
```

> docker 권한 부여

```
sudo usermod -a -G docker ec2-user
```

> docker 권한 그룹 (jenkins) 설정

```
sudo usermod -aG docker jenkins 
```

> jenkins bin bash 접근

```
sudo su -s /bin/bash jenkins
```

### Web hook 작업 중 jenkins 명령어
> ssh-keygen 발급 (github의 Deploy key 추가 용도)

```
mkdir /var/lib/jenkins/.ssh
ssh-keygen -t rsa -b 4096 -C "wh-key" -f /var/lib/jenkins/.ssh/jenkins-github-wh
```

> github deploy key에 넣을 key 파일 확인

```
cat /var/lib/jenkins/.ssh/jenkins-github-wh.pub
```

> jenkins credential에 넣을 key파일 확인 (username 은 생성한 파일 명)

```
cat /var/lib/jenkins/.ssh/jenkins-github-wh
```

### Pipeline
```groovy
pipeline {
    agent any
    tools {
        gradle 'gradle' 
    }
    options {
        timeout(time: 1, unit: 'HOURS')
    }
    environment {
        SOURCECODE_JENKINS_CREDENTIAL_ID = 'jenkins-github-wh'
        SOURCE_CODE_URL = 'https://github.com/jayjaehunchoi/Palette.git'
        RELEASE_BRANCH = 'main'
    }
    stages {
        stage('clone') {
            steps {
                git url: "$SOURCE_CODE_URL",
                    branch: "$RELEASE_BRANCH",
                    credentialsId: "$SOURCECODE_JENKINS_CREDENTIAL_ID"
                sh "ls -al"
            }
        }
		        
         stage('delete remain docker container') {
             steps {
                 echo 'clear'
                 sh 'docker stop $(docker ps -aq)'
                 sh 'docker rm $(docker ps -aq)'
             }
         }
         
         stage('frontend dockerizing') {
             steps {
                 sh "docker build -t palette/frontend ./frontend"
             }
         }

        stage('backend dockerizing') {
            steps {
                sh "pwd"
                dir("./backend"){
                    sh "pwd"

                    sh "gradle clean"
                    sh "gradle bootJar"

                    sh "docker build -t palette/backend ."
                }
            }
        }
        stage('deploy') {
            steps {
                sh '''
	              docker run -d -p 5500:5500 palette/frontend
                  docker run -d -p 8080:8080 palette/backend
                '''
            }
        }
    }
}
```


## 현재 구조
![image](https://user-images.githubusercontent.com/87312401/142388826-4ff5f724-97e7-4e33-9bde-356e0d16e56c.png)

#### 문제점
현재와 같이 빌드와 배포를 하나의 인스턴스에서 진행하다보니 생각보다 많은 메모리를 차지하고 서버의 성능이 나올까 걱정된다. 또, github에 push 하지 않는 개인정보가 담긴 ```properties```들을 어떻게 jenkins로 함께 빌드시켜야 할 지 모르겠다. 굳이 ```pipeLine```을 고집하지 말고 docker hub에 미리 빌드한 이미지를 올린 뒤 jenkins에서 실행시키고, git hook을 포기하거나, 빌드 서버를 분리해서 배포 서버로 jar파일을 던져주는 방식으로 진행하면 좋을 듯하다.
