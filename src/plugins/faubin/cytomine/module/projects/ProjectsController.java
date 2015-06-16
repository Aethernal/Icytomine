package plugins.faubin.cytomine.module.projects;

import icy.system.thread.ThreadUtil;
import be.cytomine.client.Cytomine;
import be.cytomine.client.CytomineException;
import be.cytomine.client.collections.ProjectCollection;
import plugins.faubin.cytomine.module.main.mvc.Controller;
import plugins.faubin.cytomine.module.main.mvc.Model;
import plugins.faubin.cytomine.module.main.mvc.View;

public class ProjectsController extends Controller {

	private ProjectsModel model;
	private ProjectsView view;

	public ProjectsController(Cytomine cytomine) {
		super("Projects",cytomine);

		model = new ProjectsModel(cytomine);
		view = new ProjectsView(this);

	}

	@Override
	public View getView() {
		return view;
	}

	@Override
	public Model getModel() {
		return model;
	}

	public ProjectCollection getProjects() {
		try {
			return model.getProjects();
		} catch (CytomineException e) {
			return new ProjectCollection(0, 0);
		}
	}

	public void openProject(long ID) {
		model.openProject(ID);
		
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
			view.showError("You need to select a project !");
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
			view.showError("You need to select a project !");
		}
	}
	
}
