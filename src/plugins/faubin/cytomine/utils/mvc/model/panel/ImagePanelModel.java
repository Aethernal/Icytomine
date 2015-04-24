package plugins.faubin.cytomine.utils.mvc.model.panel;

import icy.main.Icy;
import icy.roi.ROI;
import icy.roi.ROI2D;
import icy.sequence.Sequence;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;

import plugins.adufour.thresholder.Thresholder;
import plugins.faubin.cytomine.Config;
import plugins.faubin.cytomine.utils.cytomine.AnnotationTerm;
import plugins.faubin.cytomine.utils.cytomine.CytomineUtil;
import plugins.faubin.cytomine.utils.mvc.controller.panel.ImagePanelController;
import plugins.faubin.cytomine.utils.mvc.template.Model;
import plugins.faubin.cytomine.utils.roi.roi2dpolygon.CytomineImportedROI;
import plugins.faubin.cytomine.utils.threshold.Otsu;
import plugins.kernel.roi.roi2d.ROI2DPolygon;
import plugins.tprovoost.bestthreshold.BestThreshold;
import plugins.vannary.morphomaths.MorphOp;
import be.cytomine.client.Cytomine;
import be.cytomine.client.collections.AnnotationCollection;
import be.cytomine.client.models.Annotation;
import be.cytomine.client.models.ImageInstance;

public class ImagePanelModel extends Model {

	private ImagePanelController controller;

	/**
	 * @param cytomine
	 * @param controller
	 */
	public ImagePanelModel(Cytomine cytomine, ImagePanelController controller) {
		super(cytomine);
		this.controller = controller;
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
		Sequence seq = CytomineUtil.loadImage(instance, cytomine,
				controller.getMaxSize());


		
		Icy.getMainInterface().addSequence(seq);
	}

	/**
	 * @param instance
	 *            open an ImageInstance in Icy allowing it to be viewed with all
	 *            annotations from cytomines
	 */
	public void openInIcyWithAnnotations(ImageInstance instance) {

		Sequence sequence = CytomineUtil.loadImage(instance, cytomine,
				controller.getMaxSize());

		Dimension thumbnailSize = new Dimension(sequence.getWidth(),
				sequence.getHeight());
		List<CytomineImportedROI> rois = getAnnotations(instance, thumbnailSize);

		for (int i = 0; i < rois.size(); i++) {
			sequence.addROI(rois.get(i));
		}

		Icy.getMainInterface().addSequence(sequence);

	}

	/**
	 * @param instance
	 * @param thumbnailSize
	 * @return list of roi generated from cytomine annotations for an
	 *         imageInstance
	 */
	private List<CytomineImportedROI> getAnnotations(ImageInstance instance,
			Dimension thumbnailSize) {

		long ID = instance.getLong("id");

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

				Point2D ratio = CytomineUtil.getScaleRatio(imageSize,
						thumbnailSize);

				rois.add(CytomineImportedROI.build(
						CytomineUtil.WKTtoROI(polygon, ratio, imageSizeY),
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

		URL url;
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
		try {
			Map<String, String> filter = new TreeMap<String, String>();
			filter.put("user", "" + cytomine.getCurrentUser().getLong("id"));
			filter.put("image", "" + image.getLong("id"));

			AnnotationCollection collection = cytomine.getAnnotations(filter);

			for (int i = 0; i < collection.size(); i++) {
				try {
					cytomine.deleteAnnotation(collection.get(i).getLong("id"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * @param instance
	 *            generate annotation for the ImageInstance then open the image
	 *            in Cytomine as a Sequence with ROI
	 */
	public void generateSection(ImageInstance instance) {
		Sequence seq = CytomineUtil.loadImage(instance, cytomine,
				controller.getMaxSize());

		List<CytomineImportedROI> rois = CytomineUtil.generateSectionsROI(seq);
		
		int[] histo = CytomineUtil.generateHistogramme(seq);

		int seuil = Otsu.threshold(histo, seq.getWidth(), seq.getHeight());
	
		Sequence thresholded = Otsu.threshold(seq, seuil);
		MorphOp morph = new MorphOp();
		double[][] eltS2D = new double[][] { { 1.0, 1.0, 1.0 },
				{ 1.0, 1.0, 1.0 }, { 1.0, 1.0, 1.0 } };
		morph.closeGreyScale(thresholded, 0, eltS2D, 1, 1);
		
		for (int i = 0; i < rois.size(); i++) {

			thresholded.addROI(rois.get(i));
		}


		Icy.getMainInterface().addSequence(thresholded);

	}

	/**
	 * @param instance
	 * @param terms
	 *            upload all roi of the currently selected sequence
	 */
	public void uploadRoi(ImageInstance instance, DefaultListModel termsList) {

		Sequence sequence = Icy.getMainInterface().getActiveSequence();

		if (sequence != null) {
			
			List<Long> terms = new ArrayList<Long>();
			for (int j = 0; j < termsList.size(); j++) {
				terms.add( ( (AnnotationTerm) termsList.get(j)).getId() );
			}
			
			for (int i = 0; i < sequence.getROIs().size(); i++) {
				ROI roi = sequence.getROIs().get(i);
				
				//convert to ROI for cytomine if possible
				try{
					ROI2DPolygon polyROI = (ROI2DPolygon) roi;
					try{
						CytomineImportedROI cytoROI = (CytomineImportedROI) roi;
					}catch(Exception e){
						Annotation annotation = new Annotation();
						annotation.set("term", terms.toString());
						
						CytomineImportedROI newROI = new CytomineImportedROI( ( (ROI2DPolygon) roi).getPoints() , annotation);
						newROI.setColor(Color.black);
						sequence.getROI2Ds().set(i, newROI);
						
						sequence.removeROI(roi);
						sequence.addROI(newROI);
					}
				}catch(Exception e){
					
				}
			
				
			}
			CytomineUtil.uploadRoi(cytomine, instance, sequence);
			
		}

	}

	/**
	 * @param instance
	 * @param terms
	 *            delete all the annotations of an ImageInstance then upload all
	 *            roi of the selected sequence
	 */
	public void updateRoi(ImageInstance instance, DefaultListModel terms) {
		deleteRoi(instance);
		uploadRoi(instance, terms);
	}

}
