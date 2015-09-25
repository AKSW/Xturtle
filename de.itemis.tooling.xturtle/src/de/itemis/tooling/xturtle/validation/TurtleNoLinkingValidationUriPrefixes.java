/*******************************************************************************
 * Copyright (c) 2015 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle.validation;

import java.util.List;

/**
 * If there is an unresolved reference to an element whose URI starts with one of the
 * prefixes provided, Xturtle will not raise a linking error.
 * */
public interface TurtleNoLinkingValidationUriPrefixes {

	List<String> getPrefixes();
}
