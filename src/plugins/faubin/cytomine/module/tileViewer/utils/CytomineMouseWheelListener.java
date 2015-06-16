package plugins.faubin.cytomine.module.tileViewer.utils;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import plugins.faubin.cytomine.module.tileViewer.CytomineReader;

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
				reader.inc_zoom();
				break;
			case -1:
				reader.dec_zoom();
				break;

			default:
				break;
			}
			
			e.consume();
		}

	}

}
