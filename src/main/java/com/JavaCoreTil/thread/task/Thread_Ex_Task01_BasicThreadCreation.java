package com.JavaCoreTil.thread.task;

/**
 * Task01: 기본 Thread 생성과 실행
 * 
 * 학습 목표:
 * - Thread 생성 방법 이해
 * - run()과 start()의 차이점 체험
 * - Thread 실행 시간 측정
 */
public class Thread_Ex_Task01_BasicThreadCreation {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Thread 생성과 실행 ===");
        
        // 실행 시간 측정 시작
        long startTime = System.currentTimeMillis();
        
        // 카운터 스레드 생성
        Thread counterThread = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                System.out.println("[" + Thread.currentThread().getName() + "] 카운트: " + i);
                safeSleep(1000);
            }
            System.out.println("카운터 작업 완료");
        }, "카운터-스레드");
        
        counterThread.start();
        
        // 메인 스레드 작업 (카운터와 병렬 실행)
        for (int i = 1; i <= 5; i++) {
            System.out.println("[" + Thread.currentThread().getName() + "] 메인 작업: " + i);
            safeSleep(500);
        }
        
        System.out.println("메인 작업 완료");
        
        // run() vs start() 차이 비교
        System.out.println("\n=== run() vs start() 비교 ===");
        
        Thread test1 = new Thread(() -> 
            System.out.println("run() 실행 스레드: " + Thread.currentThread().getName()));
        Thread test2 = new Thread(() -> 
            System.out.println("start() 실행 스레드: " + Thread.currentThread().getName()));
        
        test1.run();    // 메인 스레드에서 직접 실행 (새 스레드 생성 X)
        test2.start();  // 새 스레드에서 실행 (새 스레드 생성 O)
        
        // 모든 스레드 완료까지 대기
        counterThread.join();
        test2.join();
        
        // 총 실행 시간 출력
        long endTime = System.currentTimeMillis();
        System.out.println("\n총 실행 시간: " + (endTime - startTime) + "ms");
        System.out.println("모든 작업 완료");
    }
    
    /**
     * InterruptedException을 안전하게 처리하는 sleep 메서드
     * @param millis 대기할 밀리초
     */
    private static void safeSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // 인터럽트 상태 복원
            Thread.currentThread().interrupt();
            System.out.println("스레드가 중단되었습니다.");
        }
    }
} 