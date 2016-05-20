package jay.monitor.sensor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import jay.monitor.sensor.impl.AbstractSensor;

public class ToggleSensor extends AbstractSensor implements ActionListener {

	@Override
	public void run() {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		setValue(getValue() <1 ? 1 : 0);

	}

}
