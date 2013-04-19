/*******************************************************************************
 * Copyright (c) 2013 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle.ui.folding;

import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.actions.IActionContributor;

//hook for using our own action group, where we want to have a string collapse action
/**
 * adapted from {@link org.eclipse.xtext.ui.editor.folding.FoldingActionContributor}
 * */
public class TurtleFoldingActionContributor implements IActionContributor {

	private TurtleFoldingActionGroup foldingActionGroup;

	public void contributeActions(XtextEditor editor) {
		foldingActionGroup = new TurtleFoldingActionGroup(editor, editor.getInternalSourceViewer());
	}

	public void editorDisposed(XtextEditor editor) {
		if(foldingActionGroup != null)
			foldingActionGroup.dispose();
	}
}
