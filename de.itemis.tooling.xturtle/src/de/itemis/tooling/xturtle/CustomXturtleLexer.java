/*******************************************************************************
 * Copyright (c) 2015 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle;

import org.antlr.runtime.CharStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;

import de.itemis.tooling.xturtle.parser.antlr.internal.InternalXturtleLexer;

public class CustomXturtleLexer extends InternalXturtleLexer {

	LexerOverrider overrider = new LexerOverrider(CustomXturtleLexer.class);

	public CustomXturtleLexer() {
		super();
	}

	public CustomXturtleLexer(CharStream input) {
		this(input, new RecognizerSharedState());
	}

	public CustomXturtleLexer(CharStream input, RecognizerSharedState state) {
		super(input, state);

	}

	@Override
	public void mTokens() throws RecognitionException {
		if (overrider.override(input, state)) {
			// done
		} else {
			super.mTokens();
		}
	}
}
