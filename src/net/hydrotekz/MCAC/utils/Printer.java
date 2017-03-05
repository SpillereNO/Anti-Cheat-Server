package net.hydrotekz.MCAC.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

import net.hydrotekz.MCAC.net.DatabaseHandler;

public class Printer {

	/***************************************************************
	 *  Copyright notice
	 *
	 *  (c) 2016 Spillere.no
	 *  All rights reserved
	 *
	 * It's not allowed to copy, reuse and change the code without the permission from the copyright holder
	 *
	 ********************************/

	public static void log(String message) {
		if (message == null) return;
		try {
			if (message.length() < 1) return;

			File file = new File(System.getProperty("user.dir") + "\\" + "Logs");
			if (!file.exists()) {
				file.mkdirs();
			}
			BufferedWriter out = new BufferedWriter(new FileWriter("Logs\\" + getDate() + ".log", true));
			out.write(getClock() + ": " + message);
			out.newLine();
			out.close();

			System.out.println("\n" + getClock() + ": " + message);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void logError(String error, String message) {
		if (message == null) return;
		try {
			if (message.length() < 1) return;

			File file = new File(System.getProperty("user.dir") + File.separator + "Errors");
			if (!file.exists()) {
				file.mkdirs();
			}
			BufferedWriter out = new BufferedWriter(new FileWriter("Errors" + File.separator + error + ".log", true));
			out.write(getTime() + ": " + message);
			out.newLine();
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void log(Exception e){
		if (e instanceof CommunicationsException){
			try {
				Thread.sleep(60000);
			} catch (Exception ex){
				// Failed to sleep
			}
			DatabaseHandler.reconnect();

		} else {
			log("---");
			log(getErrorText(e));
			String error = e.toString();
			if (error.contains(".")){
				String[] split = error.split("\\.");
				error = split[split.length-1];
			}
			logError(error, "---");
			logError(error, getErrorText(e));
		}
	}

	public static String getErrorText(Exception e){
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return(sw.toString());
	}

	private static String getDate(){
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		Calendar cal = Calendar.getInstance();
		return dateFormat.format(cal.getTime());
	}
	
	
	private static String getTime(){
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		return dateFormat.format(cal.getTime());
	}

	private static String getClock(){
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		return dateFormat.format(cal.getTime());
	}
}