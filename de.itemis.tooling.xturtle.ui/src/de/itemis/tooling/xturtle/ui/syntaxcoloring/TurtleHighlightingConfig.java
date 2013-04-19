/*******************************************************************************
 * Copyright (c) 2013 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle.ui.syntaxcoloring;

import org.eclipse.swt.SWT;
import org.eclipse.xtext.ui.editor.syntaxcoloring.DefaultHighlightingConfiguration;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfigurationAcceptor;
import org.eclipse.xtext.ui.editor.utils.TextStyle;

public class TurtleHighlightingConfig extends DefaultHighlightingConfiguration {

	public static final String URI_ID = "uri";
	public static final String URI_ID_UNRESOLVABLE = "uriNN";
	public static final String PREFIX_ID = "prefix";
	public static final String CLASS_ID = "class";
	public static final String PROPERTY_ID = "property";

	@Override
	public void configure(IHighlightingConfigurationAcceptor acceptor) {
		super.configure(acceptor);
		acceptor.acceptDefaultHighlighting(URI_ID, "URI", getUriStyle());
		acceptor.acceptDefaultHighlighting(URI_ID_UNRESOLVABLE, "unknown URI", getUnresolvableUriStyle());
		acceptor.acceptDefaultHighlighting(PREFIX_ID, "Prefix", getPrefixStyle());
		acceptor.acceptDefaultHighlighting(PROPERTY_ID, "Property", getPropertyStyle());
		acceptor.acceptDefaultHighlighting(CLASS_ID, "Class", getClassStyle());
	}
	
	public TextStyle getUriStyle(){
		return numberTextStyle().copy();
	}
	public TextStyle getUnresolvableUriStyle(){
		TextStyle style= getUriStyle().copy();
		style.setStyle(SWT.ITALIC);
		return style;
	}
	public TextStyle getPrefixStyle(){
		TextStyle style= defaultTextStyle().copy();
		return style;
	}
	public TextStyle getClassStyle(){
		TextStyle style= defaultTextStyle().copy();
		return style;
	}
	public TextStyle getPropertyStyle(){
		TextStyle style= defaultTextStyle().copy();
		return style;
	}
}
