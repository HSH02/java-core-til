package com.JavaCoreTil.io.io_example;

import java.io.*;

import com.JavaCoreTil.io.common.FilePathManager;

public class Stage1_IOBasics {

    private static final boolean DELETE_FLAG = true;

    public static void main(String[] args) {
        Stage1_IOBasics demo = new Stage1_IOBasics();

        FilePathManager.ensureDirectoryExists();

        System.out.println("=".repeat(60));
        System.out.println("                I/O 스트림 기초 실습");
        System.out.println("=".repeat(60));
        System.out.println();

        try {
            demo.demoOutputStream();
            System.out.println("\n" + "-".repeat(60) + "\n");

            demo.demoInputStream();
            System.out.println("\n" + "-".repeat(60) + "\n");

            demo.demoWriter();
            System.out.println("\n" + "-".repeat(60) + "\n");

            demo.demoReader();
            System.out.println("\n" + "-".repeat(60) + "\n");
        } finally {
            System.out.println("=".repeat(60));
            System.out.println("                   실습 완료");
            System.out.println("=".repeat(60));
            FilePathManager.cleanupFiles(DELETE_FLAG);
        }

    }

    public void demoOutputStream() {
        System.out.println("=".repeat(25) + "OutputStream" + "=".repeat(25));

        try (OutputStream output = new FileOutputStream(FilePathManager.getFilePath("output_demo.txt"))) {

            // 1. write(int b) - 단일 바이트 쓰기 (0-255 범위)
            output.write(72);  // ASCII 'H'
            output.write(101); // ASCII 'e'
            output.write(108); // ASCII 'l'
            output.write(108); // ASCII 'l'
            output.write(111); // ASCII 'o'
            System.out.println("write(int b): 단일 바이트 5개 쓰기 완료");

            // 2. write(byte[] b) - 바이트 배열 전체 쓰기
            byte[] worldBytes = " World!".getBytes();
            output.write(worldBytes);
            System.out.println("write(byte[] b): 바이트 배열 전체 쓰기 완료");

            // 3. write(byte[] b, int off, int len) - 바이트 배열 부분 쓰기
            byte[] sampleData = "\nJava I/O Programming".getBytes();
            output.write(sampleData, 1, 7); // "Java I/"만 쓰기 (인덱스 1부터 7개)
            System.out.println("write(byte[] b, int off, int len): 부분 배열 쓰기 완료");

            // 4. flush() - 버퍼에 있는 데이터 강제 출력
            output.flush();
            System.out.println("flush(): 버퍼 강제 출력 완료");

        } catch (IOException e) {
            System.out.println("OutputStream 오류: " + e.getMessage());
        }

        System.out.println("생성된 파일: " + FilePathManager.getFilePath("output_demo.txt"));
    }

    public void demoInputStream() {
        System.out.println("=".repeat(25) + "InputStream" + "=".repeat(25));

        try (InputStream input = new FileInputStream(FilePathManager.getFilePath("output_demo.txt"))) {

            // 1. read() - 단일 바이트 읽기 (0-255 또는 -1)
            int firstByte = input.read();
            System.out.println("read(): 첫 번째 바이트 = " + firstByte + " ('" + (char)firstByte + "')");

            // 2. read(byte[] b) - 바이트 배열로 읽기
            byte[] buffer = new byte[10];
            int bytesRead = input.read(buffer);
            System.out.println("read(byte[] b): " + bytesRead + "바이트 읽음");
            System.out.println("읽은 내용: " + new String(buffer, 0, bytesRead));

            // 3. read(byte[] b, int off, int len) - 배열 특정 위치에 읽기
            byte[] partialBuffer = new byte[20];
            int partialRead = input.read(partialBuffer, 5, 10); // 인덱스 5부터 10개 읽기
            System.out.println("read(byte[] b, int off, int len): " + partialRead + "바이트 읽음");
            System.out.println("읽은 내용: " + new String(partialBuffer, 5, partialRead));

            // 4. available() - 읽을 수 있는 바이트 수 확인
            int availableBytes = input.available();
            System.out.println("available(): 남은 바이트 수 = " + availableBytes);

        } catch (IOException e) {
            System.out.println("InputStream 오류: " + e.getMessage());
        }
    }
    public void demoWriter() {
        System.out.println("=".repeat(25) + "Writer" + "=".repeat(25));

        try (Writer writer = new FileWriter(FilePathManager.getFilePath("writer_demo.txt"))) {

            // 1. write(int c) - 단일 문자 쓰기
            writer.write('H');
            writer.write('e');
            writer.write('l');
            writer.write('l');
            writer.write('o');
            System.out.println("write(int c): 단일 문자 5개 쓰기 완료");

            // 2. write(char[] cbuf) - 문자 배열 전체 쓰기
            char[] worldChars = {' ', 'W', 'o', 'r', 'l', 'd', '!'};
            writer.write(worldChars);
            System.out.println("write(char[] cbuf): 문자 배열 전체 쓰기 완료");

            // 3. write(char[] cbuf, int off, int len) - 문자 배열 부분 쓰기
            char[] sampleChars = "\n안녕하세요 Java".toCharArray();
            writer.write(sampleChars, 1, 5); // "안녕하세요"만 쓰기
            System.out.println("write(char[] cbuf, int off, int len): 부분 배열 쓰기 완료");

            // 4. write(String str) - 문자열 쓰기
            writer.write("\nI/O Programming");
            System.out.println("write(String str): 문자열 쓰기 완료");

            // 5. flush() - 버퍼 강제 출력
            writer.flush();
            System.out.println("flush(): 버퍼 강제 출력 완료");

        } catch (IOException e) {
            System.out.println("Writer 오류: " + e.getMessage());
        }

        System.out.println("생성된 파일: " + FilePathManager.getFilePath("writer_demo.txt"));
    }
    public void demoReader() {
        System.out.println("=".repeat(25) + "Reader" + "=".repeat(25));

        try (Reader reader = new FileReader(FilePathManager.getFilePath("writer_demo.txt"))) {

            // 1. read() - 단일 문자 읽기 (0-65535 또는 -1)
            int firstChar = reader.read();
            System.out.println("read(): 첫 번째 문자 = " + firstChar + " ('" + (char)firstChar + "')");

            // 2. read(char[] cbuf) - 문자 배열로 읽기
            char[] buffer = new char[10];
            int charsRead = reader.read(buffer);
            System.out.println("read(char[] cbuf): " + charsRead + "문자 읽음");
            System.out.println("읽은 내용: " + new String(buffer, 0, charsRead));

            // 3. read(char[] cbuf, int off, int len) - 배열 특정 위치에 읽기
            char[] partialBuffer = new char[20];
            int partialRead = reader.read(partialBuffer, 5, 10); // 인덱스 5부터 10개 읽기
            System.out.println("read(char[] cbuf, int off, int len): " + partialRead + "문자 읽음");
            System.out.println("읽은 내용: " + new String(partialBuffer, 5, partialRead));

            // 4. ready() - 읽을 준비가 되었는지 확인
            boolean isReady = reader.ready();
            System.out.println("ready(): 읽을 준비 상태 = " + isReady);

        } catch (IOException e) {
            System.out.println("Reader 오류: " + e.getMessage());
        }
    }
}
