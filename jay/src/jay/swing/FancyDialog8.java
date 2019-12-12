package jay.swing;

import java.awt.Color;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JDialog;

public class FancyDialog8 extends JDialog implements javax.swing.RootPaneContainer {

	/** Creates new form FancyFrame */
	public FancyDialog8() {
		setUndecorated(true);
		setLocationRelativeTo(null);
		setBackground(new Color(0, 0, 0, 0));
	}

	public static GraphicsConfiguration getTranslucencyCapableGC() {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsConfiguration ret = env.getDefaultScreenDevice().getDefaultConfiguration();
		if (ret.isTranslucencyCapable()) {
			return ret;
		} else {
			GraphicsDevice[] devices = env.getScreenDevices();
			for (int i = 0; i < devices.length; i++) {
				GraphicsConfiguration[] configs = devices[i].getConfigurations();
				for (int j = 0; j < configs.length; j++) {
					if (configs[j].isTranslucencyCapable()) {
						return configs[j];
					}
				}
			}
			return null;
		}
	}


}
