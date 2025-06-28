package com.JavaCoreTil.thread.thread_example;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 5단계: wait/notify 메커니즘
 * - wait/notify 기본 개념, 생산자-소비자 패턴, notify vs notifyAll
 */
public class Stage5_WaitNotify {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 5단계: wait/notify 메커니즘 ===\n");
        
        demonstrateBasicWaitNotify();
        demonstrateProducerConsumer();
        demonstrateNotifyVsNotifyAll();
    }
    
    // wait/notify 기본 개념 시연
    static void demonstrateBasicWaitNotify() throws InterruptedException {
        System.out.println("wait/notify 기본 개념");
        
        class WaitNotifyExample {
            private final Object lock = new Object();
            private boolean ready = false;
            
            public void waitForSignal() {
                synchronized(lock) {
                    while (!ready) {
                        try {
                            System.out.println("  대기 중... wait() 호출");
                            lock.wait(); // 락 해제하고 대기
                            System.out.println("  신호 받음! wait()에서 깨어남");
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    System.out.println("  조건 만족, 작업 진행");
                }
            }
            
            public void sendSignal() {
                synchronized(lock) {
                    System.out.println("  조건 변경 및 신호 전송");
                    ready = true;
                    lock.notify(); // 대기 중인 스레드 깨우기
                }
            }
        }
        
        WaitNotifyExample example = new WaitNotifyExample();
        
        Thread waiter = new Thread(example::waitForSignal, "Waiter");
        Thread notifier = new Thread(example::sendSignal, "Notifier");
        
        waiter.start();
        Thread.sleep(1000);
        notifier.start();
        
        waiter.join();
        notifier.join();
        System.out.println();
    }
    
    // 생산자-소비자 패턴 시연
    static void demonstrateProducerConsumer() throws InterruptedException {
        System.out.println("생산자-소비자 패턴");
        
        class ProducerConsumerBuffer {
            private final Queue<Integer> buffer = new LinkedList<>();
            private final int capacity = 3;
            private final Object lock = new Object();
            
            public void produce(int item) {
                synchronized(lock) {
                    while (buffer.size() == capacity) {
                        try {
                            System.out.printf("  버퍼 가득참, 생산자 대기... (크기: %d)%n", buffer.size());
                            lock.wait(); // 버퍼에 공간이 생길 때까지 대기
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    
                    buffer.offer(item);
                    System.out.printf("  생산: %d (버퍼 크기: %d)%n", item, buffer.size());
                    lock.notifyAll(); // 소비자들에게 신호
                }
            }
            
            public Integer consume() {
                synchronized(lock) {
                    while (buffer.isEmpty()) {
                        try {
                            System.out.println("  버퍼 비어있음, 소비자 대기...");
                            lock.wait(); // 버퍼에 데이터가 들어올 때까지 대기
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return null;
                        }
                    }
                    
                    Integer item = buffer.poll();
                    System.out.printf("  소비: %d (버퍼 크기: %d)%n", item, buffer.size());
                    lock.notifyAll(); // 생산자들에게 신호
                    return item;
                }
            }
        }
        
        ProducerConsumerBuffer buffer = new ProducerConsumerBuffer();
        
        // 생산자 스레드
        Thread producer = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                buffer.produce(i);
                try { Thread.sleep(500); } catch (InterruptedException e) {}
            }
        }, "Producer");
        
        // 소비자 스레드
        Thread consumer = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                buffer.consume();
                try { Thread.sleep(800); } catch (InterruptedException e) {}
            }
        }, "Consumer");
        
        producer.start();
        consumer.start();
        
        producer.join();
        consumer.join();
        System.out.println();
    }
    
    // notify vs notifyAll 차이 시연
    static void demonstrateNotifyVsNotifyAll() throws InterruptedException {
        System.out.println("notify vs notifyAll 차이");
        
        class NotifyExample {
            private final Object lock = new Object();
            private boolean condition = false;
            
            public void waitForCondition(String name) {
                synchronized(lock) {
                    while (!condition) {
                        try {
                            System.out.printf("  %s 대기 시작%n", name);
                            lock.wait();
                            System.out.printf("  %s 깨어남%n", name);
                        } catch (InterruptedException e) {
                            System.out.printf("  %s 인터럽트로 종료%n", name);
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    System.out.printf("  %s 작업 완료%n", name);
                }
            }
            
            public void notifyOne() {
                synchronized(lock) {
                    System.out.println("  notify() - 하나의 스레드만 깨움");
                    condition = true;
                    lock.notify(); // 하나의 스레드만 깨움
                }
            }
            
            public void notifyAllThreads() {
                synchronized(lock) {
                    System.out.println("  notifyAll() - 모든 스레드 깨움");
                    condition = true;
                    lock.notifyAll(); // 모든 대기 스레드 깨움
                }
            }
            
            public void reset() {
                synchronized(lock) {
                    condition = false;
                }
            }
        }
        
        NotifyExample example = new NotifyExample();
        
        // notify() 테스트
        System.out.println("  notify() 테스트:");
        Thread[] waiters1 = new Thread[3];
        for (int i = 0; i < 3; i++) {
            final String name = "Waiter-" + (i + 1);
            waiters1[i] = new Thread(() -> example.waitForCondition(name), name);
            waiters1[i].start();
        }
        
        Thread.sleep(1000); // 모든 스레드가 대기 상태에 들어갈 시간
        example.notifyOne(); // 하나만 깨움
        
        // 나머지 스레드들을 안전하게 종료
        Thread.sleep(1000); // 깨어난 스레드가 작업을 완료할 시간
        for (Thread t : waiters1) {
            if (t.isAlive()) {
                System.out.printf("  %s 여전히 대기 중 - 강제 종료%n", t.getName());
                t.interrupt();
            }
        }
        
        // 모든 스레드 종료 대기
        for (Thread t : waiters1) {
            t.join(1000);
        }
        
        // notifyAll() 테스트
        System.out.println("\n  notifyAll() 테스트:");
        example.reset(); // condition을 false로 재설정
        
        Thread[] waiters2 = new Thread[3];
        for (int i = 0; i < 3; i++) {
            final String name = "Waiter-" + (i + 4);
            waiters2[i] = new Thread(() -> example.waitForCondition(name), name);
            waiters2[i].start();
        }
        
        Thread.sleep(1000); // 모든 스레드가 대기 상태에 들어갈 시간
        example.notifyAllThreads(); // 모두 깨움
        
        // 모든 스레드 완료 대기
        for (Thread t : waiters2) {
            t.join(3000); // 3초 대기
            if (t.isAlive()) {
                System.out.printf("  %s 타임아웃으로 강제 종료%n", t.getName());
                t.interrupt();
            }
        }
        
        System.out.println("  notify()는 하나만, notifyAll()은 모든 스레드를 깨움");
        System.out.println();
    }
} 