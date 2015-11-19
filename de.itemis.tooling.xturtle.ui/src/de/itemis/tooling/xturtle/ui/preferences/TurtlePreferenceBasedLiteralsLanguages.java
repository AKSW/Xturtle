/*******************************************************************************
 * Copyright (c) 2013 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
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
import org.eclipse.xtext.ui.editor.preferences.IPreferenceStoreAccess;

import com.google.common.collect.ImmutableList;

import de.itemis.tooling.xturtle.ui.contentassist.TurtleLiteralsLanguages;

public class TurtlePreferenceBasedLiteralsLanguages implements
		TurtleLiteralsLanguages, IPropertyChangeListener {

	IPreferenceStore prefernces;
	List<String> languages;

	@Inject
	public TurtlePreferenceBasedLiteralsLanguages(IPreferenceStoreAccess store) {
		prefernces=store.getWritablePreferenceStore();
		prefernces.addPropertyChangeListener(this);
		initValues();
	}


	public void propertyChange(PropertyChangeEvent event) {
		if(event.getProperty().equals(TurtlePreferenceConstants.CA_LANGUAGES_KEY)){
			initValues();
		}
	}

	private void initValues() {
		languages=ImmutableList.copyOf(prefernces.getString(TurtlePreferenceConstants.CA_LANGUAGES_KEY).split("\n"));
	}


	public List<String> getLanguagesToPropose() {
		return languages;
	}
}
