package plugins.faubin.cytomine.oldgui.mvc.template;

import javax.swing.JTabbedPane;

public abstract class Controller {
	
	protected JTabbedPane tabbedPane;
	
	public Controller(JTabbedPane tabbedPane) {
		this.tabbedPane = tabbedPane;
	}

	public abstract void close();

	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}
	
	

}
