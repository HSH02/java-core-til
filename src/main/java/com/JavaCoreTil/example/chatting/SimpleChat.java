package com.JavaCoreTil.example.chatting;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 간단한 1:1 채팅 프로그램
 * 서버와 클라이언트 기능을 모두 포함
 * 
 * 사용법:
 * 1. 서버 실행: java SimpleChat server [포트]
 * 2. 클라이언트 실행: java SimpleChat client [서버IP] [포트]
 */
public class SimpleChat {
    
    private static final int DEFAULT_PORT = 8080;
    private static final String DEFAULT_HOST = "localhost";
    private static final int BUFFER_SIZE = 1024;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    
    public static void main(String[] args) {
        // 시스템 인코딩 설정
        System.setProperty("file.encoding", "UTF-8");
        System.setProperty("sun.jnu.encoding", "UTF-8");
        
        if (args.length == 0) {
            printUsage();
            return;
        }
        
        String mode = args[0].toLowerCase();
        
        switch (mode) {
            case "server":
                int port = args.length > 1 ? Integer.parseInt(args[1]) : DEFAULT_PORT;
                startServer(port);
                break;
            case "client":
                String host = args.length > 1 ? args[1] : DEFAULT_HOST;
                int clientPort = args.length > 2 ? Integer.parseInt(args[2]) : DEFAULT_PORT;
                startClient(host, clientPort);
                break;
            default:
                printUsage();
        }
    }
    
    private static void printUsage() {
        System.out.println("=== 간단한 1:1 채팅 프로그램 ===");
        System.out.println("서버 실행: java SimpleChat server [포트]");
        System.out.println("클라이언트 실행: java SimpleChat client [서버IP] [포트]");
        System.out.println("예시:");
        System.out.println("  java SimpleChat server 8080");
        System.out.println("  java SimpleChat client localhost 8080");
    }
    
    /**
     * 채팅 서버 시작
     */
    private static void startServer(int port) {
        System.out.println("=== 채팅 서버 시작 ===");
        System.out.println("포트: " + port);
        System.out.println("클라이언트 연결 대기 중...");
        
        try (ServerSocketChannel serverChannel = ServerSocketChannel.open()) {
            serverChannel.bind(new InetSocketAddress(port));
            serverChannel.configureBlocking(true);
            
            // 클라이언트 연결 수락
            SocketChannel clientChannel = serverChannel.accept();
            System.out.println("클라이언트 연결됨: " + clientChannel.getRemoteAddress());
            
            // 채팅 시작
            startChatting(clientChannel, "서버");
            
        } catch (IOException e) {
            System.err.println("서버 오류: " + e.getMessage());
        }
    }
    
    /**
     * 채팅 클라이언트 시작
     */
    private static void startClient(String host, int port) {
        System.out.println("=== 채팅 클라이언트 시작 ===");
        System.out.println("서버: " + host + ":" + port);
        
        try (SocketChannel clientChannel = SocketChannel.open()) {
            clientChannel.connect(new InetSocketAddress(host, port));
            System.out.println("서버에 연결됨!");
            
            // 채팅 시작
            startChatting(clientChannel, "클라이언트");
            
        } catch (IOException e) {
            System.err.println("클라이언트 오류: " + e.getMessage());
        }
    }
    
    /**
     * 채팅 로직 (서버와 클라이언트 공통)
     */
    private static void startChatting(SocketChannel channel, String role) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Scanner scanner = new Scanner(System.in, "UTF-8");
        AtomicBoolean isRunning = new AtomicBoolean(true);
        
        System.out.println("\n=== 채팅 시작 ===");
        System.out.println("메시지를 입력하세요. (종료: 'quit' 또는 'exit')");
        System.out.println("----------------------------------------");
        
        // 메시지 수신 스레드
        executor.submit(() -> {
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            try {
                while (isRunning.get() && channel.isConnected()) {
                    buffer.clear();
                    int bytesRead = channel.read(buffer);
                    
                    if (bytesRead == -1) {
                        System.out.println("\n[" + getCurrentTime() + "] 상대방이 연결을 종료했습니다.");
                        isRunning.set(false);
                        break;
                    }
                    
                    if (bytesRead > 0) {
                        buffer.flip();
                        // 더 안전한 문자열 변환
                        String receivedMessage = decodeMessage(buffer, bytesRead);
                        
                        if (!receivedMessage.isEmpty()) {
                            System.out.println("\n[" + getCurrentTime() + "] 받은 메시지: " + receivedMessage);
                            System.out.print("[" + getCurrentTime() + "] 메시지 입력: ");
                        }
                    }
                }
            } catch (IOException e) {
                if (isRunning.get()) {
                    System.err.println("수신 오류: " + e.getMessage());
                }
            }
        });
        
        // 메시지 전송 스레드
        executor.submit(() -> {
            try {
                while (isRunning.get() && channel.isConnected()) {
                    System.out.print("[" + getCurrentTime() + "] 메시지 입력: ");
                    String message = scanner.nextLine().trim();
                    
                    if (!isRunning.get()) {
                        break;
                    }
                    
                    if (message.isEmpty()) {
                        continue;
                    }
                    
                    if (message.equalsIgnoreCase("quit") || message.equalsIgnoreCase("exit")) {
                        System.out.println("[" + getCurrentTime() + "] 채팅을 종료합니다.");
                        isRunning.set(false);
                        channel.close();
                        break;
                    }
                    
                    // 메시지 전송
                    sendMessage(channel, message);
                }
            } catch (IOException e) {
                if (isRunning.get()) {
                    System.err.println("전송 오류: " + e.getMessage());
                }
            } finally {
                isRunning.set(false);
                executor.shutdown();
                scanner.close();
            }
        });
        
        // 메인 스레드 대기
        try {
            while (!executor.isTerminated()) {
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * 메시지 전송 (안전한 인코딩)
     */
    private static void sendMessage(SocketChannel channel, String message) throws IOException {
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        ByteBuffer sendBuffer = ByteBuffer.allocate(messageBytes.length + 4);
        
        // 메시지 길이를 먼저 전송 (4바이트)
        sendBuffer.putInt(messageBytes.length);
        // 메시지 내용 전송
        sendBuffer.put(messageBytes);
        sendBuffer.flip();
        
        channel.write(sendBuffer);
    }
    
    /**
     * 메시지 수신 (안전한 디코딩)
     */
    private static String decodeMessage(ByteBuffer buffer, int bytesRead) {
        try {
            // 메시지 길이 읽기 (4바이트)
            if (buffer.remaining() < 4) {
                return "";
            }
            
            int messageLength = buffer.getInt();
            
            // 메시지 내용 읽기
            if (buffer.remaining() < messageLength) {
                return "";
            }
            
            byte[] messageBytes = new byte[messageLength];
            buffer.get(messageBytes);
            
            return new String(messageBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            // 안전한 폴백: 직접 디코딩
            buffer.rewind();
            byte[] bytes = new byte[bytesRead];
            buffer.get(bytes);
            return new String(bytes, StandardCharsets.UTF_8).trim();
        }
    }
    
    /**
     * 현재 시간을 HH:mm 형식으로 반환
     */
    private static String getCurrentTime() {
        return LocalTime.now().format(TIME_FORMATTER);
    }
} 