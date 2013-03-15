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
	}
	public void init(IWorkbench workbench) {}
}
