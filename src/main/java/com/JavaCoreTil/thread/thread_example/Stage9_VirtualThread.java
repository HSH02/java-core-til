package com.JavaCoreTil.thread.thread_example;

import java.time.Duration;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.List;
import java.util.ArrayList;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

/**
 * Stage 9: Virtual Thread (Java 21+) - 현실적이고 신뢰성 있는 성능 분석
 * 핵심 개념: Virtual Thread의 실제 성능 특성과 적절한 사용 사례
 */
public class Stage9_VirtualThread {
    
    private static final MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
    
    public static void main(String[] args) throws Exception {
        System.out.println("=== Virtual Thread 현실적 성능 분석 ===\n");
        System.out.println("Java Version: " + System.getProperty("java.version"));
        System.out.println("Available Processors: " + Runtime.getRuntime().availableProcessors());
        System.out.println();
        
        // 1. 메모리 사용량 정확한 비교
        compareMemoryUsage();
        System.out.println("========================================\n");
        
        // 2. I/O vs CPU 작업 성능 비교
        compareWorkloadTypes();
        System.out.println("========================================\n");
        
        // 3. 처리량(Throughput) 비교
        compareThroughput();
        System.out.println("========================================\n");
        
        // 4. Virtual Thread의 한계 시연
        demonstrateLimitations();
        System.out.println("========================================\n");
        
        // 5. 실무 권장사항
        showBestPractices();
    }
    
    // 1. 메모리 사용량 정확한 비교
    static void compareMemoryUsage() throws InterruptedException {
        System.out.println("1. 메모리 사용량 비교 (정확한 측정)");
        
        // 기준 메모리 측정
        forceGC();
        long baseMemory = getUsedMemory();
        System.out.println("기준 메모리: " + formatMemory(baseMemory));
        
        // Platform Thread 메모리 테스트 (500개로 제한)
        System.out.println("\n500개 Platform Thread 메모리 테스트:");
        List<Thread> platformThreads = new ArrayList<>();
        CountDownLatch startLatch = new CountDownLatch(500);
        CountDownLatch endLatch = new CountDownLatch(500);
        
        long beforePlatform = getUsedMemory();
        
        for (int i = 0; i < 500; i++) {
            Thread t = new Thread(() -> {
                startLatch.countDown();
                try {
                    endLatch.await(); // 모든 쓰레드가 종료 신호를 기다림
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            platformThreads.add(t);
            t.start();
        }
        
        startLatch.await(); // 모든 쓰레드가 시작될 때까지 대기
        Thread.sleep(1000); // 메모리 안정화
        
        long afterPlatform = getUsedMemory();
        long platformMemory = afterPlatform - beforePlatform;
        
        System.out.println("  Platform Thread 메모리 증가: " + formatMemory(platformMemory));
        System.out.println("  Thread당 평균 메모리: " + formatMemory(platformMemory / 500));
        
        // Platform Thread 정리
        for (Thread t : platformThreads) {
            endLatch.countDown();
        }
        for (Thread t : platformThreads) {
            t.join();
        }
        platformThreads.clear();
        
        forceGC();
        
        // Virtual Thread 메모리 테스트 (5000개)
        System.out.println("\n5000개 Virtual Thread 메모리 테스트:");
        CountDownLatch virtualStartLatch = new CountDownLatch(5000);
        CountDownLatch virtualEndLatch = new CountDownLatch(1);
        
        long beforeVirtual = getUsedMemory();
        
        for (int i = 0; i < 5000; i++) {
            Thread.startVirtualThread(() -> {
                virtualStartLatch.countDown();
                try {
                    virtualEndLatch.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        virtualStartLatch.await();
        Thread.sleep(1000);
        
        long afterVirtual = getUsedMemory();
        long virtualMemory = afterVirtual - beforeVirtual;
        
        System.out.println("  Virtual Thread 메모리 증가: " + formatMemory(virtualMemory));
        System.out.println("  Thread당 평균 메모리: " + formatMemory(virtualMemory / 5000));
        
        virtualEndLatch.countDown();
        Thread.sleep(1000);
        
        // 결과 비교
        double efficiency = (double)(platformMemory / 500) / (virtualMemory / 5000);
        System.out.println("\n메모리 효율성: Virtual Thread가 " + 
            String.format("%.1fx 더 효율적", efficiency));
    }
    
    // 2. I/O vs CPU 작업 성능 비교
    static void compareWorkloadTypes() throws InterruptedException {
        System.out.println("2. I/O 집약적 vs CPU 집약적 작업 비교");
        
        int taskCount = 200;
        
        // I/O 집약적 작업 비교
        System.out.println("\nI/O 집약적 작업 (200개 작업, 각 200ms 대기):");
        
        // Platform Thread Pool
        long startTime = System.nanoTime();
        ExecutorService platformExecutor = Executors.newFixedThreadPool(20);
        CountDownLatch platformLatch = new CountDownLatch(taskCount);
        
        for (int i = 0; i < taskCount; i++) {
            platformExecutor.submit(() -> {
                try {
                    Thread.sleep(200); // I/O 대기 시뮬레이션
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    platformLatch.countDown();
                }
            });
        }
        
        platformLatch.await();
        platformExecutor.shutdown();
        long platformIOTime = System.nanoTime() - startTime;
        
        // Virtual Thread
        startTime = System.nanoTime();
        CountDownLatch virtualLatch = new CountDownLatch(taskCount);
        
        for (int i = 0; i < taskCount; i++) {
            Thread.startVirtualThread(() -> {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    virtualLatch.countDown();
                }
            });
        }
        
        virtualLatch.await();
        long virtualIOTime = System.nanoTime() - startTime;
        
        System.out.println("  Platform Thread (20개 풀): " + formatTime(platformIOTime));
        System.out.println("  Virtual Thread: " + formatTime(virtualIOTime));
        System.out.println("  I/O 작업 개선: " + 
            String.format("%.1fx 빠름", (double)platformIOTime / virtualIOTime));
        
        // CPU 집약적 작업 비교
        System.out.println("\nCPU 집약적 작업 (50개 작업, 각 수학 연산):");
        int cpuTaskCount = 50;
        
        // Platform Thread Pool (코어 수만큼)
        startTime = System.nanoTime();
        ExecutorService cpuExecutor = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors());
        CountDownLatch cpuPlatformLatch = new CountDownLatch(cpuTaskCount);
        
        for (int i = 0; i < cpuTaskCount; i++) {
            cpuExecutor.submit(() -> {
                try {
                    intensiveCPUTask();
                } finally {
                    cpuPlatformLatch.countDown();
                }
            });
        }
        
        cpuPlatformLatch.await();
        cpuExecutor.shutdown();
        long platformCPUTime = System.nanoTime() - startTime;
        
        // Virtual Thread
        startTime = System.nanoTime();
        CountDownLatch cpuVirtualLatch = new CountDownLatch(cpuTaskCount);
        
        for (int i = 0; i < cpuTaskCount; i++) {
            Thread.startVirtualThread(() -> {
                try {
                    intensiveCPUTask();
                } finally {
                    cpuVirtualLatch.countDown();
                }
            });
        }
        
        cpuVirtualLatch.await();
        long virtualCPUTime = System.nanoTime() - startTime;
        
        System.out.println("  Platform Thread (코어수): " + formatTime(platformCPUTime));
        System.out.println("  Virtual Thread: " + formatTime(virtualCPUTime));
        
        if (virtualCPUTime > platformCPUTime) {
            System.out.println("  CPU 작업 성능: " + 
                String.format("%.1fx 느림 (예상된 결과)", (double)virtualCPUTime / platformCPUTime));
        } else {
            System.out.println("  CPU 작업 성능: 비슷함");
        }
    }
    
    // 3. 처리량(Throughput) 비교
    static void compareThroughput() throws InterruptedException {
        System.out.println("3. 처리량 비교 - 웹 서버 시뮬레이션");
        
        int requestCount = 1000;
        System.out.println(requestCount + "개 동시 요청 처리:");
        
        // Platform Thread 기반 서버
        System.out.println("\nPlatform Thread 기반 서버 (50개 풀):");
        long startTime = System.nanoTime();
        
        ExecutorService serverExecutor = Executors.newFixedThreadPool(50);
        CountDownLatch serverLatch = new CountDownLatch(requestCount);
        
        for (int i = 0; i < requestCount; i++) {
            serverExecutor.submit(() -> {
                try {
                    simulateWebRequest();
                } finally {
                    serverLatch.countDown();
                }
            });
        }
        
        serverLatch.await();
        serverExecutor.shutdown();
        long platformServerTime = System.nanoTime() - startTime;
        
        double platformThroughput = requestCount * 1_000_000_000.0 / platformServerTime;
        System.out.println("  처리 시간: " + formatTime(platformServerTime));
        System.out.println("  처리량: " + String.format("%.1f req/sec", platformThroughput));
        
        // Virtual Thread 기반 서버
        System.out.println("\nVirtual Thread 기반 서버:");
        startTime = System.nanoTime();
        
        CountDownLatch virtualServerLatch = new CountDownLatch(requestCount);
        
        for (int i = 0; i < requestCount; i++) {
            Thread.startVirtualThread(() -> {
                try {
                    simulateWebRequest();
                } finally {
                    virtualServerLatch.countDown();
                }
            });
        }
        
        virtualServerLatch.await();
        long virtualServerTime = System.nanoTime() - startTime;
        
        double virtualThroughput = requestCount * 1_000_000_000.0 / virtualServerTime;
        System.out.println("  처리 시간: " + formatTime(virtualServerTime));
        System.out.println("  처리량: " + String.format("%.1f req/sec", virtualThroughput));
        System.out.println("  처리량 개선: " + 
            String.format("%.1fx", virtualThroughput / platformThroughput));
    }
    
    // 4. Virtual Thread의 한계 시연
    static void demonstrateLimitations() throws InterruptedException {
        System.out.println("4. Virtual Thread 한계점 시연");
        
        System.out.println("\n한계 1: Carrier Thread Pinning");
        System.out.println("synchronized 블록에서 블로킹 시 성능 저하:");
        
        Object lock = new Object();
        int taskCount = 100;
        
        // Pinning 발생 케이스
        long startTime = System.nanoTime();
        CountDownLatch pinningLatch = new CountDownLatch(taskCount);
        
        for (int i = 0; i < taskCount; i++) {
            Thread.startVirtualThread(() -> {
                synchronized (lock) {
                    try {
                        Thread.sleep(50); // synchronized + 블로킹 = pinning
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                pinningLatch.countDown();
            });
        }
        
        pinningLatch.await();
        long pinningTime = System.nanoTime() - startTime;
        
        // ReentrantLock 사용 케이스
        ReentrantLock reentrantLock = new ReentrantLock();
        startTime = System.nanoTime();
        CountDownLatch noLatch = new CountDownLatch(taskCount);
        
        for (int i = 0; i < taskCount; i++) {
            Thread.startVirtualThread(() -> {
                reentrantLock.lock();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    reentrantLock.unlock();
                }
                noLatch.countDown();
            });
        }
        
        noLatch.await();
        long noPinningTime = System.nanoTime() - startTime;
        
        System.out.println("  synchronized 사용: " + formatTime(pinningTime));
        System.out.println("  ReentrantLock 사용: " + formatTime(noPinningTime));
        System.out.println("  성능 차이: " + 
            String.format("%.1fx", (double)pinningTime / noPinningTime));
    }
    
    // 5. 실무 권장사항
    static void showBestPractices() {
        System.out.println("5. Virtual Thread 실무 권장사항");
        System.out.println();
        System.out.println("✅ Virtual Thread 사용이 적합한 경우:");
        System.out.println("  - I/O 집약적 작업 (파일, 네트워크, DB)");
        System.out.println("  - 높은 동시성이 필요한 서버 애플리케이션");
        System.out.println("  - 블로킹 I/O가 많은 마이크로서비스");
        System.out.println("  - 웹 크롤링, API 호출 등");
        System.out.println();
        System.out.println("❌ Virtual Thread 사용을 피해야 하는 경우:");
        System.out.println("  - CPU 집약적 작업 (암호화, 이미지 처리, 수학 연산)");
        System.out.println("  - synchronized와 블로킹 I/O 조합");
        System.out.println("  - ThreadLocal을 많이 사용하는 경우");
        System.out.println();
        System.out.println("💡 최적화 팁:");
        System.out.println("  - synchronized 대신 ReentrantLock 사용");
        System.out.println("  - CPU 작업은 별도 Platform Thread Pool 사용");
        System.out.println("  - JVM 옵션: --enable-preview (Java 19-20)");
        System.out.println("  - 모니터링: JFR, VisualVM으로 carrier thread 상태 확인");
    }
    
    // 유틸리티 메서드들
    private static void forceGC() throws InterruptedException {
        System.gc();
        Thread.sleep(1000);
        System.gc();
        Thread.sleep(1000);
    }
    
    private static long getUsedMemory() {
        return memoryBean.getHeapMemoryUsage().getUsed();
    }
    
    private static String formatMemory(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
    }
    
    private static String formatTime(long nanos) {
        if (nanos < 1_000_000) return String.format("%.2f μs", nanos / 1000.0);
        if (nanos < 1_000_000_000) return String.format("%.2f ms", nanos / 1_000_000.0);
        return String.format("%.2f s", nanos / 1_000_000_000.0);
    }
    
    private static void intensiveCPUTask() {
        // 실제 CPU 집약적 작업
        double result = 0;
        for (int i = 0; i < 500_000; i++) {
            result += Math.sqrt(i) * Math.sin(i);
        }
        // 컴파일러 최적화 방지
        if (result < 0) System.out.print("");
    }
    
    private static void simulateWebRequest() {
        try {
            // 실제 웹 요청 처리 시뮬레이션
            Thread.sleep(10); // 인증
            Thread.sleep(20); // 비즈니스 로직
            Thread.sleep(100); // DB 쿼리
            Thread.sleep(15); // 응답 생성
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
} 