package com.JavaCoreTil.thread.practice;

import java.util.ArrayList;
import java.util.List;

/**
 * Thread 메모리 구조 데모
 */
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
        
        Thread thread1 = new Thread(() -> accessMemory("Thread-1"));
        Thread thread2 = new Thread(() -> accessMemory("Thread-2"));
        
        thread1.start();
        thread2.start();
        
        thread1.join();
        thread2.join();
        
        System.out.println("\n최종 공유 상태:");
        System.out.println("sharedCounter: " + sharedCounter);
        System.out.println("sharedList: " + sharedList);
    }
    
    private static void accessMemory(String threadName) {
        // Thread Stack 영역 - 각 스레드마다 독립적
        int localVariable = 0;
        String localString = "로컬";
        Object localObject = new Object();
        
        System.out.println("\n" + threadName + " 메모리 접근:");
        
        for (int i = 0; i < 3; i++) {
            // 로컬 변수 - 각 스레드 독립적
            localVariable++;
            System.out.println("  로컬 변수: " + localVariable);
            
            // 공유 변수 - 모든 스레드 공유
            sharedCounter++;
            System.out.println("  공유 카운터: " + sharedCounter);
            
            // 공유 컬렉션 수정
            sharedList.add(threadName + "-" + i);
            
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        System.out.println(threadName + " 정리:");
        System.out.println("  로컬 변수 최종값: " + localVariable);
        System.out.println("  로컬 객체 해시: " + localObject.hashCode());
        System.out.println("  // 스택 정보는 스레드 종료 시 자동 해제");
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
        
        System.out.println("\n// 핵심 포인트:");
        System.out.println("// 1. Heap: 모든 스레드가 공유 (동시성 문제 가능)");
        System.out.println("// 2. Stack: 각 스레드마다 독립적 (안전함)");
        System.out.println("// 3. 지역 변수는 스레드 안전, 공유 변수는 동기화 필요");
    }
}