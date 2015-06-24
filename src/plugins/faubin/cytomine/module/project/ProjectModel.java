package plugins.faubin.cytomine.module.project;

import icy.main.Icy;
import icy.sequence.Sequence;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import plugins.faubin.cytomine.module.main.mvc.Model;
import plugins.faubin.cytomine.module.tileViewer.CytomineReader;
import plugins.faubin.cytomine.module.tileViewer.toolbar.Toolbar;
import plugins.faubin.cytomine.utils.Config;
import plugins.faubin.cytomine.utils.Configuration;
import plugins.faubin.cytomine.utils.IcytomineUtil;
import plugins.faubin.cytomine.utils.software.SoftwareGlomeruleFinder;
import plugins.faubin.cytomine.utils.software.SoftwareSectionFinder;
import be.cytomine.client.Cytomine;
import be.cytomine.client.CytomineException;
import be.cytomine.client.collections.ImageInstanceCollection;
import be.cytomine.client.models.ImageInstance;
import be.cytomine.client.models.Project;
import be.cytomine.client.models.User;

public class ProjectModel extends Model {

	public ProjectModel(Cytomine cytomine) {
		super(cytomine);
	}

	public ImageInstanceCollection getImages(long ID){
		try {
			return cytomine.getImageInstances(ID);
		} catch (CytomineException e) {
			return new ImageInstanceCollection(0,0);
		}
	}
	
	/**
	 * @param imageInstance
	 * @return an icon image of the imageInstance to visualize the image in the
	 *         menu.
	 */
	public ImageIcon getImageIcon(ImageInstance imageInstance, int size) {

		ImageIcon icon = new ImageIcon();

		long ID = imageInstance.getLong("baseImage");

		try {
			BufferedImage image = cytomine.downloadAbstractImageAsBufferedImage(ID, size);
			icon = new ImageIcon(image);
		} catch (Exception e) {
		}

		return icon;
	}

	/**
	 * @param idProj
	 * @param start
	 * @param length
	 * @return an ImagesInstance collection that started from start with length
	 *         members (start at offset 3 and give 10 result for example)
	 */
	public ImageInstanceCollection loadImageFromOffset(long idProj, int offset, int max) {
		ImageInstanceCollection collection = null;

		try {
			collection = cytomine.getImageInstancesByOffsetWithMax(idProj, offset, max);
		} catch (CytomineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return collection;
	}

	/**
	 * this function is used to get the number of image in the project
	 * @param ID
	 * @return int
	 */
	public int getNbImages(long ID) {
		try {
			return cytomine.getImageInstances(ID).size();
		} catch (CytomineException e) {
			return 0;
		}
	}

	/**
	 * this function is used to start the dynamic viewer module
	 * @param ID
	 * @return
	 */
	public CytomineReader openReader(long ID) {
		try {
			ImageInstance instance = cytomine.getImageInstance(ID);
			CytomineReader reader = new CytomineReader(cytomine, instance, configuration.dynamicViewerDim, true);
			Toolbar toolbar = new Toolbar(reader);
			toolbar.setVisible(true);
			
			return reader;
		} catch (CytomineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * this function is used to get the thumbnail used for the previsualisation in the module
	 * @param ID
	 * @param size
	 * @return ImageIcon
	 */
	public ImageIcon getThumbnail(long ID, int size){
		ImageIcon icon = new ImageIcon();

		ImageInstance instance;
		try {
			instance = cytomine.getImageInstance(ID);
			long id = instance.getLong("baseImage");

			try {
				BufferedImage image = cytomine.downloadAbstractImageAsBufferedImage(id, size);
				icon = new ImageIcon(image);
			} catch (Exception e) {
			}
		} catch (CytomineException e1) {
		}
		
		

		return icon;
	}

	/**
	 * this function is used to start the section detection task
	 * @param ID
	 */
	public void sectionDetection(long ID) {
		processFrame.newAction();
		
		ImageInstance instance = getImage(ID);
		long projectID = instance.getLong("project");
		
		if(instance!=null){
			Sequence sequence = IcytomineUtil.loadImage(instance, cytomine, configuration.thumbnailMaxSize, processFrame);

			IcytomineUtil.createSectionSoftware(cytomine,projectID);
			
			long idSoftware = configuration.softwareID.get(cytomine.getHost()).get(new SoftwareSectionFinder().getName()).ID;
			User job;
			try {
				job = IcytomineUtil.generateNewUserJob(cytomine, idSoftware, projectID);
				IcytomineUtil.generateSectionsROI(cytomine, job, projectID, instance, sequence, processFrame);
			} catch (CytomineException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Icy.getMainInterface().addSequence(sequence);
			
		}
	}

	/**
	 * this function is used to get the image instance data.
	 * @param ID
	 * @return ImageInstance
	 */
	private ImageInstance getImage(long ID) {
		try {
			return cytomine.getImageInstance(ID);
		} catch (CytomineException e) {
			return null;
		}
	}

	/**
	 * this function is used to start the glomerule detection task
	 * @param ID
	 */
	public void glomeruleDetection(long ID) {
		
		processFrame.newAction();
		
		ImageInstance instance = getImage(ID);
		long projectID = instance.getLong("project");
		
		IcytomineUtil.createGlomeruleSoftware(cytomine,projectID);
		
		long idGlomerule = configuration.softwareID.get(cytomine.getHost()).get(new SoftwareGlomeruleFinder().getName()).ID;
		
		try {
			User user = cytomine.getCurrentUser();
			IcytomineUtil.sleep(1000);
			
			User jobGlomerule = IcytomineUtil.generateNewUserJob(cytomine, idGlomerule, projectID);
			
			IcytomineUtil.generateGlomerule(cytomine, user, jobGlomerule, instance, 2, processFrame);
		} catch (CytomineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * this function is used to start the section detection then the glomerule detection using the created section annotations.
	 * @param ID
	 */
	public void sectglomDetection(long ID) {
		processFrame.newAction();
		
		ImageInstance instance = getImage(ID);
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
		
		System.gc();
		
	}

	/**
	 * this function is used to upload annotations that were on a thumbnail.
	 * @param ID
	 */
	public void uploadThumbnail(long ID) {
		processFrame.newAction();
		
		ImageInstance instance = getImage(ID);
		long projectID = instance.getLong("project");
		Sequence sequence = Icy.getMainInterface().getActiveSequence();
		
		if(sequence!=null){
			IcytomineUtil.uploadROI(cytomine, instance, sequence, processFrame);
		}
		
		
	}

	/**
	 * this function is used to generate crop from sections annotations and save then in a folder as cytomine crop that could be loaded and uploaded later
	 * @param ID
	 */
	public void cropSection(long ID) {
		IcytomineUtil.cropSectionOfImage(cytomine, ID);
	}

}
