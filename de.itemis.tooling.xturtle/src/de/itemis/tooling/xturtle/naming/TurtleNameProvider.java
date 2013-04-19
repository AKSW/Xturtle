/*******************************************************************************
 * Copyright (c) 2013 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle.naming;

import org.eclipse.xtext.naming.DefaultDeclarativeQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;

import com.google.inject.Inject;

import de.itemis.tooling.xturtle.resource.TurtleResourceService;
import de.itemis.tooling.xturtle.xturtle.Resource;

public class TurtleNameProvider extends DefaultDeclarativeQualifiedNameProvider {

	@Inject 
	TurtleResourceService service;

	//QName defs and UriDefs have a name that is already calculated by the resource service
	public QualifiedName qualifiedName(Resource res){
		return service.getQualifiedName(res);
	}
}
