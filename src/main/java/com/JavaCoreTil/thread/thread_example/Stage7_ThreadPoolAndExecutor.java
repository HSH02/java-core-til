package com.JavaCoreTil.thread.thread_example;

import java.util.concurrent.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Stage 7: Thread Pool과 Executor
 * 핵심 개념: Thread Pool의 필요성, ExecutorService, Callable/Future, Fork/Join Framework
 */
public class Stage7_ThreadPoolAndExecutor {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Stage 7: Thread Pool과 Executor ===\n");
        
        // 1. Thread Pool의 필요성 비교
        demonstrateThreadPoolNecessity();
        System.out.println("========================================\n");
        
        // 2. ExecutorService 기본 사용법
        demonstrateExecutorService();
        System.out.println("========================================\n");
        
        // 3. Callable과 Future
        demonstrateCallableAndFuture();
        System.out.println("========================================\n");
        
        // 4. ThreadPoolExecutor 설정과 모니터링
        demonstrateThreadPoolExecutor();
        System.out.println("========================================\n");
        
        // 5. ExecutorService 안전한 종료
        demonstrateExecutorShutdown();
        System.out.println("========================================\n");
        
        // 6. Fork/Join Framework
        demonstrateForkJoinFramework();
        System.out.println("========================================\n");
    }
    
    // 1. Thread Pool의 필요성 - 스레드 생성 비용 비교
    static void demonstrateThreadPoolNecessity() {
        System.out.println("1. Thread Pool의 필요성 비교");
        System.out.println("매번 새 스레드 생성 vs Thread Pool 재사용\n");
        
        // 매번 새 스레드 생성 방식
        System.out.println("방식1: 매번 새 스레드 생성");
        long startTime = System.currentTimeMillis();
        
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            final int taskId = i;
            Thread thread = new Thread(() -> {
                processOrder("ORDER-" + taskId);
            });
            threads.add(thread);
            thread.start();
        }
        
        // 모든 스레드 완료 대기
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        long endTime = System.currentTimeMillis();
        System.out.println("소요시간: " + (endTime - startTime) + "ms");
        System.out.println("생성된 스레드 수: 100개\n");
        
        // Thread Pool 방식
        System.out.println("방식2: Thread Pool 활용");
        startTime = System.currentTimeMillis();
        
        ExecutorService executor = Executors.newFixedThreadPool(10);
        
        for (int i = 0; i < 100; i++) {
            final int taskId = i;
            executor.submit(() -> {
                processOrder("ORDER-" + taskId);
            });
        }
        
        executor.shutdown();
        try {
            executor.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        endTime = System.currentTimeMillis();
        System.out.println("소요시간: " + (endTime - startTime) + "ms");
        System.out.println("사용된 스레드 수: 10개\n");
    }
    
    // 주문 처리 시뮬레이션
    static void processOrder(String orderId) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    // 2. ExecutorService 다양한 타입
    static void demonstrateExecutorService() throws InterruptedException {
        System.out.println("2. ExecutorService 타입별 특징");
        
        // FixedThreadPool - 고정 크기 풀
        System.out.println("FixedThreadPool (고정 크기)");
        ExecutorService fixedPool = Executors.newFixedThreadPool(5);
        
        for (int i = 1; i <= 10; i++) {
            final int taskId = i;
            fixedPool.submit(() -> {
                System.out.println("Fixed Pool Task " + taskId + " by " + Thread.currentThread().getName());
                try { Thread.sleep(1000); } catch (InterruptedException e) { 
                    Thread.currentThread().interrupt(); 
                }
            });
        }
        
        fixedPool.shutdown();
        try {
            fixedPool.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // CachedThreadPool - 필요에 따라 생성
        System.out.println("\nCachedThreadPool (동적 생성)");
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        
        for (int i = 1; i <= 5; i++) {
            final int taskId = i;
            cachedPool.submit(() -> {
                System.out.println("Cached Pool Task " + taskId + " by " + Thread.currentThread().getName());
                try { Thread.sleep(500); } catch (InterruptedException e) { 
                    Thread.currentThread().interrupt(); 
                }
            });
        }
        
        cachedPool.shutdown();
        try {
            cachedPool.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // SingleThreadExecutor - 순차 처리
        System.out.println("\nSingleThreadExecutor (순차 처리)");
        ExecutorService singlePool = Executors.newSingleThreadExecutor();
        
        for (int i = 1; i <= 3; i++) {
            final int taskId = i;
            singlePool.submit(() -> {
                System.out.println("Single Pool Task " + taskId + " by " + 
                    Thread.currentThread().getName());
            });
        }
        
        singlePool.shutdown();
        try {
            singlePool.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    // 3. Callable과 Future - 결과를 반환하는 작업
    static void demonstrateCallableAndFuture() {
        System.out.println("3. Callable과 Future - 결과 반환");
        
        ExecutorService executor = Executors.newFixedThreadPool(3);
        
        // Callable - 결과를 반환하는 작업
        Callable<String> orderProcessTask = () -> {
            Thread.sleep(2000);
            return "주문 처리 완료! 결과: " + Thread.currentThread().getName();
        };
        
        // Future - 미래 결과를 담는 컨테이너
        Future<String> future1 = executor.submit(orderProcessTask);
        Future<String> future2 = executor.submit(() -> {
            Thread.sleep(1000);
            return "빠른 작업 완료!";
        });
        
        System.out.println("작업들을 제출했습니다. 다른 작업을 할 수 있습니다.");
        
        try {
            // 결과 가져오기 (블로킹)
            System.out.println("결과1: " + future1.get(3, TimeUnit.SECONDS));
            System.out.println("결과2: " + future2.get());
            
            // 여러 작업을 한 번에 실행
            List<Callable<Integer>> tasks = Arrays.asList(
                () -> { Thread.sleep(1000); return 100; },
                () -> { Thread.sleep(1500); return 200; },
                () -> { Thread.sleep(800); return 300; }
            );
            
            // 모든 작업 완료까지 대기
            List<Future<Integer>> results = executor.invokeAll(tasks);
            System.out.println("모든 작업 결과:");
            for (Future<Integer> result : results) {
                System.out.println("- " + result.get());
            }
            
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            System.err.println("작업 실행 중 오류: " + e.getMessage());
        }
        
        executor.shutdown();
        System.out.println();
    }
    
    // 4. ThreadPoolExecutor 상세 설정
    static void demonstrateThreadPoolExecutor() throws InterruptedException {
        System.out.println("4. ThreadPoolExecutor 상세 설정과 모니터링");
        
        // 커스텀 ThreadPoolExecutor 생성
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            2,                      // corePoolSize: 기본 스레드 수
            5,                      // maximumPoolSize: 최대 스레드 수
            60L,                    // keepAliveTime: 유휴 스레드 생존 시간
            TimeUnit.SECONDS,       // 시간 단위
            new ArrayBlockingQueue<>(10),  // 작업 대기 큐
            new ThreadPoolExecutor.CallerRunsPolicy()  // 거부 정책
        );
        
        System.out.println("초기 상태:");
        printPoolStatus(executor);
        
        // 작업 제출
        for (int i = 0; i < 15; i++) {
            final int taskId = i;
            executor.submit(() -> {
                System.out.println("Task " + taskId + " 실행 중... by " + 
                    Thread.currentThread().getName());
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            
            if (i % 5 == 4) {
                System.out.println("\n" + (i + 1) + "개 작업 제출 후:");
                printPoolStatus(executor);
                try { Thread.sleep(1000); } catch (InterruptedException e) { 
                    Thread.currentThread().interrupt(); 
                }
            }
        }
        
        executor.shutdown();
        System.out.println("\nThreadPoolExecutor 종료 대기 중...");
        try {
            if (!executor.awaitTermination(20, TimeUnit.SECONDS)) {
                System.out.println("강제 종료 시작");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        System.out.println("ThreadPoolExecutor 완전 종료 완료");
    }
    
    // ThreadPoolExecutor 상태 출력
    static void printPoolStatus(ThreadPoolExecutor executor) {
        System.out.println("- 활성 스레드: " + executor.getActiveCount());
        System.out.println("- 풀 크기: " + executor.getPoolSize());
        System.out.println("- 대기 작업: " + executor.getQueue().size());
        System.out.println("- 완료된 작업: " + executor.getCompletedTaskCount());
    }
    
    // 5. ExecutorService 안전한 종료
    static void demonstrateExecutorShutdown() {
        System.out.println("5. ExecutorService 안전한 종료");
        
        ExecutorService executor = Executors.newFixedThreadPool(3);
        
        // 몇 개의 장시간 작업 제출
        for (int i = 0; i < 5; i++) {
            final int taskId = i;
            executor.submit(() -> {
                try {
                    Thread.sleep(2000);
                    System.out.println("장시간 작업 " + taskId + " 완료");
                } catch (InterruptedException e) {
                    System.out.println("작업 " + taskId + " 중단됨");
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        System.out.println("작업들을 제출했습니다.");
        
        // 우아한 종료 (graceful shutdown)
        System.out.println("shutdown() 호출 - 새 작업은 거부, 기존 작업은 완료");
        executor.shutdown();
        
        try {
            // 5초 대기
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                System.out.println("시간 초과! shutdownNow() 호출 - 강제 종료");
                executor.shutdownNow();
                
                // 추가 대기
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    System.err.println("스레드 풀이 완전히 종료되지 않았습니다.");
                }
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        System.out.println("ExecutorService 종료 완료\n");
    }
    
    // 6. Fork/Join Framework - 분할정복
    static void demonstrateForkJoinFramework() {
        System.out.println("6. Fork/Join Framework - 분할정복");
        
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        
        // 1부터 10000까지의 합을 분할정복으로 계산
        SumTask task = new SumTask(1, 10000);
        
        long startTime = System.currentTimeMillis();
        Long result = forkJoinPool.invoke(task);
        long endTime = System.currentTimeMillis();
        
        System.out.println("Fork/Join 결과: " + result);
        System.out.println("소요 시간: " + (endTime - startTime) + "ms");
        
        // 일반적인 순차 계산과 비교
        startTime = System.currentTimeMillis();
        long sequentialResult = 0;
        for (int i = 1; i <= 10000; i++) {
            sequentialResult += i;
            // 인위적 지연 (계산 복잡도 시뮬레이션)
            if (i % 1000 == 0) {
                try { Thread.sleep(1); } catch (InterruptedException e) { 
                    Thread.currentThread().interrupt(); 
                }
            }
        }
        endTime = System.currentTimeMillis();
        
        System.out.println("순차 처리 결과: " + sequentialResult);
        System.out.println("순차 처리 시간: " + (endTime - startTime) + "ms");
        
        forkJoinPool.shutdown();
    }
    
    // Fork/Join을 위한 RecursiveTask
    static class SumTask extends RecursiveTask<Long> {
        private static final int THRESHOLD = 1000; // 분할 기준
        private final int start;
        private final int end;
        
        public SumTask(int start, int end) {
            this.start = start;
            this.end = end;
        }
        
        @Override
        protected Long compute() {
            int length = end - start + 1;
            
            // 작은 작업이면 직접 계산
            if (length <= THRESHOLD) {
                long sum = 0;
                for (int i = start; i <= end; i++) {
                    sum += i;
                    // 인위적 지연 (계산 복잡도 시뮬레이션)
                    if (i % 100 == 0) {
                        try { Thread.sleep(1); } catch (InterruptedException e) { 
                            Thread.currentThread().interrupt(); 
                        }
                    }
                }
                return sum;
            }
            
            // 큰 작업이면 분할
            int mid = start + length / 2;
            SumTask leftTask = new SumTask(start, mid - 1);
            SumTask rightTask = new SumTask(mid, end);
            
            // 왼쪽 작업을 별도 스레드에서 실행
            leftTask.fork();
            
            // 오른쪽 작업을 현재 스레드에서 실행
            Long rightResult = rightTask.compute();
            
            // 왼쪽 작업 결과 대기
            Long leftResult = leftTask.join();
            
            return leftResult + rightResult;
        }
    }
} 