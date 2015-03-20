/*******************************************************************************
 * Copyright (c) 2013 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle.ui.findrefs;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.EObjectDescription;
import org.eclipse.xtext.resource.IReferenceDescription;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.resource.IResourceServiceProvider.Registry;
import org.eclipse.xtext.ui.editor.findrefs.DefaultReferenceFinder;
import org.eclipse.xtext.util.IAcceptor;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;

import de.itemis.tooling.xturtle.resource.TurtleReferenceDescription;
import de.itemis.tooling.xturtle.resource.TurtleResourceService;
import de.itemis.tooling.xturtle.xturtle.ResourceRef;

@SuppressWarnings("restriction")
public class TurtleReferenceFinder extends DefaultReferenceFinder {

	@Inject
	TurtleResourceService service;

	@Inject
	public TurtleReferenceFinder(IResourceDescriptions indexData,
			Registry serviceProviderRegistry) {
		super(indexData, serviceProviderRegistry);
	}

	@Override
	protected void findLocalReferencesInResource(final Iterable<URI> targetURIs, Resource resource,
			final IAcceptor<IReferenceDescription> acceptor) {
		Set<URI> targetURISet = ImmutableSet.copyOf(targetURIs);
//		Map<EObject, URI> exportedElementsMap = createExportedElementsMap(resource);
		for(EObject content: resource.getContents()) {
			findLocalReferencesFromElement(targetURISet, content, resource, acceptor, resource.getURI());
		}
	}

	protected void findLocalReferencesFromElement(Set<URI> targetURISet,
			EObject sourceCandidate,
			org.eclipse.emf.ecore.resource.Resource localResource,
			IAcceptor<IReferenceDescription> acceptor,
			URI currentExportedContainerURI) {

		Iterator<URI> it = targetURISet.iterator();
		while(it.hasNext()){
			URI next=it.next();
			EObject obj = service.getObject(localResource, next.fragment());
			QualifiedName name = service.getQualifiedName(obj);

			if(sourceCandidate instanceof ResourceRef){
				QualifiedName sourceName = service.getQualifiedName(sourceCandidate);
				if(name.equals(sourceName)){
					acceptor.accept(new TurtleReferenceDescription(sourceCandidate,EObjectDescription.create("", obj),currentExportedContainerURI));
				}
			}else{
				EList<EObject> contents = sourceCandidate.eContents();
				for (EObject obj2 : contents) {
					findLocalReferencesFromElement(targetURISet, obj2, localResource, acceptor, currentExportedContainerURI);
				}
			}
		}
	}
	//see TurtleIndexingStrategy
}
