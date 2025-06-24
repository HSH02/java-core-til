package com.JavaCoreTil.thread.practice;

import java.util.HashMap;
import java.util.Map;

/**
 * 동시성 문제와 synchronized 실습
 */
public class Thread_Ex_ConcurrencyPractice {

	public static void main(String[] args) throws InterruptedException {
		System.out.println("=== 동시성 문제와 synchronized 실습 ===");
		practice1_RaceCondition();
		practice2_Synchronized();
		practice3_LockTypes();
		practice4_DeadlockPrevention();
		practice5_RealWorldScenario();
	}

	// 실습 1: Race Condition 문제
	private static void practice1_RaceCondition() throws InterruptedException {
		System.out.println("\n=== Race Condition 문제 ===");

		class BankAccount {
			private int balance = 0;

			public void deposit(int amount) {
				balance += amount;
			}

			public void withdraw(int amount) {
				balance -= amount;
			}

			public int getBalance() {
				return balance;
			}
		}

		BankAccount account = new BankAccount();
		Thread[] threads = new Thread[10];

		// 입금 5개, 출금 5개 스레드
		for (int i = 0; i < 5; i++) {
			threads[i] = new Thread(() -> {
				for (int j = 0; j < 1000; j++)
					account.deposit(100);
			});
			threads[i + 5] = new Thread(() -> {
				for (int j = 0; j < 1000; j++)
					account.withdraw(100);
			});
		}

		runThreads(threads);
		System.out.println("예상: 0원, 실제: " + account.getBalance() + "원");
	}

	// 실습 2: synchronized 해결
	private static void practice2_Synchronized() throws InterruptedException {
		System.out.println("\n=== synchronized 해결 ===");

		class SafeBankAccount {
			private int balance = 0;

			public synchronized void deposit(int amount) {
				balance += amount;
			}

			public synchronized void withdraw(int amount) {
				balance -= amount;
			}

			public int getBalance() {
				return balance;
			}
		}

		SafeBankAccount account = new SafeBankAccount();
		Thread[] threads = new Thread[10];

		for (int i = 0; i < 5; i++) {
			threads[i] = new Thread(() -> {
				for (int j = 0; j < 1000; j++)
					account.deposit(100);
			});
			threads[i + 5] = new Thread(() -> {
				for (int j = 0; j < 1000; j++)
					account.withdraw(100);
			});
		}

		runThreads(threads);
		System.out.println("예상: 0원, 실제: " + account.getBalance() + "원");
	}

	// 실습 3: 인스턴스 vs 클래스 락
	private static void practice3_LockTypes() throws InterruptedException {
		System.out.println("\n=== 인스턴스 vs 클래스 락 ===");

		class Counter {
			public synchronized void instanceMethod() {
				System.out.println(Thread.currentThread().getName() + " 인스턴스 시작");
				safeSleep(2000);
				System.out.println(Thread.currentThread().getName() + " 인스턴스 완료");
			}

			public static synchronized void staticMethod() {
				System.out.println(Thread.currentThread().getName() + " 클래스 시작");
				safeSleep(2000);
				System.out.println(Thread.currentThread().getName() + " 클래스 완료");
			}
		}

		Counter c1 = new Counter(), c2 = new Counter();

		// 인스턴스 락 테스트 (동시 실행)
		long start = System.currentTimeMillis();
		Thread[] instanceThreads = {
			new Thread(c1::instanceMethod, "Instance-1"),
			new Thread(c2::instanceMethod, "Instance-2")
		};
		runThreads(instanceThreads);
		System.out.println("인스턴스 락: " + (System.currentTimeMillis() - start) + "ms");

		// 클래스 락 테스트 (순차 실행)
		start = System.currentTimeMillis();
		Thread[] staticThreads = {
			new Thread(Counter::staticMethod, "Static-1"),
			new Thread(Counter::staticMethod, "Static-2")
		};
		runThreads(staticThreads);
		System.out.println("클래스 락: " + (System.currentTimeMillis() - start) + "ms");
	}

	// 실습 4: 데드락 예방
	private static void practice4_DeadlockPrevention() throws InterruptedException {
		System.out.println("\n=== 데드락 예방 ===");

		final Object lockA = new Object(), lockB = new Object();

		// 락 순서 통일로 데드락 예방
		Thread[] threads = {
			new Thread(() -> {
				synchronized (lockA) {
					System.out.println("Thread1: lockA 획득");
					safeSleep(100);
					synchronized (lockB) {
						System.out.println("Thread1: lockB 획득");
					}
				}
			}),
			new Thread(() -> {
				synchronized (lockA) { // 같은 순서로 통일
					System.out.println("Thread2: lockA 획득");
					safeSleep(100);
					synchronized (lockB) {
						System.out.println("Thread2: lockB 획득");
					}
				}
			})
		};

		runThreads(threads);
		System.out.println("데드락 예방 성공!");
	}

	// 실습 5: 실무 시나리오
	private static void practice5_RealWorldScenario() throws InterruptedException {
		System.out.println("\n=== 재고 관리 시나리오 ===");

		class Inventory {
			private Map<String, Integer> stock = new HashMap<>();

			public void addProduct(String name, int amount) {
				stock.put(name, amount);
			}

			public synchronized void addStock(String name, int amount) {
				stock.put(name, stock.get(name) + amount);
			}

			public synchronized boolean sell(String name, int amount) {
				int current = stock.get(name);
				if (current >= amount) {
					stock.put(name, current - amount);
					return true;
				}
				return false;
			}

			public int getStock(String name) {
				return stock.getOrDefault(name, 0);
			}
		}

		Inventory inventory = new Inventory();
		inventory.addProduct("노트북", 100);

		Thread[] threads = new Thread[8];

		// 판매 스레드 5개 (각 15개씩, 총 75개)
		for (int i = 0; i < 5; i++) {
			threads[i] = new Thread(() -> inventory.sell("노트북", 15));
		}

		// 입고 스레드 3개 (각 10개씩, 총 30개)
		for (int i = 5; i < 8; i++) {
			threads[i] = new Thread(() -> inventory.addStock("노트북", 10));
		}

		runThreads(threads);

		int finalStock = inventory.getStock("노트북");
		System.out.println("초기: 100개, 판매: 75개, 입고: 30개");
		System.out.println("예상: 55개, 실제: " + finalStock + "개");
		System.out.println(finalStock == 55 ? "성공!" : "Race Condition 발생!");
	}

	// 유틸리티 메서드
	private static void runThreads(Thread[] threads) throws InterruptedException {
		for (Thread t : threads)
			t.start();
		for (Thread t : threads)
			t.join();
	}

	private static void safeSleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}