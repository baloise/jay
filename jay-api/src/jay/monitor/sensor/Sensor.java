package jay.monitor.sensor;

import java.beans.PropertyChangeListener;

public interface Sensor extends Runnable{
	void addPropertyChangeListener(PropertyChangeListener listener);
	String getName();
	double getValue();
}