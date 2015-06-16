package plugins.faubin.cytomine.oldgui.mvc.controller.panel;

import icy.system.thread.ThreadUtil;

import javax.swing.JTabbedPane;

import plugins.faubin.cytomine.oldgui.mvc.model.panel.ProjectsPanelModel;
import plugins.faubin.cytomine.oldgui.mvc.template.Controller;
import plugins.faubin.cytomine.oldgui.mvc.view.panel.ProjectsPanelView;
import be.cytomine.client.Cytomine;
import be.cytomine.client.collections.ProjectCollection;

public class ProjectsPanelController extends Controller {
	ProjectsPanelModel model;
	ProjectsPanelView view;

	public ProjectsPanelController(Cytomine cytomine, JTabbedPane tabbedPane) {
		super(tabbedPane);
		this.model = new ProjectsPanelModel(cytomine, this);

		ProjectCollection projects = model.getProjects();
		this.view = new ProjectsPanelView(projects, this);

	}

	public void openProject(final long ID) {
		ThreadUtil.invokeLater(new Runnable() {
			@Override
			public void run() {
				ImagesPanelController imagesPanel = new ImagesPanelController(model.getCytomine(), ID, tabbedPane);
				
				tabbedPane.add(imagesPanel.getView().getName(), imagesPanel.getView());
				tabbedPane.setSelectedComponent(imagesPanel.getView());
			}
		});

	}

	@Override
	public void close() {
		tabbedPane.remove(view);
	}

	public ProjectsPanelModel getModel() {
		return model;
	}
	
	public ProjectsPanelView getView() {
		return view;
	}
	
}
