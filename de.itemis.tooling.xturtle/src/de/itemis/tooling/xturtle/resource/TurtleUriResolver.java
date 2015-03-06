/*******************************************************************************
 * Copyright (c) 2015 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle.resource;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.xtext.naming.QualifiedName;

import com.google.common.base.Strings;

public class TurtleUriResolver {

	private PrefixURI currentBaseUri;
	private Map<String, PrefixURI> prefixToUriMap=new HashMap<String, PrefixURI>();

	public TurtleUriResolver(String fileName) {
		currentBaseUri=new PrefixURI(URI.create("file://"+fileName));
	}

	private static URI getNormalizedUri(String uriString){
		if(uriString.contains("\\u") || uriString.contains("\\U")){
			//way to go replace escaped by (UTF16.valueOf(Integer.valueOf("10FFFF",16))
			throw new UnsupportedOperationException("utf escaping is not yet supported");
		}
		try {
			return URI.create(uriString).normalize();
		} catch (Exception e) {
			return null;
		}
	}

	public QualifiedName updateAndGetBase(String newBase){
		currentBaseUri=new PrefixURI(currentBaseUri.resolve(newBase).normalize());
		return QualifiedName.create(currentBaseUri.getURIString());
	}

	public QualifiedName getPrefixName(String prefix, String prefixUri){
		URI resolved=currentBaseUri.resolve(prefixUri);
		prefixToUriMap.put(prefix, new PrefixURI(resolved));
		return QualifiedName.create(prefixToUriMap.get(prefix).getURIString());
	}

	public QualifiedName resolveWithUri(String uriAsString){
		return getName(currentBaseUri.resolve(uriAsString).normalize());
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

		String fragment=uri.getRawFragment();
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
			int lastSlash = uriAsString.lastIndexOf('/');
			name=uriAsString.substring(lastSlash+1);
			namespace=uriAsString.substring(0,lastSlash+1);
		}
		return QualifiedName.create(namespace,name);
	}

	public static final class PrefixURI{

		private URI prefixUri;
		boolean hasFragment;
		String uriWithoutFragment;
		String uriWithoutLastSegment;
		public PrefixURI(URI prefixUri) {
			this.prefixUri=prefixUri;
			String fragment=prefixUri.getRawFragment();
			String uri=getURIString();
			if(fragment!=null){
				hasFragment=true;
				int index=uri.indexOf("#"+fragment);
				if(index<0){
					throw new IllegalStateException("unable to find fragment in "+uri);
				}else{
					uriWithoutFragment=uri.substring(0, index+1);
				}
			}else{
				uriWithoutLastSegment=uri.substring(0, uri.lastIndexOf('/')+1);
			}
		}

		URI resolveLocal(String localName){
			String lnameToUse=localName;
			if(lnameToUse.indexOf('\\')>=0){
				lnameToUse=lnameToUse.replace('\\', '/');
			}
			if(hasFragment){
				return URI.create(uriWithoutFragment+lnameToUse);
			}else{
				return URI.create(uriWithoutLastSegment+lnameToUse);
			}
		}

		public URI resolve(String uriString){
			URI uri;
			if(Strings.isNullOrEmpty(uriString)){
				return prefixUri;
			} else if((uri=getNormalizedUri(uriString))==null){
				//TODO what to do here
				throw new IllegalArgumentException("could not resolve with URI string "+uriString);
			} else if(uri.isAbsolute()){
				return uri;
			}else if(hasFragment){
				String uriAsString=uri.toString();
				String newFragment=null;
				if(uri.toString().charAt(0)=='#'){
					newFragment=uriAsString.substring(1);
				}else if(uriAsString.indexOf('/')<0 && uri.getFragment()==null){
					newFragment=uriAsString;
				}
				if(newFragment!=null){
					return URI.create(uriWithoutFragment+newFragment);
				}
			}
			return prefixUri.resolve(uri).normalize();
		}

		String getURIString(){
			return prefixUri.toString();
		}
	}
}
