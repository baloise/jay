package jay.monitor;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import jay.monitor.sensor.Sensor;
import jay.swing.ImageBlender;

public class SensorIcon extends JComponent implements PropertyChangeListener {

	protected final Sensor sensor;
	final Image red, green;
	Image current;
	final Dimension dim = new Dimension(64, 64);
	private ImageBlender blender;

	public SensorIcon(Sensor sensor) {
		green = Toolkit.getDefaultToolkit().getImage(
				"32px-Button_Icon_Green.svg.drop.png");
		red = Toolkit.getDefaultToolkit().getImage(
				"32px-Button_Icon_Red.svg.drop.png");
		current = red;
		this.sensor = sensor;
		sensor.addPropertyChangeListener(this);
		blender = new ImageBlender(red, this) {			
			@Override
			protected void update(Image image) {
				current = image;
				repaint();
			}
		};
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
    if ((Long)evt.getNewValue() == 1) {
      blender.blendTo(green);
    }
    else {
      blender.blendTo(red);
    }
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		int dx = (getWidth() - 32) / 2;
		int dy = (getHeight() - 32) / 2;
		g2d.drawImage(current, dx, dy, this);
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
