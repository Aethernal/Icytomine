package plugins.faubin.cytomine.module.main.mvc.custom;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTabbedPane;
import javax.swing.plaf.metal.MetalTabbedPaneUI;

/**
 * @author faubin
 * this class is a custom ui for the tabs of the tabbed pane, it has a black cross used to remove the clicked tab to close.
 */
public class CustomTabbedPaneUI extends MetalTabbedPaneUI {
	Rectangle xRect;

	protected void installListeners() {
		super.installListeners();
		tabPane.addMouseListener(new MyMouseHandler());
	}

	protected void paintTab(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect) {
		super.paintTab(g, tabPlacement, rects, tabIndex, iconRect, textRect);

		
		int size = textRect.height ;
		xRect = new Rectangle(textRect.x + textRect.width - size / 2 - 2, textRect.y
				+ textRect.height / 2 - size / 2, size, size);

		/*
		 * black cross allowing to close the tab
		 */
		
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

	
	/**
	 * @author faubin
	 * this class allow to detect witch tab was pressed and delete it from the tabbed pane
	 */
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