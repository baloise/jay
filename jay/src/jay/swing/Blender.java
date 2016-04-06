package jay.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;

import javax.swing.Timer;

public abstract class Blender extends PropertyChangeSupport implements ActionListener {

	private final Timer timer;
	private double step;
	private int durationMillis;
	private int fps;
	private double from;
	private double to;
	private double inverted;

	public Blender() {
		this(1000);
	}

	public Blender(int durationMillis) {
		this(durationMillis, 24);
	}

	public Blender(int durationMillis, int fps) {
		super(Blender.class);
		this.durationMillis = durationMillis;
		this.fps = fps;
		int delay = 1000 / fps;
		timer = new Timer(delay, this);
		timer.setRepeats(true);
	}

	public void blend(double from, double to) {
		timer.stop();
		if (from < to) {
			this.from = from;
			this.to = to;
			this.inverted = 0;
		} else {
			this.from = 0;
			this.to = from - to;
			this.inverted = from;
		}
		step = (this.to - this.from) * 1000 / (durationMillis * fps);
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (from == to) {
			timer.stop();
		}
		double oldValue = from;
		from += step;
		from = Math.min(from, to);
		double newValue = inverted == 0 ? from : inverted - from;
		PropertyChangeEvent pce = new PropertyChangeEvent(this, "blend", oldValue, newValue);
		firePropertyChange(pce);
	}

}