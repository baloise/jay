package jay.monitor;

import java.awt.Dimension;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import jay.monitor.sensor.Sensor;
import jay.swing.DropShadowPanel;
import jay.swing.TrafficLightIcon;

public class SensorIcon extends JComponent implements PropertyChangeListener {

	protected final Sensor sensor;
	final Dimension dim = new Dimension(64, 64);
	private TrafficLightIcon icon = new TrafficLightIcon(48);
	private DropShadowPanel dsp = new DropShadowPanel(icon.getImage());

	public SensorIcon(Sensor sensor) {
		this.sensor = sensor;
		sensor.addPropertyChangeListener(this);
		icon.setPercentage(100);
		dsp.setSize(64, 64);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		icon.blendToPercentage((int) (100 - 100D * (Double) evt.getNewValue()));
	}

	@Override
	public void paint(Graphics g) {
		dsp.setSubject(icon.getImage());
		dsp.paintComponent(g);
		// icon.paintIcon(this, g, 0, 0);
	}

	@Override
	public Dimension getMinimumSize() {
		return dim;
	}

	@Override
	public Dimension getMaximumSize() {
		return dim;
	}

	@Override
	public Dimension getPreferredSize() {
		return this.getMinimumSize();
	}
}
