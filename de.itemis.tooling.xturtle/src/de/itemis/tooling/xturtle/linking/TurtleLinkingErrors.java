package de.itemis.tooling.xturtle.linking;

import org.eclipse.xtext.diagnostics.Diagnostic;
import org.eclipse.xtext.diagnostics.DiagnosticMessage;
import org.eclipse.xtext.diagnostics.Severity;
import org.eclipse.xtext.linking.impl.LinkingDiagnosticMessageProvider;

import com.google.inject.Inject;

import de.itemis.tooling.xturtle.resource.TurtleResourceService;
import de.itemis.tooling.xturtle.xturtle.ResourceRef;
import de.itemis.tooling.xturtle.xturtle.UriRef;

public class TurtleLinkingErrors extends LinkingDiagnosticMessageProvider {
	@Inject 
	TurtleResourceService service;

	@Override
	public DiagnosticMessage getUnresolvedProxyMessage(
			ILinkingDiagnosticContext context) {
		if(context.getContext() instanceof ResourceRef){
			Severity severity = Severity.ERROR;
			if(context.getContext() instanceof UriRef){
				//severity=Severity.WARNING;
				//no error message for explicit URIs
				return null;
			}
			return new DiagnosticMessage("could not find defintion for "+service.getUriString(context.getContext()), severity, Diagnostic.LINKING_DIAGNOSTIC);
		}
		return super.getUnresolvedProxyMessage(context);
	}
}