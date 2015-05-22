package plugins.faubin.cytomine.gui.tileViewer;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class CytomineMouseWheelListener implements MouseWheelListener{

	private CytomineReader reader;
	
	public CytomineMouseWheelListener(CytomineReader reader) {
		this.reader = reader;
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (!e.isConsumed()) {
			switch (e.getWheelRotation()) {
			case 1:
				reader.inc_zoom(e.getPoint());
				break;
			case -1:
				reader.dec_zoom(e.getPoint());
				break;

			default:
				break;
			}
			e.consume();
		}

	}

}
