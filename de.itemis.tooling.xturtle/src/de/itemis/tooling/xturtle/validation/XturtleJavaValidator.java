package de.itemis.tooling.xturtle.validation;

import java.util.List;

import org.eclipse.xtext.validation.Check;

import com.google.inject.Inject;

import de.itemis.tooling.xturtle.resource.TurtleResourceService;
import de.itemis.tooling.xturtle.services.Prefixes;
import de.itemis.tooling.xturtle.xturtle.Directive;
import de.itemis.tooling.xturtle.xturtle.PrefixId;
import de.itemis.tooling.xturtle.xturtle.QNameDef;
import de.itemis.tooling.xturtle.xturtle.XturtlePackage;
 

public class XturtleJavaValidator extends AbstractXturtleJavaValidator {
	@Inject 
	private Prefixes prefixes;
	@Inject 
	TurtleResourceService service;

	//there may be a linked prefix *after* the Triple
	@Check
	public void checkEmptyPrefixDefined(QNameDef def) {
		if(service.getQualifiedName(def)==null){
			error("no @prefix-Definition up to this point", XturtlePackage.Literals.QNAME_DEF__PREFIX);
		}
	}

	@Check
	public void checkBaseUri(Directive def) {
		String uri = def.getUri();
		if(uri.length()>0){
			char lastCharacter = uri.charAt(uri.length()-1);
			if(lastCharacter!='#' && lastCharacter!='/'){
				error("onlx # and / allowed as last character, otherwise Uri-Resolution will fail", XturtlePackage.Literals.DIRECTIVE__URI);
			}
		}
	}

	//check prefix definition is in line with prefix.cc
	@Check
	public void checkPrefixCC(PrefixId def) {
		if(def.getId()!=null && prefixes.isKnownPrefix(def.getId())){
			List<String> expectedNs=prefixes.getUris(def.getId());
			if(!expectedNs.contains(service.getUriString(def))){
				warning("namespace <"+expectedNs+"> is expected", XturtlePackage.Literals.PREFIX_ID__ID);
			}
		}
		String uri = service.getUriString(def);
		if(uri!=null && prefixes.isKnownNameSpace(uri)){
			List<String> expectedPrefixes=prefixes.getPrefixes(uri);
			if(!expectedPrefixes.contains(def.getId())){
				warning("prefix '"+expectedPrefixes.get(0)+"' expected", XturtlePackage.Literals.PREFIX_ID__ID);
			}
		}
	}
}
