package jay.swing;

import static java.awt.Color.GREEN;
import static java.awt.Color.RED;
import static java.awt.Color.WHITE;
import static java.awt.MultipleGradientPaint.ColorSpaceType.SRGB;
import static java.awt.MultipleGradientPaint.CycleMethod.NO_CYCLE;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;

/**
 * This class has been automatically generated using <a
 * href="https://flamingo.dev.java.net">Flamingo SVG transcoder</a>.
 */
public class TrafficLightIcon implements javax.swing.Icon {

	private int size;

	/** The rendered image. */
	private BufferedImage image;

	private int percentage;

	private Component component;

	private int margin = 20;

	/**
	 * @return the margin
	 */
	public int getMargin() {
		return margin;
	}

	/**
	 * @param margin
	 *            the margin to set
	 */
	public void setMargin(int margin) {
		this.margin = margin;
	}

	/**
	 * Creates a new transcoded SVG image.
	 */
	public TrafficLightIcon() {
		this(20);
	}

	/**
	 * Creates a new transcoded SVG image.
	 */
	public TrafficLightIcon(int size) {
		this.size = size;
	}

	@Override
	public int getIconHeight() {
		return size + 10;
	}

	@Override
	public int getIconWidth() {
		return size + 10;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		component = c;
		size = Math.min(c.getWidth() - margin, c.getHeight() - margin);
		image = getImage();

		g.drawImage(image, x + margin / 2, y + margin / 2, null);
	}

	/**
	 * Paints the transcoded SVG image on the specified graphics context.
	 *
	 * @param g
	 *            Graphics context.
	 */
	private void paint(Graphics2D g) {
		Shape shape = null;

		java.util.LinkedList<AffineTransform> transformations = new java.util.LinkedList<AffineTransform>();

		//

		// _0
		transformations.offer(g.getTransform());
		g.transform(new AffineTransform(1, 0, 0, 1, 255, 539));

		// _0_0
		transformations.offer(g.getTransform());
		g.transform(new AffineTransform(0.949753f, 0, 0, 0.949753f, -10.31027f, -14.52566f));

		// _0_0_0
		shape = new GeneralPath();
		((GeneralPath) shape).moveTo(54.0, -394.0);
		((GeneralPath) shape).curveTo(54.0, -308.94815, -14.948149, -240.0, -100.0, -240.0);
		((GeneralPath) shape).curveTo(-185.05185, -240.0, -254.0, -308.94815, -254.0, -394.0);
		((GeneralPath) shape).curveTo(-254.0, -479.05185, -185.05185, -548.0, -100.0, -548.0);
		((GeneralPath) shape).curveTo(-14.948149, -548.0, 54.0, -479.05185, 54.0, -394.0);
		((GeneralPath) shape).closePath();

		Color[][] colors = new Color[][] { new Color[] { new Color(0xD5FFD5), new Color(0x80FF80), new Color(0x2AFF2A), GREEN, new Color(0x00D400) },
				new Color[] { new Color(0xFFF6D5), new Color(0xFFE680), new Color(0xFFD42A), new Color(0xFFCC00), new Color(0xD4AA00) },
				new Color[] { new Color(0xFFD5D5), new Color(0xFF8080), new Color(0xFF2A2A), RED, new Color(0xD40000) },

		};
		Color[] icol = interpolate(colors);
		g.setPaint(new RadialGradientPaint(new Point2D.Double(-100, -278), 155, new Point2D.Double(-100, -278), new float[] { 0, 0.36f, 0.79f, 1 }, Arrays
				.copyOf(icol, 4),
				//
				NO_CYCLE, SRGB, new AffineTransform(1.267f, 0, 0, 1.73f, 26.67f, 204.12f)));

		g.fill(shape);
		g.setPaint(icol[4]);

		g.setStroke(new BasicStroke(4, 0, 0, 4));
		g.draw(shape);

		g.setTransform(transformations.poll()); // _0_0_0
		transformations.offer(g.getTransform());
		// g.transform(new AffineTransform(1, 0, 0, 1, -255, -539));

		// _0_0_1
		shape = new GeneralPath();
		((GeneralPath) shape).moveTo(150.0, 10.0);
		((GeneralPath) shape).curveTo(74.0, 10.0, 12.0, 71.0, 10.0, 147.0);
		((GeneralPath) shape).curveTo(49.0, 182.0, 87.0, 183.0, 106.0, 174.0);
		((GeneralPath) shape).curveTo(126.0, 165.0, 142.0, 136.0, 168.0, 129.0);
		((GeneralPath) shape).curveTo(194.0, 123.0, 208.0, 124.0, 249.0, 141.0);
		((GeneralPath) shape).curveTo(272.0, 150.0, 269.0, 119.0, 286.0, 116.0);
		((GeneralPath) shape).curveTo(271.0, 55.0, 216.0, 10.0, 150.0, 10.0);
		((GeneralPath) shape).closePath();

		g.setPaint(new LinearGradientPaint(new Point2D.Double(191, 156), new Point2D.Double(148, 10), new float[] { 0, 1 }, new Color[] {
			new Color(0xFFFFFF, true), WHITE }, NO_CYCLE, SRGB, new AffineTransform()));
		g.fill(shape);

		g.setTransform(transformations.poll()); // _0_0_1

		g.setTransform(transformations.poll()); // _0_0

	}

	private Color[] interpolate(Color[][] colors) {
		float perc = percentage > 50 ? percentage - 50 : percentage;
		float fraction = perc / 50F;
		int from = percentage > 50 ? 1 : 0;
		int to = percentage > 50 ? 2 : 1;
		int i = 0;
		return new Color[] {
				//
				ColorInterpolator.interpolateColor(colors[from][i], colors[to][i++], fraction),//
				ColorInterpolator.interpolateColor(colors[from][i], colors[to][i++], fraction),//
				ColorInterpolator.interpolateColor(colors[from][i], colors[to][i++], fraction),//
				ColorInterpolator.interpolateColor(colors[from][i], colors[to][i++], fraction),//
				ColorInterpolator.interpolateColor(colors[from][i], colors[to][i++], fraction),//
		};
	}

	Blender blender = new Blender() {
		{
			addPropertyChangeListener(new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					setPercentage(((Double) evt.getNewValue()).intValue());
				}
			});
		}
	};

	public BufferedImage getImage() {
		image = new BufferedImage(getIconWidth(), getIconHeight(), BufferedImage.TYPE_INT_ARGB);
		double coef = Math.min((double) size / 297, (double) size / 297);
		Graphics2D g2d = image.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.scale(coef, coef);
		paint(g2d);
		g2d.dispose();
		return image;
	}

	public Blender getBlender() {
		return blender;
	}

	public void blendToPercentage(int percentage) {
		blender.blend(this.percentage, percentage);
	}

	public void setPercentage(int percentage) {
		if (this.percentage == percentage)
			return;
		this.percentage = percentage;
		if (component != null)
			component.repaint();
	}

  public TrafficLightIcon setComponent(Component component) {
    this.component= component; 
    return this;
  }
}
