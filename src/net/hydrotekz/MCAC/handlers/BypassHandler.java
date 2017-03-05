
package net.hydrotekz.MCAC.handlers;

import net.hydrotekz.MCAC.api.User;
import net.hydrotekz.MCAC.net.DatabaseHandler;
import net.hydrotekz.MCAC.utils.Printer;

public class BypassHandler {

	/***************************************************************
	 *  Copyright notice
	 *
	 *  (c) 2016 Spillere.no
	 *  All rights reserved
	 *
	 * It's not allowed to copy, reuse and change the code without the permission from the copyright holder
	 *
	 ********************************/

	public static void enableBypass(User user){
		try {
			int PlayerID = DatabaseHandler.getPlayerID(user.getUUID());
			if (PlayerID > 1){
				long unbanTime = DatabaseHandler.getUnbanTime(PlayerID);
				if (unbanTime == -3 && DatabaseHandler.getBanReason(PlayerID).equals("ENABLED")){
					DatabaseHandler.updateBanReason(PlayerID, "DISABLED");
					Printer.log(user.getUsername() + " was allowed access.");
				}
			}
		} catch (Exception ex){
			Printer.log("Failed to enable bypass for " + user.getUsername() + "!");
			DatabaseHandler.reconnect();
		}
	}

	public static void disableBypass(User user){
		try {
			int PlayerID = DatabaseHandler.getPlayerID(user.getUUID());
			if (PlayerID > 1){
				long unbanTime = DatabaseHandler.getUnbanTime(PlayerID);
				if (unbanTime == -3 && DatabaseHandler.getBanReason(PlayerID).equals("DISABLED")){
					DatabaseHandler.updateBanReason(PlayerID, "ENABLED");
					Printer.log(user.getUsername() + " has no longer access.");
				}
			}
		} catch (Exception ex){
			Printer.log("Failed to disable bypass for " + user.getUsername() + "!");
			DatabaseHandler.reconnect();
		}
	}
}