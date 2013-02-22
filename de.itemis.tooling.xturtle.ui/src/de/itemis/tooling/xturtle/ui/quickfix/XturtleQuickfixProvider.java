
package de.itemis.tooling.xturtle.ui.quickfix;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.ui.editor.model.edit.IModificationContext;
import org.eclipse.xtext.ui.editor.model.edit.ISemanticModification;
import org.eclipse.xtext.ui.editor.quickfix.DefaultQuickfixProvider;
import org.eclipse.xtext.ui.editor.quickfix.Fix;
import org.eclipse.xtext.ui.editor.quickfix.IssueResolutionAcceptor;
import org.eclipse.xtext.validation.Issue;

import com.google.common.collect.ObjectArrays;

import de.itemis.tooling.xturtle.services.Prefixes;
import de.itemis.tooling.xturtle.ui.validation.XturtleUIJavaValidator;
import de.itemis.tooling.xturtle.validation.XturtleJavaValidator;
import de.itemis.tooling.xturtle.xturtle.DirectiveBlock;
import de.itemis.tooling.xturtle.xturtle.PrefixId;
import de.itemis.tooling.xturtle.xturtle.XturtleFactory;

public class XturtleQuickfixProvider extends DefaultQuickfixProvider {

	@Inject
	private Prefixes prefixes;

	@Fix(XturtleJavaValidator.UNKNOWN_PREFIX)
	public void addPrefixDefinition(final Issue issue, IssueResolutionAcceptor acceptor) {
		if(issue.getData().length>0){
			final String linkText = issue.getData()[0];
			if (prefixes.isKnownPrefix(linkText)){
				final List<String> uris=prefixes.getUris(linkText);
				for (final String uri : uris) {
					acceptor.accept(issue, "add prefix "+linkText, "adds prefix "+linkText+"with namespace URI\n"+uri, null, new ISemanticModification() {
						
						public void apply(EObject element, IModificationContext context)
								throws Exception {
							DirectiveBlock block = (DirectiveBlock)EcoreUtil2.getRootContainer(element);
							PrefixId id = XturtleFactory.eINSTANCE.createPrefixId();
							id.setId(linkText);
							id.setUri(uri);
							if(block.getDirectives()==null){
								block.setDirectives(XturtleFactory.eINSTANCE.createDirectives());
							}
							block.getDirectives().getDirective().add(id);
						}
					});
				}
			}
		}
	}

	@Fix(XturtleUIJavaValidator.IMPORT_PROJECT)
	public void capitalizeName(final Issue issue, IssueResolutionAcceptor acceptor) {
		if(issue.getData().length>0 && issue.getUriToProblem().isPlatformResource()){
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			for (final String projectName : issue.getData()) {
				IProject project=root.getProject(projectName);
				if(project.exists()){
					acceptor.accept(issue, 
							"add project reference to "+projectName, 
							"adds project reference to projectName,\nmaking the subjects defined there referrable",
							null,
							new ISemanticModification() {
						
						public void apply(EObject element, IModificationContext context)
								throws Exception {
							IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
							IProject from = root.getProject(element.eResource().getURI().segment(1));
							IProject[] referenced = from.getDescription().getReferencedProjects();
							IProject to = root.getProject(projectName);
							referenced=ObjectArrays.concat(referenced, to);
							IProjectDescription desc = from.getDescription();
							desc.setReferencedProjects(referenced);
							IProgressMonitor monitor=new NullProgressMonitor();
							from.setDescription(desc, monitor);
//							from.refreshLocal(IResource.DEPTH_INFINITE, monitor);
						}
					});
				}
			}
		}
	}
}
