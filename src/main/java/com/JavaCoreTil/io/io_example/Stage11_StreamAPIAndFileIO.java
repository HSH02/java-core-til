package com.JavaCoreTil.io.io_example;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.JavaCoreTil.io.common.FilePathManager;

/**
 * I/O 로드맵 11단계: 스트림 API와 파일 I/O (Java 8+)
 * <p>
 * 학습 목표:
 * 1. Files.lines() - 파일을 스트림으로 처리
 * 2. 함수형 파일 처리 - filter, map, collect
 * 3. 대용량 파일 스트림 - 메모리 효율적 처리
 * 4. 병렬 스트림 I/O - parallelStream(), 성능 향상
 * 5. 스트림과 예외 처리 - UncheckedIOException
 * 6. 스트림 기반 로그 분석 - 실무 활용 예제
 */
public class Stage11_StreamAPIAndFileIO {

	private static final boolean DELETE_FLAG = false;

	public static void main(String[] args) throws IOException {
		Stage11_StreamAPIAndFileIO demo = new Stage11_StreamAPIAndFileIO();

		FilePathManager.ensureDirectoryExists();

		System.out.println("=== I/O 로드맵 11단계: 스트림 API와 파일 I/O (Java 8+) ===\n");

		try {
			// 1. Files.lines() 기본 사용법
			demo.example1_FilesLinesBasics();

			// 2. 함수형 파일 처리
			demo.example2_FunctionalFileProcessing();

			// 3. 대용량 파일 스트림 처리
			demo.example3_LargeFileStreamProcessing();

			// 4. 병렬 스트림 I/O
			demo.example4_ParallelStreamIO();

			// 5. 스트림과 예외 처리
			demo.example5_StreamExceptionHandling();

			// 6. 스트림 기반 로그 분석
			demo.example6_StreamBasedLogAnalysis();

			// 7. 실무 활용 - 데이터 처리 파이프라인
			demo.example7_PracticalDataProcessingPipeline();

			System.out.println("\n=== 11단계 학습 완료! ===");

		} finally {
			System.out.println("\n=== 파일 정리 ===");
			FilePathManager.cleanupFiles(DELETE_FLAG);
		}
	}

	/**
	 * 예제 1: Files.lines() 기본 사용법
	 * 파일을 스트림으로 읽는 기본적인 방법
	 */
	public void example1_FilesLinesBasics() {
		System.out.println("=== 예제 1: Files.lines() 기본 사용법 ===");

		// 테스트 파일 생성
		String testFile = FilePathManager.getFilePath("stream_test.txt");
		createTestFile(testFile);

		System.out.println("Files.lines()를 사용한 파일 읽기:");

		try (Stream<String> lines = Files.lines(Paths.get(testFile))) {
			// 모든 라인 출력
			lines.forEach(System.out::println);
		} catch (IOException e) {
			System.err.println("파일 읽기 실패: " + e.getMessage());
		}

		System.out.println("\nFiles.lines()를 사용한 라인 수 계산:");

		try (Stream<String> lines = Files.lines(Paths.get(testFile))) {
			long lineCount = lines.count();
			System.out.println("- 총 라인 수: " + lineCount);
		} catch (IOException e) {
			System.err.println("라인 수 계산 실패: " + e.getMessage());
		}

		System.out.println("\nFiles.lines()를 사용한 특정 라인 필터링:");

		try (Stream<String> lines = Files.lines(Paths.get(testFile))) {
			// "Java"가 포함된 라인만 출력
			lines.filter(line -> line.contains("Java"))
				.forEach(line -> System.out.println("- " + line));
		} catch (IOException e) {
			System.err.println("라인 필터링 실패: " + e.getMessage());
		}

		System.out.println("\nFiles.lines()를 사용한 라인 길이 분석:");

		try (Stream<String> lines = Files.lines(Paths.get(testFile))) {
			IntSummaryStatistics stats = lines.mapToInt(String::length)
				.summaryStatistics();

			System.out.println("- 최소 길이: " + stats.getMin());
			System.out.println("- 최대 길이: " + stats.getMax());
			System.out.println("- 평균 길이: " + String.format("%.2f", stats.getAverage()));
			System.out.println("- 총 라인 수: " + stats.getCount());
		} catch (IOException e) {
			System.err.println("라인 길이 분석 실패: " + e.getMessage());
		}

		System.out.println("→ Files.lines(): 파일을 스트림으로 효율적으로 처리\n");
	}

	/**
	 * 예제 2: 함수형 파일 처리
	 * filter, map, collect를 사용한 함수형 처리
	 */
	public void example2_FunctionalFileProcessing() {
		System.out.println("=== 예제 2: 함수형 파일 처리 ===");

		// 로그 파일 생성
		String logFile = FilePathManager.getFilePath("functional_log.txt");
		createLogFile(logFile);

		System.out.println("함수형 파일 처리 예제들:");

		// 1. 로그 레벨별 통계
		System.out.println("\n1. 로그 레벨별 통계:");
		try (Stream<String> lines = Files.lines(Paths.get(logFile))) {
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

			levelStats.forEach((level, count) ->
				System.out.println("- " + level + ": " + count + "개"));
		} catch (IOException e) {
			System.err.println("로그 레벨 통계 실패: " + e.getMessage());
		}

		// 2. 긴 라인 필터링 및 변환
		System.out.println("\n2. 긴 라인 필터링 및 변환:");
		try (Stream<String> lines = Files.lines(Paths.get(logFile))) {
			List<String> longLines = lines
				.filter(line -> line.length() > 50)
				.map(line -> "긴 라인: " + line.substring(0, Math.min(50, line.length())) + "...")
				.collect(Collectors.toList());

			longLines.forEach(System.out::println);
		} catch (IOException e) {
			System.err.println("긴 라인 필터링 실패: " + e.getMessage());
		}

		// 3. 단어 빈도 분석
		System.out.println("\n3. 단어 빈도 분석:");
		try (Stream<String> lines = Files.lines(Paths.get(logFile))) {
			Map<String, Long> wordFreq = lines
				.flatMap(line -> Arrays.stream(line.split("\\s+")))
				.map(word -> word.replaceAll("[^a-zA-Z가-힣]", ""))
				.filter(word -> word.length() > 0)
				.collect(Collectors.groupingBy(
					word -> word.toLowerCase(),
					Collectors.counting()
				));

			// 상위 5개 단어 출력
			wordFreq.entrySet().stream()
				.sorted(Map.Entry.<String, Long> comparingByValue().reversed())
				.limit(5)
				.forEach(entry ->
					System.out.println("- " + entry.getKey() + ": " + entry.getValue() + "회"));
		} catch (IOException e) {
			System.err.println("단어 빈도 분석 실패: " + e.getMessage());
		}

		// 4. 라인 번호와 함께 출력
		System.out.println("\n4. 라인 번호와 함께 출력:");
		try (Stream<String> lines = Files.lines(Paths.get(logFile))) {
			AtomicInteger lineNumber = new AtomicInteger(1);
			lines.limit(5)
				.map(line -> lineNumber.getAndIncrement() + ": " + line)
				.forEach(System.out::println);
		} catch (IOException e) {
			System.err.println("라인 번호 출력 실패: " + e.getMessage());
		}

		System.out.println("→ 함수형 처리: 선언적이고 읽기 쉬운 파일 처리\n");
	}

	/**
	 * 예제 3: 대용량 파일 스트림 처리
	 * 메모리 효율적인 대용량 파일 처리
	 */
	public void example3_LargeFileStreamProcessing() {
		System.out.println("=== 예제 3: 대용량 파일 스트림 처리 ===");

		// 대용량 테스트 파일 생성
		String largeFile = FilePathManager.getFilePath("large_stream_file.txt");
		createLargeFile(largeFile, 100000); // 10만 라인

		System.out.println("대용량 파일 스트림 처리:");

		// 1. 메모리 효율적인 라인 처리
		System.out.println("\n1. 메모리 효율적인 라인 처리:");
		try (Stream<String> lines = Files.lines(Paths.get(largeFile))) {
			long startTime = System.currentTimeMillis();

			// 한 번에 하나씩 처리 (메모리 효율적)
			long processedLines = lines
				.filter(line -> line.length() > 20)
				.mapToLong(line -> {
					// 각 라인을 개별적으로 처리
					return 1L;
				})
				.sum();

			long endTime = System.currentTimeMillis();
			System.out.println("- 처리된 라인 수: " + processedLines);
			System.out.println("- 처리 시간: " + (endTime - startTime) + " ms");

		} catch (IOException e) {
			System.err.println("대용량 파일 처리 실패: " + e.getMessage());
		}

		// 2. 배치 처리
		System.out.println("\n2. 배치 처리:");
		try (Stream<String> lines = Files.lines(Paths.get(largeFile))) {
			long startTime = System.currentTimeMillis();

			// 1000개씩 배치로 처리
			List<String> batch = new ArrayList<>();
			AtomicInteger batchCount = new AtomicInteger(0);

			lines.forEach(line -> {
				batch.add(line);
				if (batch.size() >= 1000) {
					processBatch(batch, batchCount.getAndIncrement());
					batch.clear();
				}
			});

			// 마지막 배치 처리
			if (!batch.isEmpty()) {
				processBatch(batch, batchCount.get());
			}

			long endTime = System.currentTimeMillis();
			System.out.println("- 총 배치 수: " + (batchCount.get() + 1));
			System.out.println("- 처리 시간: " + (endTime - startTime) + " ms");

		} catch (IOException e) {
			System.err.println("배치 처리 실패: " + e.getMessage());
		}

		// 3. 스트림 체이닝 최적화
		System.out.println("\n3. 스트림 체이닝 최적화:");
		try (Stream<String> lines = Files.lines(Paths.get(largeFile))) {
			long startTime = System.currentTimeMillis();

			// 최적화된 체이닝
			long result = lines
				.filter(line -> line.length() > 20)  // 먼저 필터링
				.limit(10000)                       // 제한
				.map(String::toUpperCase)           // 변환
				.filter(line -> line.contains("JAVA")) // 추가 필터링
				.count();

			long endTime = System.currentTimeMillis();
			System.out.println("- 결과: " + result + "개");
			System.out.println("- 처리 시간: " + (endTime - startTime) + " ms");

		} catch (IOException e) {
			System.err.println("스트림 체이닝 실패: " + e.getMessage());
		}

		System.out.println("→ 대용량 파일: 메모리 효율적인 스트림 처리로 성능 최적화\n");
	}

	/**
	 * 예제 4: 병렬 스트림 I/O
	 * parallelStream()을 사용한 성능 향상
	 */
	public void example4_ParallelStreamIO() {
		System.out.println("=== 예제 4: 병렬 스트림 I/O ===");

		// 병렬 처리용 대용량 파일 생성
		String parallelFile = FilePathManager.getFilePath("parallel_test.txt");
		createLargeFile(parallelFile, 50000); // 5만 라인

		System.out.println("병렬 스트림 I/O 성능 비교:");

		// 1. 순차 처리 vs 병렬 처리
		System.out.println("\n1. 순차 처리 vs 병렬 처리:");

		// 순차 처리
		long startTime = System.currentTimeMillis();
		try (Stream<String> lines = Files.lines(Paths.get(parallelFile))) {
			long result = lines
				.filter(line -> line.length() > 15)
				.mapToLong(line -> {
					// 시뮬레이션된 처리 시간
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
					return 1L;
				})
				.sum();

			long sequentialTime = System.currentTimeMillis() - startTime;
			System.out.println("- 순차 처리 결과: " + result + "개");
			System.out.println("- 순차 처리 시간: " + sequentialTime + " ms");

			// 병렬 처리
			startTime = System.currentTimeMillis();
			try (Stream<String> parallelLines = Files.lines(Paths.get(parallelFile))) {
				long parallelResult = parallelLines
					.parallel()
					.filter(line -> line.length() > 15)
					.mapToLong(line -> {
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
						}
						return 1L;
					})
					.sum();

				long parallelTime = System.currentTimeMillis() - startTime;
				System.out.println("- 병렬 처리 결과: " + parallelResult + "개");
				System.out.println("- 병렬 처리 시간: " + parallelTime + " ms");
				System.out.println("- 성능 향상: " + String.format("%.1f", (double)sequentialTime / parallelTime) + "배");
			}

		} catch (IOException e) {
			System.err.println("병렬 처리 실패: " + e.getMessage());
		}

		// 2. 병렬 처리 시 주의사항
		System.out.println("\n2. 병렬 처리 시 주의사항:");

		// 스레드 안전하지 않은 예제
		System.out.println("- 공유 변수 사용 시 주의:");
		try (Stream<String> lines = Files.lines(Paths.get(parallelFile))) {
			AtomicInteger counter = new AtomicInteger(0);

			lines.parallel()
				.filter(line -> line.contains("Java"))
				.forEach(line -> {
					int current = counter.incrementAndGet();
					if (current % 1000 == 0) {
						System.out.println("  처리된 Java 라인: " + current + "개");
					}
				});

			System.out.println("- 최종 결과: " + counter.get() + "개");
		} catch (IOException e) {
			System.err.println("병렬 처리 실패: " + e.getMessage());
		}

		// 3. 병렬 처리 최적화
		System.out.println("\n3. 병렬 처리 최적화:");
		try (Stream<String> lines = Files.lines(Paths.get(parallelFile))) {
			startTime = System.currentTimeMillis();

			// 병렬 처리 + 최적화된 연산
			Map<String, Long> wordCount = lines
				.parallel()
				.flatMap(line -> Arrays.stream(line.split("\\s+")))
				.map(String::toLowerCase)
				.filter(word -> word.length() > 2)
				.collect(Collectors.groupingByConcurrent(
					word -> word,
					Collectors.counting()
				));

			long endTime = System.currentTimeMillis();
			System.out.println("- 고유 단어 수: " + wordCount.size());
			System.out.println("- 처리 시간: " + (endTime - startTime) + " ms");

			// 상위 5개 단어 출력
			wordCount.entrySet().stream()
				.sorted(Map.Entry.<String, Long> comparingByValue().reversed())
				.limit(5)
				.forEach(entry ->
					System.out.println("  - " + entry.getKey() + ": " + entry.getValue() + "회"));

		} catch (IOException e) {
			System.err.println("병렬 처리 최적화 실패: " + e.getMessage());
		}

		System.out.println("→ 병렬 스트림: CPU 집약적 작업에서 성능 향상\n");
	}

	/**
	 * 예제 5: 스트림과 예외 처리
	 * UncheckedIOException과 예외 처리 방법
	 */
	public void example5_StreamExceptionHandling() throws IOException {
		System.out.println("=== 예제 5: 스트림과 예외 처리 ===");

		// 예외가 발생할 수 있는 파일들 생성
		String normalFile = FilePathManager.getFilePath("normal.txt");
		String nonExistentFile = FilePathManager.getFilePath("nonexistent.txt");

		createTestFile(normalFile);

		System.out.println("스트림과 예외 처리 방법들:");

		// 1. try-with-resources 사용
		System.out.println("\n1. try-with-resources 사용:");
		try (Stream<String> lines = Files.lines(Paths.get(normalFile))) {
			lines.forEach(System.out::println);
		} catch (IOException e) {
			System.err.println("파일 읽기 실패: " + e.getMessage());
		}

		// 2. UncheckedIOException 처리
		System.out.println("\n2. UncheckedIOException 처리:");
		try {
			Files.lines(Paths.get(nonExistentFile))
				.forEach(System.out::println);
		} catch (UncheckedIOException e) {
			System.err.println("UncheckedIOException: " + e.getCause().getMessage());
		}

		// 3. 스트림 내에서 예외 처리
		System.out.println("\n3. 스트림 내에서 예외 처리:");
		List<String> filePaths = Arrays.asList(normalFile, nonExistentFile, normalFile);

		filePaths.stream()
			.map(path -> {
				try {
					return Files.lines(Paths.get(path)).count();
				} catch (IOException e) {
					System.err.println("파일 읽기 실패: " + path + " - " + e.getMessage());
					return 0L;
				}
			})
			.forEach(count -> System.out.println("- 라인 수: " + count));

		// 4. 커스텀 예외 처리 유틸리티
		System.out.println("\n4. 커스텀 예외 처리 유틸리티:");
		filePaths.stream()
			.map(this::safeReadLines)
			.filter(Optional::isPresent)
			.map(Optional::get)
			.forEach(count -> System.out.println("- 안전하게 읽은 라인 수: " + count));

		// 5. 복구 가능한 예외 처리
		System.out.println("\n5. 복구 가능한 예외 처리:");
		filePaths.stream()
			.collect(Collectors.groupingBy(
				path -> {
					try {
						Files.lines(Paths.get(path)).count();
						return "성공";
					} catch (IOException e) {
						return "실패";
					}
				},
				Collectors.toList()
			))
			.forEach((status, paths) ->
				System.out.println("- " + status + ": " + paths.size() + "개 파일"));

		System.out.println("→ 예외 처리: 스트림에서 안전하고 우아한 예외 처리\n");
	}

	/**
	 * 예제 6: 스트림 기반 로그 분석
	 * 실무에서 사용할 수 있는 로그 분석 예제
	 */
	public void example6_StreamBasedLogAnalysis() {
		System.out.println("=== 예제 6: 스트림 기반 로그 분석 ===");

		// 복잡한 로그 파일 생성
		String logFile = FilePathManager.getFilePath("complex_log.txt");
		createComplexLogFile(logFile);

		System.out.println("스트림 기반 로그 분석:");

		// 1. 시간대별 로그 분석
		System.out.println("\n1. 시간대별 로그 분석:");
		try (Stream<String> lines = Files.lines(Paths.get(logFile))) {
			Map<String, Long> hourlyStats = lines
				.filter(line -> line.matches(".*\\d{2}:\\d{2}:\\d{2}.*"))
				.map(line -> {
					// 시간 추출 (HH:MM:SS 형식)
					String[] parts = line.split(" ");
					for (String part : parts) {
						if (part.matches("\\d{2}:\\d{2}:\\d{2}")) {
							return part.substring(0, 2) + "시"; // 시간만 추출
						}
					}
					return "알 수 없음";
				})
				.collect(Collectors.groupingBy(
					hour -> hour,
					Collectors.counting()
				));

			hourlyStats.entrySet().stream()
				.sorted(Map.Entry.comparingByKey())
				.forEach(entry ->
					System.out.println("- " + entry.getKey() + ": " + entry.getValue() + "개"));

		} catch (IOException e) {
			System.err.println("시간대별 분석 실패: " + e.getMessage());
		}

		// 2. 에러 패턴 분석
		System.out.println("\n2. 에러 패턴 분석:");
		try (Stream<String> lines = Files.lines(Paths.get(logFile))) {
			Map<String, Long> errorPatterns = lines
				.filter(line -> line.contains("ERROR"))
				.map(line -> {
					// 에러 메시지 패턴 추출
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
				.collect(Collectors.groupingBy(
					pattern -> pattern,
					Collectors.counting()
				));

			errorPatterns.entrySet().stream()
				.sorted(Map.Entry.<String, Long> comparingByValue().reversed())
				.forEach(entry ->
					System.out.println("- " + entry.getKey() + ": " + entry.getValue() + "개"));

		} catch (IOException e) {
			System.err.println("에러 패턴 분석 실패: " + e.getMessage());
		}

		// 3. 사용자 활동 분석
		System.out.println("\n3. 사용자 활동 분석:");
		try (Stream<String> lines = Files.lines(Paths.get(logFile))) {
			Map<String, List<String>> userActivities = lines
				.filter(line -> line.contains("User:"))
				.collect(Collectors.groupingBy(
					line -> {
						// 사용자 ID 추출
						int userIndex = line.indexOf("User:");
						int endIndex = line.indexOf(" ", userIndex + 5);
						return line.substring(userIndex + 5, endIndex > 0 ? endIndex : line.length());
					},
					Collectors.toList()
				));

			userActivities.entrySet().stream()
				.sorted((e1, e2) -> Integer.compare(e2.getValue().size(), e1.getValue().size()))
				.limit(3)
				.forEach(entry ->
					System.out.println("- 사용자 " + entry.getKey() + ": " + entry.getValue().size() + "개 활동"));

		} catch (IOException e) {
			System.err.println("사용자 활동 분석 실패: " + e.getMessage());
		}

		// 4. 성능 메트릭 분석
		System.out.println("\n4. 성능 메트릭 분석:");
		try (Stream<String> lines = Files.lines(Paths.get(logFile))) {
			DoubleSummaryStatistics responseTimeStats = lines
				.filter(line -> line.contains("Response time:"))
				.mapToDouble(line -> {
					// 응답 시간 추출
					int timeIndex = line.indexOf("Response time:");
					String timeStr = line.substring(timeIndex + 14).split(" ")[0];
					return Double.parseDouble(timeStr);
				})
				.summaryStatistics();

			System.out.println("- 평균 응답 시간: " + String.format("%.2f", responseTimeStats.getAverage()) + "ms");
			System.out.println("- 최소 응답 시간: " + responseTimeStats.getMin() + "ms");
			System.out.println("- 최대 응답 시간: " + responseTimeStats.getMax() + "ms");
			System.out.println("- 총 요청 수: " + responseTimeStats.getCount());

		} catch (IOException e) {
			System.err.println("성능 메트릭 분석 실패: " + e.getMessage());
		}

		System.out.println("→ 로그 분석: 스트림으로 복잡한 로그 데이터 분석\n");
	}

	/**
	 * 예제 7: 실무 활용 - 데이터 처리 파이프라인
	 * 실제 프로젝트에서 사용할 수 있는 데이터 처리 예제
	 */
	public void example7_PracticalDataProcessingPipeline() {
		System.out.println("=== 예제 7: 실무 활용 - 데이터 처리 파이프라인 ===");

		// CSV 데이터 파일 생성
		String csvFile = FilePathManager.getFilePath("sales_data.csv");
		createCSVFile(csvFile);

		System.out.println("데이터 처리 파이프라인:");

		// 1. CSV 데이터 읽기 및 파싱
		System.out.println("\n1. CSV 데이터 읽기 및 파싱:");
		try (Stream<String> lines = Files.lines(Paths.get(csvFile))) {
			List<SalesRecord> salesData = lines
				.skip(1) // 헤더 건너뛰기
				.map(this::parseSalesRecord)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.toList());

			System.out.println("- 총 판매 기록: " + salesData.size() + "개");

		} catch (IOException e) {
			System.err.println("CSV 읽기 실패: " + e.getMessage());
		}

		// 2. 데이터 집계 및 분석
		System.out.println("\n2. 데이터 집계 및 분석:");
		try (Stream<String> lines = Files.lines(Paths.get(csvFile))) {
			// 제품별 판매량 집계
			Map<String, DoubleSummaryStatistics> productStats = lines
				.skip(1)
				.map(this::parseSalesRecord)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.groupingBy(
					SalesRecord::getProduct,
					Collectors.summarizingDouble(SalesRecord::getAmount)
				));

			System.out.println("제품별 판매 통계:");
			productStats.forEach((product, stats) -> {
				System.out.println("- " + product + ":");
				System.out.println("  총 판매액: " + String.format("%.2f", stats.getSum()));
				System.out.println("  평균 판매액: " + String.format("%.2f", stats.getAverage()));
				System.out.println("  판매 건수: " + stats.getCount());
			});

		} catch (IOException e) {
			System.err.println("데이터 집계 실패: " + e.getMessage());
		}

		// 3. 데이터 변환 및 출력
		System.out.println("\n3. 데이터 변환 및 출력:");
		String outputFile = FilePathManager.getFilePath("processed_sales.txt");

		try (Stream<String> lines = Files.lines(Paths.get(csvFile));
			 PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {

			// 헤더 작성
			writer.println("제품명\t총판매액\t평균판매액\t판매건수");

			// 데이터 처리 및 출력
			Map<String, List<SalesRecord>> groupedData = lines
				.skip(1)
				.map(this::parseSalesRecord)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.groupingBy(SalesRecord::getProduct));

			groupedData.forEach((product, records) -> {
				double totalAmount = records.stream().mapToDouble(SalesRecord::getAmount).sum();
				double avgAmount = records.stream().mapToDouble(SalesRecord::getAmount).average().orElse(0.0);
				int count = records.size();

				writer.printf("%s\t%.2f\t%.2f\t%d\n", product, totalAmount, avgAmount, count);
			});

			System.out.println("- 처리된 데이터가 " + outputFile + "에 저장되었습니다.");

		} catch (IOException e) {
			System.err.println("데이터 변환 실패: " + e.getMessage());
		}

		// 4. 실시간 데이터 스트리밍 시뮬레이션
		System.out.println("\n4. 실시간 데이터 스트리밍 시뮬레이션:");
		simulateRealTimeDataProcessing();

		System.out.println("→ 데이터 파이프라인: 스트림으로 효율적인 데이터 처리\n");
	}

	// 헬퍼 메서드들

	/**
	 * 테스트 파일 생성
	 */
	private void createTestFile(String filePath) {
		try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
			writer.println("Java는 객체지향 프로그래밍 언어입니다.");
			writer.println("스트림 API는 Java 8에서 도입되었습니다.");
			writer.println("함수형 프로그래밍을 지원합니다.");
			writer.println("파일 처리를 위한 Files.lines() 메서드가 있습니다.");
			writer.println("병렬 처리를 위한 parallel() 메서드가 있습니다.");
		} catch (IOException e) {
			System.err.println("테스트 파일 생성 실패: " + e.getMessage());
		}
	}

	/**
	 * 로그 파일 생성
	 */
	private void createLogFile(String filePath) {
		try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
			writer.println("[INFO] 2024-01-15 10:30:15 사용자 로그인 성공 - user_id: 12345");
			writer.println("[ERROR] 2024-01-15 10:31:22 데이터베이스 연결 실패 - error_code: DB001");
			writer.println("[WARN] 2024-01-15 10:32:10 메모리 사용량 높음 - usage: 85%");
			writer.println("[INFO] 2024-01-15 10:33:45 주문 처리 완료 - order_id: ORD-2024-001");
			writer.println("[ERROR] 2024-01-15 10:34:12 네트워크 타임아웃 - timeout: 30초");
			writer.println("[INFO] 2024-01-15 10:35:20 파일 업로드 완료 - file_size: 2.5MB");
		} catch (IOException e) {
			System.err.println("로그 파일 생성 실패: " + e.getMessage());
		}
	}

	/**
	 * 대용량 파일 생성
	 */
	private void createLargeFile(String filePath, int lineCount) {
		try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
			for (int i = 0; i < lineCount; i++) {
				writer.println("라인 " + (i + 1) + ": 이것은 대용량 파일 처리를 위한 테스트 데이터입니다. " +
					"Java 스트림 API를 사용하여 효율적으로 처리할 수 있습니다. " +
					"test".repeat(i % 5 + 1));
			}
		} catch (IOException e) {
			System.err.println("대용량 파일 생성 실패: " + e.getMessage());
		}
	}

	/**
	 * 복잡한 로그 파일 생성
	 */
	private void createComplexLogFile(String filePath) {
		try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
			String[] hours = {"09", "10", "11", "12", "13", "14", "15", "16"};
			String[] users = {"user001", "user002", "user003", "user004", "user005"};
			String[] activities = {"로그인", "주문", "결제", "로그아웃", "조회"};

			for (int i = 0; i < 1000; i++) {
				String hour = hours[i % hours.length];
				String minute = String.format("%02d", i % 60);
				String user = users[i % users.length];
				String activity = activities[i % activities.length];

				if (i % 50 == 0) {
					writer.printf("[ERROR] %s:%s:%02d Database connection failed for User: %s\n",
						hour, minute, i % 60, user);
				} else if (i % 30 == 0) {
					writer.printf("[WARN] %s:%s:%02d Network timeout for User: %s\n",
						hour, minute, i % 60, user);
				} else if (i % 20 == 0) {
					writer.printf("[ERROR] %s:%s:%02d Permission denied for User: %s\n",
						hour, minute, i % 60, user);
				} else {
					writer.printf("[INFO] %s:%s:%02d User: %s performed %s. Response time: %.2fms\n",
						hour, minute, i % 60, user, activity, 50.0 + Math.random() * 200);
				}
			}
		} catch (IOException e) {
			System.err.println("복잡한 로그 파일 생성 실패: " + e.getMessage());
		}
	}

	/**
	 * CSV 파일 생성
	 */
	private void createCSVFile(String filePath) {
		try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
			writer.println("날짜,제품,수량,단가,총액");
			writer.println("2024-01-15,노트북,2,1200000,2400000");
			writer.println("2024-01-15,마우스,10,50000,500000");
			writer.println("2024-01-16,키보드,5,80000,400000");
			writer.println("2024-01-16,노트북,1,1200000,1200000");
			writer.println("2024-01-17,모니터,3,300000,900000");
			writer.println("2024-01-17,마우스,15,50000,750000");
			writer.println("2024-01-18,키보드,8,80000,640000");
			writer.println("2024-01-18,노트북,3,1200000,3600000");
		} catch (IOException e) {
			System.err.println("CSV 파일 생성 실패: " + e.getMessage());
		}
	}

	/**
	 * 배치 처리
	 */
	private void processBatch(List<String> batch, int batchNumber) {
		// 배치 처리 시뮬레이션
		System.out.println("  배치 " + batchNumber + " 처리 중... (" + batch.size() + "개 라인)");
	}

	/**
	 * 안전한 라인 읽기
	 */
	private Optional<Long> safeReadLines(String filePath) {
		try {
			return Optional.of(Files.lines(Paths.get(filePath)).count());
		} catch (IOException e) {
			return Optional.empty();
		}
	}

	/**
	 * 판매 기록 파싱
	 */
	private Optional<SalesRecord> parseSalesRecord(String line) {
		try {
			String[] parts = line.split(",");
			if (parts.length >= 5) {
				return Optional.of(new SalesRecord(
					parts[0], // 날짜
					parts[1], // 제품
					Integer.parseInt(parts[2]), // 수량
					Double.parseDouble(parts[3]), // 단가
					Double.parseDouble(parts[4])  // 총액
				));
			}
		} catch (NumberFormatException e) {
			// 파싱 실패 시 무시
		}
		return Optional.empty();
	}

	/**
	 * 실시간 데이터 처리 시뮬레이션
	 */
	private void simulateRealTimeDataProcessing() {
		System.out.println("실시간 데이터 스트림 처리 시뮬레이션:");

		// 실시간 데이터 생성
		Stream.generate(() -> {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
				return "실시간 데이터: " + System.currentTimeMillis();
			})
			.limit(10)
			.forEach(data -> System.out.println("- " + data));
	}

	/**
	 * 판매 기록 클래스
	 */
	static class SalesRecord {
		private String date;
		private String product;
		private int quantity;
		private double unitPrice;
		private double amount;

		public SalesRecord(String date, String product, int quantity, double unitPrice, double amount) {
			this.date = date;
			this.product = product;
			this.quantity = quantity;
			this.unitPrice = unitPrice;
			this.amount = amount;
		}

		// Getters
		public String getDate() {
			return date;
		}

		public String getProduct() {
			return product;
		}

		public int getQuantity() {
			return quantity;
		}

		public double getUnitPrice() {
			return unitPrice;
		}

		public double getAmount() {
			return amount;
		}
	}
} 