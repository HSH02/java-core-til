package com.JavaCoreTil.network.practice;

import java.net.*;
import java.util.*;

/**
 * 🎯 InetAddress 중급 실습 - 2단계
 * 
 * 📋 학습 목표:
 * - 여러 서버의 응답 시간을 정확히 측정하기
 * - 반복 측정을 통한 평균값 계산 방법
 * - 성능 기반 최적 서버 선택 알고리즘
 * - 실무에서 사용하는 서버 모니터링 패턴
 * 
 * 🔑 핵심 개념:
 * - isReachable()의 정확한 응답 시간 측정법
 * - 네트워크 지연시간 변동성 고려한 평균 계산
 * - DNS 서버별 성능 비교 분석
 * - 예외 처리를 통한 안정적인 측정
 * 
 * 완성 후 실행 결과 예시:
 * ================================
 * 🚀 서버 응답 속도 측정기
 * ================================
 * 
 * 📊 DNS 서버 응답 시간 측정 중...
 * 
 * 🔍 Google DNS (8.8.8.8)
 * - 1차 측정: 12ms
 * - 2차 측정: 8ms  
 * - 3차 측정: 15ms
 * - 평균 응답시간: 11.7ms
 * 
 * 🔍 Cloudflare DNS (1.1.1.1)
 * - 1차 측정: 25ms
 * - 2차 측정: 22ms
 * - 3차 측정: 28ms  
 * - 평균 응답시간: 25.0ms
 * 
 * 🔍 KT DNS (168.126.63.1)
 * - 1차 측정: 5ms
 * - 2차 측정: 7ms
 * - 3차 측정: 6ms
 * - 평균 응답시간: 6.0ms
 * 
 * 🏆 가장 빠른 DNS 서버: KT DNS (168.126.63.1) - 6.0ms
 * ================================
 */
public class InetAddressLab2 {
    
    // 테스트할 DNS 서버 목록 (이름, IP 주소)
    private static final String[][] DNS_SERVERS = {
        {"Google DNS", "8.8.8.8"},
        {"Cloudflare DNS", "1.1.1.1"}, 
        {"KT DNS", "168.126.63.1"}
    };
    
    // 측정 횟수 (정확도 향상을 위해)
    private static final int MEASUREMENT_COUNT = 3;
    
    public static void main(String[] args) {
        System.out.println("================================");
        System.out.println("🚀 서버 응답 속도 측정기");
        System.out.println("================================\n");
        
        System.out.println("📊 DNS 서버 응답 시간 측정 중...\n");
        
        // 최고 성능 서버 추적용 변수
        String fastestServerName = null;
        String fastestServerIP = null;
        double bestAverageTime = Double.MAX_VALUE;
        
        // ============================================
        // 📊 각 DNS 서버별 성능 측정
        // ============================================
        for (String[] serverInfo : DNS_SERVERS) {
            String serverName = serverInfo[0];
            String serverIP = serverInfo[1];
            
            System.out.println("🔍 " + serverName + " (" + serverIP + ")");
            
            // 여러 번 측정해서 평균 계산
            List<Long> responseTimes = new ArrayList<>();
            
            for (int i = 1; i <= MEASUREMENT_COUNT; i++) {
                long responseTime = measureResponseTime(serverIP);
                
                if (responseTime >= 0) {
                    System.out.println("- " + i + "차 측정: " + responseTime + "ms");
                    responseTimes.add(responseTime);
                } else {
                    System.out.println("- " + i + "차 측정: 연결 실패");
                }
                
                // 측정 간 잠시 대기 (네트워크 부하 방지)
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            
            // 평균 응답시간 계산
            if (!responseTimes.isEmpty()) {
                double averageTime = responseTimes.stream()
                    .mapToLong(Long::longValue)
                    .average()
                    .orElse(-1.0);
                
                System.out.printf("- 평균 응답시간: %.1fms%n", averageTime);
                
                // 최고 성능 서버 업데이트
                if (averageTime < bestAverageTime) {
                    bestAverageTime = averageTime;
                    fastestServerName = serverName;
                    fastestServerIP = serverIP;
                }
            } else {
                System.out.println("- 평균 응답시간: 측정 불가 (모든 시도 실패)");
            }
            
            System.out.println(); // 서버 간 구분선
        }
        
        // ============================================
        // 🏆 최종 결과 발표
        // ============================================
        System.out.println("================================");
        if (fastestServerName != null) {
            System.out.printf("🏆 가장 빠른 DNS 서버: %s (%s) - %.1fms%n", 
                fastestServerName, fastestServerIP, bestAverageTime);
            
            // 실무 팁 제공
            System.out.println("\n💡 실무 활용 팁:");
            System.out.println("- 이 서버를 시스템 DNS로 설정하면 인터넷 속도 향상");
            System.out.println("- 정기적으로 측정해서 최적 서버 유지 관리");
        } else {
            System.out.println("❌ 측정 가능한 DNS 서버가 없습니다.");
            System.out.println("💡 해결 방법: 네트워크 연결 상태 확인");
        }
        System.out.println("================================");
    }
    
    /**
     * 🎯 특정 IP 주소의 응답 시간을 정확히 측정하는 메서드
     * 
     * 학습 포인트:
     * - System.currentTimeMillis()로 정밀한 시간 측정
     * - try-catch로 네트워크 오류 안전하게 처리
     * - 타임아웃 설정으로 무한 대기 방지
     * 
     * @param ipAddress 측정할 IP 주소
     * @return 응답 시간 (밀리초), 연결 실패 시 -1
     */
    private static long measureResponseTime(String ipAddress) {
        try {
            // InetAddress 객체 생성
            InetAddress target = InetAddress.getByName(ipAddress);
            
            // 시작 시간 기록 (측정 시작점)
            long startTime = System.currentTimeMillis();
            
            // 실제 연결 테스트 (3초 타임아웃)
            boolean reachable = target.isReachable(3000);
            
            // 끝 시간 기록 (측정 종료점)
            long endTime = System.currentTimeMillis();
            
            // 연결 성공 시에만 응답 시간 반환
            if (reachable) {
                return endTime - startTime;
            } else {
                return -1; // 타임아웃 또는 연결 실패
            }
            
        } catch (Exception e) {
            // 네트워크 오류, DNS 해결 실패 등
            return -1;
        }
    }
}

