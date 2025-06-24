package com.JavaCoreTil.thread.practice;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * wait/notify 메커니즘을 보여주는 음식점 시뮬레이션
 * Producer(요리사) - Consumer(손님) 패턴으로 구현
 */
public class Thread_Ex_WaitNotify {
	
	private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");
	private static int stepCounter = 1;
	
	// 음식점 테이블 (공유 자원)
	static class RestaurantTable {
		private final String[] menuItems = {"🍩도넛", "🍔햄버거", "🍕피자"};
		private final int MAX_DISHES = 4; // 테이블 크기 줄여서 더 명확하게
		private final List<String> dishes = new ArrayList<>();
		private boolean restaurantClosed = false;
		
		// 요리사가 음식 추가 (Producer)
		public synchronized void addDish(String dish) {
			while (dishes.size() >= MAX_DISHES && !restaurantClosed) {
				printStep("🔴 [요리사] 테이블 가득참! 대기 중... " + getTableStatus());
				try {
					wait();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					return;
				}
			}
			
			if (restaurantClosed) return;
			
			dishes.add(dish);
			printStep("✅ [요리사] " + dish + " 완성! " + getTableStatus());
			
			notifyAll();
		}
		
		// 손님이 음식 주문 (Consumer)
		public synchronized boolean orderDish(String wantedDish) {
			String customerName = Thread.currentThread().getName();
			
			while (!dishes.contains(wantedDish) && !restaurantClosed) {
				if (dishes.isEmpty()) {
					printStep("⏳ [" + customerName + "] 테이블이 비어있어요. " + wantedDish + " 기다리는 중...");
				} else {
					printStep("⏳ [" + customerName + "] " + wantedDish + " 없어요. 기다리는 중... " + getTableStatus());
				}
				
				try {
					wait();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					return false;
				}
			}
			
			if (restaurantClosed && !dishes.contains(wantedDish)) {
				printStep("❌ [" + customerName + "] 음식점 마감! " + wantedDish + " 주문 실패");
				return false;
			}
			
			dishes.remove(wantedDish);
			printStep("🍽️ [" + customerName + "] " + wantedDish + " 맛있게 먹었어요! " + getTableStatus());
			
			notifyAll();
			return true;
		}
		
		// 음식점 마감
		public synchronized void closeRestaurant() {
			restaurantClosed = true;
			printStep("🏪 [음식점] 마감 시간입니다! 모든 손님께 알려드립니다.");
			notifyAll();
		}
		
		private String getTableStatus() {
			if (dishes.isEmpty()) {
				return "(테이블: 비어있음)";
			}
			return "(테이블: " + dishes + ")";
		}
		
		public String[] getMenuItems() {
			return menuItems;
		}
		
		public synchronized boolean isClosed() {
			return restaurantClosed;
		}
	}
	
	// 요리사 (Producer)
	static class Chef implements Runnable {
		private final RestaurantTable table;
		
		public Chef(RestaurantTable table) {
			this.table = table;
		}
		
		@Override
		public void run() {
			try {
				printHeader("👨‍🍳 요리사가 출근했습니다!");
				
				String[] menu = table.getMenuItems();
				
				// 각 메뉴를 3개씩 만들어서 총 9개 제작
				for (String dish : menu) {
					printHeader("👨‍🍳 " + dish + " 요리 시작!");
					for (int i = 0; i < 3; i++) {
						if (table.isClosed()) break;
						table.addDish(dish);
						Thread.sleep(300); // 요리 시간
					}
				}
				
				printHeader("👨‍🍳 모든 요리 완료! 퇴근합니다!");
				
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			} finally {
				table.closeRestaurant();
			}
		}
	}
	
	// 손님 (Consumer)
	static class Customer implements Runnable {
		private final RestaurantTable table;
		private final String favoriteFood;
		private final int orderCount;
		
		public Customer(RestaurantTable table, String favoriteFood, int orderCount) {
			this.table = table;
			this.favoriteFood = favoriteFood;
			this.orderCount = orderCount;
		}
		
		@Override
		public void run() {
			try {
				String customerName = Thread.currentThread().getName();
				printHeader("🚶 " + customerName + "이 입장했습니다!");
				
				int successCount = 0;
				
				for (int i = 1; i <= orderCount; i++) {
					printHeader("📋 " + customerName + " - " + i + "번째 " + favoriteFood + " 주문");
					
					if (table.orderDish(favoriteFood)) {
						successCount++;
						Thread.sleep(400); // 식사 시간
					} else {
						break;
					}
				}
				
				printHeader("🎉 " + customerName + " 식사 완료! (" + successCount + "/" + orderCount + " 성공)");
				
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}
	
	// 출력 메서드들
	private static synchronized void printStep(String message) {
		String time = LocalTime.now().format(TIME_FORMAT);
		System.out.printf("%2d. [%s] %s%n", stepCounter++, time, message);
	}
	
	private static synchronized void printHeader(String message) {
		String time = LocalTime.now().format(TIME_FORMAT);
		System.out.println();
		System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		System.out.printf("🎯 [%s] %s%n", time, message);
		System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
	}
	
	private static void printSeparator() {
		System.out.println("\n" + "=".repeat(80) + "\n");
	}
	
	public static void main(String[] args) throws InterruptedException {
		System.out.println("🏪 음식점 wait/notify 시뮬레이션 시작!");
		System.out.println("📝 wait/notify 메커니즘으로 요리사와 손님들이 협력합니다.");
		printSeparator();
		
		RestaurantTable table = new RestaurantTable();
		
		// 요리사 1명 생성
		Thread chef = new Thread(new Chef(table), "요리사");
		
		// 손님 3명 생성 (각각 다른 음식 선호, 각자 3개씩 주문)
		Thread customer1 = new Thread(new Customer(table, "🍩도넛", 3), "도넛손님");
		Thread customer2 = new Thread(new Customer(table, "🍔햄버거", 3), "햄버거손님");
		Thread customer3 = new Thread(new Customer(table, "🍕피자", 3), "피자손님");
		
		// 약간의 시차를 두고 시작
		chef.start();
		Thread.sleep(100);
		customer1.start();
		Thread.sleep(50);
		customer2.start();
		Thread.sleep(50);
		customer3.start();
		
		// 모든 작업 완료까지 대기 (타임아웃 설정)
		chef.join(15000);
		customer1.join(10000);
		customer2.join(10000);
		customer3.join(10000);
		
		// 혹시 아직 실행 중인 스레드가 있다면 강제 종료
		if (chef.isAlive() || customer1.isAlive() || customer2.isAlive() || customer3.isAlive()) {
			printHeader("⚠️ 비상 상황! 음식점을 강제 마감합니다.");
			table.closeRestaurant();
			
			chef.join(2000);
			customer1.join(2000);
			customer2.join(2000);
			customer3.join(2000);
		}
		
		printSeparator();
		printHeader("🎊 음식점 운영 완료! wait/notify 시뮬레이션 종료");
		System.out.println("📚 핵심 포인트:");
		System.out.println("   • wait(): 조건이 충족되지 않으면 대기 (락 반납)");
		System.out.println("   • notify()/notifyAll(): 대기 중인 스레드들에게 신호");
		System.out.println("   • while 루프: Spurious Wakeup 방지");
		System.out.println("   • 종료 조건: 무한 대기 방지");
		printSeparator();
	}
}


