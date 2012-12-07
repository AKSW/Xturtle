package de.itemis.tooling.xturtle.linking;

import org.eclipse.xtext.diagnostics.Diagnostic;
import org.eclipse.xtext.diagnostics.DiagnosticMessage;
import org.eclipse.xtext.diagnostics.Severity;
import org.eclipse.xtext.linking.impl.LinkingDiagnosticMessageProvider;

import com.google.inject.Inject;

import de.itemis.tooling.xturtle.resource.TurtleResourceService;
import de.itemis.tooling.xturtle.validation.TurtleValidationSeverityLevels;
import de.itemis.tooling.xturtle.xturtle.ResourceRef;
import de.itemis.tooling.xturtle.xturtle.UriRef;

public class TurtleLinkingErrors extends LinkingDiagnosticMessageProvider {
	@Inject 
	TurtleResourceService service;
	@Inject 
	TurtleValidationSeverityLevels levels;

	@Override
	public DiagnosticMessage getUnresolvedProxyMessage(
			ILinkingDiagnosticContext context) {
		if(context.getContext() instanceof ResourceRef){
			Severity severity = levels.getUnresolvedQNameLevel();
			if(context.getContext() instanceof UriRef){
				severity= levels.getUnresolvedUriRefLevel();
			}
			if(severity!=null){
				return new DiagnosticMessage("could not find defintion for "+service.getUriString(context.getContext()), severity, Diagnostic.LINKING_DIAGNOSTIC);
			}else{
				return null;
			}
		}
		return super.getUnresolvedProxyMessage(context);
	}
}