package com.JavaCoreTil.thread.thread_example;

/**
 * 1단계: Thread 기초 이론
 * - Thread 생명주기, 메모리 구조, 동시성 vs 병렬성
 */
public class Stage1_ThreadBasics {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 1단계: Thread 기초 이론 ===\n");
        
        demonstrateThreadLifecycle();
        demonstrateMemoryStructure();
        demonstrateConcurrencyVsParallelism();
    }
    
    // Thread 생명주기 시연
    static void demonstrateThreadLifecycle() throws InterruptedException {
        System.out.println("Thread 생명주기: NEW → RUNNABLE → TERMINATED");
        
        Thread worker = new Thread(() -> {
            System.out.println("  RUNNABLE: 작업 실행 중");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("  작업 완료");
        }, "WorkerThread");
        
        System.out.println("  NEW: " + worker.getState());
        worker.start();
        System.out.println("  RUNNABLE: " + worker.getState());
        worker.join();
        System.out.println("  TERMINATED: " + worker.getState());
        System.out.println();
    }
    
    // 메모리 구조 시연
    static void demonstrateMemoryStructure() throws InterruptedException {
        System.out.println("메모리 구조: Stack(독립) vs Heap(공유)");
        
        SharedCounter counter = new SharedCounter();
        
        Thread[] threads = new Thread[3];
        for (int i = 0; i < 3; i++) {
            final int threadId = i + 1;
            threads[i] = new Thread(() -> {
                int localVar = threadId * 10; // Stack 영역 (독립)
                System.out.printf("  Thread-%d: 지역변수=%d, 공유변수=%d%n", 
                                threadId, localVar, counter.increment());
            });
        }
        
        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();
        
        System.out.println("  지역변수는 각자 다름, 공유변수는 누적됨");
        System.out.println();
    }
    
    // 동시성 vs 병렬성 시연
    static void demonstrateConcurrencyVsParallelism() {
        System.out.println("동시성(Concurrency) vs 병렬성(Parallelism)");
        System.out.println("  동시성: 논리적으로 동시에 실행되는 것처럼 보임");
        System.out.println("  병렬성: 물리적으로 동시에 실행됨");
        System.out.printf("  현재 시스템 코어 수: %d개%n", 
                         Runtime.getRuntime().availableProcessors());
        System.out.println();
    }
    
    // 공유 카운터 클래스
    static class SharedCounter {
        private int count = 0;
        
        public synchronized int increment() {
            return ++count;
        }
    }
} 