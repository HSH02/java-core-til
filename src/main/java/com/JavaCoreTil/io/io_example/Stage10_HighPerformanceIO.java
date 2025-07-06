package com.JavaCoreTil.io.io_example;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

import com.JavaCoreTil.io.common.FilePathManager;

/**
 * I/O 로드맵 10단계: 고성능 I/O 기법
 * <p>
 * 학습 목표:
 * 1. 메모리 맵핑 - MappedByteBuffer, 대용량 파일 처리
 * 2. Zero-Copy I/O - transferTo/transferFrom
 * 3. 비동기 파일 I/O - AsynchronousFileChannel
 * 4. 논블로킹 I/O - Selector, SocketChannel
 * 5. 성능 최적화 - 버퍼 크기, 블로킹 vs 논블로킹
 * 6. 벤치마킹 - 실무 성능 측정
 */
public class Stage10_HighPerformanceIO {

	private static final boolean DELETE_FLAG = false;

	public static void main(String[] args) {
		Stage10_HighPerformanceIO demo = new Stage10_HighPerformanceIO();

		FilePathManager.ensureDirectoryExists();

		System.out.println("=== I/O 로드맵 10단계: 고성능 I/O 기법 ===\n");

		try {
			// 1. 메모리 맵핑 (Memory Mapping)
			demo.example1_MemoryMapping();

			// 2. Zero-Copy I/O
			demo.example2_ZeroCopyIO();

			// 3. 비동기 파일 I/O
			demo.example3_AsynchronousFileIO();

			// 4. 성능 최적화 기법
			demo.example4_PerformanceOptimization();

			// 5. 버퍼 크기 최적화
			demo.example5_BufferSizeOptimization();

			// 6. 벤치마킹
			demo.example6_Benchmarking();

			// 7. 실무 활용 - 고성능 파일 처리
			demo.example7_PracticalHighPerformanceIO();

			System.out.println("\n=== 10단계 학습 완료! ===");

		} finally {
			System.out.println("\n=== 파일 정리 ===");
			FilePathManager.cleanupFiles(DELETE_FLAG);
		}
	}

	/**
	 * 예제 1: 메모리 맵핑 (Memory Mapping)
	 * MappedByteBuffer를 사용한 대용량 파일 처리
	 */
	public void example1_MemoryMapping() {
		System.out.println("=== 예제 1: 메모리 맵핑 (Memory Mapping) ===");

		String sourceFile = FilePathManager.getFilePath("memory_mapping_source.txt");
		String targetFile = FilePathManager.getFilePath("memory_mapping_target.txt");

		// 대용량 테스트 파일 생성
		createLargeTestFile(sourceFile, 1024 * 1024); // 1MB

		System.out.println("메모리 맵핑을 사용한 파일 복사:");

		long startTime = System.currentTimeMillis();

		try (FileChannel sourceChannel = new FileInputStream(sourceFile).getChannel();
			 FileChannel targetChannel = new FileOutputStream(targetFile).getChannel()) {

			// 메모리 맵핑
			MappedByteBuffer sourceBuffer = sourceChannel.map(
				FileChannel.MapMode.READ_ONLY, 0, sourceChannel.size());
			MappedByteBuffer targetBuffer = targetChannel.map(
				FileChannel.MapMode.READ_WRITE, 0, sourceChannel.size());

			// 메모리에서 직접 복사
			targetBuffer.put(sourceBuffer);

			// 변경사항을 디스크에 강제 쓰기
			targetBuffer.force();

			long endTime = System.currentTimeMillis();
			System.out.println("- 복사 완료: " + (endTime - startTime) + " ms");
			System.out.println("- 파일 크기: " + sourceChannel.size() + " 바이트");

		} catch (IOException e) {
			System.err.println("메모리 맵핑 실패: " + e.getMessage());
		}

		// 메모리 맵핑을 사용한 파일 검색
		System.out.println("\n메모리 맵핑을 사용한 파일 검색:");
		try (FileChannel channel = new FileInputStream(sourceFile).getChannel()) {

			MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());

			// 특정 패턴 검색
			byte[] pattern = "test".getBytes();
			int matchCount = 0;

			for (int i = 0; i <= buffer.limit() - pattern.length; i++) {
				boolean match = true;
				for (int j = 0; j < pattern.length; j++) {
					if (buffer.get(i + j) != pattern[j]) {
						match = false;
						break;
					}
				}
				if (match) {
					matchCount++;
				}
			}

			System.out.println("- 'test' 패턴 발견: " + matchCount + "개");

		} catch (IOException e) {
			System.err.println("파일 검색 실패: " + e.getMessage());
		}

		System.out.println("→ 메모리 맵핑: 대용량 파일을 메모리처럼 빠르게 처리\n");
	}

	/**
	 * 예제 2: Zero-Copy I/O
	 * transferTo/transferFrom을 사용한 효율적인 파일 복사
	 */
	public void example2_ZeroCopyIO() {
		System.out.println("=== 예제 2: Zero-Copy I/O ===");

		String sourceFile = FilePathManager.getFilePath("zero_copy_source.txt");
		String targetFile = FilePathManager.getFilePath("zero_copy_target.txt");

		// 테스트 파일 생성
		createLargeTestFile(sourceFile, 512 * 1024); // 512KB

		System.out.println("Zero-Copy I/O를 사용한 파일 복사:");

		long startTime = System.currentTimeMillis();

		try (FileChannel sourceChannel = new FileInputStream(sourceFile).getChannel();
			 FileChannel targetChannel = new FileOutputStream(targetFile).getChannel()) {

			// Zero-Copy 전송
			long transferred = sourceChannel.transferTo(0, sourceChannel.size(), targetChannel);

			long endTime = System.currentTimeMillis();
			System.out.println("- 전송 완료: " + transferred + " 바이트");
			System.out.println("- 소요 시간: " + (endTime - startTime) + " ms");
			System.out.println("- 전송 속도: " + (transferred / 1024) / (endTime - startTime) + " KB/ms");

		} catch (IOException e) {
			System.err.println("Zero-Copy 전송 실패: " + e.getMessage());
		}

		// 여러 파일을 하나로 병합 (Zero-Copy)
		System.out.println("\nZero-Copy를 사용한 파일 병합:");
		String[] filesToMerge = {
			FilePathManager.getFilePath("merge1.txt"),
			FilePathManager.getFilePath("merge2.txt"),
			FilePathManager.getFilePath("merge3.txt")
		};

		String mergedFile = FilePathManager.getFilePath("merged.txt");

		// 병합할 파일들 생성
		for (int i = 0; i < filesToMerge.length; i++) {
			createTestFile(filesToMerge[i], "파일 " + (i + 1) + " 내용\n");
		}

		try (FileChannel targetChannel = new FileOutputStream(mergedFile).getChannel()) {

			long totalTransferred = 0;

			for (String filePath : filesToMerge) {
				try (FileChannel sourceChannel = new FileInputStream(filePath).getChannel()) {
					long transferred = sourceChannel.transferTo(0, sourceChannel.size(), targetChannel);
					totalTransferred += transferred;
					System.out.println("- " + Paths.get(filePath).getFileName() + " 병합됨: " + transferred + " 바이트");
				}
			}

			System.out.println("- 총 병합된 바이트: " + totalTransferred);

		} catch (IOException e) {
			System.err.println("파일 병합 실패: " + e.getMessage());
		}

		System.out.println("→ Zero-Copy: 커널 공간에서 직접 전송하여 성능 향상\n");
	}

	/**
	 * 예제 3: 비동기 파일 I/O
	 * AsynchronousFileChannel을 사용한 비동기 처리
	 */
	public void example3_AsynchronousFileIO() {
		System.out.println("=== 예제 3: 비동기 파일 I/O ===");

		String asyncFile = FilePathManager.getFilePath("async_test.txt");

		// 테스트 파일 생성
		createLargeTestFile(asyncFile, 256 * 1024); // 256KB

		// 비동기 파일 읽기
		System.out.println("비동기 파일 읽기:");

		try (AsynchronousFileChannel asyncChannel = AsynchronousFileChannel.open(
			Paths.get(asyncFile), StandardOpenOption.READ)) {

			ByteBuffer buffer = ByteBuffer.allocate(8192);

			// 비동기 읽기 시작
			Future<Integer> future = asyncChannel.read(buffer, 0);

			System.out.println("- 비동기 읽기 시작됨 (백그라운드에서 실행 중)");

			// 다른 작업 수행 (읽기가 완료될 때까지)
			System.out.println("- 다른 작업 수행 중...");
			Thread.sleep(100);

			// 읽기 완료 대기
			int bytesRead = future.get();
			System.out.println("- 읽기 완료: " + bytesRead + " 바이트");

			// 버퍼 내용 출력
			buffer.flip();
			byte[] data = new byte[buffer.remaining()];
			buffer.get(data);
			String content = new String(data, StandardCharsets.UTF_8);
			System.out.println("- 읽은 내용 (처음 100자): " + content.substring(0, Math.min(100, content.length())));

		} catch (IOException | InterruptedException | java.util.concurrent.ExecutionException e) {
			System.err.println("비동기 읽기 실패: " + e.getMessage());
		}

		// 비동기 파일 쓰기
		System.out.println("\n비동기 파일 쓰기:");

		try (AsynchronousFileChannel asyncChannel = AsynchronousFileChannel.open(
			Paths.get(FilePathManager.getFilePath("async_write.txt")),
			StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {

			String content = "비동기 파일 쓰기 테스트\n".repeat(1000);
			ByteBuffer buffer = ByteBuffer.wrap(content.getBytes(StandardCharsets.UTF_8));

			// 비동기 쓰기 시작
			Future<Integer> future = asyncChannel.write(buffer, 0);

			System.out.println("- 비동기 쓰기 시작됨");

			// 다른 작업 수행
			System.out.println("- 다른 작업 수행 중...");
			Thread.sleep(50);

			// 쓰기 완료 대기
			int bytesWritten = future.get();
			System.out.println("- 쓰기 완료: " + bytesWritten + " 바이트");

		} catch (IOException | InterruptedException | java.util.concurrent.ExecutionException e) {
			System.err.println("비동기 쓰기 실패: " + e.getMessage());
		}

		// CompletableFuture를 사용한 비동기 처리
		System.out.println("\nCompletableFuture를 사용한 비동기 처리:");

		ExecutorService executor = Executors.newFixedThreadPool(4);

		try {
			CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
				try {
					Thread.sleep(100);
					return "작업 1 완료";
				} catch (InterruptedException e) {
					return "작업 1 실패";
				}
			}, executor);

			CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
				try {
					Thread.sleep(150);
					return "작업 2 완료";
				} catch (InterruptedException e) {
					return "작업 2 실패";
				}
			}, executor);

			// 모든 작업 완료 대기
			CompletableFuture<Void> allFutures = CompletableFuture.allOf(future1, future2);
			allFutures.get();

			System.out.println("- " + future1.get());
			System.out.println("- " + future2.get());

		} catch (InterruptedException | java.util.concurrent.ExecutionException e) {
			System.err.println("CompletableFuture 실패: " + e.getMessage());
		} finally {
			executor.shutdown();
		}

		System.out.println("→ 비동기 I/O: I/O 작업을 백그라운드에서 처리하여 응답성 향상\n");
	}

	/**
	 * 예제 4: 성능 최적화 기법
	 * 블로킹 vs 논블로킹, 버퍼 최적화
	 */
	public void example4_PerformanceOptimization() {
		System.out.println("=== 예제 4: 성능 최적화 기법 ===");

		String testFile = FilePathManager.getFilePath("performance_test.txt");
		createLargeTestFile(testFile, 1024 * 1024); // 1MB

		// 블로킹 vs 논블로킹 비교
		System.out.println("블로킹 vs 논블로킹 성능 비교:");

		// 블로킹 방식
		long startTime = System.currentTimeMillis();
		try (FileInputStream fis = new FileInputStream(testFile);
			 FileOutputStream fos = new FileOutputStream(FilePathManager.getFilePath("blocking_copy.txt"))) {

			byte[] buffer = new byte[8192];
			int bytesRead;
			while ((bytesRead = fis.read(buffer)) != -1) {
				fos.write(buffer, 0, bytesRead);
			}

		} catch (IOException e) {
			System.err.println("블로킹 복사 실패: " + e.getMessage());
		}
		long blockingTime = System.currentTimeMillis() - startTime;

		// 논블로킹 방식 (NIO)
		startTime = System.currentTimeMillis();
		try (FileChannel sourceChannel = new FileInputStream(testFile).getChannel();
			 FileChannel targetChannel = new FileOutputStream(
				 FilePathManager.getFilePath("nonblocking_copy.txt")).getChannel()) {

			sourceChannel.transferTo(0, sourceChannel.size(), targetChannel);

		} catch (IOException e) {
			System.err.println("논블로킹 복사 실패: " + e.getMessage());
		}
		long nonblockingTime = System.currentTimeMillis() - startTime;

		System.out.println("- 블로킹 방식: " + blockingTime + " ms");
		System.out.println("- 논블로킹 방식: " + nonblockingTime + " ms");
		System.out.println("- 성능 향상: " + String.format("%.1f", (double)blockingTime / nonblockingTime) + "배");

		// 직접 버퍼 vs 간접 버퍼 성능 비교
		System.out.println("\n직접 버퍼 vs 간접 버퍼 성능 비교:");

		// 간접 버퍼
		startTime = System.currentTimeMillis();
		ByteBuffer indirectBuffer = ByteBuffer.allocate(8192);
		for (int i = 0; i < 10000; i++) {
			indirectBuffer.clear();
			indirectBuffer.put("test data".getBytes(StandardCharsets.UTF_8));
			indirectBuffer.flip();
		}
		long indirectTime = System.currentTimeMillis() - startTime;

		// 직접 버퍼
		startTime = System.currentTimeMillis();
		ByteBuffer directBuffer = ByteBuffer.allocateDirect(8192);
		for (int i = 0; i < 10000; i++) {
			directBuffer.clear();
			directBuffer.put("test data".getBytes(StandardCharsets.UTF_8));
			directBuffer.flip();
		}
		long directTime = System.currentTimeMillis() - startTime;

		System.out.println("- 간접 버퍼: " + indirectTime + " ms");
		System.out.println("- 직접 버퍼: " + directTime + " ms");
		System.out.println("- 성능 차이: " + String.format("%.1f", (double)indirectTime / directTime) + "배");

		System.out.println("→ 성능 최적화: 적절한 방식과 버퍼 선택으로 성능 향상\n");
	}

	/**
	 * 예제 5: 버퍼 크기 최적화
	 * 다양한 버퍼 크기로 성능 측정
	 */
	public void example5_BufferSizeOptimization() {
		System.out.println("=== 예제 5: 버퍼 크기 최적화 ===");

		String testFile = FilePathManager.getFilePath("buffer_test.txt");
		createLargeTestFile(testFile, 2 * 1024 * 1024); // 2MB

		int[] bufferSizes = {1024, 4096, 8192, 16384, 32768, 65536};

		System.out.println("버퍼 크기별 성능 측정:");
		System.out.println("버퍼 크기\t소요 시간(ms)\t처리 속도(KB/ms)");
		System.out.println("--------\t------------\t---------------");

		for (int bufferSize : bufferSizes) {
			long startTime = System.currentTimeMillis();

			try (FileInputStream fis = new FileInputStream(testFile);
				 FileOutputStream fos = new FileOutputStream(
					 FilePathManager.getFilePath("buffer_" + bufferSize + ".txt"))) {

				byte[] buffer = new byte[bufferSize];
				int bytesRead;
				long totalBytes = 0;

				while ((bytesRead = fis.read(buffer)) != -1) {
					fos.write(buffer, 0, bytesRead);
					totalBytes += bytesRead;
				}

			} catch (IOException e) {
				System.err.println("버퍼 크기 " + bufferSize + " 실패: " + e.getMessage());
				continue;
			}

			long endTime = System.currentTimeMillis();
			long duration = endTime - startTime;
			double speed = (2 * 1024) / (double)duration; // 2MB / ms

			System.out.printf("%d\t\t%d\t\t%.2f\n", bufferSize, duration, speed);
		}

		System.out.println("\n최적 버퍼 크기 권장사항:");
		System.out.println("- 작은 파일 (< 1MB): 4KB - 8KB");
		System.out.println("- 중간 파일 (1MB - 100MB): 8KB - 32KB");
		System.out.println("- 큰 파일 (> 100MB): 32KB - 64KB");
		System.out.println("- 네트워크 I/O: 8KB - 16KB");

		System.out.println("→ 버퍼 크기: 파일 크기와 시스템에 따라 최적화 필요\n");
	}

	/**
	 * 예제 6: 벤치마킹
	 * 실무 성능 측정 방법
	 */
	public void example6_Benchmarking() {
		System.out.println("=== 예제 6: 벤치마킹 ===");

		String testFile = FilePathManager.getFilePath("benchmark_test.txt");
		createLargeTestFile(testFile, 5 * 1024 * 1024); // 5MB

		System.out.println("I/O 성능 벤치마킹:");

		// 테스트 방법들
		String[] methods = {"전통적 I/O", "Buffered I/O", "NIO Channel", "Memory Mapping", "Zero-Copy"};

		for (String method : methods) {
			System.out.println("\n" + method + " 테스트:");

			long totalTime = 0;
			long totalBytes = 0;
			int iterations = 3;

			for (int i = 0; i < iterations; i++) {
				long startTime = System.nanoTime();

				try {
					switch (method) {
						case "전통적 I/O":
							totalBytes = copyWithTraditionalIO(testFile, "benchmark_traditional.txt");
							break;
						case "Buffered I/O":
							totalBytes = copyWithBufferedIO(testFile, "benchmark_buffered.txt");
							break;
						case "NIO Channel":
							totalBytes = copyWithNIOChannel(testFile, "benchmark_nio.txt");
							break;
						case "Memory Mapping":
							totalBytes = copyWithMemoryMapping(testFile, "benchmark_mmap.txt");
							break;
						case "Zero-Copy":
							totalBytes = copyWithZeroCopy(testFile, "benchmark_zerocopy.txt");
							break;
					}
				} catch (IOException e) {
					System.err.println("벤치마킹 실패: " + e.getMessage());
					continue;
				}

				long endTime = System.nanoTime();
				totalTime += (endTime - startTime);
			}

			double avgTime = totalTime / (double)iterations / 1_000_000; // ms
			double speed = (totalBytes / 1024.0) / avgTime; // KB/ms

			System.out.printf("- 평균 시간: %.2f ms\n", avgTime);
			System.out.printf("- 처리 속도: %.2f KB/ms\n", speed);
			System.out.printf("- 처리량: %.2f MB/s\n", speed);
		}

		System.out.println("\n벤치마킹 결과 분석:");
		System.out.println("- 작은 파일: Buffered I/O가 효율적");
		System.out.println("- 중간 파일: NIO Channel이 효율적");
		System.out.println("- 큰 파일: Memory Mapping이나 Zero-Copy가 효율적");
		System.out.println("- 네트워크: Zero-Copy가 가장 효율적");

		System.out.println("→ 벤치마킹: 실제 환경에서 성능 측정하여 최적 방법 선택\n");
	}

	/**
	 * 예제 7: 실무 활용 - 고성능 파일 처리
	 * 실제 프로젝트에서 사용할 수 있는 고성능 I/O 패턴
	 */
	public void example7_PracticalHighPerformanceIO() {
		System.out.println("=== 예제 7: 실무 활용 - 고성능 파일 처리 ===");

		// 대용량 로그 파일 처리 시뮬레이션
		String logFile = FilePathManager.getFilePath("large_log.txt");
		createLargeLogFile(logFile, 10 * 1024 * 1024); // 10MB

		System.out.println("고성능 로그 파일 처리:");

		// 1. 메모리 맵핑을 사용한 로그 분석
		System.out.println("\n1. 메모리 맵핑을 사용한 로그 분석:");
		analyzeLogWithMemoryMapping(logFile);

		// 2. 스트림을 사용한 병렬 처리
		System.out.println("\n2. 스트림을 사용한 병렬 처리:");
		analyzeLogWithParallelStream(logFile);

		// 3. 비동기 처리를 사용한 로그 모니터링
		System.out.println("\n3. 비동기 처리를 사용한 로그 모니터링:");
		monitorLogAsynchronously(logFile);

		System.out.println("\n실무 고성능 I/O 패턴:");
		System.out.println("- 대용량 파일: Memory Mapping + 병렬 처리");
		System.out.println("- 실시간 처리: 비동기 I/O + 이벤트 기반");
		System.out.println("- 네트워크 I/O: Zero-Copy + 논블로킹");
		System.out.println("- 캐싱: 직접 버퍼 + 메모리 풀링");

		System.out.println("→ 실무 활용: 요구사항에 맞는 최적의 I/O 기법 선택\n");
	}

	// 헬퍼 메서드들

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
			System.err.println("테스트 파일 생성 실패: " + e.getMessage());
		}
	}

	/**
	 * 테스트 파일 생성
	 */
	private void createTestFile(String filePath, String content) {
		try {
			Files.writeString(Paths.get(filePath), content, StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.err.println("테스트 파일 생성 실패: " + e.getMessage());
		}
	}

	/**
	 * 대용량 로그 파일 생성
	 */
	private void createLargeLogFile(String filePath, int size) {
		try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
			int lineCount = size / 100; // 평균 라인 길이 100자

			for (int i = 0; i < lineCount; i++) {
				String level = i % 10 == 0 ? "ERROR" : (i % 3 == 0 ? "WARN" : "INFO");
				writer.printf("[%s] 로그 메시지 %d: 이것은 테스트 로그 데이터입니다. %s\n",
					level, i, "test".repeat(i % 5 + 1));
			}
		} catch (IOException e) {
			System.err.println("로그 파일 생성 실패: " + e.getMessage());
		}
	}

	// 벤치마킹용 복사 메서드들

	private long copyWithTraditionalIO(String source, String target) throws IOException {
		try (FileInputStream fis = new FileInputStream(source);
			 FileOutputStream fos = new FileOutputStream(target)) {

			byte[] buffer = new byte[8192];
			int bytesRead;
			long totalBytes = 0;

			while ((bytesRead = fis.read(buffer)) != -1) {
				fos.write(buffer, 0, bytesRead);
				totalBytes += bytesRead;
			}

			return totalBytes;
		}
	}

	private long copyWithBufferedIO(String source, String target) throws IOException {
		try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(source));
			 BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(target))) {

			byte[] buffer = new byte[8192];
			int bytesRead;
			long totalBytes = 0;

			while ((bytesRead = bis.read(buffer)) != -1) {
				bos.write(buffer, 0, bytesRead);
				totalBytes += bytesRead;
			}

			return totalBytes;
		}
	}

	private long copyWithNIOChannel(String source, String target) throws IOException {
		try (FileChannel sourceChannel = new FileInputStream(source).getChannel();
			 FileChannel targetChannel = new FileOutputStream(target).getChannel()) {

			return sourceChannel.transferTo(0, sourceChannel.size(), targetChannel);
		}
	}

	private long copyWithMemoryMapping(String source, String target) throws IOException {
		try (FileChannel sourceChannel = new FileInputStream(source).getChannel();
			 FileChannel targetChannel = new FileOutputStream(target).getChannel()) {

			MappedByteBuffer sourceBuffer = sourceChannel.map(FileChannel.MapMode.READ_ONLY, 0, sourceChannel.size());
			MappedByteBuffer targetBuffer = targetChannel.map(FileChannel.MapMode.READ_WRITE, 0, sourceChannel.size());

			targetBuffer.put(sourceBuffer);
			targetBuffer.force();

			return sourceChannel.size();
		}
	}

	private long copyWithZeroCopy(String source, String target) throws IOException {
		try (FileChannel sourceChannel = new FileInputStream(source).getChannel();
			 FileChannel targetChannel = new FileOutputStream(target).getChannel()) {

			return sourceChannel.transferTo(0, sourceChannel.size(), targetChannel);
		}
	}

	// 로그 분석 메서드들

	private void analyzeLogWithMemoryMapping(String logFile) {
		try (FileChannel channel = new FileInputStream(logFile).getChannel()) {
			MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());

			int errorCount = 0;
			int warnCount = 0;
			int infoCount = 0;

			// 간단한 패턴 매칭
			for (int i = 0; i < buffer.limit() - 5; i++) {
				if (buffer.get(i) == 'E' && buffer.get(i + 1) == 'R' && buffer.get(i + 2) == 'R'
					&& buffer.get(i + 3) == 'O' && buffer.get(i + 4) == 'R') {
					errorCount++;
				} else if (buffer.get(i) == 'W' && buffer.get(i + 1) == 'A' && buffer.get(i + 2) == 'R'
					&& buffer.get(i + 3) == 'N') {
					warnCount++;
				} else if (buffer.get(i) == 'I' && buffer.get(i + 1) == 'N' && buffer.get(i + 2) == 'F'
					&& buffer.get(i + 3) == 'O') {
					infoCount++;
				}
			}

			System.out.println("- ERROR: " + errorCount + "개");
			System.out.println("- WARN: " + warnCount + "개");
			System.out.println("- INFO: " + infoCount + "개");

		} catch (IOException e) {
			System.err.println("메모리 맵핑 로그 분석 실패: " + e.getMessage());
		}
	}

	private void analyzeLogWithParallelStream(String logFile) {
		try (Stream<String> lines = Files.lines(Paths.get(logFile))) {

			long errorCount = lines.parallel()
				.filter(line -> line.contains("ERROR"))
				.count();

			long warnCount = Files.lines(Paths.get(logFile))
				.parallel()
				.filter(line -> line.contains("WARN"))
				.count();

			long infoCount = Files.lines(Paths.get(logFile))
				.parallel()
				.filter(line -> line.contains("INFO"))
				.count();

			System.out.println("- ERROR: " + errorCount + "개");
			System.out.println("- WARN: " + warnCount + "개");
			System.out.println("- INFO: " + infoCount + "개");

		} catch (IOException e) {
			System.err.println("병렬 스트림 로그 분석 실패: " + e.getMessage());
		}
	}

	private void monitorLogAsynchronously(String logFile) {
		ExecutorService executor = Executors.newFixedThreadPool(2);

		try {
			CompletableFuture<Void> monitorFuture = CompletableFuture.runAsync(() -> {
				try {
					// 간단한 모니터링 시뮬레이션
					Thread.sleep(100);
					System.out.println("- 로그 모니터링 시작됨");
					System.out.println("- 실시간 로그 분석 중...");
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}, executor);

			CompletableFuture<Void> alertFuture = CompletableFuture.runAsync(() -> {
				try {
					Thread.sleep(200);
					System.out.println("- 알림 시스템 활성화됨");
					System.out.println("- 오류 발생 시 즉시 알림");
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}, executor);

			CompletableFuture.allOf(monitorFuture, alertFuture).get();

		} catch (InterruptedException | java.util.concurrent.ExecutionException e) {
			System.err.println("비동기 모니터링 실패: " + e.getMessage());
		} finally {
			executor.shutdown();
		}
	}
} 