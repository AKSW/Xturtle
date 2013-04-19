/*******************************************************************************
 * Copyright (c) 2013 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle.naming;

import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.naming.IQualifiedNameConverter.DefaultImpl;

public class TurtleNameConverter extends DefaultImpl {

	@Override
	public String getDelimiter() {
		return "";
	}

	//our qualified names always have two segments, the namespace and the simple name
	//by convention the simple name is the URI fragment or if that does not exist the last segment 
	//the remaining prefix is the namespace
	@Override
	public QualifiedName toQualifiedName(String qualifiedNameAsString) {
		String name;
		String namespace;
		int fragmentIndex=qualifiedNameAsString.lastIndexOf('#');
		if(fragmentIndex>0){
			name=qualifiedNameAsString.substring(fragmentIndex+1);
			namespace=qualifiedNameAsString.substring(0,fragmentIndex+1);
		}else{
			int lastSlash = qualifiedNameAsString.lastIndexOf('/');
			name=qualifiedNameAsString.substring(lastSlash+1);
			namespace=qualifiedNameAsString.substring(0,lastSlash+1);
		}
		return QualifiedName.create(namespace,name);
//		return super.toQualifiedName(qualifiedNameAsString);
	}
}
