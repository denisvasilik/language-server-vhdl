package com.eccelerators.plugins.vhdl.scoping;

import java.util.List;

import javax.management.RuntimeErrorException;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.Scopes;
import org.eclipse.xtext.scoping.impl.SimpleScope;
import org.eclipse.xtext.util.SimpleAttributeResolver;

import com.eccelerators.plugins.vhdl.vhdl.ActualPart;
import com.eccelerators.plugins.vhdl.vhdl.ArchitectureDeclaration;
import com.eccelerators.plugins.vhdl.vhdl.ComponentDeclaration;
import com.eccelerators.plugins.vhdl.vhdl.ComponentIdentifier;
import com.eccelerators.plugins.vhdl.vhdl.ComponentInstantiationStatement;
import com.eccelerators.plugins.vhdl.vhdl.Declaration;
import com.eccelerators.plugins.vhdl.vhdl.DeclaredIdentifiers;
import com.eccelerators.plugins.vhdl.vhdl.DeclaredSignalIdentifiers;
import com.eccelerators.plugins.vhdl.vhdl.DesignUnit;
import com.eccelerators.plugins.vhdl.vhdl.EntityDeclaration;
import com.eccelerators.plugins.vhdl.vhdl.EntityIdentifier;
import com.eccelerators.plugins.vhdl.vhdl.ExpressionDeclaration;
import com.eccelerators.plugins.vhdl.vhdl.FullTypeDeclaration;
import com.eccelerators.plugins.vhdl.vhdl.GenericAssociationElement;
import com.eccelerators.plugins.vhdl.vhdl.GenericIdentifier;
import com.eccelerators.plugins.vhdl.vhdl.GenericIdentifierReference;
import com.eccelerators.plugins.vhdl.vhdl.GenericInterfaceDeclaration;
import com.eccelerators.plugins.vhdl.vhdl.GenericMapAspect;
import com.eccelerators.plugins.vhdl.vhdl.Identifier;
import com.eccelerators.plugins.vhdl.vhdl.IdentifierReference;
import com.eccelerators.plugins.vhdl.vhdl.InterfaceDeclaration;
import com.eccelerators.plugins.vhdl.vhdl.InterfaceList;
import com.eccelerators.plugins.vhdl.vhdl.PackageBody;
import com.eccelerators.plugins.vhdl.vhdl.PackageDeclaration;
import com.eccelerators.plugins.vhdl.vhdl.ParameterizedTypeIdentifierReference;
import com.eccelerators.plugins.vhdl.vhdl.PortIdentifier;
import com.eccelerators.plugins.vhdl.vhdl.PortIdentifierReference;
import com.eccelerators.plugins.vhdl.vhdl.PortMapAspect;
import com.eccelerators.plugins.vhdl.vhdl.ProcedureIdentifier;
import com.eccelerators.plugins.vhdl.vhdl.ProcessStatement;
import com.eccelerators.plugins.vhdl.vhdl.QualifiedIdentifierReference;
import com.eccelerators.plugins.vhdl.vhdl.RecordTypeDefinition;
import com.eccelerators.plugins.vhdl.vhdl.RecordTypeIdentifier;
import com.eccelerators.plugins.vhdl.vhdl.ScalarTypeIdentifier;
import com.eccelerators.plugins.vhdl.vhdl.SignalDeclaration;
import com.eccelerators.plugins.vhdl.vhdl.SignalOrConstantIdentifier;
import com.eccelerators.plugins.vhdl.vhdl.TypeIdentifier;
import com.eccelerators.plugins.vhdl.vhdl.TypeIdentifierReference;
import com.eccelerators.plugins.vhdl.vhdl.VariableAssignmentStatement;
import com.eccelerators.plugins.vhdl.vhdl.VhdlFactory;
import com.eccelerators.plugins.vhdl.vhdl.Model;

public class VhdlScopeProvider extends org.eclipse.xtext.scoping.impl.AbstractDeclarativeScopeProvider { 
	
	/***
	 * Ensures that the ref feature of the EntityDeclaration does only refer to its own entity 
	 * and not to one declared elsewhere.
	 * 
	 * @param entityDeclaration The entity used to refer to.
	 * @param ref Reference to the current entity.
	 * @return The current entity.
	 */
	protected IScope scope_EntityDeclaration_ref(EntityDeclaration entityDeclaration, EReference ref) {
		EList<EObject> scope = new BasicEList<EObject>();
		scope.add(entityDeclaration);
		return scopeFor(scope, true);
	}
	
	protected IScope scope_ArchitectureDeclaration_ref(ArchitectureDeclaration architectureDeclaration, EReference ref) {
		EList<EObject> scope = new BasicEList<EObject>();
		scope.add(architectureDeclaration);
		return scopeFor(scope, true);
	}
	
	protected IScope scope_ArchitectureDeclaration_entityRef(ArchitectureDeclaration architectureDeclaration, EReference ref) {
		Model model = (Model) architectureDeclaration.eResource().getContents().get(0);
		EList<EObject> scope = new BasicEList<EObject>();
		for(DesignUnit designUnit : model.getDesignUnits()) {
			scope.add(designUnit.getLibraryUnit());
		}
		return scopeFor(scope, true);
	}
	
	protected IScope scope_PackageDeclaration_ref(PackageDeclaration packageDeclaration, EReference ref) {
		EList<EObject> scope = new BasicEList<EObject>();
		scope.add(packageDeclaration);
		return scopeFor(scope, true);
	}
	
	protected IScope scope_PackageBody_ref(PackageBody packageBody, EReference ref) {
		EList<EObject> scope = new BasicEList<EObject>();
		PackageDeclaration packageDeclaration = packageBody.getName();
		scope.add(packageDeclaration);
		return scopeFor(scope, true);
	}
	 
	protected IScope scope_ProcessStatement_ref(ProcessStatement processStatement, EReference ref) {
		EList<EObject> scope = new BasicEList<EObject>();
		scope.add(processStatement);
		return scopeFor(scope, true);
	}
	 
	protected IScope scope_RecordTypeIdentifier_ref(RecordTypeIdentifier recordTypeIdentifier, EReference ref) {		
		EList<EObject> scope = new BasicEList<EObject>();
		scope.add(recordTypeIdentifier);
		return scopeFor(scope, true);
	}
	
	protected IScope scope_ScalarTypeIdentifier_ref(ScalarTypeIdentifier scalarTypeIdentifier, EReference ref) {		
		EList<EObject> scope = new BasicEList<EObject>();
		scope.add(scalarTypeIdentifier);
		return scopeFor(scope, true);
	}
	
	protected IScope scope_ComponentDeclaration_ref(ComponentDeclaration componentDeclaration, EReference ref) {		
		EList<EObject> scope = new BasicEList<EObject>();
		EntityDeclaration entityDeclaration = componentDeclaration.getEntityRef();
		
		if(entityDeclaration == null) {
			return IScope.NULLSCOPE;
		}
		
		String entityName = entityDeclaration.getName();
		componentDeclaration.setName(entityName);
		scope.add(componentDeclaration);
		return scopeFor(scope, true);
	}
	
	protected IScope scope_ComponentInstantiationStatement_type(ComponentInstantiationStatement componentInstantiationStatement, EReference ref) {
		if (!(componentInstantiationStatement.eContainer() instanceof ArchitectureDeclaration)) {
			return IScope.NULLSCOPE;
		}
		
		ArchitectureDeclaration architectureDeclaration = (ArchitectureDeclaration) componentInstantiationStatement.eContainer();
		List<ComponentDeclaration> componentDeclarations = EcoreUtil2.getAllContentsOfType(architectureDeclaration, ComponentDeclaration.class);
		
		EList<EObject> scope = new BasicEList<EObject>();
		for(ComponentDeclaration componentDeclaration : componentDeclarations) {
			EntityDeclaration entityDeclaration = componentDeclaration.getEntityRef();
			
			if(entityDeclaration == null) {
				continue;
			}
			
			String entityName = entityDeclaration.getName();
			componentDeclaration.setName(entityName);
			scope.add(componentDeclaration);
		}
		
		return scopeFor(scope, true);
	}

	protected IScope scope_QualifiedIdentifierReference_field(QualifiedIdentifierReference qualifiedIdentifierreference, EReference ref) {
		IdentifierReference object = qualifiedIdentifierreference.getObject();

		if(!(object instanceof IdentifierReference)) {
			return IScope.NULLSCOPE;
		}

		IdentifierReference identifierReference = (IdentifierReference) object;
		Identifier identifier = identifierReference.getRef();
		
		if(identifier == null || identifier.eContainer() == null) {		 
			return IScope.NULLSCOPE;
		}

		ParameterizedTypeIdentifierReference typeRef = null;
		
		InterfaceDeclaration interfaceDeclaration = EcoreUtil2.getContainerOfType(identifier, InterfaceDeclaration.class);
		
		if (interfaceDeclaration != null) {
			typeRef = interfaceDeclaration.getType();
		}
		
		Declaration declaration = EcoreUtil2.getContainerOfType(identifier, Declaration.class);

		if (declaration != null) {
			typeRef = declaration.getType();
		}
		
		if(typeRef == null || typeRef.getRef() == null) {
			return IScope.NULLSCOPE;
		}

		TypeIdentifier typeIdentifier = typeRef.getRef();
		
		PackageDeclaration packageDeclaration = EcoreUtil2.getContainerOfType(typeIdentifier, PackageDeclaration.class);
		if(packageDeclaration != null) {
			List<Identifier> identifiers = EcoreUtil2.getAllContentsOfType(packageDeclaration, Identifier.class);
			return scopeFor(identifierReference, ref, identifiers, true);
		}
		
 		return IScope.NULLSCOPE;
	}
	
	protected IScope scope_GenericIdentifierReference_ref(GenericIdentifierReference genericIdentifierReference, EReference ref) {
 		if (genericIdentifierReference.eResource() == null ||
 			genericIdentifierReference.eResource().getContents() == null ||
 			genericIdentifierReference.eResource().getContents().size() <= 0 ||
 	 	  !(genericIdentifierReference.eResource().getContents().get(0) instanceof Model)) {
 	 		return IScope.NULLSCOPE;
 	 	}
 		
 		Model model = (Model) genericIdentifierReference.eResource().getContents().get(0);
 		ComponentInstantiationStatement componentInstantiation = (ComponentInstantiationStatement)genericIdentifierReference.eContainer().eContainer().eContainer().eContainer().eContainer();
 		
 		List<ComponentDeclaration> componentDeclarations = EcoreUtil2.getAllContentsOfType(model, ComponentDeclaration.class);
		List<Identifier> identifiers = new BasicEList<Identifier>();
 		
 		for(ComponentDeclaration componentDeclaration : componentDeclarations) {
 			if(componentDeclaration.getName().equalsIgnoreCase(componentInstantiation.getType().getName())) {
 				InterfaceList genericInterfaceList = componentDeclaration.getGenericClause().getInterfaceList();
 				
 				List<InterfaceDeclaration> genericInterfaceDeclarations = new BasicEList<InterfaceDeclaration>();
 				genericInterfaceDeclarations.add(genericInterfaceList.getHead());
 				genericInterfaceDeclarations.addAll(genericInterfaceList.getTail());
 				
 				for(InterfaceDeclaration genericInterfaceDeclaration : genericInterfaceDeclarations) {
 					identifiers.add(genericInterfaceDeclaration.getIdentifiers().getHead());
 					identifiers.addAll(genericInterfaceDeclaration.getIdentifiers().getTail());
 				}
 			}
 		}
 		
 		return scopeFor(genericIdentifierReference, ref, identifiers, true);
	}
	
	protected IScope scope_PortIdentifierReference_ref(PortIdentifierReference portIdentifierReference, EReference ref) {
 		if (portIdentifierReference.eResource() == null ||
 			portIdentifierReference.eResource().getContents() == null ||
 			portIdentifierReference.eResource().getContents().size() <= 0 ||
 	 	  !(portIdentifierReference.eResource().getContents().get(0) instanceof Model)) {
 	 		return IScope.NULLSCOPE;
 	 	}
 		
 		Model model = (Model) portIdentifierReference.eResource().getContents().get(0);
 		ComponentInstantiationStatement componentInstantiation = (ComponentInstantiationStatement)portIdentifierReference.eContainer().eContainer().eContainer().eContainer().eContainer();

 		List<ComponentDeclaration> componentDeclarations = EcoreUtil2.getAllContentsOfType(model, ComponentDeclaration.class);
		List<Identifier> identifiers = new BasicEList<Identifier>();
 		
 		for(ComponentDeclaration componentDeclaration : componentDeclarations) {
 			if(componentDeclaration.getName().equalsIgnoreCase(componentInstantiation.getType().getName())) {
 				InterfaceList portInterfaceList = componentDeclaration.getPortClause().getInterfaceList();
 				
 				List<InterfaceDeclaration> portInterfaceDeclarations = new BasicEList<InterfaceDeclaration>();
 				portInterfaceDeclarations.add(portInterfaceList.getHead());
 				portInterfaceDeclarations.addAll(portInterfaceList.getTail());
 				
 				for(InterfaceDeclaration portInterfaceDeclaration : portInterfaceDeclarations) {
 					identifiers.add(portInterfaceDeclaration.getIdentifiers().getHead());
 					identifiers.addAll(portInterfaceDeclaration.getIdentifiers().getTail());
 				}
 			}
 		}
 		
 		return scopeFor(portIdentifierReference, ref, identifiers, true);
	}

	protected IScope scope_IdentifierReference_ref(IdentifierReference identifierReference, EReference ref) {
		PackageDeclaration packageDeclaration = EcoreUtil2.getContainerOfType(identifierReference, PackageDeclaration.class);
		if(packageDeclaration != null) {
			List<Identifier> identifiers = EcoreUtil2.getAllContentsOfType(packageDeclaration, Identifier.class);
			return scopeFor(identifierReference, ref, identifiers, true);
		}
		
		EntityDeclaration entityDeclaration = EcoreUtil2.getContainerOfType(identifierReference, EntityDeclaration.class);
		if(entityDeclaration != null) {
			List<Identifier> identifiers = EcoreUtil2.getAllContentsOfType(entityDeclaration, Identifier.class);
			return scopeFor(identifierReference, ref, identifiers, true);
		}
		
		ArchitectureDeclaration architectureDeclaration = EcoreUtil2.getContainerOfType(identifierReference, ArchitectureDeclaration.class);
		if(architectureDeclaration != null) {
			entityDeclaration = architectureDeclaration.getEntityRef();
			List<Identifier> entityIdentifiers = EcoreUtil2.getAllContentsOfType(entityDeclaration, Identifier.class);
			List<SignalOrConstantIdentifier> architectureIdentifiers = EcoreUtil2.getAllContentsOfType(architectureDeclaration, SignalOrConstantIdentifier.class);
			List<Identifier> identifiers = new BasicEList<Identifier>();
			identifiers.addAll(entityIdentifiers);
			identifiers.addAll(architectureIdentifiers);
			return scopeFor(identifierReference, ref, identifiers, true);
		}
 		
 		return IScope.NULLSCOPE;
	}

	protected IScope scope_TypeIdentifierReference_ref(TypeIdentifierReference typeIdentifierReference, EReference ref) {
 		if (typeIdentifierReference.eResource() == null ||
 			typeIdentifierReference.eResource().getContents() == null ||
 			typeIdentifierReference.eResource().getContents().size() <= 0 ||
 		  !(typeIdentifierReference.eResource().getContents().get(0) instanceof Model)) {
 			return IScope.NULLSCOPE;
 		}
		
		Model model = (Model) typeIdentifierReference.eResource().getContents().get(0);
 		List<TypeIdentifier> identifierDeclarations = EcoreUtil2.getAllContentsOfType(model, TypeIdentifier.class);
 		EList<TypeIdentifier> typeIdentifierDeclarations = new BasicEList<TypeIdentifier>();

 		for(TypeIdentifier identifier : identifierDeclarations) {
 			if(identifier instanceof TypeIdentifier) {
 				typeIdentifierDeclarations.add((TypeIdentifier) identifier);
 			}
 		}
 		
 		return scopeFor(typeIdentifierReference, ref, typeIdentifierDeclarations, true);
	}

    /***
     *  Makes cross-references through scopes case-insensitive and inherits parent scope! Performance Hit!
     */
	protected IScope scopeFor(EObject parent, EReference ref, Iterable<? extends EObject> iter, boolean ignoreCase)
	{
      IScope parentScope = delegateGetScope(parent, ref);
    	
	  return new SimpleScope(parentScope, Scopes.scopedElementsFor(iter, QualifiedName.wrapper(SimpleAttributeResolver.NAME_RESOLVER)), ignoreCase);
	}
    
    /***
     * Makes cross-references through scopes case-insensitive!!!
     */
	protected IScope scopeFor(Iterable<? extends EObject> iter, boolean ignoreCase)
	{
	  return new SimpleScope(IScope.NULLSCOPE, Scopes.scopedElementsFor(iter, QualifiedName.wrapper(SimpleAttributeResolver.NAME_RESOLVER)), ignoreCase);
	}
}
