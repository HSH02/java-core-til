package com.JavaCoreTil.io.io_example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.JavaCoreTil.io.common.FilePathManager;

/**
 * I/O 로드맵 12단계: 실무 I/O 패턴과 고급 기법
 * <p>
 * 학습 목표:
 * 1. 파일 압축 - ZipInputStream/OutputStream, GZIP
 * 2. 텍스트 처리와 인코딩 - Charset, UTF-8 처리
 * 3. 설정 파일 처리 - Properties, JSON, XML
 * 4. 로그 파일 분석 - 대용량 로그 효율적 처리
 * 5. 임시 파일 관리 - createTempFile(), 자동 정리
 * 6. I/O 모니터링 - 성능 측정, 병목 지점 분석
 */
public class Stage12_PracticalIOPatterns {

	private static final boolean DELETE_FLAG = false;

	public static void main(String[] args) {
		Stage12_PracticalIOPatterns demo = new Stage12_PracticalIOPatterns();

		FilePathManager.ensureDirectoryExists();

		System.out.println("=== I/O 로드맵 12단계: 실무 I/O 패턴과 고급 기법 ===\n");

		try {
			// 1. 파일 압축
			demo.example1_FileCompression();

			// 2. 텍스트 처리와 인코딩
			demo.example2_TextProcessingAndEncoding();

			// 3. 설정 파일 처리
			demo.example3_ConfigurationFileProcessing();

			// 4. 로그 파일 분석
			demo.example4_LogFileAnalysis();

			// 5. 임시 파일 관리
			demo.example5_TemporaryFileManagement();

			// 6. I/O 모니터링
			demo.example6_IOMonitoring();

			// 7. 실무 활용 - 종합 I/O 시스템
			demo.example7_PracticalIOSystem();

			System.out.println("\n=== 12단계 학습 완료! ===");

		} finally {
			System.out.println("\n=== 파일 정리 ===");
			FilePathManager.cleanupFiles(DELETE_FLAG);
		}
	}

	/**
	 * 예제 1: 파일 압축
	 * ZipInputStream/OutputStream, GZIP 사용법
	 */
	public void example1_FileCompression() {
		System.out.println("=== 예제 1: 파일 압축 ===");

		// 압축할 파일들 생성
		String[] filesToCompress = {
			FilePathManager.getFilePath("compress1.txt"),
			FilePathManager.getFilePath("compress2.txt"),
			FilePathManager.getFilePath("compress3.txt")
		};

		createFilesForCompression(filesToCompress);

		// 1. ZIP 압축
		System.out.println("1. ZIP 압축:");
		String zipFile = FilePathManager.getFilePath("compressed_files.zip");

		try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
			for (String filePath : filesToCompress) {
				File file = new File(filePath);
				try (FileInputStream fis = new FileInputStream(file)) {

					// ZIP 엔트리 생성
					ZipEntry zipEntry = new ZipEntry(file.getName());
					zos.putNextEntry(zipEntry);

					// 파일 내용 복사
					byte[] buffer = new byte[1024];
					int bytesRead;
					while ((bytesRead = fis.read(buffer)) != -1) {
						zos.write(buffer, 0, bytesRead);
					}

					zos.closeEntry();
					System.out.println("- " + file.getName() + " 압축 완료");
				}
			}

			System.out.println("- ZIP 파일 생성 완료: " + zipFile);

		} catch (IOException e) {
			System.err.println("ZIP 압축 실패: " + e.getMessage());
		}

		// 2. ZIP 압축 해제
		System.out.println("\n2. ZIP 압축 해제:");
		String extractDir = FilePathManager.getFilePath("extracted");
		new File(extractDir).mkdirs();

		try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
			ZipEntry entry;
			while ((entry = zis.getNextEntry()) != null) {
				File extractedFile = new File(extractDir, entry.getName());

				try (FileOutputStream fos = new FileOutputStream(extractedFile)) {
					byte[] buffer = new byte[1024];
					int bytesRead;
					while ((bytesRead = zis.read(buffer)) != -1) {
						fos.write(buffer, 0, bytesRead);
					}
				}

				System.out.println("- " + entry.getName() + " 압축 해제 완료");
				zis.closeEntry();
			}

		} catch (IOException e) {
			System.err.println("ZIP 압축 해제 실패: " + e.getMessage());
		}

		// 3. GZIP 압축
		System.out.println("\n3. GZIP 압축:");
		String gzipFile = FilePathManager.getFilePath("compressed.txt.gz");
		String originalFile = FilePathManager.getFilePath("compress1.txt");

		try (GZIPOutputStream gzos = new GZIPOutputStream(new FileOutputStream(gzipFile));
			 FileInputStream fis = new FileInputStream(originalFile)) {

			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = fis.read(buffer)) != -1) {
				gzos.write(buffer, 0, bytesRead);
			}

			System.out.println("- GZIP 압축 완료: " + gzipFile);

		} catch (IOException e) {
			System.err.println("GZIP 압축 실패: " + e.getMessage());
		}

		// 4. GZIP 압축 해제
		System.out.println("\n4. GZIP 압축 해제:");
		String decompressedFile = FilePathManager.getFilePath("decompressed.txt");

		try (GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(gzipFile));
			 FileOutputStream fos = new FileOutputStream(decompressedFile)) {

			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = gzis.read(buffer)) != -1) {
				fos.write(buffer, 0, bytesRead);
			}

			System.out.println("- GZIP 압축 해제 완료: " + decompressedFile);

		} catch (IOException e) {
			System.err.println("GZIP 압축 해제 실패: " + e.getMessage());
		}

		System.out.println("→ 파일 압축: ZIP, GZIP을 사용한 효율적인 파일 압축/해제\n");
	}

	/**
	 * 예제 2: 텍스트 처리와 인코딩
	 * Charset, UTF-8 처리 방법
	 */
	public void example2_TextProcessingAndEncoding() {
		System.out.println("=== 예제 2: 텍스트 처리와 인코딩 ===");

		// 다양한 인코딩으로 파일 생성
		String utf8File = FilePathManager.getFilePath("utf8_text.txt");
		String utf16File = FilePathManager.getFilePath("utf16_text.txt");
		String eucKrFile = FilePathManager.getFilePath("euckr_text.txt");

		String testText = "안녕하세요! Hello World! こんにちは! 你好!";

		// 1. UTF-8 인코딩
		System.out.println("1. UTF-8 인코딩:");
		try (OutputStreamWriter writer = new OutputStreamWriter(
			new FileOutputStream(utf8File), java.nio.charset.StandardCharsets.UTF_8)) {
			writer.write(testText);
			System.out.println("- UTF-8 파일 생성 완료: " + utf8File);
		} catch (IOException e) {
			System.err.println("UTF-8 파일 생성 실패: " + e.getMessage());
		}

		// 2. UTF-16 인코딩
		System.out.println("\n2. UTF-16 인코딩:");
		try (OutputStreamWriter writer = new OutputStreamWriter(
			new FileOutputStream(utf16File), java.nio.charset.StandardCharsets.UTF_16)) {
			writer.write(testText);
			System.out.println("- UTF-16 파일 생성 완료: " + utf16File);
		} catch (IOException e) {
			System.err.println("UTF-16 파일 생성 실패: " + e.getMessage());
		}

		// 3. 인코딩 감지 및 변환
		System.out.println("\n3. 인코딩 감지 및 변환:");
		try (InputStreamReader reader = new InputStreamReader(
			new FileInputStream(utf8File), java.nio.charset.StandardCharsets.UTF_8)) {

			char[] buffer = new char[1024];
			int charsRead = reader.read(buffer);
			String readText = new String(buffer, 0, charsRead);

			System.out.println("- 읽은 텍스트: " + readText);
			System.out.println("- 텍스트 길이: " + readText.length());
			System.out.println("- 바이트 길이: " + readText.getBytes(java.nio.charset.StandardCharsets.UTF_8).length);

		} catch (IOException e) {
			System.err.println("인코딩 변환 실패: " + e.getMessage());
		}

		// 4. 인코딩별 파일 크기 비교
		System.out.println("\n4. 인코딩별 파일 크기 비교:");
		String[] files = {utf8File, utf16File};
		String[] encodings = {"UTF-8", "UTF-16"};

		for (int i = 0; i < files.length; i++) {
			File file = new File(files[i]);
			if (file.exists()) {
				System.out.println("- " + encodings[i] + ": " + file.length() + " 바이트");
			}
		}

		// 5. 텍스트 처리 및 정규화
		System.out.println("\n5. 텍스트 처리 및 정규화:");
		String complexText = "  안녕하세요!  \n\n  Hello World!  \t こんにちは!  ";

		// 텍스트 정규화
		String normalizedText = complexText.trim()
			.replaceAll("\\s+", " ")
			.replaceAll("[!！]", "!");

		System.out.println("- 원본: '" + complexText + "'");
		System.out.println("- 정규화: '" + normalizedText + "'");

		// 문자별 분석
		System.out.println("- 문자별 분석:");
		for (int i = 0; i < normalizedText.length(); i++) {
			char c = normalizedText.charAt(i);
			System.out.println("  " + i + ": '" + c + "' (코드: " + (int)c + ")");
		}

		System.out.println("→ 텍스트 처리: 다양한 인코딩과 텍스트 정규화\n");
	}

	/**
	 * 예제 3: 설정 파일 처리
	 * Properties, JSON, XML 처리 방법
	 */
	public void example3_ConfigurationFileProcessing() {
		System.out.println("=== 예제 3: 설정 파일 처리 ===");

		// 1. Properties 파일 처리
		System.out.println("1. Properties 파일 처리:");
		String propertiesFile = FilePathManager.getFilePath("app.properties");

		// Properties 파일 생성
		Properties props = new Properties();
		props.setProperty("app.name", "MyApplication");
		props.setProperty("app.version", "1.0.0");
		props.setProperty("database.url", "jdbc:mysql://localhost:3306/mydb");
		props.setProperty("database.user", "admin");
		props.setProperty("database.password", "secret123");
		props.setProperty("server.port", "8080");
		props.setProperty("logging.level", "INFO");

		try (FileOutputStream fos = new FileOutputStream(propertiesFile)) {
			props.store(fos, "Application Configuration");
			System.out.println("- Properties 파일 생성 완료: " + propertiesFile);
		} catch (IOException e) {
			System.err.println("Properties 파일 생성 실패: " + e.getMessage());
		}

		// Properties 파일 읽기
		Properties loadedProps = new Properties();
		try (FileInputStream fis = new FileInputStream(propertiesFile)) {
			loadedProps.load(fis);

			System.out.println("- 로드된 설정:");
			loadedProps.forEach((key, value) ->
				System.out.println("  " + key + " = " + value));

		} catch (IOException e) {
			System.err.println("Properties 파일 읽기 실패: " + e.getMessage());
		}

		// 2. JSON 설정 파일 처리 (간단한 구현)
		System.out.println("\n2. JSON 설정 파일 처리:");
		String jsonFile = FilePathManager.getFilePath("config.json");

		// JSON 파일 생성
		String jsonConfig = """
			{
			    "application": {
			        "name": "MyApplication",
			        "version": "1.0.0",
			        "debug": true
			    },
			    "database": {
			        "url": "jdbc:mysql://localhost:3306/mydb",
			        "user": "admin",
			        "password": "secret123",
			        "poolSize": 10
			    },
			    "server": {
			        "port": 8080,
			        "host": "localhost",
			        "timeout": 30000
			    }
			}
			""";

		try (FileWriter writer = new FileWriter(jsonFile)) {
			writer.write(jsonConfig);
			System.out.println("- JSON 파일 생성 완료: " + jsonFile);
		} catch (IOException e) {
			System.err.println("JSON 파일 생성 실패: " + e.getMessage());
		}

		// JSON 파일 읽기 및 파싱 (간단한 파싱)
		try (BufferedReader reader = new BufferedReader(new FileReader(jsonFile))) {
			StringBuilder content = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				content.append(line);
			}

			String jsonContent = content.toString();
			System.out.println("- JSON 내용:");
			System.out.println(jsonContent);

			// 간단한 JSON 파싱 (실제로는 Jackson, Gson 등 사용)
			parseSimpleJSON(jsonContent);

		} catch (IOException e) {
			System.err.println("JSON 파일 읽기 실패: " + e.getMessage());
		}

		// 3. XML 설정 파일 처리
		System.out.println("\n3. XML 설정 파일 처리:");
		String xmlFile = FilePathManager.getFilePath("config.xml");

		// XML 파일 생성
		String xmlConfig = """
			<?xml version="1.0" encoding="UTF-8"?>
			<configuration>
			    <application>
			        <name>MyApplication</name>
			        <version>1.0.0</version>
			        <debug>true</debug>
			    </application>
			    <database>
			        <url>jdbc:mysql://localhost:3306/mydb</url>
			        <user>admin</user>
			        <password>secret123</password>
			        <poolSize>10</poolSize>
			    </database>
			    <server>
			        <port>8080</port>
			        <host>localhost</host>
			        <timeout>30000</timeout>
			    </server>
			</configuration>
			""";

		try (FileWriter writer = new FileWriter(xmlFile)) {
			writer.write(xmlConfig);
			System.out.println("- XML 파일 생성 완료: " + xmlFile);
		} catch (IOException e) {
			System.err.println("XML 파일 생성 실패: " + e.getMessage());
		}

		// XML 파일 읽기
		try (BufferedReader reader = new BufferedReader(new FileReader(xmlFile))) {
			System.out.println("- XML 내용:");
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println("  " + line);
			}
		} catch (IOException e) {
			System.err.println("XML 파일 읽기 실패: " + e.getMessage());
		}

		System.out.println("→ 설정 파일: Properties, JSON, XML 등 다양한 형식 지원\n");
	}

	/**
	 * 예제 4: 로그 파일 분석
	 * 대용량 로그 효율적 처리
	 */
	public void example4_LogFileAnalysis() {
		System.out.println("=== 예제 4: 로그 파일 분석 ===");

		// 대용량 로그 파일 생성
		String logFile = FilePathManager.getFilePath("large_log.txt");
		createLargeLogFile(logFile, 10000); // 1만 라인

		System.out.println("대용량 로그 파일 분석:");

		// 1. 스트림을 사용한 효율적인 로그 분석
		System.out.println("\n1. 스트림을 사용한 로그 분석:");
		try (Stream<String> lines = Files.lines(Paths.get(logFile))) {
			long startTime = System.currentTimeMillis();

			// 로그 레벨별 통계
			Map<String, Long> levelStats = lines
				.filter(line -> line.contains("["))
				.map(line -> {
					int start = line.indexOf('[');
					int end = line.indexOf(']');
					return line.substring(start + 1, end);
				})
				.collect(Collectors.groupingBy(
					level -> level,
					Collectors.counting()
				));

			long endTime = System.currentTimeMillis();

			System.out.println("- 분석 시간: " + (endTime - startTime) + " ms");
			levelStats.forEach((level, count) ->
				System.out.println("  " + level + ": " + count + "개"));

		} catch (IOException e) {
			System.err.println("로그 분석 실패: " + e.getMessage());
		}

		// 2. 병렬 처리를 사용한 고성능 분석
		System.out.println("\n2. 병렬 처리를 사용한 고성능 분석:");
		try (Stream<String> lines = Files.lines(Paths.get(logFile))) {
			long startTime = System.currentTimeMillis();

			// 병렬 처리로 에러 패턴 분석
			Map<String, Long> errorPatterns = lines
				.parallel()
				.filter(line -> line.contains("ERROR"))
				.map(line -> {
					if (line.contains("Database"))
						return "Database Error";
					if (line.contains("Network"))
						return "Network Error";
					if (line.contains("Timeout"))
						return "Timeout Error";
					if (line.contains("Permission"))
						return "Permission Error";
					return "Other Error";
				})
				.collect(Collectors.groupingByConcurrent(
					pattern -> pattern,
					Collectors.counting()
				));

			long endTime = System.currentTimeMillis();

			System.out.println("- 병렬 분석 시간: " + (endTime - startTime) + " ms");
			errorPatterns.forEach((pattern, count) ->
				System.out.println("  " + pattern + ": " + count + "개"));

		} catch (IOException e) {
			System.err.println("병렬 로그 분석 실패: " + e.getMessage());
		}

		// 3. 실시간 로그 모니터링 시뮬레이션
		System.out.println("\n3. 실시간 로그 모니터링 시뮬레이션:");
		simulateRealTimeLogMonitoring(logFile);

		// 4. 로그 파일 압축 및 아카이빙
		System.out.println("\n4. 로그 파일 압축 및 아카이빙:");
		String compressedLogFile = FilePathManager.getFilePath("large_log.txt.gz");

		try (GZIPOutputStream gzos = new GZIPOutputStream(new FileOutputStream(compressedLogFile));
			 FileInputStream fis = new FileInputStream(logFile)) {

			byte[] buffer = new byte[8192];
			int bytesRead;
			long totalBytes = 0;

			while ((bytesRead = fis.read(buffer)) != -1) {
				gzos.write(buffer, 0, bytesRead);
				totalBytes += bytesRead;
			}

			File originalFile = new File(logFile);
			File compressedFile = new File(compressedLogFile);

			System.out.println("- 원본 크기: " + originalFile.length() + " 바이트");
			System.out.println("- 압축 크기: " + compressedFile.length() + " 바이트");
			System.out.println("- 압축률: " + String.format("%.1f",
				(1.0 - (double)compressedFile.length() / originalFile.length()) * 100) + "%");

		} catch (IOException e) {
			System.err.println("로그 압축 실패: " + e.getMessage());
		}

		System.out.println("→ 로그 분석: 스트림과 병렬 처리를 활용한 효율적인 분석\n");
	}

	/**
	 * 예제 5: 임시 파일 관리
	 * createTempFile(), 자동 정리
	 */
	public void example5_TemporaryFileManagement() {
		System.out.println("=== 예제 5: 임시 파일 관리 ===");

		// 1. 임시 파일 생성
		System.out.println("1. 임시 파일 생성:");
		try {
			// 기본 임시 파일
			File tempFile1 = File.createTempFile("temp", ".txt");
			System.out.println("- 기본 임시 파일: " + tempFile1.getAbsolutePath());

			// 특정 디렉토리에 임시 파일
			File tempDir = new File(FilePathManager.getFilePath("temp_dir"));
			tempDir.mkdirs();
			File tempFile2 = File.createTempFile("app_", ".tmp", tempDir);
			System.out.println("- 지정 디렉토리 임시 파일: " + tempFile2.getAbsolutePath());

			// 임시 파일에 데이터 쓰기
			try (PrintWriter writer = new PrintWriter(new FileWriter(tempFile1))) {
				writer.println("임시 파일 내용");
				writer.println("이 파일은 자동으로 삭제됩니다.");
			}

			// 임시 파일 내용 확인
			try (BufferedReader reader = new BufferedReader(new FileReader(tempFile1))) {
				System.out.println("- 임시 파일 내용:");
				String line;
				while ((line = reader.readLine()) != null) {
					System.out.println("  " + line);
				}
			}

		} catch (IOException e) {
			System.err.println("임시 파일 생성 실패: " + e.getMessage());
		}

		// 2. 임시 파일 자동 정리
		System.out.println("\n2. 임시 파일 자동 정리:");
		List<File> tempFiles = new ArrayList<>();

		try {
			// 여러 임시 파일 생성
			for (int i = 0; i < 5; i++) {
				File tempFile = File.createTempFile("batch_" + i + "_", ".tmp");
				tempFiles.add(tempFile);

				// 임시 파일에 데이터 쓰기
				try (PrintWriter writer = new PrintWriter(new FileWriter(tempFile))) {
					writer.println("배치 처리 임시 파일 " + i);
					writer.println("생성 시간: " + new Date());
				}
			}

			System.out.println("- 생성된 임시 파일 수: " + tempFiles.size());

			// 임시 파일 사용 시뮬레이션
			System.out.println("- 임시 파일 처리 중...");
			Thread.sleep(1000);

			// 임시 파일 정리
			int deletedCount = 0;
			for (File tempFile : tempFiles) {
				if (tempFile.delete()) {
					deletedCount++;
				}
			}

			System.out.println("- 삭제된 임시 파일 수: " + deletedCount);

		} catch (IOException | InterruptedException e) {
			System.err.println("임시 파일 관리 실패: " + e.getMessage());
		}

		// 3. try-with-resources와 임시 파일
		System.out.println("\n3. try-with-resources와 임시 파일:");
		try (TempFileResource tempResource = new TempFileResource("resource_", ".tmp")) {
			File tempFile = tempResource.getFile();

			// 임시 파일 사용
			try (PrintWriter writer = new PrintWriter(new FileWriter(tempFile))) {
				writer.println("자동 정리되는 임시 파일");
				writer.println("try-with-resources로 관리됨");
			}

			System.out.println("- 임시 파일 생성 및 사용: " + tempFile.getAbsolutePath());

			// 임시 파일 내용 확인
			try (BufferedReader reader = new BufferedReader(new FileReader(tempFile))) {
				System.out.println("- 파일 내용:");
				String line;
				while ((line = reader.readLine()) != null) {
					System.out.println("  " + line);
				}
			}

		} catch (IOException e) {
			System.err.println("임시 파일 리소스 실패: " + e.getMessage());
		}

		// 4. 임시 디렉토리 관리
		System.out.println("\n4. 임시 디렉토리 관리:");
		try {
			Path tempDir = Files.createTempDirectory("work_");
			System.out.println("- 임시 디렉토리 생성: " + tempDir);

			// 임시 디렉토리에 파일들 생성
			for (int i = 0; i < 3; i++) {
				Path tempFile = tempDir.resolve("file_" + i + ".txt");
				Files.writeString(tempFile, "임시 파일 " + i + " 내용");
				System.out.println("  - 파일 생성: " + tempFile.getFileName());
			}

			// 임시 디렉토리 내용 확인
			try (Stream<Path> files = Files.list(tempDir)) {
				System.out.println("- 디렉토리 내용:");
				files.forEach(file -> System.out.println("  " + file.getFileName()));
			}

			// 임시 디렉토리 정리 (재귀적 삭제)
			deleteDirectoryRecursively(tempDir);
			System.out.println("- 임시 디렉토리 정리 완료");

		} catch (IOException e) {
			System.err.println("임시 디렉토리 관리 실패: " + e.getMessage());
		}

		System.out.println("→ 임시 파일 관리: 자동 정리와 리소스 관리로 안전한 임시 파일 사용\n");
	}

	/**
	 * 예제 6: I/O 모니터링
	 * 성능 측정, 병목 지점 분석
	 */
	public void example6_IOMonitoring() {
		System.out.println("=== 예제 6: I/O 모니터링 ===");

		// 1. I/O 성능 측정
		System.out.println("1. I/O 성능 측정:");
		String testFile = FilePathManager.getFilePath("performance_test.txt");
		createLargeTestFile(testFile, 1024 * 1024); // 1MB

		// 파일 읽기 성능 측정
		measureFileReadPerformance(testFile);

		// 파일 쓰기 성능 측정
		measureFileWritePerformance();

		// 2. 메모리 사용량 모니터링
		System.out.println("\n2. 메모리 사용량 모니터링:");
		monitorMemoryUsage();

		// 3. I/O 병목 지점 분석
		System.out.println("\n3. I/O 병목 지점 분석:");
		analyzeIOBottlenecks();

		// 4. 실시간 I/O 모니터링
		System.out.println("\n4. 실시간 I/O 모니터링:");
		simulateRealTimeIOMonitoring();

		System.out.println("→ I/O 모니터링: 성능 측정과 병목 지점 분석으로 최적화\n");
	}

	/**
	 * 예제 7: 실무 활용 - 종합 I/O 시스템
	 * 실제 프로젝트에서 사용할 수 있는 종합적인 I/O 시스템
	 */
	public void example7_PracticalIOSystem() {
		System.out.println("=== 예제 7: 실무 활용 - 종합 I/O 시스템 ===");

		// 종합 I/O 시스템 시뮬레이션
		System.out.println("종합 I/O 시스템 시뮬레이션:");

		// 1. 데이터 수집 및 처리 파이프라인
		System.out.println("\n1. 데이터 수집 및 처리 파이프라인:");
		simulateDataProcessingPipeline();

		// 2. 파일 백업 및 복구 시스템
		System.out.println("\n2. 파일 백업 및 복구 시스템:");
		simulateBackupAndRecoverySystem();

		// 3. 로그 집계 및 분석 시스템
		System.out.println("\n3. 로그 집계 및 분석 시스템:");
		simulateLogAggregationSystem();

		// 4. 설정 관리 시스템
		System.out.println("\n4. 설정 관리 시스템:");
		simulateConfigurationManagementSystem();

		System.out.println("→ 종합 I/O 시스템: 실무에서 사용할 수 있는 완전한 I/O 솔루션\n");
	}

	// 헬퍼 메서드들

	/**
	 * 압축용 파일들 생성
	 */
	private void createFilesForCompression(String[] filePaths) {
		for (int i = 0; i < filePaths.length; i++) {
			try (PrintWriter writer = new PrintWriter(new FileWriter(filePaths[i]))) {
				writer.println("압축 테스트 파일 " + (i + 1));
				writer.println("이 파일은 압축 테스트를 위해 생성되었습니다.");
				writer.println("여러 줄의 텍스트를 포함합니다.");
				for (int j = 0; j < 100; j++) {
					writer.println("라인 " + (j + 1) + ": 반복되는 텍스트 데이터 " + j);
				}
			} catch (IOException e) {
				System.err.println("압축용 파일 생성 실패: " + filePaths[i]);
			}
		}
	}

	/**
	 * 간단한 JSON 파싱
	 */
	private void parseSimpleJSON(String jsonContent) {
		System.out.println("- JSON 파싱 결과:");

		// 간단한 키-값 추출
		String[] lines = jsonContent.split("\n");
		for (String line : lines) {
			line = line.trim();
			if (line.contains(":")) {
				String[] parts = line.split(":");
				if (parts.length >= 2) {
					String key = parts[0].replaceAll("[{}\"\\s]", "");
					String value = parts[1].replaceAll("[,}\"\\s]", "");
					if (!key.isEmpty() && !value.isEmpty()) {
						System.out.println("  " + key + " = " + value);
					}
				}
			}
		}
	}

	/**
	 * 대용량 로그 파일 생성
	 */
	private void createLargeLogFile(String filePath, int lineCount) {
		try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
			String[] levels = {"INFO", "WARN", "ERROR", "DEBUG"};
			String[] messages = {
				"사용자 로그인", "데이터베이스 연결", "파일 업로드", "API 호출",
				"메모리 사용량", "네트워크 타임아웃", "권한 오류", "시스템 시작"
			};

			for (int i = 0; i < lineCount; i++) {
				String level = levels[i % levels.length];
				String message = messages[i % messages.length];
				String timestamp = String.format("2024-01-15 %02d:%02d:%02d",
					(i / 3600) % 24, (i / 60) % 60, i % 60);

				writer.printf("[%s] %s - %s (ID: %d)\n", level, timestamp, message, i);
			}
		} catch (IOException e) {
			System.err.println("대용량 로그 파일 생성 실패: " + e.getMessage());
		}
	}

	/**
	 * 실시간 로그 모니터링 시뮬레이션
	 */
	private void simulateRealTimeLogMonitoring(String logFile) {
		System.out.println("실시간 로그 모니터링 시뮬레이션:");

		try (Stream<String> lines = Files.lines(Paths.get(logFile))) {
			AtomicInteger errorCount = new AtomicInteger(0);
			AtomicInteger warnCount = new AtomicInteger(0);

			lines.limit(1000) // 처음 1000라인만 처리
				.forEach(line -> {
					if (line.contains("ERROR")) {
						int count = errorCount.incrementAndGet();
						if (count % 10 == 0) {
							System.out.println("  경고: ERROR 로그 " + count + "개 발견");
						}
					} else if (line.contains("WARN")) {
						warnCount.incrementAndGet();
					}
				});

			System.out.println("  - 총 ERROR: " + errorCount.get() + "개");
			System.out.println("  - 총 WARN: " + warnCount.get() + "개");

		} catch (IOException e) {
			System.err.println("실시간 모니터링 실패: " + e.getMessage());
		}
	}

	/**
	 * 디렉토리 재귀적 삭제
	 */
	private void deleteDirectoryRecursively(Path directory) throws IOException {
		if (Files.exists(directory)) {
			Files.walk(directory)
				.sorted(Comparator.reverseOrder())
				.forEach(path -> {
					try {
						Files.delete(path);
					} catch (IOException e) {
						System.err.println("파일 삭제 실패: " + path);
					}
				});
		}
	}

	/**
	 * 파일 읽기 성능 측정
	 */
	private void measureFileReadPerformance(String filePath) {
		System.out.println("- 파일 읽기 성능 측정:");

		// 버퍼 크기별 성능 측정
		int[] bufferSizes = {1024, 4096, 8192, 16384};

		for (int bufferSize : bufferSizes) {
			long startTime = System.currentTimeMillis();

			try (FileInputStream fis = new FileInputStream(filePath)) {
				byte[] buffer = new byte[bufferSize];
				int bytesRead;
				long totalBytes = 0;

				while ((bytesRead = fis.read(buffer)) != -1) {
					totalBytes += bytesRead;
				}

				long endTime = System.currentTimeMillis();
				double speed = (totalBytes / 1024.0) / (endTime - startTime); // KB/ms

				System.out.println("  버퍼 " + bufferSize + "바이트: " +
					String.format("%.2f", speed) + " KB/ms");

			} catch (IOException e) {
				System.err.println("성능 측정 실패: " + e.getMessage());
			}
		}
	}

	/**
	 * 파일 쓰기 성능 측정
	 */
	private void measureFileWritePerformance() {
		System.out.println("- 파일 쓰기 성능 측정:");

		String testFile = FilePathManager.getFilePath("write_performance_test.txt");

		long startTime = System.currentTimeMillis();

		try (FileOutputStream fos = new FileOutputStream(testFile)) {
			byte[] data = "테스트 데이터\n".getBytes(java.nio.charset.StandardCharsets.UTF_8);

			for (int i = 0; i < 10000; i++) {
				fos.write(data);
			}

			long endTime = System.currentTimeMillis();
			double speed = (10000 * data.length / 1024.0) / (endTime - startTime); // KB/ms

			System.out.println("  쓰기 속도: " + String.format("%.2f", speed) + " KB/ms");

		} catch (IOException e) {
			System.err.println("쓰기 성능 측정 실패: " + e.getMessage());
		}
	}

	/**
	 * 메모리 사용량 모니터링
	 */
	private void monitorMemoryUsage() {
		Runtime runtime = Runtime.getRuntime();

		long totalMemory = runtime.totalMemory();
		long freeMemory = runtime.freeMemory();
		long usedMemory = totalMemory - freeMemory;
		long maxMemory = runtime.maxMemory();

		System.out.println("- 메모리 사용량:");
		System.out.println("  총 메모리: " + (totalMemory / 1024 / 1024) + " MB");
		System.out.println("  사용 메모리: " + (usedMemory / 1024 / 1024) + " MB");
		System.out.println("  여유 메모리: " + (freeMemory / 1024 / 1024) + " MB");
		System.out.println("  최대 메모리: " + (maxMemory / 1024 / 1024) + " MB");
		System.out.println("  사용률: " + String.format("%.1f", (double)usedMemory / totalMemory * 100) + "%");
	}

	/**
	 * I/O 병목 지점 분석
	 */
	private void analyzeIOBottlenecks() {
		System.out.println("- I/O 병목 지점 분석:");
		System.out.println("  디스크 I/O: 파일 읽기/쓰기 속도");
		System.out.println("  네트워크 I/O: 소켓 통신 속도");
		System.out.println("  메모리 I/O: 버퍼 관리 효율성");
		System.out.println("  CPU 사용률: I/O 처리 오버헤드");
	}

	/**
	 * 실시간 I/O 모니터링 시뮬레이션
	 */
	private void simulateRealTimeIOMonitoring() {
		System.out.println("- 실시간 I/O 모니터링:");

		for (int i = 0; i < 5; i++) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

			double cpuUsage = Math.random() * 100;
			double memoryUsage = 50 + Math.random() * 30;
			double diskIO = Math.random() * 100;

			System.out.println("  시간 " + (i + 1) + "초:");
			System.out.println("    CPU: " + String.format("%.1f", cpuUsage) + "%");
			System.out.println("    메모리: " + String.format("%.1f", memoryUsage) + "%");
			System.out.println("    디스크 I/O: " + String.format("%.1f", diskIO) + " MB/s");
		}
	}

	/**
	 * 데이터 처리 파이프라인 시뮬레이션
	 */
	private void simulateDataProcessingPipeline() {
		System.out.println("데이터 수집 → 처리 → 저장 파이프라인:");

		// 데이터 수집
		System.out.println("  1. 데이터 수집 중...");

		// 데이터 처리
		System.out.println("  2. 데이터 처리 중...");

		// 데이터 저장
		System.out.println("  3. 데이터 저장 중...");

		System.out.println("  파이프라인 완료!");
	}

	/**
	 * 백업 및 복구 시스템 시뮬레이션
	 */
	private void simulateBackupAndRecoverySystem() {
		System.out.println("파일 백업 및 복구 시스템:");

		// 백업 생성
		System.out.println("  1. 백업 생성 중...");

		// 백업 검증
		System.out.println("  2. 백업 검증 중...");

		// 복구 테스트
		System.out.println("  3. 복구 테스트 중...");

		System.out.println("  백업 시스템 준비 완료!");
	}

	/**
	 * 로그 집계 시스템 시뮬레이션
	 */
	private void simulateLogAggregationSystem() {
		System.out.println("로그 집계 및 분석 시스템:");

		// 로그 수집
		System.out.println("  1. 로그 수집 중...");

		// 로그 분석
		System.out.println("  2. 로그 분석 중...");

		// 보고서 생성
		System.out.println("  3. 보고서 생성 중...");

		System.out.println("  로그 집계 완료!");
	}

	/**
	 * 설정 관리 시스템 시뮬레이션
	 */
	private void simulateConfigurationManagementSystem() {
		System.out.println("설정 관리 시스템:");

		// 설정 로드
		System.out.println("  1. 설정 로드 중...");

		// 설정 검증
		System.out.println("  2. 설정 검증 중...");

		// 설정 적용
		System.out.println("  3. 설정 적용 중...");

		System.out.println("  설정 관리 완료!");
	}

	/**
	 * 대용량 테스트 파일 생성
	 */
	private void createLargeTestFile(String filePath, int size) {
		try (FileOutputStream fos = new FileOutputStream(filePath)) {
			byte[] buffer = new byte[8192];
			int remaining = size;

			while (remaining > 0) {
				int toWrite = Math.min(buffer.length, remaining);
				fos.write(buffer, 0, toWrite);
				remaining -= toWrite;
			}
		} catch (IOException e) {
			System.err.println("대용량 테스트 파일 생성 실패: " + e.getMessage());
		}
	}

	/**
	 * 임시 파일 리소스 클래스
	 */
	static class TempFileResource implements AutoCloseable {
		private File tempFile;

		public TempFileResource(String prefix, String suffix) throws IOException {
			this.tempFile = File.createTempFile(prefix, suffix);
			this.tempFile.deleteOnExit(); // JVM 종료 시 자동 삭제
		}

		public File getFile() {
			return tempFile;
		}

		@Override
		public void close() throws IOException {
			if (tempFile != null && tempFile.exists()) {
				tempFile.delete();
			}
		}
	}
} 