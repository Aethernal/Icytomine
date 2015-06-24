package plugins.faubin.cytomine.utils;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import plugins.faubin.cytomine.Icytomine;
import plugins.faubin.cytomine.module.tileViewer.roiconfiguration.RoiConfigurationPanel;
import plugins.kernel.roi.roi2d.ROI2DPolygon;
import be.cytomine.client.Cytomine;
import be.cytomine.client.CytomineException;
import be.cytomine.client.collections.OntologyCollection;
import be.cytomine.client.collections.TermCollection;
import be.cytomine.client.models.Annotation;
import be.cytomine.client.models.ImageInstance;
import be.cytomine.client.models.Ontology;
import be.cytomine.client.models.Project;

public class CytomineROI extends ROI2DPolygon {
	public List<Long> terms;
	private RoiConfigurationPanel config;

	public RoiConfigurationPanel getConfig() {
		return config;
	}

	public CytomineROI() {
		super();
		terms = new ArrayList<Long>();
		setColor(Color.BLACK);
	}

	public CytomineROI(List<Point2D> points) {
		super(points);
		terms = new ArrayList<Long>();
		setColor(Color.BLACK);
	}

	public CytomineROI(List<Point2D> points, ImageInstance image,
			Cytomine cytomine) {
		super(points);
		terms = new ArrayList<Long>();
		initialise(cytomine, image);
		setColor(Color.BLACK);
	}

	public CytomineROI(List<Point2D> points, Annotation annotation) {
		super(points);
		if (annotation != null) {
			try {
				String annotationTerms = annotation.getStr("term");

				String[] annotationTerms2 = annotationTerms.split(",|"
						+ Pattern.quote("[") + "|" + Pattern.quote("]"));
				terms = new ArrayList<Long>();

				for (int i = 1; i < annotationTerms2.length; i++) {
					terms.add(Long.parseLong(annotationTerms2[i]));
				}
				setColor(Color.BLACK);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void initialise(Cytomine cytomine, ImageInstance image) {

		long projectID = image.getLong("project");
		try {
			Project project = cytomine.getProject(projectID);
			long ontoID = project.getLong("ontology");
			TermCollection terms = cytomine.getTermsByOntology(ontoID);

			config = new RoiConfigurationPanel(this, terms);
		} catch (CytomineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static CytomineROI build(List<Point2D> points,
			Annotation annotation, Cytomine cytomine) {

		String annotationTerms = annotation.getStr("term");

		String[] annotationType = annotationTerms.split(",|"
				+ Pattern.quote("[") + "|" + Pattern.quote("]"));

		// if there is multiple type only the first is used to set the color
		for (int i = 1; i < annotationType.length;) {
			long ID = Long.parseLong(annotationType[i]);

			CytomineROI roi = new CytomineROI(points,
					annotation);
			String color;
			try {
				color = cytomine.getTerm(ID).getStr("color");
				roi.setColor(IcytomineUtil.hexToColor(color));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return roi;

		}

		return new CytomineROI(points, annotation);
	}

}
