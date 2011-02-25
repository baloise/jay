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
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Set;

import jay.monitor.AutoHide.VISIBILITY;
import jay.monitor.sensor.DummySensor;
import jay.monitor.sensor.HTTPSensor;
import jay.monitor.sensor.Sensor;
import jay.swing.ImageBlender;

public class Main implements PropertyChangeListener {
	final Frame frame = new Frame();
	final TrayIcon trayIcon;
	final private Set<SensorUI> sensorUIs = new HashSet<SensorUI>();
	final private Set<Sensor> sensorsThatHaveBeenOk = new HashSet<Sensor>();
	final Image red, green;
	final ImageBlender blender;
	public boolean balloonDisabled = false; 

	public Main() {
		green = Toolkit.getDefaultToolkit().getImage("32px-Button_Icon_Green.svg.png");
		red= Toolkit.getDefaultToolkit().getImage("32px-Button_Icon_Red.svg.png");
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
		MenuItem exitItem = new MenuItem("Exit");
		exitItem.addActionListener(exitListener);
		popup.add(exitItem);
		return popup;
	}

  /**
   * @param args
   *          the command line arguments
   * @throws MalformedURLException
   */
	public static void main(String[] args) throws MalformedURLException {
		Main main = new Main();
    main.addSensor(new DummySensor("Parsys"));
    main.addSensor(new DummySensor("DB2"));
    main.addSensor(new HTTPSensor("x415265", "http://x415265.bvch.ch:880/memoryMonitor/"));
    main.addSensor(new HTTPSensor("Goole", "http://www.google.com"));
	}


	public void addSensor(Sensor sensor) {
    Thread thread = new Thread(sensor);
    thread.setDaemon(true);
    thread.start();
		sensor.addPropertyChangeListener(this);
		SensorUI ui = new DefaultSensorUI(sensor);
		frame.add(ui.getLabel());
		frame.add(ui.getIcon());
		sensorUIs.add(ui);
		frame.setLocation();
		if(isOk(sensor)){
			sensorsThatHaveBeenOk.add(sensor);
		}
		setIconImage();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		showBalloon(evt);
		setIconImage();
	}

	public void setIconImage() {
		for (SensorUI ui : sensorUIs) {
			if(!isOk(ui.getSensor())){
				blender.blendTo(red);
				return;
			}
		}
		blender.blendTo(green);
	}

	private void showBalloon(PropertyChangeEvent evt) {
		Sensor sensor = (Sensor) evt.getSource();
		if(isOk(sensor)&& !sensorsThatHaveBeenOk.contains(sensor)){
			sensorsThatHaveBeenOk.add(sensor);
			if(initialisationFinished()){
				frame.autoHide.setVisibility(VISIBILITY.TEMPORARLY_VISIBLE);
			}
			return;
		}
		if(frame.isVisible()|| balloonDisabled ) {
      return;
    }
		if(isOk(sensor)) {
      trayIcon.displayMessage(sensor.getName()," went up",MessageType.INFO);
    }
    else {
      trayIcon.displayMessage(sensor.getName()," went down",MessageType.WARNING);
    }
	}

	private boolean initialisationFinished() {
		return sensorsThatHaveBeenOk.size() == sensorUIs.size();
	}

	private static boolean isOk(Sensor sensor) {
		return sensor.getValue()>0;
	}
}
