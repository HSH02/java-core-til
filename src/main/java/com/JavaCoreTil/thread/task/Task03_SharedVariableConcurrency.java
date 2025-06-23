package com.JavaCoreTil.thread.task;

/**
 * Task03: 공유 변수 동시성 문제
 */
public class Task03_SharedVariableConcurrency {
    
    private static int sharedCounter = 0;
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 공유 변수 동시성 문제 ===");
        System.out.println("초기값: " + sharedCounter);
        
        // 3개 스레드가 각각 1000번씩 증가
        Thread worker1 = new Thread(() -> incrementCounter("Worker-1", 1000));
        Thread worker2 = new Thread(() -> incrementCounter("Worker-2", 1000));
        Thread worker3 = new Thread(() -> incrementCounter("Worker-3", 1000));
        
        long startTime = System.currentTimeMillis();
        worker1.start();
        worker2.start();
        worker3.start();
        
        worker1.join();
        worker2.join();
        worker3.join();
        long endTime = System.currentTimeMillis();
        
        // 결과 분석
        System.out.println("\n=== 결과 ===");
        System.out.println("예상: 3000, 실제: " + sharedCounter);
        System.out.println("차이: " + (3000 - sharedCounter));
        System.out.println("실행시간: " + (endTime - startTime) + "ms");
        
        if (sharedCounter != 3000) {
            System.out.println("Race Condition 발생!");
        }
        
        // 심화 테스트 - 더 많은 증가
        System.out.println("\n=== 심화 테스트 (10000번씩) ===");
        sharedCounter = 0;
        
        Thread heavy1 = new Thread(() -> incrementCounter("Heavy-1", 10000));
        Thread heavy2 = new Thread(() -> incrementCounter("Heavy-2", 10000));
        Thread heavy3 = new Thread(() -> incrementCounter("Heavy-3", 10000));
        
        startTime = System.currentTimeMillis();
        heavy1.start();
        heavy2.start();
        heavy3.start();
        
        heavy1.join();
        heavy2.join();
        heavy3.join();
        endTime = System.currentTimeMillis();
        
        System.out.println("예상: 30000, 실제: " + sharedCounter);
        System.out.println("차이: " + (30000 - sharedCounter));
        System.out.println("실행시간: " + (endTime - startTime) + "ms");
    }
    
    private static void incrementCounter(String threadName, int iterations) {
        for (int i = 1; i <= iterations; i++) {
            sharedCounter++; // Race Condition 발생 지점
            
            if (i % 1000 == 0) {
                System.out.println("[" + threadName + "] " + i + "번째: " + sharedCounter);
            }
        }
        System.out.println("[" + threadName + "] 완료");
    }
} 