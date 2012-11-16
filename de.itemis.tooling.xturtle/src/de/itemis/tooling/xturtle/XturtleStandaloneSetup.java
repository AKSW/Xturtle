
package de.itemis.tooling.xturtle;

/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class XturtleStandaloneSetup extends XturtleStandaloneSetupGenerated{

	public static void doSetup() {
		new XturtleStandaloneSetup().createInjectorAndDoEMFRegistration();
	}
}

