package com.JavaCoreTil.network.practice;

import java.net.*;

/**
 * 🎯 InetAddress 기초 실습 - 1단계
 * 
 * 📋 학습 목표:
 * - InetAddress 기본 메서드 3가지 마스터하기
 * - 로컬/원격 서버 정보 조회 방법 익히기  
 * - 네트워크 연결 테스트 및 응답 시간 측정
 * - 로드밸런싱을 위한 다중 IP 조회 방법
 * 
 * 🔑 핵심 개념:
 * - getByName(): 도메인 → IP 변환 (DNS 조회)
 * - getHostAddress(): 사람이 읽기 쉬운 IP 문자열
 * - isReachable(): 실제 네트워크 연결 테스트
 * - getAllByName(): 로드밸런싱용 다중 IP 조회
 * 
 * 완성 후 실행 결과 예시:
 * ================================
 * 🌐 네트워크 정보 조회 시스템
 * ================================
 * 
 * 📍 로컬 컴퓨터 정보:
 * - IP 주소: 192.168.1.100
 * - 컴퓨터 이름: DESKTOP-ABC123
 * 
 * 🔍 Google 서버 정보:
 * - 도메인: www.google.com
 * - IP 주소: 142.250.207.4
 * - 연결 가능: ✅ (응답시간: 15ms)
 * 
 * 📊 네이버 서버 분석:
 * - 총 IP 개수: 4개
 * - IP 목록:
 *   1. 223.130.195.200
 *   2. 223.130.195.95
 *   3. 223.130.200.107
 *   4. 223.130.200.104
 * ================================
 */
public class InetAddressLab1 {
    
    public static void main(String[] args) {
        System.out.println("================================");
        System.out.println("🌐 네트워크 정보 조회 시스템");
        System.out.println("================================\n");
        
        try {
            // ============================================
            // 📍 1단계: 로컬 컴퓨터 정보 조회
            // ============================================
            // 학습 포인트: getLocalHost()로 내 컴퓨터 IP 확인
            // 실무 활용: 서버 애플리케이션에서 자신의 IP 로깅
            System.out.println("📍 로컬 컴퓨터 정보:");
            InetAddress localhost = InetAddress.getLocalHost();
            System.out.println("- IP 주소: " + localhost.getHostAddress());
            System.out.println("- 컴퓨터 이름: " + localhost.getHostName());

            // ============================================
            // 🔍 2단계: 원격 서버 정보 조회 및 연결 테스트  
            // ============================================
            // 학습 포인트: DNS 조회 + 연결 테스트 + 응답 시간 측정
            // 실무 활용: 서버 상태 모니터링, 헬스 체크
            System.out.println("\n🔍 Google 서버 정보:");
            InetAddress googleIP = InetAddress.getByName("www.google.com");
            System.out.println("- 도메인: " + googleIP.getHostName());
            System.out.println("- IP 주소: " + googleIP.getHostAddress());
            
            // 연결 테스트 및 응답 시간 측정 (성능 모니터링 기초)
            long startTime = System.currentTimeMillis();
            boolean isReachable = googleIP.isReachable(5000); // 5초 타임아웃
            long responseTime = System.currentTimeMillis() - startTime;
            
            if (isReachable) {
                System.out.println("- 연결 가능: ✅ (응답시간: " + responseTime + "ms)");
            } else {
                System.out.println("- 연결 가능: ❌ (타임아웃)");
            }

            // ============================================
            // 📊 3단계: 로드밸런싱을 위한 다중 IP 조회
            // ============================================
            // 학습 포인트: getAllByName()으로 여러 서버 IP 조회
            // 실무 활용: 로드밸런싱, 서버 클러스터 관리
            System.out.println("\n📊 네이버 서버 분석:");
            InetAddress[] naverIPs = InetAddress.getAllByName("www.naver.com");
            System.out.println("- 총 IP 개수: " + naverIPs.length + "개");
            System.out.println("- IP 목록:");
            for (int i = 0; i < naverIPs.length; i++) {
                System.out.println("  " + (i+1) + ". " + naverIPs[i].getHostAddress());
            }
            
        } catch (Exception e) {
            System.err.println("❌ 오류 발생: " + e.getMessage());
            System.err.println("💡 해결 방법: 인터넷 연결 확인 또는 방화벽 설정 점검");
        }
        
        System.out.println("================================");
        System.out.println("🎉 InetAddress 기초 실습 완료!");
        System.out.println("✨ 다음 단계: 서버 응답 속도 측정 (Lab2)");
        System.out.println("================================");
    }
} 