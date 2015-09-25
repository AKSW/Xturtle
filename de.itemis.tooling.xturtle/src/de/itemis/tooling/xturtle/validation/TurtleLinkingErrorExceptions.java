/*******************************************************************************
 * Copyright (c) 2015 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle.validation;

import java.util.regex.Pattern;

import javax.inject.Inject;

public class TurtleLinkingErrorExceptions {

	@Inject
	private TurtleNoLinkingValidationUriPrefixes ignoreLinkingErrorPrefixes;

	private Pattern pattern=Pattern.compile("http://www.w3.org/1999/02/22-rdf-syntax-ns#(li|_\\d*)");

	public boolean matchesRdfListProperty(String uri){
		if(uri==null||uri.length()==0){
			return false;
		}else{
			return pattern.matcher(uri).matches();
		}
	}

	public boolean ignoreLinkingError(String uri){
		if(uri!=null&&uri.length()>0){
			if(matchesRdfListProperty(uri)){
				return true;
			}else{
				for (String prefix : ignoreLinkingErrorPrefixes.getPrefixes()) {
					if(uri.startsWith(prefix)){
						return true;
					}
				}
			}
		}
		return false;
	}
}
