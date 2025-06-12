package com.JavaCoreTil.network.practice;

import java.net.*;

/**
 * 🎯 서브넷 마스크 계산기
 * 
 * 📋 학습 목표:
 * - 서브넷 마스크의 개념과 계산 원리 이해
 * - 비트 연산(AND, XOR)을 활용한 네트워크 주소 계산
 * - IP 주소의 네트워크 부분과 호스트 부분 구분
 * - 실무에서 사용하는 서브넷 설계 기초
 * 
 * 🔑 핵심 개념:
 * - 네트워크 주소 = IP 주소 AND 서브넷 마스크
 * - 호스트 주소 = IP 주소 XOR 네트워크 주소
 * - 브로드캐스트 주소 = 네트워크 주소 OR (NOT 서브넷 마스크)
 * - 사용 가능한 호스트 수 = 2^(호스트 비트 수) - 2
 * 
 * 완성 후 실행 결과 예시:
 * ================================
 * 🌐 서브넷 마스크 계산기
 * ================================
 * 
 * 📋 입력 정보:
 * - IP 주소: 192.168.10.100
 * - 서브넷 마스크: 255.255.255.0 (/24)
 * 
 * 🔍 비트 레벨 분석:
 * IP 주소:      11000000.10101000.00001010.01100100
 * 서브넷 마스크: 11111111.11111111.11111111.00000000
 * ─────────────────────────────────────────────────
 * 네트워크:     11000000.10101000.00001010.00000000
 * 
 * 📊 계산 결과:
 * - 네트워크 주소: 192.168.10.0
 * - 호스트 주소: 0.0.0.100
 * - 브로드캐스트: 192.168.10.255
 * - 첫 번째 호스트: 192.168.10.1
 * - 마지막 호스트: 192.168.10.254
 * - 사용 가능한 호스트 수: 254개
 * 
 * 🏢 네트워크 정보:
 * - 서브넷 클래스: Class C 사설망
 * - CIDR 표기법: 192.168.10.0/24
 * - 네트워크 크기: 소규모 사무실/가정용
 * ================================
 */
public class SubnetCalculator {
    
    public static void main(String[] args) {
        System.out.println("================================");
        System.out.println("🌐 서브넷 마스크 계산기");
        System.out.println("================================\n");
        
        try {
            // 입력 데이터 설정
            String ipAddress = "192.168.10.100";
            String subnetMask = "255.255.255.0";
            
            // 계산 수행
            SubnetResult result = calculateSubnet(ipAddress, subnetMask);
            
            // 결과 출력
            displaySubnetAnalysis(result);
            
        } catch (Exception e) {
            System.err.println("❌ 계산 중 오류 발생: " + e.getMessage());
            System.err.println("💡 입력 형식 확인: IP 주소와 서브넷 마스크가 올바른지 점검하세요.");
        }
        
        System.out.println("================================");
        System.out.println("🎉 서브넷 계산 완료!");
        System.out.println("✨ 활용 팁: CIDR 표기법으로 더 간편하게 표현 가능");
        System.out.println("================================");
    }
    
    /**
     * 🎯 서브넷 마스크를 이용한 모든 계산을 수행하는 핵심 메서드
     */
    private static SubnetResult calculateSubnet(String ipStr, String maskStr) throws Exception {
        // IP 주소와 서브넷 마스크를 바이트 배열로 변환
        InetAddress ipAddr = InetAddress.getByName(ipStr);
        InetAddress maskAddr = InetAddress.getByName(maskStr);
        
        byte[] ipBytes = ipAddr.getAddress();
        byte[] maskBytes = maskAddr.getAddress();
        
        // 1. 네트워크 주소 계산 (IP AND Mask)
        byte[] networkBytes = new byte[4];
        for (int i = 0; i < 4; i++) {
            networkBytes[i] = (byte) (ipBytes[i] & maskBytes[i]);
        }
        
        // 2. 호스트 주소 계산 (IP XOR Network)
        byte[] hostBytes = new byte[4];
        for (int i = 0; i < 4; i++) {
            hostBytes[i] = (byte) (ipBytes[i] ^ networkBytes[i]);
        }
        
        // 3. 브로드캐스트 주소 계산 (Network OR (NOT Mask))
        byte[] broadcastBytes = new byte[4];
        for (int i = 0; i < 4; i++) {
            broadcastBytes[i] = (byte) (networkBytes[i] | (~maskBytes[i]));
        }
        
        // 4. 첫 번째/마지막 호스트 주소 계산
        byte[] firstHostBytes = networkBytes.clone();
        firstHostBytes[3] = (byte) (firstHostBytes[3] + 1);
        
        byte[] lastHostBytes = broadcastBytes.clone();
        lastHostBytes[3] = (byte) (lastHostBytes[3] - 1);
        
        // 5. CIDR 표기법 계산 (1의 개수 세기)
        int cidr = countOneBits(maskBytes);
        
        // 6. 사용 가능한 호스트 수 계산
        int hostBits = 32 - cidr;
        int availableHosts = (int) Math.pow(2, hostBits) - 2; // 네트워크 주소와 브로드캐스트 주소 제외
        
        // 결과 객체 생성
        return new SubnetResult(
            ipStr, maskStr, cidr,
            InetAddress.getByAddress(networkBytes).getHostAddress(),
            InetAddress.getByAddress(hostBytes).getHostAddress(),
            InetAddress.getByAddress(broadcastBytes).getHostAddress(),
            InetAddress.getByAddress(firstHostBytes).getHostAddress(),
            InetAddress.getByAddress(lastHostBytes).getHostAddress(),
            availableHosts,
            ipBytes, maskBytes, networkBytes
        );
    }
    
    /**
     * 📊 계산 결과를 보기 좋게 출력하는 메서드
     */
    private static void displaySubnetAnalysis(SubnetResult result) {
        // 입력 정보 출력
        System.out.println("📋 입력 정보:");
        System.out.println("- IP 주소: " + result.originalIP);
        System.out.println("- 서브넷 마스크: " + result.subnetMask + " (/" + result.cidr + ")");
        System.out.println();
        
        // 비트 레벨 분석
        System.out.println("🔍 비트 레벨 분석:");
        System.out.println("IP 주소:      " + bytesToBinaryString(result.ipBytes));
        System.out.println("서브넷 마스크: " + bytesToBinaryString(result.maskBytes));
        System.out.println("─────────────────────────────────────────────────");
        System.out.println("네트워크:     " + bytesToBinaryString(result.networkBytes));
        System.out.println();
        
        // 계산 결과
        System.out.println("📊 계산 결과:");
        System.out.println("- 네트워크 주소: " + result.networkAddress);
        System.out.println("- 호스트 주소: " + result.hostAddress);
        System.out.println("- 브로드캐스트: " + result.broadcastAddress);
        System.out.println("- 첫 번째 호스트: " + result.firstHostAddress);
        System.out.println("- 마지막 호스트: " + result.lastHostAddress);
        System.out.println("- 사용 가능한 호스트 수: " + result.availableHosts + "개");
        System.out.println();
        
        // 네트워크 정보
        System.out.println("🏢 네트워크 정보:");
        System.out.println("- 서브넷 클래스: " + getNetworkClass(result.networkAddress));
        System.out.println("- CIDR 표기법: " + result.networkAddress + "/" + result.cidr);
        System.out.println("- 네트워크 크기: " + getNetworkSizeDescription(result.cidr));
        System.out.println();
        
        // 실무 활용 팁
        System.out.println("💡 실무 활용 팁:");
        if (result.availableHosts < 50) {
            System.out.println("- 소규모 네트워크: 가정이나 소규모 사무실에 적합");
        } else if (result.availableHosts < 1000) {
            System.out.println("- 중간 규모 네트워크: 중소기업이나 부서별 네트워크에 적합");
        } else {
            System.out.println("- 대규모 네트워크: 대기업이나 캠퍼스 네트워크에 적합");
        }
        
        if (result.networkAddress.startsWith("192.168.") || 
            result.networkAddress.startsWith("10.") || 
            result.networkAddress.startsWith("172.")) {
            System.out.println("- 사설 IP 대역: 내부 네트워크용, 인터넷 직접 접근 불가");
        } else {
            System.out.println("- 공인 IP 대역: 인터넷에서 직접 접근 가능, 보안 주의 필요");
        }
    }
    
    /**
     * 🔢 바이트 배열을 이진 문자열로 변환 (점 구분자 포함)
     */
    private static String bytesToBinaryString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            if (i > 0) sb.append(".");
            
            // byte를 unsigned int로 변환 후 8비트 이진 문자열로 변환
            String binary = Integer.toBinaryString((bytes[i] & 0xFF) + 0x100).substring(1);
            sb.append(binary);
        }
        return sb.toString();
    }
    
    /**
     * 🧮 서브넷 마스크에서 1의 개수를 세는 메서드 (CIDR 계산용)
     */
    private static int countOneBits(byte[] maskBytes) {
        int count = 0;
        for (byte b : maskBytes) {
            count += Integer.bitCount(b & 0xFF);
        }
        return count;
    }
    
    /**
     * 🏷️ IP 주소의 클래스를 판단하는 메서드
     */
    private static String getNetworkClass(String networkAddr) {
        String[] parts = networkAddr.split("\\.");
        int firstOctet = Integer.parseInt(parts[0]);
        
        if (firstOctet >= 1 && firstOctet <= 126) {
            return "Class A (대규모 네트워크)";
        } else if (firstOctet >= 128 && firstOctet <= 191) {
            return "Class B (중간 규모 네트워크)";
        } else if (firstOctet >= 192 && firstOctet <= 223) {
            return "Class C (소규모 네트워크)";
        } else if (firstOctet >= 224 && firstOctet <= 239) {
            return "Class D (멀티캐스트)";
        } else {
            return "Class E (실험용)";
        }
    }
    
    /**
     * 📏 CIDR 값에 따른 네트워크 크기 설명
     */
    private static String getNetworkSizeDescription(int cidr) {
        int hostBits = 32 - cidr;
        int totalHosts = (int) Math.pow(2, hostBits);
        
        if (totalHosts <= 4) {
            return "초소형 네트워크 (포인트-투-포인트)";
        } else if (totalHosts <= 64) {
            return "소형 네트워크 (가정/소규모 사무실)";
        } else if (totalHosts <= 1024) {
            return "중형 네트워크 (중소기업/부서)";
        } else if (totalHosts <= 65536) {
            return "대형 네트워크 (대기업/캠퍼스)";
        } else {
            return "초대형 네트워크 (ISP/글로벌 기업)";
        }
    }
    
    /**
     * 📋 서브넷 계산 결과를 저장하는 데이터 클래스
     */
    private static class SubnetResult {
        final String originalIP;
        final String subnetMask;
        final int cidr;
        final String networkAddress;
        final String hostAddress;
        final String broadcastAddress;
        final String firstHostAddress;
        final String lastHostAddress;
        final int availableHosts;
        final byte[] ipBytes;
        final byte[] maskBytes;
        final byte[] networkBytes;
        
        SubnetResult(String originalIP, String subnetMask, int cidr,
                    String networkAddress, String hostAddress, String broadcastAddress,
                    String firstHostAddress, String lastHostAddress, int availableHosts,
                    byte[] ipBytes, byte[] maskBytes, byte[] networkBytes) {
            this.originalIP = originalIP;
            this.subnetMask = subnetMask;
            this.cidr = cidr;
            this.networkAddress = networkAddress;
            this.hostAddress = hostAddress;
            this.broadcastAddress = broadcastAddress;
            this.firstHostAddress = firstHostAddress;
            this.lastHostAddress = lastHostAddress;
            this.availableHosts = availableHosts;
            this.ipBytes = ipBytes;
            this.maskBytes = maskBytes;
            this.networkBytes = networkBytes;
        }
    }
} 