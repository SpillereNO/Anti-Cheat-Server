package net.hydrotekz.MCAC.net;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.hydrotekz.MCAC.utils.Printer;

public class DatabaseHandler {

	/***************************************************************
	 *  Copyright notice
	 *
	 *  (c) 2016 Spillere.no
	 *  All rights reserved
	 *
	 * It's not allowed to copy, reuse and change the code without the permission from the copyright holder
	 *
	 ********************************/

	private static String host = "ip";
	private static String database = "db";
	private static String username = "user";
	private static String password = "pass";
	public static Connection conn;

	/*
	 * Startup
	 */

	public static void connect() {
		try {
			String url = "jdbc:mysql://" + host + "/"+ database + "?autoReconnect=true";
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(url, username, password);
			Printer.log("Successfully connected to the MySQL server!");

		} catch (Exception e) {
			Printer.log(e);
			Printer.log("Failed connect to the MySQL server! Shutting down...");
			System.exit(0);
		}
	}

	public static void reconnect() {
		try {
			try {
				conn.close();
			} catch (Exception e){
				// Failed to close connection
			}
			String url = "jdbc:mysql://" + host + "/"+ database + "?autoReconnect=true";
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(url, username, password);
			Printer.log("Successfully reconnected to the MySQL server!");

		} catch (Exception e) {
			Printer.log(e);
			Printer.log("Failed reconnect to the MySQL server!");
		}
	}

	public static void disconnect(){
		try {
			conn.close();

		} catch (SQLException e) {
			Printer.log(e);
		}
	}

	/*
	 * Functions
	 */

	public static void banPlayer(int id, String reason, long to) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("REPLACE INTO `mc_bans_v5` (`uid`, `date`, `reason`, `to`, `server`) VALUES (?, ?, ?, ?, ?)");
		ps.setInt(1, id);ps.setString(2, getDateTime());ps.setString(3, reason);ps.setString(4, String.valueOf(to));ps.setString(5, "MCAC");
		executeCommand(ps);
	}

	public static void addMsgChannel(String msg) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("INSERT INTO `mc_msgchannel_v1` (`msg`, `due`) VALUES (?, ?)");
		ps.setString(1, msg);ps.setString(2, String.valueOf(System.currentTimeMillis()+(40000)));
		executeCommand(ps);
	}

	public static void unbanPlayer(int id) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("DELETE FROM `mc_bans_v5` WHERE uid=?");
		ps.setInt(1, id);
		executeCommand(ps);
	}

	public static void updateBanReason(int id, String reason) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("UPDATE `mc_bans_v5` SET reason=? WHERE uid=?");
		ps.setString(1, reason);ps.setInt(2, id);
		executeCommand(ps);
	}

	public static int getPlayerID(String uuid) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("SELECT `id` FROM `mc_players_v4` WHERE uuid=?");
		ps.setString(1, uuid);
		int result = getInt("id", ps);
		return result;
	}

	public static long getUnbanTime(int PlayerID) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("SELECT `to` FROM `mc_bans_v5` WHERE uid=?");
		ps.setInt(1, PlayerID);
		String result = getString("to", ps);
		if (result == null) return 0;
		return Long.parseLong(result);
	}

	public static String getBanReason(int PlayerID) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("SELECT `reason` FROM `mc_bans_v5` WHERE uid=?");
		ps.setInt(1, PlayerID);
		String result = getString("reason", ps);
		return result;
	}

	/*
	 * Utils
	 */

	public static String getDateTime(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

	private static void executeCommand(PreparedStatement ps){
		try {
			ps.executeUpdate();
			ps.close();
		} catch (Exception e){
			Printer.log(e);
		}
	}

	private static int getInt(String output, PreparedStatement ps){
		try {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int result = rs.getInt(output);
				rs.close();
				ps.close();
				return result;
			}
		} catch (Exception e) {
			Printer.log(e);
		}
		return 0;
	}

	private static String getString(String output, PreparedStatement ps){
		try {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String result = rs.getString(output);
				rs.close();
				ps.close();
				return result;
			}
		} catch (Exception e) {
			Printer.log(e);
		}
		return null;
	}
}