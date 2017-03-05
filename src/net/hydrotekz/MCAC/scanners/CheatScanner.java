package net.hydrotekz.MCAC.scanners;

import java.sql.SQLException;

import net.hydrotekz.MCAC.api.Client;
import net.hydrotekz.MCAC.api.User;
import net.hydrotekz.MCAC.net.DatabaseHandler;
import net.hydrotekz.MCAC.utils.Printer;

public class CheatScanner {

	/***************************************************************
	 *  Copyright notice
	 *
	 *  (c) 2016 Spillere.no
	 *  All rights reserved
	 *
	 * It's not allowed to copy, reuse and change the code without the permission from the copyright holder
	 *
	 ********************************/

	public static void scanFile(Client client, String path, String hash, boolean version, boolean fileInUse){
		try {
			if (version && !KnownVersions.know(hash)){
				client.sendMessage("download version " + path.replaceAll(" ", "%0%").replace("\\", "/"));
			}
			if (fileInUse){
				String fileName = path.split("/")[path.split("/").length-1];
				for (User user : client.getUsers()){
					String uuid = user.getUUID();
					String username = user.getUsername();

					// Hash ban
					if (IdentifiedHacks.hashedFiles.containsKey(fileName) || IdentifiedHacks.hashedFiles.containsValue(hash)){
						Printer.log(username + " was detected for cheating with " + path + ".");
						punishCheater(uuid, " (hacking)", 7);
					}

					// Filename ban
					for (String file : IdentifiedHacks.hashedFiles.keySet()){
						if (file.contains("_")){
							if (file.split("_")[0].equalsIgnoreCase(fileName)){
								Printer.log(username + " was detected for cheating with " + path + ".");
								punishCheater(uuid, " (hacking)", 5);
							}
						}
					}
				}
			}

		} catch (Exception e){
			Printer.log(e);
		}
	}
	
	public static void scanProcess(Client client, String fileName, String hash){
		try {
			// TODO
			
		} catch (Exception e){
			Printer.log(e);
		}
	}

	public static void scanResourcepack(Client client, String fileName){
		try {
			fileName = fileName.replaceAll("%0%", " ").toLowerCase();
			if (fileName.contains("xray") || fileName.contains("x-ray")){
				for (User user : client.getUsers()){
					String uuid = user.getUUID();
					String username = user.getUsername();
					Printer.log(username + " was detected for using of unfair resourcepack.");
					CheatScanner.punishCheater(uuid, " (xray ressurspakke)", 3);
				}
			}

		} catch (Exception e){
			Printer.log(e);
			Printer.log("Failed to scan resourcepack!");
		}
	}

	public static void punishCheater(String uuid, String detail, int days) throws SQLException {
		int PlayerID = DatabaseHandler.getPlayerID(uuid);
		if (PlayerID > 1){
			long currentUnbanTime = DatabaseHandler.getUnbanTime(PlayerID);
			if (currentUnbanTime == 0 || currentUnbanTime == -3){
				if (currentUnbanTime == -3){
					DatabaseHandler.unbanPlayer(PlayerID);
				}
				long unbanTime = System.currentTimeMillis()+(1000*60*60*24*days);
				DatabaseHandler.banPlayer(PlayerID, "Juksing i spillet" + detail, unbanTime);
				DatabaseHandler.addMsgChannel("hackkick " + uuid);
			}
		}
	}
}