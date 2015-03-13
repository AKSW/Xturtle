package de.itemis.tooling.xturtle.formatting

import com.google.inject.Inject
import de.itemis.tooling.xturtle.NoValidationInjectorProvider
import de.itemis.tooling.xturtle.TurtleParseHelper
import de.itemis.tooling.xturtle.xturtle.DirectiveBlock
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.formatting.INodeModelFormatter
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.eclipse.xtext.resource.XtextResource
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(typeof(XtextRunner))
@InjectWith(typeof(NoValidationInjectorProvider))
class FormatterTest {

	@Inject extension TurtleParseHelper<DirectiveBlock>
	@Inject extension ValidationTestHelper
	@Inject INodeModelFormatter formatter

	@Test
	def void testDiractivesSame() {
		'''@base <tada> .'''.same
		'''@prefix a:<ab> .'''.same

		'''
		@prefix a:<ab> .
		@base <tada> .'''.same

		'''
		@base <tada> .
		@prefix a:<ab> .'''.same

		'''
		@base <tada> .
		@prefix a:<ab> .
		@base <tidum> .
		@prefix b:<ab> .
		@prefix c:<ab> .
		@base <bla> .
		@base <blubbs> .'''.same
	}

	@Test
	def void testDirectiveTripleSame() {
		'''
		@prefix a:<ab> .
		
		<a> <a> <a> .'''.same

		'''
		@base <ab> .
		
		<a> <a> <a> .'''.same

		'''
		@base <tada> .
		@prefix a:<ab> .
		
		<a> <a> <a> .'''.same

		'''
		@prefix a:<ab> .
		@base <tada> .
		
		<a> <a> <a> .'''.same
	}

	@Test
	def void testTripleSame() {
		'''<a> a <a> .'''.same

		'''<a> a true .'''.same

		'''
		@prefix a:<ab> .
		
		<a> a a:a .'''.same

		'''
		@prefix a:<ab> .
		
		a:a a "sfsd"^^a:a .'''.same

		'''
		@prefix a:<ab> .
		
		a:a a "sfsd"@de .'''.same

		'''
		@prefix a:<ab> .
		
		a:a <tada> 1 .'''.same

		'''
		@prefix a:<ab> .
		
		a:a <tada> 1.27 .'''.same

		'''
		@prefix a:<ab> .
		
		a:a.asd a:asd -1e-25 .'''.same
	}

	@Test
	def void testTriplesSame() {
		'''
		@prefix a:<ab> .
		
		<a> <a> <a> .
		a:a a <a> .'''.same

		'''
		@prefix a:<ab> .
		
		<a> <a> <a> .
		
		a:a a <a> .'''.same

		'''
		@base <tada> .
		@prefix a:<ab> .
		
		<a> <a> <a> .
		a:a a <a> .
		
		@prefix b:<ab> .
		@base <tada> .
		
		<b> <b> <b> .
		b:x a <b> .'''.same
	}

	@Test
	def void testMultiplePredicatesSame() {
		'''
		@prefix a:<ab> .
		
		<a> <a> <a>;
			a <a> .'''.same

		'''
		@prefix a:<ab> .
		
		<a>
			<a> <a>;
			a <a> .'''.same

		'''
		@prefix a:<ab> .
		
		<a> a <a>;
			a:a a:a .
		a:a
			<a> a:a;
			a <a>;
			a:a <a> .'''.same
	}

	@Test
	def void testMultipleObjectsSame() {
		'''<a> a <a>, <b>, <c> .'''.same

		//hidden for StringLiteral makes space before triple end "impossible"
		'''
		@prefix a:<ab> .

		<a> a <a>, <b>, <c> .
		<b> <a> true, 1.234, "afsd"^^a:b.'''.same
	}

	@Test
	def void testBlankObjectsSame() {
		'''[] .'''.same
		'''[ <a> <b> ] .'''.same
		'''[ <a> <b>; <c> <d> ] .'''.same


		'''
		[
			<a> <b>;
			<c> <d>
		] .'''.same

		'''
		[ <a> <b>; <c> [ <d> <e>; <f> <g> ] ] .'''.same

		'''
		[
			<a> <b>;
			<c> [ <d> <e>; <f> <g> ]
		] .'''.same

		'''
		[
			<a> <b>;
			<c> [
				<d> <e>;
				<f> <g>
			]
		] .'''.same

		'''
		[
			<a> <b>;
			<c> [
				<d> <e>;
				<f> <g>
			]
		] a <a> .'''.same

		'''
		<a> a [
				<a> <b>;
				<c> [
					<d> <e>;
					<f> <g>
				]
			] .'''.same
	}

	@Test
	def void testBlankCollectionSame() {
		'''() a <a> .'''.same
		'''<a> a () .'''.same

		'''<a> a ( <a> <b> ) .'''.same
		'''<a> a ( <a> <b> true "sfsdf" 1.2 ) .'''.same

		'''
		(
			<b> <c> <d>
		) a <a> .'''.same

		'''
		(
			<b>
			<c>
			<d>
		) a <a> .'''.same

		'''
		<a> a (
				<b> <c> <d>
			) .'''.same

		'''
		<a> a (
				<b>
				<c>
				<d>
			) .'''.same
	}

	def void same(CharSequence modelString) {
		same(modelString,false)
	}

	def void same(CharSequence modelString, boolean printoutFormatted) {
		val model = modelString.parse
		model.assertNoIssues
		val formatted = format(model)
		if(printoutFormatted){
			println(formatted)
		}
		Assert::assertEquals(modelString.toString, formatted)
	}

	def String format(EObject model) {
		val parseResult = (model.eResource as XtextResource).parseResult
		formatter.format(parseResult.rootNode, 0, Integer::MAX_VALUE).formattedText
	}

}
