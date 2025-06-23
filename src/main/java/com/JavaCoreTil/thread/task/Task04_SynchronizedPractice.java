package com.JavaCoreTil.thread.task;

/**
 * 실습 4: synchronized로 Race Condition 해결하기
 * 
 * 학습 목표:
 * - Task03의 Race Condition 문제를 synchronized로 해결
 * - synchronized 메서드 vs 블록 비교  
 * - 성능 차이 체감
 */
public class Task04_SynchronizedPractice {
    
    private static int counter = 0;
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Task04: synchronized 실습 ===");
        
        // TODO 1: 문제 상황 재현
        System.out.println("\n1단계: Race Condition 재현");
        testUnsafe();
        
        // TODO 2: synchronized 메서드로 해결
        System.out.println("\n2단계: synchronized 메서드 해결");
        testSafe();
        
        // TODO 3: synchronized 블록으로 최적화
        System.out.println("\n3단계: synchronized 블록 최적화");
        testOptimized();
    }
    
    // TODO 4: 이 메서드들을 완성하세요
    
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
            counter++; // Race Condition 발생 가능
        }
    }
    
    private static synchronized void safeIncrement() {
        for (int i = 0; i < 1000; i++) {
            counter++; // synchronized 메서드로 보호
        }
    }
    
    private static void optimizedIncrement() {
        for (int i = 0; i < 1000; i++) {
            synchronized (Task04_SynchronizedPractice.class) {
                counter++; // synchronized 블록으로 최소한만 보호
            }
        }
    }
}
