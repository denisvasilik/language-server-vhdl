package com.eccelerators.plugins.vhdl.ide

import org.eclipse.xtext.ide.server.ServerLauncher

class VhdlServerLauncher extends ServerLauncher {
	def static void main(String[] args) {
		launch(VhdlServerLauncher.name, args, new VhdlServerModule)
	}
}
