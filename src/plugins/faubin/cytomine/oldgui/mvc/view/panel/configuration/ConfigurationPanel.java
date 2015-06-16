package plugins.faubin.cytomine.oldgui.mvc.view.panel.configuration;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import plugins.faubin.cytomine.oldgui.mvc.model.utils.Configuration;

@SuppressWarnings("serial")
public abstract class ConfigurationPanel extends JPanel{

	protected Configuration configuration = Configuration.getConfiguration();
	
	protected JButton save;
	
	/**
	 * don't forget to start the initialization at the end of the constructor
	 */
	public abstract void initialize();
	
	protected ActionListener saveAction = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			save();
		}
	};
	
	public abstract void save();
	
}
