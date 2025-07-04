package com.JavaCoreTil.io.io_example;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import com.JavaCoreTil.io.common.FilePathManager;

/**
 * I/O 로드맵 4단계: 문자 기반 스트림 (Character-Based Streams)
 * 
 * 학습 목표:
 * 1. Reader/Writer - 문자 스트림의 최상위 클래스
 * 2. FileReader/FileWriter - 파일 문자 입출력
 * 3. StringReader/StringWriter - 문자열 입출력
 * 4. PipedReader/PipedWriter - 스레드 간 통신
 * 5. 문자 인코딩 처리 - UTF-8, 다국어 지원
 * 6. 바이트 스트림과의 차이점 - 문자 단위 처리
 */
public class Stage4_CharacterBasedStreams {
    
    private static final boolean DELETE_FLAG = false;
    
    public static void main(String[] args) {
        Stage4_CharacterBasedStreams demo = new Stage4_CharacterBasedStreams();
        
        FilePathManager.ensureDirectoryExists();
        
        System.out.println("=== I/O 로드맵 4단계: 문자 기반 스트림 ===\n");
        
        try {
            // 1. Reader/Writer 추상 클래스의 특징
            demo.demonstrateReaderWriterConcepts();
            
            // 2. FileReader/FileWriter - 파일 문자 처리
            demo.demonstrateFileReaderWriter();
            
            // 3. StringReader/StringWriter - 메모리 기반 문자 처리
            demo.demonstrateStringReaderWriter();
            
            // 4. 문자 인코딩과 다국어 처리
            demo.demonstrateCharacterEncoding();
            
            // 5. 바이트 스트림 vs 문자 스트림 비교
            demo.demonstrateByteVsCharacterStreams();
            
            // 6. PipedReader/PipedWriter - 스레드 간 통신
            demo.demonstratePipedReaderWriter();
            
            // 7. 실무 활용 - 텍스트 파일 처리
            demo.demonstratePracticalTextProcessing();
            
            System.out.println("\n=== 4단계 학습 완료! ===");
            
        } finally {
            System.out.println("\n=== 파일 정리 ===");
            FilePathManager.cleanupFiles(DELETE_FLAG);
        }
    }
    
    /**
     * 1. Reader/Writer 추상 클래스의 특징
     * 문자 단위 처리, 유니코드 지원, 바이트 스트림과의 차이점
     */
    public void demonstrateReaderWriterConcepts() {
        System.out.println("1. Reader/Writer 추상 클래스의 특징");
        
        try {
            // Writer의 기본 메서드들
            FileWriter writer = new FileWriter(FilePathManager.getFilePath("reader_writer_demo.txt"));
            
            System.out.println("Writer의 주요 메서드:");
            
            // 1. write(int c) - 단일 문자
            writer.write('안');
            writer.write('녕');
            System.out.println("- write(int c): 단일 문자 쓰기");
            
            // 2. write(char[] cbuf) - 문자 배열
            char[] chars = {'하', '세', '요'};
            writer.write(chars);
            System.out.println("- write(char[] cbuf): 문자 배열 쓰기");
            
            // 3. write(String str) - 문자열
            writer.write("\nJava I/O 문자 스트림");
            System.out.println("- write(String str): 문자열 쓰기");
            
            writer.close();
            
        } catch (IOException e) {
            System.out.println("Writer 오류: " + e.getMessage());
        }
        
        try {
            // Reader의 기본 메서드들
            FileReader reader = new FileReader(FilePathManager.getFilePath("reader_writer_demo.txt"));
            
            System.out.println("\nReader의 주요 메서드:");
            
            // 1. read() - 단일 문자 읽기
            int firstChar = reader.read();
            System.out.println("- read(): 첫 번째 문자 = " + (char)firstChar + " (Unicode: " + firstChar + ")");
            
            // 2. read(char[] cbuf) - 문자 배열로 읽기
            char[] buffer = new char[10];
            int charsRead = reader.read(buffer);
            System.out.println("- read(char[] cbuf): " + charsRead + "문자 읽음");
            System.out.println("  읽은 내용: " + new String(buffer, 0, charsRead));
            
            reader.close();
            
        } catch (IOException e) {
            System.out.println("Reader 오류: " + e.getMessage());
        }
        
        System.out.println("→ Reader/Writer: 문자 단위 처리, 유니코드 완벽 지원\n");
    }
    
    /**
     * 2. FileReader/FileWriter - 파일 문자 처리
     * 텍스트 파일 읽기/쓰기의 기본
     */
    public void demonstrateFileReaderWriter() {
        System.out.println("2. FileReader/FileWriter - 파일 문자 처리");
        
        String textFile = FilePathManager.getFilePath("korean_text.txt");
        
        // 다국어 텍스트 파일 생성
        try (FileWriter writer = new FileWriter(textFile)) {
            writer.write("=== 다국어 텍스트 파일 ===\n");
            writer.write("한글: 안녕하세요!\n");
            writer.write("English: Hello World!\n");
            writer.write("日本語: こんにちは!\n");
            writer.write("中文: 你好!\n");
            writer.write("Emoji: 😀🎉🌟\n");
            
            System.out.println("다국어 텍스트 파일 생성 완료");
            
        } catch (IOException e) {
            System.out.println("파일 쓰기 오류: " + e.getMessage());
        }
        
        // 파일 읽기 및 내용 출력
        try (FileReader reader = new FileReader(textFile)) {
            System.out.println("\n파일 내용 읽기:");
            
            char[] buffer = new char[1024];
            int charsRead = reader.read(buffer);
            String content = new String(buffer, 0, charsRead);
            
            System.out.println(content);
            
        } catch (IOException e) {
            System.out.println("파일 읽기 오류: " + e.getMessage());
        }
        
        System.out.println("→ FileReader/Writer: 텍스트 파일의 기본 처리 도구\n");
    }
    
    /**
     * 3. StringReader/StringWriter - 메모리 기반 문자 처리
     * 문자열을 스트림처럼 처리할 때 유용
     */
    public void demonstrateStringReaderWriter() {
        System.out.println("3. StringReader/StringWriter - 메모리 기반 문자 처리");
        
        // StringWriter로 메모리에서 문자열 조립
        StringWriter stringWriter = new StringWriter();

		stringWriter.write("StringWriter로 문자열 조립:\n");
		stringWriter.write("- 첫 번째 라인\n");
		stringWriter.write("- 두 번째 라인\n");
		stringWriter.write("- 세 번째 라인\n");

		// 현재까지 조립된 문자열 확인
		String assembledString = stringWriter.toString();
		System.out.println("조립된 문자열:");
		System.out.println(assembledString);

		// StringReader로 문자열을 스트림처럼 읽기
        String sourceText = "줄1: 첫 번째 줄\n줄2: 두 번째 줄\n줄3: 세 번째 줄";
        StringReader stringReader = new StringReader(sourceText);
        
        try {
            System.out.println("\nStringReader로 문자열 스트림 읽기:");
            
            char[] buffer = new char[100];
            int charsRead = stringReader.read(buffer);
            System.out.println("읽은 내용: " + new String(buffer, 0, charsRead));
            
        } catch (IOException e) {
            System.out.println("StringReader 오류: " + e.getMessage());
        }
        
        System.out.println("→ StringReader/Writer: 메모리 기반 문자열 스트림 처리\n");
    }
    
    /**
     * 4. 문자 인코딩과 다국어 처리
     * 바이트와 문자 간 변환, 다양한 인코딩 지원
     */
    public void demonstrateCharacterEncoding() {
        System.out.println("4. 문자 인코딩과 다국어 처리");
        
        String multilingualText = "한글 English 日本語 中文 🌍";
        
        // 다양한 인코딩으로 바이트 변환 확인
        System.out.println("원본 텍스트: " + multilingualText);
        System.out.println("문자 수: " + multilingualText.length());
        
        try {
            // UTF-8 인코딩
            byte[] utf8Bytes = multilingualText.getBytes(StandardCharsets.UTF_8);
            System.out.println("UTF-8 바이트 수: " + utf8Bytes.length);
            
            // UTF-16 인코딩
            byte[] utf16Bytes = multilingualText.getBytes(StandardCharsets.UTF_16);
            System.out.println("UTF-16 바이트 수: " + utf16Bytes.length);
            
        } catch (Exception e) {
            System.out.println("인코딩 오류: " + e.getMessage());
        }
        
        // 시스템 기본 인코딩 정보
        System.out.println("시스템 기본 Charset: " + Charset.defaultCharset());
        
        System.out.println("→ 문자 인코딩: 다국어 처리의 핵심, UTF-8 권장\n");
    }
    
    /**
     * 5. 바이트 스트림 vs 문자 스트림 비교
     * 언제 어떤 스트림을 사용해야 하는지 명확히 구분
     */
    public void demonstrateByteVsCharacterStreams() {
        System.out.println("5. 바이트 스트림 vs 문자 스트림 비교");
        
        String testText = "한글ABC123!@#";
        
        try {
            String byteFile = FilePathManager.getFilePath("byte_test.dat");
            String charFile = FilePathManager.getFilePath("char_test.txt");
            
            // 1. 바이트 스트림으로 처리
            System.out.println("=== 바이트 스트림 처리 ===");
            try (FileOutputStream byteOut = new FileOutputStream(byteFile)) {
                byte[] bytes = testText.getBytes(StandardCharsets.UTF_8);
                byteOut.write(bytes);
                System.out.println("바이트 스트림 쓰기: " + bytes.length + " 바이트");
            }
            
            // 2. 문자 스트림으로 처리
            System.out.println("\n=== 문자 스트림 처리 ===");
            try (FileWriter charOut = new FileWriter(charFile)) {
                charOut.write(testText);
                System.out.println("문자 스트림 쓰기: " + testText.length() + " 문자");
            }
            
        } catch (IOException e) {
            System.out.println("스트림 비교 오류: " + e.getMessage());
        }
        
        System.out.println("\n사용 가이드:");
        System.out.println("- 텍스트 데이터: 문자 스트림 (Reader/Writer)");
        System.out.println("- 바이너리 데이터: 바이트 스트림 (InputStream/OutputStream)");
        System.out.println("- 다국어 텍스트: 문자 스트림 + 적절한 인코딩\n");
    }
    
    /**
     * 6. PipedReader/PipedWriter - 스레드 간 통신
     * 프로듀서-컨슈머 패턴의 구현
     */
    public void demonstratePipedReaderWriter() {
        System.out.println("6. PipedReader/PipedWriter - 스레드 간 통신");
        
        try {
            PipedWriter writer = new PipedWriter();
            PipedReader reader = new PipedReader(writer);
            
            // 프로듀서 스레드 (데이터 생성)
            Thread producer = new Thread(() -> {
                try {
                    System.out.println("프로듀서: 데이터 생성 시작");
                    
                    for (int i = 1; i <= 3; i++) {
                        String message = "메시지 " + i + "\n";
                        writer.write(message);
                        writer.flush();
                        System.out.println("프로듀서: " + message.trim() + " 전송");
                        Thread.sleep(500);
                    }
                    
                    writer.close();
                    System.out.println("프로듀서: 데이터 전송 완료");
                    
                } catch (IOException | InterruptedException e) {
                    System.out.println("프로듀서 오류: " + e.getMessage());
                }
            });
            
            // 컨슈머 스레드 (데이터 소비)
            Thread consumer = new Thread(() -> {
                try {
                    System.out.println("컨슈머: 데이터 수신 대기");
                    
                    char[] buffer = new char[1000];
                    int charsRead;
                    
                    while ((charsRead = reader.read(buffer)) != -1) {
                        String received = new String(buffer, 0, charsRead);
                        System.out.println("컨슈머: 수신 -> " + received.trim());
                    }
                    
                    reader.close();
                    System.out.println("컨슈머: 데이터 수신 완료");
                    
                } catch (IOException e) {
                    System.out.println("컨슈머 오류: " + e.getMessage());
                }
            });
            
            // 스레드 시작 및 완료 대기
            producer.start();
            consumer.start();
            
            producer.join();
            consumer.join();
            
        } catch (IOException | InterruptedException e) {
            System.out.println("PipedReader/Writer 오류: " + e.getMessage());
        }
        
        System.out.println("→ PipedReader/Writer: 스레드 간 안전한 문자 데이터 교환\n");
    }
    
    /**
     * 7. 실무 활용 - 텍스트 파일 처리
     * 설정 파일 읽기, CSV 처리 등 실무 패턴
     */
    public void demonstratePracticalTextProcessing() {
        System.out.println("7. 실무 활용 - 텍스트 파일 처리");
        
        // 설정 파일 생성 및 읽기
        String configFile = FilePathManager.getFilePath("app.config");
        
        try (FileWriter configWriter = new FileWriter(configFile)) {
            configWriter.write("# 애플리케이션 설정 파일\n");
            configWriter.write("server.port=8080\n");
            configWriter.write("database.url=jdbc:mysql://localhost:3306/mydb\n");
            configWriter.write("logging.level=INFO\n");
            
            System.out.println("설정 파일 생성 완료");
        } catch (IOException e) {
            System.out.println("설정 파일 생성 오류: " + e.getMessage());
        }
        
        // 설정 파일 파싱
        try (FileReader configReader = new FileReader(configFile)) {
            StringBuilder content = new StringBuilder();
            char[] buffer = new char[1024];
            int charsRead;
            
            while ((charsRead = configReader.read(buffer)) != -1) {
                content.append(buffer, 0, charsRead);
            }
            
            System.out.println("\n설정 파일 내용:");
            String[] lines = content.toString().split("\n");
            
            for (String line : lines) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue; // 빈 줄과 주석 건너뛰기
                }
                
                if (line.contains("=")) {
                    String[] parts = line.split("=", 2);
                    System.out.println("  " + parts[0] + " -> " + parts[1]);
                }
            }
            
        } catch (IOException e) {
            System.out.println("설정 파일 읽기 오류: " + e.getMessage());
        }
        
        System.out.println("→ 실무: 문자 스트림으로 텍스트 기반 데이터 안전하게 처리\n");
    }
} 