# 간단한 1:1 채팅 프로그램

## 개요
Java NIO를 사용한 간단한 1:1 채팅 프로그램입니다. 서버와 클라이언트 기능을 하나의 클래스에 모두 포함하고 있습니다.

## 주요 기능
- ✅ 1:1 실시간 채팅
- ✅ 양방향 메시지 전송/수신
- ✅ 멀티스레딩을 통한 동시 처리
- ✅ 콘솔 기반 인터페이스
- ✅ UTF-8 인코딩 지원 (한글 완벽 지원)
- ✅ 실시간 시간 표시 (HH:mm 형식)
- ✅ 안정적인 연결 관리

## 사용법

### 1. 컴파일
```bash
javac SimpleChat.java
```

### 2. 서버 실행
```bash
# 기본 포트(8080)로 서버 실행
java SimpleChat server

# 특정 포트로 서버 실행
java SimpleChat server 9090

# 한글 인코딩 문제가 있는 경우 (Windows)
java -Dfile.encoding=UTF-8 SimpleChat.java server
```

### 3. 클라이언트 실행
```bash
# localhost:8080에 연결
java SimpleChat client

# 특정 서버에 연결
java SimpleChat client 192.168.1.100 8080

# 한글 인코딩 문제가 있는 경우 (Windows)
java -Dfile.encoding=UTF-8 SimpleChat.java client
```

## 실행 예시

### 서버 실행
```
=== 채팅 서버 시작 ===
포트: 8080
클라이언트 연결 대기 중...
클라이언트 연결됨: /127.0.0.1:54321

=== 채팅 시작 ===
메시지를 입력하세요. (종료: 'quit' 또는 'exit')
----------------------------------------
[14:30] 메시지 입력: 안녕하세요!
[14:30] 보낸 메시지: 안녕하세요!

[14:31] 받은 메시지: 반갑습니다!
[14:31] 메시지 입력: 
```

### 클라이언트 실행
```
=== 채팅 클라이언트 시작 ===
서버: localhost:8080
서버에 연결됨!

=== 채팅 시작 ===
메시지를 입력하세요. (종료: 'quit' 또는 'exit')
----------------------------------------
[14:30] 받은 메시지: 안녕하세요!
[14:31] 메시지 입력: 반갑습니다!
[14:31] 보낸 메시지: 반갑습니다!
[14:31] 메시지 입력: 
```

## 개선된 기능

### 1. 한글 인코딩 문제 해결
- UTF-8 인코딩 완벽 지원
- Scanner에 UTF-8 명시적 지정
- ByteBuffer 처리 방식 개선
- 시스템 인코딩 설정 추가
- 메시지 길이 기반 안전한 전송

### 2. 실시간 시간 표시
- 모든 메시지에 [HH:mm] 형식으로 시간 표시
- 받은 메시지, 보낸 메시지 모두 시간 포함

### 3. 안정적인 양방향 통신
- AtomicBoolean을 사용한 안전한 종료 처리
- 연결 상태 실시간 모니터링
- 예외 상황 개선된 처리
- 메시지 길이 기반 안전한 전송/수신

## 기술적 특징

### 사용된 기술
- **Java NIO**: SocketChannel, ServerSocketChannel
- **ByteBuffer**: 효율적인 데이터 전송
- **멀티스레딩**: 동시 메시지 전송/수신
- **ExecutorService**: 스레드 풀 관리
- **AtomicBoolean**: 스레드 안전한 상태 관리
- **LocalTime**: 실시간 시간 처리
- **UTF-8 인코딩**: 한글 완벽 지원

### 핵심 클래스
- `ServerSocketChannel`: 서버 소켓
- `SocketChannel`: 클라이언트 소켓
- `ByteBuffer`: 메시지 버퍼링
- `ExecutorService`: 스레드 관리
- `AtomicBoolean`: 안전한 종료 처리

## 제한사항
- 1:1 채팅만 지원 (다중 사용자 미지원)
- 콘솔 기반 인터페이스
- 기본적인 에러 처리
- 파일 전송 기능 없음

## 확장 가능한 기능
- 다중 사용자 채팅
- GUI 인터페이스
- 파일 전송
- 메시지 암호화
- 사용자 인증
- 채팅방 기능
- 이모티콘 지원

## 문제 해결

### 포트가 이미 사용 중인 경우
```bash
# 다른 포트 사용
java SimpleChat server 9090
java SimpleChat client localhost 9090
```

### 연결이 안 되는 경우
1. 방화벽 설정 확인
2. 서버가 실행 중인지 확인
3. IP 주소와 포트 확인

### 한글 깨짐 현상 (완전 해결)
- UTF-8 인코딩 완벽 지원
- Scanner에 UTF-8 명시적 지정
- ByteBuffer 처리 방식 개선
- 시스템 인코딩 설정 추가
- 메시지 길이 기반 안전한 전송

**Windows에서 한글이 깨지는 경우:**
```bash
# JVM 옵션으로 UTF-8 강제 설정
java -Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8 SimpleChat server
java -Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8 SimpleChat client
```

### 채팅 종료 방법
- `quit` 또는 `exit` 입력
- Ctrl+C로 강제 종료 