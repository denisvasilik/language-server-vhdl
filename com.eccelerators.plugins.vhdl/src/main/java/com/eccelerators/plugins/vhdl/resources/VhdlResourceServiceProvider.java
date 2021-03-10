package com.eccelerators.plugins.vhdl.resources;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.resource.impl.DefaultResourceServiceProvider;

public class VhdlResourceServiceProvider extends DefaultResourceServiceProvider {

	@Override public boolean canHandle(URI uri) {
		return isValid(uri);
	}

	@Override
	public boolean isSource(URI uri) {
		return isValid(uri);
	}

	private Boolean isValid(URI uri) {
		String folderName = null;
		if(uri.isFile()) {
			folderName = getFolderNameFromFilePath(uri);
		} else if (uri.isPlatform()) {
			folderName = getFolderFromPlatformPath(uri);
		} else {
			throw new RuntimeException("Invalid resource URI");
		}

		if (folderName.equalsIgnoreCase("src") ||
			folderName.equalsIgnoreCase("tb") ||
			folderName.equalsIgnoreCase("resources")) {
			return true;
		}

		return false;
	}

	private String getFolderFromPlatformPath(URI uri) {
		return uri.segment(2);
	}

	private String getFolderNameFromFilePath(URI uri) {
		Path absolutePath = Paths.get(uri.toString().substring(7));
        Path basePath = getCurrentWorkingDirectory();
        Path relativePath = basePath.relativize(absolutePath);
		String folderName = relativePath.subpath(0, 1).toString();

		return folderName.trim();
	}

	private Path getCurrentWorkingDirectory() {
		Path cwd = null;
		try {
			cwd = Paths.get(new java.io.File( "." ).getCanonicalPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cwd;
	}
}
