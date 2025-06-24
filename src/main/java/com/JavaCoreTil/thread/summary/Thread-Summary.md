# Java Thread 핵심 정리

Java Thread 프로그래밍에서 반드시 알아야 할 기초 개념들을 정리했습니다.

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
