package jay.monitor;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

import jay.monitor.AutoHide.VISIBILITY;
import jay.monitor.sensor.Sensor;

public class DefaultSensorUI implements SensorUI {
	private final SensorLabel label;
	private final SensorIcon icon;
	private final Sensor sensor;

	public DefaultSensorUI(final Sensor sensor) {
		label = new SensorLabel(sensor);
		icon = new SensorIcon(sensor);
		label.autoHide.dontHideWhileMouseOver(icon);
		MouseListener listener = new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				label.autoHide.setVisibility(VISIBILITY.PERMANENTLY_VISIBLE);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				label.autoHide.setVisibility(VISIBILITY.TEMPORARLY_VISIBLE);
			}
		};
		icon.addMouseListener(listener);
		this.sensor = sensor;
	}

	@Override
	public JComponent getIcon() {
		return icon;
	}

	@Override
	public JComponent getLabel() {
		return label;
	}

	@Override
	public Sensor getSensor() {
		return sensor;
	}

}
