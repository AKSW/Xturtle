/*******************************************************************************
 * Copyright (c) 2013 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle.ui.preferences;

import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.google.inject.Inject;

public class TurtleValidationPreferencePage extends FieldEditorPreferencePage  implements IWorkbenchPreferencePage{

	@Inject
	public TurtleValidationPreferencePage(IPreferenceStore preferenceStore) {
		super();
		setPreferenceStore(preferenceStore);
//		setDescription("Choose the error levels for ");
	}

	private static final String[][] resourceErrors=new String[][]{
		{"no validation","null"},{"Warning","warn"},{"Error","error"}
	};

	private static final String[][] otherErrors=new String[][]{
		{"no validation","null"},{"Info","info"},{"Warning","warn"},{"Error","error"}
	};

	@Override
	protected void createFieldEditors() {
		addField(new ComboFieldEditor(TurtlePreferenceConstants.VALIDATION_UNRESOLVED_URI_KEY, "unkown URI",resourceErrors, getFieldEditorParent()));
		addField(new ComboFieldEditor(TurtlePreferenceConstants.VALIDATION_UNRESOLVED_QNAME_KEY, "unkown QName",resourceErrors, getFieldEditorParent()));
		addField(new ComboFieldEditor(TurtlePreferenceConstants.VALIDATION_PREFIX_MISMATCH_KEY, "prefix does not match URI",otherErrors, getFieldEditorParent()));
		addField(new ComboFieldEditor(TurtlePreferenceConstants.VALIDATION_NS_MISMATCH_KEY, "URI does not match prefix",otherErrors, getFieldEditorParent()));
		addField(new ComboFieldEditor(TurtlePreferenceConstants.VALIDATION_UNUSED_PREFIX_KEY, "unused prefix",otherErrors, getFieldEditorParent()));
		addField(new ComboFieldEditor(TurtlePreferenceConstants.VALIDATION_XSD_TYPE_KEY, "XSD literal types",otherErrors, getFieldEditorParent()));
		addField(new ComboFieldEditor(TurtlePreferenceConstants.VALIDATION_DUPLICATE_SUBJECT_KEY, "duplicate subject",otherErrors, getFieldEditorParent()));
		TurtleListEditor listEditor=new TurtleListEditor(TurtlePreferenceConstants.VALIDATION_NO_LINKINGERROR_URIPREFIX,"no linking validation for URI with prefix", getFieldEditorParent());
		listEditor.setNewInputData("URI", "Enter the URI", "http://");
		addField(listEditor);
		
	}
	public void init(IWorkbench workbench) {}
}
