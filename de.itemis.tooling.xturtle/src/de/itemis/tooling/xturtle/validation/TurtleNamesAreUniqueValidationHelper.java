package de.itemis.tooling.xturtle.validation;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.validation.NamesAreUniqueValidationHelper;

public class TurtleNamesAreUniqueValidationHelper extends
		NamesAreUniqueValidationHelper {

	//elements are duplicate if they have the same fully qualified name
	//here we adapt name printed
	@Override
	public String getDuplicateNameErrorMessage(IEObjectDescription description,
			EClass clusterType, EStructuralFeature feature) {
		StringBuilder result = new StringBuilder(64);
		result.append("Duplicate ");
		result.append(getTypeLabel(clusterType));
		result.append(" '");
		result.append(description.getQualifiedName().toString(""));
		result.append("'");
		return result.toString();
	}
}
