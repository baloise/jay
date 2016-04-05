package jay.monitor;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class JarFileLoader extends URLClassLoader {

	public JarFileLoader() {
		super(new URL[] {});
	}
	
	public JarFileLoader withFile(String jarFile) {
		return withFile(new File(jarFile));
	}
	
	public JarFileLoader withFile(File jarFile) {
		try {
			addURL(new URL("jar:file://" + jarFile.getAbsolutePath() + "!/"));
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException(e);
		}
		return this;
	}
	
	public JarFileLoader withLibDir(File path) {
		for (File jarFile : path.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".jar");
			}
		})) {
			withFile(jarFile);
		}
		return this;
	}
}
