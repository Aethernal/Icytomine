package plugins.faubin.cytomine.gui.roi.roi2dpolygon;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import plugins.faubin.cytomine.IcytomineUtil;
import plugins.kernel.roi.roi2d.ROI2DPolygon;
import be.cytomine.client.Cytomine;
import be.cytomine.client.models.Annotation;

public class CytomineImportedROI extends ROI2DPolygon {
	private Annotation annotation;
	public List<Long> terms;

	public CytomineImportedROI() {
		super();
	}

	public CytomineImportedROI(List<Point2D> points, Annotation annotation) {
		super(points);
		this.annotation = annotation;
		if (annotation != null) {
			try {
				String annotationTerms = annotation.getStr("term");

				String[] annotationTerms2 = annotationTerms.split(",|"
						+ Pattern.quote("[") + "|" + Pattern.quote("]"));
				terms = new ArrayList<Long>();

				for (int i = 1; i < annotationTerms2.length; i++) {
					terms.add( Long.parseLong(annotationTerms2[i]) );
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static CytomineImportedROI build(List<Point2D> points,
			Annotation annotation, Cytomine cytomine) {

		String annotationTerms = annotation.getStr("term");

		String[] annotationType = annotationTerms.split(",|"
				+ Pattern.quote("[") + "|" + Pattern.quote("]"));

		//if there is multiple type only the first is used to set the color
		for (int i = 1; i < annotationType.length;) {
			long ID = Long.parseLong(annotationType[i]);

			CytomineImportedROI roi = new CytomineImportedROI(points,
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

		return new CytomineImportedROI(points, annotation);
	}

	public Annotation getAnnotation() {
		return annotation;
	}

}
