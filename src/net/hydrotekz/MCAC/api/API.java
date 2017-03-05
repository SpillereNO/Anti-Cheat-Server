package net.hydrotekz.MCAC.api;

import java.util.HashMap;

public class API {

	/***************************************************************
	 *  Copyright notice
	 *
	 *  (c) 2016 Spillere.no
	 *  All rights reserved
	 *
	 * It's not allowed to copy, reuse and change the code without the permission from the copyright holder
	 *
	 ********************************/

	private static HashMap<String, Client> clients = new HashMap<String, Client>();

	public static Client refresh(Client client){
		if (clients.containsKey(client.getId())){
			return clients.get(client.getId());
		}
		return client;
	}

	public static void update(Client client){
		clients.put(client.getId(), client);
	}
	
	public static void remove(Client client){
		clients.remove(client.getId());
	}
	
	public static boolean contians(Client client){
		return clients.containsKey(client.getId());
	}
}