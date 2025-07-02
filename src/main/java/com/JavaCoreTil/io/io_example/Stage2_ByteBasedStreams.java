package com.JavaCoreTil.io.io_example;

import java.io.*;
import com.JavaCoreTil.io.common.FilePathManager;

/**
 * I/O 로드맵 2단계: 바이트 기반 스트림 심화
 * 
 * 학습 목표:
 * 1. InputStream/OutputStream - 바이트 스트림의 최상위 클래스
 * 2. FileInputStream/FileOutputStream - 파일 바이트 입출력  
 * 3. ByteArrayInputStream/ByteArrayOutputStream - 메모리 바이트 배열 처리
 * 4. 기본 읽기/쓰기 메서드 - read(), write(), available()
 * 5. 스트림 연결과 체이닝 - 스트림 조합 활용
 * 6. 리소스 관리 - close(), try-with-resources 패턴
 */
public class Stage2_ByteBasedStreams {
    
    private static final boolean DELETE_FLAG = false;
    
    public static void main(String[] args) {
        Stage2_ByteBasedStreams demo = new Stage2_ByteBasedStreams();
        
        FilePathManager.ensureDirectoryExists();
        
        System.out.println("=== I/O 로드맵 2단계: 바이트 기반 스트림 심화 ===\n");
        
        try {
            // 1. 추상 클래스와 구현체 이해
            demo.demonstrateAbstractVsImplementation();
            
            // 2. FileInputStream/FileOutputStream 심화
            demo.demonstrateFileStreams();
            
            // 3. ByteArray 스트림과 메모리 처리
            demo.demonstrateByteArrayStreams();
            
            // 4. 효율적인 읽기/쓰기 패턴
            demo.demonstrateEfficientReadWrite();
            
            // 5. available() 메서드 활용
            demo.demonstrateAvailableMethod();
            
            // 6. 스트림 체이닝 기초
            demo.demonstrateStreamChaining();
            
            // 7. 파일 크기별 처리 전략
            demo.demonstrateFileSizeStrategy();
            
            // 8. 리소스 관리 심화
            demo.demonstrateAdvancedResourceManagement();
            
            System.out.println("\n=== 2단계 학습 완료! ===");
            
        } finally {
            System.out.println("\n=== 파일 정리 ===");
            FilePathManager.cleanupFiles(DELETE_FLAG);
        }
    }
    
    /**
     * 1. 추상 클래스와 구현체 이해
     * InputStream/OutputStream은 추상 클래스로 직접 인스턴스 생성 불가
     */
    public void demonstrateAbstractVsImplementation() {
        System.out.println("1. 추상 클래스와 구현체 이해");
        
        // InputStream input = new InputStream();  // 컴파일 에러!
        // OutputStream output = new OutputStream(); // 컴파일 에러!
        
        // 올바른 방법: 구현체를 통한 사용
        // 먼저 파일을 생성한 후 읽기 테스트
        try (OutputStream output = new FileOutputStream(FilePathManager.getFilePath("abstract_test.txt"))) {
            // 추상 클래스에 정의된 메서드들을 모든 구현체에서 사용 가능
            output.write("추상 클래스 테스트".getBytes());
            System.out.println("출력 스트림 실제 클래스: " + output.getClass().getSimpleName());
            System.out.println("추상 클래스의 공통 메서드를 구현체에서 사용");
        } catch (IOException e) {
            System.out.println("파일 쓰기 오류: " + e.getMessage());
        }

        System.out.println();
        
        // 이제 파일을 읽어서 추상 클래스 특성 확인
        try (InputStream input = new FileInputStream(FilePathManager.getFilePath("abstract_test.txt"))) {
            // 실제 타입은 FileInputStream이지만 InputStream 타입으로 참조
            System.out.println("입력 스트림 실제 클래스: " + input.getClass().getSimpleName());
            
            // 추상 클래스의 공통 메서드 사용
            byte[] data = input.readAllBytes();
            System.out.println("읽은 데이터: " + new String(data));
            
        } catch (IOException e) {
            System.out.println("파일 읽기 오류: " + e.getMessage());
        }
        
        System.out.println("→ 추상 클래스는 공통 인터페이스 제공, 구현체가 실제 동작\n");
    }
    
    /**
     * 2. FileInputStream/FileOutputStream 심화
     * 파일 시스템과의 직접적인 상호작용
     */
    public void demonstrateFileStreams() {
        System.out.println("2. FileInputStream/FileOutputStream 심화");
        
        // 파일 생성 및 쓰기 (기본 모드: 덮어쓰기)
        try (FileOutputStream output = new FileOutputStream(FilePathManager.getFilePath("file_demo.txt"))) {
            String content = "첫 번째 내용\n두 번째 내용\n";
            output.write(content.getBytes());
            System.out.println("파일 쓰기 완료 (덮어쓰기 모드)");
        } catch (IOException e) {
            System.out.println("파일 쓰기 오류: " + e.getMessage());
        }
        
        // 파일에 내용 추가 (append 모드)
        try (FileOutputStream appendOutput = new FileOutputStream(FilePathManager.getFilePath("file_demo.txt"), true)) {
            String additionalContent = "세 번째 내용 (추가됨)\n";
            appendOutput.write(additionalContent.getBytes());
            System.out.println("파일에 내용 추가 완료 (append 모드)");
        } catch (IOException e) {
            System.out.println("파일 추가 오류: " + e.getMessage());
        }
        
        // 파일 읽기 및 내용 확인
        try (FileInputStream input = new FileInputStream(FilePathManager.getFilePath("file_demo.txt"))) {
            byte[] data = input.readAllBytes();
            String content = new String(data);
            System.out.println("최종 파일 내용:");
            System.out.println(content);
        } catch (IOException e) {
            System.out.println("파일 읽기 오류: " + e.getMessage());
        }
        
        System.out.println("→ FileInputStream/OutputStream: 실제 파일 시스템과 직접 연결\n");
    }
    
    /**
     * 3. ByteArrayInputStream/OutputStream과 메모리 처리
     * 메모리에서만 동작하는 스트림의 특징
     */
    public void demonstrateByteArrayStreams() {
        System.out.println("3. ByteArrayInputStream/OutputStream과 메모리 처리");
        
        // 메모리에서 데이터 준비 및 쓰기
        ByteArrayOutputStream memoryOutput = new ByteArrayOutputStream();
        
        try {
            // 여러 데이터를 메모리에 조립
            memoryOutput.write("헤더: ".getBytes());
            memoryOutput.write("Java I/O 학습".getBytes());
            memoryOutput.write("\n푸터: 메모리 처리 완료".getBytes());
            
            // 조립된 데이터 확인
            byte[] assembledData = memoryOutput.toByteArray();
            System.out.println("메모리에서 조립된 데이터:");
            System.out.println(new String(assembledData));
            System.out.println("데이터 크기: " + assembledData.length + " 바이트");
            
        } catch (IOException e) {
            System.out.println("메모리 쓰기 오류: " + e.getMessage());
        }
        
        // 메모리에서 데이터 읽기
        byte[] sourceData = "메모리 기반 데이터 처리 테스트".getBytes();
        ByteArrayInputStream memoryInput = new ByteArrayInputStream(sourceData);
        
        try {
            System.out.println("\n메모리에서 데이터 읽기:");
            
            // 첫 10바이트 읽기
            byte[] buffer = new byte[10];
            int bytesRead = memoryInput.read(buffer);
            System.out.println("첫 10바이트: " + new String(buffer, 0, bytesRead));
            
            // 나머지 데이터 읽기
            byte[] remaining = memoryInput.readAllBytes();
            System.out.println("나머지 데이터: " + new String(remaining));
            
        } catch (IOException e) {
            System.out.println("메모리 읽기 오류: " + e.getMessage());
        }
        
        System.out.println("→ ByteArray 스트림: 메모리에서만 동작, 매우 빠름, 파일 생성 없음\n");
    }
    
    /**
     * 4. 효율적인 읽기/쓰기 패턴
     * 단일 바이트 vs 배열 기반 처리의 성능 차이
     */
    public void demonstrateEfficientReadWrite() {
        System.out.println("4. 효율적인 읽기/쓰기 패턴");
        
        // 테스트용 데이터 준비 (1000바이트)
        byte[] testData = new byte[1000];
        for (int i = 0; i < testData.length; i++) {
            testData[i] = (byte) (i % 256);
        }
        
        // 파일에 테스트 데이터 저장
        try (FileOutputStream output = new FileOutputStream(FilePathManager.getFilePath("efficiency_test.bin"))) {
            output.write(testData);
        } catch (IOException e) {
            System.out.println("테스트 데이터 저장 오류: " + e.getMessage());
            return;
        }
        
        // 비효율적인 방법: 1바이트씩 처리
        long startTime = System.currentTimeMillis();
        try (FileInputStream input = new FileInputStream(FilePathManager.getFilePath("efficiency_test.bin"))) {
            int data;
            int count = 0;
            while ((data = input.read()) != -1) {
                count++;
            }
            System.out.println("비효율적 방법 - 1바이트씩: " + count + "바이트 읽음");
        } catch (IOException e) {
            System.out.println("비효율적 읽기 오류: " + e.getMessage());
        }
        long inefficientTime = System.currentTimeMillis() - startTime;
        
        // 효율적인 방법: 배열 기반 처리
        startTime = System.currentTimeMillis();
        try (FileInputStream input = new FileInputStream(FilePathManager.getFilePath("efficiency_test.bin"))) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            int totalBytes = 0;
            while ((bytesRead = input.read(buffer)) != -1) {
                totalBytes += bytesRead;
            }
            System.out.println("효율적 방법 - 배열 기반: " + totalBytes + "바이트 읽음");
        } catch (IOException e) {
            System.out.println("효율적 읽기 오류: " + e.getMessage());
        }
        long efficientTime = System.currentTimeMillis() - startTime;
        
        System.out.println("성능 비교:");
        System.out.println("  비효율적 방법: " + inefficientTime + "ms");
        System.out.println("  효율적 방법: " + efficientTime + "ms");
        if (inefficientTime > 0) {
            System.out.println("  성능 개선: 약 " + (inefficientTime / Math.max(efficientTime, 1)) + "배 빠름");
        }
        
        System.out.println("→ 배열 기반 읽기/쓰기가 훨씬 효율적\n");
    }
    
    /**
     * 5. available() 메서드 활용
     * 읽을 수 있는 바이트 수 확인
     */
    public void demonstrateAvailableMethod() {
        System.out.println("5. available() 메서드 활용");
        
        // 테스트 파일 생성
        String testContent = "available() 메서드 테스트용 데이터입니다.";
        try (FileOutputStream output = new FileOutputStream(FilePathManager.getFilePath("available_test.txt"))) {
            output.write(testContent.getBytes());
        } catch (IOException e) {
            System.out.println("테스트 파일 생성 오류: " + e.getMessage());
            return;
        }
        
        // FileInputStream에서 available() 사용
        try (FileInputStream fileInput = new FileInputStream(FilePathManager.getFilePath("available_test.txt"))) {
            System.out.println("FileInputStream available() 테스트:");
            System.out.println("  초기 available(): " + fileInput.available());
            
            // 일부 데이터 읽기
            byte[] buffer = new byte[10];
            int bytesRead = fileInput.read(buffer);
            System.out.println("  " + bytesRead + "바이트 읽은 후 available(): " + fileInput.available());
            
            // 나머지 모두 읽기
            fileInput.readAllBytes();
            System.out.println("  모두 읽은 후 available(): " + fileInput.available());
            
        } catch (IOException e) {
            System.out.println("FileInputStream available() 테스트 오류: " + e.getMessage());
        }
        
        // ByteArrayInputStream에서 available() 사용
        byte[] memoryData = testContent.getBytes();
        ByteArrayInputStream memoryInput = new ByteArrayInputStream(memoryData);
        
        System.out.println("\nByteArrayInputStream available() 테스트:");
        System.out.println("  초기 available(): " + memoryInput.available());
        
        try {
            // 일부 데이터 읽기
            byte[] buffer = new byte[10];
            int bytesRead = memoryInput.read(buffer);
            System.out.println("  " + bytesRead + "바이트 읽은 후 available(): " + memoryInput.available());
            
        } catch (IOException e) {
            System.out.println("ByteArrayInputStream available() 테스트 오류: " + e.getMessage());
        }
        
        System.out.println("→ available(): 읽을 수 있는 바이트 수 확인 (참고용)\n");
    }
    
    /**
     * 6. 스트림 체이닝 기초
     * 기본 스트림과 보조 스트림의 연결
     */
    public void demonstrateStreamChaining() {
        System.out.println("6. 스트림 체이닝 기초");
        
        // 기본 스트림만 사용
        System.out.println("기본 스트림만 사용:");
        try (FileOutputStream basicOutput = new FileOutputStream(FilePathManager.getFilePath("basic_chain.txt"))) {
            String data = "기본 스트림 테스트\n";
            basicOutput.write(data.getBytes());
            System.out.println("  기본 FileOutputStream으로 쓰기 완료");
        } catch (IOException e) {
            System.out.println("기본 스트림 오류: " + e.getMessage());
        }
        
        // 보조 스트림 체이닝 (BufferedOutputStream 추가)
        System.out.println("\n보조 스트림 체이닝:");
        try (FileOutputStream fileOutput = new FileOutputStream(FilePathManager.getFilePath("buffered_chain.txt"));
             BufferedOutputStream bufferedOutput = new BufferedOutputStream(fileOutput)) {
            
            System.out.println("  체인 구조: FileOutputStream → BufferedOutputStream");
            
            // 여러 번 쓰기 (버퍼에 모아서 한 번에 처리)
            for (int i = 1; i <= 5; i++) {
                String data = "체이닝 테스트 라인 " + i + "\n";
                bufferedOutput.write(data.getBytes());
                System.out.println("    라인 " + i + " 버퍼에 저장");
            }
            
            System.out.println("  flush() 호출하여 버퍼 내용 파일에 쓰기");
            bufferedOutput.flush();
            
        } catch (IOException e) {
            System.out.println("체이닝 스트림 오류: " + e.getMessage());
        }
        
        // 체이닝된 스트림으로 읽기
        try (FileInputStream fileInput = new FileInputStream(FilePathManager.getFilePath("buffered_chain.txt"));
             BufferedInputStream bufferedInput = new BufferedInputStream(fileInput)) {
            
            System.out.println("\n체이닝된 읽기 스트림:");
            System.out.println("  체인 구조: FileInputStream → BufferedInputStream");
            
            byte[] data = bufferedInput.readAllBytes();
            System.out.println("  읽은 내용:");
            System.out.println(new String(data));
            
        } catch (IOException e) {
            System.out.println("체이닝 읽기 오류: " + e.getMessage());
        }
        
        System.out.println("→ 스트림 체이닝: 기본 기능 + 추가 기능 조합\n");
    }
    
    /**
     * 7. 파일 크기별 처리 전략
     * 작은 파일 vs 큰 파일의 다른 처리 방식
     */
    public void demonstrateFileSizeStrategy() {
        System.out.println("7. 파일 크기별 처리 전략");
        
        // 작은 파일 생성 (1KB)
        byte[] smallData = new byte[1024];
        for (int i = 0; i < smallData.length; i++) {
            smallData[i] = (byte) ('A' + (i % 26));
        }
        
        // 큰 파일 생성 (100KB)
        byte[] largeData = new byte[100 * 1024];
        for (int i = 0; i < largeData.length; i++) {
            largeData[i] = (byte) ('0' + (i % 10));
        }
        
        try (FileOutputStream smallOutput = new FileOutputStream(FilePathManager.getFilePath("small_file.dat"));
             FileOutputStream largeOutput = new FileOutputStream(FilePathManager.getFilePath("large_file.dat"))) {
            
            smallOutput.write(smallData);
            largeOutput.write(largeData);
            
        } catch (IOException e) {
            System.out.println("테스트 파일 생성 오류: " + e.getMessage());
            return;
        }
        
        // 작은 파일 처리: 메모리 로드 방식
        System.out.println("작은 파일 처리 (1KB - 메모리 로드):");
        try (FileInputStream input = new FileInputStream(FilePathManager.getFilePath("small_file.dat"))) {
            byte[] allData = input.readAllBytes();
            System.out.println("  전체 데이터를 메모리에 로드: " + allData.length + " 바이트");
            
            // 메모리에서 빠른 처리
            ByteArrayInputStream memoryProcessor = new ByteArrayInputStream(allData);
            System.out.println("  메모리에서 빠른 처리 가능");
            
        } catch (IOException e) {
            System.out.println("작은 파일 처리 오류: " + e.getMessage());
        }
        
        // 큰 파일 처리: 스트리밍 방식
        System.out.println("\n큰 파일 처리 (100KB - 스트리밍):");
        try (FileInputStream input = new FileInputStream(FilePathManager.getFilePath("large_file.dat"))) {
            byte[] buffer = new byte[8192]; // 8KB 버퍼
            int bytesRead;
            int totalBytes = 0;
            int chunks = 0;
            
            while ((bytesRead = input.read(buffer)) != -1) {
                totalBytes += bytesRead;
                chunks++;
                // 실제로는 여기서 데이터 처리
            }
            
            System.out.println("  스트리밍 처리 완료:");
            System.out.println("    총 " + totalBytes + " 바이트");
            System.out.println("    " + chunks + "개 청크로 나누어 처리");
            System.out.println("    메모리 사용량: 최대 8KB (버퍼 크기)");
            
        } catch (IOException e) {
            System.out.println("큰 파일 처리 오류: " + e.getMessage());
        }
        
        System.out.println("→ 파일 크기에 따른 최적 처리 전략 선택 중요\n");
    }
    
    /**
     * 8. 리소스 관리 심화
     * 다중 리소스와 예외 상황에서의 안전한 관리
     */
    public void demonstrateAdvancedResourceManagement() {
        System.out.println("8. 리소스 관리 심화");
        
        // 다중 리소스 관리
        System.out.println("다중 리소스 안전 관리:");
        try (FileInputStream input1 = new FileInputStream(FilePathManager.getFilePath("small_file.dat"));
             FileInputStream input2 = new FileInputStream(FilePathManager.getFilePath("large_file.dat"));
             FileOutputStream output = new FileOutputStream(FilePathManager.getFilePath("merged_file.dat"));
             BufferedOutputStream bufferedOutput = new BufferedOutputStream(output)) {
            
            System.out.println("  4개의 리소스가 모두 열림");
            
            // input1 데이터 복사
            byte[] buffer = new byte[1024];
            int bytesRead;
            int totalFromInput1 = 0;
            while ((bytesRead = input1.read(buffer)) != -1) {
                bufferedOutput.write(buffer, 0, bytesRead);
                totalFromInput1 += bytesRead;
            }
            
            // input2 데이터 복사  
            int totalFromInput2 = 0;
            while ((bytesRead = input2.read(buffer)) != -1) {
                bufferedOutput.write(buffer, 0, bytesRead);
                totalFromInput2 += bytesRead;
            }
            
            System.out.println("  파일 병합 완료:");
            System.out.println("    input1에서: " + totalFromInput1 + " 바이트");
            System.out.println("    input2에서: " + totalFromInput2 + " 바이트");
            System.out.println("    총합: " + (totalFromInput1 + totalFromInput2) + " 바이트");
            
        } catch (IOException e) {
            System.out.println("다중 리소스 처리 오류: " + e.getMessage());
        }
        System.out.println("  모든 리소스가 자동으로 해제됨");
        
        // 예외 상황에서의 리소스 관리 테스트
        System.out.println("\n예외 상황에서의 안전한 리소스 관리:");
        try (FileInputStream input = new FileInputStream(FilePathManager.getFilePath("small_file.dat"));
             FileOutputStream output = new FileOutputStream(FilePathManager.getFilePath("exception_test.dat"))) {
            
            System.out.println("  리소스 열기 성공");
            
            // 의도적으로 예외 발생시키기 (0으로 나누기)
            // int error = 1 / 0;  // 이 줄의 주석을 해제하면 예외 발생
            
            byte[] data = input.readAllBytes();
            output.write(data);
            System.out.println("  정상 처리 완료");
            
        } catch (ArithmeticException e) {
            System.out.println("  산술 예외 발생: " + e.getMessage());
            System.out.println("  하지만 리소스는 안전하게 해제됨");
        } catch (IOException e) {
            System.out.println("  I/O 예외 발생: " + e.getMessage());
            System.out.println("  리소스는 안전하게 해제됨");
        }
        
        System.out.println("→ try-with-resources: 예외 발생 여부와 관계없이 안전한 리소스 해제\n");
    }
}