package com.eccelerators.plugins.vhdl.ide.server.symbol

import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.ide.server.symbol.HierarchicalDocumentSymbolService
import org.eclipse.emf.ecore.util.EcoreUtil
import com.eccelerators.plugins.vhdl.vhdl.Model
import com.eccelerators.plugins.vhdl.vhdl.EntityDeclaration
import com.eccelerators.plugins.vhdl.vhdl.ArchitectureDeclaration
import org.eclipse.xtext.resource.XtextResource
import org.eclipse.xtext.util.CancelIndicator
import org.eclipse.xtext.ide.server.Document
import org.eclipse.lsp4j.DocumentSymbolParams
import com.google.inject.Inject
import org.eclipse.xtext.ide.server.symbol.DocumentSymbolMapper
import org.eclipse.xtext.service.OperationCanceledManager
import org.eclipse.lsp4j.jsonrpc.messages.Either
import org.eclipse.lsp4j.SymbolInformation
import org.eclipse.lsp4j.DocumentSymbol
import com.eccelerators.plugins.vhdl.vhdl.ComponentDeclaration
import com.eccelerators.plugins.vhdl.vhdl.ComponentInstantiationStatement

class VhdlHierarchicalDocumentSymbolService extends HierarchicalDocumentSymbolService {

	@Inject
	DocumentSymbolMapper symbolMapper;

	@Inject
	OperationCanceledManager operationCanceledManager;

	override getSymbols(XtextResource resource, CancelIndicator cancelIndicator) {
		val rootSymbols = newArrayList;
		var entityParentSymbol = null as DocumentSymbol;
		var architectureParentSymbol = null as DocumentSymbol;
		var componentParentSymbol = null as DocumentSymbol;
		val itr = getAllContents(resource);
		while (itr.hasNext) {
			operationCanceledManager.checkCanceled(cancelIndicator);
			val next = itr.next.toEObject;
			if (next.present) {
				val object = next.get;
				var symbol = symbolMapper.toDocumentSymbol(object);
				if (symbol.valid) {
					if (object instanceof EntityDeclaration) {
						entityParentSymbol = symbol;
						rootSymbols.add(symbol);
					} else if(object instanceof ArchitectureDeclaration) {
						architectureParentSymbol = symbolMapper.toDocumentSymbol(object);
						entityParentSymbol.children.add(architectureParentSymbol) 
					} else if(object instanceof ComponentDeclaration) {
						componentParentSymbol = symbol
						architectureParentSymbol.children.add(symbol);
					} else if(object instanceof ComponentInstantiationStatement) {
						componentParentSymbol.children.add(symbol)
					}
				}
			}
		}
		return rootSymbols.map[Either.<SymbolInformation, DocumentSymbol>forRight(it)];
	}

	override protected getAllContents(Resource resource) {
		val module = resource.contents.head;
		
		if (module instanceof Model) {
			val allStatments = EcoreUtil.getAllProperContents(resource, true);
			return allStatments.filter[statement|
			      return statement instanceof EntityDeclaration || 
			      		 statement instanceof ArchitectureDeclaration ||
			      		 statement instanceof ComponentDeclaration ||
			      		 statement instanceof ComponentInstantiationStatement;
			]
		}
		
		return emptyList.iterator;
	}

}