package com.JavaCoreTil.io.io_practice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import com.JavaCoreTil.io.common.FilePathManager;

public class Stage6_setAndFile {

	public static void main(String[] args) {
		Stage6_setAndFile stage6 = new Stage6_setAndFile();

		print("RedirectStandardIO");
		stage6.RedirectStandardIO();

		print("fileClassBasic");
		stage6.fileClassBasic();

		print("fileAttributes");
		stage6.fileAttributes();
	}

	public static void print(String msg) {
		System.out.printf("%n = %s %s %n%n", msg, "=".repeat(30));
	}

	private void RedirectStandardIO() {
		InputStream originalIn = System.in;
		PrintStream originalOut = System.out;
		PrintStream originalErr = System.err;

		try {
			// 출력 파일 설정
			String outputFile = FilePathManager.getFilePath("fileOut");
			PrintStream fileOut = new PrintStream(new FileOutputStream(outputFile));

			// 입력 파일 설정
			String inputFile = FilePathManager.getFilePath("fileIn");

			// 입력 파일에 테스트 데이터 생성
			try (PrintWriter writer = new PrintWriter(new FileWriter(inputFile))) {
				writer.println("안녕하세요!");
				writer.println("이것은 표준 입력 리다이렉트 테스트입니다.");
				writer.println("종료");
			}

			// 표준 입출력 리다이렉트
			System.setOut(fileOut);
			System.setErr(fileOut);
			System.setIn(new FileInputStream(inputFile));

			System.out.println("=== 표준 입출력 리다이렉트 테스트 ===");
			System.out.println("이 내용은 파일로 출력됩니다.");
			System.err.println("이건 에러 내용입니다.");

			Scanner scanner = new Scanner(System.in);
			System.out.println("\n파일에서 읽은 내용:");

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				System.out.println("입력: " + line);
				if (line.equals("종료")) {
					break;
				}
			}
			scanner.close();

			// 원래 표준 입출력으로 복원
			System.setOut(originalOut);
			System.setErr(originalErr);
			System.setIn(originalIn);

			// 결과 확인
			System.out.println("표준 입출력 리다이렉트 테스트 완료!");
			System.out.println("출력 파일: " + outputFile);
			System.out.println("입력 파일: " + inputFile);

			// 출력 파일 내용 확인
			System.out.println("\n출력 파일 내용:");
			try (BufferedReader reader = new BufferedReader(new FileReader(outputFile))) {
				String line;
				while ((line = reader.readLine()) != null) {
					System.out.println("  " + line);
				}
			}

		} catch (IOException e) {
			System.setOut(originalOut);
			System.setErr(originalErr);
			System.setIn(originalIn);
			System.out.println("표준 입출력 리다이렉트 실패: " + e.getMessage());
		}
	}

	private void fileClassBasic() {
		List<File> fileList = new ArrayList<>();

		fileList.add(new File("fileClassBasic.txt"));
		fileList.add(new File("/example/fileClassBasic.txt"));
		fileList.add(new File("example/file", "nested.txt"));
		fileList.add(new File(new File("parent"), "child.txt"));

		for (File file : fileList) {
			System.out.println(file.getPath());
		}

		File testFile = new File(FilePathManager.getFilePath("test_file.txt"));
		System.out.println("testFile.exists() = " + testFile.exists());

		try {
			boolean created = testFile.createNewFile();
			System.out.println("created = " + created);
			System.out.println("testFile.exists() = " + testFile.exists());
		} catch (IOException e) {
			System.out.println("e.getMessage() = " + e.getMessage());
		}

	}

	private void fileAttributes() {
		String testFilePath = new FilePathManager().getFilePath("attributes_test2.txt");
		File testFile = new File(testFilePath);

		try (FileWriter writer = new FileWriter(testFile)) {
			writer.write("This example 1 \n");
			writer.write("This example 2 \n");
			writer.write("This example 3 \n");

			System.out.println("testFile.getName() = " + testFile.getName());
			System.out.println("testFile.getPath() = " + testFile.getPath());
			System.out.println("testFile.getAbsolutePath() = " + testFile.getAbsolutePath());
			System.out.println("testFile.getCanonicalFile() = " + testFile.getCanonicalPath());
			System.out.println("testFile.length() = " + testFile.length());
			System.out.println("new Date(testFile.lastModified) = " + new Date(testFile.lastModified()));
			System.out.println("testFile.isFile() = " + testFile.isFile());
			System.out.println("testFile.isDirectory() = " + testFile.isDirectory());
			System.out.println("testFile.isHidden() = " + testFile.isHidden());
			System.out.println("testFile.canRead() = " + testFile.canRead());
			System.out.println("testFile.canWrite() = " + testFile.canWrite());
			System.out.println("testFile.canExecute() = " + testFile.canExecute());
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

}
