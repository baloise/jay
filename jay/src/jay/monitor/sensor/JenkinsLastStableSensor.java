package jay.monitor.sensor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jay.monitor.sensor.impl.AbstractPollingSensor;

public class JenkinsLastStableSensor extends AbstractPollingSensor implements ActionListener {

	private URLSupport url = new URLSupport();
	long yellowMillies, redMillies;

	@Override
	public void configure(Properties props) {
		super.configure(props);
		props.setProperty("url", props.getProperty("url")+"/lastStableBuild/api/xml");
		url.configure(props);
		yellowMillies = Long.valueOf(props.getProperty("yellowMinutes")) * 60L * 1000L;
		redMillies = Long.valueOf(props.getProperty("redMinutes")) * 60L * 1000L;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		url.actionPerformed(e);
	}

	@Override
	public double poll() {
		try {
			Matcher matcher = Pattern.compile("<timestamp>(\\d+)</timestamp>").matcher(url.readUrl());
			matcher.find();
			long duration = System.currentTimeMillis() - Long.valueOf(matcher.group(1));
			if(duration > redMillies) {
				return 0;
			}
			if(duration > yellowMillies) {
				return 0.5;
			}
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

}
