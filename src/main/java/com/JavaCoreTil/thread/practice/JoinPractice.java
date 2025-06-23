package com.JavaCoreTil.thread.practice;

/**
 * join() 메서드 실습
 */
public class JoinPractice {

	public static void main(String[] args) throws InterruptedException {
		System.out.println("=== join() 실습 ===");
		
		joinBehaviorDemo();
		isAliveDemo();
		task1_WithoutJoin();
		task2_WithJoin();
		task3_JoinWithTimeout();
		task4_RealWorldScenario();
	}

	// join() 동작 원리 데모
	private static void joinBehaviorDemo() throws InterruptedException {
		System.out.println("\n1. join() 동작 원리");
		
		Thread[] workers = {
			createWorker("WorkerA", 2000),
			createWorker("WorkerB", 1000),
			createWorker("WorkerC", 3000)
		};
		
		startThreads(workers);
		
		for (Thread worker : workers) {
			worker.join();
			System.out.println("   " + worker.getName() + " 완료 확인");
		}
		
		System.out.println("모든 작업 완료");
	}

	// isAlive() 메서드 데모
	private static void isAliveDemo() throws InterruptedException {
		System.out.println("\n2. isAlive() 메서드");
		
		Thread longTask = createWorker("LongTask", 5000);
		
		System.out.println("시작 전: isAlive() = " + longTask.isAlive());
		longTask.start();
		System.out.println("시작 후: isAlive() = " + longTask.isAlive());
		
		longTask.join(2000);
		System.out.println("2초 후: isAlive() = " + longTask.isAlive());
		
		longTask.join();
		System.out.println("완료 후: isAlive() = " + longTask.isAlive());
	}

	// join() 없이 실행 - 순서 보장 안됨
	private static void task1_WithoutJoin() throws InterruptedException {
		System.out.println("\n3. join() 없이 실행");
		
		Thread[] workers = createWorkers(3, 3000);
		startThreads(workers);
		
		System.out.println("모든 작업 완료! (join 없음)");
		System.out.println("// 실제로는 아직 실행 중일 수 있음");
	}

	// join()으로 순서 보장
	private static void task2_WithJoin() throws InterruptedException {
		System.out.println("\n4. join()으로 순서 보장");
		
		Thread[] workers = createWorkers(3, 3000);
		startThreads(workers);
		joinAll(workers);
		
		System.out.println("모든 작업 완료! (join 사용)");
		System.out.println("// 모든 스레드가 실제로 완료됨");
	}

	// 타임아웃이 있는 join()
	private static void task3_JoinWithTimeout() throws InterruptedException {
		System.out.println("\n5. join(timeout) 사용");
		
		Thread fastWorker = createWorker("빠른작업", 1000);
		Thread slowWorker = createWorker("느린작업", 5000);
		
		fastWorker.start();
		slowWorker.start();
		
		fastWorker.join(2000);
		System.out.println("빠른 작업 join 완료");
		
		slowWorker.join(2000);
		System.out.println(slowWorker.isAlive() ? "느린 작업 타임아웃!" : "느린 작업 정상 완료");
	}

	// 실무 시나리오 - 서버 초기화
	private static void task4_RealWorldScenario() throws InterruptedException {
		System.out.println("\n6. 실무 시나리오 - 서버 초기화");
		System.out.println("서버 시작 준비 중...");
		
		Thread[] initTasks = {
			createTask("데이터베이스 연결", 2000),
			createTask("설정 파일 로드", 1000),
			createTask("캐시 초기화", 3000)
		};
		
		startThreads(initTasks);
		joinAll(initTasks);
		
		System.out.println("모든 준비 완료! 서버 시작!");
	}

	// 유틸리티 메서드들
	private static Thread createWorker(String name, int sleepMs) {
		return new Thread(() -> {
			System.out.println("  [" + name + "] 시작");
			sleep(sleepMs);
			System.out.println("  [" + name + "] 완료");
		}, name);
	}

	private static Thread createTask(String taskName, int sleepMs) {
		return new Thread(() -> {
			sleep(sleepMs);
			System.out.println(taskName + " 완료");
		});
	}

	private static Thread[] createWorkers(int count, int sleepMs) {
		Thread[] workers = new Thread[count];
		for (int i = 0; i < count; i++) {
			final int num = i + 1;
			workers[i] = new Thread(() -> {
				sleep(sleepMs);
				System.out.println("Worker-" + num + " 완료");
			});
		}
		return workers;
	}

	private static void startThreads(Thread[] threads) {
		for (Thread thread : threads) {
			thread.start();
		}
	}

	private static void joinAll(Thread[] threads) throws InterruptedException {
		for (Thread thread : threads) {
			thread.join();
		}
	}

	private static void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
} 