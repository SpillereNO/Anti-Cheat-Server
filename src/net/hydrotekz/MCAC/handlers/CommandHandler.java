package net.hydrotekz.MCAC.handlers;

import net.hydrotekz.MCAC.api.Client;
import net.hydrotekz.MCAC.scanners.CheatScanner;
import net.hydrotekz.MCAC.scanners.KnownVersions;

public class CommandHandler {

	/***************************************************************
	 *  Copyright notice
	 *
	 *  (c) 2016 Spillere.no
	 *  All rights reserved
	 *
	 * It's not allowed to copy, reuse and change the code without the permission from the copyright holder
	 *
	 ********************************/

	public static void scan(Client client, String[] split){
		String type = split[1];
		String path = split[2].replaceAll("%0%", " ").replace("\\", "/");
		String fileName = path.split("/")[path.split("/").length-1];
		if (type.equals("resourcepack")){
			CheatScanner.scanResourcepack(client, fileName);

		} else if (type.equals("process")) {
			// TODO
			
		} else if (type.equals("version") || type.equals("other")){
			String hash = split[3];
			CheatScanner.scanFile(client, path, hash, type.equals("version"), Boolean.parseBoolean(split[4]));
		}
	}

	public static void addKnownVersion(String[] split){
		KnownVersions.hashedFiles.put(split[2].replaceAll("%0%", " "), split[1]);
	}
}