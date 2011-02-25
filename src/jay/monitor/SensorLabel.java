package jay.monitor;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import jay.monitor.sensor.Sensor;
import jay.swing.AlphaBlender;
import sas.swing.plaf.ShadowLabelUI;

public class SensorLabel extends JLabel implements PropertyChangeListener {

	private static final long serialVersionUID = 1L;
	protected final Sensor sensor;
	public final AutoHide autoHide;
	private final AlphaBlender blender;

	public SensorLabel(Sensor sensor) {
		super(sensor.getName());
		autoHide = new AutoHide(this);
		autoHide.dontHideWhileMouseOver(this);
		this.sensor = sensor;
		sensor.addPropertyChangeListener(this);
		setHorizontalTextPosition(SwingConstants.LEADING);
		setUI(ShadowLabelUI.SHADOW_LABEL_UI);
		setIconTextGap(20);
		setHorizontalAlignment(SwingConstants.RIGHT);
		Font font = new Font("Dialog", Font.PLAIN, 18);
		setFont(font);
		setForeground(Color.white);
		autoHide.setVisible(false);
		blender = new AlphaBlender(this, 500);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(blender.g2d(g));
	}

	@Override
	public void show() {
		blender.in();
	}

	@Override
	public void hide() {
		if (blender != null)
			blender.out();
	}

}
