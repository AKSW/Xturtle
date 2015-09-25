package de.itemis.tooling.xturtle.validation

import com.google.inject.Inject
import de.itemis.tooling.xturtle.TurtleParseHelper
import de.itemis.tooling.xturtle.XturtleInjectorProvider
import de.itemis.tooling.xturtle.xturtle.DirectiveBlock
import de.itemis.tooling.xturtle.xturtle.ResourceRef
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import de.itemis.tooling.xturtle.xturtle.XturtlePackage
import org.eclipse.xtext.diagnostics.Diagnostic

@RunWith(typeof(XtextRunner))
@InjectWith(typeof(XturtleInjectorProvider))
class LinkingErrorTest {

	@Inject extension TurtleParseHelper<DirectiveBlock>
	@Inject extension ValidationTestHelper

	@Test
	def void noUriLinkingError() {
		val model='''
			<1> <2> <3> .
		'''.parse
		model.assertNoIssues
	}

	@Test
	def void qnameLinkingError() {
		val model='''
			@prefix foo:<http://www.example.de#> .
			<1> foo:bar <3> .
		'''.parse
		//assert that there is a linking error
		model.assertError(XturtlePackage$Literals::QNAME_REF, Diagnostic::LINKING_DIAGNOSTIC)
	}

	@Test
	def void listPropertiesNoLinkingError() {
		val model='''
			@prefix rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
			<1> rdf:li rdf:_1 .
			<2> <http://www.w3.org/1999/02/22-rdf-syntax-ns#li> <http://www.w3.org/1999/02/22-rdf-syntax-ns#_2> .
		'''.parse
		//assert that there are no linking errors raised, but all references are unresolved
		model.assertNoIssues
		val refs=model.eAllContents.filter(ResourceRef).toList
		Assert.assertEquals(4, refs.size)
		refs.forEach[ref|
			val unresolved=ref.ref.eIsProxy
			Assert.assertTrue(ref.toString, unresolved)
		]
	}
}
