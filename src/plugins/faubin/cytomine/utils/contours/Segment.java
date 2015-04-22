package plugins.faubin.cytomine.utils.contours;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;

import javax.vecmath.Point3d;

import plugins.kernel.roi.roi2d.ROI2DArea;
import plugins.kernel.roi.roi2d.ROI2DPolygon;

import com.vividsolutions.jts.geom.TopologyException;

public class Segment implements Iterable<Point3d>
{

    final ArrayList<Point3d> points;
    
    private Segment(Point3d head, Point3d tail)
    {
        points = new ArrayList<Point3d>(2);
        points.add(head);
        points.add(tail);
    }
    
    final Point3d getHead()
    {
        return points.get(0);
    }
    
    final Point3d getTail()
    {
        return points.get(points.size() - 1);
    }
    
    final void addHead(Point3d p)
    {
        points.add(0, p);
    }
    
    final void addHead(Segment s)
    {
        for (int i = 0; i < s.points.size(); i++)
            points.add(i, s.points.get(i));
    }
    
    final void addTail(Point3d p)
    {
        points.add(p);
    }
    
    public Iterator<Point3d> iterator()
    {
        return points.iterator();
    }
    
	public ArrayList<Point3d> getPoints() {
		return points;
	}
	
	public static ROI2DPolygon triangulate(ROI2DArea roi) throws TopologyException
    {
        ArrayList<Segment> segments = new ArrayList<Segment>();
        
        Rectangle bounds = roi.getBounds();
        
        int grid = 1;
        double halfgrid = 0.5 * grid;
        
        int cubeWidth = grid;
        int cubeHeight = grid * bounds.width;
        int cubeDiag = cubeWidth + cubeHeight;
        
        boolean[] mask = roi.getBooleanMask(roi.getBounds(), true);
        // erase first line and first row to ensure closed contours
        java.util.Arrays.fill(mask, 0, bounds.width - 1, false);
        for (int o = 0; o < mask.length; o += bounds.width)
            mask[o] = false;
        
        for (int j = 0; j < bounds.height; j += grid)
            for (int i = 0, index = j * bounds.width; i < bounds.width; i += grid, index += grid)
            {
                // The image is divided into square cells containing two
                // triangles each:
                //
                // a---b---
                // |../|../
                // |./.|./.
                // |/..|/..
                // c---d---
                //
                // By convention I choose to turn around the object in a
                // clockwise fashion
                // Warning: to ensure connectivity, the objects must NOT touch
                // the image border, strange behavior may occur otherwise
                
                boolean a = mask[index];
                boolean b = (i + grid < bounds.width) && mask[index + cubeWidth];
                boolean c = (j + grid < bounds.height) && mask[index + cubeHeight];
                boolean d = (i + grid < bounds.width) && (j + grid < bounds.height) && mask[index + cubeDiag];
                
                // For each triangle, check for difference between image values
                // to determine the contour location
                // => there are 6 possible combinations in each triangle, that
                // is 12 per cube
                
                if (a != b)
                {
                    if (b == c) // diagonal edge
                    {
                        if (a == false) // b,c are inside
                        {
                            createEdge(segments, i, j + 0.5, i + halfgrid, j);
                            
                        }
                        else
                        // b,c are outside
                        {
                            createEdge(segments, i + halfgrid, j, i, j + halfgrid);
                            
                        }
                    }
                    else
                    // a = c -> vertical edge
                    {
                        if (a == false) // a,c are outside
                        {
                            createEdge(segments, i + halfgrid, j + halfgrid, i + halfgrid, j);
                            
                        }
                        else
                        // a,c are inside
                        {
                            createEdge(segments, i + halfgrid, j, i + halfgrid, j + halfgrid);
                            
                        }
                    }
                }
                else // a = b -> horizontal edge only if c is different
                if (a != c)
                {
                    if (a == false) // a,b are outside
                    {
                        createEdge(segments, i, j + halfgrid, i + halfgrid, j + halfgrid);
                        
                    }
                    else
                    // a,b are inside
                    {
                        createEdge(segments, i + halfgrid, j + halfgrid, i, j + halfgrid);
                        
                    }
                }
                
                if (c != d)
                {
                    if (b == c) // diagonal edge
                    {
                        if (c == false) // b,c are outside
                        {
                            createEdge(segments, i + halfgrid, j + grid, i + grid, j + halfgrid);
                            
                        }
                        else
                        // b,c are inside
                        {
                            createEdge(segments, i + grid, j + halfgrid, i + halfgrid, j + grid);
                            
                        }
                    }
                    else
                    // b = d -> vertical edge
                    {
                        if (c == false) // b,d are inside
                        {
                            createEdge(segments, i + halfgrid, j + grid, i + halfgrid, j + halfgrid);
                            
                        }
                        else
                        // b,d are outside
                        {
                            createEdge(segments, i + halfgrid, j + halfgrid, i + halfgrid, j + grid);
                            
                        }
                    }
                }
                else // c = d -> horizontal edge only if b is different
                if (b != c)
                {
                    if (b == false) // c,d are inside
                    {
                        createEdge(segments, i + halfgrid, j + halfgrid, i + grid, j + halfgrid);
                        
                    }
                    else
                    // c,d are outside
                    {
                        createEdge(segments, i + grid, j + halfgrid, i + halfgrid, j + halfgrid);
                        
                    }
                }
            }
        
        if (segments.size() == 0) return null;
        
        Polygon poly = new Polygon();
        
        for (Point3d p : segments.get(0))
        {
            p.x += bounds.x;
            p.y += bounds.y;
            p.z = roi.getZ();
            
            poly.addPoint((int)p.x,(int)p.y);
        }
        
        return new ROI2DPolygon(poly);
    }

    private static void createEdge(ArrayList<Segment> segments, double xStart, double yStart, double xEnd, double yEnd)
    {
        double EPSILON = 0.00001;
        
        Point3d head = new Point3d(xStart, yStart, 0);
        Point3d tail = new Point3d(xEnd, yEnd, 0);
        
        if (segments.size() == 0)
        {
            segments.add(new Segment(head, tail));
            return;
        }
        
        int insertAtTailOf = -1, insertAtHeadOf = -1;
        
        for (int i = 0; i < segments.size(); i++)
        {
            if (tail.distance(segments.get(i).getHead()) <= EPSILON) insertAtHeadOf = i;
            else if (head.distance(segments.get(i).getTail()) <= EPSILON) insertAtTailOf = i;
        }
        
        if (insertAtTailOf >= 0)
        {
            if (insertAtHeadOf >= 0)
            {
                segments.get(insertAtHeadOf).addHead(segments.get(insertAtTailOf));
                segments.remove(insertAtTailOf);
            }
            else
            {
                segments.get(insertAtTailOf).addTail(tail);
            }
        }
        else if (insertAtHeadOf >= 0)
        {
            segments.get(insertAtHeadOf).addHead(head);
        }
        else
        {
            segments.add(new Segment(head, tail));
        }
    }

    
}