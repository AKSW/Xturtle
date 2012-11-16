package de.itemis.tooling.xturtle.resource;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.IFragmentProvider;

import com.google.inject.Inject;

public class TurtleFragmentProvider implements IFragmentProvider {

	@Inject 
	TurtleResourceService service;
	
	public String getFragment(EObject obj, Fallback fallback) {
		String result = service.getFragment(obj);
		if(result==null){
			result=fallback.getFragment(obj);
		} 
		return result;
	}

	public EObject getEObject(Resource resource, String fragment,
			Fallback fallback) {
		EObject result = service.getObject(resource,fragment);
		if(result==null){
			result=fallback.getEObject(fragment);
		}
		return result;
	}
}
