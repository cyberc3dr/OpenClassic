package org.spacehq.openclassic.api.util.io;

import java.io.File;
import java.io.FilenameFilter;

/**
 * A FilenameFilter that searches for jar files.
 */
public class JarFilter implements FilenameFilter {

	@Override
	public boolean accept(File dir, String name) {
		return name.endsWith(".jar");
	}

}
