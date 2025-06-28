package com.JavaCoreTil.thread.thread_example;

/**
 * 3단계: 메모리 가시성 (Memory Visibility)
 * - 메모리 가시성 문제, volatile 키워드, 가시성 vs 원자성
 */
public class Stage3_MemoryVisibility {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 3단계: 메모리 가시성 ===\n");
        
        demonstrateVisibilityProblem();
        demonstrateVolatileSolution();
        demonstrateVisibilityVsAtomicity();
    }
    
    // 메모리 가시성 문제 시연
    static void demonstrateVisibilityProblem() throws InterruptedException {
        System.out.println("메모리 가시성 문제");
        
        class VisibilityProblem {
            private boolean flag = false; // volatile 없음
            
            public void writer() {
                System.out.println("  Writer: flag를 true로 변경");
                flag = true;
            }
            
            public void reader() {
                System.out.println("  Reader: flag 값 확인 시작");
                int count = 0;
                while (!flag && count < 1000000) { // 무한루프 방지
                    count++;
                }
                if (flag) {
                    System.out.println("  Reader: flag 변경 감지됨");
                } else {
                    System.out.println("  Reader: flag 변경 감지 못함 (가시성 문제)");
                }
            }
        }
        
        VisibilityProblem example = new VisibilityProblem();
        
        Thread reader = new Thread(example::reader, "Reader");
        Thread writer = new Thread(example::writer, "Writer");
        
        reader.start();
        Thread.sleep(100);
        writer.start();
        
        reader.join();
        writer.join();
        System.out.println();
    }
    
    // volatile 해결책 시연
    static void demonstrateVolatileSolution() throws InterruptedException {
        System.out.println("volatile 키워드 해결책");
        
        class VolatileSolution {
            private volatile boolean flag = false; // volatile 추가
            
            public void writer() {
                System.out.println("  Writer: volatile flag를 true로 변경");
                flag = true;
            }
            
            public void reader() {
                System.out.println("  Reader: volatile flag 값 확인 시작");
                while (!flag) {
                    // volatile 덕분에 즉시 감지
                }
                System.out.println("  Reader: volatile flag 변경 즉시 감지됨");
            }
        }
        
        VolatileSolution example = new VolatileSolution();
        
        Thread reader = new Thread(example::reader, "Reader");
        Thread writer = new Thread(example::writer, "Writer");
        
        reader.start();
        Thread.sleep(100);
        writer.start();
        
        reader.join();
        writer.join();
        System.out.println();
    }
    
    // 가시성 vs 원자성 차이
    static void demonstrateVisibilityVsAtomicity() throws InterruptedException {
        System.out.println("가시성 vs 원자성");
        
        class VisibilityVsAtomicity {
            private volatile int counter = 0; // volatile: 가시성 보장, 원자성 보장 안함
            
            public void increment() {
                counter++; // 원자적이지 않음: read → modify → write
            }
            
            public int getCounter() {
                return counter; // volatile 덕분에 최신 값 보장
            }
        }
        
        VisibilityVsAtomicity example = new VisibilityVsAtomicity();
        
        Thread[] threads = new Thread[5];
        for (int i = 0; i < 5; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    example.increment();
                }
            }, "Incrementer-" + i);
        }
        
        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();
        
        System.out.printf("  예상 결과: %d%n", 5000);
        System.out.printf("  실제 결과: %d%n", example.getCounter());
        System.out.println("  volatile은 가시성만 보장, 원자성은 보장 안함");
        System.out.println("  원자성이 필요하면 synchronized나 AtomicInteger 사용");
        System.out.println();
    }
} 