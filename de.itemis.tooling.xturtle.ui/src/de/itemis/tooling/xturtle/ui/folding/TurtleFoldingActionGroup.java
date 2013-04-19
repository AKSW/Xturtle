/*******************************************************************************
 * Copyright (c) 2013 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle.ui.folding;

import java.util.Iterator;
import java.util.ResourceBundle;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.jface.text.source.projection.ProjectionAnnotationModel;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.IUpdate;
import org.eclipse.ui.texteditor.ResourceAction;
import org.eclipse.xtext.ui.editor.folding.FoldingActionGroup;

import de.itemis.tooling.xturtle.ui.folding.TurtleFoldingRegionProvider.TypedFoldedRegion;
import de.itemis.tooling.xturtle.xturtle.XturtlePackage;

class TurtleFoldingActionGroup extends FoldingActionGroup {

	//inner classes copied from super
	private static abstract class PreferenceAction extends ResourceAction implements IUpdate {
		PreferenceAction(ResourceBundle bundle, String prefix, int style) {
			super(bundle, prefix, style);
		}
	}
	private class FoldingAction extends PreferenceAction {

		FoldingAction(ResourceBundle bundle, String prefix) {
			super(bundle, prefix, IAction.AS_PUSH_BUTTON);
		}
		public void update() {
			setEnabled(TurtleFoldingActionGroup.this.isEnabled() && viewwer.isProjectionMode());
		}
	}
	
	private ProjectionViewer viewwer;
	private FoldingAction collapseStrings;

	TurtleFoldingActionGroup(final ITextEditor editor, ITextViewer viewer) {
		super(editor, viewer);
		if(!(viewer instanceof ProjectionViewer)){
			return;
		}
		this.viewwer=(ProjectionViewer) viewer;
		
		collapseStrings= new FoldingAction(TurtleFoldingMessages.getResourceBundle(), "Projection.CollapseStrings.") { //$NON-NLS-1$
			private final EClass type=XturtlePackage.Literals.STRING_LITERAL;
			public void run() {
				ProjectionAnnotationModel model = viewwer.getProjectionAnnotationModel();
				Iterator<?> iterator = model.getAnnotationIterator();
				while (iterator.hasNext()){
					Object next = iterator.next();
					if(next instanceof ProjectionAnnotation){
						ProjectionAnnotation pa = (ProjectionAnnotation) next;
						//foldable regions for strings have been marked by using the TextFoldedRegion class
						//there may indeed be better ways...
						Position position = model.getPosition(pa);
						if(position instanceof TypedFoldedRegion && type==((TypedFoldedRegion) position).getType()){
							model.collapse(pa);
						}
					}
				}
			}
		};
		collapseStrings.setActionDefinitionId("org.xtext.example.folding.ui.folding.collapseStrings");
		editor.setAction("FoldingCollapseStrings", collapseStrings); //$NON-NLS-1$

	}

	@Override
	protected void update() {
		super.update();
		if(isEnabled()){
			collapseStrings.update();
		}
	}
}
