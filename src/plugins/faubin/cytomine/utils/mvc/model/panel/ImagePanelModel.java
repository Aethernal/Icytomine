package plugins.faubin.cytomine.utils.mvc.model.panel;

import icy.gui.dialog.MessageDialog;
import icy.main.Icy;
import icy.roi.ROI;
import icy.roi.ROI2D;
import icy.sequence.Sequence;
import icy.system.thread.ThreadUtil;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import plugins.faubin.cytomine.Config;
import plugins.faubin.cytomine.utils.cytomine.AnnotationTerm;
import plugins.faubin.cytomine.utils.cytomine.CytomineUtil;
import plugins.faubin.cytomine.utils.mvc.controller.panel.ImagePanelController;
import plugins.faubin.cytomine.utils.mvc.template.Model;
import plugins.faubin.cytomine.utils.mvc.view.frame.ProcessingFrame;
import plugins.faubin.cytomine.utils.roi.roi2dpolygon.CytomineImportedROI;
import plugins.faubin.cytomine.utils.threshold.CustomThreshold;
import plugins.kernel.roi.roi2d.ROI2DPolygon;
import utils.CytomineReader;
import utils.Tile;
import be.cytomine.client.Cytomine;
import be.cytomine.client.CytomineException;
import be.cytomine.client.collections.AnnotationCollection;
import be.cytomine.client.models.Annotation;
import be.cytomine.client.models.ImageInstance;

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
		Sequence seq = CytomineUtil.loadImage(instance, cytomine, controller.getMaxSize(), processFrame);
		
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
		
		Sequence sequence = CytomineUtil.loadImage(instance, cytomine, controller.getMaxSize(), processFrame);

		Dimension thumbnailSize = new Dimension(sequence.getWidth(), sequence.getHeight());
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
		
		processFrame.newAction();
		
		CytomineUtil.deleteAllRoi(cytomine, image, processFrame);
		
		processFrame.setGlobalProgress(100);
		
	}

	/**
	 * @param instance
	 *            generate annotation for the ImageInstance then open the image
	 *            in Cytomine as a Sequence with ROI
	 */
	public void generateSection(ImageInstance instance) {
		
		processFrame.newAction();
		
		Sequence seq = CytomineUtil.loadImage(instance, cytomine, controller.getMaxSize(), processFrame);

		List<CytomineImportedROI> rois = CytomineUtil.generateSectionsROI(seq, processFrame);
		
		for (int i = 0; i < rois.size(); i++) {

			seq.addROI(rois.get(i));
		}


		Icy.getMainInterface().addSequence(seq);

		processFrame.setGlobalProgress(100);
		
	}

	/**
	 * @param instance
	 * @param terms
	 *            upload all roi of the currently selected sequence
	 */
	public void uploadRoi(ImageInstance instance, DefaultListModel termsList) {

		processFrame.newAction();
		
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
			
			CytomineUtil.uploadRoi(cytomine, instance, sequence, processFrame);
			
			processFrame.setGlobalProgress(100);
			
		}else{
			MessageDialog.showDialog("You need to have an Active sequence open to do this action !");
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

	public void generateGlomerule(ImageInstance instance, CytomineReader preview) {
		
		processFrame.newAction();
		
		Map<String, String> filter = new TreeMap<String, String>();
		try {
			filter.put("user", "" + cytomine.getCurrentUser().getLong("id"));
			filter.put("image", "" + instance.getLong("id"));
			filter.put("term", "" + Config.globalID.get("ontology_section"));

			AnnotationCollection collection = cytomine.getAnnotations(filter);
			
			processFrame.println("Generating glomerule for sections annotations");
			for (int i = 0; i < collection.size(); i++) {
				Annotation annotation = cytomine.getAnnotation(collection.get(i).getLong("id"));
				
				String poly = annotation.getStr("location");
				
				int minX = 0, maxX = 0, minY = 0, maxY = 0;
				
				WKTReader reader = new WKTReader();
				try {
					
					//create bounding box of the annotation
					Geometry geo = reader.read(poly);
					Coordinate coords[] = geo.getCoordinates();

					minX = (int) coords[0].x;
					minY = (int) coords[0].y;
					maxX = (int) coords[0].x;
					maxY = (int) coords[0].y;
					
					for (int j = 0; j < coords.length; j++) {
						maxX = (int) Math.max(maxX, coords[j].x);
						maxY = (int) Math.max(maxY, coords[j].y);
						
						minX = (int) Math.min(minX, coords[j].x);
						minY = (int) Math.min(minY, coords[j].y);
						
					}
					
					//generated bounding box
					Rectangle rect = new Rectangle(minX,minY,maxX-minX,maxY-minY);
					
					//variables
					int x = rect.x;
					int y = rect.y;
					int width = rect.width;
					int height = rect.height;
					int zoom = 3;
					
					int delta = zoom;
					double ratio = Math.pow(2, -delta);
					
					Point position = new Point( (int)(x*ratio), (int)(y*ratio));
					Dimension size = new Dimension( (int)(width*ratio), (int)(height*ratio));
					
					List<Tile> tiles = preview.getCrop(position, size, zoom);
					
					//buffered image for rendering tiles
					BufferedImage img = new BufferedImage( size.width, size.height, BufferedImage.TYPE_INT_RGB);
					Graphics2D graph = img.createGraphics();
					
					graph.setColor(Color.white);
					graph.fillRect(0, 0, size.width, size.height);
					
					Sequence sequence = new Sequence();
					processFrame.println("Collecting tiles of sections annotations");
					for (int j=0 ; j < tiles.size() ; j++){
						Tile tile = tiles.get(j);
						BufferedImage image = cytomine.downloadPictureAsBufferedImage(tile.getUrl());
						tile.image = image;
						
						graph.drawImage(image, tile.getC()-position.x, tile.getR()-position.y, null);
						
						processFrame.setActionProgress((int)((double)(j+1)/tiles.size()*100));
					}
					processFrame.println("collecting done");
					
					//generate sequence with tiles assembled in a BufferedImage
					sequence.setImage(0, 0, img);
					
					//generate histogramme and seuil for threshold
					int[] histogramme = CustomThreshold.getHistogram(sequence.getFirstImage());
					int seuil = CustomThreshold.generateSeuil(histogramme, sequence.getWidth(), sequence.getHeight());
	
					processFrame.println("processing tiles of sections");
					for (int j=0 ; j < tiles.size() ; j++){
						Tile tile = tiles.get(j);
						
						if(tile.image!=null){
							//start glomeruli detection
							Sequence seq = new Sequence(tile.image);
							CytomineUtil.generateGlomeruleROI(cytomine, seq, processFrame, seuil);
							List<ROI2D> rois = seq.getROI2Ds();
							
							for (int k = 0; k < rois.size(); k++) {
								ROI2D roi = rois.get(k);
								roi.setPosition2D( new Point2D.Double(tile.getC()-position.x, tile.getR()-position.y) );
								sequence.addROI(roi);
							}
							
						}
						processFrame.setActionProgress((int)((double)(j+1)/tiles.size()*100));
					}
					processFrame.println("processing done");
					
					//show result
					Icy.getMainInterface().addSequence(sequence);
					
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				processFrame.setGlobalProgress((int)((double)(i+1)/collection.size()*100));
				System.gc();
		
				
			}
			
			
			
			
		} catch (CytomineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
