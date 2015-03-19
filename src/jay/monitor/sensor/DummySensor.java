package jay.monitor.sensor;

import java.beans.PropertyChangeEvent;
import java.util.Properties;
import java.util.Random;

public class DummySensor extends AbstractSensor {
	Random random = new Random(System.currentTimeMillis());
	int delay=10;
	float factor=1;
	double value;
	public DummySensor(Properties props) {
	    super(props);
	}
	
	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep((long) (random.nextInt(delay*1000)*factor));
			} catch (InterruptedException e) {
			}
			delay+=10;
			factor *=1.2;
      value = (value + 1) % 2;
      firePropertyChangeEvent(new PropertyChangeEvent(this, "sensor", (value + 1) % 2, value));
		}
	}

	@Override
	public double getValue() {
    return value;
	}

  /**
   * {@inheritDoc}
   */
  @Override
  protected double sens() {
    return 0;
  }
}
