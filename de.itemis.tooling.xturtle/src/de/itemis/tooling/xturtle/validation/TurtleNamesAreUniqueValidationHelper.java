package de.itemis.tooling.xturtle.validation;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.validation.NamesAreUniqueValidationHelper;
import org.eclipse.xtext.validation.ValidationMessageAcceptor;

import com.google.inject.Inject;

public class TurtleNamesAreUniqueValidationHelper extends
		NamesAreUniqueValidationHelper {

	@Inject
	private TurtleValidationSeverityLevels levels;

	@Override
	public void checkUniqueNames(Iterable<IEObjectDescription> descriptions,
			CancelIndicator cancelIndicator, ValidationMessageAcceptor acceptor) {
		if(levels.getDuplicateSubjectLevel()!=null){
			super.checkUniqueNames(descriptions, cancelIndicator, acceptor);
		}
	}

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

	@Override
	protected void createDuplicateNameError(IEObjectDescription description,
			EClass clusterType, ValidationMessageAcceptor acceptor) {
		EObject object = description.getEObjectOrProxy();
		EStructuralFeature feature = getNameFeature(object);
		String message=getDuplicateNameErrorMessage(description, clusterType, feature);
		int index=ValidationMessageAcceptor.INSIGNIFICANT_INDEX;
		switch (levels.getDuplicateSubjectLevel()) {
		case ERROR:
			acceptor.acceptError(message, object, feature, index, null);
			break;
		case WARNING:
			acceptor.acceptWarning(message, object, feature, index, null);
			break;
		case INFO:
			acceptor.acceptInfo(message, object, feature, index, null);
			break;
		default:
			break;
		}
	}
}
