package de.itemis.tooling.xturtle.validation;

import org.eclipse.xtext.diagnostics.Severity;


public interface TurtleValidationSeverityLevels {

	Severity getUnresolvedUriRefLevel();
	Severity getUnresolvedQNameLevel();
	Severity getPrefixMismatchLevel();
	Severity getNamespaceMismatchLevel();
	Severity getUnusedPrefixLevel();
}
