package de.itemis.tooling.xturtle.ui.validation;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.resource.impl.ResourceDescriptionsProvider;
import org.eclipse.xtext.validation.Check;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import de.itemis.tooling.xturtle.validation.XturtleJavaValidator;
import de.itemis.tooling.xturtle.xturtle.PrefixId;
import de.itemis.tooling.xturtle.xturtle.XturtlePackage;

public class XturtleUIJavaValidator extends XturtleJavaValidator {

	public static final String IMPORT_PROJECT="importProject";

	@Inject
	private ResourceDescriptionsProvider index;

	@Check
	public void checkPrefixCC(PrefixId def) {
		IResourceDescriptions theIndex = index.getResourceDescriptions(def.eResource());
		Iterable<IEObjectDescription> triples = theIndex.getExportedObjectsByType(XturtlePackage.Literals.TRIPLES);
		QualifiedName myName = getService().getQualifiedName(def);
		List<IProject> matchingProject=Lists.newArrayList();
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		for (IEObjectDescription triple : triples) {
			if(myName.equals(triple.getQualifiedName()) && triple.getEObjectURI().isPlatformResource())
				matchingProject.add(root.getProject(triple.getEObjectURI().segment(1)));
		}
		
		if(def.eResource().getURI().isPlatformResource()){
			String project = def.eResource().getURI().segment(1);
			IProject theProject = root.getProject(project);
			try {
				IProject[] referencedProjects = theProject.getReferencedProjects();
				Set<IProject> referenced= Sets.newHashSet(referencedProjects);
				List<String> suggestedImports=Lists.newArrayList();
				for (IProject iProject : matchingProject) {
					if(!theProject.equals(iProject) && !referenced.contains(iProject)){
						suggestedImports.add(iProject.getName());
					}
				}
				if(!suggestedImports.isEmpty()){
					info("you might want to import "+suggestedImports, XturtlePackage.Literals.PREFIX_ID__ID,IMPORT_PROJECT,suggestedImports.toArray(new String[0]));
				}
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//		service.g
//		Severity severity=levels.getNamespaceMismatchLevel();
//		if(severity!=null){
//			if(def.getId()!=null && prefixes.isKnownPrefix(def.getId())){
//				List<String> expectedNs=prefixes.getUris(def.getId());
//				if(!expectedNs.contains(service.getUriString(def))){
//					createError(severity, "Namespace <"+expectedNs+"> is recommended by prefix.cc", XturtlePackage.Literals.PREFIX_ID__ID);
//				}
//			}
//		}
//
//		severity=levels.getPrefixMismatchLevel();
//		if(severity!=null){
//			String uri = service.getUriString(def);
//			if(uri!=null && prefixes.isKnownNameSpace(uri)){
//				List<String> expectedPrefixes=prefixes.getPrefixes(uri);
//				if(def.getId()!=null && !expectedPrefixes.contains(def.getId())){
//					createError(severity,"Prefix '"+expectedPrefixes.get(0)+"' is recommended by prefix.cc", XturtlePackage.Literals.PREFIX_ID__ID);
//				}
//			}
//		}
	}
}
