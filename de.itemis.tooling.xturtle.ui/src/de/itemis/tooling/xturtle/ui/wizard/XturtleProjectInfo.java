package de.itemis.tooling.xturtle.ui.wizard;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.xtext.ui.wizard.DefaultProjectInfo;

public class XturtleProjectInfo extends DefaultProjectInfo {

	private List<IProject> projects;

	public List<IProject> getReferencedProjects() {
		return projects;
	}

	public void setReferenceProjects(List<IProject> referencedProjects) {
		projects=referencedProjects;
	}
}
