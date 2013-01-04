package de.itemis.tooling.xturtle.validation;

import org.eclipse.xtext.diagnostics.Severity;

public class TurtleFixedSeverityLevels implements
		TurtleValidationSeverityLevels {

	public Severity getUnresolvedUriRefLevel() {
		return null;
	}
	public Severity getUnresolvedQNameLevel() {
		return Severity.ERROR;
	}
	public Severity getPrefixMismatchLevel() {
		return Severity.WARNING;
	}
	public Severity getNamespaceMismatchLevel() {
		return Severity.WARNING;
	}
	public Severity getUnusedPrefixLevel() {
		return Severity.ERROR;
	}
	public Severity getXsdTypeLevel() {
		return Severity.INFO;
	}
}
