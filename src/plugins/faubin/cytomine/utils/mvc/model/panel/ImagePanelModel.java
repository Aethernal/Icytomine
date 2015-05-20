package plugins.faubin.cytomine.utils.mvc.model.panel;

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
import plugins.faubin.cytomine.Config;
import plugins.faubin.cytomine.utils.cytomine.AnnotationTerm;
import plugins.faubin.cytomine.utils.cytomine.CytomineUtil;
import plugins.faubin.cytomine.utils.mvc.controller.panel.ImagePanelController;
import plugins.faubin.cytomine.utils.mvc.model.utils.ThreadForRunnablePool;
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
		Sequence seq = CytomineUtil.loadImage(instance, cytomine,
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

		Sequence sequence = CytomineUtil.loadImage(instance, cytomine,
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

		Sequence seq = CytomineUtil.loadImage(instance, cytomine,
				controller.getMaxSize(), processFrame);

		List<CytomineImportedROI> rois = CytomineUtil.generateSectionsROI(seq,
				processFrame);

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

			CytomineUtil.uploadROI(cytomine, instance, sequence, processFrame);

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

	public void generateGlomerule(ImageInstance instance, CytomineReader preview) {

		processFrame.newAction();

		Map<String, String> filter = new TreeMap<String, String>();
		try {
			filter.put("user", "" + cytomine.getCurrentUser().getLong("id"));
			filter.put("image", "" + instance.getLong("id"));
			filter.put("term", "" + Config.IDMap.get("ontology_section"));

			AnnotationCollection collection = cytomine.getAnnotations(filter);

			processFrame
					.println("Generating glomerule for sections annotations");
			for (int i = 0; i < collection.size(); i++) {
				Annotation annotation = cytomine.getAnnotation(collection
						.get(i).getLong("id"));

				String poly = annotation.getStr("location");

				int minX = 0, maxX = 0, minY = 0, maxY = 0;

				WKTReader reader = new WKTReader();
				try {
					processFrame.println("generating bounding box");
					// create bounding box of the annotation
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

					// generated bounding box
					Rectangle rect = new Rectangle(minX, minY, maxX - minX,
							maxY - minY);

					processFrame.println("bounding box generation done");

					// variables
					int x = rect.x;
					int y = rect.y;
					int width = rect.width;
					int height = rect.height;
					int zoom = 2;

					int delta = zoom;
					double ratio = Math.pow(2, -delta);

					final Point2D position = new Point2D.Double((int) (x * ratio),
							(int) (y * ratio));
					final Dimension size = new Dimension((int) (width * ratio),
							(int) (height * ratio));

					
					processFrame.println("generating tiles of section");
					List<Tile> tiles = preview.getCrop(position, size, zoom);
					processFrame.println("generation done");
					
					// buffered image for rendering tiles
					BufferedImage img = new BufferedImage(size.width,
							size.height, BufferedImage.TYPE_INT_RGB);
					Graphics2D graph = img.createGraphics();

					graph.setColor(Color.white);
					graph.fillRect(0, 0, size.width, size.height);

					final Sequence sequence = new Sequence();
					processFrame.println("Downloading tiles");
					for (int j = 0; j < tiles.size(); j++) {
						Tile tile = tiles.get(j);
						BufferedImage image = cytomine
								.downloadPictureAsBufferedImage(tile.getUrl());
						tile.image = image;

						graph.drawImage(image, (int)(tile.getC() - position.getX()),
								(int)(tile.getR() - position.getY()), null);

						processFrame.setActionProgress((int) ((double) (j + 1)
								/ tiles.size() * 100));
					}

					// generate sequence with tiles assembled in a BufferedImage
					sequence.setImage(0, 0, img);
					processFrame.println("download done");

					Sequence chan0 = SequenceUtil.extractChannel(sequence, 0);

					// generate histogramme and seuil for threshold
					int[] histogramme = CustomThreshold.getHistogram(chan0
							.getFirstImage());
					final int seuil = CustomThreshold.generateSeuil(
							histogramme, chan0.getWidth(), chan0.getHeight());

					// show result
//					Icy.getMainInterface().addSequence(sequence);

					// pool variable for multi thread calcul
					Stack<Runnable> runnablePool = new Stack<Runnable>();
					ThreadForRunnablePool threadPool[] = new ThreadForRunnablePool[8];

					processFrame.println("processing tiles of sections");
					for (int j = 0; j < tiles.size(); j++) {
						final Tile tile = tiles.get(j);

						if (tile.image != null) {
							Runnable runnable = new Runnable() {

								@Override
								public void run() {
									
//									// show an roi for all tiles
//									 List<Point2D> pts = new ArrayList<Point2D>();
//									 pts.add(new
//									 Point2D.Double(tile.getC()-position.x,tile.getR()-position.y));
//									 pts.add(new
//									 Point2D.Double(tile.getC()-position.x+256,tile.getR()-position.y));
//									 pts.add(new
//									 Point2D.Double(tile.getC()-position.x+256,tile.getR()-position.y+256));
//									 pts.add(new
//									 Point2D.Double(tile.getC()-position.x,tile.getR()-position.y+256));
//									 ROI2DPolygon tileROI = new ROI2DPolygon();
//									 tileROI.setPoints(pts);
//									 tileROI.setColor(Color.GREEN);
//									 tileROI.setName("Tile ["+tile.getC()/256+"]["+tile.getR()/256+"]");
//									
//									 sequence.addROI(tileROI);
									
									// start glomeruli detection
									Sequence seq = new Sequence(tile.image);
									seq = SequenceUtil.extractChannel(seq, 0);
									CytomineUtil.generateGlomeruleROI(cytomine,
											seq, processFrame, seuil);

									List<ROI2D> rois = seq.getROI2Ds();
									for (int k = 0; k < rois.size(); k++) {
										ROI2D roi = rois.get(k);
										
										double posX = tile.getC() - position.getX() + roi.getPosition2D().getX();
										double posY = tile.getR() - position.getY() + roi.getPosition2D().getY();
										
										roi.setPosition2D( new Point2D.Double( posX, posY ) );
										sequence.addROI(roi);

									}

								}
							};

							runnablePool.push(runnable);
						}

					}

					for (int j = 0; j < threadPool.length; j++) {
						threadPool[j] = new ThreadForRunnablePool(runnablePool);
						threadPool[j].start();
					}

					for (int k = 0; k < threadPool.length; k++) {
						try {
							threadPool[k].join();

						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						processFrame.setActionProgress((int) ((double) (k + 1)
								/ threadPool.length * 100));
					}

					//uploading generated roi
					List<ROI2DPolygon> polygonList = new ArrayList<ROI2DPolygon>();
					
					List<ROI2D> rois = sequence.getROI2Ds();
					
					for (int j = 0; j < rois.size(); j++) {
						try{
							ROI2DPolygon polygon = (ROI2DPolygon) rois.get(j);
							
							polygonList.add(polygon);
							
						}catch(Exception e){
						}
					}
					
					processFrame.println("Uploading roi");
					
					List<Long> terms = new ArrayList<Long>();
					terms.add(Config.IDMap.get("ontology_glomerule"));
					
					CytomineUtil.uploadROI(cytomine, controller.getView().getInstance(),polygonList, 1/ratio, (int) (instance.getInt("height")), new Point2D.Double(position.getX(), position.getY() ), terms);
					processFrame.println("Uploading done");
					
					processFrame.println("processing done");

				} catch (ParseException e) {
					e.printStackTrace();
				}
				processFrame.setGlobalProgress((int) ((double) (i + 1)
						/ collection.size() * 100));
				System.gc();

			}

		} catch (CytomineException e) {
			e.printStackTrace();
		}

	}

}
