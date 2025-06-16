package com.JavaCoreTil.thread.practice;

public class JVMThreadModel {
    
    public static void main(String[] args) throws InterruptedException {
        demonstrateJVMThreadMapping();
        demonstratePlatformDifferences();
    }
    
    // JVM Thread가 OS Thread에 매핑되는 방식
    public static void demonstrateJVMThreadMapping() {
        System.out.println("=== JVM Thread 모델 ===");
        
        // JVM 정보 출력
        System.out.println("JVM 정보:");
        System.out.println("- Java Version: " + System.getProperty("java.version"));
        System.out.println("- OS: " + System.getProperty("os.name"));
        System.out.println("- 사용 가능한 프로세서: " + 
                          Runtime.getRuntime().availableProcessors());
        
        // Thread 생성 시 OS Thread 매핑
        Thread javaThread = new Thread(() -> {
            // Java Thread ID와 Native Thread ID
            System.out.println("Java Thread ID: " + Thread.currentThread().getId());
            System.out.println("Thread Name: " + Thread.currentThread().getName());
            
            // 현재 스레드 정보
            ThreadGroup group = Thread.currentThread().getThreadGroup();
            System.out.println("Thread Group: " + group.getName());
            System.out.println("Active Threads in Group: " + group.activeCount());
        });
        
        javaThread.setName("JVM-OS-매핑-스레드");
        javaThread.start();
        
        try {
            javaThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    // 플랫폼별 차이점
    public static void demonstratePlatformDifferences() {
        System.out.println("\n=== 플랫폼별 Thread 차이점 ===");
        
        String os = System.getProperty("os.name").toLowerCase();
        
        if (os.contains("win")) {
            System.out.println("🪟 Windows 환경:");
            System.out.println("- 1:1 매핑 (Java Thread ↔ Windows Thread)");
            System.out.println("- 스케줄링: 선점형 멀티태스킹");
            System.out.println("- 우선순위: 7단계로 매핑");
        } else if (os.contains("linux")) {
            System.out.println("🐧 Linux 환경:");
            System.out.println("- 1:1 매핑 (Java Thread ↔ Native Thread)");
            System.out.println("- 스케줄링: CFS (Completely Fair Scheduler)");
            System.out.println("- 우선순위: nice 값으로 매핑");
        } else if (os.contains("mac")) {
            System.out.println("🍎 macOS 환경:");
            System.out.println("- 1:1 매핑 (Java Thread ↔ Mach Thread)");
            System.out.println("- 스케줄링: BSD 기반");
            System.out.println("- 우선순위: QoS 클래스 활용");
        }
        
        // Thread 스택 크기 (플랫폼별 차이)
        System.out.println("기본 스택 크기: " + 
                          getApproximateStackSize() + " (근사값)");
    }
    
    private static String getApproximateStackSize() {
        // 플랫폼별 기본 스택 크기 (근사값)
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) return "320KB";
        else if (os.contains("linux")) return "1MB";
        else if (os.contains("mac")) return "1MB";
        else return "플랫폼 정보 없음";
    }
}