/*******************************************************************************
 * Copyright (c) 2013 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle.resource;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.nodemodel.ICompositeNode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.resource.DefaultLocationInFileProvider;
import org.eclipse.xtext.util.ITextRegion;
import org.eclipse.xtext.util.TextRegion;

import de.itemis.tooling.xturtle.xturtle.BlankCollection;
import de.itemis.tooling.xturtle.xturtle.BlankObjects;
import de.itemis.tooling.xturtle.xturtle.StringLiteral;
import de.itemis.tooling.xturtle.xturtle.Triples;
import de.itemis.tooling.xturtle.xturtle.XturtlePackage;

public class TurtleLocationInFileProvider extends DefaultLocationInFileProvider {

	@Override
	public ITextRegion getSignificantTextRegion(EObject obj) {
		if(obj instanceof Triples){
			//the significat part of a triple is its subject (not some keyword)
			return getSignificantTextRegion(((Triples) obj).getSubject());
		}else if(obj instanceof StringLiteral){
			//in particular for folding, the significant region would otherwise be a
			//potential type of language keyword
			return getSignificantTextRegion(obj, XturtlePackage.Literals.LITERAL__VALUE,-1);
		} else if(obj instanceof BlankObjects || obj instanceof BlankCollection){
			ICompositeNode node = NodeModelUtils.findActualNodeFor(obj);
			return new TextRegion(node.getOffset(), 1);
		}
		return super.getSignificantTextRegion(obj);
	}
}
