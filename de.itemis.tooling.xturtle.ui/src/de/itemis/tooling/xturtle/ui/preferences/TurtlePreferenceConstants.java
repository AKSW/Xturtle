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

	//validation
	public static final String VALIDATION_UNRESOLVED_URI_KEY = LANGUAGE_PREFIX+".validation.unresolvedUri";
	public static final String VALIDATION_UNRESOLVED_QNAME_KEY = LANGUAGE_PREFIX+".validation.unresolvedQname";
	public static final String VALIDATION_PREFIX_MISMATCH_KEY = LANGUAGE_PREFIX+".validation.prefixMismatch";
	public static final String VALIDATION_NS_MISMATCH_KEY = LANGUAGE_PREFIX+".validation.namespaceMismatch";
	public static final String VALIDATION_UNUSED_PREFIX_KEY = LANGUAGE_PREFIX+".validation.unusedPrefix";
	
}
