package plugins.faubin.cytomine.module.main.mvc.frame;

import icy.gui.frame.IcyFrame;
import icy.system.thread.ThreadUtil;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import plugins.faubin.cytomine.module.main.mvc.custom.CustomTabbedPaneUI;
import plugins.faubin.cytomine.oldgui.mvc.controller.panel.ProjectsPanelController;
import be.cytomine.client.Cytomine;

public class IcytomineFrame extends IcyFrame {

	public static JProgressBar progressBar;
	/**
	 * Create the frame.
	 */
	public IcytomineFrame(final Cytomine cytomine) {

		super("Icytomine", true, true, false, false);

		setBounds(new Rectangle(100, 100, 450, 350));

		setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().setLayout(new BorderLayout(0, 0));

		final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setUI(new CustomTabbedPaneUI());
		getContentPane().add(tabbedPane, BorderLayout.CENTER);

		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setValue(100);
		getContentPane().add(progressBar, BorderLayout.SOUTH);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenuItem mntmConfiguration = new JMenuItem("Configuration");
		mntmConfiguration.addActionListener(showConfiguration);
		menuBar.add(mntmConfiguration);

		ThreadUtil.invokeLater(new Runnable() {

			@Override
			public void run() {
				ProjectsPanelController projects = new ProjectsPanelController(cytomine, tabbedPane);
				tabbedPane.add(projects.getView().getName(), projects.getView());
				
			}
		});
		addToDesktopPane();
		
	}
	
	ActionListener showConfiguration = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			ConfigurationFrame.getConfigFrame().setVisible(true);
			
		}
		
	};

}
