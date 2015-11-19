/*******************************************************************************
 * Copyright (c) 2013 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle.ui.preferences;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.xtext.ui.editor.preferences.IPreferenceStoreAccess;

import com.google.inject.Inject;

public class TurtleDescriptionsPreferencePage extends FieldEditorPreferencePage  implements IWorkbenchPreferencePage{

	@Inject
	public TurtleDescriptionsPreferencePage(IPreferenceStoreAccess access) {
		super();
		setPreferenceStore(access.getWritablePreferenceStore());
		setDescription("Manage the description predicates here. These URIs are used for retrieving hover text information for the subject.");
	}

	@Override
	protected void createFieldEditors() {
		TurtleListEditor listEditor=new TurtleListEditor(TurtlePreferenceConstants.DESCRIPTION_PREFERENCE_KEY,"", getFieldEditorParent());
		listEditor.setNewInputData("URI", "Enter the URI", "http://");
		addField(listEditor);
		addField(new BooleanFieldEditor(TurtlePreferenceConstants.USE_DEFAULT_LANGUAGE_PREFERENCE_KEY, "Use description with default locale "+Platform.getNL(), getFieldEditorParent()));
		addField(new BooleanFieldEditor(TurtlePreferenceConstants.USE_NOLANGUAGE_PREFERENCE_KEY, "Use description with no locale", getFieldEditorParent()));
		listEditor=new TurtleListEditor(TurtlePreferenceConstants.LANGUAGES_PREFERENCE_KEY,"Use the following locales", getFieldEditorParent());
		listEditor.setNewInputData("Locale", "Enter a locale", "");
		listEditor.setSeparator(",,");
		listEditor.setSmartProposeValue(false);
		addField(listEditor);
	}
	public void init(IWorkbench workbench) {}
}
