package de.itemis.tooling.xturtle.ui.syntaxcoloring;

import java.util.List;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightedPositionAcceptor;
import org.eclipse.xtext.ui.editor.syntaxcoloring.ISemanticHighlightingCalculator;

import com.google.common.base.Ascii;

import de.itemis.tooling.xturtle.xturtle.QNameRef;
import de.itemis.tooling.xturtle.xturtle.UriRef;
import de.itemis.tooling.xturtle.xturtle.XturtlePackage;

public class TurtleSemanticHighlighter implements
		ISemanticHighlightingCalculator {

	public void provideHighlightingFor(XtextResource resource,
			IHighlightedPositionAcceptor acceptor) {
		if(resource!=null){
			TreeIterator<EObject> iterator = resource.getAllContents();
			while(iterator.hasNext()){
				highlight(iterator.next(), acceptor);
			}
		}
	}

	private void highlight(EObject obj, IHighlightedPositionAcceptor acceptor) {
		if(obj instanceof UriRef){
			highlightUriRef((UriRef)obj,acceptor);
		} else if(obj instanceof QNameRef){
			highlightQnameRef((QNameRef)obj, acceptor);
		}
	}

	private void highlightUriRef(UriRef ref,
			IHighlightedPositionAcceptor acceptor) {
		if(ref.getRef()!=null&&ref.getRef().eIsProxy()){
			INode node = NodeModelUtils.findNodesForFeature(ref, XturtlePackage.Literals.RESOURCE_REF__REF).get(0);
			acceptor.addPosition(node.getOffset(), node.getLength(), TurtleHighlightingConfig.URI_ID_UNRESOLVABLE);
		}
	}

	private void highlightQnameRef(QNameRef ref,
			IHighlightedPositionAcceptor acceptor) {
		if(ref.getPrefix()!=null){
			INode node = NodeModelUtils.findNodesForFeature(ref, XturtlePackage.Literals.QNAME_REF__PREFIX).get(0);
			acceptor.addPosition(node.getOffset(), node.getLength(), TurtleHighlightingConfig.PREFIX_ID);
		}
		List<INode> nodes = NodeModelUtils.findNodesForFeature(ref, XturtlePackage.Literals.RESOURCE_REF__REF);
		if(nodes.size()!=0){
			INode node = nodes.get(0);
			String text=node.getText();
			if(text!=null&&text.length()>1){
				String coloringId = TurtleHighlightingConfig.PROPERTY_ID;
				if(Ascii.isUpperCase(text.charAt(1))){
					coloringId=TurtleHighlightingConfig.CLASS_ID;
				}
				acceptor.addPosition(node.getOffset()+1, node.getLength()-1, coloringId);
			}
		}
	}
}
