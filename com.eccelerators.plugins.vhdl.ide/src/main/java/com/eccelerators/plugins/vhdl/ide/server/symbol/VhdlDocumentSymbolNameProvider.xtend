package com.eccelerators.plugins.vhdl.ide.server.symbol

import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.ide.server.symbol.DocumentSymbolMapper.DocumentSymbolNameProvider
import com.eccelerators.plugins.vhdl.vhdl.EntityDeclaration
import com.eccelerators.plugins.vhdl.vhdl.ArchitectureDeclaration
import com.eccelerators.plugins.vhdl.vhdl.ComponentDeclaration
import com.eccelerators.plugins.vhdl.vhdl.ComponentInstantiationStatement

class VhdlDocumentSymbolNameProvider extends DocumentSymbolNameProvider {

	override getName(EObject object) {
		if (object instanceof EntityDeclaration) {
			val entityDeclaration = object as EntityDeclaration
			return entityDeclaration.name;
		} else if(object instanceof ArchitectureDeclaration) {
			val architectureDeclaration = object as ArchitectureDeclaration
			return architectureDeclaration.name
		} else if(object instanceof ComponentDeclaration) {
			val componentDeclaration = object as ComponentDeclaration
			return componentDeclaration.name
		} else if(object instanceof ComponentInstantiationStatement) {
			val componentInstantiationStatement = object as ComponentInstantiationStatement
			return componentInstantiationStatement.name
		}
		
		return super.getName(object);
	}

}
