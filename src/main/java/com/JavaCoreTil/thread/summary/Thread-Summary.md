# Java Thread 핵심 정리

Java Thread 프로그래밍에서 반드시 알아야 할 기초 개념들을 정리했습니다.

---

## 💡 Thread 학습 팁
- **단계별 학습**: 기본 개념 → 동시성 문제 → 동기화 → 고급 기법 순서로 학습
- **실습 중심**: 이론보다는 직접 코드를 작성하고 문제를 체험해보기
- **성능 측정**: System.currentTimeMillis()로 병렬 처리 효과 확인
- **로그 관찰**: Thread.currentThread().getName()으로 스레드 동작 추적

---

## 1. Thread 기본 개념

### Process와 Thread의 차이
- Process: 독립된 메모리 공간을 가진 실행 프로그램
- Thread: 하나의 프로세스 내에서 메모리를 공유하며 실행되는 작업 단위

### Thread 상태 변화
```
NEW → RUNNABLE → BLOCKED/WAITING/TIMED_WAITING → TERMINATED
```

### 메모리 영역
- **Stack 영역**: 각 Thread마다 독립적으로 할당 (지역변수, 메서드 호출 정보)
- **Heap 영역**: 모든 Thread가 공유 (객체, 인스턴스 변수)

---

## 2. Thread 생성과 실행

### 생성 방법
```java
// Thread 클래스 상속
class MyThread extends Thread {
    public void run() {
        System.out.println("Thread 실행");
    }
}

// Runnable 인터페이스 구현 (권장)
Thread thread = new Thread(() -> {
    System.out.println("Thread 실행");
});
thread.start();
```

### ⚠️ 실무 권장사항
- **Runnable 인터페이스 사용**: Thread 클래스 상속보다 유연함
- **의미있는 스레드 이름**: `new Thread(task, "작업명")` 형태로 디버깅 용이
- **예외 처리**: InterruptedException은 항상 적절히 처리

### 주요 메서드
```java
thread.start();        // Thread 시작
thread.join();         // Thread 완료까지 무한 대기
thread.join(1000);     // Thread 완료까지 최대 1초 대기
Thread.sleep(1000);    // 현재 Thread를 1초 정지
thread.interrupt();    // Thread 중단 요청
```

### join()과 sleep() 차이점
```java
// join() - 다른 Thread의 완료를 기다림
Thread worker = new Thread(() -> {
    // 3초 걸리는 작업
    try { Thread.sleep(3000); } catch (InterruptedException e) {}
    System.out.println("작업 완료");
});

worker.start();
worker.join(1000);  // worker가 끝나거나 1초가 지날 때까지 대기
System.out.println("메인 계속");  // 1초 후 실행 (작업이 끝나지 않아도)

// sleep() - 현재 Thread를 정지
Thread.sleep(1000);  // 현재 Thread가 무조건 1초 정지
System.out.println("1초 후 실행");  // 정확히 1초 후 실행
```

---

## 3. 메모리 가시성 문제

### 문제 상황
```java
class SharedData {
    private boolean running = true;  // 공유 변수
    
    public void stop() {
        running = false;  // Thread A에서 변경
    }
    
    public void work() {
        while (running) {  // Thread B에서 확인
            // 변경사항을 못 볼 수 있음
        }
    }
}
```

### volatile로 해결
```java
private volatile boolean running = true;  // 가시성 보장
```

### volatile의 한계
- 가시성은 보장하지만 원자성은 보장하지 않음
- `count++` 같은 복합 연산에는 사용 불가

---

## 4. 동시성 문제와 해결

### Race Condition
여러 Thread가 동시에 같은 데이터를 수정할 때 발생하는 문제

```java
class Counter {
    private int count = 0;
    
    public void increment() {
        count++;  // 원자적이지 않은 연산
    }
}
```

### synchronized로 해결
```java
// 메서드 전체 동기화
public synchronized void increment() {
    count++;
}

// 블록 동기화
public void increment() {
    synchronized(this) {
        count++;
    }
}

// 클래스 레벨 동기화
public static synchronized void staticMethod() {
    // 클래스 단위로 동기화
}
```

### Deadlock 방지
```java
// 위험한 코드 - 락 순서가 다름
public void method1() {
    synchronized(lockA) {
        synchronized(lockB) { /* 작업 */ }
    }
}

public void method2() {
    synchronized(lockB) {
        synchronized(lockA) { /* 작업 */ }
    }
}

// 안전한 코드 - 락 순서 통일
public void method1() {
    synchronized(lockA) {
        synchronized(lockB) { /* 작업 */ }
    }
}

public void method2() {
    synchronized(lockA) {  // 같은 순서
        synchronized(lockB) { /* 작업 */ }
    }
}
```

---

## 5. Thread 간 협력과 통신

### wait/notify 메커니즘
Thread 간 협력을 위한 기본 메커니즘

```java
class SharedBuffer {
    private final Object lock = new Object();
    private boolean dataReady = false;
    private String data;
    
    // 데이터 대기
    public String consume() throws InterruptedException {
        synchronized (lock) {
            while (!dataReady) {  // while 루프 필수 (Spurious Wakeup 방지)
                lock.wait();  // 데이터가 준비될 때까지 대기
            }
            dataReady = false;
            return data;
        }
    }
    
    // 데이터 생산
    public void produce(String newData) {
        synchronized (lock) {
            data = newData;
            dataReady = true;
            lock.notify();  // 대기 중인 Thread 깨우기
        }
    }
}
```

### Producer-Consumer 패턴
```java
class BoundedBuffer<T> {
    private final Queue<T> buffer = new LinkedList<>();
    private final int capacity;
    private final Object lock = new Object();
    
    public BoundedBuffer(int capacity) {
        this.capacity = capacity;
    }
    
    // Producer - 데이터 추가
    public void put(T item) throws InterruptedException {
        synchronized (lock) {
            while (buffer.size() >= capacity) {
                lock.wait();  // 버퍼가 가득 찰 때까지 대기
            }
            buffer.offer(item);
            lock.notifyAll();  // Consumer에게 알림
        }
    }
    
    // Consumer - 데이터 소비
    public T take() throws InterruptedException {
        synchronized (lock) {
            while (buffer.isEmpty()) {
                lock.wait();  // 데이터가 있을 때까지 대기
            }
            T item = buffer.poll();
            lock.notifyAll();  // Producer에게 알림
            return item;
        }
    }
}
```

### 객체 락과 모니터
- **Entry Set**: 락을 얻기 위해 대기하는 Thread들
- **Wait Set**: wait() 호출로 대기 중인 Thread들
- **Monitor**: 락과 Wait Set을 관리하는 동기화 메커니즘

```java
// 모니터 동작 과정
synchronized (obj) {           // Entry Set에서 대기 → 락 획득
    while (!condition) {
        obj.wait();            // Wait Set으로 이동
    }
    // 조건 만족 시 실행
    obj.notify();              // Wait Set의 Thread 하나를 Entry Set으로 이동
}
```

### Spurious Wakeup 대응
가짜 깨어남을 방지하기 위해 반드시 while 루프 사용

```java
// 잘못된 코드 - if 사용
synchronized (lock) {
    if (!condition) {
        lock.wait();  // 가짜 깨어남 시 조건 재확인 안 함
    }
    // 위험한 실행
}

// 올바른 코드 - while 사용
synchronized (lock) {
    while (!condition) {
        lock.wait();  // 깨어날 때마다 조건 재확인
    }
    // 안전한 실행
}
```

### wait/notify vs notifyAll
```java
// notify() - 하나의 Thread만 깨움
lock.notify();    // 특정 Thread 하나만 선택 (예측 불가)

// notifyAll() - 모든 대기 Thread 깨움
lock.notifyAll(); // 모든 대기 Thread가 깨어나서 조건 재확인 (안전)
```

---

## 6. 고급 동기화 도구

### Lock 인터페이스와 ReentrantLock
synchronized의 한계를 극복하는 명시적 락 제어

```java
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;

class BankAccount {
    private int balance = 1000;
    private final Lock lock = new ReentrantLock();
    
    // 기본 락 사용
    public boolean withdraw(int amount) {
        lock.lock();  // 명시적 락 획득
        try {
            if (balance >= amount) {
                balance -= amount;
                return true;
            }
            return false;
        } finally {
            lock.unlock();  // 반드시 finally에서 해제
        }
    }
    
    // 타임아웃이 있는 락
    public boolean withdrawWithTimeout(int amount) {
        try {
            if (lock.tryLock(3, TimeUnit.SECONDS)) {  // 3초 대기
                try {
                    if (balance >= amount) {
                        balance -= amount;
                        return true;
                    }
                    return false;
                } finally {
                    lock.unlock();
                }
            }
            return false;  // 타임아웃
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
    
    // 인터럽트 응답 가능한 락
    public boolean withdrawInterruptibly(int amount) throws InterruptedException {
        lock.lockInterruptibly();  // 인터럽트 즉시 응답
        try {
            if (balance >= amount) {
                balance -= amount;
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }
}
```

### Lock vs synchronized 비교

| 특징 | synchronized | Lock |
|------|-------------|------|
| 락 해제 | 자동 (블록 종료 시) | 수동 (finally 필수) |
| 타임아웃 | 불가능 | `tryLock(time, unit)` |
| 인터럽트 응답 | 제한적 | `lockInterruptibly()` |
| 조건별 대기 | 불가능 | `Condition` 객체 |
| 공정성 제어 | 불가능 | `new ReentrantLock(true)` |
| 성능 | 락 경합 적을 때 빠름 | 락 경합 많을 때 빠름 |

### Lock Condition을 활용한 Producer-Consumer
조건별 대기를 통한 정교한 제어

```java
import java.util.concurrent.locks.Condition;

class BoundedQueue<T> {
    private final Object[] buffer;
    private int count = 0, in = 0, out = 0;
    private final Lock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();  // 소비자용 조건
    private final Condition notFull = lock.newCondition();   // 생산자용 조건
    
    public BoundedQueue(int capacity) {
        buffer = new Object[capacity];
    }
    
    // Producer - 데이터 추가
    @SuppressWarnings("unchecked")
    public void put(T item) throws InterruptedException {
        lock.lock();
        try {
            while (count == buffer.length) {
                notFull.await();  // 버퍼가 가득 찬 동안 대기
            }
            buffer[in] = item;
            in = (in + 1) % buffer.length;  // 순환 버퍼
            count++;
            notEmpty.signal();  // 소비자에게 알림
        } finally {
            lock.unlock();
        }
    }
    
    // Consumer - 데이터 소비
    @SuppressWarnings("unchecked")
    public T take() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0) {
                notEmpty.await();  // 버퍼가 빈 동안 대기
            }
            T item = (T) buffer[out];
            buffer[out] = null;
            out = (out + 1) % buffer.length;  // 순환 버퍼
            count--;
            notFull.signal();  // 생산자에게 알림
            return item;
        } finally {
            lock.unlock();
        }
    }
}
```

### AtomicInteger - CAS 기반 원자적 연산
Lock 없이도 안전한 원자적 연산 제공

```java
import java.util.concurrent.atomic.AtomicInteger;

class Statistics {
    private final AtomicInteger totalVisits = new AtomicInteger(0);
    private final AtomicInteger uniqueUsers = new AtomicInteger(0);
    
    // 원자적 증가
    public void recordVisit() {
        int newCount = totalVisits.incrementAndGet();
        System.out.println("총 방문수: " + newCount);
    }
    
    // CAS 연산 활용
    public boolean addUniqueUser() {
        int current = uniqueUsers.get();
        int next = current + 1;
        // 현재 값이 예상값과 같으면 새 값으로 설정
        return uniqueUsers.compareAndSet(current, next);
    }
    
    // 원자적 업데이트
    public void updateStats(int visitDelta, int userDelta) {
        totalVisits.addAndGet(visitDelta);
        uniqueUsers.addAndGet(userDelta);
    }
    
    public void printStats() {
        System.out.printf("방문수: %d, 사용자: %d%n", 
            totalVisits.get(), uniqueUsers.get());
    }
}
```

### CountDownLatch - 일회용 동기화 지점
여러 Thread의 작업 완료를 기다리는 동기화 도구

```java
import java.util.concurrent.CountDownLatch;

class PaymentProcessor {
    public void processPayment(Payment payment) throws InterruptedException {
        final int BANK_COUNT = 5;
        CountDownLatch startSignal = new CountDownLatch(1);   // 시작 신호
        CountDownLatch doneSignal = new CountDownLatch(BANK_COUNT);  // 완료 신호
        AtomicInteger successCount = new AtomicInteger(0);
        
        // 5개 은행에서 병렬로 잔액 확인
        for (int i = 0; i < BANK_COUNT; i++) {
            final int bankId = i;
            new Thread(() -> {
                try {
                    startSignal.await();  // 시작 신호 대기
                    
                    boolean result = checkBalanceFromBank(bankId, payment);
                    if (result) {
                        successCount.incrementAndGet();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneSignal.countDown();  // 작업 완료 신호
                }
            }).start();
        }
        
        // 모든 Thread 동시 시작
        startSignal.countDown();
        
        // 모든 은행 응답 대기
        doneSignal.await();
        
        // 과반수 성공시 결제 진행
        if (successCount.get() >= 3) {
            processActualPayment(payment);
        }
    }
    
    private boolean checkBalanceFromBank(int bankId, Payment payment) {
        // 은행별 잔액 확인 로직
        return true;
    }
    
    private void processActualPayment(Payment payment) {
        // 실제 결제 처리
    }
}
```

### Semaphore - 리소스 접근 제한
동시에 접근할 수 있는 리소스 수를 제한하는 도구

```java
import java.util.concurrent.Semaphore;

class DatabaseConnectionPool {
    private final Semaphore semaphore;
    
    public DatabaseConnectionPool(int maxConnections) {
        this.semaphore = new Semaphore(maxConnections);  // 최대 연결 수 제한
    }
    
    // 기본 리소스 사용
    public void executeQuery(String query) throws InterruptedException {
        semaphore.acquire();  // 리소스 획득 (블로킹)
        try {
            System.out.println("쿼리 실행: " + query);
            Thread.sleep(1000);  // 쿼리 실행 시뮬레이션
        } finally {
            semaphore.release();  // 리소스 해제
        }
    }
    
    // 타임아웃이 있는 리소스 사용
    public boolean executeQueryWithTimeout(String query) throws InterruptedException {
        if (semaphore.tryAcquire(3, TimeUnit.SECONDS)) {  // 3초 대기
            try {
                System.out.println("쿼리 실행: " + query);
                Thread.sleep(1000);
                return true;
            } finally {
                semaphore.release();
            }
        }
        return false;  // 타임아웃
    }
    
    // 공정한 리소스 할당
    public static class FairConnectionPool {
        private final Semaphore semaphore = new Semaphore(3, true);  // 공정성 보장
        
        public void executeQuery(String query) throws InterruptedException {
            semaphore.acquire();  // FIFO 순서로 리소스 할당
            try {
                System.out.println("공정한 쿼리 실행: " + query);
                Thread.sleep(1000);
            } finally {
                semaphore.release();
            }
        }
    }
}
```

### 고급 동기화 도구 선택 가이드

| 상황 | 추천 도구 | 이유 |
|------|----------|------|
| 단순한 상호 배제 | `synchronized` | 간단하고 자동 해제 |
| 타임아웃이 필요한 락 | `ReentrantLock` | `tryLock()` 지원 |
| 조건별 대기가 필요 | `Lock + Condition` | 정교한 제어 가능 |
| 원자적 카운터 | `AtomicInteger` | Lock 없이 고성능 |
| 여러 작업 완료 대기 | `CountDownLatch` | 일회용 동기화 지점 |
| 리소스 수 제한 | `Semaphore` | 동시 접근 제어 |

### 성능 고려사항
```java
// 락 경합이 적은 경우: synchronized가 더 빠름 (JVM 최적화)
public synchronized void lightContention() {
    // 간단한 작업
}

// 락 경합이 심한 경우: Lock이 더 빠름 (CAS 기반)
private final Lock lock = new ReentrantLock();
public void heavyContention() {
    lock.lock();
    try {
        // 복잡한 작업
    } finally {
        lock.unlock();
    }
}

// 단순 카운터: AtomicInteger가 가장 빠름
private final AtomicInteger counter = new AtomicInteger(0);
public void increment() {
    counter.incrementAndGet();  // Lock 없이 원자적 연산
}
```

---

## 실무에서 주의할 점

### Thread 생성
- Thread 클래스 상속보다는 Runnable 인터페이스 구현을 권장
- Lambda 표현식을 활용하면 코드가 간결해짐

### 동기화
- 필요한 부분만 동기화해서 성능 저하 최소화
- 공유 데이터 접근 시 반드시 동기화 고려
- 데드락 방지를 위해 락 순서 일관성 유지

### 메모리 가시성
- 단순한 플래그 변수는 volatile 사용
- 복합 연산이 필요하면 synchronized 사용

### Thread 종료
- interrupt() 메서드로 안전하게 종료
- while 루프에서 interrupt 상태 확인

---
