package plugins.faubin.cytomine.oldgui.mvc.model.panel;

import plugins.faubin.cytomine.oldgui.mvc.controller.panel.ProjectsPanelController;
import plugins.faubin.cytomine.oldgui.mvc.template.Model;
import be.cytomine.client.Cytomine;
import be.cytomine.client.collections.ProjectCollection;

public class ProjectsPanelModel extends Model {

	@SuppressWarnings("unused")
	private ProjectsPanelController controller;

	/**
	 * @param cytomine
	 * @param controller
	 */
	public ProjectsPanelModel(Cytomine cytomine,
			ProjectsPanelController controller) {
		super(cytomine);
		this.controller = controller;
	}

	/**
	 * @return All the project the connected user has access
	 */
	public ProjectCollection getProjects() {
		try {
			return cytomine.getProjects();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
