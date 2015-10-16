package jay.monitor.sensor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;
import java.util.Random;

public class DummySensor extends AbstractSensor implements ActionListener{
	Random random = new Random(System.currentTimeMillis());

	public DummySensor(Properties props) {
		super(props);
		setDelay(1000);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double sens() {
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
