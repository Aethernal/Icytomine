package plugins.faubin.cytomine.module.projects;

import icy.sequence.Sequence;
import plugins.faubin.cytomine.module.main.IcytomineFrame;
import plugins.faubin.cytomine.module.main.mvc.Model;
import plugins.faubin.cytomine.module.project.ProjectController;
import plugins.faubin.cytomine.utils.Config;
import plugins.faubin.cytomine.utils.IcytomineUtil;
import be.cytomine.client.Cytomine;
import be.cytomine.client.CytomineException;
import be.cytomine.client.collections.ImageInstanceCollection;
import be.cytomine.client.collections.ProjectCollection;
import be.cytomine.client.models.ImageInstance;
import be.cytomine.client.models.User;

public class ProjectsModel extends Model {

	public ProjectsModel(Cytomine cytomine) {
		super(cytomine);
		// TODO Auto-generated constructor stub
	}

	public ProjectCollection getProjects() throws CytomineException{
		return cytomine.getProjects();
	}

	public void openProject(long ID) {
		IcytomineFrame frame = IcytomineFrame.getIcytomineFrame();
		frame.addModule(new ProjectController(cytomine, ID));
		
	}
	
	private ImageInstanceCollection getImages(long ID) {
		try {
			return cytomine.getImageInstances(ID);
		} catch (CytomineException e) {
			return new ImageInstanceCollection(0,0);
		}
	}
	
	public void sectionDetection(long ID) {
		processFrame.newAction();
		
		ImageInstanceCollection collection = getImages(ID);
		
		for (int i = 0; i < collection.size(); i++) {
			ImageInstance instance = collection.get(i);
			
			Sequence sequence = IcytomineUtil.loadImage(instance, cytomine, configuration.thumbnailMaxSize, processFrame);

			long idSoftware = Config.IDMap.get("SectionGenerationSoftware");
			User job;
			try {
				job = IcytomineUtil.generateNewUserJob(cytomine, idSoftware, ID);
				IcytomineUtil.generateSectionsROI(cytomine, job, ID, instance, sequence, processFrame);
			} catch (CytomineException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void glomeruleDetection(long ID) {
		processFrame.newAction();
		
		ImageInstanceCollection collection = getImages(ID);
		
		for (int i = 0; i < collection.size(); i++) {
			ImageInstance instance = collection.get(i);
			
			long idGlomerule = Config.IDMap.get("GlomeruleGenerationSoftware");
			
			try {
				User user = cytomine.getCurrentUser();
				
				User jobGlomerule = IcytomineUtil.generateNewUserJob(cytomine, idGlomerule, ID);
				
				IcytomineUtil.generateGlomerule(cytomine, user, jobGlomerule, instance, 2, processFrame);
			} catch (CytomineException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
	}
	
}
