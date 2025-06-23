package com.JavaCoreTil.thread.task;

/**
 * Thread 생명주기 시각화 데모
 * 
 * Thread의 생명주기를 실시간으로 관찰하고 시각적으로 표현하는 코드
 * NEW → RUNNABLE → BLOCKED/WAITING/TIMED_WAITING → TERMINATED
 */
public class Task05_ThreadLifeCycleVisualization {
    
    private static final Object lock = new Object();
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("\n Thread 생명주기 시각화 데모");
        System.out.println("=".repeat(50));
        
        // 1. NEW 상태 데모
        demonstrateNewState();
        
        // 2. RUNNABLE 상태 데모  
        demonstrateRunnableState();
        
        // 3. TIMED_WAITING 상태 데모
        demonstrateTimedWaitingState();
        
        // 4. BLOCKED 상태 데모
        demonstrateBlockedState();
        
        // 5. WAITING 상태 데모
        demonstrateWaitingState();
        
        // 6. TERMINATED 상태 데모
        demonstrateTerminatedState();
        
        System.out.println("\nThread 생명주기 시각화 완료!");
    }
    
    /**
     * NEW 상태: Thread 객체는 생성되었지만 start()가 호출되지 않은 상태
     */
    private static void demonstrateNewState() throws InterruptedException {
        System.out.println("\n1. NEW 상태 데모");
        System.out.println("Thread 객체 생성 후 start() 호출 전");
        
        Thread newThread = new Thread(() -> {
            System.out.println("   Thread 실행 시작");
            // RUNNABLE 상태를 유지하기 위해 더 긴 작업 수행
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("   Thread 실행 완료");
        }, "NEW-Demo-Thread");
        
        printThreadState("생성 직후", newThread);
        Thread.sleep(300);
        
        System.out.println("   start() 메서드 호출");
        newThread.start();
        Thread.sleep(200); // RUNNABLE 상태 확인을 위한 충분한 대기
        printThreadState("start() 호출 후", newThread);
        newThread.join();
    }
    
    /**
     * RUNNABLE 상태: 실행 중이거나 실행 대기 중인 상태
     */
    private static void demonstrateRunnableState() throws InterruptedException {
        System.out.println("\n2. RUNNABLE 상태 데모");
        System.out.println("CPU를 사용하여 작업 실행 중");
        
        Thread runnableThread = new Thread(() -> {
            System.out.println("   작업 시작");
            // CPU 집약적 작업만 수행 (sleep 없이)
            for (int i = 1; i <= 3; i++) {
                System.out.println("   작업 진행 중... " + i + "/3");
                // 순수 CPU 집약적 작업으로 RUNNABLE 상태 유지
                long sum = 0;
                for (int j = 0; j < 50000000; j++) { // 더 많은 연산
                    sum += j * j;
                }
            }
            System.out.println("   작업 완료");
        }, "RUNNABLE-Demo-Thread");
        
        runnableThread.start();
        Thread.sleep(100); // 실행 중일 때 상태 확인
        printThreadState("CPU 작업 중", runnableThread);
        runnableThread.join();
    }
    
    /**
     * TIMED_WAITING 상태: 지정된 시간 동안 대기하는 상태
     */
    private static void demonstrateTimedWaitingState() throws InterruptedException {
        System.out.println("\n3. TIMED_WAITING 상태 데모");
        System.out.println("Thread.sleep()으로 지정 시간 대기");
        
        Thread timedWaitingThread = new Thread(() -> {
            try {
                System.out.println("   2초간 sleep 시작");
                Thread.sleep(2000);
                System.out.println("   sleep 완료");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "TIMED_WAITING-Demo-Thread");
        
        timedWaitingThread.start();
        Thread.sleep(200); // sleep 중일 때 상태 확인
        printThreadState("sleep() 중", timedWaitingThread);
        timedWaitingThread.join();
    }
    
    /**
     * BLOCKED 상태: synchronized 블록/메서드 진입을 위해 락을 기다리는 상태
     */
    private static void demonstrateBlockedState() throws InterruptedException {
        System.out.println("\n4. BLOCKED 상태 데모");
        System.out.println("synchronized 락 대기 중");
        
        Thread blockingThread = new Thread(() -> {
            synchronized (lock) {
                System.out.println("   첫 번째 스레드가 락 획득");
                try {
                    Thread.sleep(2000); // 락을 오래 보유
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("   첫 번째 스레드가 락 해제");
            }
        }, "BLOCKING-Thread");
        
        Thread blockedThread = new Thread(() -> {
            System.out.println("   두 번째 스레드가 락 대기 시작");
            synchronized (lock) {
                System.out.println("   두 번째 스레드가 락 획득 성공");
            }
        }, "BLOCKED-Demo-Thread");
        
        blockingThread.start();
        Thread.sleep(200);
        blockedThread.start();
        Thread.sleep(500); // blocked 상태일 때 확인
        printThreadState("락 대기 중", blockedThread);
        
        blockingThread.join();
        blockedThread.join();
    }
    
    /**
     * WAITING 상태: 다른 스레드의 특정 작업을 무한정 기다리는 상태
     */
    private static void demonstrateWaitingState() throws InterruptedException {
        System.out.println("\n5. WAITING 상태 데모");
        System.out.println("wait()으로 무한 대기 중");
        
        Thread waitingThread = new Thread(() -> {
            synchronized (lock) {
                try {
                    System.out.println("   wait() 호출 - 무한 대기 시작");
                    lock.wait(); // 다른 스레드가 notify할 때까지 대기
                    System.out.println("   notify 신호 받아서 깨어남");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "WAITING-Demo-Thread");
        
        Thread notifyingThread = new Thread(() -> {
            try {
                Thread.sleep(1500);
                synchronized (lock) {
                    System.out.println("   notify() 신호 전송");
                    lock.notify();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "NOTIFYING-Thread");
        
        waitingThread.start();
        Thread.sleep(500); // wait 상태일 때 확인
        printThreadState("wait() 중", waitingThread);
        
        notifyingThread.start();
        waitingThread.join();
        notifyingThread.join();
    }
    
    /**
     * TERMINATED 상태: 실행이 완료된 상태
     */
    private static void demonstrateTerminatedState() throws InterruptedException {
        System.out.println("\n6. TERMINATED 상태 데모");
        System.out.println("실행 완료 후 종료된 상태");
        
        Thread terminatedThread = new Thread(() -> {
            System.out.println("   작업 완료");
        }, "TERMINATED-Demo-Thread");
        
        terminatedThread.start();
        terminatedThread.join(); // 완료까지 대기
        printThreadState("실행 완료 후", terminatedThread);
    }
    
    /**
     * Thread 상태를 시각적으로 출력하는 헬퍼 메서드
     */
    private static void printThreadState(String description, Thread thread) {
        Thread.State state = thread.getState();
        String stateIcon = getStateIcon(state);
        String stateName = state.name();
        
        System.out.printf("   %s [%s] %s: %s%n", 
            stateIcon, description, thread.getName(), stateName);
    }
    
    /**
     * Thread 상태별 아이콘 반환 - 간단하고 일관된 아이콘 사용
     */
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
} 