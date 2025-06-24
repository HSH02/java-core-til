package com.JavaCoreTil.thread.task;

/**
 * Task04: synchronized로 동시성 문제 해결
 */
public class Thread_Ex_Task04_SynchronizedPractice {
    
    private static int counter = 0;
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== synchronized 실습 ===");
        
        // Race Condition 재현
        System.out.println("\n1. Race Condition 재현");
        testUnsafe();
        
        // synchronized 메서드로 해결
        System.out.println("\n2. synchronized 메서드");
        testSafe();
        
        // synchronized 블록으로 최적화
        System.out.println("\n3. synchronized 블록");
        testOptimized();
    }
    
    private static void testUnsafe() throws InterruptedException {
        counter = 0;
        long start = System.currentTimeMillis();
        
        Thread t1 = new Thread(() -> unsafeIncrement());
        Thread t2 = new Thread(() -> unsafeIncrement());
        Thread t3 = new Thread(() -> unsafeIncrement());
        
        t1.start(); t2.start(); t3.start();
        t1.join(); t2.join(); t3.join();
        
        long end = System.currentTimeMillis();
        System.out.println("결과: " + counter + " (예상: 3000)");
        System.out.println("시간: " + (end - start) + "ms");
    }
    
    private static void testSafe() throws InterruptedException {
        counter = 0;
        long start = System.currentTimeMillis();
        
        Thread t1 = new Thread(() -> safeIncrement());
        Thread t2 = new Thread(() -> safeIncrement());
        Thread t3 = new Thread(() -> safeIncrement());
        
        t1.start(); t2.start(); t3.start();
        t1.join(); t2.join(); t3.join();
        
        long end = System.currentTimeMillis();
        System.out.println("결과: " + counter + " (예상: 3000)");
        System.out.println("시간: " + (end - start) + "ms");
    }
    
    private static void testOptimized() throws InterruptedException {
        counter = 0;
        long start = System.currentTimeMillis();
        
        Thread t1 = new Thread(() -> optimizedIncrement());
        Thread t2 = new Thread(() -> optimizedIncrement());
        Thread t3 = new Thread(() -> optimizedIncrement());
        
        t1.start(); t2.start(); t3.start();
        t1.join(); t2.join(); t3.join();
        
        long end = System.currentTimeMillis();
        System.out.println("결과: " + counter + " (예상: 3000)");
        System.out.println("시간: " + (end - start) + "ms");
    }
    
    private static void unsafeIncrement() {
        for (int i = 0; i < 1000; i++) {
            counter++; // Race Condition 발생
        }
    }
    
    private static synchronized void safeIncrement() {
        for (int i = 0; i < 1000; i++) {
            counter++; // synchronized 메서드로 보호
        }
    }
    
    private static void optimizedIncrement() {
        for (int i = 0; i < 1000; i++) {
            synchronized (Thread_Ex_Task04_SynchronizedPractice.class) {
                counter++; // synchronized 블록으로 최소 보호
            }
        }
    }
}
