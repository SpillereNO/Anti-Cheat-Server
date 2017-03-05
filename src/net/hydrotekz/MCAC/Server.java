package net.hydrotekz.MCAC;

import net.hydrotekz.MCAC.net.DatabaseHandler;
import net.hydrotekz.MCAC.net.SocketService;
import net.hydrotekz.MCAC.scanners.IdentifiedHacks;
import net.hydrotekz.MCAC.scanners.KnownVersions;
import net.hydrotekz.MCAC.utils.Printer;
import net.hydrotekz.MCAC.utils.RSA;

public class Server {

	/***************************************************************
	 *  Copyright notice
	 *
	 *  (c) 2016 Spillere.no  
	 *  All rights reserved
	 *
	 * It's not allowed to copy, reuse and change the code without the permission from the copyright holder
	 *
	 ********************************/
	
	public static double lowestVersion = 5.5;
	public static boolean updateClients = true;

	public static void main(String[] args){
		try {
			// Welcome
			Printer.log("");
			Printer.log("###################");
			Printer.log("#  MCAC - Server  #");
			Printer.log("###################");
			Printer.log("");
			// Load RSA keys
			RSA.keyPair = RSA.generateKeys();
			RSA.publicKey = RSA.savePublicKey(RSA.keyPair.getPublic());
			Printer.log("Public key: " + RSA.publicKey);
			Printer.log("");
			// Shutdown task
			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					SocketService.stopService();
					DatabaseHandler.disconnect();
					Printer.log("Successfully shut down.");
				}
			});
			// Load known cheats
			IdentifiedHacks.loadLocalFiles();
			// Load known versions
			KnownVersions.loadLocalFiles();
			// Connect to MySQL
			DatabaseHandler.connect();
			// Socket service
			SocketService.startService();

		} catch (Exception e) {
			Printer.log("An unknown error was detected on startup!");
			Printer.log(e);
			System.exit(0);
		}
	}
}