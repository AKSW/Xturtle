package de.itemis.tooling.xturtle.ui.contentassist;

import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;

import de.itemis.tooling.xturtle.LexerOverrider;
import de.itemis.tooling.xturtle.ui.contentassist.antlr.internal.InternalXturtleLexer;

public class CustomXturtleContentassistLexer extends InternalXturtleLexer {

	LexerOverrider overrider=new LexerOverrider(CustomXturtleContentassistLexer.class);

	@Override
	public Token nextToken() {
		return super.nextToken();
	}

	@Override
	public void mTokens() throws RecognitionException {
		if(overrider.override(input, state)){
			//done
		}else{
			super.mTokens();
		}
	}
}
