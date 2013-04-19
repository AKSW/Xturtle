/*******************************************************************************
 * Copyright (c) 2013 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle;

/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class XturtleStandaloneSetup extends XturtleStandaloneSetupGenerated{

	public static void doSetup() {
		new XturtleStandaloneSetup().createInjectorAndDoEMFRegistration();
	}
}

