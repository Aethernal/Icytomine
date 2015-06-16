package plugins.faubin.cytomine.oldgui.mvc.view.panel.configuration;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class ConnectionConfigurationPanel extends ConfigurationPanel {
	private JTextField publicKey;
	private JPasswordField privateKey;
	private JTextField host;
	private JButton btnSave;
	private JCheckBox chckbxRememberMe;
	
	/**
	 * Create the panel.
	 */
	public ConnectionConfigurationPanel() {
		setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane);
		
		JSplitPane splitPane = new JSplitPane();
		scrollPane.setViewportView(splitPane);
		splitPane.setEnabled(false);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		
		chckbxRememberMe = new JCheckBox("Remember me");
		splitPane.setLeftComponent(chckbxRememberMe);
		
		JPanel panel = new JPanel();
		splitPane.setRightComponent(panel);
		panel.setLayout(new GridLayout(8, 1, 0, 0));
		
		JLabel lblPublicKey = new JLabel("Public key");
		panel.add(lblPublicKey);
		
		publicKey = new JTextField();
		panel.add(publicKey);
		publicKey.setColumns(10);
		
		JLabel lblPrivateKey = new JLabel("Private key");
		panel.add(lblPrivateKey);
		
		privateKey = new JPasswordField();
		panel.add(privateKey);
		
		JLabel lblHost = new JLabel("host");
		panel.add(lblHost);
		
		host = new JTextField();
		panel.add(host);
		host.setColumns(10);
		
		Component horizontalStrut_2 = Box.createHorizontalStrut(20);
		panel.add(horizontalStrut_2);
		
		btnSave = new JButton("save");
		btnSave.addActionListener(saveAction);
		panel.add(btnSave);
		chckbxRememberMe.addActionListener(rememberMeAction);

		initialize();
	}
	
	public void initialize(){
		
		publicKey.setText(configuration.publicKey);
		privateKey.setText(configuration.privateKey);
		host.setText(configuration.host);
		
		chckbxRememberMe.setSelected(configuration.rememberMe);
		
		toggleField(chckbxRememberMe.isSelected());
		
	}
	
	public void toggleField(boolean value){
		publicKey.setEnabled(value);
		privateKey.setEnabled(value);
		host.setEnabled(value);
	}
	
	ActionListener rememberMeAction = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			toggleField(chckbxRememberMe.isSelected());
			
		}
		
	};
	
	public void save(){
		
		configuration.rememberMe = chckbxRememberMe.isSelected();
		
		configuration.publicKey = publicKey.getText();
		configuration.privateKey = new String(privateKey.getPassword());
		configuration.host = host.getText();
		
		try {
			configuration.save();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
	}
	
}
