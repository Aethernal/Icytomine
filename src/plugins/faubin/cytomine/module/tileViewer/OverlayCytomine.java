package plugins.faubin.cytomine.module.tileViewer;

import icy.canvas.IcyCanvas;
import icy.painter.Overlay;
import icy.type.point.Point5D;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/*
 * this overlay receive user interaction on the sequence like mouse click and mouse wheel interaction
 * then it transmit the command to the correct Listener 
 */

public class OverlayCytomine extends Overlay{
	private CytomineReader reader;
	
	public OverlayCytomine(CytomineReader reader) {
		super("Cytomine Overlay");
		this.reader=reader;
	}
	
	@Override
	public void mousePressed(MouseEvent e, Point5D.Double imagePoint,
			IcyCanvas canvas) {
		super.mousePressed(e, imagePoint, canvas);
		if (reader.locked) {
			reader.mouseListener.mousePressed(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e, Point5D.Double imagePoint,
			IcyCanvas canvas) {
		super.mouseReleased(e, imagePoint, canvas);
		if (reader.locked) {
			reader.mouseListener.mouseReleased(e);
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e,
			Point5D.Double imagePoint, IcyCanvas canvas) {
		super.mouseWheelMoved(e, imagePoint, canvas);
		if (reader.locked) {
			reader.wheelListener.mouseWheelMoved(e);
		}
	}

	@Override
	public void keyPressed(KeyEvent e, Point5D.Double imagePoint,
			IcyCanvas canvas) {
		super.keyPressed(e, imagePoint, canvas);
		if (e.getKeyCode() == 32) {
			reader.locked = !reader.locked;
			reader.mouseGrabbed = false;
		}
	}
	
}
