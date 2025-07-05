package com.JavaCoreTil.io.io_practice.Binary_Stream.support;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;

import com.JavaCoreTil.io.common.FilePathManager;

public class Stream {
	public static void main(String[] args) {
		Stream stream = new Stream();

		System.out.printf("= BufferedOutputStreamEx1 %s%n", "=".repeat(30));
		stream.BufferedOutputStreamEx1();

		System.out.printf("= DataOutputStreamEx1 %s%n", "=".repeat(30));
		stream.DataOutputStreamEx1();

		System.out.printf("= DataOutputStreamEx2 %s%n", "=".repeat(30));
		stream.DataOutputStreamEx2();

	}

	private void BufferedOutputStreamEx1() {
		try {
			FileOutputStream fos = new FileOutputStream(FilePathManager.getFilePath("123.txt"));
			BufferedOutputStream bos = new BufferedOutputStream(fos, 5);

			for (int i = '1'; i < '9'; i++) {
				bos.write(i);
			}

			fos.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void DataOutputStreamEx1() {
		FileOutputStream fos = null;
		DataOutputStream dos = null;

		try {
			fos = new FileOutputStream(FilePathManager.getFilePath("sample.data"));
			dos = new DataOutputStream(fos);
			dos.writeInt(10);
			dos.writeFloat(20.0f);
			dos.writeBoolean(true);

			dos.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void DataOutputStreamEx2() {
		ByteArrayOutputStream bos = null;
		DataOutputStream dos = null;

		byte[] result = null;

		try {
			bos = new ByteArrayOutputStream();
			dos = new DataOutputStream(bos);
			dos.writeInt(10);
			dos.writeFloat(20.0f);
			dos.writeBoolean(true);

			result = bos.toByteArray();

			String[] hex = new String[result.length];

			for (int i = 0; i < result.length; i++) {
				if (result[i] < 0) {
					hex[i] = String.format("%02x", result[i] + 256);
				} else {
					hex[i] = String.format("%02x", result[i]);
				}
			}

			System.out.printf("10진수 : %s%n", Arrays.toString(result));
			System.out.printf("16진수 : %s%n", Arrays.toString(hex));

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void DataInputStreamEx1() {
		try {
			FileInputStream fis = new FileInputStream(FilePathManager.getFilePath("sample.dat"));
			DataInputStream dis = new DataInputStream(fis);

			System.out.println(dis.readInt());
			System.out.println(dis.readFloat());
			System.out.println(dis.readBoolean());
			dis.close();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
