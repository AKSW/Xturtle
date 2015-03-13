package de.itemis.tooling.xturtle;

import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.junit4.util.ParseHelper;

public class TurtleParseHelper<T extends EObject> extends ParseHelper<T> {

	private static AtomicInteger count=new AtomicInteger(0);
	@Override
	protected URI computeUnusedUri(ResourceSet resourceSet) {
		return URI.createURI("file://testFile"+count.incrementAndGet()+".ttl");
	}
}
