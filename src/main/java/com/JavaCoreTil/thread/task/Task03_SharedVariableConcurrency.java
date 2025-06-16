package com.JavaCoreTil.thread.task;

/**
 * 실습 3: 공유 변수 동시성 문제 체험
 * 
 * 학습 목표:
 * - Race Condition (경쟁 상황) 직접 경험
 * - 공유 자원에 대한 동시 접근 문제 이해
 * - Thread-unsafe 상황 관찰
 * - 동기화 필요성 체감
 */
public class Task03_SharedVariableConcurrency {
    
    // 공유 변수 - 모든 스레드가 접근 가능
    private static int sharedCounter = 0;
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Task03: 공유 변수 동시성 문제 체험 ===");
        System.out.println("초기 sharedCounter 값: " + sharedCounter);
        
        // TODO 1: 3개의 스레드 생성
        Thread worker1 = new Thread(() -> incrementCounter("Worker-1", 1000), "Worker-1");
        Thread worker2 = new Thread(() -> incrementCounter("Worker-2", 1000), "Worker-2");
        Thread worker3 = new Thread(() -> incrementCounter("Worker-3", 1000), "Worker-3");
        
        // TODO 3: 모든 스레드 시작
        System.out.println("3개 스레드 시작 - 각각 1000번씩 증가 예상");
        long startTime = System.currentTimeMillis();
        
        worker1.start();
        worker2.start();
        worker3.start();
        
        // TODO 4: 모든 스레드 완료 대기
        worker1.join();
        System.out.println("Worker-1 완료");
        
        worker2.join();
        System.out.println("Worker-2 완료");
        
        worker3.join();
        System.out.println("Worker-3 완료");
        
        long endTime = System.currentTimeMillis();
        
        // TODO 5: 최종 결과 확인 및 분석
        int expectedResult = 3000;
        int actualResult = sharedCounter;
        
        System.out.println("\n=== 결과 분석 ===");
        System.out.println("예상 결과: " + expectedResult);
        System.out.println("실제 결과: " + actualResult);
        System.out.println("차이: " + (expectedResult - actualResult));
        System.out.println("실행 시간: " + (endTime - startTime) + "ms");
        
        if (actualResult != expectedResult) {
            System.out.println("🚨 Race Condition 발생! 동시성 문제 확인됨");
        } else {
            System.out.println("✅ 이번에는 운 좋게 정확한 결과가 나왔습니다 (드문 경우)");
        }
        
        // TODO 6: (분석과제) 여러 번 실행해보기
        System.out.println("\n=== 여러 번 실행 테스트 ===");
        System.out.println("이 프로그램을 여러 번 실행해보세요. 매번 다른 결과가 나올 것입니다!");
        
        // TODO 7: (심화과제) 증가량을 더 크게 해보기
        System.out.println("\n=== 심화 테스트: 10000번씩 증가 ===");
        sharedCounter = 0; // 초기화
        
        Thread heavyWorker1 = new Thread(() -> incrementCounter("Heavy-1", 10000), "Heavy-1");
        Thread heavyWorker2 = new Thread(() -> incrementCounter("Heavy-2", 10000), "Heavy-2");
        Thread heavyWorker3 = new Thread(() -> incrementCounter("Heavy-3", 10000), "Heavy-3");
        
        startTime = System.currentTimeMillis();
        heavyWorker1.start();
        heavyWorker2.start();
        heavyWorker3.start();
        
        heavyWorker1.join();
        heavyWorker2.join();
        heavyWorker3.join();
        endTime = System.currentTimeMillis();
        
        int heavyExpected = 30000;
        int heavyActual = sharedCounter;
        
        System.out.println("심화 테스트 - 예상: " + heavyExpected + ", 실제: " + heavyActual);
        System.out.println("심화 테스트 - 차이: " + (heavyExpected - heavyActual));
        System.out.println("심화 테스트 - 실행 시간: " + (endTime - startTime) + "ms");
        
        System.out.println("=== 동시성 문제 체험 완료 ===");
        System.out.println("다음 단계에서는 synchronized로 이 문제를 해결해볼 예정입니다!");
    }
    
    /**
     * 공유 카운터를 증가시키는 메서드
     * @param threadName 현재 스레드 이름
     * @param iterations 반복 횟수
     */
    private static void incrementCounter(String threadName, int iterations) {
        for (int i = 1; i <= iterations; i++) {
            sharedCounter++; // 🚨 여기서 Race Condition 발생!
            
            // 100의 배수마다 현재 값 출력 (너무 많은 출력 방지)
            if (i % 100 == 0) {
                System.out.println("[" + threadName + "] " + 
                                 i + "번째: sharedCounter = " + sharedCounter);
            }
        }
        System.out.println("[" + threadName + "] 모든 작업 완료!");
    }
    
    /*
     * 💡 왜 예상과 다른 결과가 나올까?
     * 
     * Race Condition 발생 과정:
     * 1. Thread-1이 sharedCounter 값을 읽음 (예: 100)
     * 2. Thread-2도 동시에 같은 값을 읽음 (예: 100)  
     * 3. Thread-1이 101로 증가시켜 저장
     * 4. Thread-2도 101로 증가시켜 저장
     * 5. 결과: 2번 증가해야 하는데 1번만 증가 (100 → 101)
     * 
     * sharedCounter++ 연산의 실제 과정:
     * 1. 메모리에서 sharedCounter 값 읽기 (LOAD)
     * 2. CPU에서 1 증가 (ADD)
     * 3. 결과를 메모리에 저장 (STORE)
     * 
     * 여러 스레드가 동시에 이 3단계를 수행하면서 서로의 작업을 덮어쓰게 됨!
     * 
     * 해결책: synchronized, Atomic 변수, Lock 등 사용
     */
} 