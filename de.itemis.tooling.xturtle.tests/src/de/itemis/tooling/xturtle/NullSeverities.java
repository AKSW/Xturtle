package de.itemis.tooling.xturtle;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.diagnostics.Severity;
import org.eclipse.xtext.validation.IssueSeverities;

import de.itemis.tooling.xturtle.validation.TurtleIssuesSeveritiesProvider;

public class NullSeverities extends TurtleIssuesSeveritiesProvider {

	@Override
	public IssueSeverities getIssueSeverities(Resource context) {
		return new IssueSeverities(null, null, null){
			@Override
			public Severity getSeverity(String code) {
				return Severity.IGNORE;
			}
		};
	}
}
