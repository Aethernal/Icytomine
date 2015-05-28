package plugins.faubin.cytomine.gui.mvc.view.frame;

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

import plugins.faubin.cytomine.Config;

public class LoginFrame extends IcyFrame {

	private JPanel contentPane;
	private JTextField publicKeyField;
	private JPasswordField privateKeyField;
	private JTextField txtHttpbetacytominebe;
	private JButton btnNewButton;

	public LoginFrame() {
		super("Icytomine: login", true, true, false, false);

		setTitle("Icytomine: login");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(new Rectangle(100, 100, 289, 300));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		contentPane.add(panel);
		panel.setLayout(new GridLayout(7, 1, 0, 0));

		JLabel lblNewLabel = new JLabel("Public Key");
		panel.add(lblNewLabel);

		publicKeyField = new JTextField();
		publicKeyField.setText(Config.defaultPublicKey);
		panel.add(publicKeyField);
		publicKeyField.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Private Key");
		panel.add(lblNewLabel_1);

		privateKeyField = new JPasswordField();
		privateKeyField.setText(Config.defaultPrivateKey);
		panel.add(privateKeyField);
		privateKeyField.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Host");
		panel.add(lblNewLabel_2);

		txtHttpbetacytominebe = new JTextField();
		txtHttpbetacytominebe.setText(Config.defaultServer);
		panel.add(txtHttpbetacytominebe);
		txtHttpbetacytominebe.setColumns(10);

		btnNewButton = new JButton("Connection");
		panel.add(btnNewButton);

		addToDesktopPane();
	}

	public String[] getInputs() {
		String[] inputs = new String[3];
		inputs[0] = publicKeyField.getText();
		inputs[1] = new String(privateKeyField.getPassword());
		inputs[2] = txtHttpbetacytominebe.getText();
		return inputs;
	}

	public JButton getBtnNewButton() {
		return btnNewButton;
	}

}
