/*******************************************************************************
 * Copyright (c) 2013 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle.ui.preferences;

/**
 * Constant definitions for plug-in preferences
 */
public class TurtlePreferenceConstants {

	private static final String LANGUAGE_PREFIX="de.itemis.tooling.xturtle.Xturtle";

	//label preferences
	public static final String LABEL_PREFERENCE_KEY = LANGUAGE_PREFIX+".indexing.label";

	//description preferences
	public static final String DESCRIPTION_PREFERENCE_KEY = LANGUAGE_PREFIX+".indexing.description";
	public static final String USE_NOLANGUAGE_PREFERENCE_KEY = LANGUAGE_PREFIX+".indexing.description.nolanguage";
	public static final String USE_DEFAULT_LANGUAGE_PREFERENCE_KEY = LANGUAGE_PREFIX+".indexing.description.defaultlanguage";
	public static final String LANGUAGES_PREFERENCE_KEY = LANGUAGE_PREFIX+".indexing.description.languages";

	//folding preferences
	public static final String FOLD_DIRECTIVES_KEY = LANGUAGE_PREFIX+".folding.directives";
	public static final String FOLD_STRINGS_KEY = LANGUAGE_PREFIX+".folding.strings";
	public static final String FOLD_TRIPLES_KEY = LANGUAGE_PREFIX+".folding.triples";
	public static final String FOLD_BLANK_COLL = LANGUAGE_PREFIX+".folding.bcol";
	public static final String FOLD_BLANK_OBJ = LANGUAGE_PREFIX+".folding.bobj";

	//languages (content assist)
	public static final String CA_LANGUAGES_KEY = LANGUAGE_PREFIX+".contentassist.languages";

}
