package com.JavaCoreTil.thread.thread_example;

/**
 * 4단계: 동시성 문제와 synchronized
 * - Race Condition, Critical Section, synchronized 동기화, 데드락
 */
public class Stage4_Synchronized {

	public static void main(String[] args) throws InterruptedException {
		System.out.println("=== 4단계: 동시성 문제와 synchronized ===\n");

		demonstrateRaceCondition();
		demonstrateSynchronizedSolution();
		demonstrateInstanceVsClassLock();
		demonstrateDeadlock();

		System.out.println("프로그램 종료");
		System.exit(0); // 모든 스레드 강제 종료 후 프로그램 완전 종료
	}

	// Race Condition 문제 시연
	static void demonstrateRaceCondition() throws InterruptedException {
		System.out.println("Race Condition 문제");

		class UnsafeCounter {
			private int count = 0;

			public void increment() { // 동기화 없음
				count++; // Critical Section (임계 영역)
			}

			public int getCount() {
				return count;
			}
		}

		UnsafeCounter counter = new UnsafeCounter();

		Thread[] threads = new Thread[5];
		for (int i = 0; i < 5; i++) {
			threads[i] = new Thread(() -> {
				for (int j = 0; j < 1000; j++) {
					counter.increment();
				}
			}, "UnsafeWorker-" + i);
		}

		for (Thread t : threads)
			t.start();
		for (Thread t : threads)
			t.join();

		System.out.printf("  예상 결과: %d%n", 5000);
		System.out.printf("  실제 결과: %d%n", counter.getCount());
		System.out.println("  Race Condition으로 인한 데이터 손실");
		System.out.println();
	}

	// synchronized 해결책 시연
	static void demonstrateSynchronizedSolution() throws InterruptedException {
		System.out.println("synchronized 해결책");

		class SafeCounter {
			private int count = 0;

			public synchronized void increment() { // 메서드 동기화
				count++;
			}

			public synchronized int getCount() { // 읽기도 동기화
				return count;
			}

			public void incrementBlock() {
				synchronized (this) { // 블록 동기화
					count++;
				}
			}
		}

		SafeCounter counter = new SafeCounter();

		Thread[] threads = new Thread[5];
		for (int i = 0; i < 5; i++) {
			threads[i] = new Thread(() -> {
				for (int j = 0; j < 1000; j++) {
					counter.increment();
				}
			}, "SafeWorker-" + i);
		}

		for (Thread t : threads)
			t.start();
		for (Thread t : threads)
			t.join();

		System.out.printf("  예상 결과: %d%n", 5000);
		System.out.printf("  실제 결과: %d%n", counter.getCount());
		System.out.println("  synchronized로 Race Condition 해결");
		System.out.println();
	}

	// 인스턴스 락 vs 클래스 락
	static void demonstrateInstanceVsClassLock() throws InterruptedException {
		System.out.println("인스턴스 락 vs 클래스 락");

		class LockExample {
			private static int staticCount = 0;
			private int instanceCount = 0;

			// 인스턴스 락 (객체별로 독립적)
			public synchronized void incrementInstance() {
				instanceCount++;
				System.out.printf("  인스턴스 락: %s, count=%d%n",
					Thread.currentThread().getName(), instanceCount);
			}

			// 클래스 락 (모든 객체가 공유)
			public static synchronized void incrementStatic() {
				staticCount++;
				System.out.printf("  클래스 락: %s, staticCount=%d%n",
					Thread.currentThread().getName(), staticCount);
			}

			public int getInstanceCount() {
				return instanceCount;
			}

			public static int getStaticCount() {
				return staticCount;
			}
		}

		LockExample obj1 = new LockExample();
		LockExample obj2 = new LockExample();

		// 인스턴스 락 테스트 (서로 다른 객체는 동시 실행 가능)
		Thread t1 = new Thread(() -> {
			for (int i = 0; i < 3; i++) {
				obj1.incrementInstance();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
			}
		}, "Thread-1");

		Thread t2 = new Thread(() -> {
			for (int i = 0; i < 3; i++) {
				obj2.incrementInstance(); // 다른 객체라서 동시 실행 가능
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
			}
		}, "Thread-2");

		// 클래스 락 테스트 (모든 스레드가 순차 실행)
		Thread t3 = new Thread(() -> {
			for (int i = 0; i < 3; i++) {
				LockExample.incrementStatic();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
			}
		}, "Thread-3");

		t1.start();
		t2.start();
		t3.start();
		t1.join();
		t2.join();
		t3.join();
		System.out.println();
	}

	// 데드락 시연
	static void demonstrateDeadlock() throws InterruptedException {
		System.out.println("데드락 (Deadlock) 위험");

		class DeadlockExample {
			private final Object lock1 = new Object();
			private final Object lock2 = new Object();

			public void method1() {
				synchronized (lock1) {
					System.out.println("  Thread-A: lock1 획득");
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						System.out.println("  Thread-A: 인터럽트 받음");
						return;
					}

					System.out.println("  Thread-A: lock2 대기 중");
					synchronized (lock2) { // 데드락 위험!
						System.out.println("  Thread-A: lock2 획득");
					}
				}
			}

			public void method2() {
				synchronized (lock2) {
					System.out.println("  Thread-B: lock2 획득");
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						System.out.println("  Thread-B: 인터럽트 받음");
						return;
					}

					System.out.println("  Thread-B: lock1 대기 중");
					synchronized (lock1) { // 데드락 위험!
						System.out.println("  Thread-B: lock1 획득");
					}
				}
			}
		}

		// 데드락 방지를 위한 별도 클래스 (새로운 락 객체 사용)
		class SafeExample {
			private final Object safeLock1 = new Object();
			private final Object safeLock2 = new Object();

			// 데드락 방지: 항상 같은 순서로 락 획득
			public void safeMethod1() {
				synchronized (safeLock1) { // 항상 lock1 먼저
					System.out.println("  Safe Thread-A: safeLock1 획득");
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
					}
					synchronized (safeLock2) { // 그 다음 lock2
						System.out.println("  Safe Thread-A: 모든 락 획득");
					}
				}
			}

			public void safeMethod2() {
				synchronized (safeLock1) { // 항상 lock1 먼저
					System.out.println("  Safe Thread-B: safeLock1 획득");
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
					}
					synchronized (safeLock2) { // 그 다음 lock2
						System.out.println("  Safe Thread-B: 모든 락 획득");
					}
				}
			}
		}

		DeadlockExample deadlockExample = new DeadlockExample();

		System.out.println("  데드락 위험 상황 (3초 후 타임아웃):");
		Thread deadlockA = new Thread(deadlockExample::method1, "Thread-A");
		Thread deadlockB = new Thread(deadlockExample::method2, "Thread-B");

		deadlockA.start();
		deadlockB.start();

		// 3초 후 강제 종료 (데드락 발생 시)
		deadlockA.join(3000);
		deadlockB.join(3000);

		if (deadlockA.isAlive() || deadlockB.isAlive()) {
			System.out.println("  데드락 발생! 강제 종료");

			// 강제 인터럽트
			if (deadlockA.isAlive()) {
				deadlockA.interrupt();
			}
			if (deadlockB.isAlive()) {
				deadlockB.interrupt();
			}

			// 추가 대기 (interrupt 처리 시간)
			Thread.sleep(500);

			// 정리 시간
			Thread.sleep(500);
		}

		System.out.println("  데드락 방지 방법:");
		SafeExample safeExample = new SafeExample(); // 새로운 객체와 락 사용
		Thread safeA = new Thread(safeExample::safeMethod1, "Safe-A");
		Thread safeB = new Thread(safeExample::safeMethod2, "Safe-B");

		safeA.start();
		safeB.start();

		// 안전한 스레드들은 정상 완료되어야 함
		safeA.join(5000);
		safeB.join(5000);

		if (safeA.isAlive() || safeB.isAlive()) {
			System.out.println("  예상치 못한 문제 발생");
			if (safeA.isAlive())
				safeA.interrupt();
			if (safeB.isAlive())
				safeB.interrupt();
		}

		System.out.println();
	}
} 