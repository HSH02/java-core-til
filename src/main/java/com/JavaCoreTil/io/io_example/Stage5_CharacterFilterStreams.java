package com.JavaCoreTil.io.io_example;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import com.JavaCoreTil.io.common.FilePathManager;

/**
 * I/O 로드맵 5단계: 문자 기반 보조 스트림 (Character Filter Streams)
 * 
 * 학습 목표:
 * 1. BufferedReader/BufferedWriter - 문자 버퍼링, readLine()
 * 2. InputStreamReader/OutputStreamWriter - 바이트↔문자 변환
 * 3. PrintWriter - 형식화된 문자 출력
 * 4. 문자 인코딩 지정 - Charset 활용
 * 5. 텍스트 파일 효율적 처리 - 줄 단위 읽기
 * 6. Scanner 활용 - 편리한 입력 처리
 */
public class Stage5_CharacterFilterStreams {
    
    private static final boolean DELETE_FLAG = false;
    
    public static void main(String[] args) {
        Stage5_CharacterFilterStreams demo = new Stage5_CharacterFilterStreams();
        
        FilePathManager.ensureDirectoryExists();
        
        System.out.println("=== I/O 로드맵 5단계: 문자 기반 보조 스트림 ===\n");
        
        try {
            // 1. BufferedReader/BufferedWriter - 성능 최적화
            demo.demonstrateBufferedCharacterStreams();
            
            // 2. InputStreamReader/OutputStreamWriter - 바이트↔문자 변환
            demo.demonstrateStreamReaderWriter();
            
            // 3. PrintWriter - 형식화된 문자 출력
            demo.demonstratePrintWriter();
            
            // 4. 문자 인코딩 지정과 국제화
            demo.demonstrateCharsetHandling();
            
            // 5. 텍스트 파일 효율적 처리 - 줄 단위 읽기
            demo.demonstrateLineBasedProcessing();
            
            // 6. Scanner 활용 - 편리한 입력 처리
            demo.demonstrateScanner();
            
            // 7. 성능 비교 - 버퍼 vs 직접 처리
            demo.demonstrateCharacterBufferPerformance();
            
            // 8. 실무 활용 - 대용량 텍스트 처리
            demo.demonstrateLargeTextProcessing();
            
            System.out.println("\n=== 5단계 학습 완료! ===");
            
        } finally {
            System.out.println("\n=== 파일 정리 ===");
            FilePathManager.cleanupFiles(DELETE_FLAG);
        }
    }
    
    /**
     * 1. BufferedReader/BufferedWriter - 문자 버퍼링의 위력
     * readLine() 메서드와 성능 최적화
     */
    public void demonstrateBufferedCharacterStreams() {
        System.out.println("1. BufferedReader/BufferedWriter - 문자 버퍼링");
        
        String textFile = FilePathManager.getFilePath("buffered_text.txt");
        
        // BufferedWriter로 줄 단위 쓰기
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(textFile))) {
            
            System.out.println("BufferedWriter 주요 기능:");
            
            // 1. write() 메서드들
            writer.write("첫 번째 줄입니다.");
            writer.newLine(); // 플랫폼 독립적인 줄바꿈
            System.out.println("- write() + newLine(): 플랫폼 독립적 줄바꿈");
            
            // 2. 여러 줄 쓰기
            String[] lines = {
                "두 번째 줄: BufferedWriter 테스트",
                "세 번째 줄: 성능 최적화",
                "네 번째 줄: 메모리 버퍼 활용",
                "다섯 번째 줄: 한글과 English 혼용"
            };
            
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            System.out.println("- 다중 줄 쓰기 완료");
            
            // 3. flush() - 버퍼 강제 출력
            writer.flush();
            System.out.println("- flush(): 버퍼 내용 강제 출력");
            
        } catch (IOException e) {
            System.out.println("BufferedWriter 오류: " + e.getMessage());
        }
        
        // BufferedReader로 줄 단위 읽기
        try (BufferedReader reader = new BufferedReader(new FileReader(textFile))) {
            
            System.out.println("\nBufferedReader 주요 기능:");
            
            // 1. readLine() - 한 줄씩 읽기 (가장 중요한 메서드)
            System.out.println("- readLine()으로 줄 단위 읽기:");
            String line;
            int lineNumber = 1;
            
            while ((line = reader.readLine()) != null) {
                System.out.println("  라인 " + lineNumber + ": " + line);
                lineNumber++;
            }
            
        } catch (IOException e) {
            System.out.println("BufferedReader 오류: " + e.getMessage());
        }
        
        // 성능 비교: FileReader vs BufferedReader
        try {
            // 대용량 테스트 파일 생성
            String performanceFile = FilePathManager.getFilePath("performance_test.txt");
            
            try (FileWriter writer = new FileWriter(performanceFile)) {
                for (int i = 0; i < 1000; i++) {
                    writer.write("성능 테스트 라인 " + (i + 1) + ": 버퍼링의 중요성을 확인하는 테스트입니다.\n");
                }
            }
            
            // FileReader 직접 사용 (느림)
            long startTime = System.nanoTime();
            try (FileReader directReader = new FileReader(performanceFile)) {
                while (directReader.read() != -1) {
                    // 문자 하나씩 읽기
                }
            }
            long directTime = System.nanoTime() - startTime;
            
            // BufferedReader 사용 (빠름)
            startTime = System.nanoTime();
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(performanceFile))) {
                while (bufferedReader.readLine() != null) {
                    // 줄 단위로 읽기
                }
            }
            long bufferedTime = System.nanoTime() - startTime;
            
            System.out.printf("\n성능 비교 결과:\n");
            System.out.printf("- FileReader 직접: %.2f ms\n", directTime / 1_000_000.0);
            System.out.printf("- BufferedReader: %.2f ms\n", bufferedTime / 1_000_000.0);
            System.out.printf("- 성능 향상: %.0f배\n", (double) directTime / bufferedTime);
            
        } catch (IOException e) {
            System.out.println("성능 비교 오류: " + e.getMessage());
        }
        
        System.out.println("→ BufferedReader/Writer: 텍스트 처리의 필수 도구\n");
    }
    
    /**
     * 2. InputStreamReader/OutputStreamWriter - 바이트↔문자 변환
     * 바이트 스트림과 문자 스트림을 연결하는 브리지
     */
    public void demonstrateStreamReaderWriter() {
        System.out.println("2. InputStreamReader/OutputStreamWriter - 바이트↔문자 변환");
        
        String bridgeFile = FilePathManager.getFilePath("bridge_test.txt");
        
        // OutputStreamWriter: 바이트 스트림 → 문자 스트림
        try (OutputStreamWriter writer = new OutputStreamWriter(
                new FileOutputStream(bridgeFile), StandardCharsets.UTF_8)) {
            
            System.out.println("OutputStreamWriter:");
            System.out.println("- FileOutputStream(바이트) → OutputStreamWriter(문자)");
            
            writer.write("UTF-8 인코딩 테스트\n");
            writer.write("한글: 안녕하세요\n");
            writer.write("English: Hello World\n");
            writer.write("Special: ©®™€\n");
            
            System.out.println("- 다국어 텍스트 저장 완료");
            
        } catch (IOException e) {
            System.out.println("OutputStreamWriter 오류: " + e.getMessage());
        }
        
        // InputStreamReader: 바이트 스트림 → 문자 스트림
        try (InputStreamReader reader = new InputStreamReader(
                new FileInputStream(bridgeFile), StandardCharsets.UTF_8)) {
            
            System.out.println("\nInputStreamReader:");
            System.out.println("- FileInputStream(바이트) → InputStreamReader(문자)");
            
            char[] buffer = new char[1000];
            int charsRead = reader.read(buffer);
            String content = new String(buffer, 0, charsRead);
            
            System.out.println("- 읽은 내용:");
            System.out.println(content);
            
        } catch (IOException e) {
            System.out.println("InputStreamReader 오류: " + e.getMessage());
        }
        
        // 다양한 인코딩으로 테스트
        try {
            // EUC-KR 인코딩으로 저장
            String euckrFile = FilePathManager.getFilePath("euckr_test.txt");
            
            try (OutputStreamWriter euckrWriter = new OutputStreamWriter(
                    new FileOutputStream(euckrFile), "EUC-KR")) {
                euckrWriter.write("EUC-KR 인코딩: 한글 테스트");
            }
            
            // UTF-8로 잘못 읽기
            try (InputStreamReader wrongReader = new InputStreamReader(
                    new FileInputStream(euckrFile), StandardCharsets.UTF_8)) {
                char[] buffer = new char[100];
                int charsRead = wrongReader.read(buffer);
                String wrongContent = new String(buffer, 0, charsRead);
                System.out.println("잘못된 인코딩으로 읽은 결과: " + wrongContent + " (깨짐)");
            }
            
            // 올바른 인코딩으로 읽기
            try (InputStreamReader correctReader = new InputStreamReader(
                    new FileInputStream(euckrFile), "EUC-KR")) {
                char[] buffer = new char[100];
                int charsRead = correctReader.read(buffer);
                String correctContent = new String(buffer, 0, charsRead);
                System.out.println("올바른 인코딩으로 읽은 결과: " + correctContent);
            }
            
        } catch (IOException e) {
            System.out.println("인코딩 테스트 오류: " + e.getMessage());
        }
        
        System.out.println("→ StreamReader/Writer: 바이트와 문자 스트림의 브리지\n");
    }
    
    /**
     * 3. PrintWriter - 형식화된 문자 출력
     * PrintStream의 문자 버전, 더 안전한 텍스트 출력
     */
    public void demonstratePrintWriter() {
        System.out.println("3. PrintWriter - 형식화된 문자 출력");
        
        String printFile = FilePathManager.getFilePath("print_writer_test.txt");
        
        // PrintWriter 기본 사용법
        try (PrintWriter pw = new PrintWriter(new FileWriter(printFile))) {
            
            System.out.println("PrintWriter 주요 기능:");
            
            // 1. 기본 출력 메서드들
            pw.println("=== PrintWriter 테스트 ===");
            pw.print("줄바꿈 없는 출력, ");
            pw.println("줄바꿈 있는 출력");
            System.out.println("- println(), print() 메서드");
            
            // 2. printf() - 형식화된 출력
            pw.printf("정수: %d, 실수: %.2f, 문자열: %s\n", 42, 3.14159, "포맷팅");
            pw.printf("날짜: %tY-%tm-%td, 시간: %tH:%tM:%tS\n", 
                new java.util.Date(), new java.util.Date(), new java.util.Date(),
                new java.util.Date(), new java.util.Date(), new java.util.Date());
            System.out.println("- printf() 형식화 출력");
            
            // 3. 다양한 데이터 타입 출력
            pw.println("boolean: " + true);
            pw.println("char: " + 'A');
            pw.println("배열: " + java.util.Arrays.toString(new int[]{1, 2, 3, 4, 5}));
            System.out.println("- 다양한 데이터 타입 출력");
            
            // 4. flush() - 버퍼 비우기
            pw.flush();
            System.out.println("- flush() 버퍼 비우기");
            
        } catch (IOException e) {
            System.out.println("PrintWriter 오류: " + e.getMessage());
        }
        
        // BufferedWriter와 조합 사용
        try (PrintWriter bufferedPW = new PrintWriter(
                new BufferedWriter(new FileWriter(FilePathManager.getFilePath("buffered_print.txt"))))) {
            
            System.out.println("\nBufferedWriter와 조합:");
            
            // 성능이 중요한 대량 출력
            for (int i = 0; i < 100; i++) {
                bufferedPW.printf("라인 %03d: 성능 최적화된 출력 테스트\n", i + 1);
            }
            
            System.out.println("- BufferedWriter + PrintWriter 조합으로 성능 최적화");
            
        } catch (IOException e) {
            System.out.println("Buffered PrintWriter 오류: " + e.getMessage());
        }
        
        // 콘솔 출력과 파일 출력 동시 수행
        try (PrintWriter fileWriter = new PrintWriter(new FileWriter(FilePathManager.getFilePath("console_file.txt")))) {
            
            String message = "콘솔과 파일에 동시 출력";
            
            // 콘솔 출력
            System.out.println("콘솔: " + message);
            
            // 파일 출력
            fileWriter.println("파일: " + message);
            
            System.out.println("- 콘솔과 파일 동시 출력 완료");
            
        } catch (IOException e) {
            System.out.println("동시 출력 오류: " + e.getMessage());
        }
        
        System.out.println("→ PrintWriter: 안전하고 편리한 텍스트 출력 도구\n");
    }
    
    /**
     * 4. 문자 인코딩 지정과 국제화
     * Charset을 이용한 다양한 인코딩 처리
     */
    public void demonstrateCharsetHandling() {
        System.out.println("4. 문자 인코딩 지정과 국제화");
        
        String multilingualText = "한국어(Korean) English 日本語 中文 العربية Русский";
        
        // 다양한 인코딩으로 파일 저장
        String[] encodings = {"UTF-8", "UTF-16", "ISO-8859-1"};
        
        for (String encoding : encodings) {
            String fileName = FilePathManager.getFilePath("text_" + encoding.replace("-", "_") + ".txt");
            
            try (OutputStreamWriter writer = new OutputStreamWriter(
                    new FileOutputStream(fileName), encoding)) {
                
                writer.write("인코딩: " + encoding + "\n");
                writer.write(multilingualText + "\n");
                
                System.out.println(encoding + " 파일 생성 완료");
                
            } catch (IOException e) {
                System.out.println(encoding + " 파일 생성 오류: " + e.getMessage());
            }
        }
        
        // 각 인코딩으로 저장된 파일 읽기
        for (String encoding : encodings) {
            String fileName = FilePathManager.getFilePath("text_" + encoding.replace("-", "_") + ".txt");
            
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(fileName), encoding))) {
                
                System.out.println("\n" + encoding + "으로 읽은 결과:");
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("  " + line);
                }
                
            } catch (IOException e) {
                System.out.println(encoding + " 파일 읽기 오류: " + e.getMessage());
            }
        }
        
        System.out.println("→ Charset: 글로벌 애플리케이션의 필수 요소\n");
    }
    
    /**
     * 5. 텍스트 파일 효율적 처리 - 줄 단위 읽기
     * 대용량 텍스트 파일의 효율적 처리 방법
     */
    public void demonstrateLineBasedProcessing() {
        System.out.println("5. 텍스트 파일 효율적 처리 - 줄 단위 읽기");
        
        String logFile = FilePathManager.getFilePath("application.log");
        
        // 가상의 로그 파일 생성
        try (PrintWriter logWriter = new PrintWriter(new BufferedWriter(new FileWriter(logFile)))) {
            
            String[] logLevels = {"INFO", "WARN", "ERROR", "DEBUG"};
            String[] messages = {
                "사용자 로그인 성공",
                "메모리 사용량 경고",
                "데이터베이스 연결 실패",
                "캐시 정리 완료"
            };
            
            // 1000줄의 로그 생성
            for (int i = 0; i < 1000; i++) {
                String level = logLevels[i % logLevels.length];
                String message = messages[i % messages.length];
                logWriter.printf("[%s] %s - %s (ID: %d)\n", 
                    level, new java.util.Date(), message, i + 1);
            }
            
            System.out.println("가상 로그 파일 생성 완료 (1000줄)");
            
        } catch (IOException e) {
            System.out.println("로그 파일 생성 오류: " + e.getMessage());
        }
        
        // 로그 파일 분석 - 줄 단위 처리
        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            
            System.out.println("\n로그 파일 분석:");
            
            String line;
            int totalLines = 0;
            int errorCount = 0;
            int warnCount = 0;
            int infoCount = 0;
            
            while ((line = reader.readLine()) != null) {
                totalLines++;
                
                if (line.contains("ERROR")) {
                    errorCount++;
                } else if (line.contains("WARN")) {
                    warnCount++;
                } else if (line.contains("INFO")) {
                    infoCount++;
                }
                
                // 첫 5줄만 출력
                if (totalLines <= 5) {
                    System.out.println("  " + line);
                }
            }
            
            if (totalLines > 5) {
                System.out.println("  ... (" + (totalLines - 5) + "줄 더)");
            }
            
            System.out.println("\n분석 결과:");
            System.out.println("- 총 라인 수: " + totalLines);
            System.out.println("- INFO 로그: " + infoCount);
            System.out.println("- WARN 로그: " + warnCount);
            System.out.println("- ERROR 로그: " + errorCount);
            
        } catch (IOException e) {
            System.out.println("로그 분석 오류: " + e.getMessage());
        }
        
        System.out.println("→ 줄 단위 처리: 메모리 효율적인 대용량 파일 처리\n");
    }
    
    /**
     * 6. Scanner 활용 - 편리한 입력 처리
     * 토큰 기반 파싱과 다양한 데이터 타입 읽기
     */
    public void demonstrateScanner() {
        System.out.println("6. Scanner 활용 - 편리한 입력 처리");
        
        // 다양한 데이터가 포함된 파일 생성
        String dataFile = FilePathManager.getFilePath("scanner_test.txt");
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(dataFile))) {
            writer.println("John 25 3.14 true");
            writer.println("Alice 30 2.71 false");
            writer.println("Bob 35 1.41 true");
            writer.println("# 주석 라인");
            writer.println("Charlie 28 1.73 false");
            
            System.out.println("Scanner 테스트 파일 생성 완료");
            
        } catch (IOException e) {
            System.out.println("파일 생성 오류: " + e.getMessage());
        }
        
        // Scanner로 파일 읽기
        try (Scanner scanner = new Scanner(new FileReader(dataFile))) {
            
            System.out.println("\nScanner로 데이터 파싱:");
            
            int recordNumber = 1;
            
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                
                // 주석 라인 건너뛰기
                if (line.trim().startsWith("#") || line.trim().isEmpty()) {
                    continue;
                }
                
                // 라인을 개별 토큰으로 파싱
                Scanner lineScanner = new Scanner(line);
                
                if (lineScanner.hasNext()) {
                    String name = lineScanner.next();
                    int age = lineScanner.nextInt();
                    double score = lineScanner.nextDouble();
                    boolean active = lineScanner.nextBoolean();
                    
                    System.out.printf("레코드 %d: %s, %d세, 점수 %.2f, 활성 %s\n", 
                        recordNumber++, name, age, score, active);
                }
                
                lineScanner.close();
            }
            
        } catch (IOException e) {
            System.out.println("Scanner 읽기 오류: " + e.getMessage());
        }
        
        // CSV 형식 파일 처리
        String csvFile = FilePathManager.getFilePath("data.csv");
        
        try (PrintWriter csvWriter = new PrintWriter(new FileWriter(csvFile))) {
            csvWriter.println("이름,나이,부서,급여");
            csvWriter.println("김철수,30,개발팀,5000000");
            csvWriter.println("이영희,25,디자인팀,4200000");
            csvWriter.println("박민수,35,마케팅팀,4800000");
            
        } catch (IOException e) {
            System.out.println("CSV 파일 생성 오류: " + e.getMessage());
        }
        
        // Scanner로 CSV 파싱
        try (Scanner csvScanner = new Scanner(new FileReader(csvFile))) {
            csvScanner.useDelimiter(",|\n"); // 쉼표와 줄바꿈을 구분자로 사용
            
            System.out.println("\nCSV 파일 파싱:");
            
            // 헤더 건너뛰기
            if (csvScanner.hasNextLine()) {
                csvScanner.nextLine();
            }
            
            int employeeNumber = 1;
            while (csvScanner.hasNext()) {
                String name = csvScanner.next();
                if (csvScanner.hasNextInt()) {
                    int age = csvScanner.nextInt();
                    String department = csvScanner.next();
                    if (csvScanner.hasNextInt()) {
                        int salary = csvScanner.nextInt();
                        
                        System.out.printf("직원 %d: %s (%d세, %s, %,d원)\n", 
                            employeeNumber++, name, age, department, salary);
                    }
                }
            }
            
        } catch (IOException e) {
            System.out.println("CSV 파싱 오류: " + e.getMessage());
        }
        
        System.out.println("→ Scanner: 토큰 기반 파싱의 강력한 도구\n");
    }
    
    /**
     * 7. 성능 비교 - 버퍼 vs 직접 처리
     * 문자 스트림에서의 버퍼링 효과 측정
     */
    public void demonstrateCharacterBufferPerformance() {
        System.out.println("7. 성능 비교 - 문자 스트림 버퍼링 효과");
        
        // 대용량 테스트 데이터 생성
        StringBuilder largeText = new StringBuilder();
        for (int i = 0; i < 5000; i++) {
            largeText.append("라인 ").append(i + 1).append(": 성능 테스트를 위한 한글과 English 혼합 데이터\n");
        }
        String testData = largeText.toString();
        
        try {
            String testFile = FilePathManager.getFilePath("performance_test_large.txt");
            
            // 테스트 파일 생성
            try (FileWriter writer = new FileWriter(testFile)) {
                writer.write(testData);
            }
            
            // 1. FileReader 직접 사용 (버퍼링 없음)
            long startTime = System.nanoTime();
            try (FileReader directReader = new FileReader(testFile)) {
                char[] buffer = new char[1];
                while (directReader.read(buffer) != -1) {
                    // 문자 하나씩 읽기
                }
            }
            long directTime = System.nanoTime() - startTime;
            
            // 2. BufferedReader 사용 (버퍼링 있음)
            startTime = System.nanoTime();
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(testFile))) {
                while (bufferedReader.readLine() != null) {
                    // 줄 단위로 읽기
                }
            }
            long bufferedTime = System.nanoTime() - startTime;
            
            // 3. 큰 버퍼로 일괄 읽기
            startTime = System.nanoTime();
            try (FileReader bulkReader = new FileReader(testFile)) {
                char[] bigBuffer = new char[8192];
                while (bulkReader.read(bigBuffer) != -1) {
                    // 큰 버퍼로 일괄 읽기
                }
            }
            long bulkTime = System.nanoTime() - startTime;
            
            System.out.println("문자 스트림 성능 비교 결과:");
            System.out.printf("- 문자 단위 읽기: %.2f ms\n", directTime / 1_000_000.0);
            System.out.printf("- BufferedReader: %.2f ms\n", bufferedTime / 1_000_000.0);
            System.out.printf("- 큰 버퍼 읽기: %.2f ms\n", bulkTime / 1_000_000.0);
            
            System.out.printf("성능 향상: BufferedReader가 %,.0f배 빠름\n", 
                (double) directTime / bufferedTime);
            
        } catch (IOException e) {
            System.out.println("성능 비교 오류: " + e.getMessage());
        }
        
        System.out.println("→ 버퍼링: 문자 스트림 성능의 핵심 요소\n");
    }
    
    /**
     * 8. 실무 활용 - 대용량 텍스트 처리
     * 메모리 효율적인 대용량 파일 처리 패턴
     */
    public void demonstrateLargeTextProcessing() {
        System.out.println("8. 실무 활용 - 대용량 텍스트 처리");
        
        String sourceFile = FilePathManager.getFilePath("large_source.txt");
        String processedFile = FilePathManager.getFilePath("processed_output.txt");
        
        // 대용량 소스 파일 생성 (시뮬레이션)
        try (PrintWriter sourceWriter = new PrintWriter(
                new BufferedWriter(new FileWriter(sourceFile)))) {
            
            for (int i = 0; i < 2000; i++) {
                sourceWriter.printf("데이터 %04d: 처리 대상 텍스트 라인입니다. 번호=%d\n", i + 1, i + 1);
            }
            
            System.out.println("대용량 소스 파일 생성 완료 (2000줄)");
            
        } catch (IOException e) {
            System.out.println("소스 파일 생성 오류: " + e.getMessage());
        }
        
        // 스트리밍 방식으로 파일 처리 (메모리 효율적)
        try (BufferedReader reader = new BufferedReader(new FileReader(sourceFile));
             PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(processedFile)))) {
            
            System.out.println("\n대용량 파일 스트리밍 처리:");
            
            String line;
            int processedLines = 0;
            int filteredLines = 0;
            
            while ((line = reader.readLine()) != null) {
                processedLines++;
                
                // 조건부 필터링 (홀수 번호만 처리)
                if (line.contains("번호=" + (processedLines * 2 - 1))) {
                    writer.println("처리됨: " + line);
                    filteredLines++;
                }
                
                // 진행 상황 출력 (매 500줄마다)
                if (processedLines % 500 == 0) {
                    System.out.println("  진행: " + processedLines + "줄 처리 완료");
                }
            }
            
            System.out.println("처리 완료:");
            System.out.println("- 원본 라인 수: " + processedLines);
            System.out.println("- 필터링된 라인 수: " + filteredLines);
            System.out.println("- 메모리 사용량: 최소화 (스트리밍 방식)");
            
        } catch (IOException e) {
            System.out.println("대용량 파일 처리 오류: " + e.getMessage());
        }
        
        // 처리 결과 확인 (처음 10줄만)
        try (BufferedReader resultReader = new BufferedReader(new FileReader(processedFile))) {
            
            System.out.println("\n처리 결과 (처음 10줄):");
            String line;
            int count = 0;
            
            while ((line = resultReader.readLine()) != null && count < 10) {
                System.out.println("  " + line);
                count++;
            }
            
        } catch (IOException e) {
            System.out.println("결과 확인 오류: " + e.getMessage());
        }
        
        System.out.println("→ 실무: 스트리밍 방식으로 메모리 효율적인 대용량 처리\n");
    }
}