package jay.monitor;

import static java.awt.Desktop.getDesktop;
import static java.awt.Desktop.isDesktopSupported;
import static java.util.Arrays.asList;
import static jay.monitor.DuckType.does;
import static jay.monitor.DuckType.let;

import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Font;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import jay.monitor.AutoHide.VISIBILITY;
import jay.monitor.sensor.Configurable;
import jay.monitor.sensor.DummySensor;
import jay.monitor.sensor.Sensor;
import jay.swing.ExceptionDialog;
import jay.swing.TrafficLightIcon;

public class Main implements PropertyChangeListener {
	private static final String CLASS = "class";
	final static File homeDir = new File(System.getProperty("user.home") + File.separator + ".jay");
	final Frame frame = new Frame();
	final TrayIcon trayIcon;
	final private Set<SensorUI> sensorUIs = new HashSet<SensorUI>();
	final private Set<Sensor> sensorsThatHaveBeenOk = new HashSet<Sensor>();
	final TrafficLightIcon trafficLight = new TrafficLightIcon(64);
	public boolean balloonDisabled = false;
    private PopupMenu popupMenu;
    private MenuItem exitItem;
    private MenuItem homeItem;
    private MenuItem seperatorItem = new MenuItem("-");
    private boolean hideAfterInitialisation = System.getProperty("pin") == null;
	private final ClassLoader classLoader = URLClassLoader.newInstance(getJarURLs(homeDir));

	private static URL[] getJarURLs(File ... files) {
		Set<URL> ret = new HashSet<>();
		for (File file : files) {
			if(file.isDirectory()) {
				ret.addAll(asList(getJarURLs(file.listFiles(new FilenameFilter() {
					public boolean accept(File dir, String name) {
						return name.endsWith(".jar");
					}
				}))));
			} else {
				try {
					ret.add(new URL("jar:file:" + file.getAbsolutePath() +"!/"));
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		}
		return ret.toArray(new URL[ret.size()]);
	}
	
	public Main() {
		SystemTray tray = SystemTray.getSystemTray();
		trafficLight.setPercentage(36);
		trafficLight.setMargin(8);
		popupMenu = createPopupMenu();
		trayIcon = new TrayIcon(trafficLight.getImage(), "Jay", popupMenu);
		trafficLight.getBlender().addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				trayIcon.setImage(trafficLight.getImage());
			}
		});

		trayIcon.setImageAutoSize(true);
		MouseListener mouseListener = new MouseAdapter() {
			@Override
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
		exitItem = new MenuItem("Exit");
		exitItem.addActionListener(exitListener);
		popup.add(exitItem);

		if (isDesktopSupported()) {
			ActionListener homeListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						getDesktop().open(homeDir);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			};
			homeItem = new MenuItem("Home Directory");
			homeItem.addActionListener(homeListener);
			popup.add(homeItem);
		}

		return popup;
	}


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
			Class pluginClass = classLoader.loadClass(className);
			Object plugin = pluginClass.newInstance();
			if(does(plugin).quackLike(Configurable.class)) {
				let(plugin).be(Configurable.class).configure(props);
			}
			addSensor(wrapSensor(plugin));
		} catch (Exception e) {
			handleException(e);
		}
	}

	private static Sensor wrapSensor(Object plugin) {
		try {
			return let(plugin).be(Sensor.class);
		} catch (InvocationTargetException e) {
			handleException(e);
		}
		return null;
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
			props.store(new FileOutputStream(new File(homeDir, name + ".cfg")), null);
		} catch (Exception e) {
			handleException(e);
		}
	}

	public void addSensor(final Sensor sensor) {
		if(sensor == null) return;
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
		final CheckboxMenuItem menuItem = new CheckboxMenuItem(sensor.getName());
    popupMenu.add(menuItem);
    final PropertyChangeListener stateListener = new PropertyChangeListener() {
      Font font;
      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        boolean ok = sensor.getValue() > 0.9999D;
        menuItem.setState(ok);
        if(font == null) {
          font = menuItem.getFont();
        } 
        if(font != null) {
          menuItem.setFont(ok ? font : font.deriveFont(Font.ITALIC));
        }
      }
    };
    menuItem.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent e) {
        stateListener.propertyChange(null);
        if(does(sensor).quackLike(ActionListener.class)) {
 	       let(sensor).wannaBe(ActionListener.class).actionPerformed(null);
 	    }
      }
    });
    sensor.addPropertyChangeListener(stateListener);
    popupMenu.add(seperatorItem);
    popupMenu.add(homeItem);
    popupMenu.add(exitItem);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		showBalloon(evt);
		setIconImage();
	}

	public void setIconImage() {
		double max = 0;
		for (SensorUI ui : sensorUIs) {
			max = Math.max(0, ui.getSensor().getValue());
		}
		trafficLight.blendToPercentage((int) (100 - 100D * max));
	}

	private void showBalloon(PropertyChangeEvent evt) {
		Sensor sensor = (Sensor) evt.getSource();
		if (isOk(sensor) && !sensorsThatHaveBeenOk.contains(sensor)) {
			sensorsThatHaveBeenOk.add(sensor);
			if (initialisationFinished() && hideAfterInitialisation && frame.autoHide.getVisibility() == VISIBILITY.PERMANENTLY_VISIBLE) {
				frame.autoHide.setVisibility(VISIBILITY.TEMPORARLY_VISIBLE);
			}
		}
		if (frame.autoHide.getVisibility() != VISIBILITY.HIDDEN || balloonDisabled) {
			return;
		}
		if (isOk(sensor)) {
			trayIcon.displayMessage(sensor.getName(), " went up", MessageType.INFO);
		} else {
			trayIcon.displayMessage(sensor.getName(), " went down", MessageType.WARNING);
		}
	}

	private boolean initialisationFinished() {
		return sensorsThatHaveBeenOk.size() == sensorUIs.size();
	}

	private static boolean isOk(Sensor sensor) {
		return sensor.getValue() > 0.7;
	}
}
