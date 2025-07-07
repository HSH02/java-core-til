package com.JavaCoreTil.io.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * I/O 예시 파일들의 경로 관리 및 자동 삭제 기능을 제공하는 유틸리티 클래스
 */
public class FilePathManager {
    
    // 기본 파일 저장 경로
    public static final String BASE_PATH = "src/main/java/com/JavaCoreTil/io/file/";
    
    // 생성된 파일들을 추적하기 위한 리스트
    private static final List<String> createdFiles = new ArrayList<>();
    
    /**
     * 파일 경로 생성 (기본 경로 + 파일명)
     * @param fileName 파일명
     * @return 전체 경로
     */
    public static String getFilePath(String fileName) {
        String fullPath = BASE_PATH + fileName;
        createdFiles.add(fullPath);
        return fullPath;
    }

    public static String getCustomFilePath(String customPath, String fileName) {
        String fullPath = customPath + fileName;
        createdFiles.add(fullPath);
        return fullPath;
    }
    
    /**
     * 디렉토리가 존재하지 않으면 생성
     */
    public static void ensureDirectoryExists() {
        try {
            Path path = Paths.get(BASE_PATH);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                System.out.println("✓ 디렉토리 생성: " + BASE_PATH);
            }
        } catch (IOException e) {
            System.out.println("디렉토리 생성 실패: " + e.getMessage());
        }
    }

    /**
     * 디렉토리가 존재하지 않으면 생성
     */
    public static void ensureCustomDirectoryExists(String customPath) {
        try {
            Path path = Paths.get(customPath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                System.out.println("✓ 디렉토리 생성: " + customPath);
            }
        } catch (IOException e) {
            System.out.println("디렉토리 생성 실패: " + e.getMessage());
        }
    }
    
    /**
     * 생성된 모든 파일 삭제
     * @param deleteFlag true면 삭제 실행, false면 삭제하지 않음
     */
    public static void cleanupFiles(boolean deleteFlag) {
        if (!deleteFlag) {
            System.out.println("파일 삭제 플래그가 false 입니다. 파일들이 보존됩니다.");
            return;
        }
        
        System.out.println("생성된 파일들을 정리합니다...");
        int deletedCount = 0;
        
        for (String filePath : createdFiles) {
            try {
                File file = new File(filePath);
                if (file.exists() && file.delete()) {
                    System.out.println("  ✓ 삭제: " + filePath);
                    deletedCount++;
                }
            } catch (Exception e) {
                System.out.println("  삭제 실패: " + filePath + " - " + e.getMessage());
            }
        }
        
        System.out.println("총 " + deletedCount + "개 파일이 삭제되었습니다.");
        
        // 리스트 초기화
        createdFiles.clear();
    }
    
    /**
     * 생성된 파일 목록 조회
     * @return 생성된 파일 경로 리스트
     */
    public static List<String> getCreatedFiles() {
        return new ArrayList<>(createdFiles);
    }
    
    /**
     * 특정 파일 삭제
     * @param fileName 삭제할 파일명
     * @return 삭제 성공 여부
     */
    public static boolean deleteFile(String fileName) {
        String fullPath = BASE_PATH + fileName;
        try {
            File file = new File(fullPath);
            if (file.exists()) {
                boolean deleted = file.delete();
                if (deleted) {
                    createdFiles.remove(fullPath);
                    System.out.println("✓ 파일 삭제: " + fullPath);
                }
                return deleted;
            }
        } catch (Exception e) {
            System.out.println("파일 삭제 실패: " + fullPath + " - " + e.getMessage());
        }
        return false;
    }
} 