package com.JavaCoreTil.thread.task;

/**
 * 실습 2: Thread 상태 모니터링
 * 
 * 학습 목표:
 * - Thread.State 열거형 이해
 * - Thread 생명주기 관찰 (NEW → RUNNABLE → TIMED_WAITING → TERMINATED)
 * - getState() 메서드 활용
 * - join() 메서드로 스레드 완료 대기
 */
public class Task02_ThreadStateMonitoring {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Task02: Thread 상태 모니터링 실습 ===");
        
        // TODO 1: 작업 스레드 생성 (아직 시작하지 않음)
        Thread workThread = new Thread(() -> {
            System.out.println("작업 스레드가 3초 동안 대기합니다");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("작업 스레드가 중단되었습니다");
            }
            System.out.println("작업 스레드 작업 완료!");
        }, "WorkerThread");
        
        // TODO 2: Thread 생성 직후 상태 확인
        System.out.println("Thread 생성 직후 상태: " + workThread.getState());
        
        // TODO 3: start() 호출 후 상태 확인
        workThread.start();
        System.out.println("start() 호출 직후 상태: " + workThread.getState());
        
        // TODO 4: 실행 중 상태 변화 관찰
        Thread.sleep(1000); // 메인 스레드 1초 대기
        System.out.println("1초 후 상태: " + workThread.getState());
        
        // TODO 5: join()으로 스레드 완료 대기
        System.out.println("작업 스레드 완료 대기 중...");
        workThread.join();
        System.out.println("작업 스레드 완료!");
        
        // TODO 6: 종료 후 상태 확인
        System.out.println("최종 상태: " + workThread.getState());
        
        // TODO 7: (도전과제) 상태 변화를 실시간으로 모니터링
        System.out.println("\n=== 도전과제: 실시간 상태 모니터링 ===");
        
        Thread monitoredThread = new Thread(() -> {
            try {
                System.out.println("모니터링 대상 스레드 시작");
                Thread.sleep(2000);
                System.out.println("모니터링 대상 스레드 중간 작업");
                Thread.sleep(2000);
                System.out.println("모니터링 대상 스레드 완료");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "MonitoredThread");
        
        // 상태 모니터링 스레드
        Thread monitorThread = new Thread(() -> {
            Thread.State previousState = null;
            while (monitoredThread.getState() != Thread.State.TERMINATED) {
                Thread.State currentState = monitoredThread.getState();
                if (currentState != previousState) {
                    System.out.println("[모니터] " + monitoredThread.getName() + 
                                     " 상태 변화: " + previousState + " → " + currentState);
                    previousState = currentState;
                }
                try {
                    Thread.sleep(100); // 100ms마다 체크
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            System.out.println("[모니터] 최종 상태: " + monitoredThread.getState());
        }, "StateMonitor");
        
        monitorThread.start();
        Thread.sleep(500); // 모니터 스레드가 먼저 시작되도록
        monitoredThread.start();
        
        monitoredThread.join();
        monitorThread.join();
        
        System.out.println("=== Thread 상태 모니터링 완료 ===");
    }
    
    /*
     * 💡 참고: Thread.State 열거형
     * 
     * - NEW: 스레드 생성, 아직 start() 호출 안 함
     * - RUNNABLE: 실행 중이거나 실행 대기 중
     * - BLOCKED: 동기화 락을 기다리는 중
     * - WAITING: 다른 스레드의 작업을 무한정 기다리는 중
     * - TIMED_WAITING: 지정된 시간 동안 기다리는 중
     * - TERMINATED: 실행 완료
     */
} 