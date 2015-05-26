package plugins.faubin.cytomine.gui.cytomine;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class testFrame extends JFrame{
	private static int id = 0;
	
	public testFrame(BufferedImage img) {
		try {
			id++;
			
			File file = new File("result/"+id+".png");
			System.out.println(img);
			ImageIO.write(img, "png", file);
			System.out.println(file.getAbsolutePath());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
