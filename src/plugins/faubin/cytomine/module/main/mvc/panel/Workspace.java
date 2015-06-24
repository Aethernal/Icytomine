package plugins.faubin.cytomine.module.main.mvc.panel;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import plugins.faubin.cytomine.module.main.mvc.View;

/**
 * @author faubin
 * it is the module workspace where the main information of the module should be displayed
 */
public abstract class Workspace extends JPanel {

	protected JScrollPane scroll;
	public Workspace() {
		setLayout(new BorderLayout());
		scroll = new JScrollPane();
		add(scroll,BorderLayout.CENTER);
	}

	public abstract View getView();
	
}
