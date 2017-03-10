package jay;

import static jay.DuckType.does;
import static jay.DuckType.let;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import jay.monitor.sensor.Polling;
import jay.monitor.sensor.Sensor;

public class DuckTypeTest {

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	 
	public interface Duck {
		String walk();
		String talk();
	}
	class Chicken {
		public String walk() { return "Chicken walks"; }
	}
	class Hen {
		public String walk() { return "Hen walks"; }
		public String talk() { return "Hen talks"; }
	}

	class PollingSensor {
		public double poll() {
			return 0.5;
		}
	}
	

	private Chicken chicken;
	private Hen hen;
	private PollingSensor pollingSensor;
	
	@Before
	public void setUp() throws Exception {
		chicken = new Chicken();
		hen = new Hen();
		pollingSensor = new PollingSensor();
	}
	
	@Test
	public void pollingSensorIsNoSensor() {
		assertFalse(does(pollingSensor).quackLike(Sensor.class));
	}
	
	@Test
	public void pollingSensorIsPolling() {
		assertTrue(does(pollingSensor).quackLike(Polling.class));
	}
	
	@Test
	public void chickenDoesNotQuackLikeDuck() {
		assertFalse(does(chicken).quackLike(Duck.class));
	}
	
	@Test
	public void chickenCannotBeDuck() throws Exception {
		expectedEx.expect(InvocationTargetException.class);
		expectedEx.expectMessage(getClass().getName()+"$Chicken does not implement\n" + 
				" - public abstract java.lang.String "+getClass().getName()+"$Duck.talk()");
		let(chicken).be(Duck.class);
	}
	
	@Test
	public void letChickenWannBeDuck() {
		Duck duck = let(chicken).wannaBe(Duck.class);
		assertEquals("Chicken walks", duck.walk());
	}
	
	@Test
	public void duckTypedHashCode() {
		assertEquals(chicken.hashCode(), let(chicken).wannaBe(Duck.class).hashCode());
	}
	
	@Test
	public void duckTypedEquals() {
		assertEquals(let(chicken).wannaBe(Duck.class), chicken);
	}
	
	@Test
	public void henDoesQuackLikeDuck() {
		assertTrue(does(hen).quackLike(Duck.class));
	}
	
	@Test
	public void letHenBeDuck() throws InvocationTargetException {
		Duck duck = let(hen).be(Duck.class);
		assertEquals("Hen walks", duck.walk());
		assertEquals("Hen talks", duck.talk());
	}

}