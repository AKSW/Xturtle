package de.itemis.tooling.xturtle.validation;

import org.junit.Assert;
import org.junit.Test;

public class TurtleLinkingErrorExceptionTest {

	TurtleLinkingErrorExceptions exc=new TurtleLinkingErrorExceptions();
	private final String rdfNs="http://www.w3.org/1999/02/22-rdf-syntax-ns#";

	@Test
	public void listProperties(){
		rdfExceptionMatch("li");
		rdfExceptionMatch("_1");
		rdfExceptionMatch("_2");
		rdfExceptionMatch("_3");
		rdfExceptionMatch("_4");
		rdfExceptionMatch("_999");
	}

	@Test
	public void noExceptions(){
		noRdfException("label");
		noRdfException("_a");
		noRdfException("3");
		noRdfException("Bag");
		noRdfException("value");
	}

	private void rdfExceptionMatch(String rdfSuffix){
		Assert.assertTrue(exc.matchesRdfListProperty(rdfNs+rdfSuffix));
	}

	private void noRdfException(String rdfSuffix){
		Assert.assertFalse(exc.matchesRdfListProperty(rdfNs+rdfSuffix));
	}
}
