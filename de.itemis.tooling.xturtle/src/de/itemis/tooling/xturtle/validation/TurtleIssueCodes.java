/*******************************************************************************
 * Copyright (c) 2015 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle.validation;

import java.util.Map;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

import javax.inject.Singleton;

import org.eclipse.xtext.diagnostics.Severity;
import org.eclipse.xtext.preferences.PreferenceKey;
import org.eclipse.xtext.validation.ConfigurableIssueCodesProvider;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;

@SuppressWarnings("restriction")
@Singleton
public class TurtleIssueCodes extends ConfigurableIssueCodesProvider{

	private static final String LANGUAGE_PREFIX="de.itemis.tooling.xturtle.Xturtle";

	//validation
	public static final String VALIDATION_UNRESOLVED_URI_KEY = LANGUAGE_PREFIX+".validation.unresolvedUri";
	public static final String VALIDATION_UNRESOLVED_QNAME_KEY = LANGUAGE_PREFIX+".validation.unresolvedQname";
	public static final String VALIDATION_PREFIX_MISMATCH_KEY = LANGUAGE_PREFIX+".validation.prefixMismatch";
	public static final String VALIDATION_NS_MISMATCH_KEY = LANGUAGE_PREFIX+".validation.namespaceMismatch";
	public static final String VALIDATION_UNUSED_PREFIX_KEY = LANGUAGE_PREFIX+".validation.unusedPrefix";
	public static final String VALIDATION_XSD_TYPE_KEY = LANGUAGE_PREFIX+".validation.xsdType";
	public static final String VALIDATION_DUPLICATE_SUBJECT_KEY = LANGUAGE_PREFIX+".validation.duplicateSubject";
	public static final String VALIDATION_NO_LINKINGERROR_URIPREFIX = LANGUAGE_PREFIX+".validation.noLinkingErrorUriprefix";

	static final PreferenceKey NO_LINKINGERROR_URIPREFIX_KEY=new PreferenceKey(VALIDATION_NO_LINKINGERROR_URIPREFIX, Joiner.on("\n").join("http://dbpedia.org/resource/","http://example.com/"));

	private Map<String, PreferenceKey> issueCodes;

	public TurtleIssueCodes() {
		issueCodes=ImmutableMap.<String, PreferenceKey>builder()
				.put(create(VALIDATION_NS_MISMATCH_KEY, Severity.WARNING))
				.put(create(VALIDATION_PREFIX_MISMATCH_KEY, Severity.WARNING))
				.put(create(VALIDATION_UNRESOLVED_QNAME_KEY, Severity.ERROR))
				.put(create(VALIDATION_UNRESOLVED_URI_KEY, Severity.IGNORE))
				.put(create(VALIDATION_UNUSED_PREFIX_KEY, Severity.INFO))
				.put(create(VALIDATION_XSD_TYPE_KEY, Severity.INFO))
				.put(create(VALIDATION_DUPLICATE_SUBJECT_KEY, Severity.INFO))
				.put(NO_LINKINGERROR_URIPREFIX_KEY.getId(), NO_LINKINGERROR_URIPREFIX_KEY)
			.build();
	}

	private Entry<String, PreferenceKey>create(String id, Severity defaultValue){
		return new SimpleEntry<String, PreferenceKey>(id, new PreferenceKey(id, defaultValue.name().toLowerCase()));
	}

	@Override
	public Map<String, PreferenceKey> getConfigurableIssueCodes() {
		return issueCodes;
	}
}
