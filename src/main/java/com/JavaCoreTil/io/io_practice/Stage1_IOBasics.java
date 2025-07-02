package com.JavaCoreTil.io.io_practice;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import com.JavaCoreTil.io.common.FilePathManager;

public class Stage1_IOBasics {

	private static final boolean DELETE_FLAG = false;

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
			// 1. write(int b) - 단일 바이트 쓰기
			output.write(72);
			output.write(101);
			output.write(108);
			output.write(108);
			output.write(111);
			System.out.println("write(int b) : 단일 바이트");
			output.flush();

			// 2. write(byte[] b) - 바이트 배열 전체 쓰기
			byte[] worldBytes = " World! Nice Day!".getBytes();
			output.write(worldBytes);
			System.out.println("write(byte[] b) : 바이트 배열");
			output.flush();

			// 3. write(byte[] b, int off, int len) - 바이트 배열 부분 쓰기
			byte[] numberData = "\n1234567890".getBytes();
			output.write(numberData, 0, 7);
			System.out.println("write(byte[] b, int off, int len) : 바이트 배열 부분 쓰기");
			output.flush();

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public void demoInputStream() {
		System.out.println("=".repeat(25) + "InputStream" + "=".repeat(25));

		try (InputStream input = new FileInputStream(FilePathManager.getFilePath("output_demo.txt"))) {
			// 1. read() - 단일 바이트 읽기
			int firstByte = input.read();
			System.out.println("firstByte = " + firstByte);

			// 2. read(byte[] b) - 바이트 배열로 읽기
			byte[] buffer = new byte[10];
			int bytesRead = input.read(buffer);
			System.out.println("bytesRead = " + bytesRead);
			System.out.println("new String(buffer, 0, bytesRead) = " + new String(buffer, 0, bytesRead));

			// 3. read(byte[] b, int off, int len) : 배열 특정 위치에 읽기
			byte[] partialBuffer = new byte[20];
			int partialRead = input.read(partialBuffer, 5, 15);
			System.out.println("read(byte[] b, int off, int len): " + partialRead + "바이트 읽음");
			System.out.println("읽은 내용: " + new String(partialBuffer, 5, partialRead));

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}

	public void demoWriter() {
		System.out.println("=".repeat(25) + "Writer" + "=".repeat(25));

		try (Writer writer = new FileWriter(FilePathManager.getFilePath("writer_demo.txt"))) {
			// 1. write(int c) - 단일 문자
			writer.write('H');
			writer.write('e');
			writer.write('l');
			writer.write('l');
			writer.write('o');
			System.out.println("write(int c): 단일 문자 쓰기 ");

			// 2. write(char[] c) - 문자 배열 전체 쓰기
			char[] chars = {' ', 'W', 'o', 'r', 'l', 'd', '!'};
			writer.write(chars);
			System.out.println("write(char[] chars) : 문자 배열 전체 쓰기");

			// 3. write(char[] chars, int off, int len) - 문자 배열 부분 쓰기
			char[] chars2 = "\n 안녕! Java".toCharArray();
			writer.write(chars, 0, chars.length);
			System.out.println("write(char[] char, int oof, int len) : 부분 배열 쓰기");

			// 4. write(String str) - 문자열 쓰기
			writer.write("\nI/O Programming");
			System.out.println("write(Strign str) : 문자열 쓰기");

			writer.flush();

		} catch (IOException e) {
			System.out.println("Writer 오류: " + e.getMessage());
		}

	}

	public void demoReader() {
		System.out.println("=".repeat(25) + "Reader" + "=".repeat(25));

		try (Reader reader = new FileReader(FilePathManager.getFilePath("writer_demo.txt"))) {

			// 1. read() - 단일 문자
			int firstChar = reader.read();
			System.out.println("read() : 첫 번째 문자");

			// 2. read(char[] char) - 문자 배열
			char[] buffer = new char[10];
			int charsRead = reader.read(buffer);
			System.out.println("read(char[] cbuf): " + charsRead + "문자 읽음");
			System.out.println("읽은 내용: " + new String(buffer, 0, charsRead));

			// 3. read(char[] char, int off, int len) - 배열 특정 위치 읽기
			char[] partialBuffer = new char[20];
			int partialRead = reader.read(partialBuffer, 5, 10); // 인덱스 5부터 10개 읽기
			System.out.println("read(char[] cbuf, int off, int len): " + partialRead + "문자 읽음");
			System.out.println("읽은 내용: " + new String(partialBuffer, 5, partialRead));

		} catch (IOException e) {
			System.out.println("Reader 오류: " + e.getMessage());
		}
	}
}
