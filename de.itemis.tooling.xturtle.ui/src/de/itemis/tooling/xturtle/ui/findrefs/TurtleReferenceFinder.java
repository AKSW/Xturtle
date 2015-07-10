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
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.EObjectDescription;
import org.eclipse.xtext.resource.IReferenceDescription;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.resource.IResourceServiceProvider.Registry;
import org.eclipse.xtext.resource.impl.DefaultReferenceDescription;
import org.eclipse.xtext.ui.editor.findrefs.DefaultReferenceFinder;
import org.eclipse.xtext.util.IAcceptor;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;

import de.itemis.tooling.xturtle.resource.TurtleReferenceDescription;
import de.itemis.tooling.xturtle.resource.TurtleResourceService;
import de.itemis.tooling.xturtle.xturtle.Blank;
import de.itemis.tooling.xturtle.xturtle.PrefixId;
import de.itemis.tooling.xturtle.xturtle.QNameRef;
import de.itemis.tooling.xturtle.xturtle.ResourceRef;
import de.itemis.tooling.xturtle.xturtle.Subject;
import de.itemis.tooling.xturtle.xturtle.XturtlePackage;

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
		URI exportedContainerURI=currentExportedContainerURI;
		Iterator<URI> it = targetURISet.iterator();
		while(it.hasNext()){
			URI next=it.next();
			EObject obj = service.getObject(localResource, next.fragment());
			QualifiedName name = service.getQualifiedName(obj);

			if(sourceCandidate instanceof ResourceRef){
				QualifiedName sourceName = service.getQualifiedName(sourceCandidate);
				if(name.equals(sourceName)){
					acceptor.accept(new TurtleReferenceDescription(sourceCandidate,EObjectDescription.create("", obj),exportedContainerURI));
				}
				if(sourceCandidate instanceof QNameRef){
					PrefixId prefix = ((QNameRef)sourceCandidate).getPrefix();
					String fragment = service.getFragment(prefix);
					if(fragment!=null && fragment.equals(next.fragment())){
						acceptor.accept(new DefaultReferenceDescription(EcoreUtil.getURI(sourceCandidate), next, XturtlePackage.Literals.QNAME_REF__PREFIX, 0, exportedContainerURI));
					}
				}
			} else{
				EList<EObject> contents = sourceCandidate.eContents();
				for (EObject obj2 : contents) {
					if(obj2 instanceof Subject){
						if(obj2 instanceof Blank){
							//do not change containerURI
						}else{
							exportedContainerURI=localResource.getURI().appendFragment(service.getFragment(obj2));
						}
					}
					findLocalReferencesFromElement(targetURISet, obj2, localResource, acceptor, exportedContainerURI);
				}
			}
		}
	}
	//see TurtleIndexingStrategy
}
