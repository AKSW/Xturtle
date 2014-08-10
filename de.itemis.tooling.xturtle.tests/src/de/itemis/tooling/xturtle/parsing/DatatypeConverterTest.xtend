package de.itemis.tooling.xturtle.parsing

import com.google.inject.Inject
import de.itemis.tooling.xturtle.XturtleInjectorProvider
import de.itemis.tooling.xturtle.xturtle.DirectiveBlock
import de.itemis.tooling.xturtle.xturtle.QNameRef
import de.itemis.tooling.xturtle.xturtle.Triples
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(typeof(XtextRunner))
@InjectWith(typeof(XturtleInjectorProvider))
class DataTypeConverterTest {

	@Inject extension ParseHelper<DirectiveBlock>
	@Inject extension ValidationTestHelper

	@Test
	@Ignore
	def void testColonRemoved() {
		val model='''
			@prefix a:<tada2/> .
			a:bc <b> a:bc .
		'''.parse
		model.assertNoIssues
		val triple=model.triples.get(0) as Triples
		val qname=triple.predObjs.get(0).objects.get(0) as QNameRef
		Assert::assertEquals("bc",qname.ref);
	}

	@Test
	def void resolveUri() {
		'''
			@base <http://www.nittka.de/alex/> .
			<name> <is/somethingLike> <http://www.nittka.de/familyName/Nittka> .
		'''.parse.assertNoErrors
	}

	@Test
	@Ignore
	def void testStuff() {
		val model='''
			@base <tada/> .
			@prefix a:<tada2/> .
			@base <tidum/> .
			@prefix b:<blubs#> .
			@prefix :<http://default.de/> .
			#sfhsf
			a:bc <b> a:bc .
			#skfhskf
			:tada <b> <c> .
		'''.parse
//		val triple=model.statements.get(1) as Triples
//		val qname=triple.predObjs.get(0).objects.get(0) as QNameRef
//		Assert::assertEquals("bc",qname.ref);
	}

}
