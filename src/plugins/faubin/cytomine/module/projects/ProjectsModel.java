package plugins.faubin.cytomine.module.projects;

import icy.sequence.Sequence;
import plugins.faubin.cytomine.module.main.IcytomineFrame;
import plugins.faubin.cytomine.module.main.mvc.Model;
import plugins.faubin.cytomine.module.project.ProjectController;
import plugins.faubin.cytomine.utils.Config;
import plugins.faubin.cytomine.utils.IcytomineUtil;
import plugins.faubin.cytomine.utils.software.SoftwareGlomeruleFinder;
import plugins.faubin.cytomine.utils.software.SoftwareSectionFinder;
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
	
	/**
	 * this function return the list of all the imageinstance of the project
	 * @param ID
	 * @return ImageInstanceCollection
	 */
	private ImageInstanceCollection getImages(long ID) {
		try {
			return cytomine.getImageInstances(ID);
		} catch (CytomineException e) {
			return new ImageInstanceCollection(0,0);
		}
	}
	
	/**
	 * this function allow to start the section detection task for the whole project
	 * @param ID
	 */
	public void sectionDetection(long ID) {
		processFrame.newAction();
		
		ImageInstanceCollection collection = getImages(ID);
		
		for (int i = 0; i < collection.size(); i++) {
			ImageInstance instance = collection.get(i);
			
			Sequence sequence = IcytomineUtil.loadImage(instance, cytomine, configuration.thumbnailMaxSize, processFrame);

			IcytomineUtil.createGlomeruleSoftware(cytomine, ID);
			long idSoftware = configuration.softwareID.get(cytomine.getHost()).get(new SoftwareSectionFinder().getName()).ID;
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
	
	/**
	 * this function allow to start the glomerule detection for the whole project
	 * @param ID
	 */
	public void glomeruleDetection(long ID) {
		processFrame.newAction();
		
		ImageInstanceCollection collection = getImages(ID);
		
		for (int i = 0; i < collection.size(); i++) {
			ImageInstance instance = collection.get(i);
			
			long idGlomerule = configuration.softwareID.get(cytomine.getHost()).get(new SoftwareGlomeruleFinder().getName()).ID;
			
			try {
				User user = cytomine.getCurrentUser();
				
				IcytomineUtil.createGlomeruleSoftware(cytomine,ID);
				User jobGlomerule = IcytomineUtil.generateNewUserJob(cytomine, idGlomerule, ID);
				
				IcytomineUtil.generateGlomerule(cytomine, user, jobGlomerule, instance, 2, processFrame);
			} catch (CytomineException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
	}

	/**
	 * this function allow to start the section and glomerule detection task for the whole project
	 * @param ID
	 */
	public void sectglomDetection(long ID) {
		processFrame.newAction();
		
		ImageInstanceCollection collection = getImages(ID);
		
		for (int i = 0; i < collection.size(); i++) {
			ImageInstance instance = collection.get(i);
			long projectID = instance.getLong("project");
			
			
			long idSection = configuration.softwareID.get(cytomine.getHost()).get(new SoftwareSectionFinder().getName()).ID;
			User jobSection;
			try {
				jobSection = IcytomineUtil.generateNewUserJob(cytomine, idSection, projectID);
		
			
				cytomine.changeStatus(jobSection.getLong("job"), Cytomine.JobStatus.RUNNING, 0);
				
		
				Sequence sequence = IcytomineUtil.loadImage(instance, cytomine, configuration.thumbnailMaxSize, processFrame);
		
				
				IcytomineUtil.generateSectionsROI(cytomine, jobSection, projectID, instance, sequence, processFrame);
				
				
				cytomine.changeStatus(jobSection.getLong("job"), Cytomine.JobStatus.SUCCESS, 100);
				
				IcytomineUtil.sleep(1000);
				
				long idGlomerule = configuration.softwareID.get(cytomine.getHost()).get(new SoftwareGlomeruleFinder().getName()).ID;
				User jobGlomerule = IcytomineUtil.generateNewUserJob(cytomine, idGlomerule, projectID);
				
				cytomine.changeStatus(jobGlomerule.getLong("job"), Cytomine.JobStatus.RUNNING, 0);
				
				IcytomineUtil.generateGlomerule(cytomine, jobSection, jobGlomerule, instance, 2, processFrame);
				
				cytomine.changeStatus(jobGlomerule.getLong("job"), Cytomine.JobStatus.SUCCESS, 100);
				
			} catch (CytomineException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
}
