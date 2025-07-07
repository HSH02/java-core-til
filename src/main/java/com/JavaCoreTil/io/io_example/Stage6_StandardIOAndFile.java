package com.JavaCoreTil.io.io_example;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.util.Date;

import com.JavaCoreTil.io.common.FilePathManager;

/**
 * I/O 로드맵 6단계: 표준 입출력과 File 클래스
 * <p>
 * 학습 목표:
 * 1. 표준 입출력 - System.in, System.out, System.err
 * 2. 표준 입출력 대상 변경 - setIn(), setOut(), setErr()
 * 3. File 클래스 - 파일/디렉토리 정보, 경로 처리
 * 4. 파일 속성 조회 - exists(), length(), lastModified()
 * 5. 디렉토리 조작 - mkdir(), list(), listFiles()
 * 6. RandomAccessFile - 임의 접근, seek 포인터
 */
public class Stage6_StandardIOAndFile {

	private static final boolean DELETE_FLAG = false;

	public static void main(String[] args) throws IOException {
		Stage6_StandardIOAndFile demo = new Stage6_StandardIOAndFile();

		FilePathManager.ensureDirectoryExists();

		System.out.println("=== I/O 로드맵 6단계: 표준 입출력과 File 클래스 ===\n");

		try {
			// 1. 표준 입출력의 기본 개념
			demo.example1_StandardIO();

			// 2. 표준 입출력 대상 변경
			demo.example2_RedirectStandardIO();

			// 3. File 클래스 기본 사용법
			demo.example3_FileClassBasics();

			// 4. 파일 속성 조회
			demo.example4_FileAttributes();

			// 5. 디렉토리 조작
			demo.example5_DirectoryOperations();

			// 6. RandomAccessFile 사용법
			demo.example6_RandomAccessFile();

			// 7. 실무 활용 - 파일 관리 시스템
			demo.example7_PracticalFileManagement();

			System.out.println("\n=== 6단계 학습 완료! ===");

		} finally {
			System.out.println("\n=== 파일 정리 ===");
			FilePathManager.cleanupFiles(DELETE_FLAG);
		}
	}

	/**
	 * 예제 1: 표준 입출력의 기본 개념
	 * System.in, System.out, System.err의 역할과 차이점
	 */
	public void example1_StandardIO() {
		System.out.println("=== 예제 1: 표준 입출력의 기본 개념 ===");

		// System.out - 표준 출력 (정상 메시지)
		System.out.println("이것은 정상적인 출력 메시지입니다.");
		System.out.println("프로그램이 정상적으로 실행되고 있습니다.");

		// System.err - 표준 오류 (오류 메시지)
		System.err.println("이것은 오류 메시지입니다!");
		System.err.println("경고: 메모리 사용량이 높습니다.");

		// System.in - 표준 입력 (키보드)
		System.out.println("\n표준 입력 테스트 (Enter 키를 누르세요):");
		try {
			int input = System.in.read();
			System.out.println("입력된 문자: " + (char)input + " (ASCII: " + input + ")");
		} catch (IOException e) {
			System.err.println("입력 읽기 실패: " + e.getMessage());
		}

		System.out.println("→ 표준 입출력: 프로그램의 기본 통로 (in, out, err)\n");
	}

	/**
	 * 예제 2: 표준 입출력 대상 변경
	 * setIn(), setOut(), setErr()를 사용한 리다이렉션
	 */
	public void example2_RedirectStandardIO() {
		System.out.println("=== 예제 2: 표준 입출력 대상 변경 ===");

		// 원래 표준 출력 보존
		PrintStream originalOut = System.out;

		try {
			// 표준 출력을 파일로 변경
			String outputFile = FilePathManager.getFilePath("redirect_output.txt");
			PrintStream fileOut = new PrintStream(new FileOutputStream(outputFile));
			System.setOut(fileOut);

			System.out.println("=== 리다이렉션 테스트 ===");
			System.out.println("이 메시지는 파일에 저장됩니다.");
			System.out.println("현재 시간: " + new Date());
			System.out.println("테스트 완료");

			// 원래 표준 출력으로 복원
			System.setOut(originalOut);
			System.out.println("표준 출력이 파일로 리다이렉션되었습니다.");
			System.out.println("결과 파일: " + outputFile);

		} catch (IOException e) {
			System.setOut(originalOut);
			System.err.println("리다이렉션 실패: " + e.getMessage());
		}

		System.out.println("→ 표준 입출력 변경: 필요에 따라 파일 등으로 방향 바꾸기\n");
	}

	/**
	 * 예제 3: File 클래스 기본 사용법
	 * 파일 객체 생성과 기본 메서드들
	 */
	public void example3_FileClassBasics() {
		System.out.println("=== 예제 3: File 클래스 기본 사용법 ===");

		// 다양한 방법으로 File 객체 생성
		File file1 = new File("simple.txt");
		File file2 = new File("/absolute/path/file.txt");
		File file3 = new File("folder", "nested.txt");
		File file4 = new File(new File("parent"), "child.txt");

		System.out.println("File 객체 생성 방법들:");
		System.out.println("- file1: " + file1.getPath());
		System.out.println("- file2: " + file2.getPath());
		System.out.println("- file3: " + file3.getPath());
		System.out.println("- file4: " + file4.getPath());

		// 파일 존재 여부 확인
		File testFile = new File(FilePathManager.getFilePath("test_file.txt"));
		System.out.println("\n파일 존재 여부: " + testFile.exists());

		// 파일 생성
		try {
			boolean created = testFile.createNewFile();
			System.out.println("파일 생성 결과: " + created);
			System.out.println("파일 존재 여부: " + testFile.exists());
		} catch (IOException e) {
			System.err.println("파일 생성 실패: " + e.getMessage());
		}

		System.out.println("→ File 클래스: 파일과 디렉토리를 다루는 객체 지향적 접근\n");
	}

	/**
	 * 예제 4: 파일 속성 조회
	 * 파일의 다양한 정보를 확인하는 방법
	 */
	public void example4_FileAttributes() throws IOException {
		System.out.println("=== 예제 4: 파일 속성 조회 ===");

		// 테스트 파일 생성
		String testFilePath = FilePathManager.getFilePath("attributes_test.txt");
		File testFile = new File(testFilePath);

		try (FileWriter writer = new FileWriter(testFile)) {
			writer.write("이것은 속성 테스트를 위한 파일입니다.\n");
			writer.write("여러 줄의 텍스트를 포함합니다.\n");
			writer.write("한글도 포함되어 있습니다.");
		} catch (IOException e) {
			System.err.println("테스트 파일 생성 실패: " + e.getMessage());
			return;
		}

		// 파일 속성 조회
		System.out.println("파일 속성 정보:");
		System.out.println("- 파일명: " + testFile.getName());
		System.out.println("- 경로: " + testFile.getPath());
		System.out.println("- 절대 경로: " + testFile.getAbsolutePath());
		System.out.println("- 정규화된 경로: " + testFile.getCanonicalPath());
		System.out.println("- 파일 크기: " + testFile.length() + " 바이트");
		System.out.println("- 파일 여부: " + testFile.isFile());
		System.out.println("- 디렉토리 여부: " + testFile.isDirectory());
		System.out.println("- 숨김 파일 여부: " + testFile.isHidden());
		System.out.println("- 읽기 가능: " + testFile.canRead());
		System.out.println("- 쓰기 가능: " + testFile.canWrite());
		System.out.println("- 실행 가능: " + testFile.canExecute());

		System.out.println("→ 파일 속성: 파일의 모든 정보를 조회할 수 있음\n");
	}

	/**
	 * 예제 5: 디렉토리 조작
	 * 디렉토리 생성, 목록 조회, 삭제 등
	 */
	public void example5_DirectoryOperations() {
		System.out.println("=== 예제 5: 디렉토리 조작 ===");

		// 디렉토리 생성
		String baseDir = FilePathManager.getFilePath("test_directory");
		File directory = new File(baseDir);

		System.out.println("디렉토리 생성:");
		if (!directory.exists()) {
			boolean created = directory.mkdir();
			System.out.println("- 디렉토리 생성 결과: " + created);
		} else {
			System.out.println("- 디렉토리가 이미 존재합니다");
		}

		// 중첩 디렉토리 생성
		String nestedDir = FilePathManager.getFilePath("nested/sub/directory");
		File nestedDirectory = new File(nestedDir);

		System.out.println("\n중첩 디렉토리 생성:");
		boolean createdAll = nestedDirectory.mkdirs();
		System.out.println("- 중첩 디렉토리 생성 결과: " + createdAll);

		// 디렉토리 내용 조회
		System.out.println("\n디렉토리 내용 조회:");
		File[] files = directory.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					System.out.println("- 파일: " + file.getName());
				} else if (file.isDirectory()) {
					System.out.println("- 폴더: " + file.getName());
				}
			}
		}

		// 파일명만 조회
		System.out.println("\n파일명 목록:");
		String[] fileNames = directory.list();
		if (fileNames != null) {
			for (String fileName : fileNames) {
				System.out.println("- " + fileName);
			}
		}

		// 필터링된 파일 목록
		System.out.println("\n.txt 파일만 조회:");
		File[] txtFiles = directory.listFiles((dir, name) -> name.endsWith(".txt"));
		if (txtFiles != null) {
			for (File file : txtFiles) {
				System.out.println("- " + file.getName());
			}
		}

		System.out.println("→ 디렉토리 조작: 생성, 목록 조회, 필터링 등 다양한 기능\n");
	}

	/**
	 * 예제 6: RandomAccessFile 사용법
	 * 파일의 임의 위치에 접근하는 방법
	 */
	public void example6_RandomAccessFile() {
		System.out.println("=== 예제 6: RandomAccessFile 사용법 ===");

		String rafFile = FilePathManager.getFilePath("random_access.dat");

		// RandomAccessFile로 데이터 쓰기
		try (RandomAccessFile raf = new RandomAccessFile(rafFile, "rw")) {
			System.out.println("RandomAccessFile로 데이터 쓰기:");

			// 파일 시작에 데이터 쓰기
			raf.writeBytes("Hello World");
			raf.writeInt(12345);
			raf.writeDouble(3.14159);

			System.out.println("- 문자열, 정수, 실수 데이터를 썼습니다");
			System.out.println("- 현재 파일 크기: " + raf.length() + " 바이트");

			// 파일의 특정 위치로 이동하여 읽기
			System.out.println("\n파일의 특정 위치에서 읽기:");

			raf.seek(0); // 파일 시작으로 이동
			String text = raf.readLine();
			System.out.println("- 읽은 문자열: " + text);

			int number = raf.readInt();
			System.out.println("- 읽은 정수: " + number);

			double pi = raf.readDouble();
			System.out.println("- 읽은 실수: " + pi);

			// 파일 끝에 추가 데이터 쓰기
			System.out.println("\n파일 끝에 추가 데이터 쓰기:");
			raf.seek(raf.length()); // 파일 끝으로 이동
			raf.writeBytes("\n추가된 데이터");
			System.out.println("- 파일 끝에 데이터를 추가했습니다");

		} catch (IOException e) {
			System.err.println("RandomAccessFile 오류: " + e.getMessage());
		}

		System.out.println("→ RandomAccessFile: 파일의 임의 위치에 접근 가능\n");
	}

	/**
	 * 예제 7: 실무 활용 - 파일 관리 시스템
	 * File 클래스를 활용한 실제 파일 관리 기능
	 */
	public void example7_PracticalFileManagement() {
		System.out.println("=== 예제 7: 실무 활용 - 파일 관리 시스템 ===");

		// 테스트 디렉토리와 파일들 생성
		String testDir = FilePathManager.getFilePath("file_management");
		File directory = new File(testDir);
		directory.mkdirs();

		// 여러 테스트 파일 생성
		String[] fileNames = {"document1.txt", "image1.jpg", "data.csv", "config.properties"};
		for (String fileName : fileNames) {
			File file = new File(directory, fileName);
			try (FileWriter writer = new FileWriter(file)) {
				writer.write("테스트 파일: " + fileName);
			} catch (IOException e) {
				System.err.println("파일 생성 실패: " + fileName);
			}
		}

		// 파일 관리 기능들
		System.out.println("파일 관리 시스템 기능들:");

		// 1. 디렉토리 크기 계산
		long totalSize = calculateDirectorySize(directory);
		System.out.println("- 디렉토리 총 크기: " + totalSize + " 바이트");

		// 2. 파일 타입별 분류
		System.out.println("- 파일 타입별 분류:");
		File[] files = directory.listFiles();
		if (files != null) {
			for (File file : files) {
				String extension = getFileExtension(file.getName());
				System.out.println("  " + file.getName() + " -> " + extension);
			}
		}

		// 3. 최근 수정된 파일 찾기
		File recentFile = findMostRecentFile(directory);
		if (recentFile != null) {
			System.out.println("- 최근 수정된 파일: " + recentFile.getName());
		}

		// 4. 파일 권한 설정
		System.out.println("- 파일 권한 설정:");
		for (File file : files) {
			file.setReadOnly();
			System.out.println("  " + file.getName() + " -> 읽기 전용으로 설정");
		}

		System.out.println("→ 실무 활용: File 클래스로 다양한 파일 관리 기능 구현\n");
	}

	/**
	 * 디렉토리 크기 계산
	 */
	private long calculateDirectorySize(File directory) {
		long totalSize = 0;
		File[] files = directory.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					totalSize += file.length();
				} else if (file.isDirectory()) {
					totalSize += calculateDirectorySize(file);
				}
			}
		}
		return totalSize;
	}

	/**
	 * 파일 확장자 추출
	 */
	private String getFileExtension(String fileName) {
		int lastDot = fileName.lastIndexOf('.');
		if (lastDot > 0) {
			return fileName.substring(lastDot + 1);
		}
		return "확장자 없음";
	}

	/**
	 * 가장 최근에 수정된 파일 찾기
	 */
	private File findMostRecentFile(File directory) {
		File[] files = directory.listFiles();
		if (files == null || files.length == 0) {
			return null;
		}

		File mostRecent = files[0];
		for (File file : files) {
			if (file.lastModified() > mostRecent.lastModified()) {
				mostRecent = file;
			}
		}
		return mostRecent;
	}
} 