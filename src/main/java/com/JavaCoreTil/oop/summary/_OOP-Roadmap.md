---
⚡ 1단계: 클래스와 객체 기초
├─ 객체지향 언어의 특징 : 코드 재사용성, 유지보수 용이성
├─ 클래스와 객체 정의 : 설계도와 실제 인스턴스
├─ 객체의 구성요소 : 속성(변수)과 기능(메서드)
├─ 인스턴스 생성과 사용 : new 연산자, 참조변수
├─ 객체 배열 : 여러 인스턴스 관리
└─ 클래스의 다른 정의 : 데이터와 함수의 결합

---
⚡ 2단계: 변수와 메서드
├─ 변수의 종류 : 클래스변수, 인스턴스변수, 지역변수
├─ 메서드 선언과 구현 : 반환타입, 매개변수, 실행블럭
├─ 메서드 호출과 return문 : 값 반환, 메서드 종료
├─ JVM 메모리 구조 : 메서드 영역, 힙, 호출스택
├─ 매개변수 타입 : 기본형과 참조형의 차이
└─ 재귀호출과 팩토리얼 : 메서드가 자신을 호출

---
⚡ 3단계: 메서드 오버로딩과 생성자
├─ 오버로딩 개념 : 같은 이름, 다른 매개변수
├─ 오버로딩 조건과 장점 : 메서드 이름 통일
├─ 가변인자(varargs) : 매개변수 개수 가변
├─ 생성자 역할 : 인스턴스 초기화
├─ 기본 생성자와 매개변수 생성자 : 자동 제공 vs 명시적 정의
└─ 생성자 체이닝 : this(), this 키워드

---
⚡ 4단계: 상속 (Inheritance)
├─ 상속의 정의와 장점 : 기존 클래스 재사용, 확장
├─ 클래스 간 관계 : 상속관계 vs 포함관계
├─ 단일 상속 : 하나의 조상클래스만 허용
├─ Object 클래스 : 모든 클래스의 최고 조상
├─ 오버라이딩 : 조상 메서드를 자손에서 재정의
└─ super 키워드 : 조상 클래스 참조, super() 생성자

---
⚡ 5단계: 패키지와 제어자
├─ 패키지 개념 : 클래스 그룹화, 네임스페이스
├─ import문 : 다른 패키지 클래스 사용
├─ 제어자 종류 : static, final, abstract
├─ 접근 제어자 : public, protected, default, private
├─ 제어자 조합 : 함께 사용 가능한 제어자
└─ 캡슐화 구현 : 접근 제어를 통한 정보 은닉

---
⚡ 6단계: 다형성 (Polymorphism)
├─ 다형성 개념 : 여러 형태로 존재할 수 있는 능력
├─ 참조변수의 형변환 : 업캐스팅, 다운캐스팅
├─ instanceof 연산자 : 참조변수 타입 확인
├─ 참조변수와 인스턴스 연결 : 멤버변수는 참조변수, 메서드는 인스턴스
├─ 매개변수의 다형성 : 조상타입 매개변수로 여러 자손 객체 처리
└─ 여러 종류 객체를 배열로 다루기 : 조상타입 배열

---
⚡ 7단계: 추상클래스와 인터페이스
├─ 추상클래스 정의 : 미완성 설계도, abstract 클래스
├─ 추상메서드 : 선언부만 있는 메서드, 구현 강제
├─ 인터페이스 개념 : 일종의 추상클래스, 추상메서드 집합
├─ 인터페이스 구현 : implements 키워드
├─ 인터페이스의 다중상속 : 여러 인터페이스 동시 구현
└─ 디폴트 메서드와 static 메서드 : JDK 1.8 추가

---
⚡ 8단계: 내부 클래스와 예외처리
├─ 내부 클래스 종류 : 인스턴스 클래스, 스태틱 클래스, 지역 클래스
├─ 익명 클래스 : 이름이 없는 클래스, 일회용 클래스
├─ 예외처리 개념 : 프로그램 오류, 예외 클래스 계층구조
├─ try-catch문 : 예외 발생 시 처리
├─ finally 블럭 : 예외 발생 여부와 관계없이 실행
└─ 사용자정의 예외 : Exception 상속, 커스텀 예외

---
⚡ 9단계: java.lang 패키지와 유용한 클래스
├─ Object 클래스 : equals(), hashCode(), toString()
├─ String 클래스 : 문자열 처리, 불변 객체
├─ StringBuffer와 StringBuilder : 가변 문자열
├─ 래퍼 클래스 : 기본형을 객체로 감싸는 클래스
├─ Math 클래스 : 수학 관련 static 메서드
└─ 유용한 클래스들 : Objects, Random, Scanner

---
⚡ 10단계: 제네릭과 열거형
├─ 제네릭스 개념 : 타입 안전성, 형변환 생략
├─ 제네릭 클래스 선언 : 타입 변수 사용
├─ 와일드 카드 : <? extends T>, <? super T>
├─ 제네릭 메서드 : 메서드에 타입 변수 선언
├─ 열거형 정의와 사용 : enum 키워드
└─ 열거형에 멤버 추가 : 생성자, 메서드 추가

---
⚡ 11단계: 애노테이션과 모던 자바 기능
├─ 애노테이션 개념 : 메타데이터, 주석과 차이
├─ 표준 애노테이션 : @Override, @Deprecated, @SuppressWarnings
├─ 메타 애노테이션 : @Target, @Retention
├─ 레코드 클래스 : 불변 데이터 클래스, final 클래스
├─ 실드 클래스 : 상속 제한, permits 키워드
└─ 모듈 시스템 : module-info.java, 캡슐화 강화

---
⚡ 12단계: 실무 객체지향 설계
├─ 객체지향 설계 원칙 : 캡슐화, 상속, 다형성 활용
├─ 디자인 패턴 적용 : 생성, 구조, 행위 패턴
├─ 코드 품질 향상 : 가독성, 재사용성, 확장성
├─ 리팩토링 기법 : 설계 개선, 코드 정리
├─ 테스트 가능한 설계 : 의존성 분리, 인터페이스 활용
└─ 객체지향 안티패턴 : 피해야 할 설계 실수

---
🎯 실무 핵심 포인트
├─ 코드 리뷰 기준 : 객체지향 설계 품질
├─ 리팩토링 전략 : 설계 개선, 기술 부채 해결
├─ 테스트 용이성 : 의존성 주입, 모킹 가능
├─ 성능 고려사항 : 객체 생성 비용, 메모리 사용
├─ 유지보수성 : 변경 영향 최소화, 확장성
└─ 도메인 모델링 : 비즈니스 로직 표현력

--- 