# 캡스톤 연습 (간단한 게시글 웹 사이트)

## 로컬에서 돌리는 법
### Spring Boot
> Just F5

### Redis (For security)
> https://github.com/MSOpenTech/redis/releases 에서 3.0.504 버전 다운로드
> 
> 설치 후 redis.windows.conf 텍스트 에디터로 열기
> 
> "# requirepass foobared" 검색해서 "requirepass Capstone2 로 수정"
> 
> 해당 디렉토리에서 관리자권한으로 cmd 열기
> 
> (윈도우 서비스로 DB 서버 시작)
> 
> redis-server --service-install redis.conf --service-name redis6379
> 
> redis-server --service-start --service-name redis6379
> 
> redis-cli -p 6379
> 
> auth Capstone2
> 
> ping 쳐서 pong 뜨면 제대로 실행된 것
> 
> redis-server --service-stop --service-name redis6379 으로 종료
> 
> redis-server --service-uninstall --service-name redis6379 으로 서비스 삭제
>
> 이제 로그아웃하면 쓰던 인증 토큰은 redisDB(블랙리스트)에 추가
