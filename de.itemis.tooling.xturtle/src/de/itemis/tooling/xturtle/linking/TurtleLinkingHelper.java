/*******************************************************************************
 * Copyright (c) 2013 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle.linking;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.CrossReference;
import org.eclipse.xtext.linking.impl.LinkingHelper;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;

import com.google.inject.Inject;

import de.itemis.tooling.xturtle.resource.TurtleResourceService;
import de.itemis.tooling.xturtle.services.XturtleGrammarAccess;

public class TurtleLinkingHelper extends LinkingHelper {

	private CrossReference prefixIdRef1;
	private CrossReference prefixIdRef2;
	@Inject 
	TurtleResourceService service;

	@Inject
	public TurtleLinkingHelper(XturtleGrammarAccess grammarAccess) {
		prefixIdRef1=grammarAccess.getQNameRefAccess().getPrefixPrefixIdCrossReference_1_0();
		prefixIdRef2=grammarAccess.getQNameDefAccess().getPrefixPrefixIdCrossReference_1_0();
	}

	//all linkable names except prefix ids (in definition and reference) are URIs
	@Override
	public String getCrossRefNodeAsString(INode node, boolean convert) {
		EObject semantic = NodeModelUtils.findActualSemanticObjectFor(node);
		EObject grammarElement = node.getGrammarElement();
		if (grammarElement != prefixIdRef1 && grammarElement != prefixIdRef2){
			String uri = service.getUriString(semantic);
			if(uri!=null){
				return uri;
			}
		}
		return super.getCrossRefNodeAsString(node, convert);
	}
}
