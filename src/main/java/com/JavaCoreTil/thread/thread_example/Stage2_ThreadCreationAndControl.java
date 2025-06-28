package com.JavaCoreTil.thread.thread_example;

/**
 * 2단계: Thread 생성과 제어
 * - Thread 생성 방법, 제어 메서드, 속성 관리, 안전한 종료
 */
public class Stage2_ThreadCreationAndControl {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 2단계: Thread 생성과 제어 ===\n");
        
        demonstrateThreadCreation();
        demonstrateThreadControl();
        demonstrateThreadProperties();
        demonstrateSafeTermination();
    }
    
    // Thread 생성 방법들
    static void demonstrateThreadCreation() throws InterruptedException {
        System.out.println("Thread 생성 방법");
        
        // 1. Runnable 인터페이스 (권장)
        Thread runnableThread = new Thread(() -> {
            System.out.println("  Runnable 방식으로 생성된 스레드");
        }, "RunnableThread");
        
        // 2. Thread 클래스 상속
        Thread extendedThread = new MyThread();
        
        // 3. 익명 클래스
        Thread anonymousThread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("  익명 클래스로 생성된 스레드");
            }
        }, "AnonymousThread");
        
        runnableThread.start();
        extendedThread.start();
        anonymousThread.start();
        
        runnableThread.join();
        extendedThread.join();
        anonymousThread.join();
        System.out.println();
    }
    
    // Thread 제어 메서드들
    static void demonstrateThreadControl() throws InterruptedException {
        System.out.println("Thread 제어 메서드");
        
        Thread worker = new Thread(() -> {
            System.out.println("  작업 시작");
            try {
                Thread.sleep(1000);
                System.out.println("  작업 완료");
            } catch (InterruptedException e) {
                System.out.println("  작업 중단됨");
                Thread.currentThread().interrupt();
            }
        }, "ControlWorker");
        
        System.out.println("  start() - 스레드 시작");
        worker.start();
        
        System.out.println("  join() - 완료까지 대기");
        worker.join();
        
        System.out.println("  interrupt() 예제");
        Thread interruptibleWorker = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println("  인터럽트 신호 받음");
            }
        });
        
        interruptibleWorker.start();
        Thread.sleep(500);
        interruptibleWorker.interrupt();
        interruptibleWorker.join();
        System.out.println();
    }
    
    // Thread 속성 관리
    static void demonstrateThreadProperties() throws InterruptedException {
        System.out.println("Thread 속성 관리");
        
        // 이름 설정
        Thread namedThread = new Thread(() -> {
            System.out.printf("  스레드 이름: %s%n", Thread.currentThread().getName());
        }, "CustomNameThread");
        
        // 우선순위 설정
        Thread highPriorityThread = new Thread(() -> {
            System.out.printf("  우선순위: %d%n", Thread.currentThread().getPriority());
        }, "HighPriorityThread");
        highPriorityThread.setPriority(Thread.MAX_PRIORITY);
        
        // 데몬 스레드 설정
        Thread daemonThread = new Thread(() -> {
            System.out.printf("  데몬 스레드: %s%n", Thread.currentThread().isDaemon());
        }, "DaemonThread");
        daemonThread.setDaemon(true);
        
        namedThread.start();
        highPriorityThread.start();
        daemonThread.start();
        
        namedThread.join();
        highPriorityThread.join();
        daemonThread.join();
        System.out.println();
    }
    
    // 안전한 종료 패턴
    static void demonstrateSafeTermination() throws InterruptedException {
        System.out.println("안전한 Thread 종료");
        
        SafeWorker worker = new SafeWorker();
        Thread workerThread = new Thread(worker, "SafeWorker");
        
        workerThread.start();
        System.out.println("  작업 시작");
        
        Thread.sleep(1000);
        
        System.out.println("  안전한 종료 요청");
        worker.stop();
        
        workerThread.join();
        System.out.println("  안전하게 종료됨");
        System.out.println();
    }
    
    // Thread 상속 예제
    static class MyThread extends Thread {
        public MyThread() {
            super("ExtendedThread");
        }
        
        @Override
        public void run() {
            System.out.println("  Thread 상속으로 생성된 스레드");
        }
    }
    
    // 안전한 종료를 위한 Worker 클래스
    static class SafeWorker implements Runnable {
        private volatile boolean running = true;
        
        @Override
        public void run() {
            while (running && !Thread.currentThread().isInterrupted()) {
                try {
                    System.out.println("  작업 수행 중");
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    System.out.println("  인터럽트 받음, 정리 중");
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            System.out.println("  리소스 정리 완료");
        }
        
        public void stop() {
            running = false;
        }
    }
} 