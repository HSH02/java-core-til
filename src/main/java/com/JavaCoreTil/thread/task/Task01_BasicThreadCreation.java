package com.JavaCoreTil.thread.task;

/**
 * 실습 1: 기본 Thread 생성과 실행 (완성된 답안)
 * 
 * 학습 목표:
 * - Thread 생성 방법 이해
 * - Thread 이름 설정과 확인
 * - 메인 스레드와 작업 스레드의 독립적 실행 체험
 * - start() vs run() 차이점 이해
 */
public class Task01_BasicThreadCreation {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Task01: 기본 Thread 생성과 실행 실습 ===");
        
        // ✅ TODO 1: "카운터-스레드"라는 이름의 스레드 생성
        Thread counterThread = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                System.out.println("[" + Thread.currentThread().getName() + 
                                 "] 카운트: " + i);
                try {
                    Thread.sleep(1000); // 1초 대기
                } catch (InterruptedException e) {
                    System.out.println("카운터 스레드 인터럽트 발생!");
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            System.out.println("카운터 스레드 작업 완료!");
        });
        
        // Thread 이름 설정 (중요!)
        counterThread.setName("카운터-스레드");
        
        // ✅ TODO 3: 스레드 시작
        counterThread.start();
        
        // ✅ TODO 2: 메인 스레드에서 직접 출력 (새 Thread 생성 안 함!)
        for (int i = 1; i <= 5; i++) {
            System.out.println("[" + Thread.currentThread().getName() + 
                             "] 메인 작업 진행 중... " + i);
            try {
                Thread.sleep(500); // 0.5초 대기
            } catch (InterruptedException e) {
                System.out.println("메인 스레드 인터럽트 발생!");
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        System.out.println("\n=== 메인 스레드의 일반 작업 완료 ===");
        
        // ✅ TODO 4: run() vs start() 차이 실험
        System.out.println("\n=== run() vs start() 실험 ===");
        
        Thread testThread1 = new Thread(() -> {
            System.out.println("run() 호출 시 실행되는 스레드: " + 
                             Thread.currentThread().getName());
        });
        
        Thread testThread2 = new Thread(() -> {
            System.out.println("start() 호출 시 실행되는 스레드: " + 
                             Thread.currentThread().getName());
        });
        
        System.out.println("1. run() 호출 (메인 스레드에서 직접 실행):");
        testThread1.run(); // 메인 스레드에서 직접 실행!
        
        System.out.println("2. start() 호출 (새 스레드에서 실행):");
        testThread2.start(); // 새 스레드에서 비동기 실행!
        
        // 카운터 스레드가 완료될 때까지 대기 (선택사항)
        try {
            counterThread.join(); // 카운터 스레드 완료 대기
            testThread2.join();   // 테스트 스레드 완료 대기
        } catch (InterruptedException e) {
            System.out.println("join 중 인터럽트 발생!");
            Thread.currentThread().interrupt();
        }
        
        System.out.println("\n=== 모든 스레드 작업 완료 ===");
        System.out.println("=== 메인 스레드 종료 ===");
    }
    
    /*
     * 💡 핵심 학습 포인트:
     * 
     * 1. Thread 생성: new Thread(람다식) + setName()
     * 2. 메인 스레드 vs 작업 스레드: 독립적으로 동시 실행
     * 3. run() vs start(): 
     *    - run(): 메인 스레드에서 직접 호출 (동기)
     *    - start(): 새 스레드에서 비동기 실행
     * 4. join(): 다른 스레드 완료까지 대기
     * 5. InterruptedException: 반드시 처리 필요
     */
} 