package de.itemis.tooling.xturtle.ui.preferences;

import javax.inject.Inject;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.xtext.diagnostics.Severity;

import de.itemis.tooling.xturtle.validation.TurtleValidationSeverityLevels;

public class TurtlePreferenceBasedValidationSeverityLevels implements
		TurtleValidationSeverityLevels, IPropertyChangeListener {

	IPreferenceStore prefernces;
	Severity unresolvedUri;
	Severity unresolvedQname;
	Severity prefixMismatch;
	Severity nsMismatch;
	Severity unusedPrefix;
	Severity xsdType;

	@Inject
	public TurtlePreferenceBasedValidationSeverityLevels(IPreferenceStore store) {
		prefernces=store;
		prefernces.addPropertyChangeListener(this);
		initValues();
	}

	public Severity getUnresolvedUriRefLevel() {
		return unresolvedUri;
	}

	public Severity getUnresolvedQNameLevel() {
		return unresolvedQname;
	}

	public Severity getPrefixMismatchLevel() {
		return prefixMismatch;
	}

	public Severity getNamespaceMismatchLevel() {
		return nsMismatch;
	}

	public Severity getUnusedPrefixLevel() {
		return unusedPrefix;
	}

	public Severity getXsdTypeLevel() {
		return xsdType;
	}

	public void propertyChange(PropertyChangeEvent event) {
		if(event.getProperty().contains(".validation.")){
			initValues();
		}
	}

	private void initValues() {
		unresolvedQname=getSeverity(TurtlePreferenceConstants.VALIDATION_UNRESOLVED_QNAME_KEY);
		unresolvedUri=getSeverity(TurtlePreferenceConstants.VALIDATION_UNRESOLVED_URI_KEY);
		unusedPrefix=getSeverity(TurtlePreferenceConstants.VALIDATION_UNUSED_PREFIX_KEY);
		nsMismatch=getSeverity(TurtlePreferenceConstants.VALIDATION_NS_MISMATCH_KEY);
		prefixMismatch=getSeverity(TurtlePreferenceConstants.VALIDATION_PREFIX_MISMATCH_KEY);
		xsdType=getSeverity(TurtlePreferenceConstants.VALIDATION_XSD_TYPE_KEY);
	}

	Severity getSeverity(String key){
		Severity result=null;
		String s=prefernces.getString(key);
		if("info".equals(s)){
			result=Severity.INFO;
		}else if("warn".equals(s)){
			result=Severity.WARNING;
		}else if("error".equals(s)){
			result=Severity.ERROR;
		}
		return result;
	}

}
