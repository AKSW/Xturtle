/*******************************************************************************
 * Copyright (c) 2013 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle.ui.preferences;

import javax.inject.Inject;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.google.common.base.Joiner;

/**
 * Class used to initialize default preference values.
 */
public class TurtlePreferenceInitializer extends AbstractPreferenceInitializer {

	@Inject
	IPreferenceStore store;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#
	 * initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		//label preferences
		store.setDefault(TurtlePreferenceConstants.LABEL_PREFERENCE_KEY, getDefaultLabelUris());

		//description preferences
		store.setDefault(TurtlePreferenceConstants.DESCRIPTION_PREFERENCE_KEY, getDefaultDescriptionUris());
		store.setDefault(TurtlePreferenceConstants.USE_NOLANGUAGE_PREFERENCE_KEY, true);
		store.setDefault(TurtlePreferenceConstants.USE_DEFAULT_LANGUAGE_PREFERENCE_KEY, true);
		store.setDefault(TurtlePreferenceConstants.LANGUAGES_PREFERENCE_KEY, "");

		//folding preferences
		store.setDefault(TurtlePreferenceConstants.FOLD_DIRECTIVES_KEY, true);
		store.setDefault(TurtlePreferenceConstants.FOLD_STRINGS_KEY, true);
		store.setDefault(TurtlePreferenceConstants.FOLD_TRIPLES_KEY, false);
		store.setDefault(TurtlePreferenceConstants.FOLD_BLANK_COLL, true);
		store.setDefault(TurtlePreferenceConstants.FOLD_BLANK_OBJ, true);

		//validation
		store.setDefault(TurtlePreferenceConstants.VALIDATION_NS_MISMATCH_KEY, "warn");
		store.setDefault(TurtlePreferenceConstants.VALIDATION_PREFIX_MISMATCH_KEY, "warn");
		store.setDefault(TurtlePreferenceConstants.VALIDATION_UNRESOLVED_QNAME_KEY, "error");
		store.setDefault(TurtlePreferenceConstants.VALIDATION_UNRESOLVED_URI_KEY, "null");
		store.setDefault(TurtlePreferenceConstants.VALIDATION_UNUSED_PREFIX_KEY, "info");
		store.setDefault(TurtlePreferenceConstants.VALIDATION_XSD_TYPE_KEY, "info");
		store.setDefault(TurtlePreferenceConstants.VALIDATION_DUPLICATE_SUBJECT_KEY, "info");
		store.setDefault(TurtlePreferenceConstants.VALIDATION_NO_LINKINGERROR_URIPREFIX, getListPreference("http://dbpedia.org/resource/"));

		//content assist
		store.setDefault(TurtlePreferenceConstants.CA_LANGUAGES_KEY, 
				getListPreference("en","zh","hi","es","fr","ar","ru","pt","bn","de","ja","ko"));
	}

	private String getDefaultDescriptionUris() {
		return getListPreference(
		"http://www.w3.org/2004/02/skos/core#definition",
		"http://www.w3.org/2000/01/rdf-schema#comment",
		"http://purl.org/dc/terms/description",
		"http://purl.org/dc/elements/1.1/description",
		"http://www.w3.org/2004/02/skos/core#note",
		"http://www.w3.org/2004/02/skos/core#editorialNote");
	}

	private String getDefaultLabelUris() {
		return getListPreference(
		"http://www.w3.org/2004/02/skos/core#prefLabel",
		"http://purl.org/dc/elements/1.1/title",
		"http://purl.org/dc/terms/title",
		"http://swrc.ontoware.org/ontology#title",
		"http://xmlns.com/foaf/0.1/name",
		"http://usefulinc.com/ns/doap#name",
		"http://rdfs.org/sioc/ns#name",
		"http://www.holygoat.co.uk/owl/redwood/0.1/tags/name",
		"http://linkedgeodata.org/vocabulary#name",
		"http://www.geonames.org/ontology#name",
		"http://www.geneontology.org/dtds/go.dtd#name",
		"http://www.w3.org/2000/01/rdf-schema#label",
		"http://xmlns.com/foaf/0.1/accountName",
		"http://xmlns.com/foaf/0.1/nick",
		"http://xmlns.com/foaf/0.1/surname",
		"http://www.w3.org/2004/02/skos/core#altLabel");
	}

	private String getListPreference(String... elements){
		return Joiner.on('\n').join(elements);
	}

}
