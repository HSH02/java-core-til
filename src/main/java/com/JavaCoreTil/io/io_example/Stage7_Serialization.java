package com.JavaCoreTil.io.io_example;

import java.io.*;
import java.util.Date;
import com.JavaCoreTil.io.common.FilePathManager;

/**
 * I/O 로드맵 7단계: 직렬화 (Serialization)
 * 
 * 학습 목표:
 * 1. 직렬화 개념 - 객체 → 바이트 스트림 변환
 * 2. ObjectInputStream/ObjectOutputStream - 객체 입출력
 * 3. Serializable 인터페이스 - 직렬화 가능 클래스
 * 4. transient 키워드 - 직렬화 제외 필드
 * 5. serialVersionUID - 클래스 버전 관리
 * 6. 직렬화 보안 이슈 - 역직렬화 공격 방어
 */
public class Stage7_Serialization {
    
    private static final boolean DELETE_FLAG = false;
    
    public static void main(String[] args) {
        Stage7_Serialization demo = new Stage7_Serialization();
        
        FilePathManager.ensureDirectoryExists();
        
        System.out.println("=== I/O 로드맵 7단계: 직렬화 (Serialization) ===\n");
        
        try {
            // 1. 직렬화 기본 개념
            demo.example1_SerializationBasics();
            
            // 2. Serializable 인터페이스
            demo.example2_SerializableInterface();
            
            // 3. transient 키워드
            demo.example3_TransientKeyword();
            
            // 4. serialVersionUID
            demo.example4_SerialVersionUID();
            
            // 5. 복잡한 객체 직렬화
            demo.example5_ComplexObjectSerialization();
            
            // 6. 직렬화 보안
            demo.example6_SerializationSecurity();
            
            // 7. 실무 활용 - 설정 저장/로드
            demo.example7_PracticalSerialization();
            
            System.out.println("\n=== 7단계 학습 완료! ===");
            
        } finally {
            System.out.println("\n=== 파일 정리 ===");
            FilePathManager.cleanupFiles(DELETE_FLAG);
        }
    }
    
    /**
     * 예제 1: 직렬화 기본 개념
     * 객체를 바이트 스트림으로 변환하는 과정
     */
    public void example1_SerializationBasics() {
        System.out.println("=== 예제 1: 직렬화 기본 개념 ===");
        
        // 직렬화 가능한 간단한 객체
        SimplePerson person = new SimplePerson("김철수", 25, "서울시 강남구");
        
        System.out.println("원본 객체:");
        System.out.println("- 이름: " + person.getName());
        System.out.println("- 나이: " + person.getAge());
        System.out.println("- 주소: " + person.getAddress());
        
        // 객체 직렬화 (객체 → 바이트 스트림)
        String serializedFile = FilePathManager.getFilePath("person.ser");
        
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(serializedFile))) {
            
            oos.writeObject(person);
            System.out.println("\n객체가 파일에 직렬화되었습니다: " + serializedFile);
            
        } catch (IOException e) {
            System.err.println("직렬화 실패: " + e.getMessage());
            return;
        }
        
        // 객체 역직렬화 (바이트 스트림 → 객체)
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(serializedFile))) {
            
            SimplePerson restoredPerson = (SimplePerson) ois.readObject();
            
            System.out.println("\n역직렬화된 객체:");
            System.out.println("- 이름: " + restoredPerson.getName());
            System.out.println("- 나이: " + restoredPerson.getAge());
            System.out.println("- 주소: " + restoredPerson.getAddress());
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("역직렬화 실패: " + e.getMessage());
        }
        
        System.out.println("→ 직렬화: 객체를 바이트 스트림으로 변환하여 저장/전송 가능\n");
    }
    
    /**
     * 예제 2: Serializable 인터페이스
     * 직렬화 가능한 클래스 정의
     */
    public void example2_SerializableInterface() {
        System.out.println("=== 예제 2: Serializable 인터페이스 ===");
        
        // Serializable을 구현한 클래스
        Student student = new Student("이영희", 20, "컴퓨터공학과", 3.8);
        
        System.out.println("원본 Student 객체:");
        System.out.println(student);
        
        // 직렬화
        String studentFile = FilePathManager.getFilePath("student.ser");
        
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(studentFile))) {
            
            oos.writeObject(student);
            System.out.println("\nStudent 객체 직렬화 완료");
            
        } catch (IOException e) {
            System.err.println("Student 직렬화 실패: " + e.getMessage());
            return;
        }
        
        // 역직렬화
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(studentFile))) {
            
            Student restoredStudent = (Student) ois.readObject();
            System.out.println("\n역직렬화된 Student 객체:");
            System.out.println(restoredStudent);
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Student 역직렬화 실패: " + e.getMessage());
        }
        
        System.out.println("→ Serializable: 직렬화 가능한 클래스임을 명시하는 인터페이스\n");
    }
    
    /**
     * 예제 3: transient 키워드
     * 직렬화에서 제외할 필드 지정
     */
    public void example3_TransientKeyword() {
        System.out.println("=== 예제 3: transient 키워드 ===");
        
        // transient 필드가 있는 객체
        SecureUser user = new SecureUser("admin", "secret123", "관리자");
        
        System.out.println("원본 SecureUser 객체:");
        System.out.println("- 사용자명: " + user.getUsername());
        System.out.println("- 비밀번호: " + user.getPassword());
        System.out.println("- 역할: " + user.getRole());
        
        // 직렬화
        String userFile = FilePathManager.getFilePath("user.ser");
        
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(userFile))) {
            
            oos.writeObject(user);
            System.out.println("\nSecureUser 객체 직렬화 완료");
            
        } catch (IOException e) {
            System.err.println("SecureUser 직렬화 실패: " + e.getMessage());
            return;
        }
        
        // 역직렬화
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(userFile))) {
            
            SecureUser restoredUser = (SecureUser) ois.readObject();
            
            System.out.println("\n역직렬화된 SecureUser 객체:");
            System.out.println("- 사용자명: " + restoredUser.getUsername());
            System.out.println("- 비밀번호: " + restoredUser.getPassword());
            System.out.println("- 역할: " + restoredUser.getRole());
            
            // transient 필드는 null이 됨
            if (restoredUser.getPassword() == null) {
                System.out.println("- 비밀번호는 transient로 설정되어 직렬화되지 않았습니다");
            }
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("SecureUser 역직렬화 실패: " + e.getMessage());
        }
        
        System.out.println("→ transient: 직렬화에서 제외할 필드를 지정하는 키워드\n");
    }
    
    /**
     * 예제 4: serialVersionUID
     * 클래스 버전 관리
     */
    public void example4_SerialVersionUID() {
        System.out.println("=== 예제 4: serialVersionUID ===");
        
        // serialVersionUID가 있는 클래스
        VersionedData data = new VersionedData("데이터1", 100);
        
        System.out.println("원본 VersionedData 객체:");
        System.out.println(data);
        
        // 직렬화
        String dataFile = FilePathManager.getFilePath("versioned_data.ser");
        
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(dataFile))) {
            
            oos.writeObject(data);
            System.out.println("\nVersionedData 객체 직렬화 완료");
            System.out.println("- serialVersionUID: " + VersionedData.getSerialVersionUID());
            
        } catch (IOException e) {
            System.err.println("VersionedData 직렬화 실패: " + e.getMessage());
            return;
        }
        
        // 역직렬화
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(dataFile))) {
            
            VersionedData restoredData = (VersionedData) ois.readObject();
            System.out.println("\n역직렬화된 VersionedData 객체:");
            System.out.println(restoredData);
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("VersionedData 역직렬화 실패: " + e.getMessage());
        }
        
        System.out.println("→ serialVersionUID: 클래스 버전 호환성을 위한 고유 식별자\n");
    }
    
    /**
     * 예제 5: 복잡한 객체 직렬화
     * 중첩된 객체와 컬렉션의 직렬화
     */
    public void example5_ComplexObjectSerialization() {
        System.out.println("=== 예제 5: 복잡한 객체 직렬화 ===");
        
        // 복잡한 객체 생성
        Company company = new Company("테크컴퍼니", "서울시 강남구");
        
        // 직원들 추가
        company.addEmployee(new Employee("김철수", "개발자", 3000000));
        company.addEmployee(new Employee("이영희", "디자이너", 2800000));
        company.addEmployee(new Employee("박민수", "기획자", 3200000));
        
        System.out.println("원본 Company 객체:");
        System.out.println(company);
        
        // 직렬화
        String companyFile = FilePathManager.getFilePath("company.ser");
        
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(companyFile))) {
            
            oos.writeObject(company);
            System.out.println("\nCompany 객체 직렬화 완료");
            
        } catch (IOException e) {
            System.err.println("Company 직렬화 실패: " + e.getMessage());
            return;
        }
        
        // 역직렬화
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(companyFile))) {
            
            Company restoredCompany = (Company) ois.readObject();
            System.out.println("\n역직렬화된 Company 객체:");
            System.out.println(restoredCompany);
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Company 역직렬화 실패: " + e.getMessage());
        }
        
        System.out.println("→ 복잡한 객체: 중첩된 객체와 컬렉션도 직렬화 가능\n");
    }
    
    /**
     * 예제 6: 직렬화 보안
     * 역직렬화 공격 방어 방법
     */
    public void example6_SerializationSecurity() {
        System.out.println("=== 예제 6: 직렬화 보안 ===");
        
        // 안전한 직렬화 방법
        SafeData safeData = new SafeData("중요한 데이터", new Date());
        
        System.out.println("원본 SafeData 객체:");
        System.out.println("- 데이터: " + safeData.getData());
        System.out.println("- 생성 시간: " + safeData.getCreatedAt());
        
        // 직렬화
        String safeFile = FilePathManager.getFilePath("safe_data.ser");
        
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(safeFile))) {
            
            oos.writeObject(safeData);
            System.out.println("\nSafeData 객체 직렬화 완료");
            
        } catch (IOException e) {
            System.err.println("SafeData 직렬화 실패: " + e.getMessage());
            return;
        }
        
        // 역직렬화 (보안 검증 포함)
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(safeFile))) {
            
            // 보안 검증을 위한 필터 설정
            ois.setObjectInputFilter(new ObjectInputFilter() {
                @Override
                public Status checkInput(FilterInfo filterInfo) {
                    if (filterInfo.serialClass() == null) {
                        return Status.UNDECIDED;
                    }
                    
                    // SafeData 클래스만 허용
                    if (filterInfo.serialClass().equals(SafeData.class)) {
                        return Status.ALLOWED;
                    }
                    
                    return Status.REJECTED;
                }
            });
            
            SafeData restoredSafeData = (SafeData) ois.readObject();
            System.out.println("\n역직렬화된 SafeData 객체:");
            System.out.println("- 데이터: " + restoredSafeData.getData());
            System.out.println("- 생성 시간: " + restoredSafeData.getCreatedAt());
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("SafeData 역직렬화 실패: " + e.getMessage());
        }
        
        System.out.println("→ 직렬화 보안: 역직렬화 공격을 방어하기 위한 검증 필요\n");
    }
    
    /**
     * 예제 7: 실무 활용 - 설정 저장/로드
     * 직렬화를 활용한 애플리케이션 설정 관리
     */
    public void example7_PracticalSerialization() {
        System.out.println("=== 예제 7: 실무 활용 - 설정 저장/로드 ===");
        
        // 애플리케이션 설정 객체
        AppConfig config = new AppConfig();
        config.setServerPort(8080);
        config.setDatabaseUrl("jdbc:mysql://localhost:3306/mydb");
        config.setLogLevel("INFO");
        config.setMaxConnections(100);
        config.setLastModified(new Date());
        
        System.out.println("원본 설정:");
        System.out.println(config);
        
        // 설정 저장
        String configFile = FilePathManager.getFilePath("app_config.ser");
        
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(configFile))) {
            
            oos.writeObject(config);
            System.out.println("\n설정이 파일에 저장되었습니다: " + configFile);
            
        } catch (IOException e) {
            System.err.println("설정 저장 실패: " + e.getMessage());
            return;
        }
        
        // 설정 로드
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(configFile))) {
            
            AppConfig loadedConfig = (AppConfig) ois.readObject();
            System.out.println("\n로드된 설정:");
            System.out.println(loadedConfig);
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("설정 로드 실패: " + e.getMessage());
        }
        
        System.out.println("→ 실무 활용: 객체 직렬화로 복잡한 설정 데이터 저장/로드\n");
    }
    
    // 직렬화 가능한 클래스들
    
    /**
     * 간단한 직렬화 가능 클래스
     */
    static class SimplePerson implements Serializable {
        private String name;
        private int age;
        private String address;
        
        public SimplePerson(String name, int age, String address) {
            this.name = name;
            this.age = age;
            this.address = address;
        }
        
        // Getters
        public String getName() { return name; }
        public int getAge() { return age; }
        public String getAddress() { return address; }
    }
    
    /**
     * Serializable 인터페이스를 구현한 클래스
     */
    static class Student implements Serializable {
        private String name;
        private int age;
        private String major;
        private double gpa;
        
        public Student(String name, int age, String major, double gpa) {
            this.name = name;
            this.age = age;
            this.major = major;
            this.gpa = gpa;
        }
        
        @Override
        public String toString() {
            return String.format("Student{name='%s', age=%d, major='%s', gpa=%.1f}", 
                               name, age, major, gpa);
        }
    }
    
    /**
     * transient 키워드를 사용한 클래스
     */
    static class SecureUser implements Serializable {
        private String username;
        private transient String password; // 직렬화 제외
        private String role;
        
        public SecureUser(String username, String password, String role) {
            this.username = username;
            this.password = password;
            this.role = role;
        }
        
        // Getters
        public String getUsername() { return username; }
        public String getPassword() { return password; }
        public String getRole() { return role; }
    }
    
    /**
     * serialVersionUID가 있는 클래스
     */
    static class VersionedData implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private String data;
        private int value;
        
        public VersionedData(String data, int value) {
            this.data = data;
            this.value = value;
        }
        
        public static long getSerialVersionUID() {
            return serialVersionUID;
        }
        
        @Override
        public String toString() {
            return String.format("VersionedData{data='%s', value=%d}", data, value);
        }
    }
    
    /**
     * 복잡한 객체 (중첩된 객체 포함)
     */
    static class Company implements Serializable {
        private String name;
        private String address;
        private java.util.List<Employee> employees;
        
        public Company(String name, String address) {
            this.name = name;
            this.address = address;
            this.employees = new java.util.ArrayList<>();
        }
        
        public void addEmployee(Employee employee) {
            employees.add(employee);
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Company{name='").append(name).append("', address='").append(address).append("', employees=[");
            for (Employee emp : employees) {
                sb.append(emp.toString()).append(", ");
            }
            if (!employees.isEmpty()) {
                sb.setLength(sb.length() - 2); // 마지막 ", " 제거
            }
            sb.append("]}");
            return sb.toString();
        }
    }
    
    static class Employee implements Serializable {
        private String name;
        private String position;
        private int salary;
        
        public Employee(String name, String position, int salary) {
            this.name = name;
            this.position = position;
            this.salary = salary;
        }
        
        @Override
        public String toString() {
            return String.format("Employee{name='%s', position='%s', salary=%d}", 
                               name, position, salary);
        }
    }
    
    /**
     * 보안을 고려한 직렬화 클래스
     */
    static class SafeData implements Serializable {
        private String data;
        private Date createdAt;
        
        public SafeData(String data, Date createdAt) {
            this.data = data;
            this.createdAt = createdAt;
        }
        
        // Getters
        public String getData() { return data; }
        public Date getCreatedAt() { return createdAt; }
    }
    
    /**
     * 애플리케이션 설정 클래스
     */
    static class AppConfig implements Serializable {
        private int serverPort;
        private String databaseUrl;
        private String logLevel;
        private int maxConnections;
        private Date lastModified;
        
        // Setters
        public void setServerPort(int serverPort) { this.serverPort = serverPort; }
        public void setDatabaseUrl(String databaseUrl) { this.databaseUrl = databaseUrl; }
        public void setLogLevel(String logLevel) { this.logLevel = logLevel; }
        public void setMaxConnections(int maxConnections) { this.maxConnections = maxConnections; }
        public void setLastModified(Date lastModified) { this.lastModified = lastModified; }
        
        @Override
        public String toString() {
            return String.format("AppConfig{serverPort=%d, databaseUrl='%s', logLevel='%s', maxConnections=%d, lastModified=%s}", 
                               serverPort, databaseUrl, logLevel, maxConnections, lastModified);
        }
    }
} 