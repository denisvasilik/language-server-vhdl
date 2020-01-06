package com.eccelerators.plugins.vhdl

/**
 * Initialization support for running Xtext languages without Equinox extension registry.
 */
class VhdlStandaloneSetup extends VhdlStandaloneSetupGenerated {

	def static void doSetup() {
		new VhdlStandaloneSetup().createInjectorAndDoEMFRegistration()
	}
}
