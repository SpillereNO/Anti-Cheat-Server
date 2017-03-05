package net.hydrotekz.MCAC.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.PublicKey;

import net.hydrotekz.MCAC.Server;
import net.hydrotekz.MCAC.api.API;
import net.hydrotekz.MCAC.api.Client;
import net.hydrotekz.MCAC.handlers.AuthHandler;
import net.hydrotekz.MCAC.handlers.CommandHandler;
import net.hydrotekz.MCAC.utils.Printer;
import net.hydrotekz.MCAC.utils.RSA;

public class SocketConnection implements Runnable {

	/***************************************************************
	 *  Copyright notice
	 *
	 *  (c) 2016 Spillere.no
	 *  All rights reserved
	 *
	 * It's not allowed to copy, reuse and change the code without the permission from the copyright holder
	 *
	 ********************************/

	private Socket socket;

	SocketConnection(Socket socket) {
		this.socket = socket;
	}

	private void exec(Client client, String text){
		if (text.startsWith("scan ")){
			CommandHandler.scan(client, text.split(" "));
		} else if (text.startsWith("auth ")){
			AuthHandler.authorize(client, text);
		} else if (text.startsWith("addknownversion ")){
			CommandHandler.addKnownVersion(text.split(" "));
		}
	}

	public void run () {
		// Listen for messages and handle them
		String rawIp = socket.getRemoteSocketAddress().toString();
		Client client = null;
		try {
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			out.writeUTF("handshake " + RSA.publicKey);
			PublicKey clientKey = null;

			String text;
			while((text = in.readUTF()) != null) {
				try {
					// Handle handshake
					if (clientKey == null){
						if (text.startsWith("handshake ")){
							clientKey = RSA.loadPublicKey(text.split(" ")[1]);
							Printer.log(rawIp + " successfully handshaked!");
						}
					} else {
						// Decrypt information
						text = RSA.decrypt(text, RSA.keyPair.getPrivate());
						if (text == null) continue;
						// Handle login
						if (text.startsWith("login ") && client == null){
							client = new Client(socket, out, rawIp, text.split(" ")[1], Double.parseDouble(text.split(" ")[2]), clientKey);
							if (Server.updateClients && client.getVersion() < Server.lowestVersion){
								client.sendMessage("update");
								Printer.log("Outdated client found, update message sent!");
							} else if (API.contians(client)){
								client.sendMessage("shutdown");
								API.refresh(client).sendMessage("showdashboard");
								Printer.log("Client already connected!");
							}
						}
						// Handle command
						if (client != null){
							client = API.refresh(client);
							if (client.isValid()){
								exec(client, text);
							} else if (text.startsWith("auth ")){
								exec(client, text);
							}
						}
					}
				} catch (Exception e){
					Printer.log(e);
					Printer.log("Failed to handle input!");
				}
			}

			AuthHandler.deauthorize(client);
			Printer.log(rawIp + " disconnected!");

		} catch (IOException e) {
			AuthHandler.deauthorize(client);
			Printer.log(rawIp + " disconnected!");

		} catch (Exception e) {
			Printer.log(e);

		} finally {
			try {
				if (!socket.isClosed()) socket.close();

			} catch (Exception e) {
				Printer.log("Failed to close socket connection!");
			}
		}
	}
}