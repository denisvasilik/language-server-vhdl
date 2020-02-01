/*******************************************************************************
 * Copyright (c) 2016, 2017 TypeFox GmbH (http://www.typefox.io) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.eccelerators.plugins.vhdl.ide.server.symbol

import com.google.inject.Inject
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.findReferences.IReferenceFinder.IResourceAccess
import org.eclipse.xtext.ide.server.DocumentExtensions
import org.eclipse.xtext.ide.server.symbol.DocumentSymbolService
import org.eclipse.xtext.resource.EObjectAtOffsetHelper
import org.eclipse.xtext.resource.XtextResource
import org.eclipse.xtext.util.CancelIndicator
import org.eclipse.xtext.util.TextRegion

class VhdlDocumentSymbolService extends DocumentSymbolService {
	
	@Inject extension DocumentExtensions 
	@Inject EObjectAtOffsetHelper helper
	
	override getDefinitions(XtextResource resource, int offset, IResourceAccess resourceAccess, CancelIndicator cancelIndicator) {
		val node = helper.getCrossReferenceNode(resource, new TextRegion(offset,0))
		if (node !== null) {
			val element = helper.getCrossReferencedElement(node)
			if (element !== null) {
				return #[element.newFullLocation]
			}
		}
		return emptyList
	}
	
	protected override void doRead(IResourceAccess resourceAccess, URI objectURI, (EObject)=>void acceptor) {
		if(objectURI.toString().contains("com.eccelerators.plugins.vhdl")) {
			return;
		}
		
		resourceAccess.readOnly(objectURI) [ resourceSet |
			val object = resourceSet.getEObject(objectURI, true)
			if (object !== null) {
				acceptor.apply(object)
			}
			return null
		]
	}
	
}
