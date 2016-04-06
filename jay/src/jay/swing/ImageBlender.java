package jay.swing;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import javax.swing.Timer;

public abstract class ImageBlender implements ActionListener {
	private Image fromImage,toImage;
	private float fromAlpha,toAlpha;
	private final float step;
	private final Timer timer;
	private final ImageObserver observer;

	
	public ImageBlender(Image image, ImageObserver observer) {
		this(image, observer, 1000);
	}

	public ImageBlender(Image image, ImageObserver observer, int durationMillis) {
		this(image,observer, durationMillis, 24);
	}
	
	public ImageBlender(Image image, ImageObserver observer, int durationMillis, int fps) {
		this.observer = observer;
		fromImage = image;
		toImage = image;
		fromAlpha = 1f;
		toAlpha = 0f;
		step = 1000f/ (durationMillis  * fps) ;
		int delay = 1000 / fps;
		timer = new Timer(delay, this);
		timer.setRepeats(true);
	}
	public void blendTo(Image image) {
		timer.stop(); // avoid concurrency
		fromImage = blend();
		toImage = image;
		fromAlpha = 1f;
		toAlpha = 0f;
		timer.start();
	}
	private Image blend() {
		waitForDestinationImageToLoad();
		BufferedImage ret = new BufferedImage( toImage.getWidth(observer), toImage.getHeight(observer), BufferedImage.TYPE_INT_ARGB );  
		Graphics2D g2d = (Graphics2D) ret.getGraphics();
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fromAlpha));
		g2d.drawImage(fromImage, 0, 0, observer);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, toAlpha));
		g2d.drawImage(toImage, 0, 0, observer);
		return ret;
	}

	private void waitForDestinationImageToLoad() {
		while(toImage.getWidth(observer)<0){}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if( toAlpha == 1){
			timer.stop();
		}
		toAlpha  += step ;
		fromAlpha  -= step ;
		toAlpha = Math.min(toAlpha, 1);
		fromAlpha = Math.max(fromAlpha, 0);
		update(blend());
	}
	protected abstract void update(Image image);
}
