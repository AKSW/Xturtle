/*******************************************************************************
 * Copyright (c) 2013 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle.validation;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtext.diagnostics.Severity;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.validation.IssueSeveritiesProvider;
import org.eclipse.xtext.validation.NamesAreUniqueValidationHelper;
import org.eclipse.xtext.validation.ValidationMessageAcceptor;

import com.google.inject.Inject;

public class TurtleNamesAreUniqueValidationHelper extends
		NamesAreUniqueValidationHelper {

	@Inject
	private IssueSeveritiesProvider levels;

	@Override
	public void checkUniqueNames(Iterable<IEObjectDescription> descriptions,
			CancelIndicator cancelIndicator, ValidationMessageAcceptor acceptor) {
		IEObjectDescription first = descriptions.iterator().next();
		if(first!=null){
			Severity severity=levels.getIssueSeverities(first.getEObjectOrProxy().eResource()).getSeverity(TurtleIssueCodes.VALIDATION_DUPLICATE_SUBJECT_KEY);
			if(severity!=Severity.IGNORE){
				super.checkUniqueNames(descriptions, cancelIndicator, acceptor);
			}
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
		Severity severity=levels.getIssueSeverities(object.eResource()).getSeverity(TurtleIssueCodes.VALIDATION_DUPLICATE_SUBJECT_KEY);

		switch (severity) {
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
