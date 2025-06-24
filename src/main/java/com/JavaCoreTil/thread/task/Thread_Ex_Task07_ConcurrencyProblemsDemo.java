package com.JavaCoreTil.thread.task;

/**
 * Task07: 동시성 문제와 synchronized 데모
 */
public class Thread_Ex_Task07_ConcurrencyProblemsDemo {
    
    private static int globalCounter = 0;
    private static int synchronizedCounter = 0;
    
    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();
    
    public static void main(String[] args) {
        Thread_Ex_Task07_ConcurrencyProblemsDemo demo = new Thread_Ex_Task07_ConcurrencyProblemsDemo();
        
        System.out.println("=== 동시성 문제와 synchronized 데모 ===");
        
        demo.demonstrateRaceCondition();
        demo.demonstrateSynchronization();
        demo.demonstrateLockTypes();
        demo.demonstrateDeadlockPrevention();
    }
    
    /**
     * Race Condition 데모
     */
    private void demonstrateRaceCondition() {
        System.out.println("\n=== Race Condition 데모 ===");
        
        globalCounter = 0;
        
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                globalCounter++;
            }
        }, "Race-Thread-1");
        
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                globalCounter++;
            }
        }, "Race-Thread-2");
        
        try {
            t1.start();
            t2.start();
            t1.join();
            t2.join();
            
            System.out.println("예상값: 20000, 실제값: " + globalCounter);
            System.out.println("매번 실행할 때마다 다른 결과가 나옵니다.");
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * synchronized 데모
     */
    private void demonstrateSynchronization() {
        System.out.println("\n=== synchronized 데모 ===");
        
        synchronizedCounter = 0;
        
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                incrementSynchronized();
            }
        }, "Sync-Thread-1");
        
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                incrementSynchronized();
            }
        }, "Sync-Thread-2");
        
        try {
            long startTime = System.currentTimeMillis();
            t1.start();
            t2.start();
            t1.join();
            t2.join();
            long endTime = System.currentTimeMillis();
            
            System.out.println("예상값: 20000, 실제값: " + synchronizedCounter);
            System.out.println("항상 정확한 결과가 나옵니다.");
            System.out.println("실행 시간: " + (endTime - startTime) + "ms");
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private synchronized void incrementSynchronized() {
        synchronizedCounter++;
    }
    
    /**
     * 인스턴스 락 vs 클래스 락 데모
     */
    private void demonstrateLockTypes() {
        System.out.println("\n=== 인스턴스 락 vs 클래스 락 데모 ===");
        
        Thread_Ex_Task07_ConcurrencyProblemsDemo obj1 = new Thread_Ex_Task07_ConcurrencyProblemsDemo();
        Thread_Ex_Task07_ConcurrencyProblemsDemo obj2 = new Thread_Ex_Task07_ConcurrencyProblemsDemo();
        
        Thread instanceThread1 = new Thread(() -> {
            obj1.instanceMethod("객체1");
        }, "Instance-Thread-1");
        
        Thread instanceThread2 = new Thread(() -> {
            obj2.instanceMethod("객체2");
        }, "Instance-Thread-2");
        
        Thread classThread1 = new Thread(() -> {
            staticMethod("정적메서드1");
        }, "Class-Thread-1");
        
        Thread classThread2 = new Thread(() -> {
            staticMethod("정적메서드2");
        }, "Class-Thread-2");
        
        try {
            System.out.println("인스턴스 락: 서로 다른 객체는 동시 실행 가능");
            instanceThread1.start();
            instanceThread2.start();
            instanceThread1.join();
            instanceThread2.join();
            
            System.out.println("\n클래스 락: 모든 객체가 같은 락 공유");
            classThread1.start();
            classThread2.start();
            classThread1.join();
            classThread2.join();
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private synchronized void instanceMethod(String caller) {
        System.out.println("[" + Thread.currentThread().getName() + "] " + caller + " 시작");
        safeSleep(1000);
        System.out.println("[" + Thread.currentThread().getName() + "] " + caller + " 완료");
    }
    
    private static synchronized void staticMethod(String caller) {
        System.out.println("[" + Thread.currentThread().getName() + "] " + caller + " 시작");
        safeSleep(1000);
        System.out.println("[" + Thread.currentThread().getName() + "] " + caller + " 완료");
    }
    
    /**
     * 데드락 예방 데모
     */
    private void demonstrateDeadlockPrevention() {
        System.out.println("\n=== 데드락 예방 데모 ===");
        
        Thread t1 = new Thread(() -> {
            synchronized(lock1) {
                System.out.println("Thread 1: lock1 획득");
                safeSleep(100);
                
                synchronized(lock2) {
                    System.out.println("Thread 1: lock2도 획득 완료");
                }
                System.out.println("Thread 1: 모든 락 해제");
            }
        }, "Prevention-Thread-1");
        
        Thread t2 = new Thread(() -> {
            synchronized(lock1) {
                System.out.println("Thread 2: lock1 획득");
                safeSleep(100);
                
                synchronized(lock2) {
                    System.out.println("Thread 2: lock2도 획득 완료");
                }
                System.out.println("Thread 2: 모든 락 해제");
            }
        }, "Prevention-Thread-2");
        
        try {
            t1.start();
            t2.start();
            t1.join();
            t2.join();
            
            System.out.println("데드락 예방 성공 - 락 순서 통일");
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private static void safeSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
} 