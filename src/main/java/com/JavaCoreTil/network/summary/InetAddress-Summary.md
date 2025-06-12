# InetAddress 클래스 완전 정리 📚

---

## 🎯 IP 주소와 포트 기본 개념

### 📍 IP 주소 (Internet Protocol Address)
**네트워크상에서 컴퓨터를 식별하는 고유한 주소**

```java
// IP 주소 = 네트워크의 "집 주소"
192.168.1.100  // 사설 IP (내부 네트워크)
8.8.8.8        // 공인 IP (구글 DNS)
127.0.0.1      // 루프백 IP (자기 자신)
```

#### 🏠 아파트 비유
```
🏢 인터넷 = 거대한 아파트 단지
├─ 🏠 192.168.1.100 = 101동 (컴퓨터 A)
├─ 🏠 192.168.1.101 = 102동 (컴퓨터 B)  
└─ 🏠 8.8.8.8 = 외부 건물 (Google 서버)
```

### 🚪 포트 (Port)
**컴퓨터 내에서 실행되는 특정 프로그램을 식별하는 번호**

```java
// 포트 = 아파트의 "호수"
192.168.1.100:80   // 웹서버 (HTTP)
192.168.1.100:22   // SSH 서버
192.168.1.100:3306 // MySQL 데이터베이스
192.168.1.100:8080 // 개발용 서버
```

#### 🎯 완전한 네트워크 주소
```java
IP 주소 + 포트 = 완전한 통신 주소
예시: 142.250.207.4:80 = Google 웹서버의 정확한 위치
```

---

## 🔍 IP 주소 유형 분류

### 🏢 사설 IP (Private IP)
**내부 네트워크에서만 사용되는 주소**

```java
// RFC 1918에서 정의된 사설 IP 대역
10.0.0.0    ~ 10.255.255.255   // Class A (대기업)
172.16.0.0  ~ 172.31.255.255   // Class B (중간 규모)
192.168.0.0 ~ 192.168.255.255  // Class C (가정/소규모)

// 특징
- 인터넷에서 직접 접근 불가
- 방화벽/NAT 환경에서 안전
- 내부 네트워크 통신용
```

### 🌐 공인 IP (Public IP)
**인터넷에서 직접 접근 가능한 주소**

```java
// 예시
8.8.8.8        // Google DNS
1.1.1.1        // Cloudflare DNS
142.250.199.68 // Google 웹서버

// 특징
- 전 세계에서 유일한 주소
- 인터넷 서비스 제공용
- 보안 설정 필요
```

### 🏠 특수 IP 주소들
```java
127.0.0.1      // 루프백 (localhost, 자기 자신)
169.254.x.x    // 링크 로컬 (DHCP 실패 시 자동 할당)
224.x.x.x      // 멀티캐스트 (그룹 통신)
```

---

## 🛠️ InetAddress 클래스 완전 분석

### 📋 주요 메서드별 분류표

| 분류 | 메서드 | 반환 타입 | 용도 | 실무 활용 |
|------|--------|-----------|------|-----------|
| **🏗️ 객체 생성** | `getByName(String)` | `InetAddress` | 도메인/IP로 객체 생성 | DNS 조회, 서버 연결 |
| | `getAllByName(String)` | `InetAddress[]` | 모든 IP 배열 조회 | 로드밸런싱, 서버 클러스터 |
| | `getLocalHost()` | `InetAddress` | 내 컴퓨터 정보 | 로깅, 시스템 진단 |
| **📝 정보 조회** | `getHostAddress()` | `String` | 읽기 쉬운 IP 문자열 | 사용자 화면 출력 |
| | `getHostName()` | `String` | 호스트명/도메인 | 로그 기록, 식별 |
| | `getAddress()` | `byte[]` | 원시 바이트 데이터 | 네트워크 프로그래밍 |
| **🔍 연결 테스트** | `isReachable(int)` | `boolean` | 연결 가능 여부 | 서버 모니터링, 헬스체크 |
| **🎯 IP 유형 판별** | `isSiteLocalAddress()` | `boolean` | 사설 IP 확인 | 보안 정책, 네트워크 분석 |
| | `isLoopbackAddress()` | `boolean` | 루프백 확인 | 로컬 테스트 환경 |
| | `isLinkLocalAddress()` | `boolean` | 링크 로컬 확인 | 네트워크 문제 진단 |
| | `isMulticastAddress()` | `boolean` | 멀티캐스트 확인 | 그룹 통신 구현 |

### 🎯 핵심 사용 패턴

#### 1. **기본 DNS 조회**
```java
InetAddress google = InetAddress.getByName("www.google.com");
System.out.println("Google IP: " + google.getHostAddress());
// 결과: Google IP: 142.250.199.68
```

#### 2. **서버 연결 테스트**
```java
InetAddress server = InetAddress.getByName("8.8.8.8");
long startTime = System.currentTimeMillis();
boolean reachable = server.isReachable(3000);
long responseTime = System.currentTimeMillis() - startTime;

if (reachable) {
    System.out.println("서버 응답: " + responseTime + "ms");
} else {
    System.out.println("서버 연결 실패");
}
```

#### 3. **로드밸런싱용 서버 목록 조회**
```java
InetAddress[] servers = InetAddress.getAllByName("www.naver.com");
System.out.println("사용 가능한 서버 수: " + servers.length);

for (int i = 0; i < servers.length; i++) {
    System.out.println((i+1) + ". " + servers[i].getHostAddress());
}
```

#### 4. **IP 유형별 분류 시스템**
```java
public static String getIPType(InetAddress address) {
    if (address.isLoopbackAddress()) {
        return "루프백 IP (로컬 테스트용)";
    } else if (address.isSiteLocalAddress()) {
        return "사설 IP (내부 네트워크용)";
    } else if (address.isLinkLocalAddress()) {
        return "링크 로컬 IP (자동 할당)";
    } else if (address.isMulticastAddress()) {
        return "멀티캐스트 IP (그룹 통신)";
    } else {
        return "공인 IP (인터넷 서비스용)";
    }
}
```

#### 5. **시스템 환경 분석**
```java
InetAddress localhost = InetAddress.getLocalHost();
String myIP = localhost.getHostAddress();
String myHostname = localhost.getHostName();

System.out.println("내 IP: " + myIP);
System.out.println("컴퓨터명: " + myHostname);
System.out.println("IP 유형: " + getIPType(localhost));

// 네트워크 주소 계산 (/24 가정)
String[] parts = myIP.split("\\.");
String networkAddr = parts[0] + "." + parts[1] + "." + parts[2] + ".0/24";
System.out.println("네트워크: " + networkAddr);
```

---

## 🚀 실무 활용 시나리오

### 💼 1. 서버 상태 모니터링
```java
// 주요 서버들의 응답 시간 측정
String[] servers = {"8.8.8.8", "1.1.1.1", "168.126.63.1"};

for (String serverIP : servers) {
    InetAddress server = InetAddress.getByName(serverIP);
    long responseTime = measureResponseTime(server);
    
    if (responseTime > 0) {
        System.out.println(serverIP + ": " + responseTime + "ms ✅");
    } else {
        System.out.println(serverIP + ": 연결 실패 ❌");
    }
}
```

### 💼 2. 최적 DNS 서버 선택
```java
String[] dnsServers = {"8.8.8.8", "1.1.1.1", "168.126.63.1"};
String fastestDNS = null;
long bestTime = Long.MAX_VALUE;

for (String dns : dnsServers) {
    long responseTime = testDNSResponse(dns);
    if (responseTime > 0 && responseTime < bestTime) {
        bestTime = responseTime;
        fastestDNS = dns;
    }
}

System.out.println("권장 DNS: " + fastestDNS + " (" + bestTime + "ms)");
```

### 💼 3. 네트워크 보안 분석
```java
public static void analyzeNetworkSecurity(String targetIP) {
    InetAddress target = InetAddress.getByName(targetIP);
    
    if (target.isSiteLocalAddress()) {
        System.out.println("✅ 안전: 사설 네트워크 환경");
        System.out.println("   방화벽/NAT 보호 하에 운영 중");
    } else {
        System.out.println("⚠️ 주의: 공인 IP 노출");
        System.out.println("   보안 설정 점검 필요");
    }
}
```

### 💼 4. 네트워크 진단 도구
```java
public static void networkDiagnostics() {
    try {
        // 1. 로컬 환경 분석
        InetAddress local = InetAddress.getLocalHost();
        System.out.println("내 IP: " + local.getHostAddress());
        
        // 2. 외부 연결 테스트
        boolean internetOK = InetAddress.getByName("8.8.8.8").isReachable(3000);
        System.out.println("인터넷 연결: " + (internetOK ? "정상" : "문제"));
        
        // 3. DNS 해결 테스트
        InetAddress google = InetAddress.getByName("www.google.com");
        System.out.println("DNS 해결: 정상 (" + google.getHostAddress() + ")");
        
    } catch (Exception e) {
        System.out.println("네트워크 문제 발견: " + e.getMessage());
    }
}
```

---

## 🔧 서브넷 마스크와 네트워크 계산

### 🎯 서브넷 마스크 기본 개념
```java
// 서브넷 마스크 = IP 주소를 네트워크 부분과 호스트 부분으로 나누는 기준
IP 주소:      192.168.10.100
서브넷 마스크: 255.255.255.0 (/24)

// 비트 레벨 계산
IP:     11000000.10101000.00001010.01100100
Mask:   11111111.11111111.11111111.00000000
────────────────────────────────────────────
Network: 11000000.10101000.00001010.00000000 = 192.168.10.0
```

### 🧮 핵심 계산 공식
```java
// 1. 네트워크 주소 = IP AND 서브넷마스크
// 2. 호스트 주소 = IP XOR 네트워크주소  
// 3. 브로드캐스트 주소 = 네트워크 OR (NOT 서브넷마스크)
// 4. 사용 가능한 호스트 = 2^호스트비트 - 2

// CIDR /24 예시
네트워크 주소: 192.168.10.0
첫 번째 호스트: 192.168.10.1
마지막 호스트: 192.168.10.254
브로드캐스트: 192.168.10.255
사용 가능한 호스트: 254개
```

---

## ⚠️ 주의사항 및 실무 팁

### 🚨 일반적인 실수들
```java
// ❌ 잘못된 사용
InetAddress.getByName("invalid-domain"); // 예외 처리 없음
address.getAddress();                    // byte[] 배열을 문자열로 착각
address.isReachable(10000);             // 너무 긴 타임아웃

// ✅ 올바른 사용  
try {
    InetAddress addr = InetAddress.getByName("www.google.com");
    String readableIP = addr.getHostAddress(); // 문자열로 변환
    boolean reachable = addr.isReachable(3000); // 적절한 타임아웃
} catch (Exception e) {
    System.err.println("네트워크 오류: " + e.getMessage());
}
```

### 💡 성능 최적화 팁
```java
// 1. DNS 조회 결과 캐싱
Map<String, InetAddress> dnsCache = new HashMap<>();

// 2. 타임아웃 설정 최적화
addr.isReachable(3000); // 3초면 충분

// 3. 병렬 처리로 여러 서버 동시 테스트
servers.parallelStream()
    .forEach(server -> testConnection(server));
```

### 🔒 보안 고려사항
```java
// 1. 사설 IP 확인 후 외부 접근 차단
if (!target.isSiteLocalAddress()) {
    throw new SecurityException("공인 IP 접근 시도 감지");
}

// 2. 입력값 검증
if (!isValidIPFormat(userInput)) {
    throw new IllegalArgumentException("잘못된 IP 형식");
}

// 3. 연결 실패 시 재시도 제한
int maxRetries = 3;
for (int i = 0; i < maxRetries; i++) {
    if (server.isReachable(timeout)) break;
    Thread.sleep(1000); // 1초 대기 후 재시도
}
```

---

## 개념 확인 체크리스트

### ✅ 기본 개념
- [ ] IP 주소 = 네트워크상 컴퓨터 주소 (아파트 번호)
- [ ] 포트 = 컴퓨터 내 프로그램 번호 (호수)
- [ ] 사설 IP vs 공인 IP 구분 (192.168.x.x vs 8.8.8.8)
- [ ] InetAddress 객체 생성 방법 (getByName)

### ✅ 핵심 메서드
- [ ] getByName("google.com") - DNS 조회
- [ ] getHostAddress() - IP 문자열 얻기
- [ ] isReachable(3000) - 서버 연결 테스트
- [ ] isSiteLocalAddress() - 사설 IP 확인

### ✅ IP 유형 구분
- [ ] 사설 IP: 192.168.x.x, 10.x.x.x (내부 네트워크)
- [ ] 공인 IP: 8.8.8.8, 1.1.1.1 (인터넷 서비스)
- [ ] 특수 IP: 127.0.0.1 (localhost), 169.254.x.x (자동 할당)

### ✅ 실무 기초
- [ ] DNS 서버 응답 시간 측정
- [ ] 네트워크 연결 상태 확인
- [ ] 서버 목록에서 최적 서버 선택
- [ ] 예외 처리와 타임아웃 설정

