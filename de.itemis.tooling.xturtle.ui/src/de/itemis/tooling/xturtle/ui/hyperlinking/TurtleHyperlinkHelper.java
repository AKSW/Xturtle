/*******************************************************************************
 * Copyright (c) 2013 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle.ui.hyperlinking;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.URLHyperlink;
import org.eclipse.xtext.Keyword;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.nodemodel.ILeafNode;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.resource.EObjectAtOffsetHelper;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.impl.ResourceDescriptionsProvider;
import org.eclipse.xtext.ui.editor.hyperlinking.HyperlinkHelper;
import org.eclipse.xtext.ui.editor.hyperlinking.IHyperlinkAcceptor;
import org.eclipse.xtext.ui.editor.hyperlinking.XtextHyperlink;

import de.itemis.tooling.xturtle.resource.TurtleResourceService;
import de.itemis.tooling.xturtle.xturtle.Resource;
import de.itemis.tooling.xturtle.xturtle.UriDef;
import de.itemis.tooling.xturtle.xturtle.UriRef;
import de.itemis.tooling.xturtle.xturtle.XturtlePackage;

public class TurtleHyperlinkHelper extends HyperlinkHelper {

	@Inject 
	private EObjectAtOffsetHelper helper;
	
	@Inject
	private ResourceDescriptionsProvider indexService;
	
	@Inject IQualifiedNameProvider nameProvider;
	@Inject 
	TurtleResourceService service;

	@Override
	public void createHyperlinksByOffset(XtextResource resource, int offset,
			IHyperlinkAcceptor acceptor) {
		
		EObject crossLinkedEObject = helper.resolveElementAt(resource, offset);
		if (crossLinkedEObject != null){
			INode node=NodeModelUtils.findLeafNodeAtOffset(resource.getParseResult().getRootNode(), offset);
			if (node instanceof ILeafNode && ((ILeafNode) node).isHidden()){
				//no linking in comments
				return;
			}
			Region region = new Region(node.getOffset(), node.getLength());
			if(crossLinkedEObject instanceof Resource){
				if(!crossLinkedEObject.eIsProxy()){
					//links to all known local definitions
					QualifiedName name = nameProvider.getFullyQualifiedName(crossLinkedEObject);
					IResourceDescriptions index = indexService.getResourceDescriptions(resource);
					Iterable<IEObjectDescription> matches = index.getExportedObjectsByType(XturtlePackage.Literals.RESOURCE);//, name, false);
					for (final IEObjectDescription desc : matches) {
						if(desc.getQualifiedName().equals(name)){
							XtextHyperlink result = getHyperlinkProvider().get();
							result.setHyperlinkRegion(region);
							result.setURI(desc.getEObjectURI());
							result.setHyperlinkText(desc.getEObjectURI().trimFragment().toString());
							acceptor.accept(result);
						}
					}
				}
				//always allow browser link (if uri is absolute, text editor will create URLHyperlink anyway)
				EObject actualNode=NodeModelUtils.findActualSemanticObjectFor(node);
				String uri = service.getUriString(actualNode);
				if(uri!=null && !isAbsoluteUri(actualNode)){
					URLHyperlink result = new URLHyperlink(region, uri);
					acceptor.accept(result);
				}
			} else if(!crossLinkedEObject.eIsProxy() && !(node.getGrammarElement() instanceof Keyword)) {
				//non-resource crosslinks are dealt with as in default implementation
				createHyperlinksTo(resource, region, crossLinkedEObject, acceptor);
			} 
		}
//		super.createHyperlinksByOffset(resource, offset, acceptor);
	}

	private boolean isAbsoluteUri(EObject source) {
		if(source instanceof UriDef){
			return ((UriDef) source).getUri().contains("http:");
		}else if(source instanceof UriRef){
			return NodeModelUtils.findNodesForFeature(source, XturtlePackage.Literals.RESOURCE_REF__REF).get(0).getText().contains("http:");
		}
		return false;
//		return NodeModelUtils.getNode(source).getText().contains("http:");
	}
}
