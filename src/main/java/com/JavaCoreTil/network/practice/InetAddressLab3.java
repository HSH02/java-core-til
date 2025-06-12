package com.JavaCoreTil.network.practice;

import java.net.*;
import java.util.*;

/**
 * 🎯 InetAddress 고급 실습 - 3단계
 * 
 * 📋 학습 목표:
 * - 사설 IP vs 공인 IP 완벽 구분하기
 * - 다양한 IP 주소 유형 판별 방법 마스터
 * - 네트워크 환경 자동 분석 시스템 구축
 * - 실무에서 사용하는 IP 주소 관리 패턴
 * 
 * 🔑 핵심 개념:
 * - isSiteLocalAddress(): 사설 IP 판별 (192.168.x.x, 10.x.x.x, 172.16-31.x.x)
 * - isLoopbackAddress(): 루프백 주소 확인 (127.x.x.x)
 * - isLinkLocalAddress(): 링크 로컬 주소 확인 (169.254.x.x)
 * - isMulticastAddress(): 멀티캐스트 주소 확인 (224-239.x.x.x)
 * 
 * 완성 후 실행 결과 예시:
 * ================================
 * 🔍 네트워크 환경 분석기
 * ================================
 * 
 * 📍 내 컴퓨터 네트워크 환경:
 * - 내부 IP: 192.168.1.100 (사설 IP)
 * - 컴퓨터명: DESKTOP-ABC123
 * - 네트워크 유형: 일반 가정/사무실 환경
 * 
 * 🌐 외부 IP 주소 분석:
 * - Google DNS: 8.8.8.8 (공인 IP) ✅
 * - 루프백: 127.0.0.1 (로컬 IP) 🏠
 * - 사설망: 192.168.1.1 (사설 IP) 🏢
 * - 링크로컬: 169.254.1.1 (자동할당 IP) ⚠️
 * 
 * 📊 IP 주소 유형별 분류:
 * ┌──────────────────┬─────────────────┬──────────────┐
 * │    IP 주소       │      유형       │    용도      │
 * ├──────────────────┼─────────────────┼──────────────┤
 * │ 192.168.1.100    │ 사설 IP        │ 내부 네트워크 │
 * │ 8.8.8.8          │ 공인 IP        │ 인터넷 서비스 │
 * │ 127.0.0.1        │ 루프백         │ 로컬 테스트   │
 * │ 169.254.1.1      │ 링크 로컬      │ 자동 할당     │
 * └──────────────────┴─────────────────┴──────────────┘
 * 
 * 🎯 네트워크 보안 권고사항:
 * - 사설 IP 발견: 방화벽/NAT 환경에서 안전하게 운영 중
 * - 공인 IP 노출: 직접 인터넷 연결, 보안 설정 점검 필요
 * ================================
 */
public class InetAddressLab3 {
    
    // 분석할 다양한 IP 주소 예시
    private static final String[] TEST_IPS = {
        "192.168.1.100",    // 사설 IP (Class C)
        "10.0.0.1",         // 사설 IP (Class A)  
        "172.16.0.1",       // 사설 IP (Class B)
        "8.8.8.8",          // 공인 IP (Google DNS)
        "127.0.0.1",        // 루프백 주소
        "169.254.1.1",      // 링크 로컬 주소
        "224.0.0.1",        // 멀티캐스트 주소
        "www.google.com",   // 도메인 (공인 IP로 변환됨)
        "localhost"         // 로컬호스트 (루프백으로 변환됨)
    };
    
    public static void main(String[] args) {
        System.out.println("================================");
        System.out.println("🔍 네트워크 환경 분석기");
        System.out.println("================================\n");
        
        try {
            // ============================================
            // 📍 1단계: 내 컴퓨터 네트워크 환경 분석
            // ============================================
            analyzeLocalEnvironment();
            
            // ============================================
            // 🌐 2단계: 다양한 IP 주소 유형 분석
            // ============================================
            System.out.println("\n🌐 IP 주소 유형별 상세 분석:\n");
            
            List<IPAnalysisResult> results = new ArrayList<>();
            
            for (String ipString : TEST_IPS) {
                IPAnalysisResult result = analyzeIPAddress(ipString);
                if (result != null) {
                    results.add(result);
                    displayIPAnalysis(result);
                }
            }
            
            // ============================================
            // 📊 3단계: 분류 결과 테이블로 정리
            // ============================================
            displaySummaryTable(results);
            
            // ============================================
            // 🎯 4단계: 네트워크 보안 권고사항
            // ============================================
            generateSecurityRecommendations(results);
            
        } catch (Exception e) {
            System.err.println("❌ 분석 중 오류 발생: " + e.getMessage());
        }
        
        System.out.println("================================");
        System.out.println("🎉 네트워크 환경 분석 완료!");
        System.out.println("✨ 활용 팁: 정기적인 네트워크 환경 점검으로 보안 강화");
        System.out.println("================================");
    }
    
    /**
     * 🏠 로컬 컴퓨터의 네트워크 환경을 분석하는 메서드
     */
    private static void analyzeLocalEnvironment() {
        System.out.println("📍 내 컴퓨터 네트워크 환경:");
        
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            String hostIP = localhost.getHostAddress();
            String hostName = localhost.getHostName();
            
            System.out.println("- 내부 IP: " + hostIP + " (" + getIPType(localhost) + ")");
            System.out.println("- 컴퓨터명: " + hostName);
            
            // 네트워크 환경 유형 판단
            String networkType = determineNetworkType(localhost);
            System.out.println("- 네트워크 유형: " + networkType);
            
        } catch (Exception e) {
            System.out.println("- 분석 실패: " + e.getMessage());
        }
    }
    
    /**
     * 🔍 개별 IP 주소를 상세 분석하는 메서드
     */
    private static IPAnalysisResult analyzeIPAddress(String ipString) {
        try {
            InetAddress address = InetAddress.getByName(ipString);
            String resolvedIP = address.getHostAddress();
            String hostname = address.getHostName();
            String ipType = getIPType(address);
            String description = getIPDescription(address);
            String usage = getTypicalUsage(address);
            
            return new IPAnalysisResult(ipString, resolvedIP, hostname, ipType, description, usage);
            
        } catch (Exception e) {
            System.out.println("❌ " + ipString + " 분석 실패: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 🎯 IP 주소 유형을 정확히 판별하는 핵심 메서드
     */
    private static String getIPType(InetAddress address) {
        if (address.isLoopbackAddress()) {
            return "루프백 IP";
        } else if (address.isSiteLocalAddress()) {
            return "사설 IP";
        } else if (address.isLinkLocalAddress()) {
            return "링크 로컬 IP";
        } else if (address.isMulticastAddress()) {
            return "멀티캐스트 IP";
        } else {
            return "공인 IP";
        }
    }
    
    /**
     * 🏢 네트워크 환경 유형을 판단하는 메서드
     */
    private static String determineNetworkType(InetAddress address) {
        if (address.isSiteLocalAddress()) {
            String ip = address.getHostAddress();
            if (ip.startsWith("192.168.")) {
                return "일반 가정/소규모 사무실 환경 (Class C)";
            } else if (ip.startsWith("10.")) {
                return "대기업/대형 조직 환경 (Class A)";
            } else if (ip.startsWith("172.")) {
                return "중간 규모 조직 환경 (Class B)";
            }
        } else if (address.isLoopbackAddress()) {
            return "로컬 개발/테스트 환경";
        } else {
            return "직접 인터넷 연결 환경 (공인 IP)";
        }
        return "알 수 없는 환경";
    }
    
    /**
     * 📝 IP 주소에 대한 상세 설명을 제공하는 메서드
     */
    private static String getIPDescription(InetAddress address) {
        if (address.isLoopbackAddress()) {
            return "자기 자신을 가리키는 특수 주소";
        } else if (address.isSiteLocalAddress()) {
            return "내부 네트워크에서만 사용되는 주소";
        } else if (address.isLinkLocalAddress()) {
            return "DHCP 실패 시 자동으로 할당되는 주소";
        } else if (address.isMulticastAddress()) {
            return "다중 수신자에게 동시 전송하는 주소";
        } else {
            return "인터넷에서 직접 접근 가능한 주소";
        }
    }
    
    /**
     * 💼 IP 유형별 일반적인 사용 용도를 설명하는 메서드
     */
    private static String getTypicalUsage(InetAddress address) {
        if (address.isLoopbackAddress()) {
            return "로컬 테스트, 개발";
        } else if (address.isSiteLocalAddress()) {
            return "내부 네트워크";
        } else if (address.isLinkLocalAddress()) {
            return "자동 할당";
        } else if (address.isMulticastAddress()) {
            return "그룹 통신";
        } else {
            return "인터넷 서비스";
        }
    }
    
    /**
     * 📺 개별 IP 분석 결과를 보기 좋게 출력하는 메서드
     */
    private static void displayIPAnalysis(IPAnalysisResult result) {
        String icon = getIPIcon(result.ipType);
        System.out.printf("🔍 %s → %s (%s) %s%n", 
            result.originalInput, result.resolvedIP, result.ipType, icon);
        System.out.printf("   📝 %s%n", result.description);
        if (!result.originalInput.equals(result.hostname) && !result.resolvedIP.equals(result.hostname)) {
            System.out.printf("   🏷️  호스트명: %s%n", result.hostname);
        }
        System.out.println();
    }
    
    /**
     * 🎨 IP 유형별 아이콘을 반환하는 메서드
     */
    private static String getIPIcon(String ipType) {
        switch (ipType) {
            case "공인 IP": return "🌐";
            case "사설 IP": return "🏢";
            case "루프백 IP": return "🏠";
            case "링크 로컬 IP": return "⚠️";
            case "멀티캐스트 IP": return "📡";
            default: return "❓";
        }
    }
    
    /**
     * 📊 분석 결과를 테이블 형태로 정리해서 출력하는 메서드
     */
    private static void displaySummaryTable(List<IPAnalysisResult> results) {
        System.out.println("📊 IP 주소 유형별 분류 요약:");
        System.out.println("┌──────────────────┬─────────────────┬──────────────┐");
        System.out.println("│    IP 주소       │      유형       │    용도      │");
        System.out.println("├──────────────────┼─────────────────┼──────────────┤");
        
        for (IPAnalysisResult result : results) {
            System.out.printf("│ %-16s │ %-15s │ %-12s │%n", 
                result.resolvedIP, result.ipType, result.usage);
        }
        
        System.out.println("└──────────────────┴─────────────────┴──────────────┘\n");
    }
    
    /**
     * 🛡️ 네트워크 보안 권고사항을 생성하는 메서드
     */
    private static void generateSecurityRecommendations(List<IPAnalysisResult> results) {
        System.out.println("🛡️ 네트워크 보안 분석 및 권고사항:");
        
        boolean hasPrivateIP = results.stream().anyMatch(r -> r.ipType.equals("사설 IP"));
        boolean hasPublicIP = results.stream().anyMatch(r -> r.ipType.equals("공인 IP"));
        boolean hasLinkLocal = results.stream().anyMatch(r -> r.ipType.equals("링크 로컬 IP"));
        
        if (hasPrivateIP) {
            System.out.println("✅ 사설 IP 환견: 방화벽/NAT 보호 하에 안전하게 운영 중");
        }
        
        if (hasPublicIP) {
            System.out.println("⚠️ 공인 IP 노출: 직접 인터넷 연결, 보안 설정 점검 권장");
            System.out.println("   💡 권장사항: 방화벽 활성화, 불필요한 포트 차단");
        }
        
        if (hasLinkLocal) {
            System.out.println("🔧 링크 로컬 IP 발견: DHCP 설정 확인 필요");
            System.out.println("   💡 해결방법: 네트워크 설정 재구성 또는 DHCP 서버 점검");
        }
        
        System.out.println("\n🎯 실무 활용 팁:");
        System.out.println("- 정기적인 네트워크 환경 점검으로 보안 취약점 사전 발견");
        System.out.println("- IP 주소 유형별 적절한 보안 정책 적용");
        System.out.println("- 사설 IP 환경에서는 포트 포워딩 설정 주의");
    }
    
    /**
     * 📋 IP 분석 결과를 저장하는 데이터 클래스
     */
    private static class IPAnalysisResult {
        final String originalInput;
        final String resolvedIP;
        final String hostname;
        final String ipType;
        final String description;
        final String usage;
        
        IPAnalysisResult(String originalInput, String resolvedIP, String hostname, 
                        String ipType, String description, String usage) {
            this.originalInput = originalInput;
            this.resolvedIP = resolvedIP;
            this.hostname = hostname;
            this.ipType = ipType;
            this.description = description;
            this.usage = usage;
        }
    }
} 