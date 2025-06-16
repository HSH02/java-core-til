package com.JavaCoreTil.thread.practice;

import java.util.ArrayList;
import java.util.List;

public class ThreadMemoryStructure {
    
    // Heap 영역 - 모든 스레드가 공유
    private static int sharedCounter = 0;
    private static String sharedString = "공유 문자열";
    private static List<String> sharedList = new ArrayList<>();
    
    public static void main(String[] args) throws InterruptedException {
        demonstrateMemoryStructure();

        visualizeMemoryStructure();
    }
    
    public static void demonstrateMemoryStructure() throws InterruptedException {
        System.out.println("=== Thread 메모리 구조 ===");
        
        // 여러 스레드가 메모리를 어떻게 사용하는지 관찰
        Thread thread1 = new Thread(() -> accessMemory("Thread-1"));
        Thread thread2 = new Thread(() -> accessMemory("Thread-2"));
        
        thread1.start();
        thread2.start();
        
        thread1.join();
        thread2.join();
        
        System.out.println("최종 공유 상태:");
        System.out.println("- sharedCounter: " + sharedCounter);
        System.out.println("- sharedList: " + sharedList);
    }
    
    private static void accessMemory(String threadName) {
        // Thread Stack 영역 - 각 스레드마다 독립적
        int localVariable = 0;           // 스택에 저장
        String localString = "로컬";     // 스택에 저장 (참조)
        Object localObject = new Object(); // 객체는 Heap, 참조는 Stack
        
        System.out.println("\n=== " + threadName + " 메모리 접근 ===");
        
        for (int i = 0; i < 3; i++) {
            // 로컬 변수 수정 (각 스레드 독립적)
            localVariable++;
            System.out.println(threadName + " 로컬 변수: " + localVariable);
            
            // 공유 변수 수정 (모든 스레드 공유, 동시성 문제 발생 가능)
            sharedCounter++;
            System.out.println(threadName + " 공유 카운터: " + sharedCounter);
            
            // 공유 컬렉션 수정
            sharedList.add(threadName + "-" + i);
            
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        // 메모리 영역 정리
        System.out.println(threadName + " 메모리 정리:");
        System.out.println("- 로컬 변수 최종값: " + localVariable);
        System.out.println("- 로컬 객체 해시: " + localObject.hashCode());
        System.out.println("- 스택 정보: 이 스레드 종료 시 자동 해제");
    }
    
    // 메모리 구조 시각화
    public static void visualizeMemoryStructure() {
        System.out.println("\n=== 메모리 구조 시각화 ===");
        System.out.println("┌─────────────────────────────────┐");
        System.out.println("│           JVM Memory            │");
        System.out.println("├─────────────────────────────────┤");
        System.out.println("│  Heap (모든 스레드 공유)         │");
        System.out.println("│  ├─ sharedCounter              │");
        System.out.println("│  ├─ sharedList                 │");
        System.out.println("│  └─ new Object() 생성 객체들    │");
        System.out.println("├─────────────────────────────────┤");
        System.out.println("│  Thread-1 Stack (독립)         │");
        System.out.println("│  ├─ localVariable              │");
        System.out.println("│  ├─ localString (참조)          │");
        System.out.println("│  └─ 메서드 호출 스택             │");
        System.out.println("├─────────────────────────────────┤");
        System.out.println("│  Thread-2 Stack (독립)         │");
        System.out.println("│  ├─ localVariable (다른 값)      │");
        System.out.println("│  ├─ localString (다른 참조)      │");
        System.out.println("│  └─ 메서드 호출 스택             │");
        System.out.println("└─────────────────────────────────┘");
    }
}