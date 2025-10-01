package jay;

import static java.io.File.separator;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.EMPTY_MAP;
import static java.util.regex.Pattern.quote;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class JRE {

	public static String jre() {
		String javaHome = System.getProperty("java.home");
		String java = javaHome + separator + "bin" + separator + "java";
		return new File(java).exists() ? java : java + "w.exe";
	}
	
	@SuppressWarnings("unchecked")
	public static ProcessBuilder spawn(Class<?> mainClass, String ... args) throws IOException {
		return spawn(mainClass, EMPTY_MAP, args);
	}
	
	public static ProcessBuilder spawn(Class<?> mainClass, Map<String, String> environment, String ... args) throws IOException {
		RuntimeMXBean mxbean = ManagementFactory.getPlatformMXBean(RuntimeMXBean.class);
		List<String> processArgs = new ArrayList<String>();
		processArgs.addAll(asList(jre(), "-cp", mxbean.getClassPath()));
		for(Entry<String, String> env : environment.entrySet()) {
			processArgs.add(format("-D%s=%s", env.getKey(), env.getValue()));
		}
		processArgs.add(mainClass.getName());
		processArgs.addAll(asList(args));
		return new ProcessBuilder(processArgs);
	}

}