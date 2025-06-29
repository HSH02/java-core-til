package com.JavaCoreTil.thread.thread_example;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 8단계: 동시성 컬렉션 실습
 * 일반 컬렉션의 문제점과 동시성 컬렉션의 해결책을 실제 코드로 확인해봅니다.
 */
public class Stage8_ConcurrentCollections {

    /**
     * 예제 1: HashMap vs ConcurrentHashMap
     * 동시 접근 시 HashMap의 위험성과 ConcurrentHashMap의 안전성을 비교합니다.
     */
    public static void example1_HashMapVsConcurrentHashMap() {
        System.out.println("=== 예제 1: HashMap vs ConcurrentHashMap ===");
        
        // 위험한 HashMap 사용
        testUnsafeHashMap();
        
        // 안전한 ConcurrentHashMap 사용
        testSafeConcurrentHashMap();
    }
    
    private static void testUnsafeHashMap() {
        System.out.println("\n[위험] HashMap 테스트:");
        Map<String, Integer> unsafeMap = new HashMap<>();
        
        // 10개 스레드가 동시에 1000번씩 put 연산
        CountDownLatch latch = new CountDownLatch(10);
        
        for (int i = 0; i < 10; i++) {
            final int threadId = i;
            new Thread(() -> {
                try {
                    for (int j = 0; j < 1000; j++) {
                        unsafeMap.put("thread" + threadId + "_key" + j, j);
                    }
                } catch (Exception e) {
                    System.out.println("HashMap에서 예외 발생: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            }).start();
        }
        
        try {
            latch.await();
            System.out.println("HashMap 최종 크기: " + unsafeMap.size() + " (예상: 10000)");
            // 데이터 손실이나 무한루프로 인한 크기 부정확
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private static void testSafeConcurrentHashMap() {
        System.out.println("\n[안전] ConcurrentHashMap 테스트:");
        Map<String, Integer> safeMap = new ConcurrentHashMap<>();
        
        CountDownLatch latch = new CountDownLatch(10);
        
        for (int i = 0; i < 10; i++) {
            final int threadId = i;
            new Thread(() -> {
                try {
                    for (int j = 0; j < 1000; j++) {
                        safeMap.put("thread" + threadId + "_key" + j, j);
                    }
                } finally {
                    latch.countDown();
                }
            }).start();
        }
        
        try {
            latch.await();
            System.out.println("ConcurrentHashMap 최종 크기: " + safeMap.size() + " (예상: 10000)");
            // 정확한 크기 보장
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 예제 2: BlockingQueue를 사용한 Producer-Consumer 패턴
     * 실제 메시지 처리 시스템을 시뮬레이션합니다.
     */
    public static void example2_BlockingQueueProducerConsumer() {
        System.out.println("\n=== 예제 2: BlockingQueue Producer-Consumer ===");
        
        BlockingQueue<Message> messageQueue = new LinkedBlockingQueue<>(10);
        AtomicInteger messageIdGenerator = new AtomicInteger(0);
        
        // Producer 스레드 - 메시지 생성
        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    Message message = new Message(
                        messageIdGenerator.incrementAndGet(),
                        "Message content " + i
                    );
                    
                    messageQueue.put(message); // 블로킹 방식으로 안전하게 추가
                    System.out.println("생산: " + message);
                    Thread.sleep(100); // 생산 속도 조절
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Producer");
        
        // Consumer 스레드 - 메시지 처리
        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    Message message = messageQueue.take(); // 블로킹 방식으로 대기
                    System.out.println("소비: " + message);
                    Thread.sleep(200); // 처리 시간 시뮬레이션
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Consumer");
        
        // 스레드 시작
        producer.start();
        consumer.start();
        
        try {
            producer.join();
            consumer.join();
            System.out.println("남은 메시지 수: " + messageQueue.size());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 예제 3: ConcurrentHashMap의 원자적 연산들
     */
    public static void example3_ConcurrentHashMapAtomicOperations() {
        System.out.println("\n=== 예제 3: ConcurrentHashMap 원자적 연산 ===");
        
        ConcurrentHashMap<String, AtomicInteger> counterMap = new ConcurrentHashMap<>();
        ExecutorService executor = Executors.newFixedThreadPool(5);
        CountDownLatch latch = new CountDownLatch(5);
        
        // 5개 스레드가 동시에 카운터 증가
        for (int i = 0; i < 5; i++) {
            final String threadName = "Thread-" + i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < 1000; j++) {
                        // 원자적 연산: 키가 없으면 새로 생성, 있으면 증가
                        counterMap.computeIfAbsent("counter", k -> new AtomicInteger(0))
                                 .incrementAndGet();
                        
                        // 스레드별 카운터도 관리
                        counterMap.computeIfAbsent(threadName, k -> new AtomicInteger(0))
                                 .incrementAndGet();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        
        try {
            latch.await();
            System.out.println("전체 카운터: " + counterMap.get("counter").get());
            counterMap.entrySet().stream()
                    .filter(entry -> entry.getKey().startsWith("Thread-"))
                    .forEach(entry -> 
                        System.out.println(entry.getKey() + ": " + entry.getValue().get()));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            executor.shutdown();
        }
    }

    // 헬퍼 클래스들
    static class Message {
        final int id;
        final String content;
        
        Message(int id, String content) {
            this.id = id;
            this.content = content;
        }
        
        @Override
        public String toString() {
            return String.format("Message{id=%d, content='%s'}", id, content);
        }
    }

    public static void main(String[] args) {
        System.out.println("동시성 컬렉션 실습을 시작합니다...\n");
        
        try {
            example1_HashMapVsConcurrentHashMap();
            Thread.sleep(1000);
            
            example2_BlockingQueueProducerConsumer();
            Thread.sleep(1000);
            
            example3_ConcurrentHashMapAtomicOperations();
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }
} 