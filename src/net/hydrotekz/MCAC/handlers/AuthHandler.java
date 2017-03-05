package net.hydrotekz.MCAC.handlers;

import net.hydrotekz.MCAC.api.API;
import net.hydrotekz.MCAC.api.Client;
import net.hydrotekz.MCAC.api.User;
import net.hydrotekz.MCAC.utils.Printer;

public class AuthHandler {

	/***************************************************************
	 *  Copyright notice
	 *
	 *  (c) 2016 Spillere.no
	 *  All rights reserved
	 *
	 * It's not allowed to copy, reuse and change the code without the permission from the copyright holder
	 *
	 ********************************/

	public static void authorize(Client client, String command){
		try {
			String[] split = command.split(" ");
			String uuid = split[1];
			String username = split[2];
			User user = client.createUser(uuid, username);
			BypassHandler.enableBypass(user);
			if (user.getLastAuth() == 0){
				Printer.log(username + " was authenticated with version " + client.getVersion() + ".");
			} else {
				long lastAuth = (System.currentTimeMillis()-user.getLastAuth())/1000;
				Printer.log(username + " was authenticated with version " + client.getVersion() + ", last auth: " + lastAuth + "s ago.");
			}
			user.setLastAuth(System.currentTimeMillis());

		} catch (Exception e){
			Printer.log("Failed to authenticate: " + command);
			Printer.log(e);
		}
	}

	public static void deauthorize(Client client){
		if (client == null) return;
		for (User user : client.getUsers()){
			Printer.log(user.getUsername() + " deauthenticated!");
			BypassHandler.disableBypass(user);
			client.removeUser(user);
		}
		API.remove(client);
	}
}