package plugins.faubin.cytomine.utils;

import icy.main.Icy;
import icy.roi.ROI2D;
import icy.sequence.Sequence;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.List;

import javax.swing.JFileChooser;

import plugins.adufour.vars.gui.swing.FileChooser;
import plugins.faubin.cytomine.module.main.mvc.frame.ProcessingFrame;
import plugins.kernel.roi.roi2d.ROI2DPolygon;
import be.cytomine.client.Cytomine;
import be.cytomine.client.models.ImageInstance;

public class CytomineCrop {
	
	private Rectangle scaledBoundingBox;
	private Rectangle originalBoundingBox;
	private double ratio;
	private Sequence sequence;
	private ImageInstance instance;
	
	public CytomineCrop(CropInformations data) {
		super();
		this.instance = data.getInstance();
		this.sequence = data.getSequence();
		this.ratio = data.getRatio();
		this.originalBoundingBox = data.getOriginalBoundingBox();
		this.scaledBoundingBox = data.getScaledBoundingBox();
		
		sequence.setName(instance.getId()+" (crop)");
	}

	public void addToDesktopPane(){
		Icy.getMainInterface().addSequence(sequence);
	}
	
	public void localSave(){
		JFileChooser fc = new JFileChooser();
		int result = fc.showSaveDialog(null);
		
		if(result == JFileChooser.APPROVE_OPTION){
			sequence.saveXMLData();
		}
	}
	
	public void upload(Cytomine cytomine){
			
	}
	
}	
