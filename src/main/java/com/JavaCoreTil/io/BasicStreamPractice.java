package com.JavaCoreTil.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * InputStream/OutputStream 기초 실습
 * 목표: 핵심 메서드 3개만 사용해서 파일 읽고 쓰기
 */
public class BasicStreamPractice {

	private static final String fileDir = "src/main/java/com/JavaCoreTil/io/filePath/test.txt";

	public static void main(String[] args) {

		// 1단계: OutputStream으로 파일 쓰기
		System.out.println("=== 1단계: 파일 쓰기 ===");
		writeToFile();

		// 2단계: InputStream으로 파일 읽기
		System.out.println("\n=== 2단계: 파일 읽기 ===");
		readFromFile();
	}

	/**
	 * TODO: OutputStream의 write() 메서드를 사용해서
	 * "Hello World" 문자열을 test.txt 파일에 쓰기
	 * <p>
	 * 힌트:
	 * - FileOutputStream 사용
	 * - try-with-resources 구문 사용
	 * - String.getBytes()로 문자열을 바이트 배열로 변환
	 * <p>
	 * 예상 결과: test.txt 파일이 생성되고 "Hello World" 내용이 저장됨
	 */
	private static void writeToFile() {
		// 여기에 코드를 작성하세요

		try (OutputStream f = new FileOutputStream(fileDir)) {
            String str = "Hello World";
            f.write(str.getBytes());
            System.out.println("파일 쓰기 정상 완료");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        // 두 번째: append 모드로 추가 쓰기
        try (OutputStream f = new FileOutputStream(fileDir, true)) {
            String str = "\nAppend Test!";
            f.write(str.getBytes());
            System.out.println("append 모드 쓰기 완료");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

	}

	/**
	 * TODO: InputStream의 read() 메서드를 사용해서
	 * test.txt 파일의 내용을 읽어와 콘솔에 출력
	 * <p>
	 * 힌트:
	 * - FileInputStream 사용
	 * - while 루프로 read() != -1 조건 확인
	 * - 읽은 바이트를 char로 변환해서 출력
	 * <p>
	 * 예상 결과: 콘솔에 "Hello World" 출력됨
	 */
	private static void readFromFile() {

		try (InputStream f = new FileInputStream(fileDir)) {

			int byteData;

			while ((byteData = f.read()) != -1) {
				System.out.print((char) byteData);
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}
} 