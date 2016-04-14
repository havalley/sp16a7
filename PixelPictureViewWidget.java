package a7;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class PixelPictureViewWidget extends JPanel implements MouseListener {
	private PictureView picture_view;
	private JPanel info_panel;
	private JLabel x, y, red, green, blue, brightness;

	public PixelPictureViewWidget(Picture picture) {
		setLayout(new BorderLayout());

		picture_view = new PictureView(picture.createObservable());
		picture_view.addMouseListener(this);
		add(picture_view, BorderLayout.CENTER);

		info_panel = new JPanel();
		info_panel.setLayout(new GridLayout(6,0));
		add(info_panel, BorderLayout.WEST);

		x = new JLabel("X: ");
		info_panel.add(x);
		y = new JLabel("Y: ");
		info_panel.add(y);
		red = new JLabel("Red: ");
		info_panel.add(red);
		green = new JLabel("Green: ");
		info_panel.add(green);
		blue = new JLabel("Blue: ");
		info_panel.add(blue);
		brightness = new JLabel("Brightness: ");
		info_panel.add(brightness);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Pixel pix = this.picture_view.getPicture().getPixel(e.getX(), e.getY());
		x.setText("X: " + e.getX());
		y.setText("Y: " + e.getY());
		red.setText("Red: " + roundTwo(pix.getRed()));
		green.setText("Green: " + roundTwo(pix.getGreen()));
		blue.setText("Blue: " + roundTwo(pix.getBlue()));
		brightness.setText("Brightness: " +  roundTwo(pix.getIntensity()));	
	}

	private double roundTwo(double value) {
		final int DECIMAL_PLACES = 2;
		int temp = (int)(value * Math.pow(10, DECIMAL_PLACES));
		double rounded = ((double)temp)/Math.pow(10, DECIMAL_PLACES);
		return rounded;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
