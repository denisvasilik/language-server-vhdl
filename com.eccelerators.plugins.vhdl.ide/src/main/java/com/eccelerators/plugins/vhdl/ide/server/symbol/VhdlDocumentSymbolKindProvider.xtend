package com.eccelerators.plugins.vhdl.ide.server.symbol

import org.eclipse.xtext.ide.server.symbol.DocumentSymbolMapper.DocumentSymbolKindProvider
import org.eclipse.emf.ecore.EClass

import static org.eclipse.lsp4j.SymbolKind.*
import static com.eccelerators.plugins.vhdl.vhdl.VhdlPackage.Literals.ENTITY_DECLARATION
import static com.eccelerators.plugins.vhdl.vhdl.VhdlPackage.Literals.ARCHITECTURE_DECLARATION
import static com.eccelerators.plugins.vhdl.vhdl.VhdlPackage.Literals.COMPONENT_DECLARATION
import static com.eccelerators.plugins.vhdl.vhdl.VhdlPackage.Literals.COMPONENT_INSTANTIATION_STATEMENT
import static com.eccelerators.plugins.vhdl.vhdl.VhdlPackage.Literals.PROCESS_STATEMENT

class VhdlDocumentSymbolKindProvider extends DocumentSymbolKindProvider {

	override protected getSymbolKind(EClass clazz) {
		return switch (clazz) {
			case ENTITY_DECLARATION: Interface
			case ARCHITECTURE_DECLARATION: Class
			case COMPONENT_DECLARATION: Method
			case COMPONENT_INSTANTIATION_STATEMENT: Field
			case PROCESS_STATEMENT: Event
			default: Property
		}
	}

}