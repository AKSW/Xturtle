package de.itemis.tooling.xturtle.resource;

import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.EObjectDescription;

import de.itemis.tooling.xturtle.xturtle.Resource;
import de.itemis.tooling.xturtle.xturtle.XturtlePackage;

class TurtleEObjectDescription extends EObjectDescription {

	private boolean isResource;
	TurtleEObjectDescription(QualifiedName qualifiedName,
			EObject element, Map<String, String> userData) {
		super(qualifiedName, element, userData);
		isResource=(element instanceof Resource);
	}

	//we actually don't care whether the definition is via QName or URI
	//in case other Xtext languages are present, we want to find exported elements
	//as resources (in particular when using the Open Model Elements dialog)
	@Override
	public EClass getEClass() {
		if(isResource){
			return XturtlePackage.Literals.RESOURCE;
		}
		return super.getEClass();
	}

}
