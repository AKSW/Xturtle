/*******************************************************************************
 * Copyright (c) 2013 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
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
