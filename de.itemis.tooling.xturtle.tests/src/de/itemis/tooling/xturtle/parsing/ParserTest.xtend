package de.itemis.tooling.xturtle.parsing

import com.google.common.io.Files
import com.google.inject.Inject
import de.itemis.tooling.xturtle.NoValidationInjectorProvider
import de.itemis.tooling.xturtle.xturtle.DirectiveBlock
import de.itemis.tooling.xturtle.xturtle.XturtlePackage
import java.io.File
import java.nio.charset.Charset
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(typeof(XtextRunner))
@InjectWith(typeof(NoValidationInjectorProvider))
class ParserTest {

	@Inject extension ParseHelper<DirectiveBlock>
	@Inject extension ValidationTestHelper

	@Test
	def void testSimple1() {
		val model='''
			@prefix b:<tada/> .
			<a> <b> <a> .
			b:b <a> <b> .
		'''.parse
		model.assertNoIssues

//		val builder=SaveOptions::newBuilder
//		builder.format
//		model.eResource.save(System::out,builder.options.toOptionsMap)
	}

	@Test
	def void testPrefixA() {
		val model='''
			@prefix a:<tada/> .
			@prefix b:<tidum/> .
			b:b b:b b:b .
			b:b a b:b .
			a:b a:b a:b .
			a:b a a:b .
			a:b a a:b.
			a:a.b b:b "".
			a:a.b a "".
			a:a.a a "".
			a:b a a:a.
			a:a.b a a:b.
			a:a.b a a:a.
			a:a a a:a .
			a: a a: .
		'''.parse
		model.assertNoIssues
	}

	@Test
	def void testLongString1() {
		'''
			<a> <b> """tada\"""" .
		'''.parse.assertNoIssues
	}

	@Test
	def void testSimple2() {
		'''
			@prefix :<tada/> .
			:a9 :b9 ( ) .
		'''.parse.assertNoIssues
	}

	@Test
	def void testHexLocal() {
		'''
			@prefix :<tada/> .
			:a%FF :b9 ( ) .
		'''.parse.assertNoIssues
	}

	@Test
	def void leadingDigitLocalName() {
		'''
			@prefix :<tada/>.
			@prefix b:<tidum/>.
			:123ab :123ab :123ab.
			:123ab a :123ab.
			b:123ab b:123ab b:123ab.
			b:123ab a b:123ab.
		'''.parse.assertNoIssues
	}

	@Test
	def void dotInLocalName() {
		'''
			@prefix :<tada/>.
			:a.b :m.017ysq :sdkfh.
			:a :a :a.b. 
			:a a :a.b.	
			:a :a :a.b.#dgkdhf
			:a :a :a.b.
		'''.parse.assertNoIssues
	}

	@Test
	def void blankNodeLabel() {
		'''
			_:fsdf a _:fsdf.
«««			_:a·̀ͯ‿.⁀ a _:a·̀ͯ‿.⁀ .
		'''.parse.assertNoIssues
	}

	@Test
	def void testLongString2() {
		'''
			@prefix :<tada/> .
			:d :e """\tThis \uABCDis\r \U00015678another\n
			one		
			""" .
		'''.parse.assertNoIssues
	}

	@Test
	def void testLongString3() {
		'''
			@prefix :<tada/> .
			:a :b """John said: ""Hello "World!\"""" .
		'''.parse.assertNoIssues
	}

	@Test
	def void testAxiom() {
		'''
			@prefix :<tada/> .
			:a :a :a.
			[].
			[ :a :a ;
				:a ( :a
					:a
				)
			] .
			
		'''.parse.assertNoIssues
	}

	@Test
	def void missingSubject() {
		'''<a>.'''.parse.assertError(XturtlePackage.Literals.TRIPLES, "axiom")
	}

	@Test
	def void preventBlankPrefix() {
		'''@prefix _:<tada/>.
		'''.parse.assertError(XturtlePackage.Literals.PREFIX_ID, "blank_prefix")
	}

	@Test
	def void testNumbers() {
		//removed "1." because I don't see that it is a valid number literal
		'''
			<http://example.org/foo>
			<http://example.org/bar> 2.345, 1, 1.0, 1.000000000, 2.3, 2.234000005, 2.2340000005, 2.23400000005, 2.234000000005, 2.2340000000005, 2.23400000000005, 2.234000000000005, 2.2340000000000005, 2.23400000000000005, 2.234000000000000005, 2.2340000000000000005, 2.23400000000000000005, 2.234000000000000000005, 2.2340000000000000000005, 2.23400000000000000000005, 1.2345678901234567890123457890 .
		'''.parse.assertNoIssues
	}

	@Test
	def void testLongString4() {

		'''
			@prefix project:<http://tada/> .
			project:LIMES a project:Project ;
			              project:content """<p>LIMES is a link discovery framework for the Web of Data. It implements time-efficient approaches for large-scale link discovery based on the characteristics of metric spaces. It is easily configurable via a web interface. It can also be downloaded as standalone tool for carrying out link discovery locally. In addition the Colanut GUI implements mechanisms for the automatic suggestion of link configurations.</p>
			
			<p>LIMES implements novel time-efficient approaches for link discovery in metric spaces. Our approaches utilize the mathematical characteristics of metric spaces to compute estimates of the similarity between instances. These estimates are then used to filter out a large amount of those instance pairs that do not suffice the mapping conditions. By these means, LIMES can reduce the number of comparisons needed during the mapping process by several orders of magnitude.</p>
			
			<p id=\"p29826-3\" class=\"auto\"><img src=\"http://aksw.org/Projects/LIMES/files?get=workflow.png\" alt=\"\" width=\"400\" height=\"190\" /></p>
			
			<p><a name=\"p29826-4\"></a></p>
			
			<p id=\"p29826-4\" class=\"auto\">The general workflow implemented by the LIMES framework comprises four steps: Given a source, a target and a threshold, LIMES first computes a set exemplars for the target data source (step 1). This process is concluded by matching each target instance to the exemplar closest to it. In step 2 and 3, the matching is carried out. In the filterig step, the distance between all source instances and target instances is approximated via the exemplars computed previously (step 3). Obvious non-matches are then filtered out. Subsequently, the real distance between the remaining source and target instances are computed (step 3). Finally, the matching instances are are serialized, i.e., written in a user-defined output stream according to a user-specified format, e.g. <a class=\"outerlink\" title=\"Outgoing link (in new window)\" href=\"http://www.w3.org/2001/sw/RDFCore/ntriples/\" target=\"_blank\"><img class=\"contexticon\" src=\"http://aksw.org/themes/aksw2007/icons/world_link.png\" alt=\"\" />NTriples</a> (step 4).</p>""" .
		'''.parse.assertNoIssues
	}

	@Test
	def void testUTF() {
		Files::toString(new File("testFiles/utf_character.ttl"), Charset::forName("UTF-8")).parse.assertNoErrors
	}

	@Test
	def void testLocalNames() {
		Files::toString(new File("testFiles/localNames.ttl"), Charset::forName("UTF-8")).parse.assertNoErrors
	}
}
