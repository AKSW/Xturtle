/*******************************************************************************
 * Copyright (c) 2013 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle.resource;

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
class TurtleResourceIndex implements Adapter {

	public void notifyChanged(Notification notification) {}

	public Notifier getTarget() {
		return null;
	}

	public void setTarget(Notifier newTarget) {}

	public boolean isAdapterForType(Object type) {
		return false;
	}

	private TurtleUriResolver resolver;
	private BiMap<EObject,String> fragmentMap;
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

		//make sure blank node labels get proper qualified name; base is file URI
		//TODO check if this works
		QualifiedName blankName=resolver.getPrefixName("_", "#");
		qNameMap.put(root, blankName);

		TreeIterator<EObject> iterator = root.eAllContents();
		while(iterator.hasNext()) {
			EObject obj = iterator.next();
			addFragmentEntry(i++, obj);
			addUriAndNameEntries(obj);
		}
	}

	private void addUriAndNameEntries(EObject obj) {
		if(obj instanceof PrefixId){
			addPrefixIdEntries((PrefixId) obj);
		} else if(obj instanceof Base){
			addBaseEntries((Base) obj);
		} else if(obj instanceof QNameDef){
			String prefix=getPrefixText(obj,XturtlePackage.Literals.QNAME_DEF__PREFIX);
			QualifiedName name=resolver.resolveWithLocalName(prefix, ((QNameDef) obj).getId());
			if(name!=null){
				qNameMap.put(obj, name);
			}
		} else if(obj instanceof UriDef){
			QualifiedName qName=resolver.resolveWithUri(((UriDef) obj).getUri());
			if(qName!=null){
				qNameMap.put(obj, qName);
			}
		} else if(obj instanceof QNameRef){
			String prefix=getPrefixText(obj, XturtlePackage.Literals.QNAME_REF__PREFIX);
			List<INode> refNodes = NodeModelUtils.findNodesForFeature(obj, XturtlePackage.Literals.RESOURCE_REF__REF);
			String ref=NodeModelUtils.getTokenText(refNodes.get(0));
			if(ref!=null&& ref.length()>0&& ref.charAt(0)==':'){
				QualifiedName name=resolver.resolveWithLocalName(prefix, ref.substring(1));
				if(name!=null){
					qNameMap.put(obj, name);
				}
			}

		} else if(obj instanceof UriRef){
			QualifiedName qName;
			List<INode> refNodes = NodeModelUtils.findNodesForFeature(obj, XturtlePackage.Literals.RESOURCE_REF__REF);
			if(refNodes.size()==1){
				String ref = refNodes.get(0).getText();
				ref=ref.substring(1, ref.length()-1);
				qName=resolver.resolveWithUri(ref);
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
		QualifiedName newBase=resolver.updateAndGetBase(obj.getUri());
		if(newBase!=null){
			qNameMap.put(obj, newBase);
		}
	}

	private void addPrefixIdEntries(PrefixId obj) {
		String prefix = Optional.fromNullable(obj.getId()).or("");
		String uriString=obj.getUri();
		
		QualifiedName name=resolver.getPrefixName(prefix, uriString);
		if(name!=null){
			qNameMap.put(obj, name);
		}
	}

	private void resetMaps(Resource resource){
		fragmentMap=HashBiMap.create();
		qNameMap=new HashMap<EObject, QualifiedName>();
		resolver=new TurtleUriResolver(resource.getURI());
	}

	private void addFragmentEntry(int index, EObject object){
		fragmentMap.put(object, ""+index);
	}
}