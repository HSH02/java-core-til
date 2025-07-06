```java
---
⚡ 1단계: Calendar와 Date (레거시)
├─ Date 클래스 : 날짜와 시간 표현 (JDK 1.0)
├─ Calendar 클래스 : Date의 단점 보완 (JDK 1.1)
├─ Calendar 인스턴스 생성 : getInstance() 메서드
├─ 날짜와 시간 필드 : YEAR, MONTH, DAY_OF_MONTH
├─ 날짜 계산 : add(), roll() 메서드
└─ Date와 Calendar 변환 : getTime(), setTime()

---
⚡ 2단계: 형식화 클래스
├─ DecimalFormat : 숫자 형식화, 패턴 사용
├─ SimpleDateFormat : 날짜와 시간 형식화
├─ ChoiceFormat : 조건에 따른 문자열 선택
├─ MessageFormat : 여러 데이터 타입 형식화
├─ 형식화 패턴 : 기호의 의미와 사용법
└─ 파싱과 형식화 : parse()와 format() 메서드

---
⚡ 3단계: java.time 패키지 핵심 클래스
├─ java.time 패키지 구조 : JDK 8에서 추가
├─ LocalDate : 날짜 정보만 (년, 월, 일)
├─ LocalTime : 시간 정보만 (시, 분, 초, 나노초)
├─ LocalDateTime : 날짜 + 시간 (시간대 정보 없음)
├─ ZonedDateTime : 날짜 + 시간 + 시간대
└─ 시간 객체의 불변성 : 메서드 호출 시 새 객체 반환

---
⚡ 4단계: LocalDate와 LocalTime
├─ 날짜 객체 생성 : now(), of(), parse()
├─ 날짜 정보 얻기 : getYear(), getMonth(), getDayOfWeek()
├─ 날짜 필드 변경 : withYear(), withMonth(), withDayOfMonth()
├─ 날짜 계산 : plusDays(), minusMonths(), plus()
├─ 시간 객체 생성과 조작 : 시간 관련 메서드들
└─ 날짜와 시간 비교 : isAfter(), isBefore(), isEqual()

---
⚡ 5단계: Instant와 타임스탬프
├─ Instant 개념 : 기계적 시간, 에포크 기준
├─ 에포크 시간 : 1970년 1월 1일 00:00:00 UTC
├─ Instant 생성 : now(), ofEpochSecond(), ofEpochMilli()
├─ Instant와 다른 시간 클래스 변환
├─ 시간 측정 : Duration을 이용한 시간 간격
└─ 시스템 시간과 Instant : System.currentTimeMillis()

---
⚡ 6단계: LocalDateTime과 ZonedDateTime
├─ LocalDateTime 생성 : LocalDate + LocalTime 결합
├─ LocalDateTime 변환 : atDate(), atTime(), atZone()
├─ ZonedDateTime : 시간대 정보 포함
├─ 시간대 ID : ZoneId.of(), systemDefault()
├─ 서머타임 처리 : 시간 변경 자동 처리
└─ OffsetDateTime : UTC 오프셋 정보만 포함

---
⚡ 7단계: 시간 조정과 계산
├─ TemporalAdjuster : 시간 조정 인터페이스
├─ 미리 정의된 조정자 : firstDayOfMonth(), lastDayOfYear()
├─ 커스텀 조정자 : 비즈니스 로직 구현
├─ 시간 필드 : ChronoField, 시간 구성 요소
├─ 시간 단위 : ChronoUnit, 기간 단위
└─ 복잡한 시간 계산 : 영업일, 휴일 제외

---
⚡ 8단계: 레거시 API 연동
├─ Date와 변환 : toInstant(), Date.from()
├─ Calendar 변환 : toZonedDateTime(), 호환성
├─ Timestamp 변환 : valueOf(), 데이터베이스 연동
├─ SimpleDateFormat 대체 : DateTimeFormatter 사용
├─ 마이그레이션 전략 : 점진적 변환, 호환성 유지
└─ 성능 비교 : 새 API vs 레거시 API

---
⚡ 9단계: 달력과 연대기
├─ Chronology : 달력 시스템 추상화
├─ IsoChronology : ISO-8601 표준 달력
├─ 다른 달력 시스템 : JapaneseChronology, HijrahChronology
├─ ChronoLocalDate : 달력 시스템별 날짜
├─ Era : 연대, 서기/기원전
└─ 달력 시스템 선택 : 지역화, 문화적 고려사항

---
⚡ 10단계: 시간 API와 스트림
├─ 시간 객체 스트림 : Stream<LocalDate> 생성
├─ 날짜 범위 생성 : datesUntil(), 연속된 날짜
├─ 시간 필터링 : 특정 조건 날짜 추출
├─ 시간 집계 : 그룹핑, 통계 계산
├─ 병렬 처리 : 시간 계산 최적화
└─ 함수형 시간 처리 : 람다식 활용

---
⚡ 11단계: 시간 API 성능 최적화
├─ 객체 생성 비용 : 불변 객체, 팩토리 메서드
├─ 포맷터 재사용 : DateTimeFormatter 캐싱
├─ 시간대 데이터 : TZDB 업데이트, 메모리 사용량
├─ 문자열 파싱 최적화 : 미리 컴파일된 패턴
├─ 벤치마킹 : JMH를 통한 성능 측정
└─ 메모리 효율성 : 불필요한 객체 생성 방지

---
⚡ 12단계: 실무 활용 패턴
├─ 비즈니스 시간 계산 : 영업일, 휴일 처리
├─ 배치 처리 : 시간 기반 스케줄링
├─ 로깅과 모니터링 : 타임스탬프, 실행 시간 측정
├─ 캐싱 TTL : 시간 기반 캐시 만료
├─ API 응답 : ISO-8601 형식, 클라이언트 호환성
└─ 테스트 지원 : 시간 목킹, Clock 추상화

```