/*******************************************************************************
 * Copyright (c) 2015 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle.ui.preferences;

import java.util.Arrays;

import javax.inject.Inject;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.xtext.ui.editor.preferences.IPreferenceStoreAccess;
import org.eclipse.xtext.ui.preferences.OptionsConfigurationBlock;
import org.eclipse.xtext.ui.validation.AbstractValidatorConfigurationBlock;

import de.itemis.tooling.xturtle.validation.TurtleIssueCodes;

@SuppressWarnings("restriction")
public class TurtleValidatorConfigBlock extends AbstractValidatorConfigurationBlock {

	private static final String[] resourceErrors=new String[]{"ignore","warning","error"};
	private static final String[] resourceErrorLabels=new String[]{"no Validation","Warning","Error"};
	private static final String[] otherErrors=new String[]{"ignore","info","warning","error"};
	private static final String[] otherErrorLabels=new String[]{"no Validation","Info","Warning","Error"};

	TurtleListEditor noLinkingErrorUriPrefixes;
	private IPreferenceStore preferenceStore;

	@Inject
	IPreferenceStoreAccess storeAccess;

	@Override
	protected void fillSettingsPage(Composite composite, int nColumns, int defaultIndent) {

		Composite linkingProblems = createSection("Linking", composite, nColumns);
		addComboBox(linkingProblems, "unknown URI", TurtleIssueCodes.VALIDATION_UNRESOLVED_URI_KEY, defaultIndent, resourceErrors, resourceErrorLabels);
		addComboBox(linkingProblems, "unkown QName", TurtleIssueCodes.VALIDATION_UNRESOLVED_QNAME_KEY, defaultIndent, resourceErrors, resourceErrorLabels);
		noLinkingErrorUriPrefixes=new TurtleListEditor();
		noLinkingErrorUriPrefixes.setPreferenceStore(preferenceStore);
		noLinkingErrorUriPrefixes.setPreferenceName(TurtleIssueCodes.VALIDATION_NO_LINKINGERROR_URIPREFIX);
		noLinkingErrorUriPrefixes.setLabelText("no linking validation for URI with prefix");
		noLinkingErrorUriPrefixes.setNewInputData("URI", "Enter the URI", "http://");
		noLinkingErrorUriPrefixes.fillIntoGrid(linkingProblems, nColumns);
		noLinkingErrorUriPrefixes.load();

		Composite prefixProblems = createSection("Qname Prefixes", composite, nColumns);
		addComboBox(prefixProblems, "prefix does not match URI", TurtleIssueCodes.VALIDATION_PREFIX_MISMATCH_KEY, defaultIndent, otherErrors, otherErrorLabels);
		addComboBox(prefixProblems, "URI does not match prefix", TurtleIssueCodes.VALIDATION_NS_MISMATCH_KEY, defaultIndent, otherErrors, otherErrorLabels);
		addComboBox(prefixProblems, "unused prefix", TurtleIssueCodes.VALIDATION_UNUSED_PREFIX_KEY, defaultIndent, otherErrors, otherErrorLabels);

		Composite otherProblems = createSection("Other", composite, nColumns);
		addComboBox(otherProblems, "XSD literal types", TurtleIssueCodes.VALIDATION_XSD_TYPE_KEY, defaultIndent, otherErrors, otherErrorLabels);
		addComboBox(otherProblems, "duplicate subject", TurtleIssueCodes.VALIDATION_DUPLICATE_SUBJECT_KEY, defaultIndent, otherErrors, otherErrorLabels);
	}

	@Override
	protected Job getBuildJob(IProject project) {
		Job buildJob = new OptionsConfigurationBlock.BuildJob("Xturtle validation", project);
		buildJob.setRule(ResourcesPlugin.getWorkspace().getRuleFactory().buildRule());
		buildJob.setUser(true);
		return buildJob;
	}

	@Override
	protected String[] getFullBuildDialogStrings(boolean workspaceSettings) {
		return new String[] { "Settings changed", "Due to changes in the settings, a full build is required. Build now?" };
	}

	@Override
	protected void validateSettings(String changedKey, String oldValue, String newValue) {}

	@Override
	public boolean hasProjectSpecificOptions(IProject project) {
		return storeAccess.getWritablePreferenceStore(project).getBoolean(IS_PROJECT_SPECIFIC);
	}

	//The following overrides were necessary, because the super class has no simple way to add controls other than combobox etc
	//the listFieldEditor has to be taken into account as well
	@Override
	public void setPreferenceStore(IPreferenceStore preferenceStore) {
		super.setPreferenceStore(preferenceStore);
		this.preferenceStore=preferenceStore;
	}

	@Override
	public void performDefaults() {
		super.performDefaults();
		noLinkingErrorUriPrefixes.loadDefault();
	}

	@Override
	protected void savePreferences() {
		noLinkingErrorUriPrefixes.store();
		super.savePreferences();
	}

	@Override
	protected void collectRegistredKeys() {
		super.collectRegistredKeys();
		this.keys=Arrays.copyOf(this.keys, this.keys.length+1);
		this.keys[this.keys.length-1]=TurtleIssueCodes.VALIDATION_NO_LINKINGERROR_URIPREFIX;
	}
}
