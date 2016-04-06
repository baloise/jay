package jay.monitor;

import static jay.monitor.DuckType.does;
import static jay.monitor.DuckType.let;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import jay.monitor.sensor.Sensor;
import jay.swing.DropShadowPanel;
import jay.swing.TrafficLightIcon;

public class SensorIcon extends JComponent implements PropertyChangeListener {

	protected final Sensor sensor;
	protected final MouseListener mouseListener = new MouseAdapter() {
	  @Override
	  public void mouseClicked(MouseEvent e) {
	    if(e.getClickCount() == 2) {
	      if(does(sensor).quackLike(ActionListener.class)) {
	       let(sensor).wannaBe(ActionListener.class).actionPerformed(null);
	      }
	    }
	  }
	};
  
	final Dimension dim = new Dimension(64, 64);
	private TrafficLightIcon icon = new TrafficLightIcon(48).setComponent(this);
	private DropShadowPanel dsp = new DropShadowPanel(icon.getImage());

	public SensorIcon(Sensor sensor) {
		this.sensor = sensor;
		sensor.addPropertyChangeListener(this);
		icon.setPercentage(100);
		dsp.setSize(64, 64);
		this.addMouseListener(mouseListener);
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
