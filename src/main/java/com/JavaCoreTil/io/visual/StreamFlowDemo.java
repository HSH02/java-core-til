package com.JavaCoreTil.io.visual;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 스트림의 "나룻배" 개념 시연
 * 대용량 파일을 작은 조각(청크)으로 나누어 처리하는 방식을 보여줌
 */
public class StreamFlowDemo {

	public static void main(String[] args) {
		StreamFlowDemo demo = new StreamFlowDemo();

		try {
			// 1. 큰 파일 생성 (시뮬레이션)
			demo.createLargeFile();
			System.out.println();

			// 2. 전통적인 방식 (메모리 부족 위험)
			System.out.println("=== 전통적인 방식 (위험!) ===\n");
			demo.traditionalWay();
			System.out.println();

			// 3. 스트림 방식 (나룻배 개념)
			System.out.println("\n=== 스트림 방식 (나룻배) ===\n");
			demo.streamWay();
			System.out.println();

			// 4. 버퍼 크기별 성능 비교
			System.out.println("\n=== 나룻배 크기별 성능 비교 ===\n");
			demo.compareBoatSizes();
			System.out.println();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 큰 파일 생성 (10MB 시뮬레이션)
	 */
	private void createLargeFile() throws IOException {
		Path largePath = Paths.get("large_file.txt");

		if (!Files.exists(largePath)) {
			System.out.println("🏗️ 10MB 파일 생성 중...");

			try (FileOutputStream fos = new FileOutputStream(largePath.toFile())) {
				// 10MB = 10 * 1024 * 1024 bytes
				byte[] chunk = "이것은 스트림 테스트용 데이터입니다. ".getBytes();
				int totalSize = 10 * 1024 * 1024; // 10MB
				int written = 0;

				while (written < totalSize) {
					fos.write(chunk);
					written += chunk.length;
				}
			}

			System.out.println("✅ 파일 생성 완료: " + Files.size(largePath) + " bytes");
		}
	}

	/**
	 * 전통적인 방식 - 전체 파일을 메모리에 로드 (위험!)
	 */
	private void traditionalWay() throws IOException {
		Path filePath = Paths.get("large_file.txt");
		long fileSize = Files.size(filePath);

		System.out.println("📁 파일 크기: " + fileSize + " bytes");
		System.out.println("⚠️ 전체 파일을 메모리에 로드 시도...");

		long startTime = System.currentTimeMillis();
		long startMemory = getUsedMemory();

		try {
			// 위험한 방식 - 전체 파일을 메모리에 로드
			byte[] allData = Files.readAllBytes(filePath);

			long endTime = System.currentTimeMillis();
			long endMemory = getUsedMemory();

			System.out.println("✅ 로드 완료");
			System.out.println("⏱️ 소요 시간: " + (endTime - startTime) + "ms");
			System.out.println("💾 메모리 사용량: " + (endMemory - startMemory) / 1024 + "KB");
			System.out.println("📊 데이터 크기: " + allData.length + " bytes");

		} catch (OutOfMemoryError e) {
			System.out.println("💥 메모리 부족! 파일이 너무 큽니다.");
		}
	}

	/**
	 * 스트림 방식 - 나룻배로 조금씩 운반
	 */
	private void streamWay() throws IOException {
		Path filePath = Paths.get("large_file.txt");
		long fileSize = Files.size(filePath);

		System.out.println("📁 파일 크기: " + fileSize + " bytes");
		System.out.println("🚤 나룻배(버퍼)로 조금씩 운반 시작...");

		long startTime = System.currentTimeMillis();
		long startMemory = getUsedMemory();

		int boatSize = 8192; // 8KB 나룻배
		byte[] boat = new byte[boatSize]; // 나룻배 준비
		long totalTransported = 0; // 총 운반량
		int tripCount = 0; // 운반 횟수

		try (FileInputStream inputStream = new FileInputStream(filePath.toFile());
			 FileOutputStream outputStream = new FileOutputStream("copied_file.txt")) {

			int bytesInBoat; // 나룻배에 실린 데이터 양

			// 나룻배가 빌 때까지 반복 운반
			while ((bytesInBoat = inputStream.read(boat)) != -1) {
				tripCount++;
				totalTransported += bytesInBoat;

				// 나룻배의 화물을 목적지에 하역
				outputStream.write(boat, 0, bytesInBoat);

				// 진행 상황 출력 (매 1000번째 운반마다)
				if (tripCount % 1000 == 0) {
					double progress = (double)totalTransported / fileSize * 100;
					System.out.printf("🚤 %d번째 운반 완료 - 진행률: %.1f%% (%d/%d bytes)%n",
						tripCount, progress, totalTransported, fileSize);
				}
			}
		}

		long endTime = System.currentTimeMillis();
		long endMemory = getUsedMemory();

		System.out.println("✅ 스트림 복사 완료!");
		System.out.println("🚤 총 운반 횟수: " + tripCount + "번");
		System.out.println("📦 나룻배 크기: " + boatSize + " bytes");
		System.out.println("📊 총 운반량: " + totalTransported + " bytes");
		System.out.println("⏱️ 소요 시간: " + (endTime - startTime) + "ms");
		System.out.println("💾 메모리 사용량: " + (endMemory - startMemory) / 1024 + "KB");

		// 원본과 복사본 크기 비교
		long originalSize = Files.size(Paths.get("large_file.txt"));
		long copiedSize = Files.size(Paths.get("copied_file.txt"));
		System.out.println("🔍 원본 크기: " + originalSize + " bytes");
		System.out.println("🔍 복사본 크기: " + copiedSize + " bytes");
		System.out.println("✅ 데이터 무결성: " + (originalSize == copiedSize ? "OK" : "ERROR"));
	}

	/**
	 * 나룻배 크기별 성능 비교
	 */
	private void compareBoatSizes() throws IOException {
		Path filePath = Paths.get("large_file.txt");
		int[] boatSizes = {512, 1024, 4096, 8192, 16384, 32768}; // 다양한 나룻배 크기

		System.out.println("🚤 나룻배 크기별 성능 테스트");
		System.out.println("┌─────────────┬──────────┬──────────┬─────────────┐");
		System.out.println("│ 크기(bytes) │ 시간(ms) │ 운반횟수  │ 효율성(MB/s)│");
		System.out.println("├─────────────┼──────────┼──────────┼─────────────┤");

		for (int boatSize : boatSizes) {
			long startTime = System.currentTimeMillis();
			int tripCount = 0;

			try (FileInputStream inputStream = new FileInputStream(filePath.toFile());
				 FileOutputStream outputStream = new FileOutputStream("temp_copy.txt")) {

				byte[] boat = new byte[boatSize];
				int bytesInBoat;

				while ((bytesInBoat = inputStream.read(boat)) != -1) {
					tripCount++;
					outputStream.write(boat, 0, bytesInBoat);
				}
			}

			long endTime = System.currentTimeMillis();
			long duration = endTime - startTime;

			// 0으로 나누기 방지
			double efficiency = duration > 0 ?
				(Files.size(filePath) / 1024.0 / 1024.0) / (duration / 1000.0) : 0.0;

			System.out.printf("│ %11d │ %8d │ %8d │ %11.2f │%n",
				boatSize, duration, tripCount, efficiency);

			// 임시 파일 삭제
			Files.deleteIfExists(Paths.get("temp_copy.txt"));
		}

		System.out.println("└─────────────┴──────────┴──────────┴─────────────┘");

		// 분석 결과 출력
		System.out.println("\n📊 성능 분석 결과:");
		System.out.println("• 512B~4KB: 운반 횟수가 많아 오버헤드 발생");
		System.out.println("• 8KB~16KB: 최적의 성능 구간 (OS 페이지 크기와 관련)");
		System.out.println("• 32KB 이상: 메모리 캐시 미스로 성능 저하 가능");
	}

	/**
	 * 현재 사용 중인 메모리 양 계산
	 */
	private long getUsedMemory() {
		Runtime runtime = Runtime.getRuntime();
		return runtime.totalMemory() - runtime.freeMemory();
	}
} 