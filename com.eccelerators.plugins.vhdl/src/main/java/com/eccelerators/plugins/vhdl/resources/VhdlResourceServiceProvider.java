package com.eccelerators.plugins.vhdl.resources;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.resource.impl.DefaultResourceServiceProvider;
 
public class VhdlResourceServiceProvider extends DefaultResourceServiceProvider {

	public String ProjectName;
	
	public URI ProjectPath;
	
	@Override public boolean canHandle(URI uri) {
		return isValid(uri);
	}

	@Override
	public boolean isSource(URI uri) {
		return isValid(uri);
	}

	private Boolean isValid(URI uri) {
		String folderName = getFolderNameFromFilePath(uri);

		if (folderName.equalsIgnoreCase("src") ||
			folderName.equalsIgnoreCase("tb") ||
			folderName.equalsIgnoreCase("resources")) {
			return true;
		}

		return false;
	}

	private String getFolderNameFromFilePath(URI uri) {
		Path absolutePath = Paths.get(uri.toString().substring(7));
        Path basePath = Paths.get(ProjectPath.toString().substring(7));
        Path relativePath = basePath.relativize(absolutePath);
		String folderName = relativePath.subpath(0, 1).toString();

		return folderName.trim();
	}
}
