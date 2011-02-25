package jay.monitor;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Timer;


public class AutoHide {
	public static enum VISIBILITY{
		HIDDEN, TEMPORARLY_VISIBLE, PERMANENTLY_VISIBLE;
		public static VISIBILITY valueOf(boolean b){
			return b ? 
						VISIBILITY.PERMANENTLY_VISIBLE
					:
						VISIBILITY.HIDDEN
					;
		}
		public boolean booleanValue(){
			return this != HIDDEN;
		}
		public VISIBILITY shift(){
			return VISIBILITY.values()[(ordinal()+1)%VISIBILITY.values().length];
		}
	}
	public VISIBILITY getVisibility() {
		return visibility;
	}

	public void shiftVisibility() {
		setVisibility(visibility.shift());
	}
		
	public void setVisibility(VISIBILITY visibility) {
		this.visibility = visibility;
		component.show(visibility.booleanValue());
		switch (visibility) {
		case TEMPORARLY_VISIBLE:
			component.repaint();
			timer.start();		
			break;
		case PERMANENTLY_VISIBLE:
			component.repaint();		
		case HIDDEN:
			timer.stop();		
			break;
		}
		
	}
	
	public void setVisible(boolean b) {
		setVisibility(VISIBILITY.valueOf(b));
	}

	private VISIBILITY visibility;
	private final Timer timer;
	private final MouseListener mouseListener;
	private final Component component;
	public AutoHide(final Component component) {
		this(component, 3000);
	}
	
	public AutoHide(final Component component, int delay) {
		this.component = component;
		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				component.setVisible(false);
			}
		};
		Timer ret =  new Timer(delay, listener );
		ret.setRepeats(false);
		timer = ret;
		visibility = VISIBILITY.HIDDEN;
		mouseListener = new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				timer.stop();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if(visibility == VISIBILITY.TEMPORARLY_VISIBLE)
					timer.start();
			}
		};
	}
	
	public void setDelay(int delay) {
		timer.setDelay(delay);
	}

	public void dontHideWhileMouseOver(Component comp) {
		comp.addMouseListener(mouseListener);
	}
	public void hideWhileMouseOver(Component comp) {
		comp.removeMouseListener(mouseListener);
	}
}
