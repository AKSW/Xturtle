/*******************************************************************************
 * Copyright (c) 2013 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle.ui.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.xtext.ui.editor.preferences.IPreferenceStoreAccess;

import com.google.inject.Inject;

public class TurtleLabelPreferencePage extends FieldEditorPreferencePage  implements IWorkbenchPreferencePage{

	@Inject
	public TurtleLabelPreferencePage(IPreferenceStoreAccess access) {
		super();
		setPreferenceStore(access.getWritablePreferenceStore());
		setDescription("Manage the label predicates here. These URIs are used for retrieving aliases that are used in the code completion of URI-simple-names within the given namespace.");
	}

	@Override
	protected void createFieldEditors() {
		TurtleListEditor listEditor=new TurtleListEditor(TurtlePreferenceConstants.LABEL_PREFERENCE_KEY,"", getFieldEditorParent());
		listEditor.setNewInputData("URI", "Enter the URI", "http://");
		addField(listEditor);
	}
	public void init(IWorkbench workbench) {}
}
