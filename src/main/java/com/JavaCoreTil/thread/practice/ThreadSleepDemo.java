package com.JavaCoreTil.thread.practice;

/**
 * Thread.sleep() 동작 원리 데모
 */
public class ThreadSleepDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Thread.sleep() 동작 원리 ===");
        
        // 각 스레드가 독립적으로 sleep 실행
        Thread thread1 = createSleepThread("Worker-1", 2000);
        Thread thread2 = createSleepThread("Worker-2", 1000);
        Thread thread3 = createSleepThread("Worker-3", 0);
        
        thread1.start();
        thread2.start();
        thread3.start();
        
        System.out.println("메인 스레드 계속 실행");
        safeSleep(500);
        System.out.println("메인 스레드 0.5초 후 깨어남");
    }
    
    private static Thread createSleepThread(String name, long sleepTime) {
        return new Thread(() -> {
            System.out.println(name + " 시작");
            if (sleepTime > 0) {
                safeSleep(sleepTime);
            }
            System.out.println(name + " 종료");
        }, name);
    }
    
    private static void safeSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
} 