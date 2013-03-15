package de.itemis.tooling.xturtle.ui.preferences;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.naming.QualifiedName;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Singleton;

import de.itemis.tooling.xturtle.resource.TurtleIndexUserDataNamesProvider;

@Singleton
public class TurtlePreferenceBasedUserDataNamesProvider implements
		TurtleIndexUserDataNamesProvider, IPropertyChangeListener {

	private IQualifiedNameConverter nameConverter;
	private Set<QualifiedName> labelNames;
	private Set<QualifiedName> descriptionNames;
	private Set<String> internalLanguageList;
	private boolean useNullLangage;
	private boolean useDefaultLanguage;
	private Set<String> languageList;

	@Inject
	public TurtlePreferenceBasedUserDataNamesProvider(IPreferenceStore store, IQualifiedNameConverter nameConverter) {
		store.addPropertyChangeListener(this);
		this.nameConverter=nameConverter;
		initLabelNames(store.getString(TurtlePreferenceConstants.LABEL_PREFERENCE_KEY));
		intiDescriptionNames(store.getString(TurtlePreferenceConstants.DESCRIPTION_PREFERENCE_KEY));
		initInternalLangageList(store.getString(TurtlePreferenceConstants.LANGUAGES_PREFERENCE_KEY));
		useNullLangage=store.getBoolean(TurtlePreferenceConstants.USE_NOLANGUAGE_PREFERENCE_KEY);
		useDefaultLanguage=store.getBoolean(TurtlePreferenceConstants.USE_DEFAULT_LANGUAGE_PREFERENCE_KEY);
		reconcileLanguageList();
	}

	private void reconcileLanguageList() {
		languageList=new HashSet<String>(internalLanguageList);
		if(useDefaultLanguage){
			String nl = Platform.getNL();
			int underscoreIndex = nl.indexOf('_');
			if(underscoreIndex>=0){
				nl=nl.substring(0,underscoreIndex);
			}
			languageList.add(nl);
		}
		if(useNullLangage){
			languageList.add(null);
		}
		//TODO normalize language list (only language not country)
//		languageList=ImmutableSet.copyOf(languageList);
	}

	private void initInternalLangageList(String string) {
		internalLanguageList=new HashSet<String>(Arrays.asList(string.split(",,")));
		internalLanguageList.remove("");
	}

	private void intiDescriptionNames(String string) {
		descriptionNames=ImmutableSet.copyOf(getQnames(string));
	}

	private void initLabelNames(String string) {
		labelNames=ImmutableSet.copyOf(getQnames(string));
	}

	private Set<QualifiedName> getQnames(String string){
		Set<QualifiedName> names=new HashSet<QualifiedName>();
		if(string!=null){
			for (String line : string.split("\n")) {
				line=line.trim();
				if(line.length()>0){
					names.add(nameConverter.toQualifiedName(line));
				}
			}
		}
		return names;
	}

	public Set<QualifiedName> getLabelNames() {
		return labelNames;
	}

	public Set<QualifiedName> getDescriptionNames() {
		return descriptionNames;
	}

	public Set<String> getDescriptionLanguages() {
		return languageList;
	}

	public void propertyChange(PropertyChangeEvent event) {
		if(TurtlePreferenceConstants.LABEL_PREFERENCE_KEY.equals(event.getProperty())){
			initLabelNames((String)event.getNewValue());
		}else if(TurtlePreferenceConstants.DESCRIPTION_PREFERENCE_KEY.equals(event.getProperty())){
			intiDescriptionNames((String)event.getNewValue());
		}else if(TurtlePreferenceConstants.LANGUAGES_PREFERENCE_KEY.equals(event.getProperty())){
			initInternalLangageList((String)event.getNewValue());
			reconcileLanguageList();
		}else if(TurtlePreferenceConstants.USE_DEFAULT_LANGUAGE_PREFERENCE_KEY.equals(event.getProperty())){
			useDefaultLanguage=(Boolean)event.getNewValue();
			reconcileLanguageList();
		}else if(TurtlePreferenceConstants.USE_NOLANGUAGE_PREFERENCE_KEY.equals(event.getProperty())){
			useNullLangage=(Boolean)event.getNewValue();
			reconcileLanguageList();
		}
	}
}
