package plugins.faubin.cytomine.utils.mvc.view.frame;

import icy.gui.frame.IcyFrame;
import icy.system.thread.ThreadUtil;

import java.awt.BorderLayout;
import java.awt.Rectangle;

import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import plugins.faubin.cytomine.utils.mvc.controller.panel.ProjectsPanelController;
import plugins.faubin.cytomine.utils.mvc.view.custom.CustomTabbedPaneUI;
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
		setLayout(new BorderLayout(0, 0));

		final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setUI(new CustomTabbedPaneUI());
		add(tabbedPane, BorderLayout.CENTER);

		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setValue(100);
		add(progressBar, BorderLayout.SOUTH);

		ThreadUtil.invokeLater(new Runnable() {

			@Override
			public void run() {
				ProjectsPanelController projects = new ProjectsPanelController(cytomine, tabbedPane);
				tabbedPane.add(projects.getView().getName(), projects.getView());
				
			}
		});
		addToDesktopPane();
		
	}

}
