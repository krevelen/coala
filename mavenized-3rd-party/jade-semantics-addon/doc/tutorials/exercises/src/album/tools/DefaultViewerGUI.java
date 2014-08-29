package album.tools;


import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.io.ByteArrayInputStream;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class DefaultViewerGUI extends JFrame implements ViewerGUI {

	static final long serialVersionUID = 3L;
	
	PhotoPanel photoPanel = new PhotoPanel();
	
	//-------------------------------------------------------
	public DefaultViewerGUI(String name) {
	//-------------------------------------------------------

		super(name);
		// Frame panel
		//------------
		JPanel panel = new JPanel(new BorderLayout());
		getContentPane().add(panel);
		panel.add(photoPanel);

		// Show frame
		//-----------
		setSize(400, 400);
		setVisible(true);
	}
	
	public void displayPhoto(byte[] bytes) {
		photoPanel.setPhoto(bytes);
	}
	
	// main method to test the default viewer GUI
	static public void main(String[] args) {
		DefaultViewerGUI viewer = new DefaultViewerGUI("toto");
		viewer.displayPhoto(JPEGUtilities.toBytesArray(args[0]));
	}
	
	// Inner Class to handle the photo panel
	public class PhotoPanel extends JPanel {

		static final long serialVersionUID = 3L;
		
		Image photo = null;
			
		public void setPhoto(byte[] bytes)
		{
			photo = JPEGUtilities.load(new ByteArrayInputStream(bytes));
			repaint();
		}
			
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if ( photo != null ) {
				g.drawImage(photo, (getWidth()-photo.getWidth(this))/2, (getHeight()-photo.getHeight(this))/2, this);
			}
		}	
	}

}
