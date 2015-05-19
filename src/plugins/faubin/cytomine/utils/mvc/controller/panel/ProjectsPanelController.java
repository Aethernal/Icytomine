package plugins.faubin.cytomine.utils.mvc.controller.panel;

import icy.system.thread.ThreadUtil;

import java.awt.Color;

import javax.swing.JTabbedPane;

import plugins.faubin.cytomine.utils.ConsoleUI;
import plugins.faubin.cytomine.utils.mvc.model.panel.ProjectsPanelModel;
import plugins.faubin.cytomine.utils.mvc.template.Controller;
import plugins.faubin.cytomine.utils.mvc.view.panel.ProjectsPanelView;
import be.cytomine.client.Cytomine;
import be.cytomine.client.collections.ProjectCollection;

public class ProjectsPanelController extends Controller {
	private ProjectsPanelModel model;
	private ProjectsPanelView view;

	public ProjectsPanelController(Cytomine cytomine, JTabbedPane tabbedPane) {
		super(tabbedPane);
		this.model = new ProjectsPanelModel(cytomine, this);

		ProjectCollection projects = model.getProjects();
		this.view = new ProjectsPanelView(projects, this);

		tabbedPane.add(view.getName(), view);

	}

	public void openProject(final long ID) {
		ThreadUtil.invokeLater(new Runnable() {
			@Override
			public void run() {
				ImagesPanelController imagesPanel = new ImagesPanelController(
						model.getCytomine(), ID, tabbedPane);
			}
		});

	}

	@Override
	public void close() {
		tabbedPane.remove(view);
	}

}
