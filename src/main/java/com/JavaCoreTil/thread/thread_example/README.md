# Java Thread 학습 예제

Thread-Roadmap.md의 6단계에 맞춰 각 개념을 깔끔하게 확인할 수 있는 예제 코드입니다.

## 단계별 학습 가이드

### 1단계: Thread 기초 이론
**파일**: `Stage1_ThreadBasics.java`
- Thread 생명주기 (NEW → RUNNABLE → TERMINATED)
- 메모리 구조 (Stack vs Heap)
- 동시성 vs 병렬성 개념

### 2단계: Thread 생성과 제어
**파일**: `Stage2_ThreadCreationAndControl.java`
- Thread 생성 방법 (Runnable, 상속, 익명클래스)
- 제어 메서드 (start, join, interrupt)
- Thread 속성 (이름, 우선순위, 데몬)
- 안전한 종료 패턴

### 3단계: 메모리 가시성
**파일**: `Stage3_MemoryVisibility.java`
- 메모리 가시성 문제 시연
- volatile 키워드 해결책
- 가시성 vs 원자성 차이점

### 4단계: 동시성 문제와 synchronized
**파일**: `Stage4_Synchronized.java`
- Race Condition 문제
- synchronized 해결책
- 인스턴스 락 vs 클래스 락
- 데드락 위험과 방지법

### 5단계: wait/notify 메커니즘
**파일**: `Stage5_WaitNotify.java`
- wait/notify 기본 개념
- 생산자-소비자 패턴
- notify vs notifyAll 차이

### 6단계: 고급 동기화 도구
**파일**: `Stage6_AdvancedSynchronization.java`
- ReentrantLock (명시적 락, 타임아웃)
- Condition (조건별 대기/신호)
- AtomicInteger (원자적 연산)
- CountDownLatch (일회성 동기화)
- Semaphore (리소스 접근 제한)

### 7단계: Thread Pool과 Executor
**파일**: `Stage7_ThreadPoolAndExecutor.java`
- Thread Pool의 필요성과 장점
- ExecutorService 다양한 타입
- Callable과 Future (결과 반환)
- ThreadPoolExecutor 세부 설정
- Fork/Join Framework

### 8단계: 동시성 컬렉션
**파일**: `Stage8_ConcurrentCollections.java`
- 동시성 문제가 있는 일반 컬렉션
- ConcurrentHashMap, CopyOnWriteArrayList
- BlockingQueue (생산자-소비자 패턴)
- 성능 비교와 선택 기준

### 9단계: Virtual Thread (Java 21+)
**파일**: `Stage9_VirtualThread.java`
- Virtual Thread 생성 방법
- Platform Thread vs Virtual Thread 성능 비교
- I/O 집약적 작업 최적화
- Virtual Thread Executor 활용
- 실무 웹 서버 시뮬레이션

## 주요 학습 목적

- **1-2단계**: Thread 기본 개념과 생성/제어
- **3-4단계**: 동시성 문제와 기본 해결책
- **5-6단계**: 고급 동기화 메커니즘
- **7-8단계**: Thread Pool과 동시성 컬렉션
- **9단계**: 최신 Virtual Thread 기술

## 실행 요구사항

- **Stage 1-8**: Java 8 이상
- **Stage 9**: Java 21 이상 (Virtual Thread 지원)
 