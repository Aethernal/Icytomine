package plugins.faubin.cytomine.module.tileViewer;

import java.util.Map;
import java.util.TreeMap;

import be.cytomine.client.Cytomine;
import be.cytomine.client.CytomineException;
import be.cytomine.client.models.AbstractImage;

public class WholeSlide {
	AbstractImage image;
	int depth;
	int width;
	int height;
	String server_urls;
	int tile_size;
	int num_tiles;
	Map<String, Integer> levels[];
	String mime;
	
	@SuppressWarnings("unchecked")
	public WholeSlide(Cytomine cytomine, long imageID, int tile_size) throws CytomineException {
		this.tile_size = tile_size;
		
		if(this.tile_size<=0){ // default value
			this.tile_size = 256;
		}
		
		this.image = cytomine.getAbstractImage(imageID);
		this.depth = image.getInt("depth");
		this.width = image.getInt("width");
		this.height = image.getInt("height");
		this.server_urls = cytomine.getImageServersOfAbstractImage(imageID);
		this.num_tiles = 0;
		this.levels = new Map[depth];
		this.mime = image.getStr("mime");
		
		for (int i = 0; i < levels.length; i++) {
			int level_width = (int) (this.width / Math.pow(2, i));
			int level_height = (int) (this.height / Math.pow(2, i));
			
			int x_tiles = (int) Math.ceil(	level_width / this.tile_size	);
			int y_tiles = (int) Math.ceil(	level_height / this.tile_size	);
			int level_num_tiles = x_tiles * y_tiles;
			
			levels[i] = new TreeMap<String,Integer>();
			levels[i].put("zoom", i);
			levels[i].put("level_width", level_width);
			levels[i].put("level_height", level_height);
			levels[i].put("x_tiles", x_tiles);
			levels[i].put("y_tiles", y_tiles);
			levels[i].put("level_num_tiles", level_num_tiles);
			
			this.num_tiles += level_num_tiles;
		}
		
	}
	
	public Map<String, Integer>[] getLevels() {
		return levels;
	}
	
	public int getDepth() {
		return depth;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getTile_size() {
		return tile_size;
	}
	
	public String getMime() {
		return mime;
	}
	
}
