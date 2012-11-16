package de.itemis.tooling.xturtle.linking;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.diagnostics.IDiagnosticConsumer;
import org.eclipse.xtext.linking.lazy.LazyLinker;

import com.google.inject.Inject;

import de.itemis.tooling.xturtle.resource.TurtleResourceService;

public class TurtleLinker extends LazyLinker {

	@Inject 
	TurtleResourceService service;
	//initialise to resource internal index
	@Override
	protected void beforeModelLinked(EObject model,
			IDiagnosticConsumer diagnosticsConsumer) {
		super.beforeModelLinked(model, diagnosticsConsumer);
		service.initialiseIndex(model);
	}
}
