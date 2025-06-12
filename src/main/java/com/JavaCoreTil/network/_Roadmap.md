
```
---
🟢 0단계: Network 기초 이론
├─ OSI 7계층 / TCP/IP 4계층 : 네트워크 계층 구조 이해
├─ IP/Port : 주소, 포트 개념, InetAddress
├─ DNS : 도메인→IP 변환, 동작 원리
├─ 방화벽/NAT : 네트워크 보안 기초
├─ 클라이언트-서버 아키텍처 : 구조와 역할
├─ TCP 3-way/4-way 핸드셰이크 : 연결/종료 과정
├─ TCP vs UDP : 신뢰성, 속도, 용도 차이
└─ HTTP 프로토콜 : 기본 구조, 요청/응답

---
🟡 1단계: Socket 프로그래밍 기초
├─ Socket 클래스(클라이언트) : connect, OutputStream, InputStream
├─ ServerSocket 클래스(서버) : bind, accept, 연결 처리
├─ 소켓 상태 관리 : CLOSE_WAIT, TIME_WAIT
├─ 에코 서버/클라이언트 : 단순 통신 구현
├─ 자원 관리 : try-with-resources
└─ 소켓 옵션 : timeout, buffer size 등

---
🟠 2단계: 멀티 클라이언트 처리
├─ Thread 기반 서버 : 클라이언트별 Thread, Thread 안전성
├─ 동시 접속/연결 관리 : 목록, 해제
├─ ThreadPool 활용 : ExecutorService
├─ 로드 밸런싱 : Round Robin, Weighted, 간단 구현
└─ 실시간 채팅 서버 : 다중 클라이언트 통신

---
🔵 3단계: NIO 기반 네트워크
├─ SocketChannel/ServerSocketChannel : 논블로킹 I/O
├─ Selector : 이벤트 기반 처리
├─ Buffer : 채널-버퍼 관계
└─ 성능 비교 : 전통 Socket vs NIO

---
🟣 4단계: HTTP 통신
├─ URL/URLConnection : 파싱, 연결 처리
├─ HttpURLConnection : GET, POST, 응답
├─ HTTP/2 : 멀티플렉싱, 서버 푸시, 헤더 압축
├─ WebSocket : 업그레이드, 프레임, 실시간 통신
├─ REST API/JSON : 데이터 처리
├─ HTTP 헤더/쿠키/세션 : 관리 방법
└─ HTTP 클라이언트 구현

---
🟤 5단계: 고급 네트워크 프로그래밍
├─ SSL/TLS : 보안 통신, 인증서
├─ UDP : DatagramSocket, DatagramPacket, 브로드캐스트
├─ 멀티캐스트 : 다중 수신
├─ 성능 최적화 : 버퍼, Keep-Alive, 커넥션 풀
├─ 네트워크 디버깅 : Wireshark, 모니터링
└─ 예외 처리 전략
---

```