/*******************************************************************************
 * Copyright (c) 2013 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle.resource;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IReferenceDescription;

import de.itemis.tooling.xturtle.xturtle.XturtlePackage;

public class TurtleReferenceDescription implements IReferenceDescription {

	private URI exportedContainer;
	private URI sourceUri;
	private URI targetUri;

	public TurtleReferenceDescription(EObject from, IEObjectDescription target, URI containerUri) {
		exportedContainer=containerUri;
		sourceUri=EcoreUtil.getURI(from);
		targetUri=target.getEObjectURI();
	}

	public URI getSourceEObjectUri() {
		return sourceUri;
	}

	public URI getTargetEObjectUri() {
		return targetUri;
	}

	public int getIndexInList() {
		return -1;
	}

	public EReference getEReference() {
		return XturtlePackage.Literals.RESOURCE_REF__REF;
	}

	public URI getContainerEObjectURI() {
		return exportedContainer;
	}

}
