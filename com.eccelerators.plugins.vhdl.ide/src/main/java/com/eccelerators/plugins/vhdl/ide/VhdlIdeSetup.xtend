/*
 * generated by Xtext 2.19.0
 */
package com.eccelerators.plugins.vhdl.ide

import com.eccelerators.plugins.vhdl.VhdlRuntimeModule
import com.eccelerators.plugins.vhdl.VhdlStandaloneSetup
import com.google.inject.Guice
import org.eclipse.xtext.util.Modules2

/**
 * Initialization support for running Xtext languages as language servers.
 */
class VhdlIdeSetup extends VhdlStandaloneSetup {

	override createInjector() {
		Guice.createInjector(Modules2.mixin(new VhdlRuntimeModule, new VhdlIdeModule))
	}
	
}
