/*******************************************************************************
 * Copyright (c) 2013 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Splitter;
import com.google.common.io.ByteStreams;
import com.google.inject.Singleton;

@Singleton
//dirty proof of concept hack
public class Prefixes {
	private Map<String,List<String>> prefixes=new HashMap<String, List<String>>();
	private Map<String,List<String>> ns=new HashMap<String, List<String>>();
	
	//TODO rather in UI, validation only in UI, List of prefixes configurable via preference page
	//possibly not as csv but property file
	public Prefixes() {
		InputStream stream = getClass().getClassLoader().getResourceAsStream("de/itemis/tooling/xturtle/services/all.file.csv");
		try {
			byte[] input;
			input = ByteStreams.toByteArray(stream);
			String string = new String(input);
			Iterable<String> lines = Splitter.on("\n").split(string);
			
			for (String line : lines) {
				String[] split = line.split(",");
				if(split[1].length()>3){
					String ns = split[1].replaceAll("\"", "").trim();
					List<String>nameSpaces=prefixes.get(split[0]);
					if(nameSpaces==null){
						nameSpaces=new ArrayList<String>();
						prefixes.put(split[0], nameSpaces);
					}
					nameSpaces.add(ns);
					List<String> prefixes=this.ns.get(ns);
					if(prefixes==null){
						prefixes=new ArrayList<String>();
						this.ns.put(ns, prefixes);
					}
					prefixes.add(split[0]);
				}
			}
		} catch (IOException e) {
		}		
	}
	public Collection<String> getPrefixes(){
		return prefixes.keySet();
	}
	public boolean isKnownPrefix(String prefix){
		return prefixes.containsKey(prefix);
	}
	public List<String> getUris(String prefix){
		List<String> candidates = prefixes.get(prefix);
		if(candidates!=null){
			return candidates;
		}
		return null;
	}
	public boolean isKnownNameSpace(String nameSpaceUri){
		return ns.containsKey(nameSpaceUri);
	}
	public List<String> getPrefixes(String nameSpaceUri){
		List<String> candidates = ns.get(nameSpaceUri);
		if(candidates!=null){
			return candidates;
		}
		return null;
	}
}
