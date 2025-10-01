package jay.monitor.sensor;

import static java.time.LocalTime.now;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import jay.monitor.sensor.impl.AbstractPollingSensor;
public class TimeSensor extends AbstractPollingSensor {

	private TimeUnit unit;

	@Override
	public void configure(Properties props) {
		name = props.getProperty("unit");
		if(name == null) {
			throw new IllegalArgumentException("Missing property 'unit'");
		}
		unit = TimeUnit.valueOf(name.toUpperCase()+"S");
		setDelaySecs(1000);
	}
	
	@Override
	public double poll() {
		switch (unit) {
		case DAYS:
			setDelaySecs(secondsUntilNextHour()+2);
			return 1d - now().getHour() / 23d;
		case HOURS:
			setDelaySecs(secondsUntilNextMinute()+1);
			return 1d - now().getMinute() / 59d;
		case MINUTES:
			setDelaySecs(5 - now().getSecond() % 5);
			return 1d - now().getSecond() / 59d;
		default:
			return 0;
		}
	}

	private long secondsUntilNextMinute() {
		return 60 - now().getSecond();
	}

	private long secondsUntilNextHour() {
		return (60-now().getMinute())*60;
	}

	private void setDelaySecs(long seconds) {
		setDelay(1000* seconds);
	}

}
