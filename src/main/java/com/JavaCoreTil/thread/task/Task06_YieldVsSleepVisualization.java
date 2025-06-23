package com.JavaCoreTil.thread.task;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * 실습 6: yield()와 sleep()의 차이점 시각화
 * 
 * 학습 목표:
 * - yield()와 sleep()의 동작 차이 시각적 확인
 * - CPU 사용권 양보 vs 시간 기반 대기 차이 이해
 * - yield()의 실제 존재 이유와 활용 시나리오 체험
 * - Thread 스케줄링 동작 원리 체험
 */
public class Task06_YieldVsSleepVisualization {
    
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Task06: yield() vs sleep() 차이점 시각화 ===\n");
        
        // 1단계: yield()의 존재 이유 - 공정성 문제
        demonstrateUnfairness();
        
        Thread.sleep(2000);
        System.out.println("\n" + "=".repeat(60) + "\n");
        
        // 2단계: yield()로 공정성 개선
        demonstrateFairness();
        
        Thread.sleep(2000);
        System.out.println("\n" + "=".repeat(60) + "\n");
        
        // 3단계: sleep()과의 차이점
        demonstrateSleepDifference();
    }
    
    /**
     * 1단계: yield() 없이 실행 - 불공정한 실행
     * 하나의 Thread가 독점하는 문제 확인
     */
    private static void demonstrateUnfairness() {
        System.out.println("😤 1단계: yield() 없는 상황 - 불공정한 실행");
        System.out.println("- 빠른 Thread가 CPU를 독점합니다");
        System.out.println("- 다른 Thread는 실행 기회를 거의 못 얻습니다\n");
        
        Thread fastThread = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                System.out.printf("[%s] 🏃‍♂️ 빠른작업: %d\n", getCurrentTime(), i);
                // yield() 없음 - CPU 독점!
                busyWait(1_000_000); // 짧은 작업
            }
        });
        
        Thread slowThread = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                System.out.printf("[%s] 🐌 느린작업: %d\n", getCurrentTime(), i);
                busyWait(10_000_000); // 긴 작업
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
        System.out.printf("\n⏱️ 불공정 실행 총 시간: %d ms\n", (endTime - startTime));
        System.out.println("👀 관찰: 빠른작업이 먼저 모두 완료되고, 느린작업이 나중에 실행됨");
    }
    
    /**
     * 2단계: yield()로 공정성 개선
     * CPU 사용권 양보로 다른 Thread에게 기회 제공
     */
    private static void demonstrateFairness() {
        System.out.println("🤝 2단계: yield()로 공정성 개선");
        System.out.println("- yield()로 다른 Thread에게 실행 기회 제공");
        System.out.println("- 상대적으로 공정한 실행 순서 보장\n");
        
        Thread politeFastThread = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                System.out.printf("[%s] 🙋‍♂️ 예의바른작업: %d\n", getCurrentTime(), i);
                Thread.yield(); // 양보!
                busyWait(1_000_000); // 짧은 작업
            }
        });
        
        Thread slowThread = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                System.out.printf("[%s] 🐌 느린작업: %d\n", getCurrentTime(), i);
                busyWait(10_000_000); // 긴 작업
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
        System.out.printf("\n⏱️ 공정한 실행 총 시간: %d ms\n", (endTime - startTime));
        System.out.println("👀 관찰: 두 작업이 번갈아가며 실행됨 (더 공정함)");
    }
    
    /**
     * 3단계: sleep()과의 차이점 명확히 구분
     * yield()와 sleep()의 서로 다른 목적 확인
     */
    private static void demonstrateSleepDifference() {
        System.out.println("⚖️ 3단계: yield() vs sleep() 목적의 차이");
        System.out.println("- yield(): 공정성을 위한 양보 (즉시 다시 실행 가능)");
        System.out.println("- sleep(): 정확한 시간 간격 제어 (확실한 대기)\n");
        
        // 실시간 데이터 처리 시나리오
        System.out.println("📡 시나리오: 실시간 데이터 처리 시스템");
        
        Thread dataProcessor = new Thread(() -> {
            for (int i = 1; i <= 8; i++) {
                System.out.printf("[%s] 📊 데이터처리: %d번째 배치 (yield 사용)\n", 
                                getCurrentTime(), i);
                
                // 데이터 처리 시뮬레이션
                busyWait(2_000_000);
                
                // 다른 중요한 작업에게 기회 양보
                Thread.yield();
            }
        });
        
        Thread alertChecker = new Thread(() -> {
            for (int i = 1; i <= 4; i++) {
                System.out.printf("[%s] 🚨 알림체크: %d번째 확인 (중요!)\n", 
                                getCurrentTime(), i);
                
                // 알림 체크 시뮬레이션
                busyWait(5_000_000);
            }
        });
        
        Thread scheduler = new Thread(() -> {
            for (int i = 1; i <= 3; i++) {
                System.out.printf("[%s] ⏰ 스케줄러: %d분 간격 실행 (sleep 사용)\n", 
                                getCurrentTime(), i);
                
                try {
                    Thread.sleep(800); // 정확한 시간 간격 필요
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
        System.out.printf("\n⏱️ 혼합 시나리오 총 시간: %d ms\n", (endTime - startTime));
        
        System.out.println("\n🎯 yield()의 존재 이유:");
        System.out.println("1. 공정성: CPU 독점 방지, 다른 Thread에게 기회 제공");
        System.out.println("2. 응답성: 중요한 작업이 더 빨리 실행될 수 있도록 도움");
        System.out.println("3. 협력적 멀티태스킹: 스스로 양보하여 전체 시스템 성능 향상");
        System.out.println("4. sleep()과 다른 점: 시간 기반이 아닌 우선순위/공정성 기반");
        
        System.out.println("\n💡 실무 활용 시나리오:");
        System.out.println("- 실시간 데이터 처리에서 다른 중요 작업에게 기회 제공");
        System.out.println("- 게임에서 렌더링과 로직 처리 간의 균형");
        System.out.println("- 배치 처리에서 사용자 요청에 더 빠른 응답 제공");
    }
    
    /**
     * 현재 시간을 밀리초 단위로 반환
     */
    private static String getCurrentTime() {
        return LocalTime.now().format(TIME_FORMAT);
    }
    
    /**
     * CPU 집약적 작업 시뮬레이션 (busy waiting)
     * @param iterations 반복 횟수
     */
    private static void busyWait(int iterations) {
        for (int i = 0; i < iterations; i++) {
            // CPU 사용 시뮬레이션
            Math.random();
        }
    }
    
    /*
     * 💡 핵심 학습 포인트:
     * 
     * 🔄 yield()의 존재 이유:
     * 1. 공정성 보장: CPU 독점 방지
     * 2. 시스템 응답성 향상: 중요한 작업 우선 실행
     * 3. 협력적 멀티태스킹: 자발적 양보
     * 4. 리소스 효율성: 불필요한 대기 시간 없음
     * 
     * 😴 sleep()과의 차이:
     * - yield(): 공정성/우선순위 기반 (즉시 재실행 가능)
     * - sleep(): 시간 기반 (정확한 대기 시간)
     * 
     * 🎯 실무 적용:
     * - 실시간 시스템: 중요도에 따른 작업 순서 조정
     * - 배치 처리: 사용자 요청에 더 빠른 응답
     * - 게임/미디어: 부드러운 사용자 경험
     * - 서버: 요청 처리의 공정성 보장
     */
} 