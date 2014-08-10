package de.itemis.tooling.xturtle;

import org.eclipse.xtext.diagnostics.Severity;

import de.itemis.tooling.xturtle.validation.TurtleValidationSeverityLevels;

public class NullSeverities implements TurtleValidationSeverityLevels {

	public Severity getUnresolvedUriRefLevel() {
		return null;
	}

	public Severity getUnresolvedQNameLevel() {
		return null;
	}

	public Severity getPrefixMismatchLevel() {
		return null;
	}

	public Severity getNamespaceMismatchLevel() {
		return null;
	}

	public Severity getUnusedPrefixLevel() {
		return null;
	}

	public Severity getXsdTypeLevel() {
		return null;
	}

	public Severity getDuplicateSubjectLevel() {
		return null;
	}
}
