package com.JavaCoreTil.thread.task;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Task06: yield()와 sleep() 차이점 시각화
 */
public class Thread_Ex_Task06_YieldVsSleepVisualization {
    
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== yield() vs sleep() 차이점 ===");
        
        // yield() 없이 실행 - 불공정
        demonstrateUnfairness();
        
        Thread.sleep(2000);
        System.out.println("\n" + "=".repeat(40));
        
        // yield()로 공정성 개선
        demonstrateFairness();
        
        Thread.sleep(2000);
        System.out.println("\n" + "=".repeat(40));
        
        // sleep()과의 차이점
        demonstrateSleepDifference();
    }
    
    // yield() 없는 상황 - CPU 독점 문제
    private static void demonstrateUnfairness() {
        System.out.println("\n1. yield() 없는 상황 - 불공정 실행");
        System.out.println("빠른 Thread가 CPU를 독점합니다");
        
        Thread fastThread = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                System.out.printf("[%s] 빠른작업: %d\n", getCurrentTime(), i);
                busyWait(1_000_000);
            }
        });
        
        Thread slowThread = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                System.out.printf("[%s] 느린작업: %d\n", getCurrentTime(), i);
                busyWait(10_000_000);
            }
        });
        
        long startTime = System.currentTimeMillis();
        fastThread.start();
        slowThread.start();
        
        try {
            fastThread.join();
            slowThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        long endTime = System.currentTimeMillis();
        System.out.printf("불공정 실행 시간: %d ms\n", (endTime - startTime));
        System.out.println("// 빠른작업이 먼저 모두 완료됨");
    }
    
    // yield()로 공정성 개선
    private static void demonstrateFairness() {
        System.out.println("\n2. yield()로 공정성 개선");
        System.out.println("다른 Thread에게 실행 기회 제공");
        
        Thread politeFastThread = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                System.out.printf("[%s] 예의바른작업: %d\n", getCurrentTime(), i);
                Thread.yield(); // CPU 양보
                busyWait(1_000_000);
            }
        });
        
        Thread slowThread = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                System.out.printf("[%s] 느린작업: %d\n", getCurrentTime(), i);
                busyWait(10_000_000);
            }
        });
        
        long startTime = System.currentTimeMillis();
        politeFastThread.start();
        slowThread.start();
        
        try {
            politeFastThread.join();
            slowThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        long endTime = System.currentTimeMillis();
        System.out.printf("공정한 실행 시간: %d ms\n", (endTime - startTime));
        System.out.println("// 두 작업이 번갈아가며 실행됨");
    }
    
    // sleep()과의 차이점 - 실시간 데이터 처리 시나리오
    private static void demonstrateSleepDifference() {
        System.out.println("\n3. yield() vs sleep() 목적 차이");
        System.out.println("yield: 공정성 양보, sleep: 시간 간격 제어");
        
        Thread dataProcessor = new Thread(() -> {
            for (int i = 1; i <= 8; i++) {
                System.out.printf("[%s] 데이터처리: %d번째 배치\n", getCurrentTime(), i);
                busyWait(2_000_000);
                Thread.yield(); // 다른 작업에게 기회 양보
            }
        });
        
        Thread alertChecker = new Thread(() -> {
            for (int i = 1; i <= 4; i++) {
                System.out.printf("[%s] 알림체크: %d번째 확인\n", getCurrentTime(), i);
                busyWait(5_000_000);
            }
        });
        
        Thread scheduler = new Thread(() -> {
            for (int i = 1; i <= 3; i++) {
                System.out.printf("[%s] 스케줄러: %d분 간격 실행\n", getCurrentTime(), i);
                try {
                    Thread.sleep(800); // 정확한 시간 간격
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        });
        
        long startTime = System.currentTimeMillis();
        dataProcessor.start();
        alertChecker.start();
        scheduler.start();
        
        try {
            dataProcessor.join();
            alertChecker.join();
            scheduler.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        long endTime = System.currentTimeMillis();
        System.out.printf("혼합 시나리오 시간: %d ms\n", (endTime - startTime));
        
        System.out.println("\n// yield() 존재 이유:");
        System.out.println("// 1. 공정성: CPU 독점 방지");
        System.out.println("// 2. 응답성: 중요 작업 우선 실행");
        System.out.println("// 3. 협력적 멀티태스킹");
        System.out.println("// 4. sleep()과 다름: 시간 기반이 아닌 우선순위 기반");
    }
    
    private static String getCurrentTime() {
        return LocalTime.now().format(TIME_FORMAT);
    }
    
    private static void busyWait(int iterations) {
        long sum = 0;
        for (int i = 0; i < iterations; i++) {
            sum += i * i;
        }
    }
} 