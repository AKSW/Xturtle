/*******************************************************************************
 * Copyright (c) 2015 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle.ui.preferences;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.swt.widgets.Composite;

import com.google.common.base.Joiner;

//TODO change in the list does not yet cause rebuildNeeded to be true
class TurtleListEditor extends ListEditor {

	private String separator="\n";
	private boolean smartProposeValue=true;
	private boolean sortList=true;
	private String dialogLabel;
	private String dialogDescription;
	private String dialogDefaultValue;

	public TurtleListEditor() {
		super();
	}

	public TurtleListEditor(String descriptionPreferenceKey, String string, Composite fieldEditorParent) {
		super(descriptionPreferenceKey, string, fieldEditorParent);
	}

	public void setNewInputData(String label, String description, String defaultValue){
		this.dialogLabel=label;
		this.dialogDescription=description;
		this.dialogDefaultValue=defaultValue;
	}
	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public void setSmartProposeValue(boolean smartProposeValue) {
		this.smartProposeValue = smartProposeValue;
	}

	public void setSortList(boolean sortList) {
		this.sortList = sortList;
	}

	@Override
	protected String createList(String[] items) {
		return Joiner.on(separator).join(items);
	}

	private String getSelectedItem(){
		int index= getList().getSelectionIndex();
		if(index>=0){
			return getList().getItems()[index];
		} else{
			return null;
		}
	}

	@Override
	protected String getNewInputObject() {
		final AtomicBoolean closedWithEscapeKey=new AtomicBoolean(false);
		String proposeValue=dialogDefaultValue;
		if(smartProposeValue){
			String currentSelection=getSelectedItem();
			if(currentSelection!=null){
				proposeValue=currentSelection;
			}
		}
		final InputDialog input=new InputDialog(getShell(), dialogLabel, dialogDescription, proposeValue, null){
			@Override
			protected boolean canHandleShellCloseEvent() {
				closedWithEscapeKey.set(true);
				return super.canHandleShellCloseEvent();
			}
		};
		input.open();
		return closedWithEscapeKey.get()?null:input.getValue();
	}

	@Override
	protected void doFillIntoGrid(Composite parent, int numColumns) {
		super.doFillIntoGrid(parent, numColumns);
		hideUpDownButtons();
	}

	@Override
	protected void createControl(Composite parent) {
		super.createControl(parent);
		hideUpDownButtons();
	}

	private void hideUpDownButtons() {
		getUpButton().setVisible(false);
		getDownButton().setVisible(false);
	}

	@Override
	protected String[] parseString(String stringList) {
		String[] list = stringList.split(separator);
		if(sortList){
			Arrays.sort(list);
		}
		return list;
	}

	@Override
	public void store() {
		super.store();
	}
}