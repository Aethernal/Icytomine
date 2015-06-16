package plugins.faubin.cytomine.oldgui.mvc.view.panel.configuration;

import javax.swing.JPanel;

import java.awt.GridLayout;

import javax.swing.JScrollPane;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JButton;

import java.awt.Component;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.Box;

@SuppressWarnings("serial")
public class ImagesViewConfigurationPanel extends ConfigurationPanel {

	private JSpinner nbRowPerPage;

	/**
	 * Create the panel.
	 */
	public ImagesViewConfigurationPanel() {
		setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane);
		
		JPanel panel = new JPanel();
		scrollPane.setViewportView(panel);
		panel.setLayout(new GridLayout(10, 1, 0, 0));
		
		JLabel lblNumberOfDisplayed = new JLabel("number of displayed row");
		panel.add(lblNumberOfDisplayed);
		
		nbRowPerPage = new JSpinner();
		panel.add(nbRowPerPage);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		panel.add(horizontalStrut);
		
		JButton btnNewButton = new JButton("save");
		btnNewButton.addActionListener(saveAction);
		panel.add(btnNewButton);

		initialize();
	}

	@Override
	public void initialize() {
		nbRowPerPage.setValue(configuration.nbRowPerPage);
		
	}

	@Override
	public void save() {
		configuration.nbRowPerPage = (Integer) nbRowPerPage.getValue();
		
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
