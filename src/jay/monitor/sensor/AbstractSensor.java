package jay.monitor.sensor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Properties;

import javax.swing.event.EventListenerList;

public abstract class AbstractSensor implements Sensor {
  EventListenerList listeners = new EventListenerList();
  private String name;
  private long delay;
  private double value;

  /**
   * @return das Attribut delay
   */
  public final long getDelay() {
    return delay;
  }

  /**
   * @param aDelay
   *          das Attribut delay das gesetzt wird
   */
  public final void setDelay(long aDelay) {
    delay = aDelay;
  }

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    listeners.add(PropertyChangeListener.class, listener);
  }

  void firePropertyChangeEvent(PropertyChangeEvent event) {
    for (PropertyChangeListener listener : listeners.getListeners(PropertyChangeListener.class)) {
      listener.propertyChange(event);
    }
  }

  @Override
  public String toString() {
    return getName() + ": " + getValue();
  }

  public AbstractSensor(Properties props) {
    this(props.getProperty("name"), Long.valueOf(props.getProperty("delay")));
  }
  
  public AbstractSensor(String name) {
    this(name, 10000);
  }

  public AbstractSensor(String name, long delay) {
    this.name = name;
    setDelay(delay);
  }

  @Override
  public String getName() {
    if(name == null) {
      name = getClass().getSimpleName();
    }
    return name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void run() {
    while(true) {
      final double oldValue = value;
      value = sens();
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
    }
    catch (InterruptedException e) {}
  }
}
