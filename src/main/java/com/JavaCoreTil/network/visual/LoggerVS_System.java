package com.JavaCoreTil.network.visual;

import java.util.logging.Logger;

public class LoggerVS_System {
	private static Logger logger = Logger.getLogger(LoggerVS_System.class.getName());

	public static void main(String[] args) {
		System.out.println("=== 공정한 성능 비교 테스트 ===\n");

		// 1단계: System.out 테스트 (동일한 내용 출력)
		System.out.println("1️⃣ System.out 테스트 시작...");
		long startTime = System.currentTimeMillis();

		for (int i = 0; i < 10000; i++) {
			System.out.println("메시지 " + i); // Logger와 동일한 내용
		}

		long systemTime = System.currentTimeMillis() - startTime;
		System.out.println("System.out 완료: " + systemTime + "ms");

		// 2단계: Logger 테스트 (동일한 내용 출력)
		System.out.println("\n2️⃣ Logger 테스트 시작...");
		startTime = System.currentTimeMillis();

		for (int i = 0; i < 10000; i++) {
			logger.info("메시지 " + i); // System.out과 동일한 내용
		}

		long loggerTime = System.currentTimeMillis() - startTime;
		System.out.println("Logger 완료: " + loggerTime + "ms");

		// 3단계: 결과 비교 및 분석
		System.out.println("\n📊 공정한 비교 결과:");
		System.out.println("System.out: " + systemTime + "ms");
		System.out.println("Logger:     " + loggerTime + "ms");
		
		if (loggerTime > systemTime) {
			System.out.println("Logger가 " + (loggerTime - systemTime) + "ms 더 느림");
			System.out.println("이유: 로그 레벨 체크, 포맷팅, 메타데이터 추가 등");
		} else {
			System.out.println("System.out이 " + (systemTime - loggerTime) + "ms 더 느림");
		}
		
		System.out.println("\n💡 결론:");
		System.out.println("- System.out: 단순 출력, 빠름, 운영환경 부적합");
		System.out.println("- Logger: 체계적 관리, 느림, 운영환경 필수");
	}
}
