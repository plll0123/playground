- nginx 기본 설정의 include를 활용해서 volume만 해두면 알아서 설정을 읽어가 사용함.
- 구조에 대해서 readme 정도만 작성해보면 될듯?
- docker가 내부 dns설정하는거 등등..

nginx 설치 및 기본 구조 이해 (http / server / location 블록)
- http 블럭 : http 요청에 대한 동작 정의
- 

단일 백엔드로 proxy_pass 설정해서 요청 포워딩

X-Forwarded-For, X-Real-IP 헤더 직접 추가

백엔드에서 request.getRemoteAddr() 찍어서 nginx IP가 보이는 것 확인

헤더로 원본 클라이언트 IP 복원하기

**핵심 질문:** "Spring Boot에서 getRemoteAddr()이 nginx의 IP를 반환하는 이유는 무엇이고, 어떻게 원본 IP를 알아낼 수 있는가?"