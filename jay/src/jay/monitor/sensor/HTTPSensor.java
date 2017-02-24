package jay.monitor.sensor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Properties;

import jay.monitor.sensor.impl.AbstractPollingSensor;

public class HTTPSensor extends AbstractPollingSensor implements ActionListener {
	private URLSupport url = new URLSupport();

	@Override
	public void configure(Properties props) {
		super.configure(props);
		url.configure(props);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		url.actionPerformed(e);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double poll() {
		try {
			HttpURLConnection con = (HttpURLConnection) url.getUrl().openConnection();
			int responseCode = con.getResponseCode();
			if (200 > responseCode || responseCode >= 300) {
				return 0;
			}
		} catch (IOException e) {
			return 0;
		}
		return 1;
	}


}
