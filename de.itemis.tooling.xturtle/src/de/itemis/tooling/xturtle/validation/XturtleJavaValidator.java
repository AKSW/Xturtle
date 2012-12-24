package de.itemis.tooling.xturtle.validation;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.diagnostics.Severity;
import org.eclipse.xtext.validation.Check;

import com.google.inject.Inject;

import de.itemis.tooling.xturtle.resource.TurtleResourceService;
import de.itemis.tooling.xturtle.services.Prefixes;
import de.itemis.tooling.xturtle.xturtle.Directive;
import de.itemis.tooling.xturtle.xturtle.Directives;
import de.itemis.tooling.xturtle.xturtle.PrefixId;
import de.itemis.tooling.xturtle.xturtle.QNameDef;
import de.itemis.tooling.xturtle.xturtle.QNameRef;
import de.itemis.tooling.xturtle.xturtle.XturtlePackage;
 

public class XturtleJavaValidator extends AbstractXturtleJavaValidator {
	@Inject 
	private Prefixes prefixes;
	@Inject 
	private TurtleResourceService service;
	@Inject
	private TurtleValidationSeverityLevels levels;

	@Check
	public void checkEmptyPrefixDefined(QNameDef def) {
		if(def.getPrefix()==null && service.getQualifiedName(def)==null){
			error("no @prefix-Definition up to this point", XturtlePackage.Literals.QNAME_DEF__PREFIX);
		}
	}
	@Check
	public void checkEmptyPrefixDefined(QNameRef ref) {
		if(ref.getPrefix()==null && service.getQualifiedName(ref)==null){
			error("no @prefix-Definition up to this point", XturtlePackage.Literals.QNAME_REF__PREFIX);
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
		Severity severity=levels.getNamespaceMismatchLevel();
		if(severity!=null){
			if(def.getId()!=null && prefixes.isKnownPrefix(def.getId())){
				List<String> expectedNs=prefixes.getUris(def.getId());
				if(!expectedNs.contains(service.getUriString(def))){
					createError(severity, "Namespace <"+expectedNs+"> is recommended by prefix.cc", XturtlePackage.Literals.PREFIX_ID__ID);
				}
			}
		}

		severity=levels.getPrefixMismatchLevel();
		if(severity!=null){
			String uri = service.getUriString(def);
			if(uri!=null && prefixes.isKnownNameSpace(uri)){
				List<String> expectedPrefixes=prefixes.getPrefixes(uri);
				if(def.getId()!=null && !expectedPrefixes.contains(def.getId())){
					createError(severity,"Prefix '"+expectedPrefixes.get(0)+"' is recommended by prefix.cc", XturtlePackage.Literals.PREFIX_ID__ID);
				}
			}
		}
	}

	@Check
	public void checkUnusedPrefix(PrefixId def) {
		Severity s=levels.getUnusedPrefixLevel();
		if(s!=null){
			if(def.getId()!=null){
				Collection<Setting> candidates = EcoreUtil.UsageCrossReferencer.find(def, def.eResource());
				if(candidates.size()==0){
					createError(s, "unused prefix", XturtlePackage.Literals.PREFIX_ID__ID);
				}
			}else{
				//TODO currently no validation for unused empty prefixes
			}
		}
	}

	@Check
	public void checkDuplicatePrefixInDirectives(Directives directives){
		Map<String,PrefixId> firstOccurence=new HashMap<String,PrefixId>();
		Set<String> duplicatePrefixes=new HashSet<String>();
		for (PrefixId prefixId : EcoreUtil2.typeSelect(directives.getDirective(), PrefixId.class)) {
			String prefix=prefixId.getId();
			if(firstOccurence.containsKey(prefix)){
				duplicatePrefixes.add(prefix);
				error("duplicate prefix id", prefixId, XturtlePackage.Literals.PREFIX_ID__ID,-1);
			}else{
				firstOccurence.put(prefix, prefixId);
			}
		}
		for (String string : duplicatePrefixes) {
			error("duplicate prefix id",firstOccurence.get(string), XturtlePackage.Literals.PREFIX_ID__ID,-1);
		}
	}

	private void createError(Severity s, String errorMEssage, EStructuralFeature feature){
		switch (s) {
		case ERROR:
			error(errorMEssage, feature);
			break;
		case WARNING:
			warning(errorMEssage, feature);
			break;
		case INFO:
			info(errorMEssage, feature);
			break;
		default:
			break;
		}
	}
}
