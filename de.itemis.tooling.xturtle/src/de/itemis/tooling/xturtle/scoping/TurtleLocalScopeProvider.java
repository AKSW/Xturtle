/*******************************************************************************
 * Copyright (c) 2013 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle.scoping;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.resource.ISelectable;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.impl.ImportNormalizer;
import org.eclipse.xtext.scoping.impl.ImportedNamespaceAwareLocalScopeProvider;
import org.eclipse.xtext.scoping.impl.MultimapBasedSelectable;

import com.google.inject.Inject;

public class TurtleLocalScopeProvider extends
		ImportedNamespaceAwareLocalScopeProvider {

	@Inject
	IResourceServiceProvider services;

	@Override
	protected List<ImportNormalizer> getImplicitImports(boolean ignoreCase) {
		return Collections.emptyList();
	}

	@Override
	public IScope getScope(EObject context, EReference reference) {
		if (context == null)
			throw new NullPointerException("context");
		IScope result = getResourceScope(context.eResource(), reference);
		return getLocalElementsScope(result, context, reference);
	}

	@Override
	protected List<ImportNormalizer> getImportedNamespaceResolvers(
			EObject context, boolean ignoreCase) {
		return Collections.emptyList();
	}

	@Override
	protected ISelectable internalGetAllDescriptions(Resource resource) {
		// use index for internal objects as well, 
		//otherwise label and description user data are not available (content assist)
		IResourceDescription rd = services.getResourceDescriptionManager()
				.getResourceDescription(resource);
		Iterable<IEObjectDescription> allDescriptions = rd.getExportedObjects();
		return new MultimapBasedSelectable(allDescriptions);
		// return super.internalGetAllDescriptions(resource);
	}
}
