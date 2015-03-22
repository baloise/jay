package jay.monitor.sensor;

import java.util.Properties;
import java.util.Random;

public class DummySensor extends AbstractSensor {
	Random random = new Random(System.currentTimeMillis());

	public DummySensor(Properties props) {
		super(props);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double sens() {
		if (getDelay() < 30000) {
			setDelay(random.nextInt(2000) + getDelay());
		}
		return (getValue() + 1) % 2;
	}
}
