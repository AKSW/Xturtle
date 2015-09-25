/*********************************************************************************
 * Copyright (c) 2013-2015 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *********************************************************************************/
package de.itemis.tooling.xturtle.linking;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.diagnostics.Diagnostic;
import org.eclipse.xtext.diagnostics.DiagnosticMessage;
import org.eclipse.xtext.diagnostics.Severity;
import org.eclipse.xtext.linking.impl.LinkingDiagnosticMessageProvider;

import com.google.inject.Inject;

import de.itemis.tooling.xturtle.resource.TurtleResourceService;
import de.itemis.tooling.xturtle.validation.TurtleLinkingErrorExceptions;
import de.itemis.tooling.xturtle.validation.TurtleValidationSeverityLevels;
import de.itemis.tooling.xturtle.validation.XturtleJavaValidator;
import de.itemis.tooling.xturtle.xturtle.QNameDef;
import de.itemis.tooling.xturtle.xturtle.QNameRef;
import de.itemis.tooling.xturtle.xturtle.ResourceRef;
import de.itemis.tooling.xturtle.xturtle.UriRef;
import de.itemis.tooling.xturtle.xturtle.XturtlePackage;

public class TurtleLinkingErrors extends LinkingDiagnosticMessageProvider {
	@Inject 
	TurtleResourceService service;
	@Inject 
	TurtleValidationSeverityLevels levels;
	@Inject
	TurtleLinkingErrorExceptions exceptions;


	private DiagnosticMessage getMissingPrefixDefinitionErrorMessage(String linkText){
		boolean isBlankLabelPrefix=linkText.length()==1 &&linkText.charAt(0)=='_';
		if(isBlankLabelPrefix){
			//no prefix definition for blank label prefix '_' allowed, so it is OK that it is missing
			return null;
		}else{
			return new DiagnosticMessage("no @prefix-Definition for "+linkText+" up to this point", Severity.ERROR, XturtleJavaValidator.UNKNOWN_PREFIX,linkText);
		}
	}

	@Override
	public DiagnosticMessage getUnresolvedProxyMessage(
			ILinkingDiagnosticContext context) {
		EObject object = context.getContext();
		String linkText=context.getLinkText();
		if(object instanceof ResourceRef){
			//unlinked prefix
			if(context.getReference()==XturtlePackage.Literals.QNAME_REF__PREFIX){
				return getMissingPrefixDefinitionErrorMessage(linkText);
			}

			Severity severity=null;
			if(isNoLinkingError(object)){
				return null;
			}else if(object instanceof UriRef){
				severity= levels.getUnresolvedUriRefLevel();
			} else if(object instanceof QNameRef){
				//if the prefix is unknown the qualified name will be null
				//an unresolved prefix is dealt with separately
				if(service.getQualifiedName(object)!=null){
					severity = levels.getUnresolvedQNameLevel();
				}
			}
			if(severity!=null){
				return new DiagnosticMessage("could not find definition for "+service.getUriString(object), severity, Diagnostic.LINKING_DIAGNOSTIC);
			}else{
				return null;
			}
		} else if(object instanceof QNameDef){
			return getMissingPrefixDefinitionErrorMessage(linkText);
		}
		return super.getUnresolvedProxyMessage(context);
	}

	private boolean isNoLinkingError(EObject object) {
		if(object instanceof ResourceRef){
			String uri = service.getUriString(object);
			if(exceptions.ignoreLinkingError(uri)){
				return true;
			}
		}
		return false;
	}
}
