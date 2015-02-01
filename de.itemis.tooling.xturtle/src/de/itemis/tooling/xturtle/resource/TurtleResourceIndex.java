/*******************************************************************************
 * Copyright (c) 2013 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle.resource;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;

import com.google.common.base.Optional;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import de.itemis.tooling.xturtle.xturtle.Base;
import de.itemis.tooling.xturtle.xturtle.PrefixId;
import de.itemis.tooling.xturtle.xturtle.QNameDef;
import de.itemis.tooling.xturtle.xturtle.QNameRef;
import de.itemis.tooling.xturtle.xturtle.UriDef;
import de.itemis.tooling.xturtle.xturtle.UriRef;
import de.itemis.tooling.xturtle.xturtle.XturtlePackage;

//THE central class for most of the turtle functionality, as it attaches a resolved URI to
//each Resource(Reference). Otherwise handling the model would become even more messy.
//this is done via an Adapter stored in the model root which holds maps with all the
//necessary information
//TODO clean up this code, extract services
class TurtleResourceIndex implements Adapter {

	public void notifyChanged(Notification notification) {
	}

	public Notifier getTarget() {
		return null;
	}

	public void setTarget(Notifier newTarget) {
	}

	public boolean isAdapterForType(Object type) {
		return false;
	}

	private BiMap<EObject,String> fragmentMap;
	private URI currentBaseUri;
	private Map<String, String> prefixToUriMap;
	private Map<EObject,QualifiedName> qNameMap;

	String getFragment(EObject o){
		return fragmentMap.get(o);
	}

	EObject getObject(String fragment){
		return fragmentMap.inverse().get(fragment);
	}
	
	QualifiedName getName(EObject object){
		return qNameMap.get(object);
	}

	void initIndex(EObject root){
		resetMaps(root.eResource());
		int i=0;
		addFragmentEntry(i++, root);

		//make sure blank node labels get proper qualified name
		prefixToUriMap.put("_",root.eResource().getURI().toString()+"#");
		qNameMap.put(root, QualifiedName.create(prefixToUriMap.get("_")));

		TreeIterator<EObject> iterator = root.eAllContents();
		while(iterator.hasNext()) {
			EObject obj = iterator.next();
			addFragmentEntry(i++, obj);
			addUriAndNameEntries(obj);
		}
	}

	private URI getNormalizedUri(String uriString){
		try {
			return URI.create(uriString).normalize();
		} catch (Exception e) {
			return null;
		}
	}
	
	private void addUriAndNameEntries(EObject obj) {
		if(obj instanceof PrefixId){
			addPrefixIdEntries((PrefixId) obj);
		} else if(obj instanceof Base){
			addBaseEntries((Base) obj);
		} else if(obj instanceof QNameDef){
			String prefix=getPrefixText(obj,XturtlePackage.Literals.QNAME_DEF__PREFIX);
			String prefixUri = prefixToUriMap.get(prefix);
			if(prefixUri!=null){
				String id=((QNameDef) obj).getId();
				qNameMap.put(obj, QualifiedName.create(prefixUri, id));
			}
		} else if(obj instanceof UriDef){
			QualifiedName qName=resolve(currentBaseUri, ((UriDef) obj).getUri());
			if(qName!=null){
				qNameMap.put(obj, qName);
			}
		} else if(obj instanceof QNameRef){
			String prefix=getPrefixText(obj, XturtlePackage.Literals.QNAME_REF__PREFIX);
			List<INode> refNodes = NodeModelUtils.findNodesForFeature(obj, XturtlePackage.Literals.RESOURCE_REF__REF);
			String ref=NodeModelUtils.getTokenText(refNodes.get(0));
			if(ref!=null&& ref.length()>0&& ref.charAt(0)==':'){
				String nsUri=prefixToUriMap.get(prefix);
				if(nsUri!=null){
					qNameMap.put(obj, QualifiedName.create(nsUri, ref.substring(1)));
				}
			}

		} else if(obj instanceof UriRef){
			QualifiedName qName;
			List<INode> refNodes = NodeModelUtils.findNodesForFeature(obj, XturtlePackage.Literals.RESOURCE_REF__REF);
			if(refNodes.size()==1){
				String ref = refNodes.get(0).getText();
				ref=ref.substring(1, ref.length()-1);
				qName=resolve(currentBaseUri, ref);
				qNameMap.put(obj, qName);
			}
//			else{
//				qNameMap.put(obj, QualifiedName.create("invalid","invalid"));
//			}
		}	
	}

	private String getPrefixText(EObject obj, EReference ref) {
		String prefix=null;
		List<INode> nodes = NodeModelUtils.findNodesForFeature(obj, ref);
		switch(nodes.size()){
			case 0: 
				prefix="";
				break;
			case 1:
				prefix=NodeModelUtils.getTokenText(nodes.get(0));
				break;
		}
		return prefix;
	}

	private void addBaseEntries(Base obj) {
		URI uri=getNormalizedUri(obj.getUri());
		if(uri!=null){
			if(uri.isAbsolute()){
				currentBaseUri=uri;
			}else{
				currentBaseUri=currentBaseUri.resolve(uri).normalize();
			}
			qNameMap.put(obj, QualifiedName.create(currentBaseUri.toString()));
		}
	}

	private void addPrefixIdEntries(PrefixId obj) {
		String prefix = Optional.fromNullable(obj.getId()).or("");
		String uriString=obj.getUri();
		URI uri=getNormalizedUri(uriString);
		if(uri!=null){
			if (uri.isAbsolute()){
				prefixToUriMap.put(prefix, uriString);
			}else{
				prefixToUriMap.put(prefix,currentBaseUri.resolve(uri).normalize().toString());
			}
			qNameMap.put(obj, QualifiedName.create(prefixToUriMap.get(prefix)));
		}
	}
	//TODO in externe Komponente auslagern, testen
	private QualifiedName resolve(URI base, String uriAsString) {
		String namespace;
		String name;
		//from baseUri alone
		if(uriAsString==null || uriAsString.length()==0){
			String fragment=base.getFragment();
			String baseAsString=base.toString();
			if(fragment!=null){
				name=fragment;
				namespace=baseAsString.substring(0, baseAsString.lastIndexOf('#')+1);
			}else{
				int lastSlash = baseAsString.lastIndexOf('/');
				name=baseAsString.substring(lastSlash+1);
				namespace=baseAsString.substring(0,lastSlash+1);
			}
		}else{
			URI uri = getNormalizedUri(uriAsString);
			if(uri==null){
				return null;
			}else if(uri.isAbsolute()){
				return resolve(uri,null);
			}else if(base.getFragment()!=null){
				if(uriAsString.indexOf('/')>=0){
					namespace="invalid";
					name="invalid";
				}else{
					//we assume that the fragment is empty as nothing else makes sense for the base uri
					namespace=base.toString();
					name=uriAsString;
				}
			}else{
				return resolve(base.resolve(uriAsString).normalize(),null);
			}
		}
		return QualifiedName.create(namespace,name);
	}

	private void resetMaps(Resource resource){
		fragmentMap=HashBiMap.create();
		prefixToUriMap=new HashMap<String, String>();
		qNameMap=new HashMap<EObject, QualifiedName>();
		StringBuilder b=new StringBuilder("file:///");
		b.append(resource.getURI().lastSegment());
		currentBaseUri=URI.create(b.toString());
	}

	private void addFragmentEntry(int index, EObject object){
		fragmentMap.put(object, ""+index);
	}


}
