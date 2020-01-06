package com.eccelerators.plugins.vhdl
//import org.eclipse.xtext.linking.ILinker;
//import org.eclipse.xtext.linking.ILinkingDiagnosticMessageProvider;

import com.eccelerators.plugins.vhdl.AbstractVhdlRuntimeModule;
import com.eccelerators.plugins.vhdl.linking.VhdlLinkingService
import com.eccelerators.plugins.vhdl.linking.VhdlLinker
import org.eclipse.xtext.linking.ILinkingService

//import com.eccelerators.plugins.vhdl.linking.VhdlLinkingDiagnosticMessageProvider;

/**
 * Use this class to register components to be used at runtime / without the Equinox extension registry.
 */
class VhdlRuntimeModule extends AbstractVhdlRuntimeModule {
	// contributed by org.eclipse.xtext.generator.scoping.AbstractScopingFragment
	override void configureIScopeProviderDelegate(com.google.inject.Binder binder) {
		binder.bind(typeof(org.eclipse.xtext.scoping.IScopeProvider))
			  .annotatedWith(com.google.inject.name.Names.named(org.eclipse.xtext.scoping.impl.AbstractDeclarativeScopeProvider.NAMED_DELEGATE))
			  .to(typeof(com.eccelerators.plugins.vhdl.scoping.VhdlImportedNamespaceAwareLocalScopeProvider));
	}
	
	override Class<? extends org.eclipse.xtext.scoping.IScopeProvider> bindIScopeProvider() {
		return typeof(com.eccelerators.plugins.vhdl.scoping.VhdlScopeProvider);
	}
	
	override Class<? extends ILinkingService> bindILinkingService() {
		return typeof(VhdlLinkingService);
	}
	
	override Class<? extends org.eclipse.xtext.linking.ILinker> bindILinker() {
		return typeof(VhdlLinker);
	}
	
//	@Override 
//	public Class<? extends ILinkingDiagnosticMessageProvider.Extended> bindILinkingDiagnosticMessageProvider() {
//		return VhdlLinkingDiagnosticMessageProvider.class;
//	}
}
