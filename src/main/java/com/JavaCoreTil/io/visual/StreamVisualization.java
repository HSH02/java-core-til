package com.JavaCoreTil.io.visual;

import java.io.*;

/**
 * 스트림의 데이터 흐름을 시각적으로 보여주는 클래스
 */
public class StreamVisualization {
    
    public static void main(String[] args) {
        StreamVisualization viz = new StreamVisualization();
        
        try {
            // 작은 파일로 스트림 흐름 시각화
            viz.createSmallFile();
            viz.visualizeStreamFlow();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 시각화용 작은 파일 생성
     */
    private void createSmallFile() throws IOException {
        String content = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        
        try (FileWriter writer = new FileWriter("small_file.txt")) {
            writer.write(content);
        }
        
        System.out.println("📝 테스트 파일 생성: " + content);
        System.out.println("📏 파일 크기: " + content.length() + " bytes");
    }
    
    /**
     * 스트림 흐름을 단계별로 시각화
     */
    private void visualizeStreamFlow() throws IOException {
        System.out.println("\n🌊 스트림 흐름 시각화");
        System.out.println("=".repeat(60));
        
        // 다양한 버퍼 크기로 테스트
        int[] bufferSizes = {1, 4, 8, 16};
        
        for (int bufferSize : bufferSizes) {
            System.out.println("\n🚤 나룻배 크기: " + bufferSize + " bytes");
            System.out.println("─".repeat(40));
            
            try (FileInputStream inputStream = new FileInputStream("small_file.txt")) {
                byte[] buffer = new byte[bufferSize];
                int bytesRead;
                int tripNumber = 1;
                
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    System.out.printf("🚤 %d번째 운반: ", tripNumber);
                    
                    // 실제 읽은 데이터만 출력
                    for (int i = 0; i < bytesRead; i++) {
                        System.out.print((char) buffer[i]);
                    }
                    
                    System.out.printf(" (%d bytes)%n", bytesRead);
                    tripNumber++;
                    
                    // 시각적 효과를 위한 잠시 대기
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                
                System.out.println("✅ 총 " + (tripNumber - 1) + "번의 운반으로 완료!");
            }
        }
    }
} 