/*******************************************************************************
 * Copyright (c) 2013 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle.ui.wizard;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class TurtleReferencedProjectsSelectionPage extends WizardPage {

	private Button[] checkBoxButtons;
	private IProject[] projects;

	protected TurtleReferencedProjectsSelectionPage(String pageName) {
		super(pageName);
		IWorkspaceRoot workspaceroot = ResourcesPlugin.getWorkspace().getRoot();
		projects = workspaceroot.getProjects();
		checkBoxButtons = new Button[projects.length];
		setPageComplete(true);
	}

	public List<IProject> getReferencedProjects() {
		List<IProject> result = new ArrayList<IProject>();
		for (Button button : checkBoxButtons) {
			if (button.getSelection()) {
				result.add((IProject) button.getData());
			}
		}
		return result;
	}

	public void createControl(final Composite parent) {
		initializeDialogUnits(parent);

		final ScrolledComposite sc = new ScrolledComposite(parent, SWT.V_SCROLL);
		final Composite composite = new Composite(sc, SWT.NONE);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		sc.setContent(composite);
		sc.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				Rectangle r = sc.getClientArea();
				sc.setMinSize(composite.computeSize(r.width, SWT.DEFAULT));
			}
		});
		createCheckboxes(composite);
		sc.setExpandVertical(true);
		sc.setExpandHorizontal(true);
		setControl(sc);
		parent.pack();

		setErrorMessage(null);
		setMessage(null);
		Dialog.applyDialogFont(composite);

	}

	private void createCheckboxes(final Composite parent) {
		Font font = parent.getFont();
		for (int i = 0; i < projects.length; i++) {
			Button checkBox = new Button(parent, SWT.CHECK | SWT.LEFT);
			checkBoxButtons[i] = checkBox;
			checkBox.setText(projects[i].getName());
			checkBox.setData(projects[i]);
			checkBox.setFont(font);
		}
	}
}
