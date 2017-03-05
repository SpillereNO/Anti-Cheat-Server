package net.hydrotekz.MCAC.scanners;

import java.io.File;
import java.util.HashMap;

import com.google.common.io.Files;

import net.hydrotekz.MCAC.utils.Printer;
import net.hydrotekz.MCAC.utils.Utils;

public class IdentifiedHacks {

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
		Printer.log("Loading identified hacks...");
		File resources = new File("Resources");
		File identified = new File(resources.getAbsolutePath() + File.separator + "Identified Hacks");
		File pending = new File(resources.getAbsolutePath() + File.separator + "Pending Hacks");
		// Moves pending hacks to identified hacks
		try {
			if (pending.exists()){
				for (File file : pending.listFiles()){
					String hash = Utils.getCRC32(file);
					int endIndex = file.getName().lastIndexOf(".");
					if (endIndex != -1){
						String fName = file.getName().substring(0, endIndex);
						String ext = file.getName().substring(endIndex, file.getName().length());
						String fileName = fName + "_" + hash + ext;
						File to = new File(identified + File.separator + fileName);
						if (!to.exists()){
							Files.move(file, to);
						} else {
							file.delete();
						}
					}
				}
			}
		} catch (Exception e) {
			Printer.log(e);
		}
		// Loads hacks into memory
		for (File file : identified.listFiles()){
			if (file.isFile() && file.getName().endsWith(".jar")){
				try {
					if (!hashedFiles.containsKey(file.getName())){
						String hash = Utils.getSHA1(file);
						hashedFiles.put(file.getName(), hash);
						Printer.log(file.getName() + " = " + hash);
					}
				} catch (Exception e){
					Printer.log("Failed to hash file: " + file.getName());
					Printer.log(e);
				}
			}
		}
		Printer.log("Identified hacks loaded: " + hashedFiles.size());
	}
}