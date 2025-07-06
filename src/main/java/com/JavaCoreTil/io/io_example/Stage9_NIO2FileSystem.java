package com.JavaCoreTil.io.io_example;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.Stream;

import com.JavaCoreTil.io.common.FilePathManager;

/**
 * I/O 로드맵 9단계: NIO.2 파일 시스템 (Java 7+)
 * <p>
 * 학습 목표:
 * 1. Path 인터페이스 - 경로 추상화, 플랫폼 독립성
 * 2. Files 유틸리티 - 파일 조작 편의 메서드
 * 3. 파일 속성 관리 - 권한, 시간 정보, 메타데이터
 * 4. 디렉토리 순회 - walkFileTree(), DirectoryStream
 * 5. 파일 변경 감지 - WatchService, 실시간 모니터링
 * 6. Symbolic Link 처리 - 링크 파일 다루기
 */
public class Stage9_NIO2FileSystem {

	private static final boolean DELETE_FLAG = false;

	public static void main(String[] args) throws IOException {
		Stage9_NIO2FileSystem demo = new Stage9_NIO2FileSystem();

		FilePathManager.ensureDirectoryExists();

		System.out.println("=== I/O 로드맵 9단계: NIO.2 파일 시스템 (Java 7+) ===\n");

		try {
			// 1. Path 인터페이스 기본 사용법
			demo.example1_PathInterface();

			// 2. Files 유틸리티 메서드들
			demo.example2_FilesUtility();

			// 3. 파일 속성 관리
			demo.example3_FileAttributes();

			// 4. 디렉토리 순회
			demo.example4_DirectoryTraversal();

			// 5. 파일 변경 감지
			demo.example5_FileWatchService();

			// 6. Symbolic Link 처리
			demo.example6_SymbolicLinks();

			// 7. 실무 활용 - 파일 관리 시스템
			demo.example7_PracticalFileManagement();

			System.out.println("\n=== 9단계 학습 완료! ===");

		} finally {
			System.out.println("\n=== 파일 정리 ===");
			FilePathManager.cleanupFiles(DELETE_FLAG);
		}
	}

	/**
	 * 예제 1: Path 인터페이스 기본 사용법
	 * 경로 추상화와 플랫폼 독립성
	 */
	public void example1_PathInterface() {
		System.out.println("=== 예제 1: Path 인터페이스 기본 사용법 ===");

		// Path 객체 생성 방법들
		Path path1 = Paths.get("folder", "subfolder", "file.txt");
		Path path2 = Path.of("folder", "subfolder", "file.txt"); // Java 11+
		Path path3 = Paths.get("/absolute/path/to/file.txt");
		Path path4 = Paths.get("relative", "path", "file.txt");

		System.out.println("Path 객체 생성:");
		System.out.println("- path1: " + path1);
		System.out.println("- path2: " + path2);
		System.out.println("- path3: " + path3);
		System.out.println("- path4: " + path4);

		// Path 조작 메서드들
		Path basePath = Paths.get("base", "folder");
		Path resolvedPath = basePath.resolve("file.txt");
		Path normalizedPath = resolvedPath.normalize();
		Path absolutePath = normalizedPath.toAbsolutePath();

		System.out.println("\nPath 조작:");
		System.out.println("- 원본: " + basePath);
		System.out.println("- resolve: " + resolvedPath);
		System.out.println("- normalize: " + normalizedPath);
		System.out.println("- absolute: " + absolutePath);

		// Path 구성 요소 접근
		Path complexPath = Paths.get("/home/user/documents/file.txt");

		System.out.println("\nPath 구성 요소:");
		System.out.println("- 파일명: " + complexPath.getFileName());
		System.out.println("- 부모: " + complexPath.getParent());
		System.out.println("- 루트: " + complexPath.getRoot());
		System.out.println("- 이름 개수: " + complexPath.getNameCount());

		for (int i = 0; i < complexPath.getNameCount(); i++) {
			System.out.println("  - 이름 " + i + ": " + complexPath.getName(i));
		}

		// Path 비교
		Path pathA = Paths.get("folder", "file.txt");
		Path pathB = Paths.get("folder", "file.txt");
		Path pathC = Paths.get("other", "file.txt");

		System.out.println("\nPath 비교:");
		System.out.println("- pathA.equals(pathB): " + pathA.equals(pathB));
		System.out.println("- pathA.equals(pathC): " + pathA.equals(pathC));
		System.out.println("- pathA.compareTo(pathB): " + pathA.compareTo(pathB));

		System.out.println("→ Path: 플랫폼 독립적인 경로 추상화\n");
	}

	/**
	 * 예제 2: Files 유틸리티 메서드들
	 * 파일 조작을 위한 편의 메서드들
	 */
	public void example2_FilesUtility() {
		System.out.println("=== 예제 2: Files 유틸리티 메서드들 ===");

		// 파일 존재 여부 확인
		Path testFile = Paths.get(FilePathManager.getFilePath("files_test.txt"));

		System.out.println("파일 존재 여부:");
		System.out.println("- exists: " + Files.exists(testFile));
		System.out.println("- notExists: " + Files.notExists(testFile));
		System.out.println("- isReadable: " + Files.isReadable(testFile));
		System.out.println("- isWritable: " + Files.isWritable(testFile));
		System.out.println("- isExecutable: " + Files.isExecutable(testFile));

		// 파일 생성
		try {
			Files.createFile(testFile);
			System.out.println("\n파일 생성 완료: " + testFile);
		} catch (IOException e) {
			System.err.println("파일 생성 실패: " + e.getMessage());
		}

		// 파일 쓰기
		try {
			String content = "NIO.2 Files 유틸리티 테스트\n두 번째 줄\n세 번째 줄";
			Files.writeString(testFile, content, StandardCharsets.UTF_8);
			System.out.println("파일 쓰기 완료");
		} catch (IOException e) {
			System.err.println("파일 쓰기 실패: " + e.getMessage());
		}

		// 파일 읽기
		try {
			String readContent = Files.readString(testFile, StandardCharsets.UTF_8);
			System.out.println("\n파일 읽기 완료:");
			System.out.println(readContent);
		} catch (IOException e) {
			System.err.println("파일 읽기 실패: " + e.getMessage());
		}

		// 파일 복사
		Path copyFile = Paths.get(FilePathManager.getFilePath("files_copy.txt"));
		try {
			Files.copy(testFile, copyFile, StandardCopyOption.REPLACE_EXISTING);
			System.out.println("\n파일 복사 완료: " + copyFile);
		} catch (IOException e) {
			System.err.println("파일 복사 실패: " + e.getMessage());
		}

		// 파일 이동
		Path moveFile = Paths.get(FilePathManager.getFilePath("files_moved.txt"));
		try {
			Files.move(copyFile, moveFile, StandardCopyOption.REPLACE_EXISTING);
			System.out.println("파일 이동 완료: " + moveFile);
		} catch (IOException e) {
			System.err.println("파일 이동 실패: " + e.getMessage());
		}

		// 파일 삭제
		try {
			Files.delete(moveFile);
			System.out.println("파일 삭제 완료");
		} catch (IOException e) {
			System.err.println("파일 삭제 실패: " + e.getMessage());
		}

		System.out.println("→ Files: 파일 조작을 위한 편의 메서드 모음\n");
	}

	/**
	 * 예제 3: 파일 속성 관리
	 * 권한, 시간 정보, 메타데이터 관리
	 */
	public void example3_FileAttributes() {
		System.out.println("=== 예제 3: 파일 속성 관리 ===");

		Path testFile = Paths.get(FilePathManager.getFilePath("attributes_test.txt"));

		// 테스트 파일 생성
		try {
			Files.writeString(testFile, "속성 테스트 파일", StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.err.println("테스트 파일 생성 실패: " + e.getMessage());
			return;
		}

		// 기본 속성 조회
		try {
			System.out.println("기본 파일 속성:");
			System.out.println("- 크기: " + Files.size(testFile) + " 바이트");
			System.out.println("- 숨김 파일: " + Files.isHidden(testFile));
			System.out.println("- 정규 파일: " + Files.isRegularFile(testFile));
			System.out.println("- 디렉토리: " + Files.isDirectory(testFile));
			System.out.println("- 심볼릭 링크: " + Files.isSymbolicLink(testFile));

		} catch (IOException e) {
			System.err.println("기본 속성 조회 실패: " + e.getMessage());
		}

		// 시간 속성 조회
		try {
			BasicFileAttributes attrs = Files.readAttributes(testFile, BasicFileAttributes.class);

			System.out.println("\n시간 속성:");
			System.out.println("- 생성 시간: " + attrs.creationTime());
			System.out.println("- 마지막 접근 시간: " + attrs.lastAccessTime());
			System.out.println("- 마지막 수정 시간: " + attrs.lastModifiedTime());

		} catch (IOException e) {
			System.err.println("시간 속성 조회 실패: " + e.getMessage());
		}

		// 파일 권한 설정
		try {
			System.out.println("\n파일 권한 설정:");

			// 읽기 전용으로 설정
			testFile.toFile().setReadOnly();
			System.out.println("- 읽기 전용 설정 완료");

			// 권한 확인
			System.out.println("- 읽기 가능: " + Files.isReadable(testFile));
			System.out.println("- 쓰기 가능: " + Files.isWritable(testFile));
			System.out.println("- 실행 가능: " + Files.isExecutable(testFile));

		} catch (Exception e) {
			System.err.println("권한 설정 실패: " + e.getMessage());
		}

		// 파일 시간 수정
		try {
			FileTime newTime = FileTime.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
			Files.setLastModifiedTime(testFile, newTime);
			System.out.println("\n파일 수정 시간 변경 완료: " + newTime);

		} catch (IOException e) {
			System.err.println("시간 수정 실패: " + e.getMessage());
		}

		System.out.println("→ 파일 속성: 권한, 시간, 메타데이터 관리\n");
	}

	/**
	 * 예제 4: 디렉토리 순회
	 * walkFileTree(), DirectoryStream 사용법
	 */
	public void example4_DirectoryTraversal() throws IOException {
		System.out.println("=== 예제 4: 디렉토리 순회 ===");

		// 테스트 디렉토리 구조 생성
		Path baseDir = Paths.get(FilePathManager.getFilePath("traversal_test"));
		createTestDirectoryStructure(baseDir);

		// DirectoryStream을 사용한 디렉토리 내용 조회
		System.out.println("DirectoryStream을 사용한 디렉토리 내용:");
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(baseDir)) {
			for (Path entry : stream) {
				if (Files.isDirectory(entry)) {
					System.out.println("- [DIR] " + entry.getFileName());
				} else {
					System.out.println("- [FILE] " + entry.getFileName());
				}
			}
		} catch (IOException e) {
			System.err.println("디렉토리 조회 실패: " + e.getMessage());
		}

		// 파일 확장자로 필터링
		System.out.println("\n.txt 파일만 조회:");
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(baseDir, "*.txt")) {
			for (Path entry : stream) {
				System.out.println("- " + entry.getFileName());
			}
		} catch (IOException e) {
			System.err.println("필터링 조회 실패: " + e.getMessage());
		}

		// walkFileTree를 사용한 전체 디렉토리 순회
		System.out.println("\nwalkFileTree를 사용한 전체 순회:");
		try {
			Files.walkFileTree(baseDir, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
					System.out.println("  파일: " + baseDir.relativize(file));
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
					if (!dir.equals(baseDir)) {
						System.out.println("  디렉토리: " + baseDir.relativize(dir));
					}
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			System.err.println("전체 순회 실패: " + e.getMessage());
		}

		// Stream API를 사용한 파일 검색
		System.out.println("\nStream API를 사용한 파일 검색:");
		try (Stream<Path> paths = Files.walk(baseDir)) {
			paths.filter(Files::isRegularFile)
				.filter(path -> path.toString().endsWith(".txt"))
				.forEach(path -> System.out.println("  - " + baseDir.relativize(path)));
		} catch (IOException e) {
			System.err.println("Stream 검색 실패: " + e.getMessage());
		}

		System.out.println("→ 디렉토리 순회: 다양한 방법으로 디렉토리 탐색\n");
	}

	/**
	 * 예제 5: 파일 변경 감지
	 * WatchService를 사용한 실시간 모니터링
	 */
	public void example5_FileWatchService() {
		System.out.println("=== 예제 5: 파일 변경 감지 ===");

		Path watchDir = Paths.get(FilePathManager.getFilePath("watch_test"));

		try {
			// 감시할 디렉토리 생성
			Files.createDirectories(watchDir);
			System.out.println("감시 디렉토리 생성: " + watchDir);

			// WatchService 생성
			WatchService watchService = FileSystems.getDefault().newWatchService();

			// 디렉토리 등록
			WatchKey watchKey = watchDir.register(watchService,
				StandardWatchEventKinds.ENTRY_CREATE,
				StandardWatchEventKinds.ENTRY_DELETE,
				StandardWatchEventKinds.ENTRY_MODIFY);

			System.out.println("파일 변경 감지 시작 (5초간 대기)...");

			// 백그라운드에서 파일 변경 시뮬레이션
			Thread fileSimulator = new Thread(() -> {
				try {
					Thread.sleep(1000);

					// 파일 생성
					Path newFile = watchDir.resolve("new_file.txt");
					Files.writeString(newFile, "새로운 파일", StandardCharsets.UTF_8);
					System.out.println("파일 생성됨: " + newFile.getFileName());

					Thread.sleep(1000);

					// 파일 수정
					Files.writeString(newFile, "수정된 내용", StandardCharsets.UTF_8);
					System.out.println("파일 수정됨: " + newFile.getFileName());

					Thread.sleep(1000);

					// 파일 삭제
					Files.delete(newFile);
					System.out.println("파일 삭제됨: " + newFile.getFileName());

				} catch (IOException | InterruptedException e) {
					System.err.println("파일 시뮬레이션 실패: " + e.getMessage());
				}
			});
			fileSimulator.start();

			// 변경 이벤트 감지
			long startTime = System.currentTimeMillis();
			while (System.currentTimeMillis() - startTime < 5000) {
				WatchKey key = watchService.poll(100, java.util.concurrent.TimeUnit.MILLISECONDS);
				if (key != null) {
					for (WatchEvent<?> event : key.pollEvents()) {
						WatchEvent.Kind<?> kind = event.kind();

						if (kind == StandardWatchEventKinds.OVERFLOW) {
							continue;
						}

						@SuppressWarnings("unchecked")
						WatchEvent<Path> ev = (WatchEvent<Path>)event;
						Path fileName = ev.context();

						System.out.println("감지된 이벤트: " + kind.name() + " - " + fileName);
					}

					key.reset();
				}
			}

			watchService.close();
			System.out.println("파일 변경 감지 종료");

		} catch (IOException | InterruptedException e) {
			System.err.println("파일 변경 감지 실패: " + e.getMessage());
		}

		System.out.println("→ WatchService: 실시간 파일 변경 감지\n");
	}

	/**
	 * 예제 6: Symbolic Link 처리
	 * 링크 파일 다루기
	 */
	public void example6_SymbolicLinks() {
		System.out.println("=== 예제 6: Symbolic Link 처리 ===");

		Path originalFile = Paths.get(FilePathManager.getFilePath("original.txt"));
		Path linkFile = Paths.get(FilePathManager.getFilePath("link.txt"));

		try {
			// 원본 파일 생성
			Files.writeString(originalFile, "원본 파일 내용", StandardCharsets.UTF_8);
			System.out.println("원본 파일 생성: " + originalFile);

			// 심볼릭 링크 생성 (Windows에서는 관리자 권한 필요할 수 있음)
			try {
				Files.createSymbolicLink(linkFile, originalFile);
				System.out.println("심볼릭 링크 생성: " + linkFile);

				// 링크 확인
				System.out.println("\n링크 정보:");
				System.out.println("- 링크 존재: " + Files.exists(linkFile));
				System.out.println("- 심볼릭 링크 여부: " + Files.isSymbolicLink(linkFile));

				// 링크 대상 확인
				Path target = Files.readSymbolicLink(linkFile);
				System.out.println("- 링크 대상: " + target);

				// 링크를 통한 파일 읽기
				String linkContent = Files.readString(linkFile, StandardCharsets.UTF_8);
				System.out.println("- 링크 내용: " + linkContent);

				// 원본 파일 수정
				Files.writeString(originalFile, "수정된 원본 파일", StandardCharsets.UTF_8);
				System.out.println("\n원본 파일 수정 완료");

				// 링크를 통한 수정된 내용 확인
				String modifiedContent = Files.readString(linkFile, StandardCharsets.UTF_8);
				System.out.println("- 수정된 링크 내용: " + modifiedContent);

			} catch (UnsupportedOperationException e) {
				System.out.println("심볼릭 링크가 지원되지 않습니다: " + e.getMessage());
			} catch (IOException e) {
				System.err.println("심볼릭 링크 생성 실패: " + e.getMessage());
			}

		} catch (IOException e) {
			System.err.println("파일 생성 실패: " + e.getMessage());
		}

		System.out.println("→ Symbolic Link: 파일의 참조 링크 생성 및 관리\n");
	}

	/**
	 * 예제 7: 실무 활용 - 파일 관리 시스템
	 * NIO.2를 활용한 실제 파일 관리 기능
	 */
	public void example7_PracticalFileManagement() {
		System.out.println("=== 예제 7: 실무 활용 - 파일 관리 시스템 ===");

		Path projectDir = Paths.get(FilePathManager.getFilePath("project"));

		try {
			// 프로젝트 디렉토리 구조 생성
			createProjectStructure(projectDir);

			// 파일 관리 기능들
			System.out.println("파일 관리 시스템 기능들:");

			// 1. 프로젝트 크기 계산
			long totalSize = calculateDirectorySize(projectDir);
			System.out.println("- 프로젝트 총 크기: " + totalSize + " 바이트");

			// 2. 파일 타입별 통계
			System.out.println("- 파일 타입별 통계:");
			try (Stream<Path> paths = Files.walk(projectDir)) {
				paths.filter(Files::isRegularFile)
					.map(path -> getFileExtension(path.getFileName().toString()))
					.collect(java.util.stream.Collectors.groupingBy(
						ext -> ext, java.util.stream.Collectors.counting()))
					.forEach((ext, count) -> System.out.println("  " + ext + ": " + count + "개"));
			}

			// 3. 최근 수정된 파일들
			System.out.println("- 최근 수정된 파일들 (상위 3개):");
			try (Stream<Path> paths = Files.walk(projectDir)) {
				paths.filter(Files::isRegularFile)
					.sorted((p1, p2) -> {
						try {
							return Files.getLastModifiedTime(p2).compareTo(Files.getLastModifiedTime(p1));
						} catch (IOException e) {
							return 0;
						}
					})
					.limit(3)
					.forEach(path -> {
						try {
							System.out.println("  " + projectDir.relativize(path) +
								" (" + Files.getLastModifiedTime(path) + ")");
						} catch (IOException e) {
							System.err.println("시간 조회 실패: " + path);
						}
					});
			}

			// 4. 중복 파일 검사 (간단한 버전)
			System.out.println("- 중복 파일 검사:");
			try (Stream<Path> paths = Files.walk(projectDir)) {
				paths.filter(Files::isRegularFile)
					.collect(java.util.stream.Collectors.groupingBy(path -> {
						try {
							return Files.size(path);
						} catch (IOException e) {
							return 0L;
						}
					}))
					.entrySet().stream()
					.filter(entry -> entry.getValue().size() > 1)
					.forEach(entry -> {
						System.out.println("  크기 " + entry.getKey() + " 바이트:");
						entry.getValue().forEach(path ->
							System.out.println("    " + projectDir.relativize(path)));
					});
			}

		} catch (IOException e) {
			System.err.println("파일 관리 실패: " + e.getMessage());
		}

		System.out.println("→ 실무 활용: NIO.2로 강력한 파일 관리 시스템 구축\n");
	}

	/**
	 * 테스트 디렉토리 구조 생성
	 */
	private void createTestDirectoryStructure(Path baseDir) throws IOException {
		Files.createDirectories(baseDir);

		// 하위 디렉토리들
		Path subDir1 = baseDir.resolve("folder1");
		Path subDir2 = baseDir.resolve("folder2");
		Files.createDirectories(subDir1);
		Files.createDirectories(subDir2);

		// 파일들
		Files.writeString(baseDir.resolve("root.txt"), "루트 파일", StandardCharsets.UTF_8);
		Files.writeString(subDir1.resolve("file1.txt"), "폴더1 파일1", StandardCharsets.UTF_8);
		Files.writeString(subDir1.resolve("file2.txt"), "폴더1 파일2", StandardCharsets.UTF_8);
		Files.writeString(subDir2.resolve("file3.txt"), "폴더2 파일3", StandardCharsets.UTF_8);
		Files.writeString(subDir2.resolve("data.csv"), "CSV 데이터", StandardCharsets.UTF_8);
	}

	/**
	 * 프로젝트 구조 생성
	 */
	private void createProjectStructure(Path projectDir) throws IOException {
		Files.createDirectories(projectDir);

		// 소스 코드 디렉토리
		Path srcDir = projectDir.resolve("src");
		Path mainDir = srcDir.resolve("main");
		Path javaDir = mainDir.resolve("java");
		Files.createDirectories(javaDir);

		// 리소스 디렉토리
		Path resourcesDir = mainDir.resolve("resources");
		Files.createDirectories(resourcesDir);

		// 테스트 디렉토리
		Path testDir = srcDir.resolve("test");
		Path testJavaDir = testDir.resolve("java");
		Files.createDirectories(testJavaDir);

		// 파일들 생성
		Files.writeString(javaDir.resolve("Main.java"), "public class Main {}", StandardCharsets.UTF_8);
		Files.writeString(javaDir.resolve("Utils.java"), "public class Utils {}", StandardCharsets.UTF_8);
		Files.writeString(testJavaDir.resolve("MainTest.java"), "public class MainTest {}", StandardCharsets.UTF_8);
		Files.writeString(resourcesDir.resolve("config.properties"), "app.name=TestApp", StandardCharsets.UTF_8);
		Files.writeString(resourcesDir.resolve("data.json"), "{\"name\":\"test\"}", StandardCharsets.UTF_8);
		Files.writeString(projectDir.resolve("README.md"), "# Test Project", StandardCharsets.UTF_8);
		Files.writeString(projectDir.resolve("pom.xml"), "<project></project>", StandardCharsets.UTF_8);
	}

	/**
	 * 디렉토리 크기 계산
	 */
	private long calculateDirectorySize(Path directory) throws IOException {
		try (Stream<Path> paths = Files.walk(directory)) {
			return paths.filter(Files::isRegularFile)
				.mapToLong(path -> {
					try {
						return Files.size(path);
					} catch (IOException e) {
						return 0L;
					}
				})
				.sum();
		}
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
} 