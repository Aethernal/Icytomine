package plugins.faubin.cytomine;

import icy.gui.dialog.MessageDialog;
import icy.main.Icy;
import icy.plugin.abstract_.PluginActionable;
import icy.system.thread.ThreadUtil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import plugins.faubin.cytomine.headless.Console;
import plugins.faubin.cytomine.utils.mvc.view.frames.LoginFrame;
import plugins.faubin.cytomine.utils.mvc.view.frames.ProjectsFrame;
import be.cytomine.client.Cytomine;

public class Icytomine extends PluginActionable {
	public Cytomine cytomine;
	private ProjectsFrame frame;
	private LoginFrame loginFrame;

	// Icy plugin start
	@Override
	public void run() {

		Config.initialize();

		if (Icy.getMainInterface().isHeadLess()) {
			// start in headless
			Console console = new Console();
		} else {
			// start with GUI
			initialize();
		}
	}

	protected void initialize() {

		// initialise login frame
		loginFrame = new LoginFrame();
		loginFrame.getBtnNewButton().addActionListener(connectionListener);

		login();

	}

	/**
	 * try to log to cytomine using username and password
	 * 
	 * @param logins
	 */
	private void tryLogin(final String[] logins) {
		ThreadUtil.bgRun(new Runnable() {

			@Override
			public void run() {
				String username = logins[0];
				String passwd = logins[1];
				String url = logins[2];

				try {
					// connect to cytomine
					System.out.println(username+","+passwd);
					cytomine = new Cytomine(url, username, passwd, "./", false);
					// test connection
					cytomine.getProjects();

					ThreadUtil.invokeLater(new Runnable() {

						@Override
						public void run() {
							logged();
						}
					});

				} catch (Exception e) {
					MessageDialog.showDialog("Connection failed !");
					login();
				}

			}
		});

	}

	private void logged() {
		// show the plugin panel
		loginFrame.setVisible(false);

		frame = new ProjectsFrame(cytomine);
		frame.setVisible(true);
	}

	public void login() {
		// show a login form to connect to Cytomine
		loginFrame.setVisible(true);
	}

	// actions
	ActionListener connectionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			String[] logins = loginFrame.getInputs();

			// try to log
			tryLogin(logins);

		}
	};

}
