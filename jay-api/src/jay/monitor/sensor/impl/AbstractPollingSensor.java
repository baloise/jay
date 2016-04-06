package jay.monitor.sensor.impl;

import java.util.Properties;

public abstract class AbstractPollingSensor extends AbstractSensor{
	
	/**
	 * Delay between two sens() calls in msec. Default is 60000 = 1 min.
	 */
	private long delay = 60000;

	public long getDelay() {
		return delay;
	}

	public void setDelay(long aDelay) {
		delay = aDelay;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		while (true) {
			setValue(poll());
			sleep();
		}
	}

	@Override
	public void configure(Properties props) {
		super.configure(props);
		try {
			String tmp = props.getProperty("delay");
			if (tmp != null)
				setDelay(Long.valueOf(tmp));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (getDelay() < 0) {
			throw new IllegalArgumentException("delay of sensor "+getName()+" must be >= 0");
		}
	}

	protected abstract double poll();

	protected void sleep() {
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
		}
	}
}
