/*******************************************************************************
 * Copyright (c) 2013 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle.ui.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.google.inject.Inject;

public class TurtleLanguagesPreferencePage extends FieldEditorPreferencePage  implements IWorkbenchPreferencePage{

	@Inject
	public TurtleLanguagesPreferencePage(IPreferenceStore preferenceStore) {
		super();
		setPreferenceStore(preferenceStore);
		setDescription("Manage the languages proposed for string literals here.");
	}

	@Override
	protected void createFieldEditors() {
		TurtleListEditor listEditor=new TurtleListEditor(TurtlePreferenceConstants.CA_LANGUAGES_KEY,"", getFieldEditorParent());
		listEditor.setNewInputData("Language", "Enter the language to add.", "");
		addField(listEditor);
	}
	public void init(IWorkbench workbench) {}
}
