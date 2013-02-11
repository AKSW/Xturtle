
package de.itemis.tooling.xturtle.ui.quickfix;

import org.eclipse.xtext.ui.editor.quickfix.DefaultQuickfixProvider;

public class XturtleQuickfixProvider extends DefaultQuickfixProvider {

//	@Inject
//	private Prefixes prefixes;
//
//	@Fix(XturtleJavaValidator.UNKNOWN_PREFIX)
//	public void capitalizeName(final Issue issue, IssueResolutionAcceptor acceptor) {
//		if(issue.getData().length>0){
//			final String linkText = issue.getData()[0];
//			if (prefixes.isKnownPrefix(linkText)){
//				final String uri=prefixes.getUris(linkText).get(0);
//				acceptor.accept(issue, "add prefix "+linkText, "adds prefix", null, new ISemanticModification() {
//					
//					public void apply(EObject element, IModificationContext context)
//							throws Exception {
//						DirectiveBlock block = EcoreUtil2.getContainerOfType(element, DirectiveBlock.class);
//						PrefixId id = XturtleFactory.eINSTANCE.createPrefixId();
//						id.setId(linkText);
//						id.setUri(uri);
//						block.getDirecives().getDirective().add(id);
//						IProject p = ResourcesPlugin.getWorkspace().getRoot().getProject("projectName");
//						IProject[] referenced = p.getDescription().getReferencedProjects();
//						IProject p2 = ResourcesPlugin.getWorkspace().getRoot().getProject("otherProject");
//						referenced=ObjectArrays.concat(referenced, p2);
//						IProjectDescription desc = p.getDescription();
//						desc.setReferencedProjects(referenced);
//						IProgressMonitor monitor=new NullProgressMonitor();
//						p.setDescription(desc, monitor);
//						p.refreshLocal(IResource.DEPTH_INFINITE, monitor);
//					}
//				});
//			}
//		}
//	}
}
