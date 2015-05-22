package plugins.faubin.cytomine.gui.mvc.view.custom;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTabbedPane;
import javax.swing.plaf.metal.MetalTabbedPaneUI;

public class CustomTabbedPaneUI extends MetalTabbedPaneUI {
	Rectangle xRect;

	protected void installListeners() {
		super.installListeners();
		tabPane.addMouseListener(new MyMouseHandler());
	}

	protected void paintTab(Graphics g, int tabPlacement, Rectangle[] rects,
			int tabIndex, Rectangle iconRect, Rectangle textRect) {
		super.paintTab(g, tabPlacement, rects, tabIndex, iconRect, textRect);

		
		int size = textRect.height ;
		xRect = new Rectangle(textRect.x + textRect.width - size / 2 - 2, textRect.y
				+ textRect.height / 2 - size / 2, size, size);


		// border
//		g.drawRect(textRect.x + textRect.width - size / 2 - 2, textRect.y
//				+ textRect.height / 2 - size / 2, size, size);

		// red cross
		g.setColor(Color.BLACK);
		g.drawLine(textRect.x + textRect.width - size / 2 - 2 + 3, textRect.y
				+ textRect.height / 2 - size / 2 + 3, textRect.x
				+ textRect.width - size / 2 - 2 + size - 3, textRect.y
				+ textRect.height / 2 - size / 2 + size - 3);
		g.drawLine(textRect.x + textRect.width - size / 2 - 2 + 3, textRect.y
				+ textRect.height / 2 - size / 2 + size - 3, textRect.x
				+ textRect.width - size / 2 - 2 + size - 3, textRect.y
				+ textRect.height / 2 - size / 2 + 3);

	}

	public class MyMouseHandler extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			if (xRect.contains(e.getPoint())) {
				JTabbedPane tabPane = (JTabbedPane) e.getSource();
				int tabIndex = tabForCoordinate(tabPane, e.getX(), e.getY());
				tabPane.remove(tabIndex);
			}
		}
	}
}