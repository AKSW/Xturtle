package de.itemis.tooling.xturtle.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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

	//label names and description names are used to enrich the index with user data
	//that is used for code completion and hover information
	
	private static final Set<QualifiedName> labelNames;
	static{labelNames=new HashSet<QualifiedName>();
		labelNames.add(QualifiedName.create("http://www.w3.org/2004/02/skos/core#","prefLabel"));
		labelNames.add(QualifiedName.create("http://purl.org/dc/elements/1.1/","title"));
		labelNames.add(QualifiedName.create("http://purl.org/dc/terms/","title"));
		labelNames.add(QualifiedName.create("http://swrc.ontoware.org/ontology#","title"));
		labelNames.add(QualifiedName.create("http://xmlns.com/foaf/0.1/","name"));
		labelNames.add(QualifiedName.create( "http://usefulinc.com/ns/doap#","name"));
		labelNames.add(QualifiedName.create("http://rdfs.org/sioc/ns#","name"));
		labelNames.add(QualifiedName.create("http://www.holygoat.co.uk/owl/redwood/0.1/tags/","name"));
		labelNames.add(QualifiedName.create("http://linkedgeodata.org/vocabulary#","name"));
		labelNames.add(QualifiedName.create("http://www.geonames.org/ontology#","name"));
		labelNames.add(QualifiedName.create("http://www.geneontology.org/dtds/go.dtd#","name"));
		labelNames.add(QualifiedName.create("http://www.w3.org/2000/01/rdf-schema#","label"));
		labelNames.add(QualifiedName.create("http://xmlns.com/foaf/0.1/","accountName"));
		labelNames.add(QualifiedName.create("http://xmlns.com/foaf/0.1/","nick"));
		labelNames.add(QualifiedName.create("http://xmlns.com/foaf/0.1/","surname"));
		labelNames.add(QualifiedName.create("http://www.w3.org/2004/02/skos/core#","altLabel"));
	}
	private static final Set<QualifiedName> descriptionNames;
	static{descriptionNames=new HashSet<QualifiedName>();
		descriptionNames.add(QualifiedName.create("http://www.w3.org/2000/01/rdf-schema#","comment"));
		descriptionNames.add(QualifiedName.create("http://purl.org/dc/terms/","description"));
		descriptionNames.add(QualifiedName.create("http://purl.org/dc/elements/1.1/","description"));
		descriptionNames.add(QualifiedName.create("http://www.w3.org/2004/02/skos/core#","note"));
		descriptionNames.add(QualifiedName.create("http://www.w3.org/2004/02/skos/core#","editorialNote"));
	}
	
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
		List<String>labels=new ArrayList<String>();
		List<String>descriptions=new ArrayList<String>();
		for (PredicateObjectList predList : triples.getPredObjs()) {
			Predicate verb = predList.getVerb();
			QualifiedName verbName = service.getQualifiedName(verb);
			if(labelNames.contains(verbName)){
				labels.addAll(getStringLiteralValues(predList));
			}else if(descriptionNames.contains(verbName)){
				descriptions.addAll(getStringLiteralValues(predList));
			}
		}
		if(labels.size()>0){
			userData.put("label", Joiner.on(",,").join(labels));
		}
		if(descriptions.size()>0){
			userData.put("descr", Joiner.on(",,").join(descriptions));
		}
		return userData;
	}

	//currently we use only StringLiterals with no attached language and English
	private List<String> getStringLiteralValues(PredicateObjectList predList){
		List<String> result=new ArrayList<String>();
		List<StringLiteral> literals = EcoreUtil2.typeSelect(predList.getObjects(), StringLiteral.class);
		for (StringLiteral stringLiteral : literals) {
			String language = stringLiteral.getLanguage();
			if(language==null||"en".equals(language)){
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
