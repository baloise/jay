package jay.monitor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;

import jay.monitor.AutoHide.VISIBILITY;
import jay.swing.FancyDialog67;
import jay.swing.FancyFrame67;
import jay.swing.AlphaBlender;
import jay.swing.PnutsLayout;

public class Frame extends FancyDialog67 implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	public final AutoHide autoHide;
	private final Image pin;
	private final AlphaBlender blender;
	
	public Frame() {
		autoHide = new AutoHide(this,4500);
		pin = Toolkit.getDefaultToolkit().getImage("pin.png");
		setMinimumSize(new Dimension(10, 10));
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setLocation();
		setLayout(new PnutsLayout("border=false,columns=2"));
		add(new JLabel(),"padding=10");
		add(new JLabel());
		blender = new AlphaBlender(null,100);
		blender.addActionListener(this);
		setWindowOpacity(0f);
		super.show();
	}
	
	@Override
	public void setVisible(boolean b) {
		autoHide.setVisible(b);
	}

	public void setLocation() {
		pack();
		Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(
				FancyFrame67.getTranslucencyCapableGC());
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((int) size.getWidth() - insets.right - getWidth(),
				(int) size.getHeight() - insets.bottom - getHeight());
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if(autoHide.getVisibility() == VISIBILITY.PERMANENTLY_VISIBLE){			
			Graphics2D g2d = (Graphics2D) g; 
			g2d.drawImage(pin, getWidth()-pin.getWidth(this)-5, 0, this);
		}
	}
	
	@Override
	public void show() {
		blender.in();
	}
	
	@Override
	public void hide() {
		blender.out();
	}
	
	@Override
	protected void addImpl(Component comp, Object constraints, int index) {
		super.addImpl(comp, constraints, index);
		if(autoHide != null) 
			autoHide.dontHideWhileMouseOver(comp);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		setWindowOpacity(blender.getAlpha());
	}
}
