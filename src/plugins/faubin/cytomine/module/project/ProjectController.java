package plugins.faubin.cytomine.module.project;

import icy.system.thread.ThreadUtil;

import javax.swing.ImageIcon;

import plugins.faubin.cytomine.module.main.mvc.Controller;
import plugins.faubin.cytomine.module.main.mvc.Model;
import plugins.faubin.cytomine.module.main.mvc.View;
import plugins.faubin.cytomine.module.tileViewer.CytomineReader;
import plugins.faubin.cytomine.utils.IcytomineUtil;
import be.cytomine.client.Cytomine;
import be.cytomine.client.CytomineException;
import be.cytomine.client.collections.ImageInstanceCollection;
import be.cytomine.client.models.ImageInstance;

public class ProjectController extends Controller {

	private ProjectModel model;
	private ProjectView view;
	
	long projectID;

	public ProjectController(Cytomine cytomine, long ID) {
		super("Project : "+ID,cytomine);

		projectID = ID;
		
		model = new ProjectModel(cytomine);
		view = new ProjectView(this, ID);
	}

	@Override
	public View getView() {
		return view;
	}

	@Override
	public Model getModel() {
		return model;
	}

	public ImageInstanceCollection getImages(long ID) {
			return model.getImages(ID);
	}

	public ImageIcon getImageIcon(ImageInstance instance, int size) {
		return model.getImageIcon(instance,size);
	}

	public ImageInstanceCollection loadImageFromOffset(int i, int nbRowPerPage) {
		return model.loadImageFromOffset(projectID, i, nbRowPerPage);
	}

	public int getNbImages(long ID) {
		return model.getNbImages(ID);
	}

	public CytomineReader openReader(long ID) {
		return model.openReader(ID);
	}

	public ImageIcon getThumbnail(long ID, int size){
		return model.getThumbnail(ID, size);
	}

	public void sectionDetection() {
		final long ID = view.getSelected();
		
		if(ID != -1){
			ThreadUtil.bgRun(new Runnable(){

				@Override
				public void run() {
					model.sectionDetection(ID);
				}
				
			});
		}else{
			view.showError("You need to select an image !");
		}
		
	}
	
	public void glomeruleDetection() {
		final long ID = view.getSelected();
		
		if(ID != -1){
			ThreadUtil.bgRun(new Runnable(){

				@Override
				public void run() {
					model.glomeruleDetection(ID);
				}
				
			});
		}else{
			view.showError("You need to select an image !");
		}
		
	}

	public void sectglomDetection() {
		final long ID = view.getSelected();
		
		if(ID != -1){
			ThreadUtil.bgRun(new Runnable(){

				@Override
				public void run() {
					model.sectglomDetection(ID);
				}
				
			});
		}else{
			view.showError("You need to select an image !");
		}
	}

	public void uploadThumbnail() {
		final long ID = view.getSelected();
		
		if(ID != -1){
			ThreadUtil.bgRun(new Runnable(){

				@Override
				public void run() {
					model.uploadThumbnail(ID);
				}
				
			});
		}else{
			view.showError("You need to select an image !");
		}
	}

	public void cropSection() {
		final long ID = view.getSelected();
		
		if(ID != -1){
			ThreadUtil.bgRun(new Runnable(){

				@Override
				public void run() {
					model.cropSection(ID);
				}
				
			});
		}else{
			view.showError("You need to select an image !");
		}
	}
	
}
