package com.JavaCoreTil.io.visual;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 스트림의 메모리 효율성을 보여주는 데모
 */
public class MemoryEfficiencyDemo {
    
    public static void main(String[] args) {
        MemoryEfficiencyDemo demo = new MemoryEfficiencyDemo();
        
        try {
            demo.createTestFile();
            demo.compareMemoryUsage();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 테스트용 파일 생성
     */
    private void createTestFile() throws IOException {
        System.out.println("🏗️ 테스트 파일 생성 중...");
        
        try (FileWriter writer = new FileWriter("test_data.txt")) {
            // 반복적인 데이터로 파일 생성
            for (int i = 0; i < 10000; i++) {
                writer.write("Line " + i + ": This is test data for stream demonstration.\n");
            }
        }
        
        long fileSize = Files.size(Paths.get("test_data.txt"));
        System.out.println("✅ 파일 생성 완료: " + fileSize + " bytes");
    }
    
    /**
     * 메모리 사용량 비교
     */
    private void compareMemoryUsage() throws IOException {
        System.out.println("\n💾 메모리 사용량 비교");
        System.out.println("=" .repeat(50));
        
        // 1. 전체 파일을 메모리에 로드하는 방식
        System.out.println("\n📖 방식 1: 전체 파일 메모리 로드");
        long beforeMemory1 = getUsedMemory();
        
        byte[] allData = Files.readAllBytes(Paths.get("test_data.txt"));
        
        long afterMemory1 = getUsedMemory();
        System.out.println("메모리 사용량: " + (afterMemory1 - beforeMemory1) / 1024 + " KB");
        System.out.println("로드된 데이터: " + allData.length + " bytes");
        
        // 메모리 정리
        allData = null;
        System.gc();
        
        // 2. 스트림을 사용한 방식
        System.out.println("\n🌊 방식 2: 스트림 사용 (8KB 버퍼)");
        long beforeMemory2 = getUsedMemory();
        
        long totalProcessed = 0;
        byte[] buffer = new byte[8192]; // 8KB 버퍼
        
        try (FileInputStream inputStream = new FileInputStream("test_data.txt")) {
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                totalProcessed += bytesRead;
                // 실제로는 여기서 데이터를 처리함
                processData(buffer, bytesRead);
            }
        }
        
        long afterMemory2 = getUsedMemory();
        System.out.println("메모리 사용량: " + (afterMemory2 - beforeMemory2) / 1024 + " KB");
        System.out.println("처리된 데이터: " + totalProcessed + " bytes");
        
        // 결과 비교
        System.out.println("\n📊 결과 비교");
        System.out.println("─".repeat(30));
        long memoryDiff = (afterMemory1 - beforeMemory1) - (afterMemory2 - beforeMemory2);
        System.out.println("메모리 절약량: " + memoryDiff / 1024 + " KB");
        System.out.println("절약률: " + String.format("%.1f%%", 
            (double) memoryDiff / (afterMemory1 - beforeMemory1) * 100));
    }
    
    /**
     * 데이터 처리 시뮬레이션
     */
    private void processData(byte[] data, int length) {
        // 실제 데이터 처리 로직이 들어갈 자리
        // 예: 데이터 변환, 필터링, 분석 등
        
        // 여기서는 단순히 줄 수를 세는 예시
        for (int i = 0; i < length; i++) {
            if (data[i] == '\n') {
                // 줄바꿈 문자 발견 시 처리
            }
        }
    }
    
    /**
     * 현재 사용 중인 메모리 계산
     */
    private long getUsedMemory() {
        Runtime runtime = Runtime.getRuntime();
        runtime.gc(); // 가비지 컬렉션 실행
        return runtime.totalMemory() - runtime.freeMemory();
    }
} 