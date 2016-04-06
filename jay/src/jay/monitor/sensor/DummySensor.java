package jay.monitor.sensor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import jay.monitor.sensor.impl.AbstractPollingSensor;

public class DummySensor extends AbstractPollingSensor implements ActionListener {
	Random random = new Random(System.currentTimeMillis());

	public DummySensor() {
		setDelay(1000);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double poll() {
		if (getDelay() < 30000) {
			setDelay(random.nextInt(2000) + getDelay());
		}
		return random.nextDouble();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Thread.currentThread().interrupt();
	}
}
