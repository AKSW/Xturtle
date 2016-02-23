package de.itemis.tooling.xturtle.formatting

import com.google.inject.Inject
import de.itemis.tooling.xturtle.NoValidationInjectorProvider
import de.itemis.tooling.xturtle.TurtleParseHelper
import de.itemis.tooling.xturtle.xturtle.DirectiveBlock
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.resource.SaveOptions
import org.eclipse.xtext.serializer.ISerializer
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.eclipse.xtext.nodemodel.util.NodeModelUtils
import org.eclipse.emf.ecore.EObject

@RunWith(typeof(XtextRunner))
@InjectWith(typeof(NoValidationInjectorProvider))
class SerializeTest {

	@Inject extension ISerializer serializer
	@Inject extension TurtleParseHelper<DirectiveBlock>

	@Test
	def void serializeTripleEnd() {
		//#69 quick fix caused serializer to add "DEFINEDINCUSTOMLEXER" due to custom lexer
		'''
			@prefix x:<y> .

			<tada> a <tidum> .'''.same
	}

	def private void same(CharSequence modelString){
		val model=modelString.plainModel
		val serialized=model.serialize
		Assert.assertEquals(modelString.toString, serialized)
	}

	def private String serialize(EObject model){
		serializer.serialize(model, SaveOptions.newBuilder.format.options)
	}

	def private DirectiveBlock getPlainModel(CharSequence modelString){
		val model=modelString.parse
		model.ensureNoNodeModel
		return model
	}

	def private void ensureNoNodeModel(EObject object){
		object.eAdapters.clear
		Assert.assertNull(NodeModelUtils.getNode(object))
	}
}
