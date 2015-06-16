package plugins.faubin.cytomine.module.project;

import icy.main.Icy;
import icy.sequence.Sequence;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import plugins.faubin.cytomine.module.main.mvc.Model;
import plugins.faubin.cytomine.module.tileViewer.CytomineReader;
import plugins.faubin.cytomine.oldgui.mvc.model.utils.Configuration;
import plugins.faubin.cytomine.utils.Config;
import plugins.faubin.cytomine.utils.IcytomineUtil;
import be.cytomine.client.Cytomine;
import be.cytomine.client.CytomineException;
import be.cytomine.client.collections.ImageInstanceCollection;
import be.cytomine.client.models.ImageInstance;
import be.cytomine.client.models.Project;
import be.cytomine.client.models.User;

public class ProjectModel extends Model {

	public ProjectModel(Cytomine cytomine) {
		super(cytomine);
		// TODO Auto-generated constructor stub
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

	public int getNbImages(long ID) {
		try {
			return cytomine.getImageInstances(ID).size();
		} catch (CytomineException e) {
			return 0;
		}
	}

	public CytomineReader openReader(long ID) {
		try {
			ImageInstance instance = cytomine.getImageInstance(ID);
			CytomineReader reader = new CytomineReader(cytomine, instance, configuration.dynamicViewerDim, true);
			
			return reader;
		} catch (CytomineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

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

	public void sectionDetection(long ID) {
		processFrame.newAction();
		
		ImageInstance instance = getImage(ID);
		long projectID = instance.getLong("project");
		
		if(instance!=null){
			Sequence sequence = IcytomineUtil.loadImage(instance, cytomine, configuration.thumbnailMaxSize, processFrame);

			long idSoftware = Config.IDMap.get("SectionGenerationSoftware");
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

	private ImageInstance getImage(long ID) {
		try {
			return cytomine.getImageInstance(ID);
		} catch (CytomineException e) {
			return null;
		}
	}

	public void glomeruleDetection(long ID) {
		
		processFrame.newAction();
		
		ImageInstance instance = getImage(ID);
		long projectID = instance.getLong("project");
		
		long idGlomerule = Config.IDMap.get("GlomeruleGenerationSoftware");
		
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

}
