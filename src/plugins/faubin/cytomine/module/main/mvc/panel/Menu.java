package plugins.faubin.cytomine.module.main.mvc.panel;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * @author faubin
 * this is the menu of a module, it should contain button and action listener that will invoke view function then controller function for a specific task
 */
public abstract class Menu extends JPanel {

	public Menu() {
		setLayout(new GridLayout(6, 1, 0, 0));
	}

}
