package de.itemis.tooling.xturtle.linking;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.diagnostics.Diagnostic;
import org.eclipse.xtext.diagnostics.DiagnosticMessage;
import org.eclipse.xtext.diagnostics.Severity;
import org.eclipse.xtext.linking.impl.LinkingDiagnosticMessageProvider;

import com.google.inject.Inject;

import de.itemis.tooling.xturtle.resource.TurtleResourceService;
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

	@Override
	public DiagnosticMessage getUnresolvedProxyMessage(
			ILinkingDiagnosticContext context) {
		EObject object = context.getContext();
		String linkText=context.getLinkText();
		if(object instanceof ResourceRef){
			//unlinked prefix
			if(context.getReference()==XturtlePackage.Literals.QNAME_REF__PREFIX){
				return new DiagnosticMessage("no @prefix-Definition up to this point", Severity.ERROR, XturtleJavaValidator.UNKNOWN_PREFIX,linkText);
			}

			Severity severity=null;
			if(object instanceof UriRef){
				severity= levels.getUnresolvedUriRefLevel();
			} else if(object instanceof QNameRef){
				//if the prefix is unknown the qualified name will be null
				//an unresolved prefix is dealt with separately
				if(service.getQualifiedName(object)!=null){
					severity = levels.getUnresolvedQNameLevel();
				}
			}
			if(severity!=null){
				return new DiagnosticMessage("could not find defintion for "+service.getUriString(object), severity, Diagnostic.LINKING_DIAGNOSTIC);
			}else{
				return null;
			}
		} else if(object instanceof QNameDef){
			return new DiagnosticMessage("no @prefix-Definition up to this point", Severity.ERROR, XturtleJavaValidator.UNKNOWN_PREFIX,linkText);
		}
		return super.getUnresolvedProxyMessage(context);
	}
}