package plugins.faubin.cytomine.module.main.mvc.frame;

import icy.gui.frame.IcyFrame;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.border.EmptyBorder;

public class InputID extends IcyFrame {

	private JPanel contentPane;
	private JSpinner spinner;
	
	private boolean finished = false;
	private Runnable runnable;
	
	/**
	 * Create the frame.
	 */
	public InputID() {
		super("ID selection");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(new Rectangle(100, 100, 300, 150));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(4, 1, 0, 0));
		
		JLabel lblEnterTheId = new JLabel("Enter the ID");
		contentPane.add(lblEnterTheId);
		
		spinner = new JSpinner();
		contentPane.add(spinner);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		contentPane.add(horizontalStrut);
		
		JButton btnValidate = new JButton("Validate");
		contentPane.add(btnValidate);
		btnValidate.addActionListener(validate);
		
		addToDesktopPane();
		
	}
	
	public boolean isFinished(){
		return finished;
	}
	
	public long getID(){
		return (long) ((Integer) spinner.getValue()).intValue();
	}
	
	ActionListener validate = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			finished = true;
			setVisible(false);
			
			if(runnable != null){
				runnable.run();
			}
			
		}
		
	};

	public void setRunnable(Runnable runnable) {
		this.runnable = runnable;
	}

}
