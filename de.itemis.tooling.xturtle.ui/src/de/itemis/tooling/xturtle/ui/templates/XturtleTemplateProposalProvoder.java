/*******************************************************************************
 * Copyright (c) 2015 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle.ui.templates;

import javax.inject.Inject;

import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ui.editor.contentassist.ITemplateAcceptor;
import org.eclipse.xtext.ui.editor.templates.ContextTypeIdHelper;
import org.eclipse.xtext.ui.editor.templates.DefaultTemplateProposalProvider;

public class XturtleTemplateProposalProvoder extends DefaultTemplateProposalProvider {

	@Inject
	public XturtleTemplateProposalProvoder(TemplateStore templateStore, ContextTypeRegistry registry,
			ContextTypeIdHelper helper) {
		super(templateStore, registry, helper);
	}

	@Override
	public void createTemplates(ContentAssistContext context, ITemplateAcceptor acceptor) {
		boolean noWhiteSpace=(context.getOffset()<=context.getLastCompleteNode().getTotalEndOffset());
		if(noWhiteSpace){
			return;
		}
		super.createTemplates(context, acceptor);
	}

}
