package plugins.faubin.cytomine.utils.tileViewer;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import be.cytomine.client.CytomineException;

public class CytomineMouseListener implements MouseListener{
	
	private CytomineReader reader;
	
	public CytomineMouseListener(CytomineReader reader) {
		this.reader = reader;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		if (!e.isConsumed()) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				reader.mouseGrabbed = false;

				try {
					reader.read();

				} catch (CytomineException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

			}
			e.consume();
		}
	}

	@Override
	public void mousePressed(final MouseEvent e) {
		// TODO Auto-generated method stub
		if (!e.isConsumed()) {
			if (e.getButton() == MouseEvent.BUTTON1) {

				if (!reader.mouseGrabbed) {
					reader.mouseGrabbed = true;

					reader.oldMouseX = e.getXOnScreen();
					reader.oldMouseY = e.getYOnScreen();

					reader.mouseMove();

					e.consume();
				}

			}
		}

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
