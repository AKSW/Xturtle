/*******************************************************************************
 * Copyright (c) 2013 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle.ui.syntaxcoloring;

import java.util.List;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightedPositionAcceptor;
import org.eclipse.xtext.ui.editor.syntaxcoloring.ISemanticHighlightingCalculator;

import com.google.common.base.Ascii;

import de.itemis.tooling.xturtle.xturtle.QNameDef;
import de.itemis.tooling.xturtle.xturtle.QNameRef;
import de.itemis.tooling.xturtle.xturtle.UriRef;
import de.itemis.tooling.xturtle.xturtle.XturtlePackage;

public class TurtleSemanticHighlighter implements
		ISemanticHighlightingCalculator {

	public void provideHighlightingFor(XtextResource resource,
			IHighlightedPositionAcceptor acceptor) {
		if(resource!=null){
			TreeIterator<EObject> iterator = resource.getAllContents();
			while(iterator.hasNext()){
				highlight(iterator.next(), acceptor);
			}
		}
	}

	private void highlight(EObject obj, IHighlightedPositionAcceptor acceptor) {
		if(obj instanceof UriRef){
			highlightUriRef((UriRef)obj,acceptor);
		} else if(obj instanceof QNameRef){
			highlightQnameRef((QNameRef)obj, acceptor);
		} else if(obj instanceof QNameDef){
			highlightQnameDef((QNameDef)obj, acceptor);
		}
	}

	private void highlightUriRef(UriRef ref, IHighlightedPositionAcceptor acceptor) {
		if(ref.getRef()!=null&&ref.getRef().eIsProxy()){
			INode node = NodeModelUtils.findNodesForFeature(ref, XturtlePackage.Literals.RESOURCE_REF__REF).get(0);
			acceptor.addPosition(node.getOffset(), node.getLength(), TurtleHighlightingConfig.URI_ID_UNRESOLVABLE);
		}
	}

	private void highlightQnameDef(QNameDef def, IHighlightedPositionAcceptor acceptor) {
		if(def.getPrefix()!=null){
			highlightPrefix(def, XturtlePackage.Literals.QNAME_DEF__PREFIX, acceptor);
		}
		highlightLocalName(def, XturtlePackage.Literals.QNAME_DEF__ID, acceptor);
	}

	private void highlightQnameRef(QNameRef ref, IHighlightedPositionAcceptor acceptor) {
		if(ref.getPrefix()!=null){
			highlightPrefix(ref, XturtlePackage.Literals.QNAME_REF__PREFIX, acceptor);
		}
		highlightLocalName(ref, XturtlePackage.Literals.RESOURCE_REF__REF, acceptor);
	}

	private void highlightPrefix(EObject defOrRef, EStructuralFeature prefixFeature, IHighlightedPositionAcceptor acceptor){
		List<INode> nodes = NodeModelUtils.findNodesForFeature(defOrRef, prefixFeature);
		if(!nodes.isEmpty()){
			INode node = nodes.get(0);
			acceptor.addPosition(node.getOffset(), node.getLength(), TurtleHighlightingConfig.PREFIX_ID);
		}
	}

	private void highlightLocalName(EObject defOrRef, EStructuralFeature localNameFeature, IHighlightedPositionAcceptor acceptor){
		List<INode> nodes = NodeModelUtils.findNodesForFeature(defOrRef, localNameFeature);
		if(nodes.size()!=0){
			INode node = nodes.get(0);
			String text=node.getText();
			if(text!=null&&text.length()>1){
				String coloringId = TurtleHighlightingConfig.PROPERTY_ID;
				if(Ascii.isUpperCase(text.charAt(1))){
					coloringId=TurtleHighlightingConfig.CLASS_ID;
				}
				acceptor.addPosition(node.getOffset()+1, node.getLength()-1, coloringId);
			}
		}
	}
}
