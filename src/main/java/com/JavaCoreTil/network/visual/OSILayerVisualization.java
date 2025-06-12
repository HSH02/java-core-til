package com.JavaCoreTil.network.visual;

import java.io.*;
import java.net.*;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * OSI 7계층 동작 과정 시각화 데모
 * 실제 Socket 연결 시 각 계층에서 일어나는 작업들을 단계별로 보여줌
 * Google HTML 내용도 실제로 가져와서 표시
 */
public class OSILayerVisualization {
    // System.out.println vs log.info() 차이 보여주기
    private static final Logger logger = Logger.getLogger(OSILayerVisualization.class.getName());
    
    public static void main(String[] args) {
        System.out.println("=== OSI 7계층 동작 과정 시각화 ===\n");
        
        // System.out.println vs log.info() 차이 설명
        demonstrateLoggingDifference();
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("Socket 연결 시 각 계층별 작업 과정");
        System.out.println("=".repeat(50));
        
        try {
            // 실제 Socket 연결로 OSI 계층 동작 확인
            demonstrateOSILayers("www.google.com", 80);
            
        } catch (Exception e) {
            System.err.println("네트워크 연결 실패: " + e.getMessage());
        }
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("로컬 네트워크 정보 확인 (1-3계층 관련)");
        System.out.println("=".repeat(50));
        
        // 1-3계층 정보를 Java에서 최대한 확인해보기
        demonstrateLowerLayers();
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("Google HTML 내용 실제 가져오기 테스트");
        System.out.println("=".repeat(50));
        
        // Google HTML 실제로 가져와서 표시
        fetchGoogleHTML();
    }
    
    /**
     * System.out.println vs log.info() 차이 설명
     */
    private static void demonstrateLoggingDifference() {
        System.out.println("📝 System.out.println vs log.info() 차이:");
        System.out.println("┌─────────────────────────────────────────────┐");
        
        // System.out.println 사용
        System.out.println("│ System.out.println: 단순 콘솔 출력          │");
        System.out.println("│ - 로그 레벨 없음                           │");
        System.out.println("│ - 시간 정보 없음                           │");
        System.out.println("│ - 개발/테스트용으로 적합                   │");
        System.out.println("│ - 성능: 동기식, 블로킹                     │");
        
        System.out.println("├─────────────────────────────────────────────┤");
        
        // log.info() 사용 (실제 로깅)
        long startTime = System.nanoTime();
        logger.info("log.info(): 전문적인 로깅 시스템");
        long logTime = System.nanoTime() - startTime;
        
        startTime = System.nanoTime();
        System.out.println("│ - 로그 레벨 (INFO, DEBUG, ERROR 등)        │");
        long printTime = System.nanoTime() - startTime;
        
        System.out.println("│ - 자동 시간 기록                           │");
        System.out.println("│ - 파일 저장, 필터링 가능                   │");
        System.out.println("│ - 실무/운영환경에서 필수                   │");
        System.out.println("│ - 성능: 비동기 가능, 버퍼링                │");
        System.out.println("│                                             │");
        System.out.println("│ 성능 비교 (나노초):                        │");
        System.out.println("│ - Logger: " + String.format("%,d ns", logTime) + "                     │");
        System.out.println("│ - System.out: " + String.format("%,d ns", printTime) + "                │");
        System.out.println("└─────────────────────────────────────────────┘");
    }
    
    /**
     * Socket 연결을 통한 OSI 7계층 동작 과정 시연
     */
    private static void demonstrateOSILayers(String hostname, int port) throws IOException {
        System.out.println("\n🌐 " + hostname + ":" + port + " 연결 과정:\n");
        
        // 7계층: Application Layer
        System.out.println("7️⃣ Application Layer (애플리케이션 계층)");
        System.out.println("   ├─ Java 애플리케이션이 Socket 연결 요청");
        System.out.println("   └─ HTTP 프로토콜 사용 준비");
        
        // 6계층: Presentation Layer  
        System.out.println("\n6️⃣ Presentation Layer (표현 계층)");
        System.out.println("   ├─ 문자 인코딩: UTF-8 설정");
        System.out.println("   └─ 데이터 압축/암호화 준비 (HTTPS의 경우)");
        
        // 5계층: Session Layer
        System.out.println("\n5️⃣ Session Layer (세션 계층)");
        System.out.println("   ├─ TCP 세션 관리 구조 생성");
        System.out.println("   └─ 연결 상태 추적 준비");
        
        // 실제 Socket 생성 (4-1계층 작업 시작)
        System.out.println("\n🔄 Socket 생성 중...");
        long startTime = System.currentTimeMillis();
        
        Socket socket = new Socket();
        
        // 4계층: Transport Layer
        System.out.println("\n4️⃣ Transport Layer (전송 계층)");
        System.out.println("   ├─ 프로토콜: TCP 선택");
        System.out.println("   ├─ 로컬 포트: 자동 할당 (예: " + getRandomPort() + ")");
        System.out.println("   ├─ 목적지 포트: " + port);
        System.out.println("   └─ TCP 3-way 핸드셰이크 준비");
        
        // 3계층: Network Layer (DNS 조회)
        System.out.println("\n3️⃣ Network Layer (네트워크 계층)");
        System.out.print("   ├─ DNS 조회 중: " + hostname + " → ");
        InetAddress address = InetAddress.getByName(hostname);
        System.out.println(address.getHostAddress());
        System.out.println("   ├─ 라우팅 테이블 확인");
        System.out.println("   └─ 최적 경로 결정");
        
        // 실제 연결 수행
        socket.connect(new InetSocketAddress(address, port), 5000);
        long connectionTime = System.currentTimeMillis() - startTime;
        
        // 2계층: Data Link Layer
        System.out.println("\n2️⃣ Data Link Layer (데이터 링크 계층)");
        System.out.println("   ├─ ARP 프로토콜로 MAC 주소 해결");
        System.out.println("   ├─ 이더넷 프레임 생성");
        System.out.println("   └─ 로컬 네트워크 내 전송 준비");
        
        // 1계층: Physical Layer
        System.out.println("\n1️⃣ Physical Layer (물리 계층)");
        System.out.println("   ├─ 전기 신호 변환");
        System.out.println("   ├─ 케이블/WiFi를 통한 실제 전송");
        System.out.println("   └─ 비트 스트림 전송");
        
        // 연결 완료 정보
        System.out.println("\n✅ 연결 완료!");
        System.out.println("   ├─ 연결 시간: " + connectionTime + "ms");
        System.out.println("   ├─ 로컬 주소: " + socket.getLocalAddress().getHostAddress() + ":" + socket.getLocalPort());
        System.out.println("   └─ 원격 주소: " + socket.getRemoteSocketAddress());
        
        socket.close();
        System.out.println("   연결 종료 완료");
    }
    
    /**
     * 1-3계층 정보를 Java에서 최대한 확인
     */
    private static void demonstrateLowerLayers() {
        System.out.println("\n🔍 Java에서 확인 가능한 하위 계층 정보:\n");
        
        try {
            // 3계층: 네트워크 인터페이스 정보
            System.out.println("3️⃣ Network Layer 관련 정보:");
            System.out.println("   ├─ 로컬 IP 주소들:");
            
            NetworkInterface.getNetworkInterfaces().asIterator().forEachRemaining(netInterface -> {
                try {
                    if (netInterface.isUp() && !netInterface.isLoopback()) {
                        System.out.println("   │  ├─ " + netInterface.getDisplayName());
                        netInterface.getInetAddresses().asIterator().forEachRemaining(addr -> {
                            if (addr instanceof Inet4Address) {
                                System.out.println("   │  │  └─ IPv4: " + addr.getHostAddress());
                            }
                        });
                    }
                } catch (SocketException e) {
                    // 무시
                }
            });
            
            // 2계층: MAC 주소 정보
            System.out.println("\n2️⃣ Data Link Layer 관련 정보:");
            System.out.println("   ├─ MAC 주소들:");
            
            NetworkInterface.getNetworkInterfaces().asIterator().forEachRemaining(netInterface -> {
                try {
                    byte[] mac = netInterface.getHardwareAddress();
                    if (mac != null && netInterface.isUp() && !netInterface.isLoopback()) {
                        StringBuilder macStr = new StringBuilder();
                        for (int i = 0; i < mac.length; i++) {
                            macStr.append(String.format("%02X", mac[i]));
                            if (i < mac.length - 1) macStr.append(":");
                        }
                        System.out.println("   │  ├─ " + netInterface.getDisplayName() + ": " + macStr);
                    }
                } catch (SocketException e) {
                    // 무시
                }
            });
            
            // 1계층: 물리적 정보 (Java에서는 제한적)
            System.out.println("\n1️⃣ Physical Layer 관련 정보:");
            System.out.println("   ├─ Java에서 직접 확인 불가능한 정보들:");
            System.out.println("   │  ├─ 케이블 연결 상태");
            System.out.println("   │  ├─ 신호 강도");
            System.out.println("   │  └─ 전송 속도");
            System.out.println("   └─ 💡 이런 정보는 OS 명령어로 확인:");
            System.out.println("      ├─ Windows: ipconfig /all");
            System.out.println("      └─ Linux/Mac: ifconfig 또는 ip addr");
            
        } catch (SocketException e) {
            System.err.println("네트워크 인터페이스 정보 조회 실패: " + e.getMessage());
        }
        
        System.out.println("\n📋 정리:");
        System.out.println("   ├─ Java는 주로 3-7계층을 다룸");
        System.out.println("   ├─ 1-2계층은 OS와 하드웨어가 처리");
        System.out.println("   └─ 네트워크 도구(Wireshark 등)로 전체 계층 분석 가능");
    }
    
    /**
     * Google HTML 실제로 가져와서 표시
     */
    private static void fetchGoogleHTML() {
        try {
            System.out.println("\n🌐 Google HTML 실제 가져오기 테스트:");
            
            URL url = new URL("http://www.google.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            
            // User-Agent 설정 (일부 사이트에서 필요)
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Java OSI Demo)");
            
            int responseCode = connection.getResponseCode();
            System.out.println("   ├─ HTTP 응답 코드: " + responseCode);
            System.out.println("   ├─ Content-Type: " + connection.getContentType());
            System.out.println("   └─ Content-Length: " + connection.getContentLength());
            
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream())
                );
                
                System.out.println("\n📄 Google HTML 내용 (처음 20줄):");
                System.out.println("┌" + "─".repeat(78) + "┐");
                
                String line;
                int lineCount = 0;
                while ((line = reader.readLine()) != null && lineCount < 20) {
                    // 긴 줄은 잘라서 표시
                    if (line.length() > 76) {
                        line = line.substring(0, 73) + "...";
                    }
                    System.out.printf("│%-76s│%n", line);
                    lineCount++;
                }
                
                System.out.println("└" + "─".repeat(78) + "┘");
                System.out.println("✅ 실제 Google 서버에서 HTML을 성공적으로 가져왔습니다!");
                
                reader.close();
            } else {
                System.out.println("❌ HTTP 요청 실패: " + responseCode);
            }
            
            connection.disconnect();
            
        } catch (Exception e) {
            System.err.println("❌ Google HTML 가져오기 실패: " + e.getMessage());
        }
    }
    
    /**
     * 랜덤 포트 번호 생성 (시연용)
     */
    private static int getRandomPort() {
        return 49152 + (int)(Math.random() * 16384); // 동적 포트 범위
    }
} 