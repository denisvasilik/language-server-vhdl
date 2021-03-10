package com.eccelerators.plugins.vhdl.ide;

import org.eclipse.xtext.ide.server.ProjectManager;
import org.eclipse.xtext.ide.server.ServerModule;

public class VhdlServerModule extends ServerModule {

	@Override
	protected void configure() {
		super.configure();
		bind(ProjectManager.class).to(VhdlProjectManager.class);
	}
	
}
