package com.JavaCoreTil.io.io_practice.Binary_Stream.main;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class ByteStream {

	public static void main(String[] args) {

		ByteStream byteStream = new ByteStream();

		System.out.printf("= Byte_IOEx1 %s%n", "=".repeat(30));
		byteStream.Byte_IOEx1();

		System.out.printf("%n= Byte_IOEx2 %s%n", "=".repeat(30));
		byteStream.Byte_IOEx2();

		System.out.printf("%n= Byte_IOEx3 %s%n", "=".repeat(30));
		byteStream.Byte_IOEx3();

		System.out.printf("%n= Byte_IOEx4 %s%n", "=".repeat(30));
		byteStream.Byte_IOEx4();

	}

	private void Byte_IOEx1() {
		byte[] inSrc = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
		byte[] outSrc = null;

		ByteArrayInputStream input = null;
		ByteArrayOutputStream output = null;

		input = new ByteArrayInputStream(inSrc);
		output = new ByteArrayOutputStream();

		int data = 0;

		while ((data = input.read()) != -1) {
			output.write(data);
		}

		outSrc = output.toByteArray();

		System.out.println("Input Source : " + Arrays.toString(inSrc));
		System.out.println("Output Source : " + Arrays.toString(outSrc));
	}

	private void Byte_IOEx2() {
		byte[] inSrc = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
		byte[] outSrc = null;
		byte[] temp = new byte[10];

		ByteArrayInputStream input = null;
		ByteArrayOutputStream output = null;

		input = new ByteArrayInputStream(inSrc);
		output = new ByteArrayOutputStream();

		input.read(temp, 0, temp.length);        // 읽어온 데이터를 temp에 담는다
		output.write(temp, 5, 5);            // temp[5]부터 5개의 데이터를 write 한다.

		outSrc = output.toByteArray();

		System.out.println("Input Source : " + Arrays.toString(inSrc));
		System.out.println("temp : " + Arrays.toString(temp));
		System.out.println("Output Source : " + Arrays.toString(outSrc));
	}

	private void Byte_IOEx3() {
		byte[] inSrc = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
		byte[] outSrc = null;
		byte[] temp = new byte[4];

		ByteArrayInputStream input = null;
		ByteArrayOutputStream output = null;

		input = new ByteArrayInputStream(inSrc);
		output = new ByteArrayOutputStream();

		System.out.println("Input Source  : " + Arrays.toString(inSrc));

		try {
			while (input.available() > 0) {
				input.read(temp);
				output.write(temp);

				outSrc = output.toByteArray();
				printArray(temp, outSrc);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	private void Byte_IOEx4() {
		byte[] inSrc = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
		byte[] outSrc = null;
		byte[] temp = new byte[4];

		ByteArrayInputStream input = null;
		ByteArrayOutputStream output = null;

		input = new ByteArrayInputStream(inSrc);
		output = new ByteArrayOutputStream();

		try {
			while (input.available() > 0) {
				int len = input.read(temp);            // 읽어 온 데이터의 개수를 반환한다.
				output.write(temp, 0, len);    // 읽어 온 만큼만 write 한다.
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		outSrc = output.toByteArray();

		System.out.println("Input Source : " + Arrays.toString(inSrc));
		System.out.println("temp : " + Arrays.toString(temp));
		System.out.println("Output Source : " + Arrays.toString(outSrc));
	}

	private void printArray(byte[] temp, byte[] outSrc) {
		System.out.printf("temp 		  : %s %n", Arrays.toString(temp));
		System.out.printf("Output Source : %s %n", Arrays.toString(outSrc));
	}
}
