package com.JavaCoreTil.io.io_practice;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.Vector;

import com.JavaCoreTil.io.common.FilePathManager;

public class Stage3_ByteBasedFilterStreams {

	private static final boolean DELETE_FLAG = true;

	public static void main(String[] args) {

		Stage3_ByteBasedFilterStreams demo = new Stage3_ByteBasedFilterStreams();

		FilePathManager.ensureDirectoryExists();

		try {

			System.out.printf("= demo_sequenceInputStream %s%n", "=".repeat(30));
			demo.demo_sequenceInputStream();

		} finally {
			FilePathManager.cleanupFiles(DELETE_FLAG);
		}
	}

	private void demo_sequenceInputStream() {
		String[] contents = {
			"This is first. \n",
			"This is second. \n",
			"This is thrid. \n"
		};

		String[] fileNames = {"part1.txt", "part2.txt", "part3.txt"};

		try {
			for (int i = 0; i < fileNames.length; i++) {
				try (FileOutputStream fos = new FileOutputStream(FilePathManager.getFilePath(fileNames[i]))) {
					fos.write(contents[i].getBytes());
				}
			}

			Vector<InputStream> streams = new Vector<>();

			for (String fileName : fileNames) {
				streams.add(new FileInputStream(FilePathManager.getFilePath(fileName)));
			}

			try (SequenceInputStream sis = new SequenceInputStream(streams.elements())) {
				byte[] buffer = new byte[1024];
				int bytesRead;
				StringBuilder result = new StringBuilder();

				while ((bytesRead = sis.read(buffer)) != -1) {
					result.append(new String(buffer, 0, bytesRead));
				}

				System.out.println(result.toString());
			}

			try (SequenceInputStream simple = new SequenceInputStream(
				new FileInputStream(FilePathManager.getFilePath("part1.txt")),
				new FileInputStream(FilePathManager.getFilePath("part2.txt")))) {
				System.out.println("\n간단한 두 스트림 연결:");
				String simpleResult = new String(simple.readAllBytes());
				System.out.print(simpleResult);
			}

		} catch (
			IOException e) {
			throw new RuntimeException(e);
		}
	}

}
