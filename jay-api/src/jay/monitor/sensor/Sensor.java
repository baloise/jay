package jay.monitor.sensor;

import java.beans.PropertyChangeListener;

public interface Sensor extends Runnable, Named {
	void addPropertyChangeListener(PropertyChangeListener listener);
	double getValue();
}