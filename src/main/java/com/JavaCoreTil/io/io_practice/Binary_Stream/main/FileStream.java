package com.JavaCoreTil.io.io_practice.Binary_Stream.main;

import java.io.FileInputStream;
import java.io.IOException;

public class FileStream {
	public static void main(String args[]) throws IOException {

		System.out.printf("= fileViewer %s%n", "=".repeat(30));
		fileViewer(args);
	}

	private static void fileViewer(String[] args) throws IOException {
		FileInputStream fis = new FileInputStream(args[0]);

		int data = 0;

		while ((data = fis.read()) != -1) {
			char c = (char) data;
			System.out.print(c);
		}
	}

}
