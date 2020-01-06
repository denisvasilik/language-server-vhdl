package com.eccelerators.plugins.vhdl.linking

import org.eclipse.xtext.linking.lazy.LazyLinker
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.diagnostics.IDiagnosticConsumer
import java.io.IOException
import org.eclipse.emf.common.util.URI
 
class VhdlLinker extends LazyLinker {

	override void beforeModelLinked(EObject model, IDiagnosticConsumer diagnosticsConsumer) {
		super.beforeModelLinked(model, diagnosticsConsumer);

		val res = model.eResource().getResourceSet().getResource(URI.createURI("com.eccelerators.plugins.vhdl.builtin:/inmemory.vhd"), false);
		if (res !== null) {
			try {
				res.delete(null);
			} catch (IOException e) {
			}
		}
	}
}
