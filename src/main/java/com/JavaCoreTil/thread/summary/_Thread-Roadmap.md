---
✅ 1단계: Thread 기초 이론 (완료)
├─ 멀티태스킹과 멀티프로세싱 : 운영체제 레벨 개념, 시분할
├─ Process vs Thread : 프로세스/스레드 차이, 메모리 구조
├─ JVM Thread 모델 : OS 매핑, 플랫폼별 차이
├─ Thread 생명주기 : NEW → RUNNABLE → BLOCKED/WAITING/TIMED_WAITING → TERMINATED
├─ 동시성 vs 병렬성 : Concurrency(논리적 동시), Parallelism(물리적 동시)
└─ 메모리 구조 : Thread Stack(지역변수), Heap(공유 객체)

---
✅ 2단계: Thread 생성과 제어 (완료)
├─ Thread 생성 방법 : 상속, Runnable, 람다, 익명 클래스
├─ 주요 제어 메서드 : start(), join(), sleep(), interrupt()
├─ Thread 속성 관리 : 이름, 우선순위, 데몬 스레드
├─ yield() : 스레드 양보, CPU 사용권 포기
└─ 안전한 종료 : interrupt 활용, 리소스 정리

---
✅ 3단계: 메모리 가시성 (Memory Visibility) (완료)
├─ 메모리 가시성 문제 : CPU 캐시, 메모리 일관성
├─ volatile 키워드 : 가시성 보장, 재정렬 방지
├─ 자바 메모리 모델(JMM) : happens-before 관계
├─ volatile 활용 패턴 : 플래그 변수, 상태 관리
└─ 메모리 가시성 vs 원자성 : 차이점과 주의사항

---
✅ 4단계: 동시성 문제와 synchronized (완료)
├─ Race Condition : 공유 변수 동시 접근, 경쟁 상황
├─ Critical Section : 상호 배제(Mutex), 임계 영역
├─ synchronized 동기화 : 메서드/블록, 객체 락
├─ synchronized 활용 : 인스턴스 락 vs 클래스 락
└─ 데드락(Deadlock) : 발생 조건, 간단 재현, 예방 방법

---
✅ 5단계: Thread 간 협력과 통신 (완료)
├─ wait/notify 메커니즘 : wait(), notify(), notifyAll()
├─ Producer-Consumer 패턴 : 버퍼, 대기/통지
├─ 객체 락과 모니터 : Entry Set, Wait Set
└─ Spurious Wakeup : 가짜 깨어남, while 루프 필요성

---
✅ 6단계: 고급 동기화 도구 (완료)
├─ Lock 인터페이스 : synchronized의 한계, 명시적 락 제어 
├─ ReentrantLock : 명시적 락, tryLock, 타임아웃, 공정성 
├─ Lock vs synchronized : 유연성, 성능, 사용 시나리오 
├─ Lock Condition : await/signal, 조건별 대기 
├─ AtomicInteger : CAS 기반 원자적 연산
├─ CountDownLatch : 여러 스레드 동기화
└─ Semaphore : 리소스 접근 제한

---
✅ 7단계: Thread Pool과 Executor (완료)
├─ Thread Pool 필요성 : 스레드 생성 비용, 리소스 관리
├─ ExecutorService : newFixedThreadPool, newCachedThreadPool
├─ Callable/Future : submit, get, cancel
├─ ThreadPoolExecutor : 기본 설정과 모니터링
├─ ExecutorService 종료 : shutdown, shutdownNow
└─ Fork/Join Framework : 분할정복, RecursiveTask/Action, Work Stealing

---
✅ 8단계: 동시성 컬렉션 기초 (완료)
├─ 동시성 컬렉션 필요성 : 일반 컬렉션의 한계
├─ ConcurrentHashMap : 기본 사용법, 성능 특징
├─ BlockingQueue : offer, poll, take
├─ ArrayBlockingQueue vs LinkedBlockingQueue
└─ 동시성 컬렉션 선택 기준

---
⚡ 9단계: Virtual Thread (Java 19+)
├─ Virtual Thread 개념 : 경량 스레드, Platform Thread 차이
├─ 생성과 사용법 : Thread.ofVirtual(), 기본 사용
├─ 내부 구조 : Carrier Thread, 스케줄링
├─ 적용 시나리오 : 대량 동시 작업, I/O 집약적 작업
├─ 제약사항 : synchronized 블록, CPU 집약적 작업
└─ 마이그레이션 가이드 : 기존 코드에서 전환

---
🎯 실무 핵심 포인트
├─ Thread 안전성 : Immutable 객체, 방어적 복사
├─ 성능 고려사항 : 락 경합 최소화, 적절한 동시성 선택
├─ 디버깅 팁 : Thread Dump 읽기, 데드락 탐지
├─ 모범 사례 : 공유 상태 최소화, 단순한 동기화
└─ 주의사항 : 과도한 동기화, 성능 vs 안전성 균형

---