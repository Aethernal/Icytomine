package plugins.faubin.cytomine.utils.cytomine;

import icy.image.IcyBufferedImage;
import icy.image.IcyBufferedImageUtil;
import icy.main.Icy;
import icy.roi.ROI;
import icy.roi.ROI2D;
import icy.sequence.Sequence;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import plugins.adufour.thresholder.Thresholder;
import plugins.faubin.contourdetector.Utils;
import plugins.faubin.cytomine.Config;
import plugins.faubin.cytomine.utils.mvc.view.frame.ProcessingFrame;
import plugins.faubin.cytomine.utils.roi.roi2dpolygon.CytomineImportedROI;
import plugins.faubin.cytomine.utils.threshold.CustomThreshold;
import plugins.faubin.glomeruledetector.GlomeruleDetector;
import plugins.kernel.roi.roi2d.ROI2DArea;
import plugins.kernel.roi.roi2d.ROI2DPolygon;
import be.cytomine.client.Cytomine;
import be.cytomine.client.CytomineException;
import be.cytomine.client.collections.AnnotationCollection;
import be.cytomine.client.models.ImageInstance;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class CytomineUtil {

	/**
	 * download an image from the ID
	 * 
	 * @return Sequence
	 * @throws Exception
	 */

	public static Sequence loadImage(ImageInstance instance, Cytomine cytomine, int maxSize , ProcessingFrame processFrame) {

		long name = instance.getLong("id");
		long ID = instance.getLong("baseImage");

		if(processFrame!=null){
			processFrame.println("downloading image "+name+" with maxSize "+maxSize+"");
		}
		BufferedImage image;
		try {
			image = cytomine.downloadAbstractImageAsBufferedImage(ID, maxSize);
			if(processFrame!=null){
				processFrame.println("downloading done");
				processFrame.setActionProgress(100);
			}
			IcyBufferedImage icyImage = IcyBufferedImage.createFrom(image);
			Sequence sequence = new Sequence(icyImage);
			
			sequence.setName(""+name);
			return sequence;
		} catch (CytomineException e) {
			if(processFrame!=null){
				processFrame.println("downloading finished with null result");
				processFrame.setActionProgress(100);
			}
			e.printStackTrace();
		}
		

		return new Sequence();
	}

	/**
	 * @param cytomine
	 * @param instance
	 * @return the number of deleted roi
	 */
	public static int deleteAllRoi(Cytomine cytomine, ImageInstance instance , ProcessingFrame processFrame) {
		int nbRemoved = 0;
		
			Map<String, String> filter = new TreeMap<String, String>();
			try {
				filter.put("user", "" + cytomine.getCurrentUser().getLong("id"));
				filter.put("image", "" + instance.getLong("id"));

				AnnotationCollection annotations = cytomine.getAnnotations(filter);

				if(processFrame!=null){
					processFrame.println(""+annotations.size()+" annotations will be deleted");
				}
				
				for (int i = 0; i < annotations.size(); i++) {
					cytomine.deleteAnnotation(annotations.get(i).getLong("id"));
					nbRemoved++;
					if(processFrame!=null){
						processFrame.setActionProgress((int)((double)i/annotations.size()*100));
					}
				}
				
				if(processFrame!=null){
					processFrame.println("delete done");
				}
				
			} catch (CytomineException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		return nbRemoved;
	}

	public static int deleteAllRoiWithTerm(Cytomine cytomine, ImageInstance instance, Long idTerm, ProcessingFrame processFrame) {
		int nbRemoved = 0;
		try {
			
			Map<String, String> filter = new TreeMap<String, String>();
			filter.put("user", "" + cytomine.getCurrentUser().getLong("id"));
			filter.put("image", "" + instance.getLong("id"));
			filter.put("term", "" + idTerm);

			AnnotationCollection annotations = cytomine.getAnnotations(filter);

			if(processFrame!=null){
				processFrame.println(""+annotations.size()+" annotations will be deleted");
			}
			
			for (int i = 0; i < annotations.size(); i++) {
				cytomine.deleteAnnotation(annotations.get(i).getLong("id"));
				nbRemoved++;
				if(processFrame!=null){
					processFrame.setActionProgress((int)((double)i/annotations.size()*100));
				}
			}

			if(processFrame!=null){
				processFrame.println("delete done");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return nbRemoved;
	}

	/**
	 * @param cytomine
	 * @param instance
	 * @param sequence
	 * @param term
	 * @return the number of uploaded roi
	 */
	public static int uploadROI(Cytomine cytomine, ImageInstance instance, Sequence sequence, ProcessingFrame processFrame) {
		int nbUploaded = 0;

		if (sequence != null) {

			Dimension thumbnailSize = new Dimension(sequence.getWidth(), sequence.getHeight());

			long ID = instance.getLong("id");

			try {

				List<CytomineImportedROI> rois = CytomineUtil
						.Roi2DPolyToCytomineImportedROI(CytomineUtil
								.roiListToRoi2DPolygonList(
										new ArrayList<ROI>(
												sequence.getROI2Ds() 
										) 
								) 
						);

				if(processFrame!=null){
					processFrame.println(rois.size()+" annotations will be uploaded");
				}
				
				for (CytomineImportedROI roi : rois) {
					
					Dimension imageSize = new Dimension(
							instance.getInt("width"), instance.getInt("height"));
					int imageSizeY = instance.getInt("height");

					Point2D ratio = CytomineUtil.getScaleRatio(thumbnailSize,
							imageSize);

					String polygon = CytomineUtil.ROItoWKT(roi, ratio, imageSizeY);

					if (!roi.terms.isEmpty()) {
						try {
							cytomine.addAnnotationWithTerms(polygon, ID,
									roi.terms);
						} catch (Exception e) {
							cytomine.addAnnotation(polygon, ID);
						}

					} else {
						cytomine.addAnnotation(polygon, ID);
					}
					
					nbUploaded++;
					
					if(processFrame!=null){
						processFrame.setActionProgress((int)((double)nbUploaded/rois.size()*100));
					}
					
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(processFrame!=null){
				processFrame.println("upload done");
			}
			
		}else{
			if(processFrame!=null){
				processFrame.println("the sequence is null, actions will not be done");
			}
		}
		return nbUploaded;
	}
	
	public static void uploadROI(Cytomine cytomine, ImageInstance instance, List<ROI2DPolygon> rois, double ratio, int height, Point2D translation, List<Long> termID){
		List<CytomineImportedROI> annotations = CytomineUtil.Roi2DPolyToCytomineImportedROI(rois);
		
		long ID = instance.getLong("id");
		
		for (int i = 0; i < annotations.size(); i++) {
			CytomineImportedROI polygon = annotations.get(i);
			
			polygon.terms = termID;
			polygon.translate(translation.getX(), translation.getY());
			Point2D ratioAsP2D = new Point2D.Double(ratio, ratio);
			try {
				String WKTPolygon = CytomineUtil.ROItoWKT(polygon, ratioAsP2D, height);
				try{
					if (!polygon.terms.isEmpty()) {
						
						try {
							cytomine.addAnnotationWithTerms(WKTPolygon, ID, polygon.terms);
						} catch (Exception e) {
							cytomine.addAnnotation(WKTPolygon, ID);
						}
	
					} else {
						cytomine.addAnnotation(WKTPolygon, ID);
					}
				} catch (CytomineException e) {
					e.printStackTrace();
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

	/**
	 * use BestThreshold plugin with Otsu method
	 * 
	 * @param seq
	 * @return the thresholded sequence
	 */
//	public static Sequence thresholdOtsu(Sequence seq) {
//
//		int otsu = BestThreshold.exec(seq, 0, 0, 2, "Otsu");
//		if (otsu > 230) {
//			otsu = 230; // TODO to fix the code of Otsu cf code
//		}
//
//		double[] thresholds = new double[1];
//		thresholds[0] = otsu;
//
//		// execute threshold
//		Sequence result = Thresholder.threshold(seq, 0, thresholds, false);
//
//		// set name for new sequence
//		result.setName(seq.getName() + " + Otsu");
//
//		return result;
//	}
	
	/**
	 * @param rois
	 *            ROI2DArea
	 * @return a list of ROI2DPolygon made from ROI2DArea
	 */
	public static List<ROI2DPolygon> convertToPolygon(List<ROI2DArea> rois) {

		List<ROI2DPolygon> newRois = new ArrayList<ROI2DPolygon>();

		for (ROI2DArea roi : rois) {
			ROI2DPolygon poly = (ROI2DPolygon) convertFromArea(roi);
			if (poly != null) {
				newRois.add(poly);
			}
		}

		return newRois;
	}

	/**
	 * @param pointslist
	 * @param degree
	 * @param bound
	 * @return rotated points as a List<Point2D>
	 */
	private static List<Point2D> rotatePoints(List<Point2D> pointslist,
			double degree, Rectangle bound) {
		double angle = Math.toRadians(degree);

		Point2D center = new Point2D.Double(bound.getCenterX(),
				bound.getCenterY());
		List<Point2D> newPoints = new ArrayList<Point2D>();

		for (int i = 0; i < pointslist.size(); i++) {
			Point2D point = pointslist.get(i);

			double newX = center.getX() + (point.getX() - center.getX())
					* Math.cos(angle) - (point.getY() - center.getY())
					* Math.sin(angle);

			double newY = center.getY() + (point.getX() - center.getX())
					* Math.sin(angle) + (point.getY() - center.getY())
					* Math.cos(angle);

			newPoints.add(new Point2D.Double(newX, newY));

		}

		return newPoints;
	}

	/**
	 * @param points
	 * @return sorted list
	 */
	private static List<Point2D> sortByXY(List<Point2D> points) {
		List<Point2D> sorted = new ArrayList<Point2D>();

		for (Point2D point : points) {
			if (sorted.isEmpty()) {
				sorted.add(point);
			}
			boolean placed = false;

			for (int i = 0; i < sorted.size(); i++) {
				if (sorted.get(i).getY() < point.getY()) {
					sorted.add(i, point);
					placed = true;
					break;
				} else if (sorted.get(i).getY() == point.getY()) {
					if (sorted.get(i).getX() < point.getX()) {
						sorted.add(i, point);
						placed = true;
						break;
					}
				}
			}
			if (!placed) {
				sorted.add(point);
			}

		}

		return sorted;
	}

	/**
	 * @param roi
	 * @return converted ROI2DPolygon
	 */
	private static ROI2DPolygon convertFromArea(ROI2DArea roi) {
		Point2D[] points = ((ROI2D) roi).getBooleanMask(false)
				.getContourPoints();
		int rotation = -1;
		Point2D minX = new Point2D.Double(0, 0), maxX = new Point2D.Double(0, 0);

		List<Point2D> pointslist = new ArrayList<Point2D>();
		for (int i = 0; i < points.length; i++) {
			pointslist.add(points[i]);
			if (i == 0) {
				minX = points[i];
				maxX = points[i];
			} else if (minX.getX() > points[i].getX()) {
				minX = points[i];
			} else if (maxX.getX() < points[i].getX()) {
				maxX = points[i];
			}

		}

		if (minX.getY() >= maxX.getY()) {
			rotation = 1;
		}

		if (roi.getBounds().getWidth() > roi.getBounds().getHeight()) {
			pointslist = sortByXY(rotatePoints(pointslist, 90 * rotation,
					roi.getBounds()));
		}

		List<List<Point2D>> listOfList = new ArrayList<List<Point2D>>();
		List<Point2D> list = new ArrayList<Point2D>();
		double lastY = 0;

		for (int i = 0; i < pointslist.size(); i++) {
			double currentY = pointslist.get(i).getY();
			double distance = Math.abs(currentY - lastY);

			// New line if Y has go past the maximum allowed for a line
			if (distance > 1) {
				lastY = currentY;
				listOfList.add(list);
				list = new ArrayList<Point2D>();
			}

			list.add(pointslist.get(i));

		}

		// creating the contour array
		List<Point2D> left = new ArrayList<Point2D>();
		List<Point2D> right = new ArrayList<Point2D>();

		// left contour
		for (List<Point2D> l : listOfList) {
			if (l.size() != 0) {
				left.add(l.get(0));
			}
		}

		// right contour
		for (List<Point2D> l : listOfList) {
			if (l.size() != 0) {
				right.add(l.get(l.size() - 1));
			}
		}

		// add left contour to right
		for (int i = 0; i < left.size(); i++) {
			right.add(left.get(left.size() - 1 - i));
		}

		// rotate to be on the good direction
		if (roi.getBounds().getWidth() > roi.getBounds().getHeight()) {
			right = rotatePoints(right, -90 * rotation, roi.getBounds());
		}

		return new ROI2DPolygon(right);

	}

	/**
	 * @param seq
	 * @return list of generated rois
	 */
	public static List<CytomineImportedROI> generateSectionsROI(Sequence seq, ProcessingFrame processFrame) {

		if(processFrame!=null){
			processFrame.println("Generating section of the sequence");
		}
		
		List<CytomineImportedROI> result = new ArrayList<CytomineImportedROI>();

		Sequence thresholded = SeqRGB2SeqBINARY(seq, processFrame, true);
		// extract image label to generate roi

		if(processFrame!=null){
			processFrame.setGlobalProgress(100/4);
			processFrame.println("finding contours");
		}
		
		List<ROI2DPolygon> rois = Utils.findContours(thresholded);

		if(processFrame!=null){
			processFrame.setGlobalProgress(100/4*2);
			processFrame.println("converting Icy roi to Cytomine annotations");
		}
		
		try {

			// process generated points
			List<CytomineImportedROI> roisConvex = CytomineUtil
					.Roi2DPolyToCytomineImportedROI(rois);

			if(processFrame!=null){
				processFrame.setGlobalProgress(100/4*3);
				processFrame.println("adding annotations to the sequence");
			}
			
			// section term id
			List<Long> terms = new ArrayList<Long>();
			terms.add(Config.IDMap.get("ontology_section"));
			
			int count = 0;
			for (CytomineImportedROI roi : roisConvex) {
				// add generated roi to sequence
				// only take roi that are at least 30 pixel in width or height

				int width = roi.getBounds().width;
				int height = roi.getBounds().height;

				if (width >= 30 && height >= 30) {
					roi.terms = terms;
					result.add(roi);
				}
				if(processFrame!=null){
					processFrame.setActionProgress((int)((double)count / roisConvex.size()*100));
				}
				count++;
			}
			
			if(processFrame!=null){
				processFrame.println("finished");
				processFrame.setGlobalProgress(100);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public static Sequence SeqRGB2SeqBINARY(Sequence seq, ProcessingFrame processFrame,boolean inversed) {
		if(processFrame!=null){
			processFrame.println("Generating histogramme for the image");
		}
		int[] histo = CustomThreshold.getHistogram(seq.getFirstImage());

		if(processFrame!=null){
			processFrame.println("thresholding");
			processFrame.setActionProgress(50);
		}
		
		int seuil = CustomThreshold.generateSeuil(histo, seq.getWidth(), seq.getHeight());

		Sequence thresholded = Thresholder.threshold(seq, 0, new double[]{seuil}, false);/**/ /*CustomThreshold.threshold(seq, seuil, inversed);/**/

		if(processFrame!=null){
			processFrame.println("thresholding done");
			processFrame.setActionProgress(100);
		}
		
		return thresholded;
	}

	/**
	 * generate a point that contain the ratio
	 * 
	 * @param thumbSize
	 * @param imageSize
	 * @return
	 */
	public static Point2D getScaleRatio(Dimension thumbSize, Dimension imageSize) {
		Point2D ratio;

		ratio = new Point2D.Double(imageSize.getWidth() / thumbSize.getWidth(),
				imageSize.getHeight() / thumbSize.getHeight());

		return ratio;
	}

	/**
	 * scale the points to the correct size
	 * 
	 * @param points
	 * @param ratio
	 * @return
	 * @throws Exception
	 */
	private static ArrayList<Point2D> ScalePoints(ArrayList<Point2D> points,
			Point2D ratio) {

		ArrayList<Point2D> newPoints = new ArrayList<Point2D>();

		for (int i = 0; i < points.size(); i++) {
			Point2D p = points.get(i);
			newPoints.add(new Point2D.Double(p.getX() * ratio.getX(), p.getY()
					* ratio.getY()));

		}

		return newPoints;
	}

	/**
	 * @param points
	 * @param imageSizeY
	 * @return corrected points
	 */
	private static ArrayList<Point2D> correctYPosition(
		ArrayList<Point2D> points, int imageSizeY) {
		ArrayList<Point2D> newPoints = new ArrayList<Point2D>();
		for (int i = 0; i < points.size(); i++) {
			newPoints.add(new Point2D.Double(points.get(i).getX(), imageSizeY
					- points.get(i).getY()));
		}

		return newPoints;
	}

	/**
	 * @param points
	 * @param ratio
	 * @param imageSizeY
	 * @return converted points to be used for cytomine
	 */
	private static ArrayList<Point2D> ConvertPointsForCytomine(
			ArrayList<Point2D> points, Point2D ratio, int imageSizeY) {
		return correctYPosition(ScalePoints(points, ratio), imageSizeY);
	}

	/**
	 * @param points
	 * @param ratio
	 * @param imageSizeY
	 * @return converted points to be user for Icy
	 */
	private static ArrayList<Point2D> ConvertPointsForIcy(
			ArrayList<Point2D> points, Point2D ratio, int imageSizeY) {
		return ScalePoints(correctYPosition(points, imageSizeY), ratio);
	}

	/**
	 * convert roi to wkt with resized point coordinate to match cytomine image
	 * scale
	 * 
	 * @param roi
	 * @return a polygon enveloppe representing the roi
	 * @throws ParseException
	 * @throws Exception
	 */
	public static String ROItoWKT(ROI2DPolygon roi, Point2D ratio,
			int imageSizeY) throws ParseException {
		String base = "POLYGON ((";
		String end = "))";
		String points = "";

		Polygon poly = roi.getPolygon();
		ArrayList<Point2D> qpts = new ArrayList<Point2D>();
		for (int i = 0; i < poly.npoints; i++) {
			qpts.add(new Point2D.Double(poly.xpoints[i], poly.ypoints[i]));
		}

		// Scales points to transfer from the thumbnail to Cytomine
		qpts = ConvertPointsForCytomine(qpts, ratio, imageSizeY);

		// add points to the point list in the polygon with the translation
		for (int i = 0; i < qpts.size(); i++) {
			points += (qpts.get(i).getX()) + " " + (qpts.get(i).getY());
			points += ",";
		}

		// add the first point again to close the polygon
		points += (qpts.get(0).getX()) + " " + (qpts.get(0).getY());

		return base + points + end;

	}

	/**
	 * @param polygon
	 * @param ratio
	 * @param imageSizeY
	 * @return a list of points representing the roi
	 */
	public static List<Point2D> WKTtoROI(String polygon, Point2D ratio,
			int imageSizeY) {
		WKTReader reader = new WKTReader();
		try {
			Geometry geo = reader.read(polygon);
			Coordinate coords[] = geo.getCoordinates();

			ArrayList<Point2D> qpts = new ArrayList<Point2D>();
			for (int i = 0; i < coords.length; i++) {
				qpts.add(new Point2D.Double(coords[i].x, coords[i].y));
			}

			qpts = ConvertPointsForIcy(qpts, ratio, imageSizeY);

			return qpts;

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * @param rois
	 * @return converted list
	 */
	public static List<ROI2DPolygon> roiListToRoi2DPolygonList(List<ROI> rois) {
		List<ROI2DPolygon> newList = new ArrayList<ROI2DPolygon>();

		for (int i = 0; i < rois.size(); i++) {
			newList.add((ROI2DPolygon) rois.get(i));
		}

		return newList;

	}

	/**
	 * @param rois
	 * @return converted list
	 */
	public static List<CytomineImportedROI> Roi2DPolyToCytomineImportedROI(
			List<ROI2DPolygon> rois) {
		List<CytomineImportedROI> newList = new ArrayList<CytomineImportedROI>();

		for (int i = 0; i < rois.size(); i++) {
			try {
				newList.add((CytomineImportedROI) rois.get(i));
			} catch (Exception e) {
				newList.add(new CytomineImportedROI(rois.get(i).getPoints(),
						null));
			}
		}

		return newList;

	}

	/**
	 * @param hexColor
	 * @return Color representing the hex color value
	 */
	public static Color hexToColor(String hexColor) {
		int red = Integer.parseInt(hexColor.substring(1, 3), 16);
		int green = Integer.parseInt(hexColor.substring(3, 5), 16);
		int blue = Integer.parseInt(hexColor.substring(5, 7), 16);
		Color termColor = new Color(red, green, blue);
		return termColor;
	}

	public static Sequence toGray(Sequence sequence) {
		BufferedImage gray = new BufferedImage(sequence.getWidth(),
				sequence.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		IcyBufferedImageUtil.toBufferedImage(sequence.getFirstImage(), gray);
		Sequence sequence2 = new Sequence(IcyBufferedImage.createFrom(gray));
		sequence2.setName(sequence.getName());

		return sequence2;
	}

	public static void generateGlomeruleROI(Cytomine cytomine,Sequence seq, ProcessingFrame processFrame, int seuil) {
		Sequence thresholded = Thresholder.threshold(seq, 0, new double[]{seuil}, false);//CustomThreshold.threshold(seq, seuil, true);
		if(processFrame!=null){
			processFrame.println("Detecting glomerules");
		}
		GlomeruleDetector.ellipses_detect(seq, thresholded, 200, 231000, 1, 2, 10);
		
		if(processFrame!=null){
			processFrame.println("detection done");
		}
		
	}

}
