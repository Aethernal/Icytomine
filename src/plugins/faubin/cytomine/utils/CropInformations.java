package plugins.faubin.cytomine.utils;

import icy.sequence.Sequence;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.List;

import plugins.faubin.cytomine.module.tileViewer.Tile;

public class CropInformations {
	private Rectangle originalBoundingBox;
	private Rectangle scaledBoundingBox;
	private double ratio;
	private List<Point2D> points;
	private List<Tile> tiles;
	private Sequence sequence;
	
	public CropInformations(Rectangle originalBoundingBox, Rectangle scaledBoundingBox, double ratio, List<Point2D> points, List<Tile> tiles, Sequence sequence) {
		super();
		this.originalBoundingBox = originalBoundingBox;
		this.scaledBoundingBox = scaledBoundingBox;
		this.ratio = ratio;
		this.points = points;
		this.tiles = tiles;
		this.sequence = sequence;
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

	public List<Point2D> getPoints() {
		return points;
	}

	public List<Tile> getTiles() {
		return tiles;
	}

	public Sequence getSequence() {
		return sequence;
	}
	
	
}