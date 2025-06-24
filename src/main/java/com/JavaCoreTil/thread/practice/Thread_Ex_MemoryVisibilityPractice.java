package com.JavaCoreTil.thread.practice;

/**
 * 메모리 가시성 실습
 */
public class Thread_Ex_MemoryVisibilityPractice {

	public static void main(String[] args) {
		System.out.println("=== 메모리 가시성 실습 ===");

		practice1_VisibilityProblem();
		practice2_VolatileSolution();
		practice3_VisibilityVsAtomicity();
		practice4_VolatilePatterns();
	}

	// 실습 1: 메모리 가시성 문제 재현
	private static void practice1_VisibilityProblem() {
		System.out.println("\n1. 메모리 가시성 문제");

		class VisibilityTest {
			private boolean flag = false;

			public void writer() {
				safeSleep(1000);
				flag = true;
				System.out.println("Writer: flag를 true로 변경");
			}

			public void reader() {
				while (!flag) {
					// 빈 루프
				}
				System.out.println("Reader: 종료");
			}
		}

		VisibilityTest test = new VisibilityTest();

		Thread writer = new Thread(test::writer);
		Thread reader = new Thread(test::reader);
		
		writer.start();
		reader.start();

		safeSleep(3000);
		System.out.println("타임아웃 - 강제 종료");
		System.out.println("// Reader가 무한 루프에 빠질 수 있음");
	}

	// 실습 2: volatile로 가시성 해결
	private static void practice2_VolatileSolution() {
		System.out.println("\n2. volatile로 가시성 해결");

		class VolatileTest {
			private volatile boolean flag = false;

			public void writer() {
				safeSleep(1000);
				flag = true;
				System.out.println("Writer: flag를 true로 변경");
			}

			public void reader() {
				while (!flag) {
					// 빈 루프
				}
				System.out.println("Reader: 종료");
			}
		}

		VolatileTest test = new VolatileTest();

		Thread writer = new Thread(test::writer);
		Thread reader = new Thread(test::reader);
		
		writer.start();
		reader.start();

		safeSleep(3000);
		System.out.println("타임아웃 - 정상 종료");
		System.out.println("// volatile로 즉시 종료됨");
	}

	// 실습 3: 가시성 vs 원자성
	private static void practice3_VisibilityVsAtomicity() {
		System.out.println("\n3. 가시성 vs 원자성");

		class AtomicityTest {
			private volatile int counter = 0;

			public void increment() {
				for (int i = 0; i < 1000; i++) {
					counter++;
				}
			}

			public int getCounter() {
				return counter;
			}
		}

		AtomicityTest test = new AtomicityTest();

		Thread[] threads = new Thread[5];
		for (int i = 0; i < 5; i++) {
			threads[i] = new Thread(test::increment);
			threads[i].start();
		}

		// 모든 스레드 완료 대기
		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

		System.out.println("예상: 5000, 실제: " + test.getCounter());
		System.out.println("// volatile은 원자성을 보장하지 않음");
	}

	// 실습 4: volatile 활용 패턴
	private static void practice4_VolatilePatterns() {
		System.out.println("\n4. volatile 활용 패턴");

		// 패턴 1: 플래그 제어
		class FlagPattern {
			private volatile boolean running = false;

			public void start() {
				running = true;
			}

			public void stop() {
				running = false;
			}
		}

		// 패턴 2: 상태 관리
		class StatusPattern {
			private volatile String status = "INIT";

			public void updateStatus(String newStatus) {
				status = newStatus;
			}

			public String getStatus() {
				return status;
			}
		}

		// 패턴 테스트
		FlagPattern flagTest = new FlagPattern();
		flagTest.start();

		Thread worker = new Thread(() -> {
			while (flagTest.running) {
				System.out.println("작업 중...");
				safeSleep(500);
			}
			System.out.println("작업 종료");
		});

		worker.start();
		safeSleep(2000);
		flagTest.stop();
		System.out.println("정지 신호 전송");

		StatusPattern statusTest = new StatusPattern();
		System.out.println("초기 상태: " + statusTest.getStatus());
		statusTest.updateStatus("RUNNING");
		System.out.println("변경 상태: " + statusTest.getStatus());
		statusTest.updateStatus("COMPLETED");
		System.out.println("최종 상태: " + statusTest.getStatus());
	}

	private static void safeSleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
} 