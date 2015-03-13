package de.itemis.tooling.xturtle.parsing

import com.google.common.io.Files
import com.google.inject.Inject
import de.itemis.tooling.xturtle.NoValidationInjectorProvider
import de.itemis.tooling.xturtle.TurtleParseHelper
import de.itemis.tooling.xturtle.xturtle.DirectiveBlock
import java.io.File
import java.nio.charset.Charset
import org.eclipse.xtext.diagnostics.Severity
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(typeof(XtextRunner))
@InjectWith(typeof(NoValidationInjectorProvider))
class ParsingSuiteTest {

	@Inject extension TurtleParseHelper<DirectiveBlock>
	@Inject extension ValidationTestHelper

	@Test
	def void testPositive() {
		val folder=new File("testFiles/ParsingSuite/passing/positive")
		val files =folder.listFiles
		files.forEach[
			try{
				Files::toString(it, Charset::forName("UTF-8")).parse.assertNoIssues
			}catch(Throwable e){
				throw new RuntimeException("failed for file "+it.name,e)
			}
		] 
	}

	@Test
	def void testNegative() {
		val folder=new File("testFiles/ParsingSuite/passing/negative")
		val files =folder.listFiles
		files.forEach[
			val issues=Files::toString(it, Charset::forName("UTF-8")).parse.validate
			Assert::assertFalse(it.name, issues.empty)
			val errors=issues.filter[it.severity==Severity::ERROR]
			Assert::assertFalse(it.name, errors.empty)
		] 
	}
}
