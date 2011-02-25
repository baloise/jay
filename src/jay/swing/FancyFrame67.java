package jay.swing;

import java.awt.Color;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Shape;
import java.lang.reflect.Method;

public class FancyFrame67 extends javax.swing.JFrame implements
		javax.swing.RootPaneContainer {

	/** Creates new form FancyFrame */
	public FancyFrame67() {
		super(getTranslucencyCapableGC());
		setUndecorated(true);
		setWindowOpaque(false);
		setLocationRelativeTo(null);
		setBackground(new Color(0, 0, 0, 0));
	}

	public static GraphicsConfiguration getTranslucencyCapableGC() {
		GraphicsEnvironment env = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		GraphicsConfiguration ret = env.getDefaultScreenDevice()
				.getDefaultConfiguration();
		if (AWTUtilitiesWrapper.isTranslucencyCapable(ret)) {
			return ret;
		} else {
			GraphicsDevice[] devices = env.getScreenDevices();
			for (int i = 0; i < devices.length; i++) {
				GraphicsConfiguration[] configs = devices[i]
						.getConfigurations();
				for (int j = 0; j < configs.length; j++) {
					if (AWTUtilitiesWrapper.isTranslucencyCapable(configs[j])) {
						return configs[j];
					}
				}
			}
			return null;
		}
	}

	public void setWindowShape(Shape shape) {
		if (!callSuper("setShape", shape)) {
			jay.swing.AWTUtilitiesWrapper.setWindowShape(this, shape);
		}
	}

	private boolean callSuper(String name, Object param) {
		try {
			Method[] methods = java.awt.Window.class.getMethods();
			for (Method method : methods) {
				if(method.getName().equals(name)){
					method.invoke(this, param);
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	public void setWindowOpacity(float opacity) {
		if (!callSuper("setOpacity", opacity)) {
			jay.swing.AWTUtilitiesWrapper.setWindowOpacity(this,
					opacity);
		}
	}

	private void setWindowOpaque(boolean opaque) {
		jay.swing.AWTUtilitiesWrapper.setWindowOpaque(this, opaque);
	}
}
