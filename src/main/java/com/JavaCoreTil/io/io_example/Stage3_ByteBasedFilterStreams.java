package com.JavaCoreTil.io.io_example;

import java.io.*;
import java.util.Vector;
import com.JavaCoreTil.io.common.FilePathManager;

/**
 * I/O 로드맵 3단계: 바이트 기반 보조 스트림 (Filter Streams)
 * 
 * 학습 목표:
 * 1. FilterInputStream/FilterOutputStream - 보조 스트림 기반 클래스
 * 2. BufferedInputStream/BufferedOutputStream - 버퍼링을 통한 성능 향상
 * 3. DataInputStream/DataOutputStream - 기본 타입 데이터 입출력
 * 4. PrintStream - 형식화된 출력, System.out
 * 5. SequenceInputStream - 여러 스트림 연결
 * 6. 보조 스트림 조합 - 다중 기능 적용
 */
public class Stage3_ByteBasedFilterStreams {
    
    private static final boolean DELETE_FLAG = false;
    
    public static void main(String[] args) {
        Stage3_ByteBasedFilterStreams demo = new Stage3_ByteBasedFilterStreams();
        
        FilePathManager.ensureDirectoryExists();
        
        System.out.println("=== I/O 로드맵 3단계: 바이트 기반 보조 스트림 ===\n");
        
        try {
            // 1. Filter 스트림의 개념과 데코레이터 패턴
            demo.demonstrateFilterStreamConcept();
            
            // 2. BufferedInputStream/OutputStream - 성능 최적화
            demo.demonstrateBufferedStreams();
            
            // 3. DataInputStream/OutputStream - 타입별 데이터 처리
            demo.demonstrateDataStreams();
            
            // 4. PrintStream - 형식화된 출력
            demo.demonstratePrintStream();
            
            // 5. SequenceInputStream - 스트림 연결
            demo.demonstrateSequenceInputStream();
            
            // 6. 보조 스트림 조합 - 다중 기능
            demo.demonstrateStreamCombination();
            
            // 7. 성능 비교 - 버퍼 vs 직접 접근
            demo.demonstratePerformanceComparison();
            
            // 8. 실무 활용 패턴
            demo.demonstratePracticalUsage();
            
            System.out.println("\n=== 3단계 학습 완료! ===");
            
        } finally {
            System.out.println("\n=== 파일 정리 ===");
            FilePathManager.cleanupFiles(DELETE_FLAG);
        }
    }
    
    /**
     * 1. Filter 스트림의 개념과 데코레이터 패턴
     * 보조 스트림은 혼자서는 입출력을 수행할 수 없고, 다른 스트림과 연결되어 사용
     */
    public void demonstrateFilterStreamConcept() {
        System.out.println("1. Filter 스트림의 개념과 데코레이터 패턴");
        
        try {
            // 기본 스트림 생성
            FileOutputStream fileOutput = new FileOutputStream(FilePathManager.getFilePath("filter_demo.txt"));
            
            // 보조 스트림으로 감싸기 (데코레이터 패턴)
            BufferedOutputStream bufferedOutput = new BufferedOutputStream(fileOutput);
            
            System.out.println("기본 스트림: " + fileOutput.getClass().getSimpleName());
            System.out.println("보조 스트림: " + bufferedOutput.getClass().getSimpleName());
            System.out.println("→ 보조 스트림이 기본 스트림을 감싸서 추가 기능 제공");
            
            // 데이터 쓰기
            String data = "보조 스트림을 통한 데이터 쓰기\n";
            bufferedOutput.write(data.getBytes());
            bufferedOutput.close(); // 보조 스트림을 닫으면 기본 스트림도 함께 닫힘
            
            System.out.println("→ 보조 스트림 close() 시 기본 스트림도 자동 close()");
            
        } catch (IOException e) {
            System.out.println("Filter 스트림 오류: " + e.getMessage());
        }
        
        // 실제 Filter Stream 구현 예시
        demonstrateCustomFilterStream();
        
        System.out.println();
    }
    
    /**
     * 커스텀 Filter Stream 구현 예시
     * 대문자 변환 필터와 암호화 필터를 직접 구현
     */
    private void demonstrateCustomFilterStream() {
        System.out.println("\n=== 커스텀 Filter Stream 구현 ===");
        
        try {
            // 1. 대문자 변환 Filter 출력 스트림
            FileOutputStream baseOutput = new FileOutputStream(FilePathManager.getFilePath("uppercase_output.txt"));
            UpperCaseFilterOutputStream uppercaseFilter = new UpperCaseFilterOutputStream(baseOutput);
            
            String originalText = "Hello World! Java Filter Stream Example.";
            System.out.println("원본 텍스트: " + originalText);
            
            uppercaseFilter.write(originalText.getBytes());
            uppercaseFilter.close();
            
            // 결과 확인
            try (FileInputStream input = new FileInputStream(FilePathManager.getFilePath("uppercase_output.txt"))) {
                byte[] result = input.readAllBytes();
                System.out.println("변환된 텍스트: " + new String(result));
            }
            
        } catch (IOException e) {
            System.out.println("커스텀 Filter 오류: " + e.getMessage());
        }
        
        try {
            // 2. ROT13 암호화 Filter (읽기/쓰기)
            String secretMessage = "Secret Message for ROT13 Encryption!";
            System.out.println("\n원본 메시지: " + secretMessage);
            
            // 암호화하여 저장
            FileOutputStream encryptOutput = new FileOutputStream(FilePathManager.getFilePath("encrypted.txt"));
            ROT13FilterOutputStream encryptFilter = new ROT13FilterOutputStream(encryptOutput);
            encryptFilter.write(secretMessage.getBytes());
            encryptFilter.close();
            
            // 암호화된 파일 내용 확인
            try (FileInputStream rawInput = new FileInputStream(FilePathManager.getFilePath("encrypted.txt"))) {
                byte[] encrypted = rawInput.readAllBytes();
                System.out.println("암호화된 텍스트: " + new String(encrypted));
            }
            
            // 복호화하여 읽기
            FileInputStream decryptInput = new FileInputStream(FilePathManager.getFilePath("encrypted.txt"));
            ROT13FilterInputStream decryptFilter = new ROT13FilterInputStream(decryptInput);
            byte[] decrypted = decryptFilter.readAllBytes();
            decryptFilter.close();
            
            System.out.println("복호화된 텍스트: " + new String(decrypted));
            
        } catch (IOException e) {
            System.out.println("ROT13 Filter 오류: " + e.getMessage());
        }
        
        System.out.println("→ Filter Stream: 기본 기능에 추가 처리 로직을 투명하게 적용");
    }
    
    /**
     * 대문자 변환 Filter Output Stream
     * 모든 소문자를 대문자로 변환하는 필터
     */
    static class UpperCaseFilterOutputStream extends FilterOutputStream {
        public UpperCaseFilterOutputStream(OutputStream out) {
            super(out);
        }
        
        @Override
        public void write(int b) throws IOException {
            // 소문자인 경우 대문자로 변환
            if (b >= 'a' && b <= 'z') {
                b = b - 'a' + 'A';
            }
            super.write(b);
        }
        
        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            // 배열의 각 바이트를 개별적으로 처리
            for (int i = 0; i < len; i++) {
                write(b[off + i]);
            }
        }
    }
    
    /**
     * ROT13 암호화 Filter Output Stream
     * 알파벳을 13자리씩 이동시키는 간단한 암호화
     */
    static class ROT13FilterOutputStream extends FilterOutputStream {
        public ROT13FilterOutputStream(OutputStream out) {
            super(out);
        }
        
        @Override
        public void write(int b) throws IOException {
            super.write(rot13Transform(b));
        }
        
        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            for (int i = 0; i < len; i++) {
                write(b[off + i]);
            }
        }
        
        private int rot13Transform(int b) {
            if (b >= 'A' && b <= 'Z') {
                return ((b - 'A' + 13) % 26) + 'A';
            } else if (b >= 'a' && b <= 'z') {
                return ((b - 'a' + 13) % 26) + 'a';
            }
            return b; // 알파벳이 아닌 경우 그대로 반환
        }
    }
    
    /**
     * ROT13 복호화 Filter Input Stream
     * ROT13는 자기 자신이 역함수이므로 동일한 변환 적용
     */
    static class ROT13FilterInputStream extends FilterInputStream {
        public ROT13FilterInputStream(InputStream in) {
            super(in);
        }
        
        @Override
        public int read() throws IOException {
            int b = super.read();
            if (b == -1) return -1;
            return rot13Transform(b);
        }
        
        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            int bytesRead = super.read(b, off, len);
            if (bytesRead == -1) return -1;
            
            // 읽은 바이트들을 변환
            for (int i = 0; i < bytesRead; i++) {
                b[off + i] = (byte) rot13Transform(b[off + i]);
            }
            return bytesRead;
        }
        
        private int rot13Transform(int b) {
            if (b >= 'A' && b <= 'Z') {
                return ((b - 'A' + 13) % 26) + 'A';
            } else if (b >= 'a' && b <= 'z') {
                return ((b - 'a' + 13) % 26) + 'a';
            }
            return b;
        }
    }
    
    /**
     * 2. BufferedInputStream/OutputStream - 성능 최적화의 핵심
     * 내부 버퍼를 통해 시스템 호출 횟수를 줄여 성능 향상
     */
    public void demonstrateBufferedStreams() {
        System.out.println("2. BufferedInputStream/OutputStream - 성능 최적화");
        
        // 대용량 테스트 데이터 준비
        StringBuilder largeData = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            largeData.append("라인 ").append(i + 1).append(": 버퍼 스트림 성능 테스트 데이터\n");
        }
        byte[] testData = largeData.toString().getBytes();
        
        try {
            // 1. 버퍼 없이 직접 쓰기
            long startTime = System.nanoTime();
            try (FileOutputStream directOutput = new FileOutputStream(FilePathManager.getFilePath("direct_write.txt"))) {
                for (byte b : testData) {
                    directOutput.write(b); // 바이트 하나씩 쓰기 (매우 비효율)
                }
            }
            long directTime = System.nanoTime() - startTime;
            
            // 2. 버퍼를 사용한 쓰기 (기본 버퍼 크기: 8192바이트)
            startTime = System.nanoTime();
            try (BufferedOutputStream bufferedOutput = new BufferedOutputStream(
                    new FileOutputStream(FilePathManager.getFilePath("buffered_write.txt")))) {
                for (byte b : testData) {
                    bufferedOutput.write(b); // 내부 버퍼에 저장 후 일괄 처리
                }
            } // try-with-resources에서 자동으로 flush() 및 close() 호출
            long bufferedTime = System.nanoTime() - startTime;
            
            // 3. 커스텀 버퍼 크기 지정
            try (BufferedOutputStream customBuffered = new BufferedOutputStream(
                    new FileOutputStream(FilePathManager.getFilePath("custom_buffered.txt")), 16384)) { // 16KB 버퍼
                customBuffered.write(testData);
                System.out.println("커스텀 버퍼 크기: 16KB");
            }
            
            System.out.printf("직접 쓰기 시간: %.2f ms\n", directTime / 1_000_000.0);
            System.out.printf("버퍼 쓰기 시간: %.2f ms\n", bufferedTime / 1_000_000.0);
            System.out.printf("성능 향상: %.0f배\n", (double) directTime / bufferedTime);
            
        } catch (IOException e) {
            System.out.println("Buffered 스트림 오류: " + e.getMessage());
        }

        System.out.println("=".repeat(50));
        
        // 읽기 성능 비교
        try {
            // 직접 읽기 vs 버퍼 읽기
            long startTime = System.nanoTime();
            try (FileInputStream directInput = new FileInputStream(FilePathManager.getFilePath("direct_write.txt"))) {
                while (directInput.read() != -1) {
                    // 바이트 하나씩 읽기
                }
            }
            long directReadTime = System.nanoTime() - startTime;
            
            startTime = System.nanoTime();
            try (BufferedInputStream bufferedInput = new BufferedInputStream(
                    new FileInputStream(FilePathManager.getFilePath("buffered_write.txt")))) {
                while (bufferedInput.read() != -1) {
                    // 내부 버퍼를 통한 읽기
                }
            }
            long bufferedReadTime = System.nanoTime() - startTime;
            
            System.out.printf("직접 읽기 시간: %.2f ms\n", directReadTime / 1_000_000.0);
            System.out.printf("버퍼 읽기 시간: %.2f ms\n", bufferedReadTime / 1_000_000.0);
            System.out.printf("읽기 성능 향상: %.0f배\n", (double) directReadTime / bufferedReadTime);
            
        } catch (IOException e) {
            System.out.println("읽기 성능 비교 오류: " + e.getMessage());
        }
        
        System.out.println("→ BufferedStream: 시스템 호출 최소화로 대폭적인 성능 향상\n");
    }
    
    /**
     * 3. DataInputStream/OutputStream - 타입별 데이터 처리
     * 기본 데이터 타입을 바이너리 형태로 안전하게 입출력
     */
    public void demonstrateDataStreams() {
        System.out.println("3. DataInputStream/OutputStream - 타입별 데이터 처리");
        
        String dataFile = FilePathManager.getFilePath("data_types.bin");
        
        // 다양한 데이터 타입 쓰기
        try (DataOutputStream dos = new DataOutputStream(
                new BufferedOutputStream(new FileOutputStream(dataFile)))) {
            
            // 기본 타입별 데이터 쓰기
            dos.writeBoolean(true);
            dos.writeByte(127);
            dos.writeShort(32767);
            dos.writeInt(2147483647);
            dos.writeLong(9223372036854775807L);
            dos.writeFloat(3.14159f);
            dos.writeDouble(2.718281828459045);
            dos.writeChar('A');
            dos.writeUTF("DataStream으로 저장된 문자열");
            
            System.out.println("다양한 데이터 타입 저장 완료:");
            System.out.println("- boolean: true");
            System.out.println("- byte: 127");
            System.out.println("- short: 32767");
            System.out.println("- int: 2147483647");
            System.out.println("- long: 9223372036854775807");
            System.out.println("- float: 3.14159");
            System.out.println("- double: 2.718281828459045");
            System.out.println("- char: 'A'");
            System.out.println("- UTF 문자열: DataStream으로 저장된 문자열");
            
        } catch (IOException e) {
            System.out.println("DataOutputStream 오류: " + e.getMessage());
        }
        
        // 저장된 데이터 읽기 (순서가 중요!)
        try (DataInputStream dis = new DataInputStream(
                new BufferedInputStream(new FileInputStream(dataFile)))) {
            
            System.out.println("\n저장된 데이터 읽기:");
            System.out.println("- boolean: " + dis.readBoolean());
            System.out.println("- byte: " + dis.readByte());
            System.out.println("- short: " + dis.readShort());
            System.out.println("- int: " + dis.readInt());
            System.out.println("- long: " + dis.readLong());
            System.out.println("- float: " + dis.readFloat());
            System.out.println("- double: " + dis.readDouble());
            System.out.println("- char: '" + dis.readChar() + "'");
            System.out.println("- UTF 문자열: " + dis.readUTF());
            
        } catch (IOException e) {
            System.out.println("DataInputStream 오류: " + e.getMessage());
        }
        
        // 파일 크기 확인
        File file = new File(dataFile);
        System.out.println("\n바이너리 파일 크기: " + file.length() + " 바이트");
        System.out.println("→ DataStream: 타입 안전성과 공간 효율성 제공\n");
    }
    
    /**
     * 4. PrintStream - 형식화된 출력의 강력함
     * System.out도 PrintStream의 인스턴스
     */
    public void demonstratePrintStream() {
        System.out.println("4. PrintStream - 형식화된 출력");
        
        try (PrintStream ps = new PrintStream(
                new BufferedOutputStream(new FileOutputStream(FilePathManager.getFilePath("print_output.txt"))))) {
            
            // 1. 기본 출력 메서드들
            ps.println("=== PrintStream 기능 테스트 ===");
            ps.print("줄바꿈 없는 출력, ");
            ps.println("줄바꿈 있는 출력");
            
            // 2. printf로 형식화된 출력
            ps.printf("정수: %d, 실수: %.2f, 문자열: %s\n", 42, 3.14159, "포맷팅");
            ps.printf("16진수: %x, 8진수: %o, 과학표기법: %e\n", 255, 64, 1234.5);
            
            // 3. 다양한 데이터 타입 출력
            ps.println("boolean: " + true);
            ps.println("char: " + 'X');
            ps.println("int array: " + java.util.Arrays.toString(new int[]{1, 2, 3}));
            
            // 4. 객체 출력 (toString() 자동 호출)
            ps.println("현재 시간: " + new java.util.Date());
            
            System.out.println("PrintStream 출력 완료");
            
        } catch (IOException e) {
            System.out.println("PrintStream 오류: " + e.getMessage());
        }
        
        // 생성된 파일 내용 확인
        try (BufferedReader reader = new BufferedReader(
                new FileReader(FilePathManager.getFilePath("print_output.txt")))) {
            
            System.out.println("\n생성된 파일 내용:");
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("  " + line);
            }
            
        } catch (IOException e) {
            System.out.println("파일 읽기 오류: " + e.getMessage());
        }
        
        // System.out도 PrintStream임을 증명
        System.out.println("\nSystem.out 클래스: " + System.out.getClass().getSimpleName());
        System.out.println("→ PrintStream: 개발자 친화적인 출력 인터페이스 제공\n");
    }
    
    /**
     * 5. SequenceInputStream - 여러 스트림을 하나로 연결
     * 분할된 파일이나 데이터를 순차적으로 처리할 때 유용
     */
    public void demonstrateSequenceInputStream() {
        System.out.println("5. SequenceInputStream - 여러 스트림 연결");
        
        try {
            // 여러 개의 소스 파일 생성
            String[] contents = {
                "첫 번째 파일의 내용입니다.\n",
                "두 번째 파일의 내용입니다.\n", 
                "세 번째 파일의 내용입니다.\n"
            };
            
            String[] fileNames = {"part1.txt", "part2.txt", "part3.txt"};
            
            // 분할 파일들 생성
            for (int i = 0; i < fileNames.length; i++) {
                try (FileOutputStream fos = new FileOutputStream(FilePathManager.getFilePath(fileNames[i]))) {
                    fos.write(contents[i].getBytes());
                }
                System.out.println(fileNames[i] + " 생성 완료");
            }
            
            // Vector를 사용한 여러 스트림 연결
            Vector<InputStream> streams = new Vector<>();
            for (String fileName : fileNames) {
                streams.add(new FileInputStream(FilePathManager.getFilePath(fileName)));
            }
            
            // SequenceInputStream으로 모든 스트림 연결
            try (SequenceInputStream sis = new SequenceInputStream(streams.elements())) {
                
                System.out.println("\n연결된 스트림에서 전체 내용 읽기:");
                byte[] buffer = new byte[1024];
                int bytesRead;
                StringBuilder result = new StringBuilder();
                
                while ((bytesRead = sis.read(buffer)) != -1) {
                    result.append(new String(buffer, 0, bytesRead));
                }
                
                System.out.println("=== 합쳐진 내용 ===");
                System.out.print(result.toString());
                System.out.println("=== 끝 ===");
                
            }
            
            // 두 개 스트림만 연결하는 간단한 방법
            try (SequenceInputStream simple = new SequenceInputStream(
                    new FileInputStream(FilePathManager.getFilePath("part1.txt")),
                    new FileInputStream(FilePathManager.getFilePath("part2.txt")))) {
                
                System.out.println("\n간단한 두 스트림 연결:");
                String simpleResult = new String(simple.readAllBytes());
                System.out.print(simpleResult);
            }
            
        } catch (IOException e) {
            System.out.println("SequenceInputStream 오류: " + e.getMessage());
        }
        
        System.out.println("→ SequenceInputStream: 여러 입력 소스를 하나로 통합\n");
    }
    
    /**
     * 6. 보조 스트림 조합 - 여러 기능을 동시에 활용
     * 실무에서 가장 중요한 패턴
     */
    public void demonstrateStreamCombination() {
        System.out.println("6. 보조 스트림 조합 - 다중 기능 적용");
        
        String combinedFile = FilePathManager.getFilePath("combined_features.bin");
        
        // 쓰기: File → Buffer → Data (안쪽부터 바깥쪽 순서)
        try (DataOutputStream dos = new DataOutputStream(
                new BufferedOutputStream(
                    new FileOutputStream(combinedFile), 4096))) { // 4KB 버퍼
            
            System.out.println("조합된 스트림 구조: File ← Buffer ← Data");
            System.out.println("- FileOutputStream: 파일 시스템 접근");
            System.out.println("- BufferedOutputStream: 4KB 버퍼로 성능 최적화");
            System.out.println("- DataOutputStream: 타입 안전 데이터 쓰기");
            
            // 복잡한 데이터 구조 저장
            dos.writeUTF("상품 정보");
            dos.writeInt(12345); // 상품 ID
            dos.writeUTF("MacBook Pro"); // 상품명
            dos.writeDouble(2490000.0); // 가격
            dos.writeBoolean(true); // 재고 있음
            dos.writeLong(System.currentTimeMillis()); // 등록 시간
            
            System.out.println("복합 데이터 저장 완료");
            
        } catch (IOException e) {
            System.out.println("조합된 쓰기 스트림 오류: " + e.getMessage());
        }
        
        // 읽기: File → Buffer → Data (동일한 조합)
        try (DataInputStream dis = new DataInputStream(
                new BufferedInputStream(
                    new FileInputStream(combinedFile), 4096))) {
            
            System.out.println("\n저장된 복합 데이터 읽기:");
            String header = dis.readUTF();
            int productId = dis.readInt();
            String productName = dis.readUTF();
            double price = dis.readDouble();
            boolean inStock = dis.readBoolean();
            long timestamp = dis.readLong();
            
            System.out.println("헤더: " + header);
            System.out.println("상품 ID: " + productId);
            System.out.println("상품명: " + productName);
            System.out.println("가격: " + String.format("%,.0f원", price));
            System.out.println("재고: " + (inStock ? "있음" : "없음"));
            System.out.println("등록시간: " + new java.util.Date(timestamp));
            
        } catch (IOException e) {
            System.out.println("조합된 읽기 스트림 오류: " + e.getMessage());
        }
        
        System.out.println("→ 스트림 조합: 성능 + 편의성 + 안전성을 모두 확보\n");
    }
    
    /**
     * 7. 성능 비교 - 버퍼의 위력 체감
     */
    public void demonstratePerformanceComparison() {
        System.out.println("7. 성능 비교 - 버퍼의 중요성");
        
        // 대용량 테스트 데이터 생성 (10,000개 정수)
        int[] testNumbers = new int[10000];
        for (int i = 0; i < testNumbers.length; i++) {
            testNumbers[i] = i * i;
        }
        
        try {
            // 1. 버퍼 없이 DataOutputStream 사용
            long startTime = System.nanoTime();
            try (DataOutputStream slowDos = new DataOutputStream(
                    new FileOutputStream(FilePathManager.getFilePath("slow_data.bin")))) {
                for (int number : testNumbers) {
                    slowDos.writeInt(number);
                }
            }
            long slowTime = System.nanoTime() - startTime;
            
            // 2. 버퍼와 함께 DataOutputStream 사용
            startTime = System.nanoTime();
            try (DataOutputStream fastDos = new DataOutputStream(
                    new BufferedOutputStream(new FileOutputStream(FilePathManager.getFilePath("fast_data.bin"))))) {
                for (int number : testNumbers) {
                    fastDos.writeInt(number);
                }
            }
            long fastTime = System.nanoTime() - startTime;
            
            System.out.printf("버퍼 없음: %.2f ms\n", slowTime / 1_000_000.0);
            System.out.printf("버퍼 사용: %.2f ms\n", fastTime / 1_000_000.0);
            System.out.printf("성능 개선: %.0f배 빨라짐\n", (double) slowTime / fastTime);
            
            // 파일 크기는 동일함을 확인
            File slowFile = new File(FilePathManager.getFilePath("slow_data.bin"));
            File fastFile = new File(FilePathManager.getFilePath("fast_data.bin"));
            System.out.println("두 파일 크기 동일: " + (slowFile.length() == fastFile.length()));
            
        } catch (IOException e) {
            System.out.println("성능 비교 오류: " + e.getMessage());
        }
        
        System.out.println("→ 실무에서는 항상 BufferedStream과 함께 사용할 것!\n");
    }
    
    /**
     * 8. 실무 활용 패턴 - 종합 예제
     */
    public void demonstratePracticalUsage() {
        System.out.println("8. 실무 활용 패턴 - 로그 파일 분석기");
        
        // 가상의 로그 데이터 생성
        try (PrintStream logWriter = new PrintStream(
                new BufferedOutputStream(new FileOutputStream(FilePathManager.getFilePath("app.log"))))) {
            
            String[] logLevels = {"INFO", "WARN", "ERROR", "DEBUG"};
            String[] messages = {
                "사용자 로그인 성공",
                "메모리 사용량 80% 경고", 
                "데이터베이스 연결 실패",
                "쿼리 실행 시간: 1.2초"
            };
            
            for (int i = 0; i < 50; i++) {
                String level = logLevels[i % logLevels.length];
                String message = messages[i % messages.length];
                logWriter.printf("[%s] %s - %s (ID: %d)\n", 
                    level, new java.util.Date(), message, i + 1);
            }
            
            System.out.println("가상 로그 파일 생성 완료 (50개 엔트리)");
        } catch (IOException e) {
            System.out.println("로그 파일 생성 오류: " + e.getMessage());
        }
        
        // 로그 파일 분석
        try (BufferedReader logReader = new BufferedReader(
                new FileReader(FilePathManager.getFilePath("app.log")))) {
            
            int totalLines = 0;
            int errorCount = 0;
            int warnCount = 0;
            
            String line;
            while ((line = logReader.readLine()) != null) {
                totalLines++;
                if (line.contains("ERROR")) errorCount++;
                if (line.contains("WARN")) warnCount++;
            }
            
            System.out.println("\n=== 로그 분석 결과 ===");
            System.out.println("총 로그 라인: " + totalLines);
            System.out.println("ERROR 로그: " + errorCount);
            System.out.println("WARN 로그: " + warnCount);
            System.out.println("정상 로그: " + (totalLines - errorCount - warnCount));
            
        } catch (IOException e) {
            System.out.println("로그 분석 오류: " + e.getMessage());
        }
        
        System.out.println("→ 실무: 보조 스트림 조합으로 효율적인 파일 처리 구현\n");
    }
}