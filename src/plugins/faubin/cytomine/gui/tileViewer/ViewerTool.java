package plugins.faubin.cytomine.gui.tileViewer;

import java.awt.geom.Point2D;
import java.util.List;

import plugins.kernel.roi.roi2d.ROI2DPolygon;

public class ViewerTool {

	public static int findTileGroup(WholeSlide img ,int zoom, int col, int row){
		int depth = img.depth;
		int num_tile = 0;
		for (int i = depth-zoom; i < depth; i++) {
			num_tile += col + row * img.levels[i].get("level_num_tiles");
		}
		
		num_tile += col + row * img.levels[zoom].get("x_tiles");
		
		int tileGroup = num_tile / img.tile_size;
		
		return tileGroup;
	}
	
	public static void scaleROIPolygon(ROI2DPolygon polygon, double scale){
		List<Point2D> points = polygon.getPoints();
		Point2D polygonPos = polygon.getPosition2D();
		
		polygon.setPosition2D(new Point2D.Double(0,0));
		
		for (int i = 0; i < points.size(); i++) {
			Point2D p = points.get(i);
			//scale
			p.setLocation(p.getX()*scale, p.getY()*scale);
		}
		
		polygon.setPoints(points);
		
		polygon.setPosition2D(polygonPos);
		
	}

}
