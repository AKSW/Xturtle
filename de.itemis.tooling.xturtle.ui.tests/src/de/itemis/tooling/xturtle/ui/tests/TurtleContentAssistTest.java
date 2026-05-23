package de.itemis.tooling.xturtle.ui.tests;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.XtextRunner;
import org.eclipse.xtext.ui.editor.contentassist.ConfigurableCompletionProposal;
import org.eclipse.xtext.ui.editor.contentassist.ReplacementTextApplier;
import org.eclipse.xtext.ui.testing.AbstractContentAssistTest;
import org.eclipse.xtext.ui.testing.ContentAssistProcessorTestBuilder;
import org.eclipse.xtext.util.Strings;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;
import com.google.inject.Provider;

@RunWith(XtextRunner.class)
@InjectWith(XturtleUiInjectorProvider.class)
public class TurtleContentAssistTest extends AbstractContentAssistTest {

	@Inject
	private Provider<XtextResourceSet> resourceSetProvider;
	
	private static final int NL_LENGHT=Strings.newLine().length();
	private static int counter=1;
	int preInfixOffset;

	@Override
	//overridden in order to create an absolute URI
	public XtextResource getResourceFor(InputStream stream) {
		XtextResourceSet resourceSet = resourceSetProvider.get();
		initializeTypeProvider(resourceSet);
		try {
			URI resourceUri = URI.createURI("platform:/resource/project/testmodel"+(counter++)+".ttl");
			Resource resource = resourceSet.createResource(resourceUri);
			resource.load(stream, null);
			return (XtextResource) resource;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void simpleCAfailingAfterFirstLexerModifications() throws Exception{
		newBuilder().assertProposal("@prefix");

		newBuilder().appendNl("@prefix x:</tada>.")
		.assertProposal("x:").apply().append("a ")
		.assertProposal("x:").apply()
		.assertProposal(":a");
	}

	@Test
	//TODO
	@Ignore("empty prefix proposals are currenty broken")
	public void emptyPrefixProposal() throws Exception{
		newBuilder().appendNl("@prefix :</tada>.")
		.assertProposal(":").apply().append("a ")
		.assertProposal(":").apply()
		.assertProposal(":a");
	}

	@Test
	//TODO
	@Ignore("empty prefix proposals are currenty broken")
	public void prefixWithNonemptyFragment() throws Exception{
		newBuilder().appendNl("@prefix :<http://www.example.de/tidum#ta>.")
		.appendNl("<http://www.example.de/tidum#tada> ")
		.assertProposal(":").apply()
		.assertProposal(":da");
	}

	@Test
	public void prefixCC() throws Exception{
		newBuilder().appendNl("@prefix ")
		.assertProposal("rdf").apply()
		.assertProposal(":").apply()
		.assertProposal("<http://www.w3.org/1999/02/22-rdf-syntax-ns#>");
	}

	@Test
	public void languageProposals() throws Exception{
		newBuilder().appendNl("<> a \"foo\"").assertProposal("@en");
		newBuilder().appendNl("<> a \"foo\"").assertProposal("@de");
	}

	//infixlines start at offset 38
	private ContentAssistProcessorTestBuilder getBuilder(String ... infixLines)throws Exception{
		String line1="@prefix x:</tada>.";
		String line2="x:test1 a <tada>.";

		preInfixOffset=line1.length()+NL_LENGHT+line2.length()+NL_LENGHT;

		ContentAssistProcessorTestBuilder result=newBuilder()
		.appendNl(line1)
		.appendNl(line2);
		for (String string : infixLines) {
			result=result.appendNl(string);
		}
		result=result.appendNl("x:test2 a <tada>.");
		return result;
	}

	@Test
	public void referenceInSimpleTriple() throws Exception{
		ICompletionProposal[] proposals = getBuilder("x:a x:").computeCompletionProposals(preInfixOffset+6);
		checkProposalExists(":a", proposals);
		checkProposalExists(":test1", proposals);
		//at this point the following defintion of x:test2 is not known, as
		//the parser has no way of knowing that a new triple has started
		//checkProposalExists(":test2", proposals);

		proposals = getBuilder("x:a x: .").computeCompletionProposals(preInfixOffset+6);
		checkProposalExists(":a", proposals);
		checkProposalExists(":test1", proposals);
		//here the parser has the triple end to indicate that x:test2 is a new subject
		checkProposalExists(":test2", proposals);
	}

	@Test
	//bug reported by Alexander Willner
	//reason was that the language tag for a string literal caused the partitioner
	//to fail - none of the following triples was known, fixed with a grammar change
	//making the language a terminal rule
	public void referenceInTripleWithStringLiteral() throws Exception{
		ICompletionProposal[] proposals = getBuilder("x:a x: ;","a 'abc'@de .").computeCompletionProposals(preInfixOffset+6);
		checkProposalExists(":a", proposals);
		checkProposalExists(":test1", proposals);
		checkProposalExists(":test2", proposals);

		int infixOffset="x:a a a 'abc'@de;".length()+NL_LENGHT+6;
		proposals = getBuilder("x:a a a 'abc'@de;","x.a x: .").computeCompletionProposals(preInfixOffset+infixOffset);
		checkProposalExists(":a", proposals);
		checkProposalExists(":test1", proposals);
		checkProposalExists(":test2", proposals);
	}

	void checkProposalExists(String expectedProposal, ICompletionProposal[] proposals){
		for(ICompletionProposal proposal: proposals) {
			if (expectedProposal.equals(toString(proposal))) {
				return;
			}
		}
		Assert.fail("No such proposal: " + expectedProposal + " Found: " + toString(proposals));
	}

	protected String toString(ICompletionProposal proposal) {
		String proposedText = proposal.getDisplayString();
		if (proposal instanceof ConfigurableCompletionProposal) {
			ConfigurableCompletionProposal configurableProposal = (ConfigurableCompletionProposal) proposal;
			proposedText = configurableProposal.getReplacementString();
			if (configurableProposal.getTextApplier() instanceof ReplacementTextApplier)
				proposedText = ((ReplacementTextApplier) configurableProposal.getTextApplier()).getActualReplacementString(configurableProposal);
		}
		return proposedText;
	}

	public List<String> toString(ICompletionProposal[] proposals) {
		if (proposals == null)
			return Collections.emptyList();
		List<String> res = new ArrayList<String>(proposals.length);
		for (ICompletionProposal proposal : proposals) {
			String proposedText = toString(proposal);
			res.add(proposedText);
		}
		Collections.sort(res);
		return res;
	}
}
