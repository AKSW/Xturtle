/*******************************************************************************
 * Copyright (c) 2013 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle.resource;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.naming.QualifiedName;

import com.google.inject.Singleton;

@Singleton
class DefaultTurtleResourceService implements TurtleResourceService{
	
	private TurtleResourceIndex getIndex(EObject o){
		if(o!=null){
			EList<Adapter> adapters = EcoreUtil2.getRootContainer(o).eAdapters();
			for (Adapter adapter : adapters) {
				if(adapter instanceof TurtleResourceIndex){
					return (TurtleResourceIndex)adapter;
				}
			}
		}
		return null;
	}

	public String getFragment(EObject object){
		TurtleResourceIndex index=getIndex(object);
		if(index!=null){
			return index.getFragment(object);
		}
		return null;
	}
	
	public EObject getObject(Resource resource, String fragment){
		TurtleResourceIndex index=getIndex(resource.getContents().get(0));
		if(index!=null){
			return index.getObject(fragment);
		}
		return null;
	}

	public QualifiedName getQualifiedName(EObject res){
		TurtleResourceIndex index=getIndex(res);
		if(index!=null){
			return index.getName(res);
		}
		return null;
	}

	public String getUriString(EObject object){
		QualifiedName qName = getQualifiedName(object);
		if(qName!=null){
			return qName.toString("");
		}
		return null;
	}
	
	public void initialiseIndex(EObject root){
		root=EcoreUtil2.getRootContainer(root);
		TurtleResourceIndex cacheAdapter=getIndex(root);
		if(cacheAdapter==null){
			cacheAdapter=new TurtleResourceIndex();
			root.eAdapters().add(cacheAdapter);
		}
		cacheAdapter.initIndex(root);
	}
}
