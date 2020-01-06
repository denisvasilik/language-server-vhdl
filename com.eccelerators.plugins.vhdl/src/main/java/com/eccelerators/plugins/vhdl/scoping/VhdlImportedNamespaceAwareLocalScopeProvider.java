package com.eccelerators.plugins.vhdl.scoping;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.scoping.ICaseInsensitivityHelper;
import org.eclipse.xtext.scoping.IGlobalScopeProvider;
import org.eclipse.xtext.scoping.impl.ImportNormalizer;
import org.eclipse.xtext.scoping.impl.ImportedNamespaceAwareLocalScopeProvider;

import com.eccelerators.plugins.vhdl.vhdl.ArchitectureDeclaration;
import com.eccelerators.plugins.vhdl.vhdl.DesignUnit;
import com.eccelerators.plugins.vhdl.vhdl.LibraryUnit;
import com.eccelerators.plugins.vhdl.vhdl.PackageBodyDeclaration;
import com.eccelerators.plugins.vhdl.vhdl.PackageDeclaration;
import com.eccelerators.plugins.vhdl.vhdl.PackageReference; 
import com.eccelerators.plugins.vhdl.vhdl.UseClause;
import com.eccelerators.plugins.vhdl.vhdl.Model;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

public class VhdlImportedNamespaceAwareLocalScopeProvider extends ImportedNamespaceAwareLocalScopeProvider {

	private String defaultStandardNamespace = "STANDARD.all";
	private String defaultTextIoNamespace = "TEXTIO.all";
	
	private List<ImportNormalizer> defaultPackages;
	
	@Inject
	public VhdlImportedNamespaceAwareLocalScopeProvider() {
		super();
	}

	public VhdlImportedNamespaceAwareLocalScopeProvider(IGlobalScopeProvider globalScopeProvider,
			IQualifiedNameProvider qualifiedNameProvider, IQualifiedNameConverter qualifiedNameConverter,
			ICaseInsensitivityHelper caseInsensitivityHelper) {
		super(globalScopeProvider, qualifiedNameProvider, qualifiedNameConverter, caseInsensitivityHelper);
	}

	protected List<ImportNormalizer> getImplicitImports(boolean ignoreCase) {
		defaultPackages = Lists.newArrayList();
		
		ImportNormalizer temporaryResolver1 = createImportedNamespaceResolver(defaultStandardNamespace, ignoreCase);
		ImportNormalizer temporaryResolver2 = createImportedNamespaceResolver(defaultTextIoNamespace, ignoreCase);
		
		defaultPackages.add(temporaryResolver1);
		defaultPackages.add(temporaryResolver2);

		return defaultPackages;
	}
	
	@Override
	protected List<ImportNormalizer> internalGetImportedNamespaceResolvers(final EObject context, boolean ignoreCase) {
		if(!(context instanceof DesignUnit) &&
		   !(context instanceof ArchitectureDeclaration) &&
		   !(context instanceof PackageBodyDeclaration)) {
			return Collections.emptyList();
		}

		List<ImportNormalizer> importedNamespaceResolvers = Lists.newArrayList();
		List<UseClause> useClauses = null;
		
		if(context instanceof DesignUnit) {
			// Add packages defined at the top of the file to the namespace of the first DesignUnit.
			useClauses = EcoreUtil2.getAllContentsOfType(context, UseClause.class);
		} else if(context instanceof ArchitectureDeclaration)	{
			// Add packages defined at the top of the file to the namespace of the ArchitectureDeclaration.
			Model model = (Model) context.eResource().getContents().get(0);
			useClauses = EcoreUtil2.getAllContentsOfType(model, UseClause.class);
		} else if(context instanceof PackageBodyDeclaration) {
			// Add packages defined at the top of the file to the namespace of the PackageBodyDeclaration.
			Model model = (Model) context.eResource().getContents().get(0);
			List<DesignUnit> designUnits = model.getDesignUnits();
			for(DesignUnit designUnit : designUnits) {
				LibraryUnit libraryUnit = designUnit.getLibraryUnit();
				if(libraryUnit instanceof PackageDeclaration) {
					PackageDeclaration packageDeclaration = (PackageDeclaration) libraryUnit;
					String namespace = packageDeclaration.getName() + ".all";
					ImportNormalizer importNormalizer = createImportedNamespaceResolver(namespace, true);
					importedNamespaceResolvers.add(importNormalizer);
				}
			}
			useClauses = EcoreUtil2.getAllContentsOfType(model, UseClause.class);
		}
		
		// NOTE: Performance impact -> Using implicit imports is more efficient!
		//		 importedNamespaceResolvers.addAll(getImplicitImports(true));
		 
		for(UseClause useClause : useClauses) {
			String importedNamespace = getImportedNamespace(useClause);
			ImportNormalizer temporaryResolver = createImportedNamespaceResolver(importedNamespace, ignoreCase);
			if (temporaryResolver != null) {
				importedNamespaceResolvers.add(temporaryResolver);
			}
		}		

		return importedNamespaceResolvers;
	}
	
	@Override
	protected String getImportedNamespace(EObject object) {
		EStructuralFeature feature = object.eClass().getEStructuralFeature("importedNamespace");
		if(feature != null && PackageReference.class.equals(feature.getEType().getInstanceClass())) {
			PackageReference packageReference = (PackageReference) object.eGet(feature);
			String packageName = packageReference.getPackage();
			String postfix = packageReference.getPostfix();
			return packageName + '.' + postfix;
		}
		return null;
	}
	
	@Override
	public String getWildCard() {
		return "all";
	}
}
