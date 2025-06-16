---
🟢 0단계: Thread 기초 이론
├─ Process vs Thread : 프로세스/스레드 차이, 메모리 구조
├─ JVM Thread 모델 : OS 매핑, 플랫폼별 차이
├─ Thread 생명주기 : NEW → RUNNABLE → BLOCKED/WAITING/TIMED_WAITING → TERMINATED
├─ 동시성 vs 병렬성 : Concurrency(논리적 동시), Parallelism(물리적 동시)
└─ 메모리 구조 : Thread Stack(지역변수), Heap(공유 객체)

---
🟡 1단계: Thread 생성과 제어
├─ Thread 생성 방법 : 상속, Runnable, 람다, 익명 클래스
├─ 주요 제어 메서드 : start(), join(), sleep(), interrupt()
├─ Thread 속성 관리 : 이름, 우선순위, 데몬, 그룹, 상태
└─ 안전한 종료 : interrupt, volatile, 리소스 정리

---
🟠 2단계: 동시성 문제와 synchronized
├─ Race Condition : 공유 변수 동시 접근, 경쟁 상황
├─ Critical Section : 상호 배제(Mutex)
├─ synchronized 동기화 : 메서드/블록, 객체 락
└─ 데드락(Deadlock) : 발생 조건, 간단 재현

---
🔵 3단계: Thread 간 협력과 통신
├─ wait/notify 메커니즘 : wait(), notify(), notifyAll(), 모니터, Spurious Wakeup
├─ Producer-Consumer 패턴 : 버퍼, 대기/통지
└─ 객체 락 심화 : Entry Set, Wait Set

---
🟣 4단계: 실무 Thread Pool 활용
├─ ExecutorService : newFixedThreadPool 등
├─ Callable/Future : submit, get, cancel, 타임아웃
├─ ThreadPoolExecutor : core/max size, queue, 정책
├─ 작업 우선순위 관리 : PriorityBlockingQueue, 커스텀 Comparator, 긴급도별 처리
└─ Fork/Join Framework : 분할정복, RecursiveTask/Action

---
🟤 5단계: 고급 동기화와 성능 최적화
├─ 명시적 락 : ReentrantLock, ReadWriteLock, StampedLock
├─ Atomic/CAS : AtomicInteger 등, compareAndSet, Lock-free
├─ volatile/메모리 가시성 : 가시성, 재정렬 방지, Double-checked Locking
├─ 동기화 유틸리티 : CountDownLatch, Semaphore, CyclicBarrier, Exchanger
├─ ThreadLocal : 활용, 메모리 누수 방지
└─ 성능 측정/모니터링 : Thread Dump, Lock Contention, 병목 분석

---
⚡ 6단계: Virtual Thread (Java 19+)
├─ Virtual Thread 개념 : 경량 스레드, Platform Thread vs Virtual Thread
├─ 생성과 사용법 : Thread.ofVirtual(), Executors.newVirtualThreadPerTaskExecutor()
├─ 내부 구조 : Carrier Thread, ForkJoinPool, 스케줄링
├─ I/O 최적화 : Blocking I/O 처리, 컨텍스트 스위칭 비용 절약
├─ 제약사항 : synchronized 블록, native 메서드, CPU 집약적 작업
└─ 실무 적용 : 대용량 동시 요청 처리, 마이크로서비스 아키텍처

---
🌐 네트워크와 관련된 멀티스레딩
├─ 소켓 통신에서 Thread 역할 : 클라이언트별 Thread, ThreadPool
├─ 비동기 네트워크 I/O : NIO, Selector
├─ 동시성 이슈 : 네트워크 프로그래밍 특징
└─ 실시간 서버 설계 : Thread 안전성, 효율적 통신

---