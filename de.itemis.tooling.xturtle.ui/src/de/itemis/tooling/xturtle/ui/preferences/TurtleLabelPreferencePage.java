package de.itemis.tooling.xturtle.ui.preferences;

import java.util.Arrays;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.google.common.base.Joiner;
import com.google.inject.Inject;

public class TurtleLabelPreferencePage extends FieldEditorPreferencePage  implements IWorkbenchPreferencePage{

	@Inject
	public TurtleLabelPreferencePage(IPreferenceStore preferenceStore) {
		super();
		setPreferenceStore(preferenceStore);
		setDescription("Manage the label predicates here. These URIs are used for retrieving aliases that are used in the code completion of URI-simple-names within the given namespace.");
	}

	@Override
	protected void createFieldEditors() {
		ListEditor listeditor = new ListEditor(TurtlePreferenceConstants.LABEL_PREFERENCE_KEY,"", getFieldEditorParent()) {
			@Override
			protected String[] parseString(String stringList) {
				String[] list = stringList.split("\n");
				Arrays.sort(list);
				return list;
			}
			@Override
			protected String getNewInputObject() {
				InputDialog input=new InputDialog(getShell(), "URI", "Enter the URI", "http://", null);
				input.open();
				return input.getValue();
			}
			@Override
			protected String createList(String[] items) {
				return Joiner.on("\n").join(items);
			}
			@Override
			protected void createControl(Composite parent) {
				super.createControl(parent);
				getUpButton().setVisible(false);
				getDownButton().setVisible(false);
			}
		};
		addField(listeditor);
	}
	public void init(IWorkbench workbench) {}
}
