package a7;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ImagePictureViewWidget extends JPanel implements MouseListener {
	private PictureView picture_view;
	private JPanel controller_panel, label_panel, slider_panel;
	private JSlider blurSlider, saturationSlider, brightnessSlider;
	/*
	 * the dimensions of the picture object encapsulated
	 * by picture_view
	 */
	private int height;
	private int width;

	public ImagePictureViewWidget(Picture picture) {
		setLayout(new BorderLayout());

		picture_view = new PictureView(picture.createObservable());
		picture_view.addMouseListener(this);
		add(picture_view, BorderLayout.CENTER);
		height = picture_view.getPicture().getHeight();
		width = picture_view.getPicture().getWidth();

		controller_panel = new JPanel();
		controller_panel.setLayout(new BorderLayout());
		add(controller_panel, BorderLayout.SOUTH);

		label_panel = new JPanel();
		label_panel.setLayout(new GridLayout(3,0));
		controller_panel.add(label_panel, BorderLayout.LINE_START);

		slider_panel = new JPanel();
		slider_panel.setLayout(new GridLayout(3,0));
		controller_panel.add(slider_panel, BorderLayout.LINE_END);

		JLabel blurLabel = new JLabel("Blur: ");
		blurSlider = new JSlider(JSlider.HORIZONTAL,0,5,0);
		blurSlider.setMajorTickSpacing(1);
		blurSlider.setPaintTicks(true);
		blurSlider.setPaintLabels(true);
		blurSlider.setSnapToTicks(true);
		blurSlider.addChangeListener
		(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				/*
				 * value is the value to which the slider is set
				 */
				int value = blurSlider.getValue();
				picture_view.setPicture(blurProcess(value));
			}//end of stateChanged
		});//end of new ChangeListener statement

		JLabel saturationLabel = new JLabel("Saturation: ");
		saturationSlider = new JSlider(JSlider.HORIZONTAL,-100,100,-100);
		saturationSlider.setMajorTickSpacing(25);
		saturationSlider.setSnapToTicks(false);
		saturationSlider.setPaintTicks(true);
		saturationSlider.setPaintLabels(true);
		saturationSlider.addChangeListener
		(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int value = saturationSlider.getValue();
			}
		});

		JLabel brightnessLabel = new JLabel("Brightness: ");
		brightnessSlider = new JSlider(JSlider.HORIZONTAL,-100,100,-100);
		brightnessSlider.setMajorTickSpacing(25);
		brightnessSlider.setSnapToTicks(false);
		brightnessSlider.setPaintTicks(true);
		brightnessSlider.setPaintLabels(true);
		brightnessSlider.addChangeListener
		(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int value = brightnessSlider.getValue();
			}
		});

		label_panel.add(blurLabel);
		slider_panel.add(blurSlider);
		label_panel.add(saturationLabel);
		slider_panel.add(saturationSlider);
		label_panel.add(brightnessLabel);
		slider_panel.add(brightnessSlider);
	}
	//HELPER METHOD
	public boolean isWithinRegion(Region r, Coordinate c)  {
		if(c.getX() > r.getLeft() && c.getX() < r.getRight() &&
				c.getY() > r.getTop() && c.getY() < r.getBottom()) {
			return true;
		}else{
			return false;
		}
	}
	//HELPER METHOD
	public ObservablePicture blurProcess(int sliderValue) {
		ObservablePicture blurPic = picture_view.getPicture();
		Coordinate a = new Coordinate(0,0);
		Coordinate b = new Coordinate(blurPic.getWidth(), blurPic.getHeight());
		Region originalRegion = new RegionImpl(a,b);
		/*
		 * outer for loops iterate through each pixel in the picture_view object
		 */
		for(int i = 0; i < width; i++) {
			for(int j = 0;j < height; j++ ) {
				Coordinate c = new Coordinate(i-sliderValue, j-sliderValue);
				Coordinate d = new Coordinate(i+sliderValue, j+sliderValue);
				Region blurringRegion = new RegionImpl(c,d);
				while(isWithinRegion(originalRegion, c) &&
						isWithinRegion(originalRegion, d)) {
					Pixel p = averagePixel(blurringRegion, blurPic, i, j, sliderValue);
					blurPic.setPixel(i, j, p);
				}//end of while loop
			}//---
		}//---end of for loop
		
		return blurPic;
	}//end of blurProcess method
	//HELPER METHOD
	/*
	 * 
	 */
	private Pixel averagePixel(Region r, ObservablePicture o, int x, int y, int sv) {
		/*
		 * inner for loops iterate through each pixel in the new
		 * region surrounding the pixel to be blurred
		 */
		double redSum = 0.0;
		double redAverage = 0.0;
		double greenSum = 0.0;
		double greenAverage = 0.0;
		double blueSum = 0.0;
		double blueAverage = 0.0;
		/*
		 * x should be the value of i from the outer for loop
		 * of the blurProcess method
		 * 
		 * y should be the value of j from the inner for loop
		 * of the blurProcess method
		 * 
		 * sv should be the sliderValue
		 * 
		 * r should be the region within which all the pixels' values
		 * are averaged except for the pixel to be blurred
		 * 
		 * o should be the ObservablePicture to be blurred
		 */
		int pixelsInBlurringRegion = (x+sv) * (y+sv);
		for(int i = 0; i < r.getRight(); i++) {
			for(int j = 0; j < r.getBottom(); j++) {
				while(i != x && j != y) {
					redSum += o.getPixel(i, j).getRed();
					greenSum += o.getPixel(i,j).getGreen();
					blueSum += o.getPixel(i,j).getBlue();
				}
			}//---
		}//---end of inner for loop
		/*
		 * average all adjacent pixels and replace
		 * each pixel with each average of all 
		 * its surrounding pixels
		 */
		redAverage = redSum / pixelsInBlurringRegion;
		greenAverage = greenSum / pixelsInBlurringRegion;
		blueAverage = blueSum / pixelsInBlurringRegion;
		return new ColorPixel(redAverage, greenAverage, blueAverage);
	}//end of getAveragePixel method

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

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
