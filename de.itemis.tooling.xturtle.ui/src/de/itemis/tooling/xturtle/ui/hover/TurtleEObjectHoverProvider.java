/*******************************************************************************
 * Copyright (c) 2013 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle.ui.hover;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.resource.impl.ResourceDescriptionsProvider;
import org.eclipse.xtext.ui.editor.hover.html.DefaultEObjectHoverProvider;

import com.google.inject.Inject;

import de.itemis.tooling.xturtle.resource.TurtleResourceService;
import de.itemis.tooling.xturtle.xturtle.XturtlePackage;

public class TurtleEObjectHoverProvider extends DefaultEObjectHoverProvider {

	@Inject 
	TurtleResourceService service;

	@Inject
	ResourceDescriptionsProvider indexService;
	@Override
	protected String getFirstLine(EObject o) {
		String uri = service.getUriString(o);
		if(uri!=null){
			return "<b>"+uri+"</b>";
		}
		return "";
	}
	
	@Override
	protected String getDocumentation(EObject o) {
		QualifiedName qName = service.getQualifiedName(o);
		TurtleHoverInfoCollector collector=new TurtleHoverInfoCollector();
		if(qName!=null){
			IResourceDescriptions index = indexService.getResourceDescriptions(o.eResource());
			Iterable<IEObjectDescription> matches = index.getExportedObjects(XturtlePackage.Literals.RESOURCE, qName, false);
			for (IEObjectDescription match : matches) {
				String desc = match.getUserData("descr");
				collector.addDescriptionUserData(desc);
			}
		}
		return collector.getAsHtml();
	}

	@Override
	protected boolean hasHover(EObject o) {
		return service.getUriString(o)!=null;
	}
}
