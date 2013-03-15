package de.itemis.tooling.xturtle.ui.hover;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.resource.impl.ResourceDescriptionsProvider;
import org.eclipse.xtext.ui.editor.hover.html.DefaultEObjectHoverProvider;

import com.google.common.base.Splitter;
import com.google.inject.Inject;

import de.itemis.tooling.xturtle.resource.TurtleResourceService;
import de.itemis.tooling.xturtle.xturtle.XturtlePackage;

public class TurtleEObjectHoverProvider extends DefaultEObjectHoverProvider {

	@Inject 
	TurtleResourceService service;

	@Inject
	ResourceDescriptionsProvider indexService;
	@Override
	protected String getFirstLine(EObject o) {
		String uri = service.getUriString(o);
		if(uri!=null){
			return "<b>"+uri+"</b>";
		}
		return "";
	}
	
	@Override
	protected String getDocumentation(EObject o) {
		QualifiedName qName = service.getQualifiedName(o);
		StringBuilder b=new StringBuilder();
		if(qName!=null){
			IResourceDescriptions index = indexService.getResourceDescriptions(o.eResource());
			Iterable<IEObjectDescription> matches = index.getExportedObjects(XturtlePackage.Literals.RESOURCE, qName, false);
			for (IEObjectDescription match : matches) {
				String desc = match.getUserData("descr");
				if(desc!=null){
					Iterable<String> singleDesc=Splitter.on(",,").split(desc);
					for (String string : singleDesc) {
						b.append(simplify(string));
						b.append("</br>");
					}
					b.append("<dl></dl><dl></dl><dl></dl><dl></dl>");
				}
			}
		}
		return b.toString();
	}

	//remove surrounding quotation marks and escaped quotes
	private String simplify(String value) {
		String resultString="";
		if(value!=null){
			if(value.startsWith("\"\"\"")){
				resultString=value.substring(3, value.length()-3);
			}else{
				resultString=value.substring(1, value.length()-1);
			}
		}
		return resultString.replaceAll("\\\\\"", "\"").replaceAll("\n", "</br>");
	}

	@Override
	protected boolean hasHover(EObject o) {
		return service.getUriString(o)!=null;
	}
}
