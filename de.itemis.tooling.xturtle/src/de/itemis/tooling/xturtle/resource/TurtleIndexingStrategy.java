package de.itemis.tooling.xturtle.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IReferenceDescription;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.resource.impl.DefaultResourceDescriptionStrategy;
import org.eclipse.xtext.resource.impl.ResourceDescriptionsProvider;
import org.eclipse.xtext.util.IAcceptor;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;

import de.itemis.tooling.xturtle.xturtle.Predicate;
import de.itemis.tooling.xturtle.xturtle.PredicateObjectList;
import de.itemis.tooling.xturtle.xturtle.Resource;
import de.itemis.tooling.xturtle.xturtle.ResourceRef;
import de.itemis.tooling.xturtle.xturtle.StringLiteral;
import de.itemis.tooling.xturtle.xturtle.Subject;
import de.itemis.tooling.xturtle.xturtle.Triples;
import de.itemis.tooling.xturtle.xturtle.XturtlePackage;

public class TurtleIndexingStrategy extends DefaultResourceDescriptionStrategy {
	@Inject 
	TurtleResourceService service;

	@Inject
	private ResourceDescriptionsProvider resourceDescriptionsProvider;

	@com.google.inject.Inject(optional=true)
	private TurtleIndexUserDataNamesProvider namesProvider;	

	//enrich object description with label and description information of the given Subject
	@Override
	public boolean createEObjectDescriptions(EObject eObject,
			IAcceptor<IEObjectDescription> acceptor) {
		if(eObject instanceof Resource){
			QualifiedName qualifiedName = getQualifiedNameProvider().getFullyQualifiedName(eObject);
			if(qualifiedName!=null){
				Triples triples=(Triples)eObject.eContainer();
				Map<String,String> userData=getUserData(triples); 
				IEObjectDescription desc=new TurtleEObjectDescription(qualifiedName, eObject,userData);
				acceptor.accept(desc);
			}
			return false;
		}
		return true;
	}

	private Map<String, String> getUserData(Triples triples) {
		Map<String,String> userData=new HashMap<String,String>();
		if(namesProvider!=null){
			List<String>labels=new ArrayList<String>();
			List<String>descriptions=new ArrayList<String>();
			for (PredicateObjectList predList : triples.getPredObjs()) {
				Predicate verb = predList.getVerb();
				QualifiedName verbName = service.getQualifiedName(verb);
				if(namesProvider.getLabelNames().contains(verbName)){
					labels.addAll(getStringLiteralValues(predList,Optional.<Set<String>>absent()));
				}else if(namesProvider.getDescriptionNames().contains(verbName)){
					descriptions.addAll(getStringLiteralValues(predList,Optional.of(namesProvider.getDescriptionLanguages())));
				}
			}
			if(labels.size()>0){
				userData.put("label", Joiner.on(",,").join(labels));
			}
			if(descriptions.size()>0){
				userData.put("descr", Joiner.on(",,").join(descriptions));
			}
		}
		return userData;
	}

	private List<String> getStringLiteralValues(PredicateObjectList predList, Optional<Set<String>> languages){
		List<String> result=new ArrayList<String>();
		List<StringLiteral> literals = EcoreUtil2.typeSelect(predList.getObjects(), StringLiteral.class);
		for (StringLiteral stringLiteral : literals) {
			boolean doAdd=true;
			if(languages.isPresent()){
				String language = stringLiteral.getLanguage();
				doAdd=languages.get().contains(language);
			}
			if(doAdd){
				result.add(stringLiteral.getValue());
			}
		}
		return result;
	}

	@Override
	public boolean createReferenceDescriptions(final EObject from,
			URI exportedContainerURI, IAcceptor<IReferenceDescription> acceptor) {
		if(from instanceof ResourceRef){
			//add to the index pointers to ALL external subjects that have the given qualified name
			Resource ref = ((ResourceRef) from).getRef();
			if(ref !=null && !ref.eIsProxy()){
				URI fromUri=from.eResource().getURI();
				URI containerUri = getExportedSubjectUri(from);
				QualifiedName name = getQualifiedNameProvider().getFullyQualifiedName(ref);
				IResourceDescriptions index = resourceDescriptionsProvider.getResourceDescriptions(from.eResource().getResourceSet());
				Iterable<IEObjectDescription> matches = index.getExportedObjectsByType(XturtlePackage.Literals.RESOURCE);//, name, false);
				for (IEObjectDescription desc : matches) {
					if(desc.getQualifiedName().equals(name) &&!fromUri.equals(desc.getEObjectURI().trimFragment())){
						acceptor.accept(new TurtleReferenceDescription(from,desc,containerUri));
					}
				}
			}
			return false;
		}
		return super.createReferenceDescriptions(from, exportedContainerURI, acceptor);
//		return false;
	}

	//Technically, the subject of the triple is not a container of the reference,
	//however, the subject is exported and represents (the name) of the triple.
	//Using its URI in the reference description ensures that "find references" gives 
	//a (more) meaningful label in the search result view 
	private URI getExportedSubjectUri(EObject ref) {
		Triples triples = EcoreUtil2.getContainerOfType(ref, Triples.class);
		if(triples!=null){
			Subject subject = triples.getSubject();
			if(subject instanceof Resource){
				org.eclipse.emf.ecore.resource.Resource res = subject.eResource();
				return res.getURI().appendFragment(res.getURIFragment(subject));
			}
		}
		return null;
	}
}
