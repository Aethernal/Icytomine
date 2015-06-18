package plugins.faubin.cytomine.utils;

import icy.sequence.Sequence;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.List;

import be.cytomine.client.models.ImageInstance;
import plugins.faubin.cytomine.module.tileViewer.utils.Tile;

public class CropInformations {
	private Rectangle originalBoundingBox;
	private Rectangle scaledBoundingBox;
	private double ratio;
	private List<Tile> tiles;
	private Sequence sequence;
	private ImageInstance instance;
	
	public CropInformations( ImageInstance instance, Rectangle originalBoundingBox, Rectangle scaledBoundingBox, double ratio, List<Tile> tiles, Sequence sequence) {
		super();
		this.instance = instance;
		this.originalBoundingBox = originalBoundingBox;
		this.scaledBoundingBox = scaledBoundingBox;
		this.ratio = ratio;
		this.tiles = tiles;
		this.sequence = sequence;
	}
	
	public ImageInstance getInstance() {
		return instance;
	}
	
	public Rectangle getOriginalBoundingBox() {
		return originalBoundingBox;
	}

	public Rectangle getScaledBoundingBox() {
		return scaledBoundingBox;
	}

	public double getRatio() {
		return ratio;
	}

	public List<Tile> getTiles() {
		return tiles;
	}

	public Sequence getSequence() {
		return sequence;
	}
	
	
}