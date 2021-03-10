package com.eccelerators.plugins.vhdl.ide

import org.eclipse.xtext.ide.server.ProjectManager;

import java.util.List
import org.eclipse.emf.common.util.URI
import org.eclipse.xtext.build.IncrementalBuilder.Result
import org.eclipse.xtext.util.CancelIndicator
import org.eclipse.xtext.resource.IResourceDescription
import com.eccelerators.plugins.vhdl.resources.VhdlResourceServiceProvider

class VhdlProjectManager extends ProjectManager {

    override Result doBuild(List<URI> dirtyFiles, List<URI> deletedFiles, List<IResourceDescription.Delta> externalDeltas, CancelIndicator cancelIndicator) {
    	val vhdlResourceServiceProvider = languagesRegistry.getExtensionToFactoryMap().get("vhd") as VhdlResourceServiceProvider;
    	
    	vhdlResourceServiceProvider.ProjectName = projectDescription.name;
    	vhdlResourceServiceProvider.ProjectPath = this.baseDir;
    	
    	super.doBuild(dirtyFiles, deletedFiles, externalDeltas, cancelIndicator);
    }
	
}
