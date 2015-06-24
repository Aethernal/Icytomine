package plugins.faubin.cytomine.module.main.mvc.frame;

import icy.gui.frame.IcyFrame;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import plugins.faubin.cytomine.utils.Configuration;

import java.awt.Color;

public class LoginFrame extends IcyFrame {

	private JPanel contentPane;
	private JTextField publicKeyField;
	private JPasswordField privateKeyField;
	private JTextField hostField;
	private JButton btnNewButton;

	
	/**
	 * this frame is used to ask the user to enter his login informations
	 */
	public LoginFrame() {
		super("Icytomine: login", true, true, false, false);

		
		
		setTitle("Icytomine: login");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(new Rectangle(100, 100, 289, 300));
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		panel.setBackground(Color.DARK_GRAY);
		contentPane.add(panel);
		panel.setLayout(new GridLayout(7, 1, 0, 0));

		JLabel lblNewLabel = new JLabel("Public Key");
		lblNewLabel.setForeground(Color.WHITE);
		panel.add(lblNewLabel);

		publicKeyField = new JTextField();
		
		panel.add(publicKeyField);
		publicKeyField.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Private Key");
		lblNewLabel_1.setForeground(Color.WHITE);
		panel.add(lblNewLabel_1);

		privateKeyField = new JPasswordField();

		panel.add(privateKeyField);
		privateKeyField.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Host");
		lblNewLabel_2.setForeground(Color.WHITE);
		panel.add(lblNewLabel_2);

		hostField = new JTextField();

		panel.add(hostField);
		hostField.setColumns(10);

		btnNewButton = new JButton("Connection");
		panel.add(btnNewButton);

		addToDesktopPane();
		
		Configuration config = Configuration.getConfiguration();
		if(config.rememberMe){
			hostField.setText(config.host);
			publicKeyField.setText(config.publicKey);
			privateKeyField.setText(config.privateKey);
		}
		
	}

	/**
	 * this function return the writed login information as a tab
	 * @return String[]
	 */
	public String[] getInputs() {
		String[] inputs = new String[3];
		inputs[0] = publicKeyField.getText();
		inputs[1] = new String(privateKeyField.getPassword());
		inputs[2] = hostField.getText();
		return inputs;
	}

	public JButton getBtnNewButton() {
		return btnNewButton;
	}

}
