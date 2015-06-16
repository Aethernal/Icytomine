package plugins.faubin.cytomine.module.tileViewer.utils;



import java.awt.image.BufferedImage;

public class Tile {
	
	public String url;
	public int c;
	public int r;
	public BufferedImage image;
	
	public Tile(String url, int c, int r) {
		super();
		this.url = url;
		this.c = c;
		this.r = r;
	}
	
	public int getC() {
		return c;
	}
	
	public int getR() {
		return r;
	}
	
	public String getUrl() {
		return url;
	}
	
}
