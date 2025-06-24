package com.JavaCoreTil.thread.task;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Task09: Lock 인터페이스와 고급 동기화 실습
 * 스토리: 온라인 게임 서버의 랭킹 시스템 구축
 */
public class Thread_Ex_Task09_LockPractice {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 온라인 게임 랭킹 시스템 구축 프로젝트 ===\n");
        
        // 1. Lock vs synchronized 비교
        System.out.println("1. Lock vs synchronized 성능 비교");
        compareLockPerformance();
        
        // 2. ReentrantLock 타임아웃 기능
        System.out.println("\n2. ReentrantLock 타임아웃 기능");
        demonstrateTimeout();
        
        // 3. 공정한 락 vs 비공정한 락
        System.out.println("\n3. 공정한 락 vs 비공정한 락");
        demonstrateFairness();
        
        // 4. Lock Condition 활용
        System.out.println("\n4. Lock Condition으로 생산자-소비자 패턴");
        demonstrateCondition();
        
        System.out.println("\n=== 모든 실습 완료 ===");
    }
    
    // 1. Lock vs synchronized 성능 비교
    private static void compareLockPerformance() throws InterruptedException {
        System.out.println("찰리: 게임 점수 업데이트 시스템을 구현해야 해요!");
        
        // synchronized 방식
        SynchronizedCounter syncCounter = new SynchronizedCounter();
        long syncTime = measurePerformance(syncCounter, "synchronized");
        
        // ReentrantLock 방식
        LockCounter lockCounter = new LockCounter();
        long lockTime = measurePerformance(lockCounter, "ReentrantLock");
        
        System.out.println("벤저민: 성능 차이를 보니 " + 
            (syncTime > lockTime ? "Lock이 더 빠르네!" : "synchronized가 더 빠르네!"));
    }
    
    private static long measurePerformance(Counter counter, String type) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        
        Thread[] threads = new Thread[5];
        for (int i = 0; i < 5; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    counter.increment();
                }
            }, "게이머-" + (i + 1));
            threads[i].start();
        }
        
        for (Thread thread : threads) {
            thread.join();
        }
        
        long endTime = System.currentTimeMillis();
        System.out.println(type + " 방식 - 최종 점수: " + counter.getValue() + 
                          ", 소요시간: " + (endTime - startTime) + "ms");
        
        return endTime - startTime;
    }
    
    // 2. ReentrantLock 타임아웃 기능
    private static void demonstrateTimeout() throws InterruptedException {
        System.out.println("찰리: DB가 느려서 점수 업데이트가 멈춰요!");
        System.out.println("일론: 타임아웃 기능으로 해결해보자!");
        
        ReentrantLock dbLock = new ReentrantLock();
        
        // 첫 번째 스레드가 락을 오래 점유
        Thread slowDbThread = new Thread(() -> {
            dbLock.lock();
            try {
                System.out.println("[느린 DB] DB 작업 시작... (5초 소요)");
                safeSleep(5000);
                System.out.println("[느린 DB] DB 작업 완료");
            } finally {
                dbLock.unlock();
            }
        }, "느린-DB");
        
        // 두 번째 스레드가 타임아웃으로 포기
        Thread timeoutThread = new Thread(() -> {
            System.out.println("[타임아웃-체크 DB] 점수 업데이트 시도 중...");
            try {
                if (dbLock.tryLock(2, TimeUnit.SECONDS)) {
                    try {
                        System.out.println("[타임아웃-체크 DB] 점수 업데이트 성공!");
                    } finally {
                        dbLock.unlock();
                    }
                } else {
                    System.out.println("[타임아웃-체크 DB] 타임아웃! 캐시 사용으로 대체");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "타임아웃-체크");
        
        slowDbThread.start();
        safeSleep(1000);
        timeoutThread.start();
        
        slowDbThread.join();
        timeoutThread.join();
    }
    
    // 3. 공정한 락 vs 비공정한 락
    private static void demonstrateFairness() throws InterruptedException {
        System.out.println("찰리: 일부 플레이어만 계속 랭킹 업데이트를 해요!");
        System.out.println("앤드류: 공정한 락으로 순서를 보장해보자!");
        
        ReentrantLock unfairLock = new ReentrantLock(false); // 비공정
        ReentrantLock fairLock = new ReentrantLock(true);    // 공정
        
        System.out.println("\n비공정한 락 (성능 우선):");
        testFairness(unfairLock);
        
        System.out.println("\n공정한 락 (순서 보장):");
        testFairness(fairLock);
    }
    
    private static void testFairness(ReentrantLock lock) throws InterruptedException {
        Thread[] threads = new Thread[3];
        
        for (int i = 0; i < 3; i++) {
            final int playerId = i + 1;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 3; j++) {
                    lock.lock();
                    try {
                        System.out.println("플레이어" + playerId + " 랭킹 업데이트 " + (j + 1));
                        safeSleep(100);
                    } finally {
                        lock.unlock();
                    }
                }
            }, "플레이어-" + playerId);
        }
        
        for (Thread thread : threads) {
            thread.start();
        }
        
        for (Thread thread : threads) {
            thread.join();
        }
    }
    
    // 4. Lock Condition으로 생산자-소비자 패턴
    private static void demonstrateCondition() throws InterruptedException {
        System.out.println("찰리: 대규모 게임 매칭 시스템을 만들어야 해요!");
        System.out.println("벤저민: 생산자-소비자 패턴으로 만들어보자!");
        System.out.println("- 생산자: 플레이어들이 매칭 요청 생성");
        System.out.println("- 소비자: 매칭메이커가 10명씩 모아서 게임 생성");
        System.out.println("- 버퍼: 대기 큐 (최대 50명)\n");
        
        GameMatchingQueue matchingQueue = new GameMatchingQueue();
        
        // 매칭메이커(소비자) 시작
        Thread matchmaker = new Thread(() -> {
            try {
                for (int gameNumber = 1; gameNumber <= 3; gameNumber++) {
                    matchingQueue.createGame(gameNumber);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "매칭메이커");
        matchmaker.start();
        
        safeSleep(500);
        
        // 플레이어들(생산자)이 지속적으로 매칭 요청
        Thread[] players = new Thread[25]; // 25명의 플레이어
        for (int i = 0; i < 25; i++) {
            final int playerId = i + 1;
            players[i] = new Thread(() -> {
                try {
                    matchingQueue.requestMatch(playerId);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "플레이어-" + playerId);
            players[i].start();
            safeSleep(200); // 플레이어들이 순차적으로 요청
        }
        
        for (Thread player : players) {
            player.join();
        }
        matchmaker.join();
        
        System.out.println("\n벤저민: 생산자-소비자 패턴으로 완벽한 매칭 시스템 완성!");
        System.out.println("찰리: 이제 수천 명이 와도 문제없겠네요!");
    }
    
    // 유틸리티 메서드
    private static void safeSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    // Counter 인터페이스
    interface Counter {
        void increment();
        int getValue();
    }
    
    // synchronized 방식 카운터
    static class SynchronizedCounter implements Counter {
        private int count = 0;
        
        @Override
        public synchronized void increment() {
            count++;
        }
        
        @Override
        public synchronized int getValue() {
            return count;
        }
    }
    
    // ReentrantLock 방식 카운터
    static class LockCounter implements Counter {
        private final Lock lock = new ReentrantLock();
        private int count = 0;
        
        @Override
        public void increment() {
            lock.lock();
            try {
                count++;
            } finally {
                lock.unlock();
            }
        }
        
        @Override
        public int getValue() {
            lock.lock();
            try {
                return count;
            } finally {
                lock.unlock();
            }
        }
    }
    
    // 생산자-소비자 패턴 게임 매칭 큐
    static class GameMatchingQueue {
        private final Lock lock = new ReentrantLock();
        private final Condition notEmpty = lock.newCondition(); // 소비자(매칭메이커) 대기
        private final Condition notFull = lock.newCondition();  // 생산자(플레이어) 대기
        private final Condition gameReady = lock.newCondition(); // 게임 준비 완료
        
        private final java.util.Queue<Integer> waitingQueue = new java.util.LinkedList<>();
        private final int maxCapacity = 50; // 최대 대기 인원
        private final int gameSize = 10;    // 게임당 필요 인원
        
        // 생산자: 플레이어가 매칭 요청
        public void requestMatch(int playerId) throws InterruptedException {
            lock.lock();
            try {
                // 대기 큐가 가득 찬 경우 대기
                while (waitingQueue.size() >= maxCapacity) {
                    System.out.println("😴 플레이어" + playerId + "님, 대기실이 가득참! 잠시 기다려주세요...");
                    notFull.await();
                }
                
                // 대기 큐에 추가
                waitingQueue.offer(playerId);
                System.out.println("🎮 플레이어" + playerId + "님 대기 중... (대기자: " + waitingQueue.size() + "/" + maxCapacity + "명)");
                
                // 매칭메이커에게 알림 (10명 이상 모이면)
                if (waitingQueue.size() >= gameSize) {
                    notEmpty.signal();
                }
                
                // 게임 시작될 때까지 대기
                while (waitingQueue.contains(playerId)) {
                    gameReady.await();
                }
                
                System.out.println("🚀 플레이어" + playerId + "님 게임 입장 완료!");
                
            } finally {
                lock.unlock();
            }
        }
        
        // 소비자: 매칭메이커가 게임 생성
        public void createGame(int gameNumber) throws InterruptedException {
            lock.lock();
            try {
                System.out.println("🎯 매칭메이커: 게임" + gameNumber + " 생성 준비 중...");
                
                // 10명 이상 모일 때까지 대기
                while (waitingQueue.size() < gameSize) {
                    System.out.println("⏳ 매칭메이커: 플레이어 대기 중... (현재: " + waitingQueue.size() + "/" + gameSize + "명)");
                    notEmpty.await();
                }
                
                // 10명을 게임에 배정
                System.out.println("⚡ 게임" + gameNumber + " 시작! 다음 플레이어들이 입장합니다:");
                for (int i = 0; i < gameSize && !waitingQueue.isEmpty(); i++) {
                    Integer playerId = waitingQueue.poll();
                    System.out.println("   👤 플레이어" + playerId);
                }
                
                System.out.println("✅ 게임" + gameNumber + " 매칭 완료! (남은 대기자: " + waitingQueue.size() + "명)");
                
                // 대기 중인 플레이어들에게 알림
                gameReady.signalAll();
                
                // 대기실에 자리가 생겼음을 알림
                notFull.signalAll();
                
                // 게임 진행 시뮬레이션
                safeSleep(1000);
                
            } finally {
                lock.unlock();
            }
        }
    }
} 