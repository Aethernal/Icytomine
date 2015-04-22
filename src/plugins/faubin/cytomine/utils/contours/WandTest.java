package plugins.faubin.cytomine.utils.contours;
import ij.ImagePlus;
import ij.gui.PolygonRoi;
import ij.gui.Roi;
import ij.gui.Wand;
import ij.process.ImageProcessor;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Date;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author lrollus
 */
public class WandTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        WandTest wand = new WandTest();
        ImagePlus img = new ImagePlus("/home/lrollus/Images/WANDTESTBIG.PNG");
        wand.doWand(img, 7000, 5346, 4, null);
       // ImagePlus img = new ImagePlus("/home/lrollus/Images/TESTWAND.png");
        //wand.doWand(img, 470, 334, 4, null);        
        
    }
    
	public static String doWand(ImagePlus img, int x, int y, double tolerance, String mode) {
		ImageProcessor ip = img.getProcessor();
		if ((img.getType()==ImagePlus.GRAY32) && Double.isNaN(ip.getPixelValue(x,y)))
			return "";
		int imode = Wand.LEGACY_MODE;
		if (mode!=null) {
			if (mode.startsWith("4"))
				imode = Wand.FOUR_CONNECTED;
			else if (mode.startsWith("8"))
				imode = Wand.EIGHT_CONNECTED;
		}
		Wand w = new Wand(ip);
		double t1 = ip.getMinThreshold();
		if (t1==ImageProcessor.NO_THRESHOLD || (ip.getLutUpdateMode()==ImageProcessor.NO_LUT_UPDATE&& tolerance>0.0))
			w.autoOutline(x, y, tolerance, imode);
		else
			w.autoOutline(x, y, t1, ip.getMaxThreshold(), imode);
		if (w.npoints>0) {
			Roi previousRoi = img.getRoi();
			int type = Wand.allPoints()?Roi.FREEROI:Roi.TRACED_ROI;
			Roi roi = new PolygonRoi(w.xpoints, w.ypoints, w.npoints, type);
                        
         
                        String str = "POLYGON((";
                        for(int i=0;i<w.xpoints.length;i++) {
                            if(w.xpoints[i]==0 && w.ypoints[i]==0) break;
                            str = str + w.xpoints[i]+" "+w.ypoints[i]+",";
                        }
                        str = str + w.xpoints[0]+" "+w.ypoints[0] + "))";
                        
			return str;
		}
		return "";
	}
        
        public static void writeInFile(String s) {
                            // Create file 
            try {
                System.out.println("writeInFile");
                FileWriter fstream = new FileWriter("result.txt");
                BufferedWriter out = new BufferedWriter(fstream);
                out.write(s);
                //Close the output stream
            out.close();  
            } catch(Exception e) {
                
            }
        }
}
