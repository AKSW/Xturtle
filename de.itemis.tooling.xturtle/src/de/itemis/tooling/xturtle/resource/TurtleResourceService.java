/*******************************************************************************
 * Copyright (c) 2013 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle.resource;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.naming.QualifiedName;

import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultTurtleResourceService.class)
//resource internal index in particular for providing
//pre-calculated resolved URIs and corresponding qualified names
public interface TurtleResourceService {

	void initialiseIndex(EObject root);

	//for space optimisations, the index may have calculated a short fragment
	String getFragment(EObject object);
	EObject getObject(Resource resource, String fragment);

	QualifiedName getQualifiedName(EObject object);
	String getUriString(EObject object);
}
