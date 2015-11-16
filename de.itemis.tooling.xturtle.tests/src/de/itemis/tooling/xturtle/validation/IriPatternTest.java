package de.itemis.tooling.xturtle.validation;

import org.junit.Assert;
import org.junit.Test;

public class IriPatternTest {

	@Test
	public void validIris(){
		match("");
		match("http://example.org");
		match("\\u1ab9");
		match("\\U1ab9F77B");
		match("http://example.org/%20/\\u1Ab7zd\\UFFa987bbZ");
	}

	@Test
	public void invalidIris(){
		noMatch(" ");
		noMatch("<");
		noMatch(">");
		noMatch("\"");
		noMatch("{");
		noMatch("}");
		noMatch("|");
		noMatch("^");
		noMatch("`");
		noMatch("\\");
		noMatch("\\u123");
		noMatch("\\U1234567");
	}

	private void match(String iri){
		Assert.assertTrue(XturtleJavaValidator.IRI_PATTERN.matcher(iri).matches());
	}

	private void noMatch(String iri){
		Assert.assertFalse(XturtleJavaValidator.IRI_PATTERN.matcher(iri).matches());
	}
}
