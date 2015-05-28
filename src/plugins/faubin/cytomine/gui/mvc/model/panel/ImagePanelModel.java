package plugins.faubin.cytomine.gui.mvc.model.panel;

import icy.gui.dialog.MessageDialog;
import icy.main.Icy;
import icy.roi.ROI;
import icy.roi.ROI2D;
import icy.sequence.Sequence;
import icy.sequence.SequenceUtil;
import icy.system.thread.ThreadUtil;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;

import ome.xml.model.Image;
import plugins.faubin.cytomine.AnnotationTerm;
import plugins.faubin.cytomine.Config;
import plugins.faubin.cytomine.IcytomineUtil;
import plugins.faubin.cytomine.gui.mvc.controller.panel.ImagePanelController;
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
import be.cytomine.client.models.Annotation;
import be.cytomine.client.models.ImageInstance;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class ImagePanelModel extends Model {

	private ImagePanelController controller;
	private ProcessingFrame processFrame;

	/**
	 * @param cytomine
	 * @param controller
	 */
	public ImagePanelModel(Cytomine cytomine, ImagePanelController controller) {
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
	 * @return an ImageInstance corresponding to the given id
	 */
	public ImageInstance getImage(long id) {
		try {
			return cytomine.getImageInstance(id);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param instance
	 *            open an ImageInstance in Icy allowing it to be viewed
	 */
	public void openInIcy(ImageInstance instance) {

		processFrame.newAction();

		processFrame.setGlobalProgress(50);
		Sequence seq = IcytomineUtil.loadImage(instance, cytomine,
				controller.getMaxSize(), processFrame);

		Icy.getMainInterface().addSequence(seq);

		processFrame.setGlobalProgress(100);
	}

	/**
	 * @param instance
	 *            open an ImageInstance in Icy allowing it to be viewed with all
	 *            annotations from cytomines
	 */
	public void openInIcyWithAnnotations(ImageInstance instance) {

		processFrame.newAction();

		Sequence sequence = IcytomineUtil.loadImage(instance, cytomine,
				controller.getMaxSize(), processFrame);

		Dimension thumbnailSize = new Dimension(sequence.getWidth(),
				sequence.getHeight());
		List<CytomineImportedROI> rois = getAnnotations(instance, thumbnailSize);

		for (int i = 0; i < rois.size(); i++) {
			sequence.addROI(rois.get(i));
		}

		Icy.getMainInterface().addSequence(sequence);

		processFrame.setGlobalProgress(100);

	}

	/**
	 * @param instance
	 * @param thumbnailSize
	 * @return list of roi generated from cytomine annotations for an
	 *         imageInstance
	 */
	private List<CytomineImportedROI> getAnnotations(ImageInstance instance,
			Dimension thumbnailSize) {

		ArrayList<CytomineImportedROI> rois = new ArrayList<CytomineImportedROI>();

		try {
			Map<String, String> filter = new TreeMap<String, String>();
			filter.put("user", "" + cytomine.getCurrentUser().getLong("id"));
			filter.put("image", "" + instance.getLong("id"));

			AnnotationCollection collection = cytomine.getAnnotations(filter);

			for (int i = 0; i < collection.size(); i++) {
				Long annonationID = collection.get(i).getLong("id");

				Annotation annotation = cytomine.getAnnotation(annonationID);

				String polygon = annotation.getStr("location");

				Dimension imageSize = new Dimension(instance.getInt("width"),
						instance.getInt("height"));
				int imageSizeY = instance.getInt("height");

				Point2D ratio = IcytomineUtil.getScaleRatio(imageSize,
						thumbnailSize);

				rois.add(CytomineImportedROI.build(
						IcytomineUtil.WKTtoPoint2D(polygon, ratio, imageSizeY),
						annotation, cytomine));

			}
			return rois;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rois;
	}

	/**
	 * @param imageInstance
	 * @return a preview icon image for the ImageInstance
	 */
	public ImageIcon getIcon(ImageInstance imageInstance) {

		ImageIcon icon = new ImageIcon();

		long ID = imageInstance.getLong("baseImage");

		try {

			BufferedImage image = cytomine
					.downloadAbstractImageAsBufferedImage(ID,
							Config.previewDefaultMaxSize);

			icon = new ImageIcon(image);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return icon;
	}

	/**
	 * @param image
	 *            delete all the annotations for an ImageInstance
	 */
	public void deleteRoi(ImageInstance image) {

		processFrame.newAction();

		IcytomineUtil.deleteAllRoi(cytomine, image, processFrame);

		processFrame.setGlobalProgress(100);

	}

	/**
	 * @param instance
	 *            generate annotation for the ImageInstance then open the image
	 *            in Cytomine as a Sequence with ROI
	 */
	public void generateSection(ImageInstance instance) {

		processFrame.newAction();

		Sequence seq = IcytomineUtil.loadImage(instance, cytomine,
				controller.getMaxSize(), processFrame);

		IcytomineUtil.generateSectionsROI(cytomine, instance, seq,
				processFrame);

		Icy.getMainInterface().addSequence(seq);

		processFrame.setGlobalProgress(100);

	}

	/**
	 * @param instance
	 * @param terms
	 *            upload all roi of the currently selected sequence
	 */
	public void uploadRoi(ImageInstance instance,
			DefaultListModel<Object> termsList) {

		processFrame.newAction();

		Sequence sequence = Icy.getMainInterface().getActiveSequence();

		if (sequence != null) {

			List<Long> terms = new ArrayList<Long>();
			for (int j = 0; j < termsList.size(); j++) {
				terms.add(((AnnotationTerm) termsList.get(j)).getId());
			}

			for (int i = 0; i < sequence.getROIs().size(); i++) {
				ROI roi = sequence.getROIs().get(i);

				// convert to ROI for cytomine if possible
				try {
					@SuppressWarnings("unused")
					CytomineImportedROI cytoROI = (CytomineImportedROI) roi;
				} catch (Exception e) {
					Annotation annotation = new Annotation();
					annotation.set("term", terms.toString());

					CytomineImportedROI newROI = new CytomineImportedROI(
							((ROI2DPolygon) roi).getPoints(), annotation);
					newROI.setColor(Color.black);
					sequence.getROI2Ds().set(i, newROI);

					sequence.removeROI(roi);
					sequence.addROI(newROI);
				}

			}

			IcytomineUtil.uploadROI(cytomine, instance, sequence, processFrame);

			processFrame.setGlobalProgress(100);

		} else {
			MessageDialog
					.showDialog("You need to have an Active sequence open to do this action !");
		}

	}

	/**
	 * @param instance
	 * @param terms
	 *            delete all the annotations of an ImageInstance then upload all
	 *            roi of the selected sequence
	 */
	public void updateRoi(ImageInstance instance, DefaultListModel<Object> terms) {
		deleteRoi(instance);
		uploadRoi(instance, terms);

	}

	public void generateGlomerule(ImageInstance instance) {

		processFrame.newAction();
		
		IcytomineUtil.generateGlomerule(cytomine, instance, 2, processFrame);
		
		processFrame.setGlobalProgress(100);
		
	}

}
