/*******************************************************************************
 * Copyright (c) 2015 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle.ui.preferences;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

import com.google.common.collect.ImmutableList;

import de.itemis.tooling.xturtle.validation.TurtleNoLinkingValidationUriPrefixes;

public class TurtlePreferenceBasedNoLinkingValidationUriPrefixes implements
		TurtleNoLinkingValidationUriPrefixes, IPropertyChangeListener {

	IPreferenceStore prefernces;
	List<String> uriPrefixesToIgnore;

	@Inject
	public TurtlePreferenceBasedNoLinkingValidationUriPrefixes(IPreferenceStore store) {
		prefernces=store;
		prefernces.addPropertyChangeListener(this);
		initValues();
	}

	public void propertyChange(PropertyChangeEvent event) {
		if(event.getProperty().contains(".validation.")){
			initValues();
		}
	}

	private void initValues() {
		String[] elements = prefernces.getString(TurtlePreferenceConstants.VALIDATION_NO_LINKINGERROR_URIPREFIX).split("\n");
		uriPrefixesToIgnore=ImmutableList.copyOf(elements);
	}

	@Override
	public List<String> getPrefixes() {
		return uriPrefixesToIgnore;
	}
}
