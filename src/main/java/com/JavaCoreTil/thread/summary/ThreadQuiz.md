# Java Thread 실무 회상 퀴즈 (1~6단계)
 
**제한시간**: 30분

---

## 1단계: Thread 기초 이론

### Q1. Thread 생명주기
Thread의 생명주기를 완성하세요.
```
_____ → _____ → _____ → _____ 
```

### Q2. 메모리 구조
다음 표를 완성하시오.

| 메모리 영역 | 공유 여부 | 저장 내용 |
|-------------|-----------|----------|
| Stack | _______ | _______ |
| Heap | _______ | _______ |

### Q3. 동시성 vs 병렬성
- **Concurrency(동시성)**: _____________적 동시 실행
- **Parallelism(병렬성)**: _____________적 동시 실행

### Q4. Process vs Thread
Process와 Thread의 핵심 차이점 2가지를 작성하시오.
1. 메모리 관점: _________________
2. 생성 비용 관점: _________________

---

## 2단계: Thread 생성과 제어

### Q5. Thread 생성 방법
Thread를 생성하는 3가지 주요 방법을 코드로 작성하시오.

**방법 1 (상속):**
```java
class MyThread extends _______ {
    public void _____() {
        // 작업 내용
    }
}
```

**방법 2 (Runnable 인터페이스):**
```java
class MyTask implements _______ {
    public void _____() {
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
다음 메서드들의 기능을 간단히 설명하시오.
- `start()`: _________________
- `join()`: _________________
- `sleep()`: _________________
- `interrupt()`: _________________

### Q7. 안전한 종료 패턴
Thread를 안전하게 종료하기 위한 2가지 핵심 요소를 작성하시오.
1. _______ 플래그 변수 사용
2. _______ 메서드로 중단 신호 전송

---

## 3단계: 메모리 가시성

### Q8. volatile 키워드
volatile 키워드가 보장하는 것과 보장하지 않는 것을 구분하시오.

**보장하는 것**: _________________

**보장하지 않는 것**: _________________

### Q9. 메모리 가시성 문제 해결
다음 코드에서 메모리 가시성 문제를 해결하기 위해 추가해야 할 키워드는?
```java
private _______ boolean running = true;
```

---

## 4단계: 동시성 문제와 synchronized

### Q10. Race Condition
Race Condition이 발생하는 이유를 한 문장으로 설명하시오.

**답**: _________________________________

### Q11. synchronized 보장 사항
synchronized가 보장하는 2가지를 작성하시오.
1. ___________ 보장 (메모리 동기화)
2. ___________ 보장 (상호 배제)

### Q12. synchronized 사용법
synchronized를 사용하는 2가지 방법을 코드로 작성하시오.

**메서드 동기화:**
```java
public _______ void increment() {
    count++;
}
```

**블록 동기화:**
```java
public void increment() {
    _______(this) {
        count++;
    }
}
```

---

## 5단계: Thread 간 협력과 통신

### Q13. wait/notify 메커니즘
다음 빈칸을 채워 Producer-Consumer 패턴의 핵심 구조를 완성하시오.

```java
// Consumer
synchronized (lock) {
    while (!condition) {
        lock._____();  // 조건 만족까지 대기
    }
    // 작업 수행
}

// Producer  
synchronized (lock) {
    // 조건 변경
    lock._____();  // 대기 중인 스레드 깨움
}
```

### Q14. Spurious Wakeup
Spurious Wakeup을 방지하기 위해 wait() 사용 시 반드시 사용해야 하는 제어문은?

**답**: _______ 루프

---

## 6단계: 고급 동기화 도구

### Q15. ReentrantLock 사용 패턴
ReentrantLock의 안전한 사용 패턴을 코드로 작성하시오.
```java
Lock lock = new ReentrantLock();
lock._____();
try {
    // 임계 영역
} finally {
    lock._____();
}
```

### Q16. AtomicInteger 주요 메서드
AtomicInteger의 주요 메서드 4개를 작성하시오.
1. 값 조회: _____()
2. 값 설정: _____(int value)
3. 증가 후 반환: _____()
4. 비교 후 설정: _____(int expect, int update)

### Q17. 동기화 도구 선택
다음 상황에 가장 적합한 동기화 도구를 선택하시오.

**상황 A**: 여러 스레드가 모두 준비될 때까지 기다린 후 동시에 시작

**답**: _________________

**상황 B**: 동시 접근 가능한 리소스 수를 제한

**답**: _________________

---
