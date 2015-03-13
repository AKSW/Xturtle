/*******************************************************************************
 * Copyright (c) 2015 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/package de.itemis.tooling.xturtle;

import java.lang.reflect.Field;

import org.antlr.runtime.BaseRecognizer;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.RecognizerSharedState;

public class LexerOverrider {

	private int tripleEndRule;
	private int numberRule;
	private int stringRule;
	private int idRule;
	private static final int DEFAULT_TOKEN_CHANNEL = BaseRecognizer.DEFAULT_TOKEN_CHANNEL;

	private boolean colonAsId=false;

	private RuntimeException getException(String rule, Throwable cause) {
		throw new RuntimeException(
				"cannot determine the index of rule " + rule, cause);
	}

	private int getRuleIndex(Class<?> lexerClass, String ruleName) {
		try {
			Field field = lexerClass.getField(ruleName);
			Object value = field.get(lexerClass);
			return (Integer) value;
		} catch (SecurityException e) {
			throw getException(ruleName, e);
		} catch (NoSuchFieldException e) {
			throw getException(ruleName, e);
		} catch (IllegalArgumentException e) {
			throw getException(ruleName, e);
		} catch (IllegalAccessException e) {
			throw getException(ruleName, e);
		}
	}

	public LexerOverrider(Class<?> lexerClass) {
		this.tripleEndRule = getRuleIndex(lexerClass, "RULE_TRIPELEND");
		this.numberRule = getRuleIndex(lexerClass, "RULE_NUMBER");
		this.stringRule = getRuleIndex(lexerClass, "RULE_STRING");
		this.idRule = getRuleIndex(lexerClass, "RULE_ID");
	}

	private void stateOK(RecognizerSharedState state, int rule) {
		state.type = rule;
		state.channel = DEFAULT_TOKEN_CHANNEL;
		state.failed = false;
		state.backtracking = 0;
	}

	private void resetColonId(){
		colonAsId=false;
	}

	private boolean overrideTripleEndGeneration(CharStream input,
			RecognizerSharedState state) {
		if (input.LA(1) == '.') {
			int next = input.LA(2);
			switch (next) {
			case -1:// EOF
			case '\n':
			case ' ':
			case '\t':
			case '\r':
			case '@':
			case '<':
			case '#':
				input.consume();
				stateOK(state, tripleEndRule);
				resetColonId();
				return true;
			default:
				break;
			}
		}
		return false;
	}

	boolean isDigit(int c) {
		return c >= '0' && c <= '9';
	}

	private void consumeExponent(CharStream input) {
		int e = input.LA(1);
		int signOrDigit = input.LA(2);
		if (e == 'e' || e == 'E') {
			if (signOrDigit == '+' || signOrDigit == '-') {
				if (isDigit(input.LA(3))) {
					input.consume();// e
					input.consume();// sign
					consumeDigits(input);
				}
			} else if (isDigit(signOrDigit)) {
				input.consume();// e
				consumeDigits(input);
			}
		}
	}

	private void consumeDigits(CharStream input) {
		while (isDigit(input.LA(1))) {
			input.consume();
		}
	}

	private boolean isNumberContinuation(int c1, int c2, int c3){
		if(isDigit(c1)){
			return true;
		} else if(c1=='e'|| c1=='E'){
			if(isDigit(c2)){
				return true;
			}else if(c2=='+'||c2=='-'){
				return isDigit(c3);
			}
			
		}
		return false;
	}

	private boolean overrideNumberGeneration(CharStream input,
			RecognizerSharedState state) {

		int nextChar = input.LA(1);
		int nextChar2 = input.LA(2);
		boolean result = false;

		if (isDigit(nextChar)) {
			result = true;
			consumeDigits(input);
			if (input.LA(1) == '.' && isNumberContinuation(input.LA(2), input.LA(3), input.LA(4))) {
				input.consume();
				consumeDigits(input);
			}
		} else if (nextChar == '.' && isDigit(nextChar2)) {
			result = true;
			input.consume();
			consumeDigits(input);
		}

		if (result) {
			consumeExponent(input);
			stateOK(state, numberRule);
		}
		return result;
	}

	private boolean endOfStringFound(int quoteCharacter, int length,
			CharStream input) {
		boolean result = true;
		for (int i = 0; i < length; i++) {
			result &= input.LA(i + 1) == quoteCharacter;
		}

		return result;
	}

	private void consumeString(int quoteCharacter, CharStream input,
			RecognizerSharedState state) {
		boolean isLong = input.LA(1) == quoteCharacter
				&& input.LA(2) == quoteCharacter
				&& input.LA(3) == quoteCharacter;
		int numberOfExpectedEndQuotes;
		boolean endReached = false;
		if (isLong) {
			numberOfExpectedEndQuotes = 3;
		} else {
			numberOfExpectedEndQuotes = 1;
		}
		for (int i = 0; i < numberOfExpectedEndQuotes; i++) {
			// start quotes
			input.consume();
		}
		while (!endReached) {
			if (input.LA(1) == '\\') {
				input.consume();
				input.consume();
			} else if (endOfStringFound(quoteCharacter,
					numberOfExpectedEndQuotes, input)) {
				for (int i = 0; i < numberOfExpectedEndQuotes; i++) {
					// start quotes
					input.consume();
				}
				endReached = true;
			} else if (input.LA(1) == CharStream.EOF) {
				endReached = true;
				state.failed = true;
			} else {
				input.consume();
			}
		}
	}

	private boolean overrideString(CharStream input, RecognizerSharedState state) {
		int start = input.LA(1);
		boolean isString = start == '"' || start == '\'';
		if (isString) {
			consumeString(start, input, state);
			stateOK(state, stringRule);
		}
		return isString;
	}

	private boolean overrideAasID(CharStream input, RecognizerSharedState state) {
		if (input.LA(1) == 'a') {
			int next = input.LA(2);
			boolean aIsPartOfId = next == ':' || next == '.';
			if (aIsPartOfId) {
				input.consume();
				stateOK(state, idRule);
				return true;
			}
		}
		return false;
	}

	private boolean isUTF16high(int i) {
		// x10000-xeffff
		return i > 65535 && i < 983040;
	}

	private boolean overrideUTF16(CharStream input, RecognizerSharedState state) {
		boolean isUTF16 = false;
		while (isUTF16high(input.LA(1))) {
			input.consume();
			isUTF16 = true;
		}
		if(isUTF16){
			stateOK(state, idRule);
		}
		return isUTF16;
	}

	private void checkResetColonId(CharStream input){
		switch (input.LA(1)) {
		case -1:// EOF
		case '\n':
		case ' ':
		case ',':
		case ';':
		case '[':
		case '\t':
		case '\r':
		case '#':
			resetColonId();
		default:
			break;
		}
	}

	private boolean overrideColonAsID(CharStream input, RecognizerSharedState state) {
		if (input.LA(1) == ':') {
			if(colonAsId){
				input.consume();
				stateOK(state, idRule);
				return true;
			}else{
				colonAsId=true;
			}
		}
		return false;
	}

	public boolean override(CharStream input, RecognizerSharedState state) {
		checkResetColonId(input);
		return overrideTripleEndGeneration(input, state)
				|| overrideNumberGeneration(input, state)
				|| overrideString(input, state)
				|| overrideColonAsID(input, state)
				|| overrideAasID(input, state)
				|| overrideUTF16(input, state);
	}
}
