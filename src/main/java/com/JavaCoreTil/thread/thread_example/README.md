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

## 주요 학습 목적

- **1-2단계**: Thread 기본 개념과 생성/제어
- **3-4단계**: 동시성 문제와 기본 해결책
- **5-6단계**: 고급 동기화 메커니즘

각 단계는 실무에서 자주 마주치는 상황들을 바탕으로 구성되어 있습니다. 