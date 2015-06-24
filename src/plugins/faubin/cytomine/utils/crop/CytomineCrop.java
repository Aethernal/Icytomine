package plugins.faubin.cytomine.utils.crop;

import icy.file.Loader;
import icy.file.Saver;
import icy.main.Icy;
import icy.roi.ROI2D;
import icy.sequence.Sequence;
import icy.sequence.SequenceUtil;
import icy.util.XMLUtil;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

import org.w3c.dom.Node;

import plugins.adufour.vars.gui.swing.FileChooser;
import plugins.faubin.cytomine.module.main.mvc.frame.ProcessingFrame;
import plugins.faubin.cytomine.utils.CytomineROI;
import plugins.faubin.cytomine.utils.IcytomineUtil;
import plugins.kernel.roi.roi2d.ROI2DPolygon;
import be.cytomine.client.Cytomine;
import be.cytomine.client.CytomineException;
import be.cytomine.client.models.Annotation;
import be.cytomine.client.models.ImageInstance;

public class CytomineCrop {
	
	private Rectangle scaledBoundingBox;
	private Rectangle originalBoundingBox;
	private double ratio;
	private Sequence sequence;
	private long instanceID;
	
	public CytomineCrop(CropInformations data) {
		this(data.getSequence(), data.getInstance().getId(), data.getOriginalBoundingBox(), data.getScaledBoundingBox(), data.getRatio());
	}
	
	public CytomineCrop(Sequence sequence, long instanceID, Rectangle original, Rectangle scaled, double ratio) {
		super();
		this.instanceID = instanceID;
		this.sequence = sequence;
		this.ratio = ratio;
		this.originalBoundingBox = original;
		this.scaledBoundingBox = scaled;
		
		sequence.setName(instanceID+" (crop) "+originalBoundingBox);
	}

	public void addToDesktopPane(){
		Icy.getMainInterface().addSequence(sequence);
	}
	
	public void upload(Cytomine cytomine){
		List<Long> terms = new ArrayList<Long>();
		
		for (int i = 0; i < sequence.getROI2Ds().size(); i++) {
			ROI2D roi = sequence.getROI2Ds().get(i);

			// convert to ROI for cytomine if possible
			CytomineROI newROI;
			try {
				newROI = (CytomineROI) roi;
				terms = newROI.terms;
			} catch (Exception e) {
				Annotation annotation = new Annotation();
				annotation.set("term", terms.toString());

				newROI = new CytomineROI(((ROI2DPolygon) roi).getPoints(), annotation);
				newROI.setColor(Color.black);
				sequence.getROI2Ds().set(i, newROI);

			}
			
			Point2D.Double translation = new Point2D.Double( (scaledBoundingBox.getX()) , (scaledBoundingBox.getY()));
			
			Point2D.Double rate = new Point2D.Double(1/ratio, 1/ratio);
			
			ImageInstance instance;
			try {
				instance = cytomine.getImageInstance(instanceID);
				IcytomineUtil.uploadROI(cytomine, instance, newROI, rate, instance.getInt("height"), translation, terms, null);
			} catch (CytomineException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public void replace(Cytomine cytomine){
		/*
		 * get contained annotations
		 * remove them
		 * add the new one's
		 */
	}
	
	public void load(Cytomine cytomine){
		
	}
	
	public void localSave(){
	
		addCytomineNodes();
		
		saveToFile();
		sequence.saveXMLData();
	}
	
	public void localSave(String path){
		
		File file = new File(path);
		
		addCytomineNodes();
		
		Saver.save(sequence, file);
		sequence.saveXMLData();
		
	}
	
	public void addCytomineNodes(){
		Node cytomine = sequence.getNode("cytomine");
		Node original = XMLUtil.setElement(cytomine, "originalBounding");
		Node scaled = XMLUtil.setElement(cytomine, "scaledBounding");
		
		XMLUtil.setElementDoubleValue(cytomine, "ratio", ratio);
		XMLUtil.setElementLongValue(cytomine, "instance", instanceID);
		
		XMLUtil.setElementIntValue(original, "x", originalBoundingBox.x);
		XMLUtil.setElementIntValue(original, "y", originalBoundingBox.y);
		XMLUtil.setElementIntValue(original, "width", originalBoundingBox.width);
		XMLUtil.setElementIntValue(original, "height", originalBoundingBox.height);
		
		XMLUtil.setElementIntValue(scaled, "x", scaledBoundingBox.x);
		XMLUtil.setElementIntValue(scaled, "y", scaledBoundingBox.y);
		XMLUtil.setElementIntValue(scaled, "width", scaledBoundingBox.width);
		XMLUtil.setElementIntValue(scaled, "height", scaledBoundingBox.height);
	}
	
	public void saveToFile(){
		JFileChooser ch = new JFileChooser();
		int r = ch.showSaveDialog(null);
		
		if(r == JFileChooser.APPROVE_OPTION){
			Saver.save(sequence, ch.getSelectedFile());
		}
		
		sequence.saveXMLData();
	}
	
	public static CytomineCrop localLoad(Sequence sequence){
		/* search for cytomine node in the xml ( root -> cytomine )
		 * if found read variables node	( cytomine -> originalBoundingBox, scaledBoundingBox, ratio)
		 * then create CytomineCrop object from vars
		 */
		
		Node cytomine = sequence.isNodeExisting("cytomine");
		if(cytomine != null){
			
			Node original = XMLUtil.getElement(cytomine, "originalBounding");
			Node scaled = XMLUtil.getElement(cytomine, "scaledBounding");
			
			int x,y,width,height;
			
			x = XMLUtil.getElementIntValue(original, "x",0);
			y = XMLUtil.getElementIntValue(original, "y",0);
			width = XMLUtil.getElementIntValue(original, "width",0);
			height = XMLUtil.getElementIntValue(original, "height",0);
			
			Rectangle originalRect = new Rectangle(x, y, width, height);
			
			x = XMLUtil.getElementIntValue(scaled, "x",0);
			y = XMLUtil.getElementIntValue(scaled, "y",0);
			width = XMLUtil.getElementIntValue(scaled, "width",0);
			height = XMLUtil.getElementIntValue(scaled, "height",0);
			
			Rectangle scaledRect = new Rectangle(x, y, width, height);
			
			double ratio = XMLUtil.getElementDoubleValue(cytomine, "ratio", 1);
			long instanceID = XMLUtil.getElementLongValue(cytomine, "instance", 1);
			
			return new CytomineCrop(sequence, instanceID, originalRect, scaledRect, ratio);
		}
		
		return null;
	}

	public Sequence getSequence() {
		return sequence;
	}
	
	
}	
