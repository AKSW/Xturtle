/*******************************************************************************
 * Copyright (c) 2013 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle.ui.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.xtext.ui.editor.preferences.IPreferenceStoreAccess;

import com.google.inject.Inject;

public class TurtleFoldingPreferencePage extends FieldEditorPreferencePage  implements IWorkbenchPreferencePage{

	@Inject
	public TurtleFoldingPreferencePage(IPreferenceStoreAccess access) {
		super();
		setPreferenceStore(access.getWritablePreferenceStore());
		setDescription("Choose which type of sections will be folded by default on editor start.");
	}

	@Override
	protected void createFieldEditors() {
		addField(new BooleanFieldEditor(TurtlePreferenceConstants.FOLD_DIRECTIVES_KEY, "directives", getFieldEditorParent()));
		addField(new BooleanFieldEditor(TurtlePreferenceConstants.FOLD_STRINGS_KEY, "multi-line strings", getFieldEditorParent()));
		addField(new BooleanFieldEditor(TurtlePreferenceConstants.FOLD_TRIPLES_KEY, "triple definitions", getFieldEditorParent()));
		addField(new BooleanFieldEditor(TurtlePreferenceConstants.FOLD_BLANK_COLL, "blank collection", getFieldEditorParent()));
		addField(new BooleanFieldEditor(TurtlePreferenceConstants.FOLD_BLANK_OBJ, "blank objects", getFieldEditorParent()));
	}
	public void init(IWorkbench workbench) {}
}
