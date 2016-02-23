package de.itemis.tooling.xturtle.serializer;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.RuleCall;
import org.eclipse.xtext.nodemodel.INode;

public class XturtleCustomSyntacticSequencer extends XturtleSyntacticSequencer {

	@Override
	@SuppressWarnings("restriction")
	protected String getTRIPELENDToken(EObject semanticObject,
			RuleCall ruleCall, INode node) {
		if (node != null){
			return getTokenText(node);
		}
		return ".\n";
	}
}
