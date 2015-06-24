package plugins.faubin.cytomine.module.project;

import icy.gui.dialog.MessageDialog;

import javax.swing.ImageIcon;

import plugins.faubin.cytomine.module.main.mvc.View;
import plugins.faubin.cytomine.module.main.mvc.panel.Menu;
import plugins.faubin.cytomine.module.main.mvc.panel.Workspace;
import plugins.faubin.cytomine.module.project.panel.ProjectMenu;
import plugins.faubin.cytomine.module.project.panel.ProjectWorkspace;
import plugins.faubin.cytomine.module.tileViewer.CytomineReader;
import be.cytomine.client.collections.ImageInstanceCollection;
import be.cytomine.client.models.ImageInstance;
import be.cytomine.client.models.Project;

public class ProjectView extends View {

	ProjectController controller;
	ProjectMenu menu;
	ProjectWorkspace workspace;
	
	public ProjectView(ProjectController controller, long ID) {
		this.controller = controller;
		workspace = new ProjectWorkspace(this, ID);
		menu = new ProjectMenu(this);
	}

	@Override
	public Workspace getWorkSpace() {
		return workspace;
	}

	@Override
	public Menu getMenu() {
		return menu;
	}
	
	public ImageInstanceCollection getImages(long ID) {
		return controller.getImages(ID);
	}
	
	/**
	 * @return project ID or -1 if no row was selected
	 */
	public long getSelected(){
		return workspace.getSelected();
	}

	public ImageIcon getImageIcon(ImageInstance instance, int size) {
		return controller.getImageIcon(instance,size);
	}

	public ImageInstanceCollection loadImageFromOffset(int i, int nbRowPerPage) {
		return controller.loadImageFromOffset(i, nbRowPerPage);
	}

	public int getNbImages(long ID) {
		return controller.getNbImages(ID);
	}

	public CytomineReader openReader(long ID) {
		return controller.openReader(ID);
	}

	public ImageIcon getThumbnail(long ID, int size){
		return controller.getThumbnail(ID, size);
	}

	public void sectionDetection() {
		controller.sectionDetection();
		
	}

	public void showError(String string) {
		MessageDialog.showDialog(string);
	}

	public void glomeruleDetection() {
		controller.glomeruleDetection();
	}

	public void sectglomDetection() {
		controller.sectglomDetection();
	}

	public void uploadThumbnail() {
		controller.uploadThumbnail();
	}

	public void cropSection() {
		controller.cropSection();
	}

}
