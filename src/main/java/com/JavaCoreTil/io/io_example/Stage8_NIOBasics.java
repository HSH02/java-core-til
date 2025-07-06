package com.JavaCoreTil.io.io_example;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import com.JavaCoreTil.io.common.FilePathManager;

/**
 * I/O 로드맵 8단계: NIO 기초 (New I/O)
 * 
 * 학습 목표:
 * 1. NIO 등장 배경 - 기존 I/O 한계, 성능 문제
 * 2. Buffer 클래스 - ByteBuffer, position/limit/capacity
 * 3. Channel 개념 - 양방향 통신, FileChannel
 * 4. FileChannel - 파일 채널, 효율적 파일 처리
 * 5. Buffer 조작 - flip(), rewind(), clear(), compact()
 * 6. 직접/간접 버퍼 - 성능 차이, 사용 시나리오
 */
public class Stage8_NIOBasics {
    
    private static final boolean DELETE_FLAG = false;
    
    public static void main(String[] args) {
        Stage8_NIOBasics demo = new Stage8_NIOBasics();
        
        FilePathManager.ensureDirectoryExists();
        
        System.out.println("=== I/O 로드맵 8단계: NIO 기초 (New I/O) ===\n");
        
        try {
            // 1. NIO 등장 배경과 기본 개념
            demo.example1_NIOBackground();
            
            // 2. Buffer 클래스 기본 사용법
            demo.example2_BufferBasics();
            
            // 3. Buffer 상태 관리
            demo.example3_BufferStateManagement();
            
            // 4. FileChannel 기본 사용법
            demo.example4_FileChannelBasics();
            
            // 5. Buffer 조작 메서드들
            demo.example5_BufferOperations();
            
            // 6. 직접/간접 버퍼 비교
            demo.example6_DirectVsIndirectBuffer();
            
            // 7. 실무 활용 - 대용량 파일 처리
            demo.example7_PracticalFileProcessing();
            
            System.out.println("\n=== 8단계 학습 완료! ===");
            
        } finally {
            System.out.println("\n=== 파일 정리 ===");
            FilePathManager.cleanupFiles(DELETE_FLAG);
        }
    }
    
    /**
     * 예제 1: NIO 등장 배경과 기본 개념
     * 기존 I/O의 한계와 NIO의 장점
     */
    public void example1_NIOBackground() {
        System.out.println("=== 예제 1: NIO 등장 배경과 기본 개념 ===");
        
        System.out.println("기존 I/O의 한계:");
        System.out.println("- 블로킹 방식: 한 번에 하나의 작업만 처리");
        System.out.println("- 스트림 기반: 단방향 통신");
        System.out.println("- 성능 제한: 작은 버퍼로 인한 오버헤드");
        
        System.out.println("\nNIO의 장점:");
        System.out.println("- 논블로킹 방식: 여러 작업 동시 처리 가능");
        System.out.println("- 채널 기반: 양방향 통신");
        System.out.println("- 버퍼 기반: 효율적인 메모리 사용");
        System.out.println("- 선택자(Selector): 다중 I/O 처리");
        
        System.out.println("\nNIO 핵심 구성 요소:");
        System.out.println("- Buffer: 데이터를 담는 컨테이너");
        System.out.println("- Channel: 데이터 전송 통로");
        System.out.println("- Selector: 다중 채널 관리");
        
        System.out.println("→ NIO: 고성능 I/O를 위한 새로운 API\n");
    }
    
    /**
     * 예제 2: Buffer 클래스 기본 사용법
     * ByteBuffer의 기본적인 사용법
     */
    public void example2_BufferBasics() {
        System.out.println("=== 예제 2: Buffer 클래스 기본 사용법 ===");
        
        // ByteBuffer 생성
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        
        System.out.println("ByteBuffer 생성 후 상태:");
        System.out.println("- capacity: " + buffer.capacity());
        System.out.println("- position: " + buffer.position());
        System.out.println("- limit: " + buffer.limit());
        System.out.println("- remaining: " + buffer.remaining());
        
        // 데이터 쓰기
        String data = "Hello NIO World!";
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        buffer.put(bytes);
        
        System.out.println("\n데이터 쓰기 후 상태:");
        System.out.println("- position: " + buffer.position());
        System.out.println("- limit: " + buffer.limit());
        System.out.println("- remaining: " + buffer.remaining());
        
        // 읽기 모드로 전환
        buffer.flip();
        
        System.out.println("\nflip() 후 상태:");
        System.out.println("- position: " + buffer.position());
        System.out.println("- limit: " + buffer.limit());
        System.out.println("- remaining: " + buffer.remaining());
        
        // 데이터 읽기
        byte[] readBytes = new byte[buffer.remaining()];
        buffer.get(readBytes);
        String readData = new String(readBytes, StandardCharsets.UTF_8);
        
        System.out.println("\n읽은 데이터: " + readData);
        System.out.println("읽기 후 position: " + buffer.position());
        
        System.out.println("→ Buffer: 데이터를 담고 조작하는 컨테이너\n");
    }
    
    /**
     * 예제 3: Buffer 상태 관리
     * Buffer의 position, limit, capacity 이해
     */
    public void example3_BufferStateManagement() {
        System.out.println("=== 예제 3: Buffer 상태 관리 ===");
        
        // 16바이트 버퍼 생성
        ByteBuffer buffer = ByteBuffer.allocate(16);
        
        System.out.println("초기 상태:");
        printBufferState(buffer);
        
        // 데이터 쓰기
        buffer.put((byte) 1);
        buffer.put((byte) 2);
        buffer.put((byte) 3);
        buffer.put((byte) 4);
        
        System.out.println("\n4바이트 쓰기 후:");
        printBufferState(buffer);
        
        // 읽기 모드로 전환
        buffer.flip();
        
        System.out.println("\nflip() 후 (읽기 모드):");
        printBufferState(buffer);
        
        // 2바이트 읽기
        byte first = buffer.get();
        byte second = buffer.get();
        
        System.out.println("\n2바이트 읽기 후:");
        System.out.println("- 읽은 값: " + first + ", " + second);
        printBufferState(buffer);
        
        // 다시 쓰기 모드로 전환
        buffer.clear();
        
        System.out.println("\nclear() 후 (쓰기 모드):");
        printBufferState(buffer);
        
        // 새로운 데이터 쓰기
        buffer.put((byte) 10);
        buffer.put((byte) 20);
        
        System.out.println("\n새로운 데이터 쓰기 후:");
        printBufferState(buffer);
        
        System.out.println("→ Buffer 상태: position, limit, capacity로 관리\n");
    }
    
    /**
     * 예제 4: FileChannel 기본 사용법
     * FileChannel을 사용한 파일 읽기/쓰기
     */
    public void example4_FileChannelBasics() {
        System.out.println("=== 예제 4: FileChannel 기본 사용법 ===");
        
        String testFile = FilePathManager.getFilePath("nio_test.txt");
        
        // FileChannel로 파일 쓰기
        try (FileChannel writeChannel = new FileOutputStream(testFile).getChannel()) {
            
            ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
            String content = "NIO FileChannel 테스트\n두 번째 줄\n세 번째 줄";
            writeBuffer.put(content.getBytes(StandardCharsets.UTF_8));
            
            // 읽기 모드로 전환
            writeBuffer.flip();
            
            // 파일에 쓰기
            int bytesWritten = writeChannel.write(writeBuffer);
            System.out.println("쓰기 완료: " + bytesWritten + " 바이트");
            
        } catch (IOException e) {
            System.err.println("파일 쓰기 실패: " + e.getMessage());
            return;
        }
        
        // FileChannel로 파일 읽기
        try (FileChannel readChannel = new FileInputStream(testFile).getChannel()) {
            
            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
            
            // 파일에서 읽기
            int bytesRead = readChannel.read(readBuffer);
            System.out.println("읽기 완료: " + bytesRead + " 바이트");
            
            // 읽기 모드로 전환
            readBuffer.flip();
            
            // 버퍼에서 문자열로 변환
            byte[] data = new byte[readBuffer.remaining()];
            readBuffer.get(data);
            String readContent = new String(data, StandardCharsets.UTF_8);
            
            System.out.println("\n읽은 내용:");
            System.out.println(readContent);
            
        } catch (IOException e) {
            System.err.println("파일 읽기 실패: " + e.getMessage());
        }
        
        System.out.println("→ FileChannel: 효율적인 파일 I/O를 위한 채널\n");
    }
    
    /**
     * 예제 5: Buffer 조작 메서드들
     * flip(), rewind(), clear(), compact() 메서드
     */
    public void example5_BufferOperations() {
        System.out.println("=== 예제 5: Buffer 조작 메서드들 ===");
        
        ByteBuffer buffer = ByteBuffer.allocate(20);
        
        // 데이터 쓰기
        buffer.put("Hello".getBytes(StandardCharsets.UTF_8));
        System.out.println("데이터 쓰기 후:");
        printBufferState(buffer);
        
        // flip() - 읽기 모드로 전환
        buffer.flip();
        System.out.println("\nflip() 후:");
        printBufferState(buffer);
        
        // 일부 데이터 읽기
        byte[] partial = new byte[3];
        buffer.get(partial);
        System.out.println("3바이트 읽기: " + new String(partial, StandardCharsets.UTF_8));
        printBufferState(buffer);
        
        // compact() - 읽은 데이터 제거하고 남은 데이터를 앞으로 이동
        buffer.compact();
        System.out.println("\ncompact() 후:");
        printBufferState(buffer);
        
        // 추가 데이터 쓰기
        buffer.put("World".getBytes(StandardCharsets.UTF_8));
        System.out.println("\n추가 데이터 쓰기 후:");
        printBufferState(buffer);
        
        // rewind() - position을 0으로 되돌림
        buffer.flip(); // 먼저 읽기 모드로
        buffer.rewind();
        System.out.println("\nrewind() 후:");
        printBufferState(buffer);
        
        // 전체 데이터 읽기
        byte[] allData = new byte[buffer.remaining()];
        buffer.get(allData);
        System.out.println("전체 데이터: " + new String(allData, StandardCharsets.UTF_8));
        
        // clear() - 버퍼 초기화
        buffer.clear();
        System.out.println("\nclear() 후:");
        printBufferState(buffer);
        
        System.out.println("→ Buffer 조작: flip(), rewind(), clear(), compact()\n");
    }
    
    /**
     * 예제 6: 직접/간접 버퍼 비교
     * 성능 차이와 사용 시나리오
     */
    public void example6_DirectVsIndirectBuffer() {
        System.out.println("=== 예제 6: 직접/간접 버퍼 비교 ===");
        
        // 간접 버퍼 (JVM 힙 메모리)
        ByteBuffer indirectBuffer = ByteBuffer.allocate(1024);
        System.out.println("간접 버퍼:");
        System.out.println("- isDirect: " + indirectBuffer.isDirect());
        System.out.println("- 힙 메모리에 할당됨");
        System.out.println("- 가비지 컬렉션 대상");
        
        // 직접 버퍼 (네이티브 메모리)
        ByteBuffer directBuffer = ByteBuffer.allocateDirect(1024);
        System.out.println("\n직접 버퍼:");
        System.out.println("- isDirect: " + directBuffer.isDirect());
        System.out.println("- 네이티브 메모리에 할당됨");
        System.out.println("- 가비지 컬렉션 대상 아님");
        
        // 성능 테스트
        System.out.println("\n성능 테스트:");
        
        long startTime = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            indirectBuffer.clear();
            indirectBuffer.put("Test data".getBytes(StandardCharsets.UTF_8));
            indirectBuffer.flip();
        }
        long indirectTime = System.nanoTime() - startTime;
        
        startTime = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            directBuffer.clear();
            directBuffer.put("Test data".getBytes(StandardCharsets.UTF_8));
            directBuffer.flip();
        }
        long directTime = System.nanoTime() - startTime;
        
        System.out.println("- 간접 버퍼 시간: " + indirectTime + " ns");
        System.out.println("- 직접 버퍼 시간: " + directTime + " ns");
        System.out.println("- 성능 차이: " + (directTime < indirectTime ? "직접 버퍼가 빠름" : "간접 버퍼가 빠름"));
        
        // 메모리 해제
        if (directBuffer.isDirect()) {
            // 직접 버퍼는 명시적으로 해제해야 함
            System.out.println("\n직접 버퍼 메모리 해제");
        }
        
        System.out.println("→ 직접 버퍼: 대용량 데이터나 장시간 사용 시 유리\n");
    }
    
    /**
     * 예제 7: 실무 활용 - 대용량 파일 처리
     * NIO를 활용한 효율적인 파일 처리
     */
    public void example7_PracticalFileProcessing() {
        System.out.println("=== 예제 7: 실무 활용 - 대용량 파일 처리 ===");
        
        String sourceFile = FilePathManager.getFilePath("large_source.txt");
        String targetFile = FilePathManager.getFilePath("large_target.txt");
        
        // 대용량 테스트 파일 생성
        try (FileChannel writeChannel = new FileOutputStream(sourceFile).getChannel()) {
            
            ByteBuffer buffer = ByteBuffer.allocate(8192); // 8KB 버퍼
            
            for (int i = 0; i < 100; i++) {
                String line = "라인 " + (i + 1) + ": 이것은 대용량 파일 처리를 위한 테스트 데이터입니다.\n";
                buffer.clear();
                buffer.put(line.getBytes(StandardCharsets.UTF_8));
                buffer.flip();
                writeChannel.write(buffer);
            }
            
            System.out.println("테스트 파일 생성 완료: " + sourceFile);
            
        } catch (IOException e) {
            System.err.println("테스트 파일 생성 실패: " + e.getMessage());
            return;
        }
        
        // NIO를 사용한 파일 복사
        long startTime = System.currentTimeMillis();
        
        try (FileChannel sourceChannel = new FileInputStream(sourceFile).getChannel();
             FileChannel targetChannel = new FileOutputStream(targetFile).getChannel()) {
            
            // 직접 버퍼 사용 (대용량 파일에 유리)
            ByteBuffer buffer = ByteBuffer.allocateDirect(8192);
            long totalBytes = 0;
            
            while (true) {
                buffer.clear();
                int bytesRead = sourceChannel.read(buffer);
                
                if (bytesRead == -1) {
                    break; // 파일 끝
                }
                
                buffer.flip();
                targetChannel.write(buffer);
                totalBytes += bytesRead;
            }
            
            long endTime = System.currentTimeMillis();
            System.out.println("파일 복사 완료:");
            System.out.println("- 복사된 바이트: " + totalBytes);
            System.out.println("- 소요 시간: " + (endTime - startTime) + " ms");
            System.out.println("- 처리 속도: " + (totalBytes / 1024) / (endTime - startTime) + " KB/ms");
            
        } catch (IOException e) {
            System.err.println("파일 복사 실패: " + e.getMessage());
        }
        
        // 파일 내용 검증
        try (FileChannel sourceChannel = new FileInputStream(sourceFile).getChannel();
             FileChannel targetChannel = new FileInputStream(targetFile).getChannel()) {
            
            long sourceSize = sourceChannel.size();
            long targetSize = targetChannel.size();
            
            System.out.println("\n파일 검증:");
            System.out.println("- 원본 파일 크기: " + sourceSize + " 바이트");
            System.out.println("- 복사 파일 크기: " + targetSize + " 바이트");
            System.out.println("- 복사 성공: " + (sourceSize == targetSize));
            
        } catch (IOException e) {
            System.err.println("파일 검증 실패: " + e.getMessage());
        }
        
        System.out.println("→ 실무 활용: NIO로 대용량 파일을 효율적으로 처리\n");
    }
    
    /**
     * Buffer 상태 출력 헬퍼 메서드
     */
    private void printBufferState(ByteBuffer buffer) {
        System.out.println("- capacity: " + buffer.capacity());
        System.out.println("- position: " + buffer.position());
        System.out.println("- limit: " + buffer.limit());
        System.out.println("- remaining: " + buffer.remaining());
    }
} 