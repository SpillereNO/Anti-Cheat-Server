package net.hydrotekz.MCAC.net;

import java.io.*;
import java.net.*;
import java.util.HashMap;

import net.hydrotekz.MCAC.utils.Printer;

public class SocketService {

	/***************************************************************
	 *  Copyright notice
	 *
	 *  (c) 2016 Spillere.no
	 *  All rights reserved
	 *
	 * It's not allowed to copy, reuse and change the code without the permission from the copyright holder
	 *
	 ********************************/

	private static int port = 443;

	private static ServerSocket listener;
	public static HashMap<String, String> users = new HashMap<String, String>();

	// Listen for incoming connections and handle them
	public static void startService() {
		try {
			listener = new ServerSocket(port);

			Printer.log("Listening for connections...");
			while(!listener.isClosed()){
				Socket socket = listener.accept();
				SocketConnection connection = new SocketConnection(socket);
				Thread t = new Thread(connection);
				t.start();
				Printer.log(socket.getRemoteSocketAddress().toString() + " connected!");
			}

		} catch (IOException ioe) {
			Printer.log("Failed to start socket service!");
		}
	}

	public static void stopService(){
		try {
			if (!listener.isClosed() && listener != null){
				listener.close();
				Printer.log("Socket server was successfully closed!");
			}

		} catch (Exception e){
			Printer.log("Failed to stop socket service!");
		}	
	}
}