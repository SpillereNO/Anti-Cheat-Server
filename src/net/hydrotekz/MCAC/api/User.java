package net.hydrotekz.MCAC.api;

public class User {

	/***************************************************************
	 *  Copyright notice
	 *
	 *  (c) 2016 Spillere.no
	 *  All rights reserved
	 *
	 * It's not allowed to copy, reuse and change the code without the permission from the copyright holder
	 *
	 ********************************/

	private String uuid;
	private String username;
	private long lastAuth;

	public User(String uuid, String username){
		this.uuid = uuid;
		this.username = username;
	}

	public String getUUID(){
		return uuid;
	}

	public String getUsername(){
		return username;
	}

	public void setLastAuth(long time){
		lastAuth = time;
	}

	public long getLastAuth(){
		return lastAuth;
	}
}