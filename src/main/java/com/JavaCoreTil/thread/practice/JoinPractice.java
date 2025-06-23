package com.JavaCoreTil.thread.practice;

/**
 * 📝 실습 과제: join() 메서드 마스터하기 
 * <p>
 * 🎯 학습 목표:
 * - join()의 동작 원리 이해
 * - join() 유무에 따른 실행 순서 차이 체험
 * - join(timeout) 사용법 익히기
 * - 실무에서 join()이 필요한 상황 이해
 */
public class JoinPractice {

	public static void main(String[] args) throws InterruptedException {
		System.out.println("=== join() 실습 과제 ===\n");
		
		joinBehaviorDemo();
		isAliveDemo();
		task1_WithoutJoin();
		task2_WithJoin();
		task3_JoinWithTimeout();
		task4_RealWorldScenario();
	}

	/**
	 * 🔍 join() 동작 원리 데모
	 * <p>
	 * 🎯 목표: 어떤 스레드가 누구를 기다리는지 명확히 이해
	 * <p>
	 * 💡 핵심: A.join()을 호출한 스레드가 A의 완료를 기다림
	 */
	private static void joinBehaviorDemo() throws InterruptedException {
		System.out.println("📍 join() 동작 원리 데모");
		
		Thread[] workers = {
			createWorker("WorkerA", 2000),
			createWorker("WorkerB", 1000),
			createWorker("WorkerC", 3000)
		};
		
		startThreads(workers);
		
		for (Thread worker : workers) {
			worker.join();
			System.out.println("📍 " + worker.getName() + " 완료 확인!");
		}
		
		System.out.println("🎉 모든 작업 완료!\n");
	}

	/**
	 * isAlive() 메서드 데모
	 * <p>
	 * 🎯 목표: isAlive()로 스레드 상태 확인하는 방법 이해
	 * <p>
	 * 💡 핵심: join(timeout) 후 isAlive()로 완료 여부 확인
	 */
	private static void isAliveDemo() throws InterruptedException {
		System.out.println("📍 isAlive() 메서드 데모");
		
		Thread longTask = createWorker("LongTask", 5000);
		
		System.out.println("시작 전: isAlive() = " + longTask.isAlive());
		longTask.start();
		System.out.println("시작 후: isAlive() = " + longTask.isAlive());
		
		longTask.join(2000);
		System.out.println("2초 후: isAlive() = " + longTask.isAlive());
		
		longTask.join();
		System.out.println("완료 후: isAlive() = " + longTask.isAlive() + "\n");
	}

	/**
	 * 📋 과제 1: join() 없이 실행해보기
	 * <p>
	 * 🎯 목표: join() 없이 실행했을 때의 문제점 체험
	 * <p>
	 * ✅ 예상 결과:
	 * - "모든 작업 완료!"가 worker 스레드 작업보다 먼저 출력될 수 있음
	 * - 실행할 때마다 출력 순서가 달라질 수 있음
	 * <p>
	 * 💡 TODO: 아래 코드를 완성하세요
	 */
	private static void task1_WithoutJoin() throws InterruptedException {
		System.out.println("📍 과제 1: join() 없이 실행");
		
		Thread[] workers = createWorkers(3, 3000);
		startThreads(workers);
		
		System.out.println("모든 작업 완료! (join 없음)\n");
	}

	/**
	 * 📋 과제 2: join()으로 순서 보장하기
	 * <p>
	 * 🎯 목표: join()을 사용해서 실행 순서 보장
	 * <p>
	 * ✅ 예상 결과:
	 * - 모든 worker 스레드가 완료된 후 "모든 작업 완료!" 출력
	 * - 매번 실행해도 순서가 일정함
	 * <p>
	 * 💡 TODO: 과제 1과 동일한 코드 + join() 추가
	 */
	private static void task2_WithJoin() throws InterruptedException {
		System.out.println("📍 과제 2: join()으로 순서 보장");
		
		Thread[] workers = createWorkers(3, 3000);
		startThreads(workers);
		joinAll(workers);
		
		System.out.println("모든 작업 완료! (join 사용)\n");
	}

	/**
	 * 📋 과제 3: 타임아웃이 있는 join() 사용
	 * <p>
	 * 🎯 목표: join(timeout) 사용법 익히기
	 * <p>
	 * ✅ 예상 결과:
	 * - 빠른 작업은 정상 완료
	 * - 느린 작업은 타임아웃으로 중단됨
	 * - "타임아웃 발생" 메시지 출력
	 * <p>
	 * 💡 TODO: 빠른 작업과 느린 작업 스레드 만들기
	 */
	private static void task3_JoinWithTimeout() throws InterruptedException {
		System.out.println("📍 과제 3: join(timeout) 사용");
		
		Thread fastWorker = createWorker("빠른작업", 1000);
		Thread slowWorker = createWorker("느린작업", 5000);
		
		fastWorker.start();
		slowWorker.start();
		
		fastWorker.join(2000);
		System.out.println("빠른 작업 join 완료");
		
		slowWorker.join(2000);
		System.out.println(slowWorker.isAlive() ? "느린 작업 타임아웃!" : "느린 작업 정상 완료");
		System.out.println();
	}

	/**
	 * 📋 과제 4: 실무 시나리오 - 데이터 준비 후 처리
	 * <p>
	 * 🎯 목표: 실무에서 join()이 필요한 상황 체험
	 * <p>
	 * ✅ 예상 결과:
	 * - 데이터베이스 연결 완료
	 * - 설정 파일 로드 완료
	 * - 캐시 초기화 완료
	 * - 모든 준비 완료 후 "서버 시작!" 출력
	 * <p>
	 * 💡 TODO: 서버 시작 전 준비 작업들을 시뮬레이션
	 */
	private static void task4_RealWorldScenario() throws InterruptedException {
		System.out.println("📍 과제 4: 실무 시나리오");
		System.out.println("서버 시작 준비 중...");
		
		Thread[] initTasks = {
			createTask("데이터베이스 연결", 2000),
			createTask("설정 파일 로드", 1000),
			createTask("캐시 초기화", 3000)
		};
		
		startThreads(initTasks);
		joinAll(initTasks);
		
		System.out.println("🚀 모든 준비 완료! 서버 시작!\n");
	}

	// 유틸리티 메서드들
	private static Thread createWorker(String name, int sleepMs) {
		return new Thread(() -> {
			System.out.println("  [" + name + "] 시작!");
			sleep(sleepMs);
			System.out.println("  [" + name + "] 완료!");
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

/*
 * 🎓 학습 포인트 정리:
 *
 * 1. join() 없이 실행하면?
 *    → 메인 스레드가 다른 스레드 완료를 기다리지 않음
 *    → 예측 불가능한 실행 순서
 *
 * 2. join() 사용하면?
 *    → 다른 스레드 완료까지 현재 스레드가 대기
 *    → 순서 보장 가능
 *
 * 3. join(timeout) 사용하면?
 *    → 지정된 시간까지만 대기
 *    → 타임아웃 후에는 다음 코드 실행
 *
 * 4. 실무에서 언제 사용하나?
 *    → 서버 초기화 시 모든 준비 작업 완료 대기
 *    → 배치 작업에서 모든 처리 완료 확인
 *    → 테스트에서 비동기 작업 완료 대기
 *
 * 💡 다음 단계: 각 과제를 완성한 후 실행해서 결과를 비교해보세요!
 */ 