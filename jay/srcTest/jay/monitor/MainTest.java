package jay.monitor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.beans.PropertyChangeListener;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Test;

import jay.monitor.sensor.Sensor;
import jay.monitor.sensor.TimeSensor;
import jay.monitor.sensor.impl.AbstractPollingSensor;

public class MainTest {

	Consumer<Exception> fail = e-> fail(e.getMessage());
	
	class PollingSensor {
		public double poll() {
			return 0.5;
		}
	}
	
	class AllInterfacesSensor {
		public double poll() {
			return 0.5;
		}
		public String getName() {
			return "someName";
		}
		
		public long getDelay() {
			return -123;
		}
	}
	
	class SomeSensor {
		public void run() {}
		public void addPropertyChangeListener(PropertyChangeListener listener) {}
		public String getName() {return null;}
		public double getValue() {return 0;}
	}
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testWrapPollingSensor() throws Exception {
		AbstractPollingSensor sensor = (AbstractPollingSensor) Main.wrapSensor(new PollingSensor(), fail );
		assertEquals(TimeUnit.MINUTES.toMillis(1), sensor.getDelay());
		assertEquals("", sensor.getName());
	}
	
	@Test
	public void AllInterfacesSensor() throws Exception {
		AbstractPollingSensor sensor = (AbstractPollingSensor) Main.wrapSensor(new AllInterfacesSensor(), fail );
		assertEquals(-123, sensor.getDelay());
		assertEquals("someName", sensor.getName());
	}
	
	@Test
	public void testWrapSomeSensor() throws Exception {
		Main.wrapSensor(new SomeSensor(), fail );
	}
	
	@Test
	public void testWrapTimeSensor() throws Exception {
		Main.wrapSensor(new TimeSensor(), fail );
	}

}
