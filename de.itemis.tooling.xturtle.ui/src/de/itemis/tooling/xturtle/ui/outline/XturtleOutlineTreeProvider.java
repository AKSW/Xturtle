/*******************************************************************************
 * Copyright (c) 2013 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
/*
* generated by Xtext
*/
package de.itemis.tooling.xturtle.ui.outline;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.graphics.Image;
import org.eclipse.xtext.ui.editor.outline.IOutlineNode;
import org.eclipse.xtext.ui.editor.outline.impl.AbstractOutlineNode;
import org.eclipse.xtext.ui.editor.outline.impl.DefaultOutlineTreeProvider;
import org.eclipse.xtext.ui.editor.outline.impl.DocumentRootNode;

import de.itemis.tooling.xturtle.xturtle.Directive;
import de.itemis.tooling.xturtle.xturtle.DirectiveBlock;
import de.itemis.tooling.xturtle.xturtle.Directives;
import de.itemis.tooling.xturtle.xturtle.Triples;

/**
 * customization of the default outline structure
 * 
 */
public class XturtleOutlineTreeProvider extends DefaultOutlineTreeProvider {
	protected boolean _isLeaf(DirectiveBlock doc) {
		return false;
	}

	protected boolean _isLeaf(org.eclipse.emf.ecore.EObject modelElement) {
		return true;
	}

	protected void _createNode(IOutlineNode parentNode, Triples modelElement) {
		createEObjectNode(parentNode, modelElement, labelProvider.getImage(modelElement.getSubject()), labelProvider.getText(modelElement.getSubject()), true);
	}

	@Override
	protected void _createChildren(DocumentRootNode parentNode,
			EObject modelElement) {
		DirectiveBlock doc = (DirectiveBlock)modelElement;
		createDirectiveBlocksNodes(parentNode,doc);
	}

	private void createTriplesNodes(IOutlineNode parentNode, List<Triples> triples) {
		if(triples!=null){
			for (Triples triple : triples) {
				createEObjectNode(parentNode, triple, labelProvider.getImage(triple.getSubject()), labelProvider.getText(triple.getSubject()), true);
			}
		}
	}

	private void createDirectiveBlocksNodes(IOutlineNode parentNode, DirectiveBlock block){
		if(block !=null){
			createDirectivesNode(parentNode, block.getDirectives());
		} else{
			return;
		}
		createTriplesNodes(parentNode, block.getTriples());
		createDirectiveBlocksNodes(parentNode, block.getDirectiveblock());
	}

	private void createDirectivesNode(IOutlineNode parentNode,
			Directives direcives) {
		if(direcives!=null && direcives.getDirective().size()>0){
			AbstractOutlineNode newParent = new AbstractOutlineNode(parentNode,(Image)null,"Directives",false) {};
			for (Directive directive : direcives.getDirective()) {
				super.createEObjectNode(newParent, directive);
			}
		}
	}
}