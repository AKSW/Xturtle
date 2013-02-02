package de.itemis.tooling.xturtle.ui.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.google.inject.Inject;

public class TurtleFoldingPreferencePage extends FieldEditorPreferencePage  implements IWorkbenchPreferencePage{

	@Inject
	public TurtleFoldingPreferencePage(IPreferenceStore preferenceStore) {
		super();
		setPreferenceStore(preferenceStore);
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
