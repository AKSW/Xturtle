/*******************************************************************************
 * Copyright (c) 2013 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle.naming;

import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.naming.IQualifiedNameConverter.DefaultImpl;
import org.eclipse.xtext.naming.QualifiedName;

import de.itemis.tooling.xturtle.resource.TurtleUriResolver;

public class TurtleNameConverter extends DefaultImpl {

	@Override
	public String getDelimiter() {
		return "";
	}

	@Override
	public QualifiedName toQualifiedName(String qualifiedNameAsString) {
		return TurtleUriResolver.getName(URI.createURI(qualifiedNameAsString));
	}
}
