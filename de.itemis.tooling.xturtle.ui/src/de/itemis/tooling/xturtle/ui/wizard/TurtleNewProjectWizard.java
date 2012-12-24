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
	/**
	 * Use this method to add pages to the wizard.
	 * The one-time generated version of this class will add a default new project page to the wizard.
	 */
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

	/**
	 * Use this method to read the project settings from the wizard pages and feed them into the project info class.
	 */
	@Override
	protected IProjectInfo getProjectInfo() {
		de.itemis.tooling.xturtle.ui.wizard.XturtleProjectInfo projectInfo = new de.itemis.tooling.xturtle.ui.wizard.XturtleProjectInfo();
		projectInfo.setProjectName(mainPage.getProjectName());
		projectInfo.setReferenceProjects(refProjectPage.getReferencedProjects());
		return projectInfo;
	}

}
