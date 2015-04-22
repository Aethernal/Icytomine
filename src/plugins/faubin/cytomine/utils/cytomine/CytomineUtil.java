package plugins.faubin.cytomine.utils.cytomine;

import icy.image.IcyBufferedImage;
import icy.image.IcyBufferedImageUtil;
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

import plugins.adufour.roi.LabelExtractor;
import plugins.adufour.roi.LabelExtractor.ExtractionType;
import plugins.adufour.thresholder.Thresholder;
import plugins.faubin.cytomine.Config;
import plugins.faubin.cytomine.utils.roi.roi2dpolygon.CytomineImportedROI;
import plugins.faubin.cytomine.utils.threshold.Otsu;
import plugins.kernel.roi.roi2d.ROI2DArea;
import plugins.kernel.roi.roi2d.ROI2DPolygon;
import plugins.tprovoost.bestthreshold.BestThreshold;
import plugins.vannary.morphomaths.MorphOp;
import plugins.ylemontag.histogram.BadHistogramParameters;
import plugins.ylemontag.histogram.Histogram;
import be.cytomine.client.Cytomine;
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

	public static Sequence loadImage(ImageInstance instance, Cytomine cytomine,
			int maxSize) {
		String name = instance.getStr("id");
		Long ID = instance.getLong("baseImage");

		try {

			BufferedImage image = cytomine
					.downloadAbstractImageAsBufferedImage(ID, maxSize);
			IcyBufferedImage icyImage = IcyBufferedImage.createFrom(image);
			Sequence sequence = new Sequence(icyImage);
			sequence.setName(name);
			return sequence;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Sequence();
	}

	/**
	 * @param cytomine
	 * @param instance
	 * @return the number of deleted roi
	 */
	public static int deleteAllRoi(Cytomine cytomine, ImageInstance instance) {
		int nbRemoved = 0;
		try {
			Map<String, String> filter = new TreeMap<String, String>();
			filter.put("user", ""+cytomine.getCurrentUser());
			filter.put("image", ""+instance.getLong("id"));
			
			AnnotationCollection annotations = cytomine.getAnnotations(filter);

			for (int i = 0; i < annotations.size(); i++) {
				cytomine.deleteAnnotation(annotations.get(i).getLong("id"));
				nbRemoved++;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return nbRemoved;
	}

	public static int deleteAllRoiWithTerm(Cytomine cytomine,
			ImageInstance instance, Long idTerm) {
		int nbRemoved = 0;
		try {
			Map<String, String> filter = new TreeMap<String, String>();
			filter.put("user", ""+cytomine.getCurrentUser());
			filter.put("image", ""+instance.getLong("id"));
			filter.put("term", ""+idTerm);
			
			AnnotationCollection annotations = cytomine.getAnnotations(filter);

			for (int i = 0; i < annotations.size(); i++) {
				cytomine.deleteAnnotation(annotations.get(i).getLong("id"));
				nbRemoved++;
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
	public static int uploadRoi(Cytomine cytomine, ImageInstance instance,
			Sequence sequence) {
		int nbUploaded = 0;

		if (sequence != null) {

			Dimension thumbnailSize = new Dimension(sequence.getWidth(),
					sequence.getHeight());

			long ID = instance.getLong("id");

			try {

				List<CytomineImportedROI> rois = CytomineUtil
						.Roi2DPolyToCytomineImportedROI(CytomineUtil
								.roiListToRoi2DPolygonList(new ArrayList<ROI>(
										sequence.getROI2Ds())));

				for (CytomineImportedROI roi : rois) {

					Dimension imageSize = new Dimension(
							instance.getInt("width"), instance.getInt("height"));
					int imageSizeY = instance.getInt("height");

					Point2D ratio = CytomineUtil.getScaleRatio(thumbnailSize,
							imageSize);

					String polygon = CytomineUtil.ROItoWKT(roi, ratio,
							imageSizeY);
					
					if (!roi.terms.isEmpty()) {
						cytomine.addAnnotationWithTerms(polygon, ID, roi.terms);
						
					} else {
						cytomine.addAnnotation(polygon, ID);
					}

					nbUploaded++;

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return nbUploaded;
	}

	/**
	 * use BestThreshold plugin with Otsu method
	 * 
	 * @param seq
	 * @return the thresholded sequence
	 */
	public static Sequence thresholdOtsu(Sequence seq) {

		int otsu = BestThreshold.exec(seq, 0, 0, 2, "Otsu");
		if (otsu > 230) {
			otsu = 230; // TODO to fix the code of Otsu cf code
		}

		double[] thresholds = new double[1];
		thresholds[0] = otsu;

		// execute threshold
		Sequence result = Thresholder.threshold(seq, 0, thresholds, false);

		// set name for new sequence
		result.setName(seq.getName() + " + Otsu");

		return result;
	}

	/**
	 * extract roi from a thresholded sequence
	 * 
	 * @param seq
	 * @return a list of the generated rois as ROI2DArea
	 */
	public static List<ROI2DArea> labelExtract(Sequence seq) {

		List<ROI> result = LabelExtractor.extractLabels(seq,
				ExtractionType.ALL_LABELS_VS_BACKGROUND, 1);

		List<ROI2DArea> resultAsArea = new ArrayList<ROI2DArea>();

		for (int i = 0; i < result.size(); i++) {
			resultAsArea.add((ROI2DArea) result.get(i));
		}

		return resultAsArea;
	}

	/**
	 * @param rois
	 *            ROI2DArea
	 * @return a list of ROI2DPolygon made from ROI2DArea
	 */
	public static List<ROI2DPolygon> convertToPolygon(List<ROI2DArea> rois) {

		List<ROI2DPolygon> newRois = new ArrayList<ROI2DPolygon>();

		for (ROI2DArea roi : rois) {
			Point2D p = roi.getBooleanMask(false).getPoints()[0];
			ROI2DPolygon poly = (ROI2DPolygon) /* Segment.triangulate(roi) /* *//*
																				 * Convexify
																				 * .
																				 * createConvexROI
																				 * (
																				 * roi
																				 * )
																				 * ;
																				 * /
																				 * *
																				 */convertFromArea(roi) /**/;
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
		ROI2DPolygon poly = new ROI2DPolygon();
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

		return poly = new ROI2DPolygon(right);

	}

	/**
	 * @param seq
	 * @return list of generated rois
	 */
	public static List<CytomineImportedROI> generateSectionsROI(Sequence seq) {
		List<CytomineImportedROI> result = new ArrayList<CytomineImportedROI>();

		int[] histo = CytomineUtil.generateHistogramme(seq);

		int seuil = Otsu.threshold(histo, seq.getWidth(), seq.getHeight());

		Sequence thresholded = Thresholder.threshold(seq, 0,
				new double[] { seuil }, false);

		MorphOp morph = new MorphOp();
		double[][] eltS2D = new double[][] { { 1.0, 1.0, 1.0 },
				{ 1.0, 1.0, 1.0 }, { 1.0, 1.0, 1.0 } };
		morph.closeGreyScale(thresholded, 0, eltS2D, 1, 1);

		// extract image label to generate roi

		List<ROI2DArea> rois = CytomineUtil.labelExtract(thresholded);

		try {

			// process generated points
			List<CytomineImportedROI> roisConvex = CytomineUtil
					.Roi2DPolyToCytomineImportedROI(CytomineUtil
							.convertToPolygon(rois));

			// section term id
			List<Long> terms = new ArrayList<Long>();
			terms.add(Config.globalID.get("ontology_section"));

			for (CytomineImportedROI roi : roisConvex) {
				// add generated roi to sequence
				// only take roi that are at least 30 pixel in width or height
				if (roi.getBounds5D().getSizeX() >= 30
						|| roi.getBounds5D().getSizeY() >= 30) {
					roi.terms = terms;
					result.add(roi);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
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

		// Scales points to transfer from the thumbil to Cytomine
		qpts = ConvertPointsForCytomine(qpts, ratio, imageSizeY);

		// add points to the point list in the polygon
		for (int i = 0; i < qpts.size(); i++) {
			points += qpts.get(i).getX() + " " + qpts.get(i).getY();
			points += ",";
		}

		// add the first point again to close the polygon
		points += qpts.get(0).getX() + " " + qpts.get(0).getY();

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
		// todo
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

	public static int[] generateHistogramme(Sequence sequence) {
		int[] histo = new int[255];
		try {
			Histogram hist = Histogram.compute(sequence, 255, 0, 255);
			for (int i = 0; i < 255; i++) {
				histo[i] = hist.getBin(i).getCount();
			}

		} catch (BadHistogramParameters e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return histo;
	}

}
