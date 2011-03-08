package jay.monitor;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import jay.monitor.AutoHide.VISIBILITY;
import jay.monitor.sensor.DummySensor;
import jay.monitor.sensor.Sensor;
import jay.swing.ExceptionDialog;
import jay.swing.ImageBlender;

public class Main implements PropertyChangeListener {
	private static final String CLASS = "class";
	final Frame frame = new Frame();
	final TrayIcon trayIcon;
	final private Set<SensorUI> sensorUIs = new HashSet<SensorUI>();
	final private Set<Sensor> sensorsThatHaveBeenOk = new HashSet<Sensor>();
	final Image red, green;
	final ImageBlender blender;
	public boolean balloonDisabled = false;

	public Main() {
		green = Toolkit.getDefaultToolkit().getImage(
				"32px-Button_Icon_Green.svg.png");
		red = Toolkit.getDefaultToolkit().getImage(
				"32px-Button_Icon_Red.svg.png");
		SystemTray tray = SystemTray.getSystemTray();

		trayIcon = new TrayIcon(red, "Jay", createPopupMenu());
		blender = new ImageBlender(red, null) {

			@Override
			protected void update(Image image) {
				trayIcon.setImage(image);
			}
		};

		trayIcon.setImageAutoSize(true);
		MouseListener mouseListener = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == 1) {
					frame.autoHide.shiftVisibility();
				}
			}
		};

		trayIcon.addMouseListener(mouseListener);

		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			System.err.println("TrayIcon could not be added.");
		}
		frame.autoHide.setVisible(true);
	}

	private PopupMenu createPopupMenu() {
		ActionListener exitListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Exiting...");
				System.exit(0);
			}
		};

		PopupMenu popup = new PopupMenu();
		MenuItem exitItem = new MenuItem("Exit");
		exitItem.addActionListener(exitListener);
		popup.add(exitItem);
		return popup;
	}

	final static File homeDir = new File(System.getProperty("user.home")
			+ File.separator + ".jay");


	public static void main(String[] args) throws MalformedURLException {
		homeDir.mkdirs();
		final Main main = new Main();
		File[] cfgFiles = listConfigFiles();
		if (cfgFiles.length == 0) {
			firstRun();
			cfgFiles = listConfigFiles();
		}
		for (File cfgFile : cfgFiles) {
			main.loadConfig(cfgFile);
		}
	}

	private static File[] listConfigFiles() {
		return homeDir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".cfg");
			}
		});
	}

	private void loadConfig(File cfgFile) {
		System.out.println("Loading " + cfgFile.getAbsolutePath());
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(cfgFile));
		} catch (Exception e) {
			handleException(e);
			return;
		}
		String className = props.getProperty(CLASS);
		try {
			Class pluginClass = Class.forName(className);
			Object plugin;
			try {
				Constructor constructor = pluginClass
						.getConstructor(Properties.class);
				plugin = constructor.newInstance(props);
			} catch (Exception e) {
				try {
					plugin = pluginClass.newInstance();
				} catch (Exception e1) {
					handleException(e1);
					return;
				}
			}
			Sensor sensor = wrapSensor(plugin);
			addSensor(sensor);
		} catch (ClassNotFoundException e) {
			handleException(e);
		}
	}

	private static Sensor wrapSensor(Object plugin) {
		// TODO handle classcastexception with a wrapper
		return (Sensor) plugin;
	}

	private static void handleException(Exception e) {
		new ExceptionDialog(null, e).setVisible(true);
	}

	private static void firstRun() {
		Properties props = new Properties();
		props.put(CLASS, DummySensor.class.getName());
		storeProps(props, "Hey");
		storeProps(props, "Jay");
		System.out.println("first run");
	}

	private static void storeProps(Properties props, String name) {
		try {
			props.put("name", name);
			props.store(new FileOutputStream(new File(homeDir, name + ".cfg")),
					null);
		} catch (Exception e) {
			handleException(e);
		}
	}

	public void addSensor(Sensor sensor) {
		sensor.addPropertyChangeListener(this);
		SensorUI ui = new DefaultSensorUI(sensor);
		frame.add(ui.getLabel());
		frame.add(ui.getIcon());
		sensorUIs.add(ui);
		frame.setLocation();
		if (isOk(sensor)) {
			sensorsThatHaveBeenOk.add(sensor);
		}
		Thread thread = new Thread(sensor);
		thread.setDaemon(true);
		thread.start();
		setIconImage();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		showBalloon(evt);
		setIconImage();
	}

	public void setIconImage() {
		for (SensorUI ui : sensorUIs) {
			if (!isOk(ui.getSensor())) {
				blender.blendTo(red);
				return;
			}
		}
		blender.blendTo(green);
	}

	private void showBalloon(PropertyChangeEvent evt) {
		Sensor sensor = (Sensor) evt.getSource();
		if (isOk(sensor) && !sensorsThatHaveBeenOk.contains(sensor)) {
			sensorsThatHaveBeenOk.add(sensor);
			if (initialisationFinished()) {
				frame.autoHide.setVisibility(VISIBILITY.TEMPORARLY_VISIBLE);
			}
			return;
		}
		if (frame.isVisible() || balloonDisabled)
			return;
		if (isOk(sensor))
			trayIcon.displayMessage(sensor.getName(), " went up",
					MessageType.INFO);
		else
			trayIcon.displayMessage(sensor.getName(), " went down",
					MessageType.WARNING);
	}

	private boolean initialisationFinished() {
		return sensorsThatHaveBeenOk.size() == sensorUIs.size();
	}

	private static boolean isOk(Sensor sensor) {
		return sensor.getValue() > 0;
	}
}
