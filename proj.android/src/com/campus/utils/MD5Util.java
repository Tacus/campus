package com.campus.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.util.Log;

public class MD5Util {
	public static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String toHexString(byte[] b) {
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	public static String generateMd5(String filename, boolean allowFmCache) {
		File srcFile = new File(filename);
		if (!srcFile.exists())
			return "N/A";
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
		FileInputStream is;
		try {
			is = new FileInputStream(srcFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "";
		}

		try {
			FileChannel ch = is.getChannel();
			long blockSize = 1024 * 100; // or even bigger
			long remainder = srcFile.length();
			for (long position = 0; remainder > 0; position += blockSize) {
				long size = Math.min(remainder, blockSize);
				MappedByteBuffer buffer = ch.map(FileChannel.MapMode.READ_ONLY,
						position, size);
				md.update(buffer);
				remainder -= size;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String md5result = toHexString(md.digest());
		Log.e("", "fileName:" + filename + "----------------------md5:"
				+ md5result);

		return md5result;
	}

	public static String generateMd5(byte bytes[]) {
		String md5 = "";
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(bytes);
			md5 = toHexString(md.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
		return md5;
	}

	public static byte[] stream2bytes(InputStream is) {
		ByteArrayOutputStream baos;
		byte[] buffer = null;
		try {
			baos = new ByteArrayOutputStream(is.available());
			int len = 0;
			byte[] b = new byte[1024];
			try {
				while ((len = is.read(b, 0, b.length)) != -1) {
					baos.write(b, 0, len);
				}
				buffer = baos.toByteArray();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return buffer;
	}

}
