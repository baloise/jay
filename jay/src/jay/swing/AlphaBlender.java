package jay.swing;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class AlphaBlender implements ActionListener {

	private final Component component;
	private final float step;
	private float alpha;
	public float getAlpha() {
		return alpha;
	}

	private final Timer timer;
	private float direction;
	

	public AlphaBlender(Component component) {
		this(component, 1000);
	}

	public AlphaBlender(Component component, int durationMillis) {
		this(component, durationMillis, 24);
	}

	public AlphaBlender(Component component, int durationMillis, int fps) {
		this.component = component;
		step = 1000f/ (durationMillis  * fps) ;
		int delay = 1000 / fps;
		alpha = 0;
		timer = new Timer(delay, this);
		timer.setRepeats(true);
	}

	public void addActionListener(ActionListener listener) {
		timer.addActionListener(listener);
	}

	public void removeActionListener(ActionListener listener) {
		timer.removeActionListener(listener);
	}

	public void in() {
		direction = 1;
		timer.start();
	}

	public void out() {
		direction = -1;
		timer.start();
	}

	public Graphics2D g2d(Graphics g) {
		Graphics2D ret = (Graphics2D) g;
		ret.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		return ret;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if((direction == -1 && alpha == 0)
				|| (direction == 1 && alpha == 1)
				){
			timer.stop();
		}
		alpha += step * direction;
		alpha = Math.max(alpha, 0);
		alpha = Math.min(alpha, 1);
		if(component != null) {
			component.repaint();
		}
	}

	public boolean isVisible() {
		return alpha>0;
	}


}
