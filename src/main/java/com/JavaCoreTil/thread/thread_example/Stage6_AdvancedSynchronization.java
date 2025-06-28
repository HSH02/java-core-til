package com.JavaCoreTil.thread.thread_example;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 6단계: 고급 동기화 도구
 * - Lock/Condition, AtomicInteger, CountDownLatch, Semaphore
 */
public class Stage6_AdvancedSynchronization {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 6단계: 고급 동기화 도구 ===\n");
        
        demonstrateReentrantLock();
        demonstrateCondition();
        demonstrateAtomicInteger();
        demonstrateCountDownLatch();
        demonstrateSemaphore();
    }
    
    // ReentrantLock 시연
    static void demonstrateReentrantLock() throws InterruptedException {
        System.out.println("ReentrantLock - 명시적 락 제어");
        
        class LockExample {
            private final ReentrantLock lock = new ReentrantLock();
            private int count = 0;
            
            public void increment() {
                lock.lock(); // 명시적 락 획득
                try {
                    count++;
                    System.out.printf("  %s: count=%d (락 보유 중)%n", 
                                    Thread.currentThread().getName(), count);
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    lock.unlock(); // 반드시 finally에서 해제
                }
            }
            
            public boolean tryIncrementWithTimeout() {
                try {
                    if (lock.tryLock(200, TimeUnit.MILLISECONDS)) { // 타임아웃 시도
                        try {
                            count++;
                            System.out.printf("  %s: 타임아웃 내 락 획득 성공, count=%d%n", 
                                            Thread.currentThread().getName(), count);
                            return true;
                        } finally {
                            lock.unlock();
                        }
                    } else {
                        System.out.printf("  %s: 락 획득 실패 (타임아웃)%n", 
                                        Thread.currentThread().getName());
                        return false;
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }
        }
        
        LockExample example = new LockExample();
        
        Thread[] threads = new Thread[3];
        for (int i = 0; i < 3; i++) {
            threads[i] = new Thread(() -> {
                example.increment();
                example.tryIncrementWithTimeout();
            }, "Worker-" + i);
        }
        
        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();
        System.out.println();
    }
    
    // Condition 시연
    static void demonstrateCondition() throws InterruptedException {
        System.out.println("Condition - 조건별 대기/신호");
        
        class ConditionExample {
            private final ReentrantLock lock = new ReentrantLock();
            private final Condition notEmpty = lock.newCondition(); // 비어있지 않음 조건
            private final Condition notFull = lock.newCondition();  // 가득 차지 않음 조건
            private int[] buffer = new int[3];
            private int count = 0, putIndex = 0, takeIndex = 0;
            
            public void put(int item) throws InterruptedException {
                lock.lock();
                try {
                    while (count == buffer.length) {
                        System.out.printf("  버퍼 가득참, 생산자 대기... (count=%d)%n", count);
                        notFull.await(); // 가득 차지 않을 때까지 대기
                    }
                    
                    buffer[putIndex] = item;
                    putIndex = (putIndex + 1) % buffer.length;
                    count++;
                    System.out.printf("  생산: %d (count=%d)%n", item, count);
                    notEmpty.signal(); // 비어있지 않음을 알림
                } finally {
                    lock.unlock();
                }
            }
            
            public int take() throws InterruptedException {
                lock.lock();
                try {
                    while (count == 0) {
                        System.out.printf("  버퍼 비어있음, 소비자 대기... (count=%d)%n", count);
                        notEmpty.await(); // 비어있지 않을 때까지 대기
                    }
                    
                    int item = buffer[takeIndex];
                    takeIndex = (takeIndex + 1) % buffer.length;
                    count--;
                    System.out.printf("  소비: %d (count=%d)%n", item, count);
                    notFull.signal(); // 가득 차지 않음을 알림
                    return item;
                } finally {
                    lock.unlock();
                }
            }
        }
        
        ConditionExample buffer = new ConditionExample();
        
        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    buffer.put(i);
                    Thread.sleep(300);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Producer");
        
        Thread consumer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    buffer.take();
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Consumer");
        
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
        System.out.println();
    }
    
    // AtomicInteger 시연
    static void demonstrateAtomicInteger() throws InterruptedException {
        System.out.println("AtomicInteger - 원자적 연산");
        
        class AtomicExample {
            private final AtomicInteger atomicCounter = new AtomicInteger(0);
            private int normalCounter = 0;
            
            public void incrementAtomic() {
                int oldValue = atomicCounter.get();
                int newValue = atomicCounter.incrementAndGet(); // 원자적 증가
                System.out.printf("  %s: atomic %d → %d%n", 
                                Thread.currentThread().getName(), oldValue, newValue);
            }
            
            public synchronized void incrementNormal() {
                int oldValue = normalCounter;
                normalCounter++;
                System.out.printf("  %s: normal %d → %d%n", 
                                Thread.currentThread().getName(), oldValue, normalCounter);
            }
            
            public void compareAndSet() {
                int current = atomicCounter.get();
                boolean success = atomicCounter.compareAndSet(current, current + 10);
                System.out.printf("  %s: CAS 시도 %d → %d, 성공: %s%n", 
                                Thread.currentThread().getName(), current, current + 10, success);
            }
        }
        
        AtomicExample example = new AtomicExample();
        
        Thread[] threads = new Thread[3];
        for (int i = 0; i < 3; i++) {
            threads[i] = new Thread(() -> {
                example.incrementAtomic();
                example.incrementNormal();
                example.compareAndSet();
            }, "AtomicWorker-" + i);
        }
        
        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();
        System.out.println();
    }
    
    // CountDownLatch 시연
    static void demonstrateCountDownLatch() throws InterruptedException {
        System.out.println("CountDownLatch - 일회성 동기화");
        
        CountDownLatch startSignal = new CountDownLatch(1); // 시작 신호
        CountDownLatch doneSignal = new CountDownLatch(3);  // 완료 신호
        
        // 작업자 스레드들
        for (int i = 0; i < 3; i++) {
            final int workerId = i + 1;
            new Thread(() -> {
                try {
                    System.out.printf("  Worker-%d: 시작 신호 대기 중%n", workerId);
                    startSignal.await(); // 시작 신호까지 대기
                    
                    System.out.printf("  Worker-%d: 작업 시작%n", workerId);
                    Thread.sleep(1000 + workerId * 500); // 작업 시뮬레이션
                    System.out.printf("  Worker-%d: 작업 완료%n", workerId);
                    
                    doneSignal.countDown(); // 완료 카운트 감소
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "Worker-" + workerId).start();
        }
        
        Thread.sleep(1000);
        System.out.println("  메인: 모든 작업자에게 시작 신호 전송");
        startSignal.countDown(); // 시작 신호 전송
        
        System.out.println("  메인: 모든 작업 완료까지 대기");
        doneSignal.await(); // 모든 작업 완료까지 대기
        System.out.println("  메인: 모든 작업 완료!");
        System.out.println();
    }
    
    // Semaphore 시연
    static void demonstrateSemaphore() throws InterruptedException {
        System.out.println("Semaphore - 리소스 접근 제한");
        
        class SemaphoreExample {
            private final Semaphore semaphore = new Semaphore(2); // 최대 2개 허용
            
            public void useResource(int workerId) {
                try {
                    System.out.printf("  Worker-%d: 리소스 접근 시도%n", workerId);
                    semaphore.acquire(); // 허가 획득
                    
                    System.out.printf("  Worker-%d: 리소스 사용 중 (사용 가능: %d)%n", 
                                    workerId, semaphore.availablePermits());
                    Thread.sleep(2000); // 리소스 사용 시뮬레이션
                    
                    System.out.printf("  Worker-%d: 리소스 사용 완료%n", workerId);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    semaphore.release(); // 허가 반납
                }
            }
        }
        
        SemaphoreExample example = new SemaphoreExample();
        
        Thread[] threads = new Thread[5];
        for (int i = 0; i < 5; i++) {
            final int workerId = i + 1;
            threads[i] = new Thread(() -> example.useResource(workerId), "Worker-" + workerId);
        }
        
        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();
        
        System.out.println("  Semaphore로 동시 접근을 2개로 제한");
        System.out.println();
    }
} 