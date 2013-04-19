/*******************************************************************************
 * Copyright (c) 2013 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
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
	public Severity getDuplicateSubjectLevel() {
		return Severity.ERROR;
	}
}
