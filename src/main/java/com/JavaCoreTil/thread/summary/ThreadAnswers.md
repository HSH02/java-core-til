# Java Thread 실무 회상 퀴즈 정답 및 해설

**정답 확인**: 각 문제별 정답과 실무 활용 포인트  
**학습 목표**: 틀린 부분을 정확히 이해하고 실무에 적용

---

## 1단계: Thread 기초 이론 정답

### Q1. Thread 생명주기
**정답:**
```
NEW → RUNNABLE → BLOCKED/WAITING/TIMED_WAITING → TERMINATED
```

### Q2. 메모리 구조
**정답:**

| 메모리 영역 | 공유 여부 | 저장 내용 |
|-------------|-----------|----------|
| Stack | **Thread별 독립** | **지역변수, 메서드 호출 정보** |
| Heap | **모든 Thread 공유** | **객체, 인스턴스 변수** |

### Q3. 동시성 vs 병렬성
**정답:**
- **Concurrency(동시성)**: **논리**적 동시 실행
- **Parallelism(병렬성)**: **물리**적 동시 실행

### Q4. Process vs Thread
**정답:**
1. 메모리 관점: **Process는 독립된 메모리 공간, Thread는 메모리 공유**
2. 생성 비용 관점: **Process 생성이 Thread 생성보다 비용이 높음**

---

## 2단계: Thread 생성과 제어 정답

### Q5. Thread 생성 방법
**정답:**

**방법 1 (상속):**
```java
class MyThread extends Thread {
    public void run() {
        // 작업 내용
    }
}
```

**방법 2 (Runnable 인터페이스):**
```java
class MyTask implements Runnable {
    public void run() {
        // 작업 내용
    }
}
Thread thread = new Thread(new MyTask());
```

**방법 3 (람다 표현식 - 권장):**
```java
Thread thread = new Thread(() -> {
    // 작업 내용
});
```

### Q6. 제어 메서드
**정답:**
- `start()`: **Thread를 시작하여 run() 메서드 실행**
- `join()`: **다른 Thread의 완료를 대기**
- `sleep()`: **현재 Thread를 지정된 시간만큼 정지**
- `interrupt()`: **Thread에 중단 신호 전송**

### Q7. 안전한 종료 패턴
**정답:**
1. **volatile** 플래그 변수 사용
2. **interrupt** 메서드로 중단 신호 전송

---

## 3단계: 메모리 가시성 정답

### Q8. volatile 키워드
**정답:**
- **보장하는 것**: **메모리 가시성 (Memory Visibility)**
- **보장하지 않는 것**: **원자성 (Atomicity)**

### Q9. 메모리 가시성 문제 해결
**정답:**
```java
private volatile boolean running = true;
```

---

## 4단계: 동시성 문제와 synchronized 정답

### Q10. Race Condition
**정답:**
**여러 Thread가 공유 자원에 동시에 접근하여 실행 순서에 따라 결과가 달라지는 문제**

### Q11. synchronized 보장 사항
**정답:**
1. **가시성** 보장 (메모리 동기화)
2. **원자성** 보장 (상호 배제)

### Q12. synchronized 사용법
**정답:**

**메서드 동기화:**
```java
public synchronized void increment() {
    count++;
}
```

**블록 동기화:**
```java
public void increment() {
    synchronized(this) {
        count++;
    }
}
```

---

## 5단계: Thread 간 협력과 통신 정답

### Q13. wait/notify 메커니즘
**정답:**
```java
// Consumer
synchronized (lock) {
    while (!condition) {
        lock.wait();  // 조건 만족까지 대기
    }
    // 작업 수행
}

// Producer  
synchronized (lock) {
    // 조건 변경
    lock.notify();  // 대기 중인 스레드 깨움
}
```

### Q14. Spurious Wakeup
**정답:**
**while** 루프

---

## 6단계: 고급 동기화 도구 정답

### Q15. ReentrantLock 사용 패턴
**정답:**
```java
Lock lock = new ReentrantLock();
lock.lock();
try {
    // 임계 영역
} finally {
    lock.unlock();
}
```

### Q16. AtomicInteger 주요 메서드
**정답:**
1. 값 조회: **get**()
2. 값 설정: **set**(int value)
3. 증가 후 반환: **incrementAndGet**()
4. 비교 후 설정: **compareAndSet**(int expect, int update)

### Q17. 동기화 도구 선택
**정답:**
- **상황 A**: **CountDownLatch**
- **상황 B**: **Semaphore**

---

## 7단계: Thread Pool과 Executor 정답

### Q18. ExecutorService 생성
**정답:**

**CPU 집약적 작업 (4개 코어):**
```java
ExecutorService executor = Executors.newFixedThreadPool(4);
```

**I/O 집약적 작업 (동적 크기):**
```java
ExecutorService executor = Executors.newCachedThreadPool();
```

**순차 처리 보장:**
```java
ExecutorService executor = Executors.newSingleThreadExecutor();
```

**해설:**
- **newFixedThreadPool**: CPU 집약적 작업에 최적, 코어 수만큼 스레드 생성
- **newCachedThreadPool**: I/O 대기 시간 활용, 필요에 따라 스레드 생성/제거
- **newSingleThreadExecutor**: 작업 순서 보장, 단일 스레드로 순차 처리

### Q19. Callable과 Future
**정답:**
```java
// 결과 반환 작업
Callable<String> task = () -> {
    return "작업 완료";
};

// 작업 제출 및 결과 처리
Future<String> future = executor.submit(task);
String result = future.get();  // 결과 대기
```

**해설:**
- **Callable**: 결과를 반환하는 작업 (Runnable과 달리 반환값 있음)
- **Future**: 미래의 결과를 담는 컨테이너, 비동기 작업 결과 관리

### Q20. ExecutorService 안전한 종료
**정답:**
```java
executor.shutdown();  // 새 작업 거부
if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
    executor.shutdownNow();  // 강제 종료
}
```

**해설:**
- **shutdown()**: 새 작업 거부, 진행 중인 작업은 완료까지 대기
- **awaitTermination()**: 지정 시간 동안 종료 대기
- **shutdownNow()**: 진행 중인 작업도 강제 중단

---

## 8단계: 동시성 컬렉션 정답

### Q21. 동시성 컬렉션 선택
**정답:**

**키-값 저장, 높은 동시성 필요:**
```java
Map<String, Integer> map = new ConcurrentHashMap<>();
```

**Producer-Consumer 패턴, 블로킹 지원:**
```java
BlockingQueue<String> queue = new LinkedBlockingQueue<>();
```

**순서 없는 큐, Lock-free 성능:**
```java
Queue<String> queue = new ConcurrentLinkedQueue<>();
```

**해설:**
- **ConcurrentHashMap**: 세그먼트 기반 락으로 높은 동시성 지원
- **LinkedBlockingQueue**: 블로킹 지원으로 Producer-Consumer 패턴에 최적
- **ConcurrentLinkedQueue**: Lock-free 알고리즘으로 최고 성능

### Q22. ConcurrentHashMap 원자적 연산
**정답:**
```java
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

// 없을 때만 추가
map.putIfAbsent(key, 1);

// 조건부 교체  
map.replace(key, 1, 2);

// 없으면 계산 후 추가
map.computeIfAbsent(key, k -> 1);

// 병합 연산
map.merge(key, 1, Integer::sum);
```

**해설:**
- **putIfAbsent**: 키가 없을 때만 값 추가 (원자적)
- **replace**: 기존 값과 일치할 때만 교체 (원자적)
- **computeIfAbsent**: 키가 없으면 함수 실행 후 결과 저장
- **merge**: 기존 값과 새 값을 병합 함수로 결합

### Q23. BlockingQueue 메서드
**정답:**

| 메서드 | 블로킹 여부 | 타임아웃 지원 |
|--------|-------------|---------------|
| put() | **블로킹** | **지원 안함** |
| offer() | **비블로킹** | **지원함** |
| take() | **블로킹** | **지원 안함** |
| poll() | **비블로킹** | **지원함** |

**해설:**
- **put/take**: 블로킹 방식, 공간/요소 있을 때까지 무한 대기
- **offer/poll**: 비블로킹 방식, 즉시 결과 반환, 타임아웃 버전 제공

---

## 9단계: Virtual Thread (Java 19+) 정답

### Q24. Virtual Thread 생성
**정답:**

**기본 생성:**
```java
Thread virtualThread = Thread.ofVirtual().start(() -> {
    // 작업 내용
});
```

**이름 지정:**
```java
Thread virtualThread = Thread.ofVirtual()
    .name(threadName)
    .start(task);
```

**해설:**
- **Thread.ofVirtual()**: Virtual Thread 빌더 생성
- **name()**: 디버깅을 위한 스레드 이름 설정
- **start()**: 작업과 함께 즉시 시작

### Q25. Virtual Thread vs Platform Thread
**정답:**

| 구분 | Virtual Thread | Platform Thread |
|------|----------------|-----------------|
| 생성 비용 | **매우 낮음** | **높음** |
| 메모리 사용량 | **KB 단위** | **MB 단위** |
| 동시 생성 가능 수 | **수백만 개** | **수천 개** |
| I/O 블로킹 시 | **Carrier Thread 해제** | **OS Thread 블로킹** |

**해설:**
- **Virtual Thread**: JVM이 관리하는 경량 스레드, 대량 생성 가능
- **Platform Thread**: OS 스레드와 1:1 매핑, 리소스 사용량 많음
- **Carrier Thread**: Virtual Thread를 실제로 실행하는 Platform Thread

### Q26. Virtual Thread 적용 시나리오
**정답:**

**상황 A**: 대량의 HTTP 요청 처리
**적합성**: **적합** (적합/부적합)

**상황 B**: CPU 집약적 수학 계산
**적합성**: **부적합** (적합/부적합)

**상황 C**: 파일 I/O 작업 1000개 동시 처리
**적합성**: **적합** (적합/부적합)

**해설:**
- **I/O 집약적 작업**: Virtual Thread의 강점, 블로킹 시 Carrier Thread 해제
- **CPU 집약적 작업**: Platform Thread가 더 적합, Virtual Thread 오버헤드 발생
- **대량 동시 작업**: Virtual Thread의 핵심 장점

### Q27. Virtual Thread 제약사항
**정답:**
1. **synchronized** 블록 사용 시 Platform Thread로 고정
2. **CPU** 집약적 작업에는 부적합

**해설:**
- **synchronized 블록**: Virtual Thread가 Platform Thread에 고정되어 성능 저하
- **CPU 집약적 작업**: 컨텍스트 스위칭 오버헤드로 성능 저하

---

## 실무 종합 문제 정답

### Q28. Thread Pool 설정
**정답:**
```java
ThreadPoolExecutor executor = new ThreadPoolExecutor(
    5,                                  // corePoolSize
    10,                                 // maximumPoolSize  
    60, TimeUnit.SECONDS,               // keepAliveTime
    new ArrayBlockingQueue<>(20),       // workQueue
    new ThreadPoolExecutor.CallerRunsPolicy()  // 거부 정책
);
```

**해설:**
- **corePoolSize**: 기본 유지 스레드 수
- **maximumPoolSize**: 최대 스레드 수
- **keepAliveTime**: 유휴 스레드 생존 시간
- **workQueue**: 작업 대기 큐
- **CallerRunsPolicy**: 거부 시 호출자 스레드에서 실행

### Q29. 동시성 문제 해결
**정답:**

**방법 1 (synchronized):**
```java
public synchronized void increment() {
    counter++;
}
```

**방법 2 (AtomicInteger):**
```java
private AtomicInteger counter = new AtomicInteger(0);
public void increment() {
    counter.incrementAndGet();
}
```

**방법 3 (ReentrantLock):**
```java
private final Lock lock = new ReentrantLock();
public void increment() {
    lock.lock();
    try {
        counter++;
    } finally {
        lock.unlock();
    }
}
```

**해설:**
- **synchronized**: 간단하고 안전, JVM 최적화 지원
- **AtomicInteger**: Lock-free, 최고 성능
- **ReentrantLock**: 유연한 제어, 타임아웃 지원

### Q30. 성능 최적화 선택
**정답:**

**최적 선택**: **AtomicInteger**

**이유**: **Lock-free 알고리즘으로 락 경합 없이 원자적 연산 수행, 대량 작업에 최적 성능**

**해설:**
- **AtomicInteger**: CAS(Compare-And-Swap) 기반, 락 없이 원자적 연산
- **synchronized/ReentrantLock**: 락 경합으로 성능 저하 가능
- **volatile**: 원자성 보장 안 함, 단순 증가 연산에 부적합

