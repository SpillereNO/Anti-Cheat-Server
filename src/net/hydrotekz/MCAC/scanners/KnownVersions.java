package net.hydrotekz.MCAC.scanners;

import java.io.File;
import java.util.HashMap;

import net.hydrotekz.MCAC.utils.Printer;
import net.hydrotekz.MCAC.utils.Utils;

public class KnownVersions {

	/***************************************************************
	 *  Copyright notice
	 *
	 *  (c) 2016 Spillere.no
	 *  All rights reserved
	 *
	 * It's not allowed to copy, reuse and change the code without the permission from the copyright holder
	 *
	 ********************************/

	// Format: FileName, Hash
	public static HashMap<String, String> hashedFiles = new HashMap<String, String>();

	// Load and hash files
	public static void loadLocalFiles(){
		Printer.log("Loading known versions...");
		File resources = new File("Resources");
		File versions = new File(resources.getAbsolutePath() + File.separator + "Known Versions");
		// Loads hacks into memory
		for (File file : versions.listFiles()){
			if (file.isFile() && file.getName().endsWith(".jar")){
				try {
					if (!hashedFiles.containsKey(file.getName())){
						String hash = Utils.getSHA1(file);
						hashedFiles.put(file.getName(), hash);
					}
				} catch (Exception e){
					Printer.log("Failed to hash file: " + file.getName());
					Printer.log(e);
				}
			}
		}
		Printer.log("Known versions loaded: " + hashedFiles.size());
	}
	
	public static boolean know(String hash){
		for (String sha1 : hashedFiles.values()){
			if (hash.equals(sha1)){
				return true;
			}
		}
		return false;
	}
}