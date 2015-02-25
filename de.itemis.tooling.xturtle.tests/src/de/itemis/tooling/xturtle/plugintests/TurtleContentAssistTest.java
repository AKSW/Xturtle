package de.itemis.tooling.xturtle.plugintests;

import org.eclipse.xtext.ISetup;
import org.eclipse.xtext.junit4.ui.AbstractContentAssistProcessorTest;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.Injector;

import de.itemis.tooling.xturtle.XturtleUiInjectorProvider;

@SuppressWarnings("restriction")
@Ignore("plugin test")
public class TurtleContentAssistTest extends AbstractContentAssistProcessorTest {

	@BeforeClass
	public static void useSI(){
		useStaticInjector=false;
	}

	@Test
	public void simpleCAfailingAfterFirstLexerModifications() throws Exception{
		newBuilder().assertProposal("@prefix ");

		newBuilder().append("@prefix x:</tada>. ")
		.assertProposal("x:").apply().append("a ")
		.assertProposal("x:").apply()
		.assertProposal(":a");
	}
	@Override
	protected ISetup doGetSetup() {
		return new ISetup() {
			@Override
			public Injector createInjectorAndDoEMFRegistration() {
				return new XturtleUiInjectorProvider().getInjector();
			}
		};
	}

}
