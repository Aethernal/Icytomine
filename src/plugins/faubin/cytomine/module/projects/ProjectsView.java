package plugins.faubin.cytomine.module.projects;

import icy.gui.dialog.MessageDialog;
import be.cytomine.client.collections.ProjectCollection;
import plugins.faubin.cytomine.module.main.mvc.View;
import plugins.faubin.cytomine.module.main.mvc.panel.Menu;
import plugins.faubin.cytomine.module.main.mvc.panel.Workspace;
import plugins.faubin.cytomine.module.projects.panel.ProjectsMenu;
import plugins.faubin.cytomine.module.projects.panel.ProjectsWorkspace;

public class ProjectsView extends View {

	ProjectsController controller;
	ProjectsMenu menu;
	ProjectsWorkspace workspace;
	
	public ProjectsView(ProjectsController controller) {
		this.controller = controller;
		workspace = new ProjectsWorkspace(this);
		menu = new ProjectsMenu(this);
	}

	@Override
	public Workspace getWorkSpace() {
		return workspace;
	}

	@Override
	public Menu getMenu() {
		return menu;
	}
	
	public ProjectCollection getProjects() {
		return controller.getProjects();
	}
	
	/**
	 * @return project ID or -1 if no row was selected
	 */
	public long getSelected(){
		return workspace.getSelected();
	}

	public void openProject(long ID) {
		controller.openProject(ID);
		
	}

	public void sectionDetection() {
		controller.sectionDetection();
		
	}

	public void glomeruleDetection() {
		controller.glomeruleDetection();
		
	}

	public void showError(String string) {
		MessageDialog.showDialog(string);
	}

	public void sectglomDetection() {
		controller.sectglomDetection();
	}

}
