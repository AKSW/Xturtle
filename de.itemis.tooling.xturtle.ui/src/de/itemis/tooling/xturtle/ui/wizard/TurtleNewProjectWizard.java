/*******************************************************************************
 * Copyright (c) 2013 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle.ui.wizard;

import javax.inject.Inject;

import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.xtext.ui.wizard.IProjectCreator;
import org.eclipse.xtext.ui.wizard.IProjectInfo;
import org.eclipse.xtext.ui.wizard.XtextNewProjectWizard;

public class TurtleNewProjectWizard extends XtextNewProjectWizard {

	private TurtleReferencedProjectsSelectionPage refProjectPage;
	private WizardNewProjectCreationPage mainPage;

	@Inject
	public TurtleNewProjectWizard(IProjectCreator projectCreator) {
		super(projectCreator);
		setWindowTitle("New Xturtle Project");
	}

	public void addPages() {
		mainPage = new WizardNewProjectCreationPage("basicNewProjectPage");
		mainPage.setTitle("Xturtle Project");
		mainPage.setDescription("Create a new Xturtle project. Go to the next page to select projects, whose ttl-files should be visible from the new project.");
		addPage(mainPage);
		refProjectPage=new TurtleReferencedProjectsSelectionPage("refSelection");
		refProjectPage.setTitle("Xturtle Project");
		refProjectPage.setDescription("Choose those projects whose turtle files should be visible from the new project.");
		addPage(refProjectPage);
	}

	@Override
	protected IProjectInfo getProjectInfo() {
		de.itemis.tooling.xturtle.ui.wizard.XturtleProjectInfo projectInfo = new de.itemis.tooling.xturtle.ui.wizard.XturtleProjectInfo();
		projectInfo.setProjectName(mainPage.getProjectName());
		projectInfo.setReferenceProjects(refProjectPage.getReferencedProjects());
		return projectInfo;
	}

}
