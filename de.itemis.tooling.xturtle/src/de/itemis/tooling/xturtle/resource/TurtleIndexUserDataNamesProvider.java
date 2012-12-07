package de.itemis.tooling.xturtle.resource;

import java.util.Set;

import org.eclipse.xtext.naming.QualifiedName;

public interface TurtleIndexUserDataNamesProvider {

	Set<QualifiedName> getLabelNames();
	Set<QualifiedName> getDescriptionNames();
	Set<String> getDescriptionLanguages();
}
