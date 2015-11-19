package de.itemis.tooling.xturtle;

import org.eclipse.xtext.junit4.GlobalRegistries;
import org.eclipse.xtext.validation.IssueSeveritiesProvider;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.itemis.tooling.xturtle.validation.TurtleIssuesSeveritiesProvider;

public class NoValidationInjectorProvider extends XturtleInjectorProvider {

	public Injector getInjector() {
		if (injector == null) {
			stateBeforeInjectorCreation = GlobalRegistries.makeCopyOfGlobalState();
			this.injector = new XturtleStandaloneSetup() {
				@Override
				public Injector createInjector() {
					return Guice.createInjector(new XturtleRuntimeModule(){
						@Override
						public Class<? extends IssueSeveritiesProvider> bindIssueSeverityServiceProvider() {
							return NullSeverities.class;
						}

						@SuppressWarnings("unused")
						public Class<? extends TurtleIssuesSeveritiesProvider> bindTurtleIssueSeverityServiceProvider() {
							return NullSeverities.class;
						}
					});
				}
			}.createInjectorAndDoEMFRegistration();
			stateAfterInjectorCreation = GlobalRegistries.makeCopyOfGlobalState();
		}
		return injector;
	}
}
