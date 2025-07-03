---
✅ 1단계: I/O 기초 이론
├─ 입출력(I/O) 개념 : 프로그램과 외부 데이터 교환
├─ 스트림(Stream) 개념 : 데이터 흐름, 단방향성
├─ 바이트 기반 스트림 : InputStream/OutputStream, 바이너리 데이터
├─ 문자 기반 스트림 : Reader/Writer, 텍스트 데이터
├─ 보조 스트림 : 기능 향상, 데코레이터 패턴
└─ 예외 처리 : IOException, try-with-resources

---
✅ 2단계: 바이트 기반 스트림
├─ InputStream/OutputStream : 바이트 스트림의 최상위 클래스
├─ FileInputStream/FileOutputStream : 파일 바이트 입출력
├─ ByteArrayInputStream/ByteArrayOutputStream : 메모리 바이트 배열 처리
├─ 기본 읽기/쓰기 메서드 : read(), write(), available()
├─ 스트림 연결과 체이닝 : 스트림 조합 활용
└─ 리소스 관리 : close(), try-with-resources 패턴

---
⚡ 3단계: 바이트 기반 보조 스트림
├─ FilterInputStream/FilterOutputStream : 보조 스트림 기반 클래스
├─ BufferedInputStream/BufferedOutputStream : 버퍼링을 통한 성능 향상
├─ DataInputStream/DataOutputStream : 기본 타입 데이터 입출력
├─ PrintStream : 형식화된 출력, System.out
├─ SequenceInputStream : 여러 스트림 연결
└─ 보조 스트림 조합 : 다중 기능 적용

---
⚡ 4단계: 문자 기반 스트림
├─ Reader/Writer : 문자 스트림의 최상위 클래스
├─ FileReader/FileWriter : 파일 문자 입출력
├─ StringReader/StringWriter : 문자열 입출력
├─ PipedReader/PipedWriter : 스레드 간 통신
├─ 문자 인코딩 처리 : UTF-8, 다국어 지원
└─ 바이트 스트림과의 차이점 : 문자 단위 처리

---
⚡ 5단계: 문자 기반 보조 스트림
├─ BufferedReader/BufferedWriter : 문자 버퍼링, readLine()
├─ InputStreamReader/OutputStreamWriter : 바이트↔문자 변환
├─ PrintWriter : 형식화된 문자 출력
├─ 문자 인코딩 지정 : Charset 활용
├─ 텍스트 파일 효율적 처리 : 줄 단위 읽기
└─ Scanner 활용 : 편리한 입력 처리

---
⚡ 6단계: 표준 입출력과 File 클래스
├─ 표준 입출력 : System.in, System.out, System.err
├─ 표준 입출력 대상 변경 : setIn(), setOut(), setErr()
├─ File 클래스 : 파일/디렉토리 정보, 경로 처리
├─ 파일 속성 조회 : exists(), length(), lastModified()
├─ 디렉토리 조작 : mkdir(), list(), listFiles()
└─ RandomAccessFile : 임의 접근, seek 포인터

---
⚡ 7단계: 직렬화 (Serialization)
├─ 직렬화 개념 : 객체 → 바이트 스트림 변환
├─ ObjectInputStream/ObjectOutputStream : 객체 입출력
├─ Serializable 인터페이스 : 직렬화 가능 클래스
├─ transient 키워드 : 직렬화 제외 필드
├─ serialVersionUID : 클래스 버전 관리
└─ 직렬화 보안 이슈 : 역직렬화 공격 방어

---
⚡ 8단계: NIO 기초 (New I/O)
├─ NIO 등장 배경 : 기존 I/O 한계, 성능 문제
├─ Buffer 클래스 : ByteBuffer, position/limit/capacity
├─ Channel 개념 : 양방향 통신, FileChannel
├─ FileChannel : 파일 채널, 효율적 파일 처리
├─ Buffer 조작 : flip(), rewind(), clear(), compact()
└─ 직접/간접 버퍼 : 성능 차이, 사용 시나리오

---
⚡ 9단계: NIO.2 파일 시스템 (Java 7+)
├─ Path 인터페이스 : 경로 추상화, 플랫폼 독립성
├─ Files 유틸리티 : 파일 조작 편의 메서드
├─ 파일 속성 관리 : 권한, 시간 정보, 메타데이터
├─ 디렉토리 순회 : walkFileTree(), DirectoryStream
├─ 파일 변경 감지 : WatchService, 실시간 모니터링
└─ Symbolic Link 처리 : 링크 파일 다루기

---
⚡ 10단계: 고성능 I/O 기법
├─ 메모리 맵핑 : MappedByteBuffer, 대용량 파일 처리
├─ Zero-Copy I/O : transferTo/transferFrom
├─ 비동기 파일 I/O : AsynchronousFileChannel
├─ 논블로킹 I/O : Selector, SocketChannel
├─ 성능 최적화 : 버퍼 크기, 블로킹 vs 논블로킹
└─ 벤치마킹 : 실무 성능 측정

---
⚡ 11단계: 스트림 API와 파일 I/O (Java 8+)
├─ Files.lines() : 파일을 스트림으로 처리
├─ 함수형 파일 처리 : filter, map, collect
├─ 대용량 파일 스트림 : 메모리 효율적 처리
├─ 병렬 스트림 I/O : parallelStream(), 성능 향상
├─ 스트림과 예외 처리 : UncheckedIOException
└─ 스트림 기반 로그 분석 : 실무 활용 예제

---
⚡ 12단계: 실무 I/O 패턴과 고급 기법
├─ 파일 압축 : ZipInputStream/OutputStream, GZIP
├─ 텍스트 처리와 인코딩 : Charset, UTF-8 처리
├─ 설정 파일 처리 : Properties, JSON, XML
├─ 로그 파일 분석 : 대용량 로그 효율적 처리
├─ 임시 파일 관리 : createTempFile(), 자동 정리
└─ I/O 모니터링 : 성능 측정, 병목 지점 분석

