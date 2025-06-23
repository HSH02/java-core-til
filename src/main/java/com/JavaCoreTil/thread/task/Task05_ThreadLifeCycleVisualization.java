package com.JavaCoreTil.thread.task;

/**
 * Task05: Thread 생명주기 시각화
 */
public class Task05_ThreadLifeCycleVisualization {
    
    private static final Object lock = new Object();
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Thread 생명주기 시각화 ===");
        
        demonstrateNewState();
        demonstrateRunnableState();
        demonstrateTimedWaitingState();
        demonstrateBlockedState();
        demonstrateWaitingState();
        demonstrateTerminatedState();
        
        System.out.println("\n생명주기 시각화 완료");
    }
    
    // NEW 상태 - Thread 생성 후 start() 호출 전
    private static void demonstrateNewState() throws InterruptedException {
        System.out.println("\n1. NEW 상태");
        
        Thread newThread = new Thread(() -> {
            System.out.println("   Thread 실행");
            safeSleep(1000);
        }, "NEW-Thread");
        
        printThreadState("생성 직후", newThread);
        newThread.start();
        safeSleep(200);
        printThreadState("start() 호출 후", newThread);
        newThread.join();
    }
    
    // RUNNABLE 상태 - 실행 중이거나 실행 대기
    private static void demonstrateRunnableState() throws InterruptedException {
        System.out.println("\n2. RUNNABLE 상태");
        
        Thread runnableThread = new Thread(() -> {
            System.out.println("   CPU 작업 시작");
            for (int i = 1; i <= 3; i++) {
                System.out.println("   작업 진행: " + i + "/3");
                // CPU 집약적 작업
                long sum = 0;
                for (int j = 0; j < 50000000; j++) {
                    sum += j * j;
                }
            }
        }, "RUNNABLE-Thread");
        
        runnableThread.start();
        safeSleep(100);
        printThreadState("CPU 작업 중", runnableThread);
        runnableThread.join();
    }
    
    // TIMED_WAITING 상태 - 지정 시간 대기
    private static void demonstrateTimedWaitingState() throws InterruptedException {
        System.out.println("\n3. TIMED_WAITING 상태");
        
        Thread timedWaitingThread = new Thread(() -> {
            System.out.println("   2초 sleep 시작");
            safeSleep(2000);
            System.out.println("   sleep 완료");
        }, "TIMED_WAITING-Thread");
        
        timedWaitingThread.start();
        safeSleep(200);
        printThreadState("sleep() 중", timedWaitingThread);
        timedWaitingThread.join();
    }
    
    // BLOCKED 상태 - synchronized 락 대기
    private static void demonstrateBlockedState() throws InterruptedException {
        System.out.println("\n4. BLOCKED 상태");
        
        Thread blockingThread = new Thread(() -> {
            synchronized (lock) {
                System.out.println("   첫 번째 스레드 락 획득");
                safeSleep(2000);
                System.out.println("   첫 번째 스레드 락 해제");
            }
        }, "BLOCKING-Thread");
        
        Thread blockedThread = new Thread(() -> {
            System.out.println("   두 번째 스레드 락 대기");
            synchronized (lock) {
                System.out.println("   두 번째 스레드 락 획득");
            }
        }, "BLOCKED-Thread");
        
        blockingThread.start();
        safeSleep(200);
        blockedThread.start();
        safeSleep(500);
        printThreadState("락 대기 중", blockedThread);
        
        blockingThread.join();
        blockedThread.join();
    }
    
    // WAITING 상태 - wait() 무한 대기
    private static void demonstrateWaitingState() throws InterruptedException {
        System.out.println("\n5. WAITING 상태");
        
        Thread waitingThread = new Thread(() -> {
            synchronized (lock) {
                try {
                    System.out.println("   wait() 무한 대기");
                    lock.wait();
                    System.out.println("   notify 신호로 깨어남");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "WAITING-Thread");
        
        Thread notifyingThread = new Thread(() -> {
            safeSleep(1500);
            synchronized (lock) {
                System.out.println("   notify() 신호 전송");
                lock.notify();
            }
        }, "NOTIFYING-Thread");
        
        waitingThread.start();
        safeSleep(500);
        printThreadState("wait() 중", waitingThread);
        
        notifyingThread.start();
        waitingThread.join();
        notifyingThread.join();
    }
    
    // TERMINATED 상태 - 실행 완료
    private static void demonstrateTerminatedState() throws InterruptedException {
        System.out.println("\n6. TERMINATED 상태");
        
        Thread terminatedThread = new Thread(() -> {
            System.out.println("   작업 완료");
        }, "TERMINATED-Thread");
        
        terminatedThread.start();
        terminatedThread.join();
        printThreadState("실행 완료 후", terminatedThread);
    }
    
    private static void printThreadState(String description, Thread thread) {
        Thread.State state = thread.getState();
        String icon = getStateIcon(state);
        System.out.println("   " + icon + " " + description + ": " + state);
    }
    
    private static String getStateIcon(Thread.State state) {
        return switch (state) {
            case NEW -> "●";
            case RUNNABLE -> "▶";
            case BLOCKED -> "■";
            case WAITING -> "⏸";
            case TIMED_WAITING -> "⏱";
            case TERMINATED -> "✓";
        };
    }
    
    private static void safeSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
} 