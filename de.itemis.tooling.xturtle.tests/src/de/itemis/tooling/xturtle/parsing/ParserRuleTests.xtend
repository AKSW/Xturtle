package de.itemis.tooling.xturtle.parsing

import com.google.inject.Inject
import de.itemis.tooling.xturtle.XturtleInjectorProvider
import de.itemis.tooling.xturtle.parser.antlr.XturtleParser
import de.itemis.tooling.xturtle.services.XturtleGrammarAccess
import java.io.StringReader
import org.eclipse.xtext.ParserRule
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(typeof(XtextRunner))
@InjectWith(typeof(XturtleInjectorProvider))
class ParserRuleTests {

	@Inject XturtleParser parser

	@Inject
	extension XturtleGrammarAccess ga;

	def void assertParsable(String input, ParserRule entryRule){
		val result=parser.parse(entryRule, new StringReader(input))
		Assert::assertFalse(result.syntaxErrors.map[syntaxErrorMessage].join(", "),result.hasSyntaxErrors)
	}

	@Test
	def void prefixName() {
		val rule=prefixNameRule
		assertParsable("tada",rule)
		assertParsable("a·̀ͯ‿",rule)
//		assertParsable("a·̀ͯ‿.⁀",rule)
//		assertParsable("AZazÀÖØöø˿Ͱͽ΄῾‌‍⁰↉Ⰰ⿕、ퟻ﨎ﷇﷰ￯𐀀󠇯",rule)
		assertParsable("tada.123.eg",rule)
	}

	@Test
	def void name() {
		val rule=nameRule
		assertParsable("a",rule)
		assertParsable("a.a",rule)
		assertParsable("a.b",rule)
		assertParsable("tada",rule)
		assertParsable("a·̀ͯ‿⁀",rule)
//		assertParsable("a·̀ͯ‿.⁀",rule)
//		assertParsable("AZazÀÖØöø˿ͰͽͿ῿‌‍⁰↏Ⰰ⿯、퟿豈﷏ﷰ�𐀀󯿽",rule)
		assertParsable("tada.123.eg",rule)
	}

	@Test
	def void StringLiteral() {
		val rule=stringLiteralRule
		assertParsable('''"tada"@en''',rule)
		assertParsable('''"""tada"""@en''',rule)
	}

}
