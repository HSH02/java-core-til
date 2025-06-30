# Java Thread 핵심 정리

Java Thread 프로그래밍에서 반드시 알아야 할 기초적이고 실용적인 개념들을 정리한 문서입니다.

---

## 1. Thread 기본 개념

### 핵심 개념
- **Process**: 독립된 메모리 공간을 가진 실행 프로그램
- **Thread**: 프로세스 내에서 메모리를 공유하며 실행되는 작업 단위

### Thread 생명주기
```
NEW → RUNNABLE → BLOCKED/WAITING/TIMED_WAITING → TERMINATED
```

### 메모리 구조
| 영역 | 공유 여부 | 용도 |
|------|-----------|------|
| Stack | Thread별 독립 | 지역변수, 메서드 호출 정보 |
| Heap | 모든 Thread 공유 | 객체, 인스턴스 변수 |

---

## 2. Thread 생성과 제어

### 생성 방법
```java
// 방법 1: Thread 클래스 상속
class MyThread extends Thread {
    public void run() {
        System.out.println("Thread 실행");
    }
}

// 방법 2: Runnable 인터페이스 구현 (권장)
Thread thread = new Thread(() -> {
    System.out.println("Thread 실행");
});
```

### 주요 메서드
```java
thread.start();               // Thread 시작
thread.join();                // Thread 완료까지 대기
thread.join(1000);            // 최대 1초간 대기
Thread.sleep(1000);           // 현재 Thread 1초 정지
thread.interrupt();           // Thread 중단 요청
thread.isInterrupted();       // 중단 요청 상태 확인
Thread.yield();               // CPU 사용권 양보
```

### Thread 속성 관리
```java
// Thread 이름 설정
Thread thread = new Thread(task, "WorkerThread");  // 생성시 이름 지정
thread.setName("CustomName");                      // 이름 변경
String name = thread.getName();                    // 이름 조회

// 우선순위 설정 (1-10, 기본값 5)
thread.setPriority(Thread.MAX_PRIORITY);          // 최고 우선순위 (10)
thread.setPriority(Thread.NORM_PRIORITY);         // 기본 우선순위 (5)
thread.setPriority(Thread.MIN_PRIORITY);          // 최저 우선순위 (1)

// 데몬 스레드 설정
thread.setDaemon(true);                           // 데몬 스레드로 설정
boolean isDaemon = thread.isDaemon();             // 데몬 여부 확인
```

### 데몬 스레드 특징
| 구분 | 일반 스레드 | 데몬 스레드 |
|------|-------------|-------------|
| JVM 종료 | 모든 일반 스레드 완료 시 | JVM과 함께 강제 종료 |
| 용도 | 주요 작업 수행 | 백그라운드 서비스 |
| 예시 | 메인 스레드, 사용자 작업 | GC, 로그 정리, 모니터링 |

### 메서드 비교
| 메서드 | 대상 | 동작 | 용도 |
|--------|------|------|------|
| `join()` | 다른 Thread | 완료까지 대기 | 작업 순서 보장 |
| `sleep()` | 현재 Thread | 지정 시간 정지 | 일시 정지 |
| `yield()` | 현재 Thread | CPU 사용권 양보 | 스케줄링 조정 |

### 안전한 Thread 종료 패턴
```java
class SafeWorkerThread extends Thread {
    private volatile boolean running = true;
    
    public SafeWorkerThread() {
        super("SafeWorker");        // 스레드 이름 설정
        setDaemon(false);           // 일반 스레드로 설정
    }
    
    @Override
    public void run() {
        while (running && !isInterrupted()) {
            try {
                doWork();           // 주요 작업 수행
                Thread.sleep(1000); // 주기적 실행
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // 인터럽트 상태 복원
                break;                              // 루프 종료
            }
        }
        cleanup();  // 리소스 정리
    }
    
    public void stopSafely() {
        running = false;    // volatile 플래그 변경
        interrupt();        // 인터럽트 신호 전송
    }
    
    private void doWork() {
        System.out.println(getName() + " 작업 수행 중...");
    }
    
    private void cleanup() {
        System.out.println(getName() + " 정리 완료");
    }
}

// 사용 예시
SafeWorkerThread worker = new SafeWorkerThread();
worker.start();
// 작업 완료 후
worker.stopSafely();
worker.join();  // 안전한 종료 대기
```

### 데몬 스레드 활용 예시
```java
// 로그 정리 데몬 스레드
Thread logCleaner = new Thread(() -> {
    while (true) {
        cleanOldLogs();
        try {
            Thread.sleep(60000);  // 1분 주기
        } catch (InterruptedException e) {
            break;
        }
    }
}, "LogCleaner");

logCleaner.setDaemon(true);     // 데몬 스레드로 설정
logCleaner.start();             // JVM 종료 시 자동 종료
```

---

## 3. 메모리 가시성 문제

### 문제 발생 원인
Thread별 CPU 캐시로 인해 메모리 변경사항이 다른 Thread에게 즉시 보이지 않음

### 해결 방법
```java
// 문제가 있는 코드
private boolean running = true;

// 해결된 코드
private volatile boolean running = true;  // 가시성 보장
```

### volatile 특징
- **장점**: 메모리 가시성 보장
- **한계**: 원자성 보장 안 함 (복합 연산 불가)

---

## 4. 동시성 문제와 동기화

### Race Condition 문제
```java
// 문제 코드
class Counter {
    private int count = 0;
    public void increment() {
        count++;  // 원자적이지 않음
    }
}
```

### synchronized 해결책
```java
// 메서드 동기화
public synchronized void increment() {
    count++;
}

// 블록 동기화
public void increment() {
    synchronized(this) {
        count++;
    }
}
```

### Deadlock 방지
```java
// 위험: 서로 다른 락 순서
synchronized(lockA) { synchronized(lockB) { ... } }
synchronized(lockB) { synchronized(lockA) { ... } }

// 안전: 동일한 락 순서
synchronized(lockA) { synchronized(lockB) { ... } }
synchronized(lockA) { synchronized(lockB) { ... } }
```

---

## 5. Thread 간 통신

### wait/notify 메커니즘
```java
// 기본 패턴
synchronized (lock) {
    while (!condition) {
        lock.wait();           // 조건 만족까지 대기
    }
    // 작업 수행
    lock.notify();            // 대기 Thread 깨움
}
```

### 주요 메서드
```java
lock.wait();                  // 무한 대기
lock.wait(1000);              // 1초간 대기
lock.notify();                // Thread 하나 깨움
lock.notifyAll();             // 모든 Thread 깨움
```

### 중요 규칙
- **while 루프 필수**: Spurious Wakeup 방지
- **synchronized 블록 내에서만 사용**: IllegalMonitorStateException 방지
- **notifyAll() 권장**: 예측 가능한 동작

---

## 6. 고급 동기화 도구

### ReentrantLock
```java
Lock lock = new ReentrantLock();

lock.lock();                          // 락 획득
lock.unlock();                        // 락 해제 (finally 필수)
lock.tryLock();                       // 즉시 시도
lock.tryLock(3, TimeUnit.SECONDS);    // 3초 대기
lock.lockInterruptibly();             // 인터럽트 가능
```

### Condition
```java
Condition condition = lock.newCondition();

condition.await();                    // 조건 대기
condition.signal();                   // Thread 하나 깨움
condition.signalAll();                // 모든 Thread 깨움
```

### AtomicInteger
```java
AtomicInteger counter = new AtomicInteger(0);

counter.get();                        // 값 조회
counter.set(10);                      // 값 설정
counter.incrementAndGet();            // 증가 후 반환
counter.addAndGet(5);                 // 덧셈 후 반환
counter.compareAndSet(10, 20);        // 비교 후 설정
```

### CountDownLatch
```java
CountDownLatch latch = new CountDownLatch(3);

latch.await();                        // 카운트 0까지 대기
latch.countDown();                    // 카운트 감소
latch.getCount();                     // 현재 카운트 조회
```

### Semaphore
```java
Semaphore semaphore = new Semaphore(3);

semaphore.acquire();                  // 허가 획득
semaphore.release();                  // 허가 반환
semaphore.tryAcquire();               // 즉시 시도
semaphore.availablePermits();         // 사용 가능한 허가 수
```

---

##  동기화 도구 선택 및 확인 사항

| 상황 | 권장 도구 | 특징 |
|------|----------|------|
| 간단한 락 제어 | `synchronized` | 자동 해제, 사용 간편 |
| 타임아웃 필요 | `ReentrantLock` | 유연한 락 제어 |
| 조건별 대기 | `Condition` | 정교한 Thread 제어 |
| 원자적 연산 | `AtomicInteger` | Lock 없이 안전한 연산 |
| 작업 완료 대기 | `CountDownLatch` | 일회용 동기화 지점 |
| 리소스 수 제한 | `Semaphore` | 동시 접근 수 제한 |

---


### 성능 고려사항
| 상황 | 권장 동기화 | 이유 |
|------|-------------|------|
| 락 경합 적음 | `synchronized` | JVM 최적화, 사용 간편 |
| 락 경합 많음 | `ReentrantLock` | CAS 기반, 더 나은 처리량 |
| 단순 카운터 | `AtomicInteger` | Lock-free, 최고 성능 |
| 복잡한 조건 | `Lock + Condition` | 정교한 제어 가능 |

### 안전한 코딩 패턴
```java
// 1. Lock 사용 시 반드시 finally 블록에서 해제
Lock lock = new ReentrantLock();
lock.lock();
try {
    // 작업 수행
} finally {
    lock.unlock();  // 반드시 해제
}

// 2. wait() 사용 시 while 루프 필수
synchronized (lock) {
    while (!condition) {    // if가 아닌 while 사용
        lock.wait();
    }
}

// 3. 데드락 방지를 위한 동일한 락 순서
void transfer(Account from, Account to, int amount) {
    Account firstLock = from.getId() < to.getId() ? from : to;
    Account secondLock = from.getId() < to.getId() ? to : from;
    
    synchronized (firstLock) {
        synchronized (secondLock) {
            // 이체 로직
        }
    }
}
```

### Thread 안전성 체크리스트
- [ ] **공유 변수**에 적절한 동기화 적용
- [ ] **volatile** 키워드 필요성 검토
- [ ] **Thread 이름** 설정으로 디버깅 편의성 확보
- [ ] **데몬 스레드** 여부 명확히 구분
- [ ] **안전한 종료** 패턴 구현
- [ ] **예외 처리** 시 인터럽트 상태 복원

### 디버깅 및 모니터링
```java
// Thread 정보 조회
Thread current = Thread.currentThread();
System.out.println("Thread 이름: " + current.getName());
System.out.println("Thread 상태: " + current.getState());
System.out.println("데몬 여부: " + current.isDaemon());
System.out.println("우선순위: " + current.getPriority());

// 모든 활성 Thread 조회
ThreadGroup group = Thread.currentThread().getThreadGroup();
Thread[] threads = new Thread[group.activeCount()];
group.enumerate(threads);
for (Thread t : threads) {
    if (t != null) {
        System.out.println(t.getName() + " - " + t.getState());
    }
}
```

### 주의사항
- **과도한 동기화** 피하기 (성능 저하)
- **너무 작은 임계 영역** 분할하지 말기 (오버헤드)
- **Thread 수** 적절히 제한 (메모리, CPU 고려)
- **Thread 간 통신** 시 데이터 불변성 우선 고려

---

## 7. Thread Pool과 ExecutorService

### Thread Pool의 필요성
매번 새 스레드 생성 시 발생하는 비용을 줄이고 리소스를 효율적으로 관리

### ExecutorService 주요 타입
```java
// 고정 크기 풀 (CPU 집약적 작업에 적합)
ExecutorService fixedPool = Executors.newFixedThreadPool(4);

// 동적 크기 풀 (I/O 집약적 작업에 적합)
ExecutorService cachedPool = Executors.newCachedThreadPool();

// 단일 스레드 풀 (순차 처리 보장)
ExecutorService singlePool = Executors.newSingleThreadExecutor();
```

### Callable과 Future
```java
// 결과를 반환하는 작업
Callable<String> task = () -> {
    Thread.sleep(1000);
    return "작업 완료";
};

// 미래 결과를 담는 컨테이너
Future<String> future = executor.submit(task);

// 결과 가져오기
String result = future.get();                    // 블로킹 대기
String result = future.get(3, TimeUnit.SECONDS); // 타임아웃 설정
boolean done = future.isDone();                  // 완료 여부 확인
boolean cancelled = future.cancel(true);         // 작업 취소
```

### ThreadPoolExecutor 설정
```java
ThreadPoolExecutor executor = new ThreadPoolExecutor(
    2,                                    // corePoolSize: 기본 스레드 수
    5,                                    // maximumPoolSize: 최대 스레드 수
    60L, TimeUnit.SECONDS,               // keepAliveTime: 유휴 스레드 생존 시간
    new ArrayBlockingQueue<>(10),        // workQueue: 작업 대기 큐
    new ThreadPoolExecutor.CallerRunsPolicy()  // handler: 거부 정책
);
```

### 안전한 종료 패턴
```java
executor.shutdown();                     // 새 작업 거부, 기존 작업 완료
if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
    executor.shutdownNow();              // 강제 종료
    if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
        System.err.println("완전 종료 실패");
    }
}
```

### ExecutorService 선택 기준
| 상황 | 권장 타입 | 특징 |
|------|----------|------|
| CPU 집약적 작업 | `newFixedThreadPool(코어수)` | 최적 성능, 리소스 제한 |
| I/O 집약적 작업 | `newCachedThreadPool()` | 동적 확장, 대기 시간 활용 |
| 순차 처리 필요 | `newSingleThreadExecutor()` | 작업 순서 보장 |
| 세밀한 제어 | `ThreadPoolExecutor` | 모든 설정 커스터마이징 |

---

## 8. 동시성 컬렉션

### 일반 컬렉션의 문제점
```java
// 위험: 동시 접근 시 데이터 손실, 무한루프 가능
Map<String, Integer> unsafeMap = new HashMap<>();
List<String> unsafeList = new ArrayList<>();
```

### 동시성 컬렉션 해결책
```java
// 안전: 동시 접근 보장
Map<String, Integer> safeMap = new ConcurrentHashMap<>();
Queue<String> safeQueue = new ConcurrentLinkedQueue<>();
```

### ConcurrentHashMap 주요 기능
```java
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

// 기본 연산 (스레드 안전)
map.put("key", 1);
Integer value = map.get("key");
map.remove("key");

// 원자적 연산
map.putIfAbsent("key", 1);               // 없을 때만 추가
map.replace("key", 1, 2);                // 조건부 교체
map.computeIfAbsent("key", k -> 1);      // 없으면 계산 후 추가
map.merge("key", 1, Integer::sum);       // 병합 연산
```

### BlockingQueue 활용
```java
// Producer-Consumer 패턴
BlockingQueue<String> queue = new LinkedBlockingQueue<>(10);

// Producer
queue.put("item");                       // 블로킹 추가
boolean added = queue.offer("item");     // 즉시 시도
boolean added = queue.offer("item", 1, TimeUnit.SECONDS); // 타임아웃

// Consumer  
String item = queue.take();              // 블로킹 대기
String item = queue.poll();              // 즉시 시도
String item = queue.poll(1, TimeUnit.SECONDS); // 타임아웃
```

### BlockingQueue 구현체 비교
| 구현체 | 크기 | 특징 | 용도 |
|--------|------|------|------|
| `ArrayBlockingQueue` | 고정 | 배열 기반, 공정성 선택 가능 | 크기 제한 필요 |
| `LinkedBlockingQueue` | 가변 | 링크 기반, 높은 처리량 | 일반적인 큐 |
| `PriorityBlockingQueue` | 무제한 | 우선순위 기반 정렬 | 우선순위 처리 |
| `SynchronousQueue` | 0 | 직접 전달, 저장 공간 없음 | 즉시 전달 |

### 동시성 컬렉션 선택 기준
| 용도 | 권장 컬렉션 | 특징 |
|------|-------------|------|
| 키-값 저장 | `ConcurrentHashMap` | 높은 동시성, 원자적 연산 |
| 순서 없는 큐 | `ConcurrentLinkedQueue` | Lock-free, 높은 성능 |
| Producer-Consumer | `BlockingQueue` | 블로킹 지원, 크기 제한 |
| 집합 연산 | `ConcurrentSkipListSet` | 정렬 유지, 로그 시간 |

### 실무 활용 패턴
```java
// 캐시 구현
ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<>();

// 원자적 카운터
ConcurrentHashMap<String, AtomicInteger> counters = new ConcurrentHashMap<>();
counters.computeIfAbsent("visits", k -> new AtomicInteger(0)).incrementAndGet();

// 작업 큐 시스템
BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>();

// Producer
taskQueue.put(new Task("work"));

// Consumer
Task task = taskQueue.take();
task.execute();
```

### 성능 고려사항
| 상황 | 권장 방식 | 이유 |
|------|----------|------|
| 읽기 많음 | `ConcurrentHashMap` | 읽기 시 락 없음 |
| 쓰기 많음 | `ConcurrentHashMap` | 세그먼트 기반 락 |
| 순서 중요 | `BlockingQueue` | FIFO 보장 |
| 메모리 제한 | `ArrayBlockingQueue` | 고정 크기 |

---

## 동시성 프로그래밍 모범 사례

### Thread 안전성 체크리스트
- [ ] **공유 변수**에 적절한 동기화 적용
- [ ] **volatile** 키워드 필요성 검토
- [ ] **Thread 이름** 설정으로 디버깅 편의성 확보
- [ ] **데몬 스레드** 여부 명확히 구분
- [ ] **안전한 종료** 패턴 구현
- [ ] **예외 처리** 시 인터럽트 상태 복원
- [ ] **동시성 컬렉션** 활용으로 안전성 확보

### 디버깅 및 모니터링
```java
// Thread 정보 조회
Thread current = Thread.currentThread();
System.out.println("Thread 이름: " + current.getName());
System.out.println("Thread 상태: " + current.getState());
System.out.println("데몬 여부: " + current.isDaemon());
System.out.println("우선순위: " + current.getPriority());

// 모든 활성 Thread 조회
ThreadGroup group = Thread.currentThread().getThreadGroup();
Thread[] threads = new Thread[group.activeCount()];
group.enumerate(threads);
for (Thread t : threads) {
    if (t != null) {
        System.out.println(t.getName() + " - " + t.getState());
    }
}
```
