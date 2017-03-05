package net.hydrotekz.MCAC.api;

import java.io.DataOutputStream;
import java.net.Socket;
import java.security.PublicKey;
import java.util.LinkedList;

import net.hydrotekz.MCAC.utils.RSA;

public class Client {

	/***************************************************************
	 *  Copyright notice
	 *
	 *  (c) 2016 Spillere.no
	 *  All rights reserved
	 *
	 * It's not allowed to copy, reuse and change the code without the permission from the copyright holder
	 *
	 ********************************/

	private LinkedList<User> users;
	private Socket socket;
	private DataOutputStream out;
	private String rawIp;
	private String id;
	private double version;
	private PublicKey clientKey;

	public Client(Socket socket, DataOutputStream out, String rawIp, String id, double version, PublicKey clientKey){
		this.socket = socket;
		this.out = out;
		this.rawIp = rawIp;
		this.id = id;
		this.version = version;
		this.clientKey = clientKey;
	}

	public LinkedList<User> getUsers(){
		return users;
	}

	public Socket getSocket(){
		return socket;
	}

	public String getRawIp(){
		return rawIp;
	}

	public String getId(){
		return id;
	}

	public double getVersion(){
		return version;
	}

	public void addUser(User user){
		if (users == null) users = new LinkedList<User>();
		users.add(user);
		API.update(this);
	}

	public void removeUser(User user){
		if (users != null) users.remove(user);
		API.update(this);
	}

	public User createUser(String uuid, String username){
		users = API.refresh(this).getUsers();
		if (users == null) users = new LinkedList<User>();
		for (User user : users){
			if (user.getUUID().equals(uuid)){
				return user;
			}
		}
		User user = new User(uuid, username);
		addUser(user);
		return user;
	}

	public Client refresh(){
		return API.refresh(this);
	}

	public boolean isValid(){
		return users != null && !users.isEmpty();
	}

	public void sendMessage(String msg){
		try {
			String encrypted = RSA.encrypt(msg, clientKey);
			out.writeUTF(encrypted);
		} catch (Exception e){}
	}
}