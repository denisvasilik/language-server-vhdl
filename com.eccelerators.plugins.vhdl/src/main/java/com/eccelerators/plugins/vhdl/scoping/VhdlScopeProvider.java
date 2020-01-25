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
		Identifier signalIdentifier = identifierReference.getRef();
		
		if(signalIdentifier == null ||
		   signalIdentifier.eContainer() == null || 
		 !(signalIdentifier.eContainer() instanceof DeclaredIdentifiers)) { 
			return IScope.NULLSCOPE;
		}
		
		DeclaredIdentifiers declaredIdentifiers = (DeclaredIdentifiers) signalIdentifier.eContainer();
		
		if(declaredIdentifiers.eContainer() == null ||
		  !(declaredIdentifiers.eContainer() instanceof Declaration)) {
			return IScope.NULLSCOPE;
		}
		
		Declaration declaration = (Declaration) declaredIdentifiers.eContainer();
		ParameterizedTypeIdentifierReference typeRef = declaration.getType();
		
		if(typeRef == null ||
		   typeRef.getRef().eContainer() == null ||
		 !(typeRef.getRef().eContainer() instanceof FullTypeDeclaration)) {
			return IScope.NULLSCOPE;
		}
		
		FullTypeDeclaration fullTypeDeclaration = (FullTypeDeclaration) typeRef.getRef().eContainer();

		List<Identifier> identifierDeclarations = EcoreUtil2.getAllContentsOfType(fullTypeDeclaration, Identifier.class);
		List<SignalOrConstantIdentifier> signalIdentifierDeclarations = new BasicEList<SignalOrConstantIdentifier>();
		
		// Filter for SignalIdentifier
 		for(Identifier identifier : identifierDeclarations) {
 			if(identifier instanceof SignalOrConstantIdentifier) {
 				signalIdentifierDeclarations.add((SignalOrConstantIdentifier) identifier);
 			}
 		}
		
 		return scopeFor(signalIdentifierDeclarations, true);
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
	
	protected IScope scope_IdentifierReference_ref(IdentifierReference qualifiedVarRef, EReference ref) {
 		if (qualifiedVarRef.eResource() == null ||
 			qualifiedVarRef.eResource().getContents() == null ||
 			qualifiedVarRef.eResource().getContents().size() <= 0 ||
 	 	  !(qualifiedVarRef.eResource().getContents().get(0) instanceof Model)) {
 	 		return IScope.NULLSCOPE;
 	 	}
		
 		Model model = (Model) qualifiedVarRef.eResource().getContents().get(0);
 		
 		List<Identifier> plaindentifierDeclarations = EcoreUtil2.getAllContentsOfType(model, Identifier.class);
		List<Identifier> signalIdentifierDeclarations = new BasicEList<Identifier>();
		
		// Handle references to generics
		if(qualifiedVarRef.eContainer().eContainer().eContainer() instanceof GenericInterfaceDeclaration) {
			GenericInterfaceDeclaration genericInterfaceDeclaration = (GenericInterfaceDeclaration) qualifiedVarRef.eContainer().eContainer().eContainer();
			// (1) Handle identifier references to generics from within a component declaration
			if(genericInterfaceDeclaration.eContainer().eContainer().eContainer() instanceof ComponentDeclaration) {
				ComponentDeclaration componentDeclaration = (ComponentDeclaration)genericInterfaceDeclaration.eContainer().eContainer().eContainer();
				ArchitectureDeclaration architectureDeclaration = (ArchitectureDeclaration)componentDeclaration.eContainer();
				EntityDeclaration entityDeclaration = architectureDeclaration.getEntityRef();
				// Get generic identifiers from corresponding component declaration
				List<GenericIdentifier> genericIdentifiers = EcoreUtil2.getAllContentsOfType(componentDeclaration, GenericIdentifier.class);
				// Get generic identifiers from corresponding entity declaration
				genericIdentifiers.addAll(EcoreUtil2.getAllContentsOfType(entityDeclaration, GenericIdentifier.class));
				return scopeFor(qualifiedVarRef, ref, genericIdentifiers, true);
			}
		}
		
		// (2) Handle identifier references to generics from within a component instantiation
		if(qualifiedVarRef.eContainer().eContainer().eContainer().eContainer() instanceof GenericAssociationElement) {
			GenericAssociationElement genericAssociationElement = (GenericAssociationElement)qualifiedVarRef.eContainer().eContainer().eContainer().eContainer();
			ComponentInstantiationStatement componentInstantiation = (ComponentInstantiationStatement)genericAssociationElement.eContainer().eContainer().eContainer();
			ComponentDeclaration componentDeclaration = componentInstantiation.getType();
			ArchitectureDeclaration architectureDeclaration = (ArchitectureDeclaration)componentDeclaration.eContainer();
			EntityDeclaration entityDeclaration = architectureDeclaration.getEntityRef();
			// Get generic identifiers from corresponding component declaration
			List<GenericIdentifier> genericIdentifiers = EcoreUtil2.getAllContentsOfType(componentDeclaration, GenericIdentifier.class);
			// Get generic identifiers from corresponding entity declaration
			genericIdentifiers.addAll(EcoreUtil2.getAllContentsOfType(entityDeclaration, GenericIdentifier.class));
			return scopeFor(qualifiedVarRef, ref, genericIdentifiers, true);
		}
		
 		for(Identifier identifier : plaindentifierDeclarations) {
 			if(identifier instanceof PortIdentifier) {
 				if(identifier.eContainer().eContainer().eContainer().eContainer().eContainer() instanceof EntityDeclaration) {
 					signalIdentifierDeclarations.add((PortIdentifier) identifier);
 				}
 			}
 			if(identifier instanceof SignalOrConstantIdentifier) {
 				signalIdentifierDeclarations.add((SignalOrConstantIdentifier) identifier);
 			}
 		}
 		
 		return scopeFor(qualifiedVarRef, ref, signalIdentifierDeclarations, true);
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
