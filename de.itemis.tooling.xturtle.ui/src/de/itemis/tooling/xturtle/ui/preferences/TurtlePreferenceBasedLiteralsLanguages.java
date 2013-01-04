package de.itemis.tooling.xturtle.ui.preferences;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

import com.google.common.collect.ImmutableList;

import de.itemis.tooling.xturtle.ui.contentassist.TurtleLiteralsLanguages;

public class TurtlePreferenceBasedLiteralsLanguages implements
		TurtleLiteralsLanguages, IPropertyChangeListener {

	IPreferenceStore prefernces;
	List<String> languages;

	@Inject
	public TurtlePreferenceBasedLiteralsLanguages(IPreferenceStore store) {
		prefernces=store;
		prefernces.addPropertyChangeListener(this);
		initValues();
	}


	public void propertyChange(PropertyChangeEvent event) {
		if(event.getProperty().equals(TurtlePreferenceConstants.CA_LANGUAGES_KEY)){
			initValues();
		}
	}

	private void initValues() {
		languages=ImmutableList.copyOf(prefernces.getString(TurtlePreferenceConstants.CA_LANGUAGES_KEY).split(",,"));
	}


	public List<String> getLanguagesToPropose() {
		return languages;
	}
}
