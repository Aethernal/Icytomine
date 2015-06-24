package plugins.faubin.cytomine.oldgui.mvc.view.panel.configuration;

import java.awt.Component;
import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSpinner;

import plugins.faubin.cytomine.utils.Configuration;

@SuppressWarnings("serial")
public class ImagesDownloadConfigurationPanel extends ConfigurationPanel {
	private JLabel icon;
	private JSpinner iconSize;
	private JLabel thumbnail;
	private JSpinner thumbnailSize;
	private JLabel lblPreviewMaxSize;
	private JSpinner previewSize;

	/**
	 * Create the panel.
	 */
	public ImagesDownloadConfigurationPanel() {
		setLayout(new GridLayout(10, 1, 0, 0));
		
		icon = new JLabel("Thumbnail max size");
		iconSize = new JSpinner();
		thumbnail = new JLabel("Icon max size");
		add(thumbnail);
		add(iconSize);
		
		lblPreviewMaxSize = new JLabel("Preview max size");
		add(lblPreviewMaxSize);
		
		previewSize = new JSpinner();
		add(previewSize);
		add(icon);
		
		thumbnailSize = new JSpinner();
		add(thumbnailSize);
		
		JButton btnSave = new JButton("save");
		btnSave.addActionListener(saveAction);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		add(horizontalStrut);
		add(btnSave);

		initialize();
	}

	@Override
	public void initialize() {
		Configuration values = Configuration.getConfiguration();
		
		iconSize.setValue(values.iconPreviewMaxSize);
		previewSize.setValue(values.previewMaxSize);
		thumbnailSize.setValue(values.thumbnailMaxSize);
		
	}
	
	public void save(){
		configuration.iconPreviewMaxSize = (Integer) iconSize.getValue();
		configuration.previewMaxSize = (Integer) previewSize.getValue();
		configuration.thumbnailMaxSize = (Integer) thumbnailSize.getValue();
		
		try {
			configuration.save();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
