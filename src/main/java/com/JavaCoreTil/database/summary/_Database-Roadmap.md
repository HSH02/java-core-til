```java
---
⚡ 1단계: 데이터베이스 기초
├─ 관계형 데이터베이스 : RDBMS 개념, 테이블/행/열
├─ SQL 기초 : DDL, DML, DCL, 기본 쿼리문
├─ 트랜잭션 : ACID 속성, 격리 수준
├─ 인덱스와 성능 : 클러스터/논클러스터 인덱스
├─ 정규화 : 1NF~3NF, 데이터 중복 제거
└─ 데이터베이스 설계 : ERD, 관계 설정

---
⚡ 2단계: JDBC 기초
├─ JDBC 개념 : Java Database Connectivity
├─ 드라이버 로딩 : Class.forName(), DriverManager
├─ 연결 생성 : Connection, URL 구조
├─ Statement 실행 : executeQuery(), executeUpdate()
├─ ResultSet 처리 : next(), getString(), getInt()
└─ 리소스 관리 : close(), try-with-resources

---
⚡ 3단계: PreparedStatement와 SQL 인젝션
├─ PreparedStatement : 사전 컴파일, 성능 향상
├─ 파라미터 바인딩 : setString(), setInt(), setDate()
├─ SQL 인젝션 방지 : 파라미터화 쿼리
├─ 배치 처리 : addBatch(), executeBatch()
├─ 메타데이터 : DatabaseMetaData, ResultSetMetaData
└─ CallableStatement : 저장 프로시저 호출

---
⚡ 4단계: 트랜잭션 관리
├─ 자동 커밋 : setAutoCommit(false)
├─ 수동 트랜잭션 : commit(), rollback()
├─ 세이브포인트 : setSavepoint(), rollback(savepoint)
├─ 격리 수준 설정 : TRANSACTION_READ_COMMITTED
├─ 데드락 처리 : 타임아웃, 재시도 로직
└─ 분산 트랜잭션 : XA 트랜잭션, 2PC

---
⚡ 5단계: 연결 풀링
├─ 연결 풀 필요성 : 연결 생성 비용, 리소스 관리
├─ HikariCP : 고성능 연결 풀, 설정 최적화
├─ Apache DBCP : 기본 연결 풀, 설정 옵션
├─ 풀 크기 조정 : minimumIdle, maximumPoolSize
├─ 연결 검증 : connectionTestQuery, leakDetectionThreshold
└─ 모니터링 : 풀 상태, 성능 메트릭

---
⚡ 6단계: DataSource와 JNDI
├─ DataSource 인터페이스 : 연결 팩토리 추상화
├─ JNDI 설정 : 서버 환경, 외부 설정
├─ 프로퍼티 기반 설정 : application.properties
├─ 환경별 설정 : dev/test/prod 분리
├─ 스프링 설정 : @Configuration, @Bean
└─ 설정 외부화 : ConfigurationProperties

---
⚡ 7단계: ORM 기초 (JPA/Hibernate)
├─ ORM 개념 : 객체-관계 매핑, 임피던스 불일치
├─ JPA 소개 : Java Persistence API, 표준 스펙
├─ 엔티티 매핑 : @Entity, @Table, @Column
├─ 기본키 생성 : @Id, @GeneratedValue, 전략
├─ 영속성 컨텍스트 : EntityManager, 생명주기
└─ JPQL : 객체 지향 쿼리 언어

---
⚡ 8단계: JPA 관계 매핑
├─ 일대일 관계 : @OneToOne, 단방향/양방향
├─ 일대다 관계 : @OneToMany, @ManyToOne
├─ 다대다 관계 : @ManyToMany, 연결 테이블
├─ 지연 로딩 : LAZY vs EAGER, N+1 문제
├─ 영속성 전이 : CASCADE 옵션
└─ 고아 객체 제거 : orphanRemoval

---
⚡ 9단계: Spring Data JPA
├─ Repository 패턴 : CrudRepository, JpaRepository
├─ 쿼리 메서드 : findBy, countBy, 메서드 이름 규칙
├─ 커스텀 쿼리 : @Query, JPQL/Native SQL
├─ 페이징과 정렬 : Pageable, Sort
├─ Auditing : @CreatedDate, @LastModifiedDate
└─ Specification : 동적 쿼리, 조건 조합

---
⚡ 10단계: 트랜잭션과 동시성
├─ @Transactional : 선언적 트랜잭션
├─ 전파 속성 : REQUIRED, REQUIRES_NEW, NESTED
├─ 격리 수준 : READ_COMMITTED, REPEATABLE_READ
├─ 읽기 전용 : readOnly=true, 성능 최적화
├─ 롤백 조건 : rollbackFor, noRollbackFor
└─ 동시성 제어 : 낙관적/비관적 락

---
⚡ 11단계: 성능 최적화
├─ 쿼리 최적화 : EXPLAIN, 실행 계획 분석
├─ 인덱스 활용 : 복합 인덱스, 커버링 인덱스
├─ 배치 처리 : batch insert/update
├─ 캐싱 전략 : L1/L2 캐시, 쿼리 캐시
├─ 연결 풀 튜닝 : 적절한 풀 크기 설정
└─ 프로파일링 : 슬로우 쿼리, P6Spy

```