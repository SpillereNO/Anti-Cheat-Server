package net.hydrotekz.MCAC.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Formatter;
import java.util.zip.CRC32;

public class Utils {

	/***************************************************************
	 *  Copyright notice
	 *
	 *  (c) 2016 Spillere.no
	 *  All rights reserved
	 *
	 * It's not allowed to copy, reuse and change the code without the permission from the copyright holder
	 *
	 ********************************/

	public static File refresh(File f){
		return new File(f.getAbsolutePath());
	}

	public static String getCRC32(byte[] bytes){
		CRC32 hash = new CRC32();
		for (byte b : bytes){
			hash.update(b);
		}
		return Long.toHexString(hash.getValue());
	}

	public static String getCRC32(File file){
		try {
			CRC32 hash = new CRC32();
			try (InputStream is = new BufferedInputStream(new FileInputStream(file))) {
				final byte[] buffer = new byte[1024];
				for (int read = 0; (read = is.read(buffer)) != -1;) {
					hash.update(buffer, 0, read);
				}
			}
			return Long.toHexString(hash.getValue());

		} catch (Exception e){
			Printer.log("Failed to hash file: " + file.getName());
			Printer.log(e);
		}
		return null;
	}

	public static String getSHA1(File file) {
		try {
			final MessageDigest messageDigest = MessageDigest.getInstance("SHA1");

			try (InputStream is = new BufferedInputStream(new FileInputStream(file))) {
				final byte[] buffer = new byte[1024];
				for (int read = 0; (read = is.read(buffer)) != -1;) {
					messageDigest.update(buffer, 0, read);
				}
			}

			// Convert the byte to hex format
			try (Formatter formatter = new Formatter()) {
				for (final byte b : messageDigest.digest()) {
					formatter.format("%02x", b);
				}
				return formatter.toString();
			}
		} catch (Exception e){
			Printer.log("Failed to hash file: " + file.getName());
			Printer.log(e);
		}
		return null;
	}
}