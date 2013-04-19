/*******************************************************************************
 * Copyright (c) 2013 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle.ui.syntaxcoloring;

import java.util.regex.Pattern;

import org.eclipse.xtext.ui.editor.syntaxcoloring.DefaultAntlrTokenToAttributeIdMapper;

import com.google.inject.Singleton;

@Singleton
public class TurtleHighlightingMapper extends
		DefaultAntlrTokenToAttributeIdMapper {

private static final Pattern QUOTED = Pattern.compile("(?:^'([^']*)'$)|(?:^\"([^\"]*)\")$", Pattern.MULTILINE);
	
	private static final Pattern PUNCTUATION = Pattern.compile("\\p{Punct}*");
	
	@Override
	protected String calculateId(String tokenName, int tokenType) {
		if(PUNCTUATION.matcher(tokenName).matches()) {
			return TurtleHighlightingConfig.PUNCTUATION_ID;
		}
		if(QUOTED.matcher(tokenName).matches()||"RULE_AT".equals(tokenName)) {
			return TurtleHighlightingConfig.KEYWORD_ID;
		}
		if("RULE_STRING".equals(tokenName)) {
			return TurtleHighlightingConfig.STRING_ID;
		}
		if("RULE_NUMBER".equals(tokenName)) {
			return TurtleHighlightingConfig.NUMBER_ID;
		}
		if("RULE_SL_COMMENT".equals(tokenName)) {
			return TurtleHighlightingConfig.COMMENT_ID;
		}
		if("RULE_URI".equals(tokenName)){
			return TurtleHighlightingConfig.URI_ID;
		}
		return TurtleHighlightingConfig.DEFAULT_ID;
	}
}
