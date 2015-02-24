package de.itemis.tooling.xturtle.lexing

import com.google.inject.Inject
import de.itemis.tooling.xturtle.XturtleInjectorProvider
import de.itemis.tooling.xturtle.services.XturtleGrammarAccess
import java.util.List
import org.antlr.runtime.ANTLRStringStream
import org.antlr.runtime.CharStream
import org.antlr.runtime.Token
import org.eclipse.xtext.TerminalRule
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.parser.antlr.ITokenDefProvider
import org.eclipse.xtext.parser.antlr.Lexer
import org.eclipse.xtext.parser.antlr.XtextTokenStream
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(typeof(XtextRunner))
@InjectWith(typeof(XturtleInjectorProvider))
class LexerTests {

	@Inject Lexer lexer
	@Inject ITokenDefProvider tokenProvider

	@Inject
	extension XturtleGrammarAccess ga;

	def private String tokenName(Token t){
		tokenProvider.tokenDefMap.get(t.type)
	}

	def void assertTokens(String input, TerminalRule ... expectedRules){
		val CharStream stream = new ANTLRStringStream(input);
		lexer.setCharStream(stream);
		val XtextTokenStream tokenStream = new XtextTokenStream(lexer,tokenProvider);
		val List<Token> tokens = tokenStream.getTokens();
		if(tokens.size!= expectedRules.size){
			Assert::fail('''token number does not match
			expected: «expectedRules.map[name].join(', ')»
			actual: «tokens.map[tokenName].join(', ')»''')
		}
		(0..tokens.size-1).forEach[tokenNr|
			val it=tokens.get(tokenNr)
			Assert::assertEquals("rule mismatch for token "+(tokenNr+1), "RULE_"+expectedRules.get(tokenNr).name, it.tokenName)
		]
	}

	@Test
	def void ids() {
		val rule=IDRule
		assertTokens("tada",rule)
	}

	@Test
	def void nr() {
		val rule=NUMBERRule
		assertTokens("123",rule)
	}

	@Test
	def void uri() {
		val rule=URIRule
		assertTokens("<adashdk/alkda>",rule)
	}

	@Test
	def void escape() {
		val rule=LOCALESCRule
		assertTokens("%26",rule)
		assertTokens("\\.",rule)
		assertTokens("\\/",rule)
	}

	@Test
	def void comment() {
		val rule=SL_COMMENTRule
		assertTokens("#gdfgkdhfk",rule)
		assertTokens("# 		gdfgkdhfk",rule)
	}

	@Test
	def void number() {
		val rule=NUMBERRule
		assertTokens("1",rule)
		assertTokens("12",rule)
		assertTokens("123",rule)
		assertTokens("1123123",rule)
		//I can't see that 1. is not a valid number literal
//		assertTokens("1.",rule)
		assertTokens("1.9",rule)
		assertTokens(".2",rule)
		assertTokens(".294723",rule)

		assertTokens("1e13",rule)
		assertTokens("12e+13",rule)
		assertTokens("99e-34",rule)

		assertTokens("1.123e13",rule)
		assertTokens("12.123e+13",rule)
		assertTokens("99.123e-34",rule)

		assertTokens(".123e13",rule)
		assertTokens(".123e+13",rule)
		assertTokens(".123e-34",rule)
	}

	@Test
	def void string() {
		val rule=STRINGRule
		assertTokens('''"sldfjs"''',rule)
		assertTokens('''"sldfjs
		"''',rule)
		assertTokens('''"""dkghd
			fkg"""''',rule)
		assertTokens('''"""dkgh"dfk""g"""''',rule)
		assertTokens('''"""dkgh"dfk\"\""""''',rule)
	}

//	@Test
//	def void keywords() {
//		assertTokens("@",ATRule)
//		assertTokens(":",ATRule)
//	}

}
