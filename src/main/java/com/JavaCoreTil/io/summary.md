# Java I/O 기초 개념 마스터 요약

## 📚 0단계: I/O 기초 이론 (완료 ✅)

### 🎯 1. 스트림(Stream) 기본 개념
- **정의**: 입출력을 수행할 때 **단방향으로 진행하는 연결 통로**
- **특징**: 
  - 오직 한 번에 하나의 방향만 가능 (Input과 Output 분리)
  - **순차 접근**: 앞에서부터 차례대로만 읽기 가능
  - **실시간 처리**: 받자마자 바로 처리 (조립 방식 아님)

**💡 핵심 비유**: 컨베이어 벨트 - 물건을 받자마자 바로 포장

### 🎯 2. 바이트 스트림 vs 문자 스트림
| 구분 | 바이트 스트림 | 문자 스트림 |
|------|--------------|------------|
| **용도** | 모든 파일 형식 | 텍스트 파일만 |
| **예시** | `.jpg`, `.pdf`, `.exe`, `.mp4` | `.txt`, `.java`, `.properties` |
| **범용성** | ⭐⭐⭐ 더 범용적 | ⭐⭐ 제한적 |
| **이유** | 모든 데이터는 결국 바이트 | 바이트 + 인코딩/디코딩 자동 처리 |

### 🎯 3. 동기 vs 비동기 I/O
```java
// 동기 I/O (1개 스레드)
public void syncProcess() {
    processFile1(); // 완료까지 대기
    processFile2(); // 완료까지 대기
    processFile3(); // 완료까지 대기
} // 메모리: 1MB (1개 스레드 스택)

// 비동기 I/O (3개 스레드)
public void asyncProcess() {
    CompletableFuture.runAsync(() -> processFile1());
    CompletableFuture.runAsync(() -> processFile2());
    CompletableFuture.runAsync(() -> processFile3());
} // 메모리: 3MB (3개 스레드 스택) + 컨텍스트 스위칭 비용
```

### 🎯 4. 블로킹 vs 논블로킹 I/O
- **블로킹**: I/O 작업 완료까지 스레드 대기
- **논블로킹**: I/O 작업과 상관없이 즉시 리턴
- **주의**: 동기/비동기와는 **별개 개념**

### 🎯 5. 버퍼링의 원리와 성능 영향

#### 🚚 택배 화물차 비유
```
HDD(창고) → 시스템 버스(고속도로) → 메인 메모리 RAM 버퍼(물류센터) → CPU 캐시(편의점) → CPU(집)
```

#### 속도 차이 (1초를 1년으로 환산)
| 구간 | 실제 시간 | 환산 시간 |
|------|----------|----------|
| CPU 연산 | 1나노초 | 1초 |
| 메모리 접근 | 100나노초 | 4분 |
| SSD 접근 | 100마이크로초 | 4일 |
| HDD 접근 | 10밀리초 | **4개월** ⚠️ |

**가장 느린 병목**: 디스크 접근!

### 🎯 6. HDD vs SSD 동작 원리

#### HDD (기계적 동작)
```
1. 플래터 회전 (7200 RPM)
2. 헤드 물리적 이동 (8-15ms)
3. 회전 대기 (4ms)
4. 데이터 읽기
```
**비유**: 레코드 플레이어에서 바늘이 다른 홈으로 이동

#### SSD (전자적 동작)
```java
// SSD = 거대한 전자 우편함 시스템
boolean[] mailbox = new boolean[1000000];

// 저장: 특정 우편함에 편지(전자) 넣기
mailbox[12345] = true;

// 읽기: 우편함 번호로 즉시 확인  
boolean hasLetter = mailbox[12345]; // 전기 신호로 즉시
```

#### 현실적 선택
- **SSD**: 운영체제, 자주 쓰는 프로그램 (속도 중요)
- **HDD**: 대용량 저장 (가격 4배 저렴, 용량 중요)

### 🎯 7. 인코딩/디코딩 및 UTF-8

#### 기본 개념
- **인코딩**: 문자 → 바이트 변환
- **디코딩**: 바이트 → 문자 해석

#### UTF-8 가변 길이 인코딩
```
UTF-8 저장 공간 할당:
- 1바이트: 0~127 (영어, 숫자)
- 2바이트: 128~2,047 (유럽 문자)
- 3바이트: 2,048~65,535 (한국어, 중국어, 일본어)
- 4바이트: 65,536~ (이모지)

한국어 "가" = U+AC00 = 44,032
44,032 > 2,047 → 3바이트 필요!
```

## 🎯 8. 시스템 버스와 메인보드
- **메인보드**: 모든 부품을 연결하는 물리적 판 (도시 전체)
- **시스템 버스**: 메인보드 위의 데이터 전송 통로 (고속도로망)
  - 데이터 버스: 실제 데이터 전송
  - 주소 버스: 메모리 주소 전송
  - 제어 버스: 읽기/쓰기 신호 전송

---

## 🎯 핵심 포인트 정리

### ✅ 반드시 기억할 것
1. **Stream = 실시간 순차 처리** (조립 아님)
2. **바이트 스트림이 더 범용적** (모든 데이터는 바이트)
3. **비동기 ≠ 메모리 효율적** (스레드별 스택 메모리 필요)
4. **디스크가 가장 느린 병목** (버퍼링 중요성)
5. **블로킹/논블로킹 ≠ 동기/비동기** (별개 개념)

### 🔧 실무 적용점
- 대용량 파일 처리 시 Stream 사용 필수
- 텍스트는 문자 스트림, 나머지는 바이트 스트림
- 성능 최적화의 핵심은 디스크 접근 최소화
- SSD + HDD 하이브리드 구성이 현실적

---

## 🔧 1단계: 전통적인 I/O (java.io 패키지) - 기본기 (완료 ✅)

### 🎯 InputStream/OutputStream 핵심 개념

#### 추상 클래스 설계 원리
- **InputStream**: 모든 바이트 읽기 스트림의 **최상위 조상**
- **OutputStream**: 모든 바이트 쓰기 스트림의 **최상위 조상**
- **설계 목적**: 파일, 네트워크, 메모리 등 다양한 데이터 소스를 **통일된 방식**으로 처리

```java
// 다형성 활용 - 데이터 소스와 무관하게 동일한 코드
InputStream is = new FileInputStream("file.txt");      // 파일에서
InputStream is = new ByteArrayInputStream(bytes);      // 메모리에서  
InputStream is = socket.getInputStream();              // 네트워크에서
// 모두 동일한 read() 메서드 사용!
```

### 🎯 핵심 메서드 3개

#### InputStream 메서드
```java
// 1. int read() - 1바이트 읽기 (가장 중요!)
int byteData = inputStream.read();
if (byteData == -1) {
    System.out.println("EOF 도달!");
} else {
    char c = (char) byteData;
    System.out.println("읽은 문자: " + c);
}

// 2. int read(byte[] b) - 배열에 읽기
byte[] buffer = new byte[1024];
int bytesRead = inputStream.read(buffer);
String data = new String(buffer, 0, bytesRead);

// 3. void close() - 리소스 해제 (필수!)
inputStream.close(); // try-with-resources로 자동화 권장
```

#### OutputStream 메서드
```java
// 1. void write(int b) - 1바이트 쓰기
outputStream.write(65); // 'A' 문자 쓰기

// 2. void write(byte[] b) - 배열 쓰기
String message = "Hello World";
outputStream.write(message.getBytes());

// 3. void close() - 리소스 해제
outputStream.close();
```

### 🎯 중요한 설계 원리들

#### 1. read() 메서드가 int를 반환하는 이유
```java
// Java byte 범위: -128 ~ 127 (signed)
// 실제 바이트 데이터: 0 ~ 255 (unsigned)  
// EOF 신호: -1

// 만약 byte를 사용한다면?
byte byteEOF = (byte) -1;  // -1
byte byteData = (byte) 255; // -1 (오버플로우)
// EOF와 실제 데이터(255) 구분 불가능! ❌

// int를 사용하면?
int intEOF = -1;        // EOF 신호
int intData = 255;      // 실제 바이트 값
// 명확한 구분 가능! ✅
```

#### 2. FileOutputStream 모드
```java
// 기본 모드: 덮어쓰기 (기존 내용 삭제)
new FileOutputStream("file.txt")

// append 모드: 기존 내용 뒤에 추가
new FileOutputStream("file.txt", true)

// 실무 예시
// 로그 파일: append 모드 사용
// 설정 파일: 덮어쓰기 모드 사용
```

### 🎯 리소스 관리의 중요성

#### close() 메서드의 역할
```java
// ❌ 잘못된 이해: GC와 관련
// ✅ 올바른 이해: OS 리소스 즉시 해제

// OS 파일 핸들 테이블 (프로세스당 제한: 1024개)
┌─────┬─────────────┬─────────────┐
│핸들#│   파일명    │    상태     │
├─────┼─────────────┼─────────────┤
│  3  │  test.txt   │   열림     │  ← close() 호출 시 즉시 해제
│  4  │  data.txt   │   열림     │
│ ... │    ...      │   ...      │
│1023 │  last.txt   │   열림     │
│1024 │     -       │ 사용불가    │  ← 한계 도달!
└─────┴─────────────┴─────────────┘
```

#### 리소스 누수 유형별 문제점
```java
// 1. 메모리 누수 (Java 힙)
List<byte[]> leak = new ArrayList<>();
leak.add(new byte[1024 * 1024]); // 1MB씩 누적
// 해결: leak.clear() + System.gc()

// 2. 파일 핸들 누수 (OS 리소스)  
new FileInputStream("file.txt"); // close() 없음
// 결과: "Too many open files" 오류
// 해결: close() 호출 필수

// 3. 파일 잠금 (프로세스 간 충돌)
FileOutputStream fos = new FileOutputStream("shared.txt");
// close() 없으면 다른 프로세스가 파일 접근 불가
// 해결: close()로 잠금 해제
```

### 🎯 성능 비교 실험 결과

#### 1바이트씩 vs 배열 단위 처리
```java
// 1KB 파일 처리 시간 비교
// 1바이트씩 처리: 15.2ms  
// 배열 단위 처리: 0.8ms
// 성능 향상: 19배 빨라짐!

// 이유: 시스템 콜 호출 횟수 차이
// 1바이트씩: 1,024번 시스템 콜
// 배열 단위: 1번 시스템 콜
```

### 🎯 핵심 패턴

#### try-with-resources (권장)
```java
try (FileInputStream fis = new FileInputStream("input.txt");
     FileOutputStream fos = new FileOutputStream("output.txt")) {
    
    int data;
    while ((data = fis.read()) != -1) {
        fos.write(data);
    }
    // 자동으로 close() 호출됨
}
```

#### 배열 기반 효율적 복사
```java
try (InputStream is = new FileInputStream("source.txt");
     OutputStream os = new FileOutputStream("dest.txt")) {
    
    byte[] buffer = new byte[8192]; // 8KB 버퍼
    int bytesRead;
    while ((bytesRead = is.read(buffer)) != -1) {
        os.write(buffer, 0, bytesRead);
    }
}
```

---

## 🎯 핵심 포인트 정리

### ✅ 0단계에서 배운 것
1. **Stream = 실시간 순차 처리** (조립 아님)
2. **바이트 스트림이 더 범용적** (모든 데이터는 바이트)
3. **비동기 ≠ 메모리 효율적** (스레드별 스택 메모리 필요)
4. **디스크가 가장 느린 병목** (버퍼링 중요성)
5. **블로킹/논블로킹 ≠ 동기/비동기** (별개 개념)

### ✅ 1단계에서 배운 것
1. **read() 메서드는 int 반환** (EOF 구분용)
2. **close()는 OS 리소스 해제** (GC와 무관)
3. **파일 핸들 = OS 번호표** (RAM과 별개)
4. **배열 처리가 훨씬 효율적** (시스템 콜 최소화)
5. **try-with-resources 필수** (리소스 누수 방지)

### 🔧 실무 적용점
- **모든 스트림 작업에 try-with-resources 사용**
- **대용량 파일은 배열 단위 처리**
- **로그 파일은 append 모드**
- **파일 핸들 한계 고려한 설계**

---

## 🚀 다음 단계: BufferedInputStream/BufferedOutputStream