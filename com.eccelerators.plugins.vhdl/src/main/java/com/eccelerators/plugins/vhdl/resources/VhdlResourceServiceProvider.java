package com.eccelerators.plugins.vhdl.resources;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.resource.impl.DefaultResourceServiceProvider;

public class VhdlResourceServiceProvider extends DefaultResourceServiceProvider {

	private final static Logger LOG = Logger.getLogger(VhdlResourceServiceProvider.class);

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
		String folderName = uri.segment(2);

		return folderName;
	}

	private String getFolderNameFromFilePath(URI uri) {
		Path absolutePath = Paths.get(uri.toString().substring(7));
        Path basePath = Paths.get("/home/ubuntu-dev/runtime-EclipseXtext");
        Path relativePath = basePath.relativize(absolutePath);
		String folderName = relativePath.subpath(1, 2).toString();

		return folderName;
	}
}
