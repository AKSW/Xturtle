/*******************************************************************************
 * Copyright (c) 2013 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle.ui.autoedit;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DefaultIndentLineAutoEditStrategy;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextUtilities;

public class TurtleNewLineAutoedit extends DefaultIndentLineAutoEditStrategy {

	//if "enter" follows a after "." do not keep the indentation
	public void customizeDocumentCommand(IDocument d, DocumentCommand c) {
		if (c.length == 0 && c.text != null && TextUtilities.endsWith(d.getLegalLineDelimiters(), c.text) != -1){
			try {
				int offset = c.offset;
				int line = d.getLineOfOffset(offset);
				int lineOffset=d.getLineOffset(line);
				String lineString=d.get(lineOffset, offset-lineOffset).trim();
				if(lineString.length()>0 && lineString.charAt(lineString.length()-1)=='.'){
					return;
				}
			} catch (BadLocationException e) {
				//don'care
			}
			super.customizeDocumentCommand(d, c);
		}
	}
}
