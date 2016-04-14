package a7;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ImagePictureViewWidget extends JPanel implements MouseListener {
private PictureView picture_view;

public ImagePictureViewWidget(Picture picture) {
	setLayout(new BorderLayout());
	
	picture_view = new PictureView(picture.createObservable());
	picture_view.addMouseListener(this);
	add(picture_view, BorderLayout.CENTER);
	
	
}

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
