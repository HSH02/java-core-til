```java
---
⚡ 1단계: 네트워크 기초 이론
├─ OSI 7계층 모델 : 물리/데이터링크/네트워크/전송/세션/표현/응용
├─ TCP/IP 모델 : 링크/인터넷/전송/응용 계층
├─ IP 주소와 포트 : IPv4/IPv6, 포트 번호 체계
├─ DNS 개념 : 도메인 이름 해석, 계층 구조
├─ 프로토콜 기초 : TCP vs UDP 특성 비교
└─ 네트워크 바이트 순서 : Big-Endian, 네트워크 순서

---
⚡ 2단계: 소켓 프로그래밍 기초
├─ Socket 개념 : 네트워크 통신 끝점, 파일 디스크립터
├─ 클라이언트 소켓 : Socket 클래스, 연결 설정
├─ 서버 소켓 : ServerSocket 클래스, 연결 수락
├─ TCP 연결 과정 : 3-way handshake, 연결 해제
├─ InputStream/OutputStream : 네트워크 스트림 처리
└─ 간단한 에코 서버 : 기본 클라이언트-서버 구현

---
⚡ 3단계: TCP 소켓 심화
├─ 소켓 옵션 : SO_TIMEOUT, SO_REUSEADDR, SO_KEEPALIVE
├─ 버퍼링과 성능 : SO_RCVBUF, SO_SNDBUF 설정
├─ Nagle 알고리즘 : TCP_NODELAY, 작은 패킷 최적화
├─ 연결 풀링 : 소켓 재사용, 연결 관리
├─ 예외 처리 : SocketException, 네트워크 오류 대응
└─ 타임아웃 관리 : 연결/읽기/쓰기 타임아웃 설정

---
⚡ 4단계: UDP 소켓 프로그래밍
├─ UDP 특성 : 비연결성, 신뢰성 없음, 빠른 전송
├─ DatagramSocket : UDP 소켓 클래스
├─ DatagramPacket : UDP 패킷 구조
├─ UDP 클라이언트/서버 : 패킷 송수신
├─ 브로드캐스트/멀티캐스트 : 일대다 통신
└─ UDP vs TCP : 사용 시나리오, 트레이드오프

---
⚡ 5단계: InetAddress와 네트워크 유틸리티
├─ InetAddress 클래스 : IP 주소 추상화
├─ 주소 해석 : getByName(), getAllByName()
├─ 로컬 주소 정보 : getLocalHost(), 네트워크 인터페이스
├─ 네트워크 연결성 테스트 : isReachable(), ping 구현
├─ URL/URI 처리 : URL 클래스, 구성 요소 파싱
└─ NetworkInterface : 네트워크 카드 정보

---
⚡ 6단계: HTTP 클라이언트 (Java 11+)
├─ HttpClient 클래스 : 모던 HTTP 클라이언트
├─ HttpRequest : 요청 빌더, 메서드/헤더 설정
├─ HttpResponse : 응답 처리, 상태 코드
├─ 동기/비동기 요청 : send(), sendAsync()
├─ 인증과 쿠키 : 기본 인증, 쿠키 매니저
└─ HTTP/2 지원 : 멀티플렉싱, 서버 푸시

---
⚡ 7단계: NIO 네트워크 프로그래밍
├─ Selector 개념 : 이벤트 기반 I/O, 멀티플렉싱
├─ SocketChannel : 논블로킹 클라이언트 소켓
├─ ServerSocketChannel : 논블로킹 서버 소켓
├─ SelectionKey : 이벤트 타입, 관심 연산
├─ 이벤트 루프 : select(), 이벤트 처리 패턴
└─ 고성능 서버 : 적은 스레드로 많은 연결 처리

---
⚡ 8단계: 웹 서버 구현
├─ HTTP 프로토콜 파싱 : 요청 라인, 헤더, 바디
├─ HTTP 응답 생성 : 상태 코드, 헤더, 컨텐츠
├─ 정적 파일 서빙 : 파일 시스템 연동
├─ 멀티스레드 서버 : 스레드 풀 활용
├─ Keep-Alive 연결 : 연결 재사용, 성능 최적화
└─ 가상 호스트 : 도메인별 라우팅

-

```