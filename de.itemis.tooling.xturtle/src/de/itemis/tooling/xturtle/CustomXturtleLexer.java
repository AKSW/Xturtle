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

	boolean isDigit(int c){
		return c>='0' &&c<='9';
	}

	private void consumeExponent(){
		int e=input.LA(1);
		int signOrDigit=input.LA(2);
		if(e=='e' || e=='E'){
			if(signOrDigit=='+'|| signOrDigit=='-'){
				if(isDigit(input.LA(3))){
					input.consume();//e
					input.consume();//sign
					consumeDigits();
				}
			}else if(isDigit(signOrDigit)){
				input.consume();//e
				consumeDigits();
			}
		}
	}

	private void consumeDigits(){
		while(isDigit(input.LA(1))){
			input.consume();
		}
	}

	private boolean overrideNumberGeneration(){

		int nextChar=input.LA(1);
		int nextChar2=input.LA(2);
		boolean result=false;

		if(isDigit(nextChar)){
			result=true;
			consumeDigits();
			if(input.LA(1)=='.'){
				input.consume();
				consumeDigits();
			}
		}else if(nextChar=='.' && isDigit(nextChar2)){
			result=true;
			input.consume();
			consumeDigits();
		}

		if(result){
			consumeExponent();
			state.type = RULE_NUMBER;
			state.channel = DEFAULT_TOKEN_CHANNEL;
			state.failed=false;
			state.backtracking=0;
		}
		return result;
	}

	private boolean endOfStringFound(int quoteCharacter, int length){
		boolean result=true;
		for(int i=0;i<length;i++){
			result&=input.LA(i+1)==quoteCharacter;
		}

		return result;
	}

	private void consumeString(int quoteCharacter){
		boolean isLong=input.LA(1)==quoteCharacter && input.LA(2)==quoteCharacter && input.LA(3)==quoteCharacter;
		int numberOfExpectedEndQuotes;
		boolean endReached=false;
		if(isLong){
			numberOfExpectedEndQuotes=3;
		}else {
			numberOfExpectedEndQuotes=1;
		}
		for(int i=0;i<numberOfExpectedEndQuotes;i++){
			//start quotes
			input.consume();
		}
		while(!endReached){
			if(input.LA(1)=='\\'){
				input.consume();
				input.consume();
			}else if(endOfStringFound(quoteCharacter, numberOfExpectedEndQuotes)){
				for(int i=0;i<numberOfExpectedEndQuotes;i++){
					//start quotes
					input.consume();
				}
				endReached=true;
			}else if(input.LA(1)==CharStream.EOF){
				endReached=true;
				state.failed=true;
			}else{
				input.consume();
			}
		}
	}

	private boolean overrideString(){
		int start=input.LA(1);
		boolean isString=start=='"'||start=='\'';
		if(isString){
			state.type = RULE_STRING;
			state.channel = DEFAULT_TOKEN_CHANNEL;
			state.failed=false;
			state.backtracking=0;
			consumeString(start);
		}
		return isString;
	}
	private boolean customOverride(){
		return overrideTripleEndGeneration() 
			|| overrideNumberGeneration()
			|| overrideString();
	}
	@Override
	public void mTokens() throws RecognitionException {
		if(customOverride()){
			//done
		}else{
			super.mTokens();
		}
	}
}
