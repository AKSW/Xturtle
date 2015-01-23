package de.itemis.tooling.xturtle;

import org.antlr.runtime.RecognitionException;

import de.itemis.tooling.xturtle.parser.antlr.internal.InternalXturtleLexer;

public class CustomXturtleLexer extends InternalXturtleLexer {

	private boolean overrideTripleEndGeneration() {
		if (input.LA(1) == '.') {
			switch (input.LA(2)) {
			case -1://EOF
			case '\n':
			case ' ':
			case '\t':
			case '\r':
			case '#':
				input.consume();
				state.type = RULE_TRIPELEND;
				state.channel = DEFAULT_TOKEN_CHANNEL;
				state.failed=false;
				state.backtracking=0;
				return true;
			default:
				break;
			}
		}
		return false;
	}

	@Override
	public void mTokens() throws RecognitionException {
		if(!overrideTripleEndGeneration()){
			super.mTokens();
		}
	}
}
