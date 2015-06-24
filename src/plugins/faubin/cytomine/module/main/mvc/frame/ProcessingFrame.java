package plugins.faubin.cytomine.module.main.mvc.frame;

import icy.gui.frame.IcyFrame;
import icy.system.thread.ThreadUtil;

import java.awt.Color;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import plugins.faubin.cytomine.utils.ConsoleUI;

public class ProcessingFrame extends IcyFrame {

	private JPanel contentPane;

	private JProgressBar globalProgressBar; // progress of all the actions
	private JProgressBar actionProgressBar; // progress of one action

	// console and color nexts print will be
	private ConsoleUI console;
	private Color textColor;

	// reset or not the console between each use 
	private JCheckBox checkBox;


	/**
	 * this frame is used to show the progression of a task, it use a console and 2 progress bar.
	 */
	public ProcessingFrame() {
		super("Processing", true, true, false, false);

		setSize(455, 350);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		globalProgressBar = new JProgressBar();
		globalProgressBar.setStringPainted(true);
		globalProgressBar.setBounds(5, 274, 440, 20);
		actionProgressBar = new JProgressBar();
		actionProgressBar.setStringPainted(true);
		actionProgressBar.setBounds(6, 236, 439, 20);
		console = new ConsoleUI();

		textColor = Color.black;
		contentPane.setLayout(null);

		contentPane.add(actionProgressBar);
		contentPane.add(globalProgressBar);

		JScrollPane scroll = new JScrollPane(console);
		scroll.setBounds(5, 6, 440, 206);
		contentPane.add(scroll);

		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(5, 188, 61, 16);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Global Progress");
		lblNewLabel_1.setBounds(5, 256, 439, 16);
		contentPane.add(lblNewLabel_1);

		JLabel lblActionProgress = new JLabel("Action Progress");
		lblActionProgress.setBounds(5, 216, 435, 16);
		contentPane.add(lblActionProgress);

		checkBox = new JCheckBox("keep previous log");
		checkBox.setBounds(5, 300, 435, 16);
		contentPane.add(checkBox);

		addToDesktopPane();
	}

	
	/**
	 * reset the frame and the console if the option keep is not selected
	 */
	public void newAction() {
		setVisible(true);
		setActionProgress(0);
		setGlobalProgress(0);
		setColor(Color.BLACK);

		if (!checkBox.isSelected()) {
			console.clear();
		} else {
			console.append("");
		}
	}

	/**
	 * this function allow to set the progression of the action
	 * @param value
	 */
	public void setActionProgress(final int value) {
		ThreadUtil.invokeLater(new Runnable() {

			@Override
			public void run() {
				actionProgressBar.setValue(value);
			}
		});

	}

	/**
	 * this function allow to set the progression of the task
	 */
	public void setGlobalProgress(final int value) {
		ThreadUtil.invokeLater(new Runnable() {

			@Override
			public void run() {
				globalProgressBar.setValue(value);
			}
		});

	}

	/**
	 * this function is used to print a message to the console with a line end
	 * @param msg
	 */
	public void println(String msg) {
		print(msg + "\n");
	}

	/**
	 * this function is used to print a message to the console
	 * @param msg
	 */
	public void print(final String msg) {
		ThreadUtil.invokeLater(new Runnable() {
			@Override
			public void run() {
				console.append(msg, textColor);
			}
		});
	}

	/**
	 * this function is used to set the color of the next console messages.
	 * @param textColor
	 */
	public void setColor(Color textColor) {
		this.textColor = textColor;
	}
}
