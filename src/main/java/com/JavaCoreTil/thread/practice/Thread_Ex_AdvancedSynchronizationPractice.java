package com.JavaCoreTil.thread.practice;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 6단계: 고급 동기화 도구 실습
 * <p>
 * 이 실습에서는 다음 개념들을 직접 구현해보겠습니다:
 * 1. Lock 인터페이스와 ReentrantLock
 * 2. Lock vs synchronized 성능 비교
 * 3. Lock Condition을 활용한 Producer-Consumer
 * 4. AtomicInteger 원자적 연산
 * 5. CountDownLatch 동기화
 * 6. Semaphore 리소스 제한
 */
public class Thread_Ex_AdvancedSynchronizationPractice {

	public static void main(String[] args) throws InterruptedException {
		System.out.println("=== 6단계: 고급 동기화 도구 실습 ===");

		practice1_LockBasics();
		practice2_LockVsSynchronized();
		practice3_LockCondition();
		practice4_AtomicInteger();
		practice5_CountDownLatch();
		practice6_Semaphore();
	}

	// ============================================================================
	// 실습 1: Lock 인터페이스 기본 사용법
	// ============================================================================
	private static void practice1_LockBasics() throws InterruptedException {
		System.out.println("\n=== 실습 1: Lock 인터페이스 기본 사용법 ===");

		class BankAccount {
			private int balance = 1000;
			private final Lock lock = new ReentrantLock();

			// TODO: Lock을 사용해서 안전한 출금 메서드를 구현하세요
			// 힌트: lock.lock()과 lock.unlock()을 try-finally로 감싸세요
			public boolean withdraw(int amount) {
				// 여기에 구현하세요
				try {
					lock.lock();
					if (balance >= amount) {
						balance -= amount;
					} else {
						System.out.println("잔액이 부족합니다");
					}
				} finally {
					lock.unlock();
				}

				return true;
			}

			// TODO: tryLock()을 사용해서 타임아웃이 있는 출금 메서드를 구현하세요
			// 힌트: tryLock(1, TimeUnit.SECONDS) 사용
			public boolean withdrawWithTimeout(int amount) {
				// 여기에 구현하세요
				try {
					if (lock.tryLock(1, TimeUnit.SECONDS))

						try {
							if (balance >= amount) {
								balance -= amount;
							} else {
								System.out.println("잔액이 부족합니다");
							}
						} finally {
							lock.unlock();
						}
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}

				return true;
			} // withdrawWithTimeout

			public int getBalance() {
				lock.lock();
				try {
					return balance;
				} finally {
					lock.unlock();
				}
			}
		}

		BankAccount account = new BankAccount();

		// 테스트: 동시 출금 시도
		Thread[] threads = new Thread[5];
		for (int i = 0; i < 5; i++) {
			final int threadId = i;
			threads[i] = new Thread(() -> {
				boolean success = account.withdraw(300);
				System.out.println("Thread-" + threadId + " 출금 " +
					(success ? "성공" : "실패") + ", 잔액: " + account.getBalance());
			});
		}

		runThreadsAndWait(threads);

		System.out.println("최종 잔액: " + account.getBalance());
		System.out.println("예상 결과: 1~2개 스레드만 성공, 잔액 400원 또는 700원");
	}

	// ============================================================================
	// 실습 2: Lock vs synchronized 성능 비교
	// ============================================================================
	private static void practice2_LockVsSynchronized() throws InterruptedException {
		System.out.println("\n=== 실습 2: Lock vs synchronized 성능 비교 ===");

		class SynchronizedCounter {
			private int count = 0;

			// TODO: synchronized를 사용한 increment 메서드를 구현하세요
			synchronized public void increment() {
				count++;
			}

			public int getCount() {
				return count;
			}
		}

		class LockCounter {
			private int count = 0;
			private final Lock lock = new ReentrantLock();

			// TODO: Lock을 사용한 increment 메서드를 구현하세요
			public void increment() {
				lock.lock();
				count++;
				lock.unlock();
			}

			public int getCount() {
				return count;
			}
		}

		// 성능 테스트
		final int THREAD_COUNT = 10;
		final int INCREMENT_COUNT = 100000;

		// synchronized 테스트
		SynchronizedCounter syncCounter = new SynchronizedCounter();
		long startTime = System.currentTimeMillis();

		Thread[] syncThreads = new Thread[THREAD_COUNT];
		for (int i = 0; i < THREAD_COUNT; i++) {
			syncThreads[i] = new Thread(() -> {
				for (int j = 0; j < INCREMENT_COUNT; j++) {
					syncCounter.increment();
				}
			});
		}

		runThreadsAndWait(syncThreads);
		long syncTime = System.currentTimeMillis() - startTime;

		// Lock 테스트
		LockCounter lockCounter = new LockCounter();
		startTime = System.currentTimeMillis();

		Thread[] lockThreads = new Thread[THREAD_COUNT];
		for (int i = 0; i < THREAD_COUNT; i++) {
			lockThreads[i] = new Thread(() -> {
				for (int j = 0; j < INCREMENT_COUNT; j++) {
					lockCounter.increment();
				}
			});
		}

		runThreadsAndWait(lockThreads);
		long lockTime = System.currentTimeMillis() - startTime;

		System.out.println("synchronized 결과: " + syncCounter.getCount() + ", 시간: " + syncTime + "ms");
		System.out.println("Lock 결과: " + lockCounter.getCount() + ", 시간: " + lockTime + "ms");
		System.out.println("예상 결과: 둘 다 " + (THREAD_COUNT * INCREMENT_COUNT) + ", 성능은 상황에 따라 다름");
	}

	// ============================================================================
	// 실습 3: Lock Condition을 활용한 Producer-Consumer
	// ============================================================================
	private static void practice3_LockCondition() throws InterruptedException {
		System.out.println("\n=== 실습 3: Lock Condition Producer-Consumer ===");

		class Buffer {
			private final int[] buffer = new int[5];
			private int count = 0, in = 0, out = 0;
			private final Lock lock = new ReentrantLock();
			private final Condition notEmpty = lock.newCondition();
			private final Condition notFull = lock.newCondition();

			// TODO: 데이터를 버퍼에 추가하는 put 메서드를 구현하세요
			// 힌트: 버퍼가 가득 찬 경우 notFull.await() 사용
			public void put(int item) throws InterruptedException {
				lock.lock();
				try {
					// 버퍼가 가득 찬 동안 대기
					while (count == buffer.length) {
						notFull.await();
					}
					
					// 데이터 추가
					buffer[in] = item;
					in = (in + 1) % buffer.length; // 순환 버퍼
					count++;
					
					// 소비자에게 알림
					notEmpty.signal();
				} finally {
					lock.unlock();
				}
			}

			// TODO: 버퍼에서 데이터를 가져오는 take 메서드를 구현하세요
			// 힌트: 버퍼가 비어있는 경우 notEmpty.await() 사용
			public int take() throws InterruptedException {
				lock.lock();
				try {
					// 버퍼가 비어있는 동안 대기
					while (count == 0) {
						notEmpty.await();
					}
					
					// 데이터 가져오기
					int item = buffer[out];
					out = (out + 1) % buffer.length; // 순환 버퍼
					count--;
					
					// 생산자에게 알림
					notFull.signal();
					
					return item;
				} finally {
					lock.unlock();
				}
			}
		}

		Buffer buffer = new Buffer();

		// Producer 스레드
		Thread producer = new Thread(() -> {
			try {
				for (int i = 1; i <= 10; i++) {
					buffer.put(i);
					System.out.println("생산: " + i);
					Thread.sleep(100);
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		});

		// Consumer 스레드
		Thread consumer = new Thread(() -> {
			try {
				for (int i = 0; i < 10; i++) {
					int item = buffer.take();
					System.out.println("소비: " + item);
					Thread.sleep(150);
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		});

		producer.start();
		consumer.start();
		producer.join();
		consumer.join();

		System.out.println("예상 결과: 1~10까지 순서대로 생산/소비, 버퍼 크기 5로 제한");
	}

	// ============================================================================
	// 실습 4: AtomicInteger 원자적 연산
	// ============================================================================
	private static void practice4_AtomicInteger() throws InterruptedException {
		System.out.println("\n=== 실습 4: AtomicInteger 원자적 연산 ===");

		class Statistics {
			private AtomicInteger totalVisits = new AtomicInteger(0);
			private AtomicInteger uniqueUsers = new AtomicInteger(0);

			// TODO: 방문수를 원자적으로 증가시키는 메서드를 구현하세요
			// 힌트: incrementAndGet() 또는 addAndGet() 사용
			public void recordVisit() {
				// 여기에 구현하세요
			}

			// TODO: 새로운 사용자를 원자적으로 추가하는 메서드를 구현하세요
			// 힌트: compareAndSet()을 사용해서 중복 방지 로직 구현
			public boolean addUniqueUser(int expectedCount) {
				// 여기에 구현하세요
				return false;
			}

			public int getTotalVisits() {
				return totalVisits.get();
			}

			public int getUniqueUsers() {
				return uniqueUsers.get();
			}
		}

		Statistics stats = new Statistics();

		// 동시 방문 기록
		Thread[] visitThreads = new Thread[10];
		for (int i = 0; i < 10; i++) {
			visitThreads[i] = new Thread(() -> {
				for (int j = 0; j < 1000; j++) {
					stats.recordVisit();
				}
			});
		}

		runThreadsAndWait(visitThreads);

		System.out.println("총 방문수: " + stats.getTotalVisits());
		System.out.println("예상 결과: 정확히 10000번 방문 (동기화 없이도 원자적 보장)");
	}

	// ============================================================================
	// 실습 5: CountDownLatch 동기화
	// ============================================================================
	private static void practice5_CountDownLatch() throws InterruptedException {
		System.out.println("\n=== 실습 5: CountDownLatch 동기화 ===");

		final int WORKER_COUNT = 5;
		CountDownLatch startSignal = new CountDownLatch(1);
		CountDownLatch doneSignal = new CountDownLatch(WORKER_COUNT);

		class Worker implements Runnable {
			private final int workerId;

			public Worker(int workerId) {
				this.workerId = workerId;
			}

			@Override
			public void run() {
				try {
					System.out.println("Worker-" + workerId + " 준비 완료, 시작 신호 대기...");

					// TODO: 시작 신호를 기다리는 코드를 구현하세요
					// 힌트: startSignal.await() 사용

					System.out.println("Worker-" + workerId + " 작업 시작!");

					// 작업 시뮬레이션
					Thread.sleep((int)(Math.random() * 2000) + 1000);

					System.out.println("Worker-" + workerId + " 작업 완료!");

					// TODO: 작업 완료를 알리는 코드를 구현하세요
					// 힌트: doneSignal.countDown() 사용

				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}

		// 워커 스레드들 시작
		for (int i = 0; i < WORKER_COUNT; i++) {
			new Thread(new Worker(i)).start();
		}

		Thread.sleep(1000); // 모든 워커가 준비될 시간

		System.out.println("모든 워커 시작!");
		// TODO: 모든 워커에게 시작 신호를 보내는 코드를 구현하세요
		// 힌트: startSignal.countDown() 사용

		// TODO: 모든 워커의 작업 완료를 기다리는 코드를 구현하세요
		// 힌트: doneSignal.await() 사용

		System.out.println("모든 작업 완료!");
		System.out.println("예상 결과: 모든 워커가 동시에 시작하고, 모든 작업 완료 후 메인 스레드 종료");
	}

	// ============================================================================
	// 실습 6: Semaphore 리소스 제한
	// ============================================================================
	private static void practice6_Semaphore() throws InterruptedException {
		System.out.println("\n=== 실습 6: Semaphore 리소스 제한 ===");

		class DatabaseConnectionPool {
			private final Semaphore semaphore = new Semaphore(3); // 최대 3개 연결

			// TODO: 데이터베이스 연결을 획득하는 메서드를 구현하세요
			// 힌트: semaphore.acquire() 사용
			public void connect(int userId) throws InterruptedException {
				System.out.println("User-" + userId + " 연결 요청...");

				// 여기에 구현하세요 (연결 획득)

				System.out.println("User-" + userId + " 연결 성공! DB 작업 중...");

				// DB 작업 시뮬레이션
				Thread.sleep(2000);

				System.out.println("User-" + userId + " 작업 완료, 연결 해제");

				// TODO: 연결을 해제하는 코드를 구현하세요
				// 힌트: semaphore.release() 사용
			}
		}

		DatabaseConnectionPool pool = new DatabaseConnectionPool();

		// 10명의 사용자가 동시에 DB 접근 시도
		Thread[] users = new Thread[10];
		for (int i = 0; i < 10; i++) {
			final int userId = i;
			users[i] = new Thread(() -> {
				try {
					pool.connect(userId);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			});
		}

		runThreadsAndWait(users);

		System.out.println("예상 결과: 최대 3명씩만 동시 접근, 나머지는 대기 후 순차 처리");
	}

	// ============================================================================
	// 유틸리티 메서드들
	// ============================================================================
	private static void runThreadsAndWait(Thread[] threads) throws InterruptedException {
		for (Thread thread : threads) {
			thread.start();
		}
		for (Thread thread : threads) {
			thread.join();
		}
	}
} 