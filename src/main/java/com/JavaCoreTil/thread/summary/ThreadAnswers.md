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

