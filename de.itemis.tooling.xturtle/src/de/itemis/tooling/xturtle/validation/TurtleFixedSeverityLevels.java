/*******************************************************************************
 * Copyright (c) 2013 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle.validation;

import java.util.List;

import org.eclipse.xtext.diagnostics.Severity;

import com.google.common.collect.ImmutableList;

public class TurtleFixedSeverityLevels implements
		TurtleValidationSeverityLevels, TurtleNoLinkingValidationUriPrefixes {

	@Override
	public Severity getUnresolvedUriRefLevel() {
		return null;
	}
	@Override
	public Severity getUnresolvedQNameLevel() {
		return Severity.ERROR;
	}
	@Override
	public Severity getPrefixMismatchLevel() {
		return Severity.WARNING;
	}
	@Override
	public Severity getNamespaceMismatchLevel() {
		return Severity.WARNING;
	}
	@Override
	public Severity getUnusedPrefixLevel() {
		return Severity.ERROR;
	}
	@Override
	public Severity getXsdTypeLevel() {
		return Severity.INFO;
	}
	@Override
	public Severity getDuplicateSubjectLevel() {
		return Severity.ERROR;
	}

	private List<String> testIgnorePrefixes=ImmutableList.of("http://dbpedia.org/resource/");

	@Override
	public List<String> getPrefixes() {
		return testIgnorePrefixes;
	}
}
