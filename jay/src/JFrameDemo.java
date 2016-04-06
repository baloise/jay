import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jay.swing.TrafficLightIcon;

public class JFrameDemo {

	public static void main(String s[]) {
		JFrame frame = new JFrame("JFrame Source Demo");
		// Add a window listner for close button
		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		final TrafficLightIcon icon = new TrafficLightIcon(200);
		icon.setPercentage(50);
		// This is an empty content area in the frame
		JLabel jlbempty = new JLabel("", icon, JLabel.CENTER);
		// jlbempty.setPreferredSize(new Dimension(175, 100));
		frame.getContentPane().add(jlbempty, BorderLayout.CENTER);

		final JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
		frame.getContentPane().add(slider, BorderLayout.NORTH);
		ChangeListener listi = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				icon.blendToPercentage(slider.getValue());
			}
		};
		slider.addChangeListener(listi);

		final JSlider greenYellowRed = new JSlider(JSlider.HORIZONTAL, 0, 2, 1);
		greenYellowRed.setSnapToTicks(true);
		greenYellowRed.setPaintTicks(true);
		frame.getContentPane().add(greenYellowRed, BorderLayout.SOUTH);
		ChangeListener listi2 = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				slider.setValue(greenYellowRed.getValue() * 50);
				// icon.setPercentage(greenYellowRed.getValue() * 50);
			}
		};
		greenYellowRed.addChangeListener(listi2);

		frame.pack();
		frame.setVisible(true);
	}
}