# 캡스톤 연습 - 간단한 게시글 웹 사이트

## 로컬에서 실행하는 방법

### Spring Boot
- **실행 방법**: Just F5

### Redis 설치 (보안을 위해 사용)
1. [Redis 3.0.504 버전 다운로드](https://github.com/MSOpenTech/redis/releases)

<br>

2. 설치 후, `redis.windows.conf` 파일을 텍스트 에디터로 열기

<br>

3. `# requirepass foobared`를 찾아서 `requirepass Capstone2`로 수정

<br>

4. Redis 설치 디렉토리에서 **관리자 권한으로 cmd를 실행**

<br>

5. 아래 명령어로 윈도우 서비스로 DB 서버 시작
```
redis-server --service-install redis.conf --service-name redis6379
redis-server --service-start --service-name redis6379
redis-cli -p 6379
auth Capstone2
```
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ping 쳐서 pong 뜨면 제대로 실행된 것
```
redis-server --service-stop --service-name redis6379 //종료
redis-server --service-uninstall --service-name redis6379 //서비스 삭제
```

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;이제 로그아웃하면 쓰던 인증 토큰은 redisDB(블랙리스트)에 추가
