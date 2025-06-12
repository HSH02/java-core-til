package com.JavaCoreTil.network.practice;

import java.net.InetAddress;
import java.util.*;

/**
 * 🎯 InetAddress 종합 시험 과제
 *
 * 📋 과제 목표:
 * - InetAddress 클래스 완전 이해도 측정
 * - IP 주소 유형 판별 능력 검증
 * - 네트워크 연결 테스트 및 성능 분석
 * - 실무 수준의 네트워크 진단 기능 구현
 *
 * ⚠️ 규칙:
 * - 힌트나 TODO 주석 없음
 * - 순수하게 본인 실력으로 구현
 * - 모든 메서드를 직접 작성
 * - 예외 처리 필수
 *
 * 완성 후 실행 결과:
 * ================================
 * 🌐 네트워크 진단 시스템
 * ================================
 *
 * 📍 시스템 환경 분석:
 * - 로컬 IP: 192.168.1.100
 * - 호스트명: DESKTOP-ABC123
 * - IP 유형: 사설 IP (Class C)
 * - 네트워크: 192.168.1.0/24
 *
 * 🔍 DNS 서버 성능 테스트:
 * Google DNS (8.8.8.8): 12ms ✅
 * Cloudflare (1.1.1.1): 25ms ✅
 * KT DNS (168.126.63.1): 8ms ✅
 *
 * 🏆 최적 DNS: KT DNS (8ms)
 *
 * 📊 IP 주소 분석 결과:
 * ┌─────────────────┬──────────────┬────────────┬──────────┐
 * │   대상 주소     │   해석된 IP   │   IP 유형   │ 연결상태  │
 * ├─────────────────┼──────────────┼────────────┼──────────┤
 * │ www.google.com  │ 142.250.199.68│   공인 IP   │    ✅    │
 * │ 192.168.1.1     │ 192.168.1.1   │   사설 IP   │    ✅    │
 * │ 127.0.0.1       │ 127.0.0.1     │  루프백 IP  │    ✅    │
 * │ 10.0.0.1        │ 10.0.0.1      │   사설 IP   │    ❌    │
 * │ 8.8.8.8         │ 8.8.8.8       │   공인 IP   │    ✅    │
 * └─────────────────┴──────────────┴────────────┴──────────┘
 *
 * 🎯 네트워크 환경 요약:
 * - 총 테스트 대상: 5개
 * - 연결 성공: 4개 (80%)
 * - 연결 실패: 1개 (20%)
 * - 사설 IP 비율: 40%
 * - 공인 IP 비율: 40%
 * - 특수 IP 비율: 20%
 *
 * 💡 권장사항:
 * - 최적 DNS 서버: 168.126.63.1 사용 권장
 * - 네트워크 상태: 양호 (80% 연결 성공)
 * - 보안 상태: 사설 네트워크 환경으로 안전
 * ================================
 */
public class InetAddressExam {

    // 테스트할 주소 목록
    private static final String[] TEST_ADDRESSES = {
        "www.google.com",
        "192.168.1.1",
        "127.0.0.1",
        "10.0.0.1",
        "8.8.8.8"
    };

    // DNS 서버 목록 [이름, IP]
    private static final String[][] DNS_SERVERS = {
        {"Google DNS", "8.8.8.8"},
        {"Cloudflare", "1.1.1.1"},
        {"KT DNS", "168.126.63.1"}
    };

    // 전역 변수로 통계 데이터 저장
    private static String bestDNS = "";
    private static long bestDNSTime = Long.MAX_VALUE;
    private static List<AddressResult> addressResults = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("================================");
        System.out.println("🌐 네트워크 진단 시스템");
        System.out.println("================================\n");

        try {
            // 1단계: 시스템 환경 분석
            analyzeSystemEnvironment();

            // 2단계: DNS 성능 테스트
            testDNSPerformance();

            // 3단계: IP 주소 분석
            analyzeIPAddresses();

            // 4단계: 결과 요약
            generateSummary();

        } catch (Exception e) {
            System.err.println("❌ 진단 중 오류: " + e.getMessage());
        }

        System.out.println("================================");
    }

    /**
     * 시스템 환경을 분석하는 메서드
     * 출력해야 할 정보:
     * - 로컬 IP 주소
     * - 호스트명
     * - IP 유형 (사설/공인/루프백 등)
     * - 네트워크 주소 (/24 서브넷 가정)
     */
    private static void analyzeSystemEnvironment() throws Exception{
        System.out.println("📍 시스템 환경 분석:");
        InetAddress ip = InetAddress.getLocalHost();

        System.out.println("- 로컬 IP: " + ip.getHostAddress());
        System.out.println("- 호스트명: " + ip.getHostName());

        System.out.print("- IP 유형: ");

        String ipType = "";
        if(ip.isLoopbackAddress()) {
            ipType = "루프백 IP";
        } else if(ip.isSiteLocalAddress()){
            ipType = "사설 IP";
            // Class 판별
            String ipStr = ip.getHostAddress();
            if (ipStr.startsWith("192.168.")) {
                ipType += " (Class C)";
            } else if (ipStr.startsWith("10.")) {
                ipType += " (Class A)";
            } else if (ipStr.startsWith("172.")) {
                ipType += " (Class B)";
            }
        } else {
            ipType = "공인 IP";
        }
        System.out.println(ipType);

        // 네트워크 주소 계산 (/24 가정)
        String ipStr = ip.getHostAddress();
        String[] parts = ipStr.split("\\.");
        String networkAddress = parts[0] + "." + parts[1] + "." + parts[2] + ".0/24";
        System.out.println("- 네트워크: " + networkAddress);

        System.out.println();
    }

    /**
     * DNS 서버들의 응답 시간을 테스트하는 메서드
     * 출력해야 할 정보:
     * - 각 DNS 서버별 응답 시간
     * - 연결 성공/실패 상태
     * - 가장 빠른 DNS 서버
     */
    private static void testDNSPerformance() {
        System.out.println("DNS 서버 성능 테스트:");
        
        for (String[] server : DNS_SERVERS) {
            String name = server[0];
            String ip = server[1];
            
            try {
                InetAddress dnsServer = InetAddress.getByName(ip);
                long startTime = System.currentTimeMillis();
                boolean reachable = dnsServer.isReachable(3000);
                long responseTime = System.currentTimeMillis() - startTime;
                
                if (reachable) {
                    System.out.println(name + " (" + ip + "): " + responseTime + "ms ✅");
                    
                    // 최적 DNS 서버 업데이트
                    if (responseTime < bestDNSTime) {
                        bestDNSTime = responseTime;
                        bestDNS = name + " (" + responseTime + "ms)";
                    }
                } else {
                    System.out.println(name + " (" + ip + "): 연결 실패 ❌");
                }
                
            } catch (Exception e) {
                System.out.println(name + " (" + ip + "): 오류 ❌");
            }
        }
        
        if (!bestDNS.isEmpty()) {
            System.out.println("\n🏆 최적 DNS: " + bestDNS);
        }
        
        System.out.println();
    }

    /**
     * 주어진 IP 주소들을 분석하는 메서드
     * 출력해야 할 정보:
     * - 테이블 형태로 정리된 분석 결과
     * - 대상 주소, 해석된 IP, IP 유형, 연결 상태
     */
    private static void analyzeIPAddresses() {
        System.out.println("📊 IP 주소 분석 결과:");
        
        // 테이블 헤더
        System.out.println("┌─────────────────┬──────────────┬────────────┬──────────┐");
        System.out.println("│   대상 주소     │   해석된 IP   │   IP 유형   │ 연결상태  │");
        System.out.println("├─────────────────┼──────────────┼────────────┼──────────┤");
        
        for (String address : TEST_ADDRESSES) {
            try {
                InetAddress target = InetAddress.getByName(address);
                String resolvedIP = target.getHostAddress();
                
                // IP 유형 판별
                String ipType = "";
                if (target.isLoopbackAddress()) {
                    ipType = "루프백 IP";
                } else if (target.isSiteLocalAddress()) {
                    ipType = "사설 IP";
                } else {
                    ipType = "공인 IP";
                }
                
                // 연결 테스트
                boolean reachable = target.isReachable(2000);
                String status = reachable ? "✅" : "❌";
                
                // 결과 저장
                addressResults.add(new AddressResult(address, resolvedIP, ipType, reachable));
                
                // 테이블 출력 (포맷팅)
                System.out.printf("│ %-15s │ %-12s │ %-10s │    %-4s │%n",
                    address, resolvedIP, ipType, status);
                
            } catch (Exception e) {
                System.out.printf("│ %-15s │ %-12s │ %-10s │    %-4s │%n", 
                    address, "해석 실패", "알 수 없음", "❌");
                addressResults.add(new AddressResult(address, "해석 실패", "알 수 없음", false));
            }
        }
        
        System.out.println("└─────────────────┴──────────────┴────────────┴──────────┘");
        System.out.println();
    }

    /**
     * 전체 분석 결과를 요약하는 메서드
     * 출력해야 할 정보:
     * - 총 테스트 대상 수
     * - 연결 성공/실패 개수 및 비율
     * - IP 유형별 비율 (사설/공인/특수)
     * - 권장사항 (최적 DNS, 네트워크 상태, 보안 상태)
     */
    private static void generateSummary() {
        System.out.println("🎯 네트워크 환경 요약:");
        
        int totalTests = addressResults.size();
        int successCount = 0;
        int privateCount = 0;
        int publicCount = 0;
        int specialCount = 0;
        
        for (AddressResult result : addressResults) {
            if (result.isReachable) successCount++;
            
            if (result.ipType.contains("사설")) {
                privateCount++;
            } else if (result.ipType.contains("공인")) {
                publicCount++;
            } else {
                specialCount++;
            }
        }
        
        int failCount = totalTests - successCount;
        int successRate = (successCount * 100) / totalTests;
        int failRate = 100 - successRate;
        int privateRate = (privateCount * 100) / totalTests;
        int publicRate = (publicCount * 100) / totalTests;
        int specialRate = (specialCount * 100) / totalTests;
        
        System.out.println("- 총 테스트 대상: " + totalTests + "개");
        System.out.println("- 연결 성공: " + successCount + "개 (" + successRate + "%)");
        System.out.println("- 연결 실패: " + failCount + "개 (" + failRate + "%)");
        System.out.println("- 사설 IP 비율: " + privateRate + "%");
        System.out.println("- 공인 IP 비율: " + publicRate + "%");
        System.out.println("- 특수 IP 비율: " + specialRate + "%");

        System.out.println("\n💡 권장사항:");
        
        if (!bestDNS.isEmpty()) {
            String dnsIP = bestDNS.contains("KT") ? "168.126.63.1" : 
                          bestDNS.contains("Google") ? "8.8.8.8" : "1.1.1.1";
            System.out.println("- 최적 DNS 서버: " + dnsIP + " 사용 권장");
        }
        
        if (successRate >= 80) {
            System.out.println("- 네트워크 상태: 양호 (" + successRate + "% 연결 성공)");
        } else if (successRate >= 60) {
            System.out.println("- 네트워크 상태: 보통 (" + successRate + "% 연결 성공)");
        } else {
            System.out.println("- 네트워크 상태: 불량 (" + successRate + "% 연결 성공)");
        }
        
        if (privateRate > 0) {
            System.out.println("- 보안 상태: 사설 네트워크 환경으로 안전");
        } else {
            System.out.println("- 보안 상태: 공인 IP 환경, 보안 설정 점검 필요");
        }
    }
    
    // 분석 결과를 저장하는 클래스
    private static class AddressResult {
        String originalAddress;
        String resolvedIP;
        String ipType;
        boolean isReachable;
        
        AddressResult(String originalAddress, String resolvedIP, String ipType, boolean isReachable) {
            this.originalAddress = originalAddress;
            this.resolvedIP = resolvedIP;
            this.ipType = ipType;
            this.isReachable = isReachable;
        }
    }
} 