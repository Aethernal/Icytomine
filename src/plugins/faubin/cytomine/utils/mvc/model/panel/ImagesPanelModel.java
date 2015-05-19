package plugins.faubin.cytomine.utils.mvc.model.panel;

import icy.sequence.Sequence;
import icy.system.thread.ThreadUtil;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import plugins.faubin.cytomine.Config;
import plugins.faubin.cytomine.utils.cytomine.CytomineUtil;
import plugins.faubin.cytomine.utils.mvc.controller.panel.ImagesPanelController;
import plugins.faubin.cytomine.utils.mvc.template.Model;
import plugins.faubin.cytomine.utils.mvc.view.frame.ProcessingFrame;
import plugins.faubin.cytomine.utils.roi.roi2dpolygon.CytomineImportedROI;
import be.cytomine.client.Cytomine;
import be.cytomine.client.collections.AnnotationCollection;
import be.cytomine.client.collections.ImageInstanceCollection;
import be.cytomine.client.models.Annotation;
import be.cytomine.client.models.ImageInstance;

public class ImagesPanelModel extends Model {

	private ImagesPanelController controller;
	private ProcessingFrame processFrame;

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
	 */
	public void generateAndUploadSectionROI(ImageInstanceCollection images) {

		processFrame.newAction();

		processFrame.println("Generating sections and upload to cytomine for "
				+ images.size() + " images");

		for (int i = 0; i < images.size(); i++) {
			ImageInstance instance = images.get(i);

			Sequence sequence = CytomineUtil.loadImage(instance, cytomine,
					Config.thumbnailDefaultMaxSize, processFrame);

			List<CytomineImportedROI> rois = CytomineUtil.generateSectionsROI(
					sequence, processFrame);

			Dimension thumbnailSize = new Dimension(sequence.getWidth(),
					sequence.getHeight());

			long ID = instance.getLong("id");

			try {

				for (CytomineImportedROI roi : rois) {
					sequence.addROI(roi);
				}

				processFrame.setGlobalProgress((int) ((double) i
						/ images.size() * 100));
				CytomineUtil.uploadRoi(cytomine, instance, sequence,
						processFrame);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
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
				int nb = CytomineUtil.deleteAllRoi(cytomine, image,
						processFrame);
				processFrame.println("deleted " + nb
						+ " annotations for image " + image.getStr("id"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			processFrame
					.setGlobalProgress((int) ((double) i / images.size() * 100));

		}

		processFrame.println("delete finished");
		processFrame.setGlobalProgress(100);
	}

}
