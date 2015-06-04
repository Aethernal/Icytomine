package plugins.faubin.cytomine.gui.mvc.model.panel;

import icy.roi.ROI2D;
import icy.sequence.Sequence;
import icy.sequence.SequenceUtil;
import icy.system.thread.ThreadUtil;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import javax.swing.ImageIcon;

import plugins.faubin.cytomine.Config;
import plugins.faubin.cytomine.IcytomineUtil;
import plugins.faubin.cytomine.gui.mvc.controller.panel.ImagesPanelController;
import plugins.faubin.cytomine.gui.mvc.model.utils.Configuration;
import plugins.faubin.cytomine.gui.mvc.model.utils.ThreadForRunnablePool;
import plugins.faubin.cytomine.gui.mvc.template.Model;
import plugins.faubin.cytomine.gui.mvc.view.frame.ProcessingFrame;
import plugins.faubin.cytomine.gui.roi.roi2dpolygon.CytomineImportedROI;
import plugins.faubin.cytomine.gui.tileViewer.CytomineReader;
import plugins.faubin.cytomine.gui.tileViewer.Tile;
import plugins.faubin.cytomine.utils.threshold.CustomThreshold;
import plugins.kernel.roi.roi2d.ROI2DPolygon;
import be.cytomine.client.Cytomine;
import be.cytomine.client.CytomineException;
import be.cytomine.client.collections.AnnotationCollection;
import be.cytomine.client.collections.ImageInstanceCollection;
import be.cytomine.client.models.Annotation;
import be.cytomine.client.models.ImageInstance;
import be.cytomine.client.models.User;

public class ImagesPanelModel extends Model {

	@SuppressWarnings("unused")
	private ImagesPanelController controller;
	private ProcessingFrame processFrame;

	static Configuration configuration = Configuration.getConfiguration();
	
	/**
	 * @param cytomine
	 * @param controller
	 */
	public ImagesPanelModel(Cytomine cytomine, ImagesPanelController controller) {
		super(cytomine);
		this.controller = controller;

		ThreadUtil.invokeLater(new Runnable() {

			@Override
			public void run() {
				processFrame = new ProcessingFrame();

			}
		});

	}

	/**
	 * @param id
	 * @return all the images present in the project who's ID is the id in
	 *         parameter
	 */
	public ImageInstanceCollection getImages(long id) {
		try {
			return cytomine.getImageInstances(id);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param images
	 *            take all the images in the list and generate section roi then
	 *            upload them to cytomine
	 * @throws CytomineException 
	 */
	public void generateAndUploadSectionROI(ImageInstanceCollection images) throws CytomineException {

		processFrame.newAction();

		processFrame.println("Generating sections and upload to cytomine for "
				+ images.size() + " images");

		for (int i = 0; i < images.size(); i++) {
			ImageInstance instance = images.get(i);

			Sequence sequence = IcytomineUtil.loadImage(instance, cytomine,
					configuration.iconPreviewMaxSize, processFrame);

			long idSoftware = Config.IDMap.get("SectionGenerationSoftware");
			User job = IcytomineUtil.generateNewUserJob(cytomine, idSoftware,controller.getProjectID());
			
			IcytomineUtil.generateSectionsROI(cytomine, job, controller.getProjectID(),instance, 
					sequence, processFrame);

			
		}

		processFrame.setGlobalProgress(100);

	}

	/**
	 * @param imageInstance
	 * @return an icon image of the imageInstance to visualize the image in the
	 *         menu.
	 */
	public ImageIcon getIcon(ImageInstance imageInstance, int size) {

		ImageIcon icon = new ImageIcon();

		long ID = imageInstance.getLong("baseImage");

		try {

			BufferedImage image = cytomine
					.downloadAbstractImageAsBufferedImage(ID, size);
			icon = new ImageIcon(image);
		} catch (Exception e) {
			e.printStackTrace();
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
	public ImageInstanceCollection getImagesFromIndexByNb(long idProj,
			int offset, int max) {
		ImageInstanceCollection collection = null;

		try {
			collection = cytomine.getImageInstancesByOffsetWithMax(idProj,
					offset, max);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return collection;
	}

	/**
	 * @param images
	 *            Delete all the annotation made by the user for all images in
	 *            the list
	 */
	public void deleteAllAnnotations(ImageInstanceCollection images) {

		processFrame.newAction();

		processFrame.println("deleting annotations of " + images.size()
				+ " images");

		for (int i = 0; i < images.size(); i++) {
			ImageInstance image = images.get(i);

			try {
				int nb = IcytomineUtil.deleteAllRoi(cytomine, image,
						processFrame);
				processFrame.println("deleted " + nb
						+ " annotations for image " + image.getStr("id"));
			} catch (Exception e) {
				e.printStackTrace();
			}

			processFrame.setGlobalProgress((int) ((double) (i+1) / images.size() * 100));

		}

		processFrame.println("delete finished");
		processFrame.setGlobalProgress(100);
	}
	
	public void generateGlomerule(ImageInstanceCollection instances) throws CytomineException {

		processFrame.newAction();
		
		for (int i = 0; i < instances.size(); i++) {
			
			long idSection = Config.IDMap.get("SectionGenerationSoftware");
			long idGlomerule = Config.IDMap.get("GlomeruleGenerationSoftware");
			User jobSection = IcytomineUtil.generateNewUserJob(cytomine, idSection, controller.getProjectID());
			User jobGlomerule = IcytomineUtil.generateNewUserJob(cytomine, idGlomerule, controller.getProjectID());
			
			ImageInstance instance = instances.get(i);
		
			IcytomineUtil.generateGlomerule(cytomine, jobSection, jobGlomerule, instance, 2, processFrame);
			
			System.gc();
			
			processFrame.setGlobalProgress((int) ((double) (i+1) / instances.size()*100));
		}
		
		processFrame.setGlobalProgress(100);
		
	}

	public void generateSectionAndGlomerules(ImageInstanceCollection instances) throws CytomineException {
		

		processFrame.newAction();

		processFrame.println("Generating sections and glomerules then uploading to Cytomine for "
				+ instances.size() + " images");

		for (int i = 0; i < instances.size(); i++) {
			
			long idSection = Config.IDMap.get("SectionGenerationSoftware");
			User jobSection = IcytomineUtil.generateNewUserJob(cytomine, idSection, controller.getProjectID());
			
			cytomine.changeStatus(jobSection.getLong("job"), Cytomine.JobStatus.RUNNING, 0);
			
			ImageInstance instance = instances.get(i);

			Sequence sequence = IcytomineUtil.loadImage(instance, cytomine, configuration.thumbnailMaxSize, processFrame);

			IcytomineUtil.generateSectionsROI(cytomine, jobSection, controller.getProjectID(),instance, 
					sequence, processFrame);
		
			
			cytomine.changeStatus(jobSection.getLong("job"), Cytomine.JobStatus.SUCCESS, 100);
			
			long idGlomerule = Config.IDMap.get("GlomeruleGenerationSoftware");
			User jobGlomerule = IcytomineUtil.generateNewUserJob(cytomine, idGlomerule, controller.getProjectID());
			
			IcytomineUtil.generateGlomerule(cytomine, jobSection, jobGlomerule, instance, 2, processFrame);
			
			
			System.gc();
			
			processFrame.println(i+" of " + instances.size() + " images");
			
			processFrame.setGlobalProgress((int) ((double) (i+1) / instances.size()*100));
		}
		
		processFrame.setGlobalProgress(100);
		
	}

}
