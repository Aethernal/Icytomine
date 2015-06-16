package plugins.faubin.cytomine.oldgui.mvc.view.panel.configuration;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;


@SuppressWarnings("serial")
public class GlomGenConfigurationPanel extends ConfigurationPanel{

	private JSpinner cValue;
	private JSpinner minSize;
	private JSpinner maxSize;
	private JSpinner vMinLong;
	private JSpinner valRatioAxis;

	/**
	 * Create the panel.
	 */
	public GlomGenConfigurationPanel() {
		setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane);
		
		JPanel panel = new JPanel();
		scrollPane.setViewportView(panel);
		panel.setLayout(new GridLayout(12, 1, 0, 0));
		
		JLabel lblNewLabel = new JLabel("cValue");
		panel.add(lblNewLabel);
		
		cValue = new JSpinner();
		panel.add(cValue);
		
		JLabel lblNewLabel_1 = new JLabel("minSize");
		panel.add(lblNewLabel_1);
		
		minSize = new JSpinner();
		panel.add(minSize);
		
		JLabel lblNewLabel_2 = new JLabel("maxSize");
		panel.add(lblNewLabel_2);
		
		maxSize = new JSpinner();
		panel.add(maxSize);
		
		JLabel lblNewLabel_3 = new JLabel("vMinLong");
		panel.add(lblNewLabel_3);
		
		vMinLong = new JSpinner();
		panel.add(vMinLong);
		
		JLabel lblNewLabel_4 = new JLabel("valRatioAxis");
		panel.add(lblNewLabel_4);
		
		valRatioAxis = new JSpinner();
		panel.add(valRatioAxis);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		panel.add(horizontalStrut);
		
		JButton btnSave = new JButton("save");
		btnSave.addActionListener(saveAction);
		panel.add(btnSave);

		
		initialize();
	}

	@Override
	public void initialize() {
		cValue.setValue(configuration.cValue);
		minSize.setValue(configuration.minSize);
		maxSize.setValue(configuration.maxSize);
		vMinLong.setValue(configuration.vMinLong);
		valRatioAxis.setValue(configuration.valRatioAxis);
		
	}

	@Override
	public void save() {
		configuration.cValue = (Integer) cValue.getValue();
		configuration.minSize = (Integer) cValue.getValue();
		configuration.maxSize = (Integer) cValue.getValue();
		configuration.vMinLong = (Integer) cValue.getValue();
		configuration.valRatioAxis = (Integer) cValue.getValue();
	}
}
