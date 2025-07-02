---
⚡ 1단계: 함수형 프로그래밍 기초
├─ 함수형 프로그래밍 개념 : 선언적 프로그래밍, 수학적 함수
├─ 순수 함수 : 부작용 없음, 동일 입력-동일 출력
├─ 불변성 : 상태 변경 금지, 새 객체 생성
├─ 고차 함수 : 함수를 매개변수/반환값으로 사용
├─ 1급 객체 : 함수를 값으로 취급
└─ 함수형 vs 명령형 : 패러다임 비교

---
⚡ 2단계: 람다 표현식 기초
├─ 람다 문법 : (매개변수) -> 표현식
├─ 타입 추론 : 컴파일러 자동 타입 결정
├─ 변수 캡처 : effectively final, 클로저
├─ 람다 vs 익명 클래스 : 성능, 메모리 사용
├─ 메서드 지역 변수 접근 : 제약사항 이해
└─ 람다 표현식 활용 예시 : 간단한 변환

---
⚡ 3단계: 함수형 인터페이스
├─ @FunctionalInterface : 단일 추상 메서드
├─ Predicate<T> : 조건 검사, test() 메서드
├─ Function<T,R> : 변환 함수, apply() 메서드
├─ Consumer<T> : 소비 함수, accept() 메서드
├─ Supplier<T> : 공급 함수, get() 메서드
└─ 커스텀 함수형 인터페이스 : 비즈니스 로직 추상화

---
⚡ 4단계: 메서드 참조
├─ 정적 메서드 참조 : ClassName::staticMethod
├─ 인스턴스 메서드 참조 : instance::method
├─ 특정 타입 인스턴스 메서드 참조 : ClassName::instanceMethod
├─ 생성자 참조 : ClassName::new
├─ 배열 생성자 참조 : int[]::new
└─ 메서드 참조 vs 람다 : 가독성, 성능 비교

---
⚡ 5단계: Stream API 기초
├─ Stream 개념 : 데이터 흐름, 지연 평가
├─ Stream 생성 : of(), generate(), iterate()
├─ 중간 연산 : filter(), map(), sorted()
├─ 최종 연산 : collect(), forEach(), reduce()
├─ Stream 파이프라인 : 연산 체이닝
└─ Stream vs Collection : 일회성, 지연 처리

---
⚡ 6단계: Stream 중간 연산
├─ filter() : 조건부 필터링, Predicate 활용
├─ map() : 요소 변환, Function 적용
├─ flatMap() : 중첩 구조 평면화
├─ distinct() : 중복 제거, equals/hashCode 기반
├─ sorted() : 정렬, Comparator 활용
└─ peek() : 디버깅, 중간 상태 확인

---
⚡ 7단계: Stream 최종 연산
├─ collect() : Collectors 활용, 컬렉션 수집
├─ reduce() : 누적 연산, 단일 값 산출
├─ forEach() : 각 요소 처리, Consumer 적용
├─ count(), min(), max() : 집계 함수
├─ anyMatch(), allMatch(), noneMatch() : 조건 검사
└─ findFirst(), findAny() : 요소 검색

---
⚡ 8단계: Collectors 활용
├─ toList(), toSet(), toMap() : 기본 수집
├─ groupingBy() : 그룹핑, 분류별 수집
├─ partitioningBy() : 이분할, boolean 기준 분리
├─ joining() : 문자열 결합, 구분자 지정
├─ summarizingInt() : 통계 정보 수집
└─ 커스텀 Collector : 특별한 수집 로직

---
⚡ 9단계: Optional 클래스
├─ Optional 개념 : null 안전성, 명시적 부재 표현
├─ Optional 생성 : of(), ofNullable(), empty()
├─ 값 확인 : isPresent(), isEmpty()
├─ 값 추출 : get(), orElse(), orElseThrow()
├─ 함수형 연산 : map(), flatMap(), filter()
└─ Optional 활용 패턴 : null 처리 개선

---
⚡ 10단계: 병렬 스트림
├─ 병렬 처리 개념 : Fork-Join 프레임워크
├─ parallelStream() : 병렬 스트림 생성
├─ 스레드 안전성 : 상태 공유 문제 해결
├─ 성능 고려사항 : 데이터 크기, 연산 복잡도
├─ 병렬 스트림 최적화 : 적절한 사용 시나리오
└─ 순차 vs 병렬 : 성능 측정, 벤치마킹

---
⚡ 11단계: 고급 함수형 패턴
├─ 함수 합성 : andThen(), compose() 메서드
├─ 커링 : 다중 매개변수 함수 변환
├─ 부분 적용 : 일부 매개변수 고정
├─ 모나드 패턴 : flatMap 체이닝, 컨텍스트 처리
├─ 함수형 에러 처리 : Try, Either 패턴
└─ 지연 평가 : Supplier를 통한 지연 실행

---
⚡ 12단계: 실무 함수형 프로그래밍
├─ 비즈니스 로직 함수화 : 도메인 함수 설계
├─ 테스트 용이성 : 순수 함수 테스트
├─ 성능 최적화 : 스트림 연산 효율화
├─ 함수형과 객체지향 조화 : 하이브리드 설계
├─ 레거시 코드 리팩토링 : 점진적 함수형 도입
└─ 함수형 라이브러리 : Vavr, RxJava 활용

---
🎯 실무 핵심 포인트
├─ 가독성 개선 : 선언적 코드 작성
├─ 성능 고려사항 : 스트림 오버헤드, 병렬 처리
├─ 디버깅 전략 : peek(), 중간 상태 확인
├─ 메모리 관리 : 무한 스트림, 지연 평가
├─ 팀 협업 : 함수형 스타일 가이드
└─ 점진적 도입 : 기존 코드베이스 개선

--- 