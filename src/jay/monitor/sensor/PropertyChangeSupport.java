package jay.monitor.sensor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.event.EventListenerList;

public abstract class PropertyChangeSupport {
	EventListenerList listeners = new EventListenerList();

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		listeners.add(PropertyChangeListener.class, listener);
	}

	void firePropertyChangeEvent(PropertyChangeEvent event) {
		for (PropertyChangeListener listener : listeners.getListeners(PropertyChangeListener.class)) {
			listener.propertyChange(event);
		}
	}

	PropertyChangeEvent createValueChangedEvent(final double oldValue, final double newValue) {
		return new PropertyChangeEvent(this, "value", oldValue, newValue);
	}
	
}
