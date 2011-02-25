package jay.monitor.sensor;

import java.beans.PropertyChangeListener;

public interface Sensor extends Runnable{
	public void addPropertyChangeListener(PropertyChangeListener listener);
	public String getName();
	public double getValue();
}