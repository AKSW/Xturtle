/*******************************************************************************
 * Copyright (c) 2013 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.jface.text.source.projection.ProjectionAnnotationModel;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.xtext.ui.XtextUIMessages;
import org.eclipse.xtext.ui.editor.XtextEditor;

import com.google.common.collect.ObjectArrays;

import de.itemis.tooling.xturtle.ui.folding.TurtleFoldingRegionProvider.TypedFoldedRegion;
import de.itemis.tooling.xturtle.ui.preferences.TurtlePreferenceConstants;
import de.itemis.tooling.xturtle.xturtle.XturtlePackage;

public class TurtleXtextEditor extends XtextEditor{

	@Inject
	private IPreferenceStore preferencStore; 

	//need to override as the FoldingActionGroup is not used for populating the menu
	//(although it has a fillMenu-method)
	@Override
	protected void rulerContextMenuAboutToShow(IMenuManager menu) {
		super.rulerContextMenuAboutToShow(menu);
		//remove the projection menu introduced by super call
		//unfortunately we cannot call super.super.rulerC...
		menu.remove("projection");

		IMenuManager foldingMenu = new MenuManager(XtextUIMessages.Editor_FoldingMenu_name, "projection"); //$NON-NLS-1$
		menu.appendToGroup(ITextEditorActionConstants.GROUP_RULERS, foldingMenu);
		IAction action = getAction("FoldingToggle"); //$NON-NLS-1$
		foldingMenu.add(action);
		action = getAction("FoldingExpandAll"); //$NON-NLS-1$
		foldingMenu.add(action);
		action = getAction("FoldingCollapseAll"); //$NON-NLS-1$
		foldingMenu.add(action);
		action = getAction("FoldingCollapseStrings"); //$NON-NLS-1$
		foldingMenu.add(action);
		action = getAction("FoldingRestore"); //$NON-NLS-1$
		foldingMenu.add(action);
	}

	@Override
	protected void installFoldingSupport(ProjectionViewer projectionViewer) {
		super.installFoldingSupport(projectionViewer);
		ProjectionAnnotationModel model = projectionViewer.getProjectionAnnotationModel();
		foldRegionsOnStartup(model);
	}

	private void foldRegionsOnStartup(ProjectionAnnotationModel model){
		//TODO retrieve set of types to fold from helper, as other types might be added
		Set<EClass> typesToFold=new HashSet<EClass>();
		if(preferencStore.getBoolean(TurtlePreferenceConstants.FOLD_TRIPLES_KEY)){
			typesToFold.add(XturtlePackage.Literals.TRIPLES);
		}
		if(preferencStore.getBoolean(TurtlePreferenceConstants.FOLD_STRINGS_KEY)){
			typesToFold.add(XturtlePackage.Literals.STRING_LITERAL);
		}
		if(preferencStore.getBoolean(TurtlePreferenceConstants.FOLD_DIRECTIVES_KEY)){
			typesToFold.add(XturtlePackage.Literals.DIRECTIVES);
		}
		if(preferencStore.getBoolean(TurtlePreferenceConstants.FOLD_BLANK_COLL)){
			typesToFold.add(XturtlePackage.Literals.BLANK_COLLECTION);
		}
		if(preferencStore.getBoolean(TurtlePreferenceConstants.FOLD_BLANK_OBJ)){
			typesToFold.add(XturtlePackage.Literals.BLANK_OBJECTS);
		}
		if(!typesToFold.isEmpty()){
			List<Annotation> changes=new ArrayList<Annotation>(); 
			Iterator<?> iterator = model.getAnnotationIterator();
			while (iterator.hasNext()){
				Object next = iterator.next();
				if(next instanceof ProjectionAnnotation){
					ProjectionAnnotation pa = (ProjectionAnnotation) next;
					Position position = model.getPosition(pa);
					if(position instanceof TypedFoldedRegion &&typesToFold.contains(((TypedFoldedRegion) position).getType())){
						pa.markCollapsed();
						changes.add(pa);
					}
				}
			}
			model.modifyAnnotations(null,null, changes.toArray(new Annotation[0]));
		}
	}

	@Override
	protected String[] collectContextMenuPreferencePages() {
		String[] superPages = super.collectContextMenuPreferencePages();
		String[] xturtlePages = new String[] {
				getLanguageName() + ".indexing.Labels",
				getLanguageName() + ".indexing.Descriptions",
				getLanguageName() + ".Folding",
				getLanguageName() + ".Validation",
				getLanguageName() + ".LanguageContentAssist"
				};
		return ObjectArrays.concat(superPages, xturtlePages, String.class);
	}
}
