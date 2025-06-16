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
        
        // TODO: 3개 스레드가 unsafeIncrement() 실행
        // 힌트: Thread t1 = new Thread(() -> unsafeIncrement());
        
        long end = System.currentTimeMillis();
        System.out.println("결과: " + counter + " (예상: 3000)");
        System.out.println("시간: " + (end - start) + "ms");
    }
    
    private static void testSafe() throws InterruptedException {
        counter = 0;
        long start = System.currentTimeMillis();
        
        // TODO: 3개 스레드가 safeIncrement() 실행
        
        long end = System.currentTimeMillis();
        System.out.println("결과: " + counter + " (예상: 3000)");
        System.out.println("시간: " + (end - start) + "ms");
    }
    
    private static void testOptimized() throws InterruptedException {
        counter = 0;
        long start = System.currentTimeMillis();
        
        // TODO: 3개 스레드가 optimizedIncrement() 실행
        
        long end = System.currentTimeMillis();
        System.out.println("결과: " + counter + " (예상: 3000)");
        System.out.println("시간: " + (end - start) + "ms");
    }
    
    // TODO 5: 이 메서드들을 구현하세요
    
    private static void unsafeIncrement() {
        // TODO: counter를 1000번 증가 (synchronized 없음)
    }
    
    // TODO: synchronized 키워드 추가
    private static void safeIncrement() {
        // TODO: counter를 1000번 증가 (synchronized method)
    }
    
    private static void optimizedIncrement() {
        // TODO: synchronized 블록만 사용해서 counter를 1000번 증가
        // 힌트: synchronized (Task04_SynchronizedPractice.class) { counter++; }
    }
}
