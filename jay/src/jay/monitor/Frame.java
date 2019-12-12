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

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;

import jay.monitor.AutoHide.VISIBILITY;
import jay.swing.AlphaBlender;
import jay.swing.FancyDialog8;
import jay.swing.PnutsLayout;

public class Frame extends FancyDialog8 implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	public final AutoHide autoHide;
	private Image pin;
	private final AlphaBlender blender;
	private final static boolean IS_MAC = System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0;
	
	public Frame() {
		autoHide = new AutoHide(this,4500);
		pin = loadImage("pin.png");
		setMinimumSize(new Dimension(10, 10));
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setLocation();
		setLayout(new PnutsLayout("border=false,columns=2"));
		add(new JLabel(),"padding=10");
		add(new JLabel());
		blender = new AlphaBlender(null,100);
		blender.addActionListener(this);
		setOpacity(0f);
		super.show();
	}

	private Image loadImage(String name) {
		try {
			return ImageIO.read(getClass().getResourceAsStream("/"+name));
		} catch (Exception e) {
			try {
				return Toolkit.getDefaultToolkit().getImage("res/"+name);
			} catch (Exception ex) {
				 throw new IllegalStateException(ex);
			 }
		}
	}
	
	@Override
	public void setVisible(boolean b) {
		autoHide.setVisible(b);
	}

	public void setLocation() {
		pack();
		Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(
				FancyDialog8.getTranslucencyCapableGC());
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		if(IS_MAC){
			setLocation((int) size.getWidth() - insets.right - getWidth(),
					insets.top);			
		} else {
			setLocation((int) size.getWidth() - insets.right - getWidth(),
					(int) size.getHeight() - insets.bottom - getHeight());			
		}
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
		setOpacity(blender.getAlpha());
	}
}
