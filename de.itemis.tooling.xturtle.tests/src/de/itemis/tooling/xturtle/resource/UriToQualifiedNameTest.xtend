package de.itemis.tooling.xturtle.resource

import org.junit.Assert
import org.junit.Test
import org.eclipse.xtext.naming.QualifiedName
import org.eclipse.emf.common.util.URI

class UriToQualifiedNameTest {

	def void assertQname(String uriString, String... expectedElements){
		val URI uri=URI::createURI(uriString)
		val qName=TurtleUriResolver::getName(uri)
		Assert::assertEquals(expectedElements.size, qName.segmentCount)
		(1..qName.segmentCount).forEach[
			Assert::assertEquals('''segment «it» for «uriString»''', expectedElements.get(it-1), qName.segments.get(it-1))
		]
	}

	@Test
	def void testNames() {
		assertQname("http://a/b/c#", "http://a/b/c#","")
		assertQname("http://a/b/c#a", "http://a/b/c#","a")
		assertQname("http://a/b/c", "http://a/b/","c")
	}

	@Test
	def void testBlankQName(){
		val TurtleUriResolver resolver=new TurtleUriResolver(URI.createURI("file://testfile.ttl"))
		val blankPrefixQname=resolver.getPrefixName("_","#")
		val expectedBaseQName=QualifiedName::create("file://testfile.ttl#")
		Assert::assertEquals(expectedBaseQName, blankPrefixQname)
		val blankQName=resolver.resolveWithLocalName("_","tidum")
		Assert::assertEquals(expectedBaseQName.append("tidum"), blankQName)
	}
}
