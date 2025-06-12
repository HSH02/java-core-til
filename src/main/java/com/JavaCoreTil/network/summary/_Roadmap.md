## Java Network 프로그래밍 학습 로드맵 

```
0단계: Network 기초 이론
├─ OSI 7계층 / TCP/IP 4계층 모델 이해
├─ IP 주소와 포트 개념 (InetAddress 클래스)
├─ DNS 동작 원리 (도메인 → IP 변환 과정)
├─ 방화벽과 NAT 개념 (네트워크 보안 기초)
├─ 클라이언트-서버 아키텍처
├─ TCP 3-way 핸드셰이크 / 4-way 핸드셰이크
├─ TCP vs UDP 차이점
└─ HTTP 프로토콜 기본

1단계: Socket 프로그래밍 기초
├─ Socket 클래스 (클라이언트 측)
│ ├─ 서버 연결 (connect)
│ ├─ 데이터 송신 (OutputStream)
│ └─ 데이터 수신 (InputStream)
├─ ServerSocket 클래스 (서버 측)
│ ├─ 포트 바인딩 (bind)
│ ├─ 클라이언트 대기 (accept)
│ └─ 연결 처리
├─ 소켓 상태 관리 (CLOSE_WAIT, TIME_WAIT)
├─ 단순 에코 서버/클라이언트 구현
├─ try-with-resources로 자원 관리
└─ 소켓 옵션 설정 (timeout, buffer size 등)

2단계: 멀티 클라이언트 처리
├─ Thread 기반 서버
│ ├─ 클라이언트별 Thread 생성
│ └─ Thread 안전성 고려
├─ 동시 접속 처리
├─ 클라이언트 연결 관리
│ ├─ 연결 목록 관리
│ └─ 연결 해제 처리
├─ ThreadPool 활용 (ExecutorService)
├─ 로드 밸런싱 기초 개념
│ ├─ Round Robin 방식
│ ├─ Weighted Round Robin
│ └─ 간단한 로드 밸런서 구현
└─ 실시간 채팅 서버 구현

3단계: NIO 기반 네트워크
├─ SocketChannel (클라이언트)
├─ ServerSocketChannel (서버)
├─ Selector를 이용한 비동기 처리
│ ├─ 이벤트 기반 처리
│ └─ 논블로킹 I/O
├─ Buffer 활용
├─ 채널과 버퍼의 관계
└─ 성능 비교 (전통적 Socket vs NIO)

4단계: HTTP 통신
├─ URL 클래스 (URL 파싱과 구성요소)
├─ URLConnection (일반적인 연결 처리)
├─ HttpURLConnection (HTTP 전용)
│ ├─ GET 요청
│ ├─ POST 요청
│ └─ 응답 처리
├─ HTTP/2 기초 개념
│ ├─ 멀티플렉싱
│ ├─ 서버 푸시
│ └─ 헤더 압축
├─ WebSocket 프로토콜 기초
│ ├─ HTTP에서 WebSocket으로 업그레이드
│ ├─ 프레임 구조 이해
│ └─ 실시간 양방향 통신
├─ REST API 호출
├─ JSON 데이터 처리
├─ HTTP 헤더 처리
├─ 쿠키와 세션 관리
└─ HTTP 클라이언트 구현

5단계: 고급 네트워크 프로그래밍
├─ SSL/TLS 보안 통신
│ ├─ SSLSocket / SSLServerSocket
│ └─ 인증서 처리
├─ UDP 프로그래밍 (DatagramSocket)
│ ├─ DatagramPacket 처리
│ └─ 브로드캐스트 통신
├─ 멀티캐스트 통신
├─ 네트워크 성능 최적화
│ ├─ 버퍼 크기 조정
│ ├─ Keep-Alive 연결
│ └─ 커넥션 풀링
├─ 네트워크 디버깅 도구
│ ├─ Wireshark 기초
│ └─ 네트워크 모니터링
└─ 네트워크 예외 처리 전략
```