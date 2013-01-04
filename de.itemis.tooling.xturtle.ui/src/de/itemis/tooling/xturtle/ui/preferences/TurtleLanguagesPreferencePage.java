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

public class TurtleLanguagesPreferencePage extends FieldEditorPreferencePage  implements IWorkbenchPreferencePage{

	@Inject
	public TurtleLanguagesPreferencePage(IPreferenceStore preferenceStore) {
		super();
		setPreferenceStore(preferenceStore);
		setDescription("Manage the languages proposed for string literals here.");
	}

	@Override
	protected void createFieldEditors() {
		ListEditor listeditor = new ListEditor(TurtlePreferenceConstants.CA_LANGUAGES_KEY,"", getFieldEditorParent()) {
			@Override
			protected String[] parseString(String stringList) {
				String[] list = stringList.split(",,");
				Arrays.sort(list);
				return list;
			}
			@Override
			protected String getNewInputObject() {
				InputDialog input=new InputDialog(getShell(), "Language", "Enter the language to add.", "", null);
				input.open();
				return input.getValue();
			}
			@Override
			protected String createList(String[] items) {
				return Joiner.on(",,").join(items);
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
