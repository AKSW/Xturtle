package de.itemis.tooling.xturtle.ui.findrefs;

import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.resource.IResourceServiceProvider.Registry;
import org.eclipse.xtext.ui.editor.findrefs.DefaultReferenceFinder;

import com.google.inject.Inject;

import de.itemis.tooling.xturtle.xturtle.ResourceRef;
import de.itemis.tooling.xturtle.xturtle.Triples;

@SuppressWarnings("restriction")
public class TurtleReferenceFinder extends DefaultReferenceFinder {

	@Inject
	public TurtleReferenceFinder(IResourceDescriptions indexData,
			Registry serviceProviderRegistry) {
		super(indexData, serviceProviderRegistry);
	}
	
	@Override
	protected URI findClosestExportedContainerURI(EObject element,
			Map<EObject, URI> exportedElementsMap) {
		if(element instanceof ResourceRef){
			Triples triples = EcoreUtil2.getContainerOfType(element, Triples.class);
			//the subject is not exactly the container of a reference, but it represents the triple
			return super.findClosestExportedContainerURI(triples.getSubject(), exportedElementsMap);
		}else{
			return super.findClosestExportedContainerURI(element, exportedElementsMap);
		}
	}

}
