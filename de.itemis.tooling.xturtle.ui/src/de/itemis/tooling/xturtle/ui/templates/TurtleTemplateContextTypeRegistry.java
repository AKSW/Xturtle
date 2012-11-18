package de.itemis.tooling.xturtle.ui.templates;

import java.util.Collections;
import java.util.List;

import org.eclipse.xtext.IGrammarAccess;
import org.eclipse.xtext.ParserRule;
import org.eclipse.xtext.ui.editor.templates.ContextTypeIdHelper;
import org.eclipse.xtext.ui.editor.templates.XtextTemplateContextType;
import org.eclipse.xtext.ui.editor.templates.XtextTemplateContextTypeRegistry;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.itemis.tooling.xturtle.services.XturtleGrammarAccess;

@Singleton
public class TurtleTemplateContextTypeRegistry extends
		XtextTemplateContextTypeRegistry {

	@Inject
	public TurtleTemplateContextTypeRegistry(IGrammarAccess grammarAccess,
			Provider<XtextTemplateContextType> ctxTypeProvider,
			ContextTypeIdHelper helper) {
		super(grammarAccess, ctxTypeProvider, helper);
	}
	
	@Override
	protected void registerContextTypes(IGrammarAccess grammarAccess,
			Provider<XtextTemplateContextType> ctxTypeProvider) {
		XturtleGrammarAccess ga = (XturtleGrammarAccess)grammarAccess;
		List<XtextTemplateContextType> allContextTypes = Lists.newArrayList();
		allContextTypes.add(getType(ga.getDirectiveRule(), ctxTypeProvider));
		allContextTypes.add(getType(ga.getSubjectRule(), ctxTypeProvider));
		allContextTypes.add(getType(ga.getObjectRule(), ctxTypeProvider));
		allContextTypes.add(getType(ga.getTurtleDocRule(), ctxTypeProvider));
		Collections.sort(allContextTypes);
		for (XtextTemplateContextType templateContextType: allContextTypes) {
			addContextType(templateContextType);
		}
	}

	private XtextTemplateContextType getType(ParserRule rule, Provider<XtextTemplateContextType> ctxTypeProvider){
		XtextTemplateContextType type = ctxTypeProvider.get();
		type.setName(rule.getName());
		type.setId(getId(rule));
		return type;
	}
}
