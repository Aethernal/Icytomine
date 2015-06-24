package plugins.faubin.cytomine.utils;

import icy.image.IcyBufferedImage;
import icy.image.IcyBufferedImageUtil;
import icy.main.Icy;
import icy.roi.ROI;
import icy.roi.ROI2D;
import icy.sequence.Sequence;
import icy.sequence.SequenceUtil;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import javax.swing.JFileChooser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import plugins.adufour.thresholder.Thresholder;
import plugins.faubin.contourdetector.Utils;
import plugins.faubin.cytomine.module.main.mvc.frame.ProcessingFrame;
import plugins.faubin.cytomine.module.tileViewer.CytomineReader;
import plugins.faubin.cytomine.module.tileViewer.utils.Tile;
import plugins.faubin.cytomine.utils.crop.CropInformations;
import plugins.faubin.cytomine.utils.crop.CytomineCrop;
import plugins.faubin.cytomine.utils.software.Parameter;
import plugins.faubin.cytomine.utils.software.SoftwareData;
import plugins.faubin.cytomine.utils.software.SoftwareGlomeruleFinder;
import plugins.faubin.cytomine.utils.software.SoftwareSectionFinder;
import plugins.faubin.cytomine.utils.threshold.CustomThreshold;
import plugins.faubin.glomeruledetector.GlomeruleDetector;
import plugins.kernel.roi.roi2d.ROI2DArea;
import plugins.kernel.roi.roi2d.ROI2DPolygon;
import be.cytomine.client.Cytomine;
import be.cytomine.client.CytomineException;
import be.cytomine.client.collections.AnnotationCollection;
import be.cytomine.client.collections.OntologyCollection;
import be.cytomine.client.collections.SoftwareCollection;
import be.cytomine.client.collections.SoftwareParameterCollection;
import be.cytomine.client.collections.TermCollection;
import be.cytomine.client.models.Annotation;
import be.cytomine.client.models.ImageInstance;
import be.cytomine.client.models.Ontology;
import be.cytomine.client.models.Software;
import be.cytomine.client.models.SoftwareParameter;
import be.cytomine.client.models.Term;
import be.cytomine.client.models.User;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class IcytomineUtil {

	/**
	 * download an image from the ID
	 * 
	 * @return Sequence
	 * @throws Exception
	 */
	
	static Configuration configuration = Configuration.getConfiguration();

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
	 * delete all the user annotation from this image
	 * @param cytomine
	 * @param instance
	 * @return the number of deleted roi
	 */
	public static int deleteAllAnnotation(Cytomine cytomine, ImageInstance instance , ProcessingFrame processFrame) {
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

	/**
	 * this function is used to store ontology ID
	 * @param cytomine
	 * @param projectID
	 */
	public static void updateOntology(Cytomine cytomine, long projectID){
		
			
		OntologyCollection ontologies;
		try {
			ontologies = cytomine.getOntologies();

			for (int i = 0; i < ontologies.size(); i++) {
				Ontology onto = ontologies.get(i);
				try {
					TermCollection collection = cytomine.getTermsByOntology(onto.getId());
					for (int j = 0; j < collection.size(); j++) {
						Term term = collection.get(j);
						
						String name = term.getStr("name");
						long ID = term.getId();
						configuration.ontologyID.put(name, ID);
					}
					
				} catch (CytomineException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		} catch (CytomineException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			
		
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

				List<CytomineROI> rois = IcytomineUtil
						.Roi2DPolyToCytomineImportedROI(IcytomineUtil
								.roiListToRoi2DPolygonList(
										new ArrayList<ROI>(
												sequence.getROI2Ds() 
										) 
								) 
						);

				if(processFrame!=null){
					processFrame.println(rois.size()+" annotations will be uploaded");
				}
				
				for (CytomineROI roi : rois) {
					
					Dimension imageSize = new Dimension(
							instance.getInt("width"), instance.getInt("height"));
					int imageSizeY = instance.getInt("height");

					Point2D.Double ratio = IcytomineUtil.getScaleRatio(thumbnailSize,
							imageSize);

					String polygon = IcytomineUtil.ROItoWKT(roi, ratio, imageSizeY);

					if (!roi.terms.isEmpty()) {
						try {
							Annotation annotation = cytomine.addAnnotation(polygon, ID);
							for (int j = 0; j < roi.terms.size(); j++) {
								cytomine.addAnnotationTerm(annotation.getId(), roi.terms.get(j));
							}
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
	
	public static void uploadROI(Cytomine cytomine, ImageInstance instance, List<ROI2DPolygon> rois, double ratio, int height, Point2D.Double translation, List<Long> termID, ProcessingFrame processFrame){
		List<CytomineROI> annotations = IcytomineUtil.Roi2DPolyToCytomineImportedROI(rois);
		
		long ID = instance.getLong("id");
		
		for (int i = 0; i < annotations.size(); i++) {
			CytomineROI polygon = annotations.get(i);
			
			polygon.terms = termID;
			polygon.translate(translation.getX(), translation.getY());
			Point2D.Double ratioAsP2D = new Point2D.Double(ratio, ratio);
			try {
				String WKTPolygon = IcytomineUtil.ROItoWKT(polygon, ratioAsP2D, height);
				try{
					if (!polygon.terms.isEmpty()) {
						
						Annotation annotation = cytomine.addAnnotation(WKTPolygon, ID);
						for (int j = 0; j < polygon.terms.size(); j++) {
							cytomine.addAnnotationTerm(annotation.getId(), polygon.terms.get(j));
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
			if(processFrame!=null){
				processFrame.setActionProgress((int) ((double) (i + 1)/ annotations.size() * 100));
			}
		}
		
	}
	
	public static void uploadROI(Cytomine cytomine, ImageInstance instance, CytomineROI polygon, Point2D.Double ratio, int height, Point2D.Double translation, List<Long> termID, ProcessingFrame processFrame){
		
		long ID = instance.getLong("id");
			
		polygon.terms = termID;
		polygon.translate(translation.getX(), translation.getY());
		
		try {
			String WKTPolygon = IcytomineUtil.ROItoWKT(polygon, ratio, height);
			try{
				if (!polygon.terms.isEmpty()) {
					
					Annotation annotation = cytomine.addAnnotation(WKTPolygon, ID);
					for (int j = 0; j < polygon.terms.size(); j++) {
						cytomine.addAnnotationTerm(annotation.getId(), polygon.terms.get(j));
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
	 * @return rotated points as a List<Point2D.Double>
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

	

	
	static User currentUser=null;
	public static Cytomine switchUser(Cytomine cytomine, User job){
		String pbKey="";
		String pvKey="";
		if(currentUser==null){
			try {
				currentUser = cytomine.getCurrentUser();
			} catch (CytomineException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(job!=null){
			pvKey = job.getStr("privateKey");
			pbKey = job.getStr("publicKey");
		}else{
			pvKey = currentUser.getStr("privateKey");
			pbKey = currentUser.getStr("publicKey");
		}
		
		
		return new Cytomine(cytomine.getHost(), pbKey, pvKey);
		
	}
	
	public static Software createSectionDetectionSoftware(Cytomine cytomine) throws CytomineException{
		Software soft = cytomine.addSoftware("Icy_Section_Finder", "pyxitSuggestedTermJobService", "ValidateAnnotation", "");

		cytomine.addSoftwareParameter("imageID", "Number", soft.getId(), "0", true, 100);
		cytomine.addSoftwareParameter("maxSize", "Number", soft.getId(), "2048", true, 200);
		cytomine.addSoftwareParameter("threshold", "Number", soft.getId(), "0", true, 300);
		
		return soft;
	}
	
	/**
	 * this function is used to log informations about section and glomerules generation
	 * @param str
	 * @param f
	 */
	@SuppressWarnings("resource")
	public static void logInFile(String str, File f){
		
		try {
			PrintWriter writer = new PrintWriter(new FileOutputStream(f, true), true);
			writer.println(str);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static User generateNewUserJob(Cytomine cytomine, long idSoftware, long idProject) throws CytomineException{
		User current = null;
		User job = null;
		current = cytomine.getCurrentUser();
		job = cytomine.addUserJob( idSoftware, current.getId(), idProject, new Date(), null);
		
		return job;
	}
	
	public static void checkSoftwareCreated(Cytomine cytomine, SoftwareData data, long projectID) throws CytomineException{
		
		Software software = null;
		SoftwareCollection softwares = cytomine.getSoftwares();

		//search for software from the data information, create soft if not found or update if found
		software = searchSoftware(cytomine, softwares, data.ID, data.getName());
		
		if(software == null){
			try {
				createSoftware(cytomine, data);
			} catch (CytomineException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			updateSoftwareID(cytomine, software, data);
			//is software registeres for this project ? if no add it
			try {
				softwares = cytomine.getSoftwareByProject(projectID);
				Software software2 = searchSoftware(cytomine, softwares, data.ID, data.getName());
				
				if(software2 == null){
					cytomine.addSoftwareProject(software.getId(), projectID);
				}else{
				}
				
			} catch (CytomineException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
			

		
		
	}
	
	public static void cropSectionOfImage(Cytomine cytomine, long instanceID){
		
		JFileChooser fch = new JFileChooser();
		
		fch.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		int r = fch.showSaveDialog(null);
		
		if(r == JFileChooser.APPROVE_OPTION){
			File file = fch.getSelectedFile();
		
			try {
				ImageInstance instance = cytomine.getImageInstance(instanceID);
				
				Map<String, String> filter = new TreeMap<String, String>();
				
				filter.put("user", "" + cytomine.getCurrentUser().getLong("id"));
				filter.put("image", "" + instanceID);
				filter.put("term", "" + configuration.ontologyID.get("Section"));
				
				AnnotationCollection collection = cytomine.getAnnotations(filter);
				System.out.println(collection.size());
				for (int i = 0; i < collection.size(); i++) {
					Annotation annotation = cytomine.getAnnotation(collection.get(i).getId());
					
					
						CropInformations data = getCropAtZoom(cytomine, annotation, instance, 2, null);
						
						CytomineCrop crop = new CytomineCrop(data);
						crop.localSave(file.getAbsolutePath()+"/section_"+i);
				}
				
				
			} catch (CytomineException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		
	}
	
	public static Software searchSoftware(Cytomine cytomine, SoftwareCollection softwares, long ID, String name){
		Software soft = null;
		
		soft = getSoftwareByID(cytomine, softwares, ID);
		
		if(soft == null){
			soft = getSoftwareByName(cytomine, softwares, name);
		}
		
		return soft;
	}
	
	public static Software getSoftwareByName(Cytomine cytomine, SoftwareCollection softwares, String name){
		Software software = null;
		for (int i = 0; i < softwares.size(); i++) {
			software = softwares.get(i);
			if(software.getStr("name").equals(name)){
				return software;
			}
		}
		return null;
	}
	
	public static SoftwareParameter getSoftwareParameterByName(Cytomine cytomine, SoftwareParameterCollection softwareParameters, String name){
		SoftwareParameter parameter = null;
		for (int i = 0; i < softwareParameters.size(); i++) {
			parameter = softwareParameters.get(i);
			if(parameter.getStr("name").equals(name)){
				return parameter;
			}
		}
		return null;
	}
	
	public static Software getSoftwareByID(Cytomine cytomine, SoftwareCollection softwares, long ID){
		Software software = null;
		for (int i = 0; i < softwares.size(); i++) {
			software = softwares.get(i);
			if(software.getId() == ID){
				return software;
			}
		}
		return null;
	}
	
	public static Software createSoftware(Cytomine cytomine, SoftwareData data) throws CytomineException{
		Software soft = cytomine.addSoftware(data.getName(), "pyxitSuggestedTermJobService", "ValidateAnnotation", "");

		int idParam = 0;
		for(String paramName : data.getParameters().keySet()){
			Parameter param = data.getParameters().get(paramName);
			SoftwareParameter softParam = cytomine.addSoftwareParameter(param.getName(), param.getType().toString(), soft.getId(), param.getDefaultValue(), true, idParam);
			
			param.ID = softParam.getId();
			
			// save
			if(configuration.softwareID.get(cytomine.getHost()) == null){
				configuration.softwareID.put(cytomine.getHost(), new TreeMap<String, SoftwareData>());
			}
			configuration.softwareID.get(cytomine.getHost()).put(data.getName(), data);
			
			idParam+=100;
		}
		
		return soft;
	}
	
	public static void updateSoftwareID(Cytomine cytomine, Software soft, SoftwareData data){
		long idSoft = soft.getId();
		data.ID = idSoft;
		
		for(String paramName : data.getParameters().keySet()){
			Parameter param = data.getParameters().get(paramName);

			SoftwareParameter softParam;
			try {
				softParam = getSoftwareParameterByName(cytomine, cytomine.getSoftwareParameters(idSoft), param.getName());
				param.ID = softParam.getId();
			} catch (CytomineException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// save
			if(configuration.softwareID.get(cytomine.getHost()) == null){
				configuration.softwareID.put(cytomine.getHost(), new TreeMap<String, SoftwareData>());
			}
			configuration.softwareID.get(cytomine.getHost()).put(data.getName(), data);
		}
	}
	
	public static void createSectionSoftware(Cytomine cytomine, long projectID){
		
		SoftwareData data = new SoftwareSectionFinder();
		
		try {
			checkSoftwareCreated(cytomine, data, projectID);
		} catch (CytomineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void createGlomeruleSoftware(Cytomine cytomine, long projectID){
		
		SoftwareData data = new SoftwareGlomeruleFinder();
		
		try {
			checkSoftwareCreated(cytomine, data, projectID);
		} catch (CytomineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public class SoftwareResult{
		/**
		 * the software is valid
		 */
		public static final int VALID = 0;
		/**
		 * The software don't exist
		 */
		public static final int INEXISTENT = 1;
		/**
		 * it was added to the project
		 */
		public static final int WASNOTINPROJECT = 2;
	}
	
	/**
	 * generate Section annotation, then upload them to cytomine and return the number of generated annotations
	 * @param seq
	 * @return number of generated annotation
	 */
	public static int generateSectionsROI(Cytomine cytomine, User job, long projectID, ImageInstance instance, Sequence seq, ProcessingFrame processFrame) {
		
		updateOntology(cytomine, projectID);
		
		int nbAnnotation=0;
		long startTime = System.currentTimeMillis();
//		logInFile("image, job, nbAnnotation, time", new File("result/sectionGeneration.txt"));
		try {
			
			//switch the user for a job
			cytomine = switchUser(cytomine, job);
			
			if(processFrame!=null){
				processFrame.println("Generating section of the sequence");
			}
			
			List<CytomineROI> result = new ArrayList<CytomineROI>();
			
			int threshold = thresholdCalculation(seq,processFrame);
			
			
			Sequence thresholded = SeqRGB2SeqBINARY(seq, threshold,processFrame, true);
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
				List<CytomineROI> roisConvex = IcytomineUtil
						.Roi2DPolyToCytomineImportedROI(rois);
	
				if(processFrame!=null){
					processFrame.setGlobalProgress(100/4*3);
					processFrame.println("adding annotations to the sequence");
				}
				
				// section term id
				List<Long> terms = new ArrayList<Long>();
				terms.add(configuration.ontologyID.get("Section"));
				
				int count = 0;
				for (CytomineROI roi : roisConvex) {
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
			
			try {
	
				for (CytomineROI roi : result) {
					seq.addROI(roi);
					nbAnnotation++;
				}
	
				IcytomineUtil.uploadROI(cytomine, instance, seq, processFrame);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		
			long time = System.currentTimeMillis()-startTime;
			
			//switch the user back
			cytomine = switchUser(cytomine, null);
			
			cytomine.addJobParameter(job.getLong("job"), configuration.softwareID.get(cytomine.getHost()).get(new SoftwareSectionFinder().getName()).getParameter("imageID").ID, ""+instance.getId());
			cytomine.addJobParameter(job.getLong("job"), configuration.softwareID.get(cytomine.getHost()).get(new SoftwareSectionFinder().getName()).getParameter("maxSize").ID, ""+configuration.thumbnailMaxSize);
			cytomine.addJobParameter(job.getLong("job"), configuration.softwareID.get(cytomine.getHost()).get(new SoftwareSectionFinder().getName()).getParameter("threshold").ID, ""+threshold);
			
			logInFile(instance.getId()+","+job.getLong("id")+","+nbAnnotation+","+time, new File("result/sectionGeneration.txt"));
			
			cytomine.changeStatus(job.getLong("job"), Cytomine.JobStatus.SUCCESS, 100);
			
		} catch (CytomineException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return nbAnnotation;
		
	}
	
	public static int thresholdCalculation(Sequence seq, ProcessingFrame processFrame){
		if(processFrame!=null){
			processFrame.println("Generating histogramme for the image");
		}
		int[] histo = CustomThreshold.getHistogram(seq.getFirstImage());

		if(processFrame!=null){
			processFrame.println("thresholding");
			processFrame.setActionProgress(50);
		}

		int threshold = CustomThreshold.generateSeuil(histo, seq.getWidth(), seq.getHeight());
		
		return threshold;
	}

	public static Sequence SeqRGB2SeqBINARY(Sequence seq, int threshold,ProcessingFrame processFrame,boolean inversed) {

		Sequence thresholded = /*Thresholder.threshold(seq, 0, new double[]{seuil}, false);/**/ CustomThreshold.threshold(seq, threshold, inversed);/**/

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
	public static Point2D.Double getScaleRatio(Dimension thumbSize, Dimension imageSize) {
		Point2D.Double ratio;

		ratio = new Point2D.Double.Double(imageSize.getWidth() / thumbSize.getWidth(),
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
	public static List<Point2D> WKTtoPoint2D(String polygon, Point2D ratio,
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
	public static List<CytomineROI> Roi2DPolyToCytomineImportedROI(
			List<ROI2DPolygon> rois) {
		List<CytomineROI> newList = new ArrayList<CytomineROI>();

		for (int i = 0; i < rois.size(); i++) {
			try {
				newList.add((CytomineROI) rois.get(i));
			} catch (Exception e) {
				newList.add(new CytomineROI(rois.get(i).getPoints(),
						null));
			}
		}

		return newList;

	}
	
	public static CytomineROI ROI2D2CytomineROI(ROI2D roi){
		try {
			ROI2DPolygon r = (ROI2DPolygon) roi;
			return ROI2DPoly2CytomineROI(r);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static CytomineROI ROI2DPoly2CytomineROI(ROI2DPolygon roi){
		try {
			CytomineROI r = (CytomineROI) roi;
			return r;
		} catch (Exception e) {
			CytomineROI r = new CytomineROI(roi.getPoints(), null);
			return r;
		}
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

	public static void generateGlomeruleROI(Cytomine cytomine,Sequence seq, ProcessingFrame processFrame, int threshold, int minSize, int maxSize, int cvalue, double valratioAxis, double vminlong) {
		
		Sequence thresholded = Thresholder.threshold(seq, 0, new double[]{threshold}, false);//CustomThreshold.threshold(seq, seuil, true);
		
		if(processFrame!=null){
			processFrame.println("Detecting glomerules");
		}
		GlomeruleDetector.ellipses_detect(seq, thresholded, minSize, maxSize, cvalue, valratioAxis, vminlong);
		
		if(processFrame!=null){
			processFrame.println("detection done");
		}
		
	}
	
	public Software createGlomeruleGenerationSoftware(Cytomine cytomine) throws CytomineException{
		Software soft = cytomine.addSoftware("GlomeruleGeneration", "GlomeruleGenerationService", "Annotations", "");
		
		cytomine.addSoftwareParameter("imageID", "Number", soft.getId(), "0", true, 100);
		cytomine.addSoftwareParameter("zoom", "Number", soft.getId(), "0", true, 200);
		
		
		return soft;
	}
	
	public static Software createGlomeruleDetectionSoftware(Cytomine cytomine) throws CytomineException{
		Software soft = cytomine.addSoftware("Icy_Glomeruli_Finder", "pyxitSuggestedTermJobService", "ValidateAnnotation", "");

		cytomine.addSoftwareParameter("imageID", "Number", soft.getId(), "0", true, 100);
		cytomine.addSoftwareParameter("zoom", "Number", soft.getId(), "2", true, 200);
		
		//minSize, maxSize, cvalue, valratioAxis, vminlong 
		cytomine.addSoftwareParameter("minSize", "Number", soft.getId(), "200", true, 300);
		cytomine.addSoftwareParameter("maxSize", "Number", soft.getId(), "231000", true, 400);
		cytomine.addSoftwareParameter("cvalue", "Number", soft.getId(), "1", true, 500);
		cytomine.addSoftwareParameter("valratioAxis", "Number", soft.getId(), "2", true, 600);
		cytomine.addSoftwareParameter("vminlong", "Number", soft.getId(), "10", true, 700);
		
		return soft;
	}

	/**
	 * this function generate a rectangle to crop the annotation
	 * @param cytomine
	 * @param annotation
	 * @param instance
	 * @param zoom
	 * @param processFrame
	 * @return
	 * @throws CytomineException
	 */
	public static CropInformations getCropAtZoom(Cytomine cytomine, Annotation annotation, ImageInstance instance, int zoom, ProcessingFrame processFrame) throws CytomineException{
		
			
			//get the String representation of the polygon
			String poly = annotation.getStr("location");
			System.out.println(annotation.getId());
			System.out.println("polygon : " + poly);
			
			//generate a list of points from the String
			List<Point2D> points = WKTtoPoint2D(poly, new Point2D.Double(1, 1), instance.getInt("height"));
			
			Rectangle rect = IcytomineUtil.SectionROI2Rectangle(points);

			return getCropAtZoom(cytomine, rect, instance,zoom, processFrame);
	}
	
	
	/**
	 * this function allow to get data to work on an annotation thanks to the dynamic viewer that will only download needed tiles
	 * informations about the scaled and the original annotation are stored inside the returned object to allow to save or load the annotation
	 * @param cytomine
	 * @param annotation
	 * @param instance
	 * @param zoom
	 * @param processFrame
	 * @return
	 * @throws CytomineException
	 */
	public static CropInformations getCropAtZoom(Cytomine cytomine, Rectangle rect, ImageInstance instance, int zoom, ProcessingFrame processFrame) throws CytomineException{
		
		//Initialize cytomine reader, it will be used to download tiles
		CytomineReader preview = new CytomineReader(cytomine, instance, new Dimension(10,10),false);
		
		// variables

		double ratio = Math.pow(2, -zoom);

		if(processFrame!=null){
			processFrame.println("Generating scaled annotation");
			processFrame.setActionProgress(10);
		}
		
		//defining position and size of the scaled annotation bounding box by multiplying the original by the ratio between the zoom and the source image
		Point position = new Point( (int)(rect.x * ratio),	(int)(rect.y * ratio) );
		Dimension size = new Dimension( (int) Math.max(1, (rect.width * ratio)), (int) Math.max(1, (rect.height * ratio)) );
		Rectangle scaled = new Rectangle(position, size);
		
		//generating tiles object with the col and row and an empty tile waiting to be downloaded
		List<Tile> tiles = preview.getCrop(position, size, zoom);
		
		BufferedImage img = new BufferedImage(size.width,size.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graph = img.createGraphics();
		Sequence sequence = new Sequence();

		if(processFrame!=null){
			processFrame.println("downloading tiles");
			processFrame.setActionProgress(20);
		}
		
		BufferedImage image;
		//downloading tiles
		for (int j = 0; j < tiles.size(); j++) {
			Tile tile = tiles.get(j);
			image = cytomine.downloadPictureAsBufferedImage(tile.getUrl(),"");
			tile.image = image;
			
			if(processFrame!=null){
				processFrame.setActionProgress(20+(60*(int)((double)j/tiles.size())));
			}
		}
		if(processFrame!=null){
			processFrame.println("Drawing image");
			processFrame.setActionProgress(80);
		}
		//drawing tiles at the correct position on the bufferedImage
		for (int j = 0; j < tiles.size(); j++) {
			Tile tile = tiles.get(j);
			graph.drawImage(tile.image, (int)(tile.getC()*256-position.getX()), (int)(tile.getR()*256-position.getY()), null);
		}

		//set the bufferedImage to be the image shown in the sequence
		sequence.setImage(0, 0, img);
		
		//generating object to store all results
		CropInformations data = new CropInformations(instance, rect, scaled, ratio, tiles, sequence);
			
		if(processFrame!=null){
			processFrame.setActionProgress(100);
		}
		
		return data;
}
	
	public static int generateGlomerule(Cytomine cytomine, User jobSection, User jobGlomerule, final ImageInstance instance, int zoom, final ProcessingFrame processFrame){
		
		long projectID = instance.getLong("project");
		updateOntology(cytomine, projectID);
		
		final int minSize = configuration.minSize; 
		final int maxSize = configuration.maxSize;
		final int cValue = configuration.cValue;
		final int valRatioAxis = configuration.valRatioAxis;
		final int vMinLong = configuration.vMinLong;		
		
		try {
			//saving parameters to cytomine
			cytomine.addJobParameter(jobGlomerule.getLong("job"), configuration.softwareID.get(cytomine.getHost()).get(new SoftwareGlomeruleFinder().getName()).getParameter("imageID").ID, ""+instance.getId());
			cytomine.addJobParameter(jobGlomerule.getLong("job"), configuration.softwareID.get(cytomine.getHost()).get(new SoftwareGlomeruleFinder().getName()).getParameter("zoom").ID, ""+zoom);
			
			cytomine.addJobParameter(jobGlomerule.getLong("job"), configuration.softwareID.get(cytomine.getHost()).get(new SoftwareGlomeruleFinder().getName()).getParameter("minSize").ID, ""+minSize);
			cytomine.addJobParameter(jobGlomerule.getLong("job"), configuration.softwareID.get(cytomine.getHost()).get(new SoftwareGlomeruleFinder().getName()).getParameter("maxSize").ID, ""+maxSize);
			cytomine.addJobParameter(jobGlomerule.getLong("job"), configuration.softwareID.get(cytomine.getHost()).get(new SoftwareGlomeruleFinder().getName()).getParameter("cvalue").ID, ""+cValue);
			cytomine.addJobParameter(jobGlomerule.getLong("job"), configuration.softwareID.get(cytomine.getHost()).get(new SoftwareGlomeruleFinder().getName()).getParameter("valratioAxis").ID, ""+valRatioAxis);
			cytomine.addJobParameter(jobGlomerule.getLong("job"), configuration.softwareID.get(cytomine.getHost()).get(new SoftwareGlomeruleFinder().getName()).getParameter("vminlong").ID, ""+vMinLong);
			
			cytomine.changeStatus(jobGlomerule.getLong("job"), Cytomine.JobStatus.RUNNING, 0);
		} catch (CytomineException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		//compteurs
		long startExecutionTime = System.currentTimeMillis();
		int nbAnnotations = 0;
		int nbSection = 0;
		
		Map<String, String> filter = new TreeMap<String, String>();
		try {


			//switch the user for a job
			cytomine = switchUser(cytomine, jobSection);
			
			filter.put("user", "" + cytomine.getCurrentUser().getLong("id"));
			filter.put("image", "" + instance.getLong("id"));
			filter.put("term", "" + configuration.ontologyID.get("Section"));
			
			AnnotationCollection collection = cytomine.getAnnotations(filter);
			
			//switch back
			cytomine = switchUser(cytomine, null);
			
			
			if(processFrame!=null){
				processFrame.println("Generating glomerule for sections annotations");
			}
			
			for (int i = 0; i < /*1/**/collection.size()/**/; i++) {
				
				nbSection++;
				Annotation annotation = cytomine.getAnnotation(collection.get(i).getLong("id"));
				if(processFrame!=null){
					processFrame.println("generating bounding box");
				}
				
				CropInformations data = getCropAtZoom(cytomine, annotation, instance, zoom, processFrame);
				Sequence chan0 = SequenceUtil.extractChannel(data.getSequence(), 0);

				// generate histogramme and seuil for threshold
				int[] histogramme = CustomThreshold.getHistogram(chan0.getFirstImage());
				final int threshold = CustomThreshold.generateSeuil(histogramme, chan0.getWidth(), chan0.getHeight());
				
				// generate sequence with tiles assembled in a BufferedImage
				if(processFrame!=null){
					processFrame.println("download done");
				}

				// pool variable for multi thread calcul
				Stack<Runnable> runnablePool = new Stack<Runnable>();
				
				if(processFrame!=null){
					processFrame.println("processing tiles of sections");
				}
				for (int j = 0; j < data.getTiles().size(); j++) {
					final Tile tile = data.getTiles().get(j);
				
//						Tuiles annotations
//						List<Point2D> pts = new ArrayList<Point2D>();
//						pts.add(new Point2D.Double(		tile.getC()-position.getX(), tile.getR()-position.getY()));
//						pts.add(new Point2D.Double(		tile.getC()-position.getX()+256, tile.getR()-position.getY()	));
//						pts.add(new Point2D.Double(		tile.getC()-position.getX()+256, tile.getR()-position.getY()+256	));
//						pts.add(new Point2D.Double(		tile.getC()-position.getX(), tile.getR()-position.getY()+256	));
//						
//						ROI2DPolygon tuile = new ROI2DPolygon(pts);
//						tuile.setColor(Color.GREEN);
//						sequence.addROI(tuile);
				
					if (tile.image != null) {
						
						final Cytomine tempCytomine = cytomine;
						final double scaledPosX = data.getScaledBoundingBox().getX();
						final double scaledPosY = data.getScaledBoundingBox().getY();
						final Sequence tempSequence = data.getSequence();
						
						Runnable runnable = new Runnable() {

							@Override
							public void run() {
								
								// start glomeruli detection
								Sequence seq = new Sequence(tile.image);
								
								seq = SequenceUtil.extractChannel(seq, 0);
								IcytomineUtil.generateGlomeruleROI(tempCytomine, seq, processFrame, threshold, minSize, maxSize, cValue, valRatioAxis, vMinLong);

								List<ROI2D> rois = seq.getROI2Ds();
								
								for (int k = 0; k < rois.size(); k++) {
									ROI2D roi = rois.get(k);
									
									double posX = tile.getC()*256 - scaledPosX + roi.getPosition2D().getX();
									double posY = tile.getR()*256 - scaledPosY + roi.getPosition2D().getY();
									
									roi.setPosition2D( new Point2D.Double( posX, posY ) );
									tempSequence.addROI(roi);

								}
							}
						};

						runnablePool.push(runnable);
					}

				}
				
				
				startRunnablePool(runnablePool);
				
				//uploading generated roi
				List<ROI2DPolygon> polygonList = new ArrayList<ROI2DPolygon>();
				
				List<ROI2D> rois = data.getSequence().getROI2Ds();
				
				for (int j = 0; j < rois.size(); j++) {
					try{
						ROI2DPolygon polygon = (ROI2DPolygon) rois.get(j);
						
						polygonList.add(polygon);
						nbAnnotations++;
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				
//				//show on icy
//				Icy.getMainInterface().addSequence(data.getSequence());
				
				if(processFrame!=null){
					processFrame.println("Uploading roi");
				}
				List<Long> terms = new ArrayList<Long>();
				terms.add(configuration.ontologyID.get("Glomerule"));
				
				//switch the user for a job
				cytomine = switchUser(cytomine, jobGlomerule);
				IcytomineUtil.uploadROI(cytomine, instance,polygonList, 1/data.getRatio(), (int) (instance.getInt("height")), new Point2D.Double(data.getScaledBoundingBox().getX(), data.getScaledBoundingBox().getY() ), terms, processFrame);
				
				//switch back
				cytomine = switchUser(cytomine, null);
				
				if(processFrame!=null){
					processFrame.println("Uploading done");
				}
				
				if(processFrame!=null){
					processFrame.println("processing done");
				}

				System.gc();
			}
				
			} catch (CytomineException e) {
				e.printStackTrace();
			}

		long endExecutionTime = System.currentTimeMillis();
		
		long time = endExecutionTime-startExecutionTime;
		
		try {

			logInFile(instance.getId()+","+jobSection.getLong("id")+","+nbSection+","+jobGlomerule.getLong("id")+","+nbAnnotations+","+time, new File("result/GlomeruleGeneration.txt"));
			
			cytomine.changeStatus(jobGlomerule.getLong("job"), Cytomine.JobStatus.SUCCESS, 100);
		} catch (CytomineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.gc();
		
		return nbAnnotations;
		
		
		
	}
	
	private static void startRunnablePool(Stack<Runnable> runnablePool) {
		
		ThreadForRunnablePool threadPool[] = new ThreadForRunnablePool[8];

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
		}
		
	}

	public static Rectangle SectionROI2Rectangle(List<Point2D> points){
		Rectangle boundingBox = new Rectangle();
		
		int minX = 0, maxX = 0, minY = 0, maxY = 0;

			// create bounding box of the annotation
			minX = (int) points.get(0).getX();
			minY = (int) points.get(0).getY();
			maxX = (int) points.get(0).getX();
			maxY = (int) points.get(0).getY();

			for (int j = 0; j < points.size(); j++) {
				maxX = (int) Math.max(maxX, points.get(j).getX());
				maxY = (int) Math.max(maxY, points.get(j).getY());

				minX = (int) Math.min(minX, points.get(j).getX());
				minY = (int) Math.min(minY, points.get(j).getY());
				
			}

			// generated bounding box
			boundingBox = new Rectangle(minX, minY, maxX - minX,
					maxY - minY);
		
		return boundingBox;
	}

	//sleep function that make the program wait for X ms
	public static void sleep(long ms){ long d = System.currentTimeMillis(); while(d+ms>System.currentTimeMillis()); }
	
}
