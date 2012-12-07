package de.itemis.tooling.xturtle.ui.preferences;

import java.util.Arrays;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.google.common.base.Joiner;
import com.google.inject.Inject;

public class TurtleDescriptionsPreferencePage extends FieldEditorPreferencePage  implements IWorkbenchPreferencePage{

	@Inject
	public TurtleDescriptionsPreferencePage(IPreferenceStore preferenceStore) {
		super();
		setPreferenceStore(preferenceStore);
		setDescription("Manage the description predicates here. These URIs are used for retrieving hover text information for the subject.");
	}

	@Override
	protected void createFieldEditors() {
		ListEditor listeditor = new ListEditor(TurtlePreferenceConstants.DESCRIPTION_PREFERENCE_KEY,"", getFieldEditorParent()) {
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
		addField(new BooleanFieldEditor(TurtlePreferenceConstants.USE_DEFAULT_LANGUAGE_PREFERENCE_KEY, "Use description with default locale "+Platform.getNL(), getFieldEditorParent()));
		addField(new BooleanFieldEditor(TurtlePreferenceConstants.USE_NOLANGUAGE_PREFERENCE_KEY, "Use description with no locale", getFieldEditorParent()));
		listeditor = new ListEditor(TurtlePreferenceConstants.LANGUAGES_PREFERENCE_KEY,"Use the following locales", getFieldEditorParent()) {
			@Override
			protected String[] parseString(String stringList) {
				return stringList.split(",,");
			}
			@Override
			protected String getNewInputObject() {
				InputDialog input=new InputDialog(getShell(), "Locale", "Enter a locale", "", null);
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
