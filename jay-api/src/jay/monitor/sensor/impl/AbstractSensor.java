package jay.monitor.sensor.impl;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Properties;

import jay.monitor.sensor.Configurable;
import jay.monitor.sensor.Sensor;

public abstract class AbstractSensor implements Sensor, Configurable {
	protected PropertyChangeSupport propertyChange = new PropertyChangeSupport(this);
	private String name;
	private double value;

	@Override
	public void configure(Properties props) {
		name = props.getProperty("name");
		if (name == null) {
			name = getClass().getSimpleName();
		}
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return getName() + ": " + getValue();
	}
	
	public void setValue(double value) {
		final double oldValue = value;
		this.value = value;
		propertyChange.firePropertyChange("value", oldValue, value);		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getValue() {
		return value;
	}
	
	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChange.addPropertyChangeListener(listener);
	}
	
}
