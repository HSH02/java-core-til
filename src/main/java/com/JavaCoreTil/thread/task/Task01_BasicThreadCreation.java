package com.JavaCoreTil.thread.task;

/**
 * Task01: 기본 Thread 생성과 실행
 */
public class Task01_BasicThreadCreation {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Thread 생성과 실행 ===");
        
        // 카운터 스레드 생성
        Thread counterThread = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                System.out.println("[" + Thread.currentThread().getName() + "] 카운트: " + i);
                safeSleep(1000);
            }
            System.out.println("카운터 작업 완료");
        }, "카운터-스레드");
        
        counterThread.start();
        
        // 메인 스레드 작업
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
        
        test1.run();    // 메인 스레드에서 직접 실행
        test2.start();  // 새 스레드에서 실행
        
        counterThread.join();
        test2.join();
        System.out.println("모든 작업 완료");
    }
    
    private static void safeSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
} 