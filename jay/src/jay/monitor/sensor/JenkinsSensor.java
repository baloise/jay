package jay.monitor.sensor;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import jay.monitor.sensor.impl.AbstractPollingSensor;

public class JenkinsSensor extends AbstractPollingSensor implements ActionListener {

	Jenkins jenkins;
	private URLSupport url = new URLSupport();

	@Override
	public void configure(Properties props) {
		super.configure(props);
		url.configure(props);
		jenkins = new Jenkins(url.getUrl());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		url.actionPerformed(e);
	}

	@Override
	protected double poll() {
		jenkins.update();
		if (Color.green.equals(jenkins.color)) {
			return 1;
		} else if (Color.yellow.equals(jenkins.color)) {
			return 0.5;
		} else {
			return 0;
		}
	}

}
