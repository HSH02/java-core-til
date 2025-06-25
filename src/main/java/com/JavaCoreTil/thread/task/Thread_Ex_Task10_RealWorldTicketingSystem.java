package com.JavaCoreTil.thread.task;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.Random;

/**
 * 실습 10: 실제 서비스 - 콘서트 티켓팅 시스템
 * 
 * 📖 스토리: 찰리의 콘서트 티켓팅 시스템 개발기
 * 
 * 배경: 글로벌 엔터테인먼트 회사 '뮤직월드'에서 BTS 월드투어 티켓팅 시스템을 개발하게 된 찰리
 * 상황: 동시접속자 10만명, 티켓 1000장, 서버 5대로 안정적인 티켓팅 시스템 구축 필요
 * 
 * 문제들:
 * 1. 티켓 재고 관리 - 동시 구매로 인한 overselling 문제
 * 2. 서버 리소스 관리 - 무제한 접속으로 인한 서버 다운
 * 3. 시스템 동기화 - 모든 서버가 준비된 후 티켓팅 시작
 * 
 * 해결 도구들:
 * - AtomicInteger: 안전한 티켓 재고 관리
 * - Semaphore: 서버 동시 접속자 수 제한
 * - CountDownLatch: 모든 서버 준비 완료 후 티켓팅 시작
 */
public class Thread_Ex_Task10_RealWorldTicketingSystem {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("🎵 BTS 월드투어 티켓팅 시스템 시작! 🎵");
        System.out.println("개발자: 찰리 (신입 개발자의 성장 스토리)");
        System.out.println("=".repeat(60));
        
        // 찰리의 여정 시작
        step1_CharliesFirstAttempt();
        step2_CharliesAtomicSolution();
        step3_CharliesSemaphoreSolution();
        step4_CharliesCountDownLatchSolution();
        step5_CharliesFinalSystem();
    }
    
    // ============================================================================
    // 1단계: 찰리의 첫 번째 시도 (문제 발생)
    // ============================================================================
    private static void step1_CharliesFirstAttempt() throws InterruptedException {
        System.out.println("\n🚨 1단계: 찰리의 첫 번째 시도 (재앙의 시작)");
        System.out.println("찰리: '간단하게 int 변수로 티켓 수를 관리하면 되겠지?'");
        
        class BrokenTicketSystem {
            private int availableTickets = 100; // 위험한 일반 변수
            private int soldTickets = 0;
            
            public boolean buyTicket(int userId) {
                if (availableTickets > 0) {
                    // 💥 Race Condition 발생 지점!
                    try { Thread.sleep(1); } catch (InterruptedException e) {}
                    availableTickets--;
                    soldTickets++;
                    System.out.println("✅ User-" + userId + " 티켓 구매 성공! 남은 티켓: " + availableTickets);
                    return true;
                } else {
                    System.out.println("❌ User-" + userId + " 티켓 매진!");
                    return false;
                }
            }
            
            public void printResult() {
                System.out.println("🎫 판매된 티켓: " + soldTickets);
                System.out.println("🎫 남은 티켓: " + availableTickets);
                System.out.println("⚠️  문제: " + (soldTickets + availableTickets != 100 ? "재고 불일치!" : "정상"));
            }
        }
        
        BrokenTicketSystem brokenSystem = new BrokenTicketSystem();
        Thread[] buyers = new Thread[50]; // 50명의 구매자
        
        for (int i = 0; i < 50; i++) {
            final int userId = i;
            buyers[i] = new Thread(() -> brokenSystem.buyTicket(userId));
        }
        
        // 동시 구매 시작
        for (Thread buyer : buyers) buyer.start();
        for (Thread buyer : buyers) buyer.join();
        
        brokenSystem.printResult();
        System.out.println("찰리: '어? 티켓이 100장보다 더 팔렸어... 이게 뭐지?'");
    }
    
    // ============================================================================
    // 2단계: 찰리의 AtomicInteger 해결책
    // ============================================================================
    private static void step2_CharliesAtomicSolution() throws InterruptedException {
        System.out.println("\n⚡ 2단계: 찰리의 AtomicInteger 해결책");
        System.out.println("시니어 개발자: '찰리, AtomicInteger를 써봐. 원자적 연산이 보장돼.'");
        System.out.println("찰리: '원자적이요? 아하! 더 이상 쪼갤 수 없는 연산이군요!'");
        
        class AtomicTicketSystem {
            private final AtomicInteger availableTickets = new AtomicInteger(100);
            private final AtomicInteger soldTickets = new AtomicInteger(0);
            private final AtomicInteger totalRevenue = new AtomicInteger(0);
            
            public boolean buyTicket(int userId, int price) {
                // CAS (Compare-And-Swap) 기반 안전한 감소
                int currentTickets = availableTickets.get();
                while (currentTickets > 0) {
                    if (availableTickets.compareAndSet(currentTickets, currentTickets - 1)) {
                        // 성공적으로 티켓 감소
                        int sold = soldTickets.incrementAndGet();
                        int revenue = totalRevenue.addAndGet(price);
                        
                        System.out.println("✅ User-" + userId + " 티켓 구매 성공! " +
                                         "남은 티켓: " + availableTickets.get() + 
                                         ", 총 판매: " + sold + "장, 매출: " + revenue + "원");
                        return true;
                    }
                    currentTickets = availableTickets.get(); // 재시도
                }
                
                System.out.println("❌ User-" + userId + " 티켓 매진!");
                return false;
            }
            
            public void printResult() {
                System.out.println("🎫 판매된 티켓: " + soldTickets.get());
                System.out.println("🎫 남은 티켓: " + availableTickets.get());
                System.out.println("💰 총 매출: " + totalRevenue.get() + "원");
                System.out.println("✅ 재고 일치: " + (soldTickets.get() + availableTickets.get() == 100));
            }
        }
        
        AtomicTicketSystem atomicSystem = new AtomicTicketSystem();
        Thread[] buyers = new Thread[50];
        
        for (int i = 0; i < 50; i++) {
            final int userId = i;
            buyers[i] = new Thread(() -> atomicSystem.buyTicket(userId, 150000)); // 15만원 티켓
        }
        
        for (Thread buyer : buyers) buyer.start();
        for (Thread buyer : buyers) buyer.join();
        
        atomicSystem.printResult();
        System.out.println("찰리: '와! 이제 티켓 재고가 정확해졌어요!'");
    }
    
    // ============================================================================
    // 3단계: 찰리의 Semaphore 해결책 (서버 리소스 관리)
    // ============================================================================
    private static void step3_CharliesSemaphoreSolution() throws InterruptedException {
        System.out.println("\n🎫 3단계: 찰리의 Semaphore 해결책 (서버 리소스 관리)");
        System.out.println("시스템 관리자: '찰리, 서버가 다운됐어! 동시 접속자가 너무 많아!'");
        System.out.println("찰리: '아! 서버 용량을 제한해야겠군요. Semaphore를 써볼게요!'");
        
        class ServerManagedTicketSystem {
            private final AtomicInteger availableTickets = new AtomicInteger(30);
            private final AtomicInteger soldTickets = new AtomicInteger(0);
            private final Semaphore serverCapacity = new Semaphore(10); // 동시 10명만 처리
            private final AtomicInteger currentUsers = new AtomicInteger(0);
            private final AtomicInteger rejectedUsers = new AtomicInteger(0);
            
            public boolean buyTicket(int userId) {
                System.out.println("🔄 User-" + userId + " 서버 접속 시도...");
                
                try {
                    // 서버 용량 확인 (논블로킹)
                    if (serverCapacity.tryAcquire(2, TimeUnit.SECONDS)) {
                        int current = currentUsers.incrementAndGet();
                        System.out.println("🟢 User-" + userId + " 서버 접속 성공! (현재 사용자: " + current + "/10)");
                        
                        try {
                            // 티켓 구매 처리 시뮬레이션
                            Thread.sleep(new Random().nextInt(1000) + 500); // 0.5~1.5초
                            
                            return processPurchase(userId);
                            
                        } finally {
                            currentUsers.decrementAndGet();
                            serverCapacity.release(); // 서버 리소스 해제
                            System.out.println("🔴 User-" + userId + " 서버 접속 해제");
                        }
                    } else {
                        rejectedUsers.incrementAndGet();
                        System.out.println("⏰ User-" + userId + " 서버 대기 시간 초과 (접속 포기)");
                        return false;
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }
            
            private boolean processPurchase(int userId) {
                int currentTickets = availableTickets.get();
                while (currentTickets > 0) {
                    if (availableTickets.compareAndSet(currentTickets, currentTickets - 1)) {
                        soldTickets.incrementAndGet();
                        System.out.println("✅ User-" + userId + " 티켓 구매 성공! 남은 티켓: " + availableTickets.get());
                        return true;
                    }
                    currentTickets = availableTickets.get();
                }
                
                System.out.println("❌ User-" + userId + " 티켓 매진!");
                return false;
            }
            
            public void printResult() {
                System.out.println("🎫 판매된 티켓: " + soldTickets.get());
                System.out.println("🎫 남은 티켓: " + availableTickets.get());
                System.out.println("🚫 접속 거부된 사용자: " + rejectedUsers.get());
                System.out.println("🖥️  서버 안정성: " + (rejectedUsers.get() > 0 ? "서버 보호됨" : "모든 사용자 처리"));
            }
        }
        
        ServerManagedTicketSystem serverSystem = new ServerManagedTicketSystem();
        Thread[] buyers = new Thread[30]; // 30명이 동시 접속 시도
        
        for (int i = 0; i < 30; i++) {
            final int userId = i;
            buyers[i] = new Thread(() -> serverSystem.buyTicket(userId));
        }
        
        for (Thread buyer : buyers) buyer.start();
        for (Thread buyer : buyers) buyer.join();
        
        serverSystem.printResult();
        System.out.println("찰리: '서버가 안정적으로 운영되네요! Semaphore로 리소스 관리 완벽!'");
    }
    
    // ============================================================================
    // 4단계: 찰리의 CountDownLatch 해결책 (시스템 동기화)
    // ============================================================================
    private static void step4_CharliesCountDownLatchSolution() throws InterruptedException {
        System.out.println("\n🚦 4단계: 찰리의 CountDownLatch 해결책 (시스템 동기화)");
        System.out.println("프로젝트 매니저: '찰리, 5개 서버가 모두 준비된 후에 티켓팅을 시작해야 해!'");
        System.out.println("찰리: '동시 시작이 중요하군요! CountDownLatch로 해결해볼게요!'");
        
        class SynchronizedTicketSystem {
            private final AtomicInteger availableTickets = new AtomicInteger(20);
            private final CountDownLatch serverReadySignal = new CountDownLatch(5); // 5개 서버 준비 대기
            private final CountDownLatch ticketingStartSignal = new CountDownLatch(1); // 티켓팅 시작 신호
            private final AtomicInteger soldTickets = new AtomicInteger(0);
            
            // 서버 초기화 시뮬레이션
            public void initializeServer(int serverId) {
                new Thread(() -> {
                    try {
                        System.out.println("🔧 Server-" + serverId + " 초기화 시작...");
                        Thread.sleep(new Random().nextInt(2000) + 1000); // 1~3초 초기화 시간
                        System.out.println("✅ Server-" + serverId + " 준비 완료!");
                        
                        serverReadySignal.countDown(); // 준비 완료 신호
                        
                        // 모든 서버 준비 완료 대기
                        System.out.println("⏳ Server-" + serverId + " 티켓팅 시작 신호 대기...");
                        ticketingStartSignal.await();
                        
                        System.out.println("🚀 Server-" + serverId + " 티켓팅 서비스 시작!");
                        
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }).start();
            }
            
            public boolean buyTicket(int userId) {
                try {
                    // 티켓팅 시작까지 대기
                    ticketingStartSignal.await();
                    
                    int currentTickets = availableTickets.get();
                    while (currentTickets > 0) {
                        if (availableTickets.compareAndSet(currentTickets, currentTickets - 1)) {
                            soldTickets.incrementAndGet();
                            System.out.println("✅ User-" + userId + " 티켓 구매 성공! 남은 티켓: " + availableTickets.get());
                            return true;
                        }
                        currentTickets = availableTickets.get();
                    }
                    
                    System.out.println("❌ User-" + userId + " 티켓 매진!");
                    return false;
                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }
            
            public void startTicketing() throws InterruptedException {
                // 5개 서버 초기화
                for (int i = 1; i <= 5; i++) {
                    initializeServer(i);
                }
                
                // 모든 서버 준비 완료 대기
                System.out.println("⏳ 모든 서버 준비 완료 대기 중...");
                serverReadySignal.await();
                
                System.out.println("🎉 모든 서버 준비 완료! 티켓팅 시작!");
                ticketingStartSignal.countDown(); // 티켓팅 시작 신호 발송
            }
            
            public void printResult() {
                System.out.println("🎫 판매된 티켓: " + soldTickets.get());
                System.out.println("🎫 남은 티켓: " + availableTickets.get());
                System.out.println("⚡ 동기화 상태: " + (serverReadySignal.getCount() == 0 ? "완료" : "대기 중"));
            }
        }
        
        SynchronizedTicketSystem syncSystem = new SynchronizedTicketSystem();
        
        // 티켓팅 시스템 시작
        syncSystem.startTicketing();
        
        // 구매자들 대기
        Thread[] buyers = new Thread[20];
        for (int i = 0; i < 20; i++) {
            final int userId = i;
            buyers[i] = new Thread(() -> syncSystem.buyTicket(userId));
        }
        
        for (Thread buyer : buyers) buyer.start();
        for (Thread buyer : buyers) buyer.join();
        
        syncSystem.printResult();
        System.out.println("찰리: '완벽해요! 모든 서버가 동시에 시작했어요! CountDownLatch 최고!'");
    }
    
    // ============================================================================
    // 5단계: 찰리의 최종 통합 시스템
    // ============================================================================
    private static void step5_CharliesFinalSystem() throws InterruptedException {
        System.out.println("\n🏆 5단계: 찰리의 최종 통합 시스템 (모든 기술 결합)");
        System.out.println("CTO: '찰리, 이제 모든 기술을 합쳐서 완벽한 시스템을 만들어봐!'");
        System.out.println("찰리: '네! AtomicInteger + Semaphore + CountDownLatch = 완벽한 시스템!'");
        
        class UltimateTicketingSystem {
            // AtomicInteger: 안전한 재고 관리
            private final AtomicInteger availableTickets = new AtomicInteger(20);
            private final AtomicInteger soldTickets = new AtomicInteger(0);
            private final AtomicInteger totalRevenue = new AtomicInteger(0);
            
            // Semaphore: 서버 리소스 관리
            private final Semaphore serverCapacity = new Semaphore(5);
            private final AtomicInteger rejectedUsers = new AtomicInteger(0);
            
            // CountDownLatch: 시스템 동기화
            private final CountDownLatch systemReady = new CountDownLatch(3); // DB, Cache, API 서버
            private final CountDownLatch ticketingStart = new CountDownLatch(1);
            
            public void initializeSystem() {
                // 시스템 컴포넌트 초기화
                initializeComponent("Database", 1500);
                initializeComponent("Cache", 1000);
                initializeComponent("API Server", 2000);
                
                new Thread(() -> {
                    try {
                        systemReady.await();
                        System.out.println("🎉 시스템 초기화 완료! 티켓팅 오픈!");
                        ticketingStart.countDown();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }).start();
            }
            
            private void initializeComponent(String componentName, int initTime) {
                new Thread(() -> {
                    try {
                        System.out.println("🔧 " + componentName + " 초기화 중...");
                        Thread.sleep(initTime);
                        System.out.println("✅ " + componentName + " 준비 완료!");
                        systemReady.countDown();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }).start();
            }
            
            public boolean buyTicket(int userId) {
                try {
                    // 1. 시스템 준비 대기 (CountDownLatch)
                    ticketingStart.await();
                    
                    // 2. 서버 리소스 획득 (Semaphore)
                    if (serverCapacity.tryAcquire(3, TimeUnit.SECONDS)) {
                        try {
                            System.out.println("🟢 User-" + userId + " 서버 접속 성공!");
                            
                            // 구매 처리 시뮬레이션
                            Thread.sleep(new Random().nextInt(500) + 200);
                            
                            // 3. 안전한 티켓 구매 (AtomicInteger)
                            return processPurchase(userId);
                            
                        } finally {
                            serverCapacity.release();
                            System.out.println("🔴 User-" + userId + " 서버 접속 해제");
                        }
                    } else {
                        rejectedUsers.incrementAndGet();
                        System.out.println("⏰ User-" + userId + " 서버 접속 실패 (대기 시간 초과)");
                        return false;
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }
            
            private boolean processPurchase(int userId) {
                int currentTickets = availableTickets.get();
                while (currentTickets > 0) {
                    if (availableTickets.compareAndSet(currentTickets, currentTickets - 1)) {
                        soldTickets.incrementAndGet();
                        totalRevenue.addAndGet(200000); // 20만원 티켓
                        System.out.println("🎫 User-" + userId + " 티켓 구매 성공! 남은 티켓: " + availableTickets.get());
                        return true;
                    }
                    currentTickets = availableTickets.get();
                }
                
                System.out.println("❌ User-" + userId + " 티켓 매진!");
                return false;
            }
            
            public void printFinalResult() {
                System.out.println("\n" + "=".repeat(50));
                System.out.println("🏆 찰리의 최종 티켓팅 시스템 결과");
                System.out.println("=".repeat(50));
                System.out.println("🎫 판매된 티켓: " + soldTickets.get() + "장");
                System.out.println("🎫 남은 티켓: " + availableTickets.get() + "장");
                System.out.println("💰 총 매출: " + String.format("%,d", totalRevenue.get()) + "원");
                System.out.println("🚫 접속 거부: " + rejectedUsers.get() + "명");
                System.out.println("✅ 재고 정확성: " + (soldTickets.get() + availableTickets.get() == 20 ? "완벽" : "오류"));
                System.out.println("🖥️  서버 안정성: " + (rejectedUsers.get() >= 0 ? "안정적" : "불안정"));
                System.out.println("⚡ 시스템 동기화: " + (systemReady.getCount() == 0 ? "완료" : "미완료"));
            }
        }
        
        UltimateTicketingSystem ultimateSystem = new UltimateTicketingSystem();
        
        // 시스템 초기화
        ultimateSystem.initializeSystem();
        
        // 구매자들 생성 (25명이 20장 티켓에 도전)
        Thread[] buyers = new Thread[25];
        for (int i = 0; i < 25; i++) {
            final int userId = i + 1;
            buyers[i] = new Thread(() -> ultimateSystem.buyTicket(userId));
        }
        
        // 모든 구매자 시작
        for (Thread buyer : buyers) buyer.start();
        for (Thread buyer : buyers) buyer.join();
        
        ultimateSystem.printFinalResult();
        
        System.out.println("\n🎉 찰리의 성장 완료!");
    }
} 