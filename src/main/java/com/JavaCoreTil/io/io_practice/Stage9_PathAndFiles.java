package com.JavaCoreTil.io.io_practice;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import com.JavaCoreTil.io.common.FilePathManager;

public class Stage9_PathAndFiles {

	private static final String BASE_PATH = "src/main/java/com/JavaCoreTil/io/file/PathAndFiles";
	private static final String FILE_PATH = BASE_PATH + "/example.txt";

	public static void main(String[] args) throws IOException {

		Stage9_PathAndFiles stage = new Stage9_PathAndFiles();

		FilePathManager.ensureCustomDirectoryExists(BASE_PATH);

		print("createFile");
		stage.createFile();

		print("createFileWithContent");
		stage.createFileWithContent();

		print("createDirectory");
		stage.createDirectory();

		print("readFileAsBytes");
		stage.readFileAsBytes();

		print("readFileAsLines");
		stage.readFileAsLines();

		print("readFileAttributes");
		stage.readFileAttributes();

		print("updateFileContent");
		stage.updateFileContent();
		stage.readFileAsBytes();

		print("appendToFile");
		stage.appendToFile();
		stage.readFileAsBytes();

		print("updateFileAttributes");
		stage.updateFileAttributes();

		print("copyFile");
		stage.copyFile();

		print("moveFile");
		stage.moveFile();

		print("deleteFileIfExists");
		stage.deleteFileIfExists();

		print("deleteDirectory");
		stage.deleteDirectory();
	}

	public static void print(String msg) {
		System.out.printf("%n = %s %s %n%n", msg, "=".repeat(30));
	}

	private void createFile() throws IOException {
		Path filePath = Paths.get(FILE_PATH);

		Files.createDirectories(filePath.getParent());
		Files.createFile(filePath);
		System.out.println("파일 생성됨 :" + filePath);
	}

	private void createFileWithContent() throws IOException {
		Path filePath = Paths.get(BASE_PATH + "/sample.txt");

		String content = "엘렐레";
		Files.write(filePath, content.getBytes(StandardCharsets.UTF_8));
		System.out.println("파일 생성 및 내용 쓰기 완료: " + filePath);
	}

	private void createDirectory() throws IOException {
		Path dirPath = Paths.get(BASE_PATH, "subdir", "nested");

		Files.createDirectories(dirPath);
		System.out.println("디렉토리 생성됨: " + dirPath);
	}

	private void readFileAsBytes() throws IOException {
		Path filePath = Paths.get(BASE_PATH + "/sample.txt");

		if (Files.exists(filePath)) {
			byte[] bytes = Files.readAllBytes(filePath);
			String content = new String(bytes, StandardCharsets.UTF_8);
			System.out.println("파일 내용: " + content);
		} else {
			System.out.println("파일이 존재하지 않음: " + filePath);
		}
	}

	private void readFileAsLines() throws IOException {
		Path filePath = Paths.get(BASE_PATH + "/sample.txt");

		if (Files.exists(filePath)) {
			List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
			System.out.println("파일 라인 수" + lines.size());

			for (String line : lines) {
				System.out.println("라인: " + line);
			}
		}
	}

	private void readFileAttributes() throws IOException {
		Path filePath = Paths.get(BASE_PATH + "/sample.txt");

		if (Files.exists(filePath)) {
			// 기본 속성
			long size = Files.size(filePath);
			FileTime lastModified = Files.getLastModifiedTime(filePath);
			boolean isDirectory = Files.isDirectory(filePath);
			boolean isRegularFile = Files.isRegularFile(filePath);

			System.out.println("파일 크기: " + size + " bytes");
			System.out.println("마지막 수정: " + lastModified);
			System.out.println("디렉토리 여부: " + isDirectory);
			System.out.println("일반 파일 여부: " + isRegularFile);
		}
	}

	private void updateFileContent() throws IOException {
		Path filePath = Paths.get(FILE_PATH);

		if (Files.exists(filePath)) {
			String newContent = "Updated content - " + LocalDateTime.now();
			Files.write(filePath, newContent.getBytes(StandardCharsets.UTF_8));
			System.out.println("파일 내용 업데이트 완료");
		}
	}

	private void appendToFile() throws IOException {
		Path filePath = Paths.get(FILE_PATH);

		if (Files.exists(filePath)) {
			String appendContent = "\nAppended content - " + LocalDateTime.now();
			Files.write(filePath, appendContent.getBytes(StandardCharsets.UTF_8),
				StandardOpenOption.APPEND);
			System.out.println("파일 내용 추가 완료");
		}
	}

	private void updateFileAttributes() throws IOException {
		Path filePath = Paths.get(FILE_PATH);

		if (Files.exists(filePath)) {
			// 마지막 수정 시간 변경
			FileTime newTime = FileTime.from(Instant.now());
			Files.setLastModifiedTime(filePath, newTime);

			// 읽기 전용으로 설정
			Files.setAttribute(filePath, "dos:readonly", true);

			System.out.println("파일 속성 수정 완료");
		}
	}

	private void moveFile() throws IOException {
		Path sourcePath = Paths.get(FILE_PATH);
		Path targetPath = Paths.get(BASE_PATH + "/backup");

		if (Files.exists(sourcePath)) {
			// 대상 디렉토리 생성
			Files.createDirectories(targetPath.getParent());

			// 파일 이동
			Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
			System.out.println("파일 이동 완료: " + sourcePath + " → " + targetPath);
		}
	}

	private void copyFile() throws IOException {
		Path sourcePath = Paths.get(FILE_PATH);
		Path targetPath = Paths.get(BASE_PATH + "/copy");

		if (Files.exists(sourcePath)) {
			// 대상 디렉토리 생성
			Files.createDirectories(targetPath.getParent());

			// 파일 복사
			Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
			System.out.println("파일 복사 완료: " + sourcePath + " → " + targetPath);
		}
	}

	private void deleteFileIfExists() throws IOException {
		Path filePath = Paths.get(FILE_PATH);

		boolean deleted = Files.deleteIfExists(filePath);
		if (deleted) {
			System.out.println("파일 삭제 완료: " + filePath);
		} else {
			System.out.println("파일이 존재하지 않음: " + filePath);
		}
	}

	private void deleteDirectory() throws IOException {
		Path dirPath = Paths.get("/dgfjm;gsdfnjklgdfnjklgdfjnkgsdfnjklsdfgnjkl");

		if (Files.exists(dirPath) && Files.isDirectory(dirPath)) {
			// 디렉토리가 비어있어야 삭제 가능
			Files.delete(dirPath);
			System.out.println("디렉토리 삭제 완료: " + dirPath);
		}
	}
}
