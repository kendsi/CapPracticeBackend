# 캡스톤 연습 - 간단한 게시글 웹 사이트

## 로컬에서 실행하는 방법

### Spring Boot
- **실행 방법**: Just F5

### Redis(RAM에서 구동되는 DB) 설치 (보안을 위해 사용)
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
redis-server --service-install redis.windows.conf --service-name redis6379
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

## Postman으로 API 테스트하기

### 인증
/api/auth/login 으로 올바른 요청을 보내면 인증토큰이 반환됨

<br>

/api/articles/

해당 url의 하위 url에 보내는 GET 요청은 로그인 필요 X

<br>

/api/articles/search/

/api/auth/

해당 url의 하위 url에 보내는 모든 요청은 로그인 필요 X

<br>

그 외에는 Authorization -> Auth Type: Bearer Token 선택 후, 로그인한 뒤 나온 인증토큰 입력해야 함.

### RequestBody (POST, PUT 요청에서 사용)
#### AuthController (UserDTO)
```
{
    "id": "",
    "username": "test2",
    "nickname": "tester2",
    "password": "testpass2"
}
```
(login 할 때는 nickname 불필요)

위 같은 JSON 객체를 Body - raw에 입력하여 요청 보내야 함.

위에서 id 값 같은 경우 DB에서 자동할당 하기 때문에 서버에 요청 보낼때는 필요 없음.

아래도 값이 비어있으면 DB나 서버에서 자동할당한다고 보면 됨.

근데도 id 필드가 존재하는 이유는 GET 요청을 보내면 반환하기 때문.

#### ArticleController (ArticleDTO)
```
{
    "id": "",
    "title": "Test Article",
    "content": "Spring Boot is an open-source Java-based framework used to create microservices...",
    "createdAt": "",
    "modifiedAt": "",
    "authorId": "",
    "authorNickname": ""
}
```

#### CommentController (CommentDTO)
```
{
    "id": "",
    "articleId": "",
    "parentId": "",
    "content": "Test Comment",
    "createdAt": "",
    "modifiedAt": "",
    "authorId": "",
    "authorNickname": ""
}
```
OR
```
{
    "id": "",
    "articleId": "",
    "parentId": "{아무 Comment 객체 ID}",
    "content": "Test Reply",
    "createdAt": "",
    "modifiedAt": "",
    "authorId": "",
    "authorNickname": ""
}
```

댓글의 경우에는 Delete 요청 보낼 때 해당 댓글을 Parent로 하는 댓글이 있다면 댓글 내용만 지워지고 ID만 남음.

EX)
![image](https://github.com/user-attachments/assets/5cb86707-9695-4ed5-af71-6d82eccc7e00)


AuthController의 logout의 경우에 내가 링크 올린거에 잘못해놨는데 특이하게 Headers 탭에서

Key: Authorization

Value: Bearer (인증토큰)

(Bearer 후에 **띄어쓰기 한 번** + 인증토큰)

로 값을 넣어줘야 함.
