package com.JavaCoreTil.io.io_practice;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

import com.JavaCoreTil.io.common.FilePathManager;

public class Stage8_BufferAndChannel {

	private static final String BASE_PATH = "src/main/java/com/JavaCoreTil/io/file/BufferAndChannel";
	private static final String FILE_PATH = BASE_PATH + "/example.txt";

	public static void main(String[] args) throws IOException {
		Stage8_BufferAndChannel stage = new Stage8_BufferAndChannel();

		FilePathManager.ensureCustomDirectoryExists(BASE_PATH);

		print("FileCreate");
		stage.FileCreate();

		print("FileRead");
		stage.FileRead();

		print("FileUpdateWithOverwrite");
		stage.FileUpdateWithOverwrite();
		stage.FileRead();

		print("FileUpdateWithPartialOverwrite");
		stage.FileUpdateWithPartialOverwrite();
		stage.FileRead();

		print("FileUpdateWithAppend");
		stage.FileUpdateWithAppend();
		stage.FileRead();
	}

	public static void print(String msg) {
		System.out.printf("%n = %s %s %n%n", msg, "=".repeat(30));
	}

	private void FileCreate() throws IOException {
		try (FileChannel channel = FileChannel.open(
			Paths.get(FILE_PATH),
			StandardOpenOption.CREATE,
			StandardOpenOption.WRITE)
		) {
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			String data = LocalDateTime.now() + " This is test file | 안녕!";
			buffer.put(data.getBytes(StandardCharsets.UTF_8));
			buffer.flip();
			int writen = channel.write(buffer);
			System.out.println("Bytes written = " + writen);
		}

		System.out.println("File.Create()");
	}

	private void FileRead() throws IOException {
		try (FileChannel channel = FileChannel.open(
			Paths.get(FILE_PATH),
			StandardOpenOption.READ
		)) {
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			channel.read(buffer);
			buffer.flip();

			byte[] bytes = new byte[buffer.remaining()];
			buffer.get(bytes);

			String result = new String(bytes, StandardCharsets.UTF_8);
			System.out.println(result);
		}
	}

	private void FileUpdateWithOverwrite() throws IOException {
		try (FileChannel channel = FileChannel.open(
			Paths.get(FILE_PATH),
			StandardOpenOption.WRITE,
			StandardOpenOption.TRUNCATE_EXISTING
		)) {
			ByteBuffer buffer = ByteBuffer.wrap("UPDATED CONTENT".getBytes(StandardCharsets.UTF_8));

			int bytesWritten = channel.write(buffer);
			System.out.println("Bytes written: " + bytesWritten);
		}
	}

	private void FileUpdateWithPartialOverwrite() throws IOException {
		try (FileChannel channel = FileChannel.open(
			Paths.get(FILE_PATH),
			StandardOpenOption.WRITE
		)) {
			ByteBuffer buffer = ByteBuffer.wrap("NEW CONTENT!!!".getBytes(StandardCharsets.UTF_8));
			channel.position(8);

			int bytesWritten = channel.write(buffer);
			System.out.println("Bytes written: " + bytesWritten);
		}
	}

	private void FileUpdateWithAppend() throws IOException {
		try (FileChannel channel = FileChannel.open(
			Paths.get(FILE_PATH),
			StandardOpenOption.WRITE
		)) {
			ByteBuffer buffer = ByteBuffer.wrap(" PATCH NOTE : v1.0.0".getBytes(StandardCharsets.UTF_8));
			channel.position(channel.size());

			int bytesWritten = channel.write(buffer);
			System.out.println("Bytes written: " + bytesWritten);
		}
	}

}
