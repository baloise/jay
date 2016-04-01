package jay.monitor.sensor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Properties;

import javax.swing.event.EventListenerList;

public abstract class AbstractSensor extends PropertyChangeSupport implements Sensor {
	private String name;
	/**
	 * Delay between two sens() calls in msec. Default is 60000 = 1 min.
	 */
	private long delay = 60000;
	private double value;

	public final long getDelay() {
		return delay;
	}

	public final void setDelay(long aDelay) {
		delay = aDelay;
	}

	@Override
	public String toString() {
		return getName() + ": " + getValue();
	}

	public AbstractSensor(Properties props) {
		this.name = props.getProperty("name");
		if (name == null) {
			name = getClass().getSimpleName();
		}
		try {
			String tmp = props.getProperty("delay");
			if (tmp != null)
				setDelay(Long.valueOf(tmp));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (getDelay() < 0) {
			throw new IllegalArgumentException("delay must be >= 0");
		}
	}

	@Override
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		while (true) {
			final double oldValue = value;
			value = sens();
			if(value != oldValue) 
			  firePropertyChangeEvent(new PropertyChangeEvent(this, "value", oldValue, value));
			sleep();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getValue() {
		return value;
	}

	protected abstract double sens();

	protected void sleep() {
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
		}
	}
}
