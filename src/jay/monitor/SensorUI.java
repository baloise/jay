package jay.monitor;

import javax.swing.JComponent;

import jay.monitor.sensor.Sensor;

public interface SensorUI {
	public JComponent getIcon();
	public JComponent getLabel();
	public Sensor getSensor();
	
}
