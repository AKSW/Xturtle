/*******************************************************************************
 * Copyright (c) 2015 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.naming.QualifiedName;

import com.google.common.base.Strings;

public class TurtleUriResolver {

	private PrefixURI currentBaseUri;
	private Map<String, PrefixURI> prefixToUriMap=new HashMap<String, PrefixURI>();

	public TurtleUriResolver(URI uri) {
		currentBaseUri=new PrefixURI(uri);
	}

	public QualifiedName updateAndGetBase(String newBase){
		currentBaseUri=new PrefixURI(currentBaseUri.resolve(newBase));
		return QualifiedName.create(currentBaseUri.getURIString());
	}

	public QualifiedName getPrefixName(String prefix, String prefixUri){
		URI resolved=currentBaseUri.resolve(prefixUri);
		prefixToUriMap.put(prefix, new PrefixURI(resolved));
		return QualifiedName.create(prefixToUriMap.get(prefix).getURIString());
	}

	public QualifiedName resolveWithUri(String uriAsString){
		return getName(currentBaseUri.resolve(uriAsString));
	}

	public QualifiedName resolveWithLocalName(String prefix, String localName){
		PrefixURI prefixUri = prefixToUriMap.get(prefix);
		if(prefixUri!=null){
			URI newUri=prefixUri.resolveLocal(localName);
			return getName(newUri);
		}else{
			return null;
		}
	}

	public static final QualifiedName getName(URI uri){
		String namespace;
		String name;

		String fragment=uri.fragment();
		String uriAsString=uri.toString();
		if(fragment!=null){
			name=fragment;
			int index=uriAsString.indexOf("#"+fragment);
			if(index<0){
				throw new IllegalStateException("unable to find fragment in "+uri);
			}else{
				namespace=uriAsString.substring(0, index+1);
			}
		}else{
			List<String> segments = uri.segmentsList();
			if(segments.size()==0){
				name="";
				namespace=uri.toString();
			}else{
				name=segments.get(segments.size()-1);
				namespace=uri.trimSegments(1).toString()+"/";
			}
		}
		return QualifiedName.create(namespace,name);
	}

	public static final class PrefixURI{

		private URI prefixUri;
		private String uriAsString;

		public PrefixURI(URI prefixUri) {
			this.prefixUri=prefixUri;
			uriAsString=prefixUri.toString();
		}

		public URI resolveLocal(String localName){
			return URI.createURI(uriAsString+localName);
		}

		public URI resolve(String uriString){
			if(Strings.isNullOrEmpty(uriString)){
				return prefixUri;
			}
			try {
				return URI.createURI(uriString).resolve(prefixUri);
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("could not resolve '"+uriString+"' with respect to base uri "+prefixUri ,e);
			}
		}

		String getURIString(){
			return uriAsString;
		}
	}
}