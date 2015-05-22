package plugins.faubin.cytomine.gui.tileViewer;

import java.awt.Color;

import javax.swing.JFrame;

import be.cytomine.client.Cytomine;
import be.cytomine.client.CytomineException;
import be.cytomine.client.models.ImageInstance;

public class launcher {

	public static void main(String[] args){
		final JFrame frame = new JFrame();
		frame.getContentPane().setBackground(Color.BLACK);
		frame.setSize(800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		Cytomine cytomine = new Cytomine("http://cytomine.web.pasteur.fr","d42e7fe6-4e84-4c04-82f5-9863d10d590e","d22a6cd4-04cb-4d06-8a46-082379219b4c");
		
		try {
			ImageInstance instance = cytomine.getImageInstance((long) 1451069);
			CytomineReader viewer = new CytomineReader(cytomine, instance, frame.getSize());
			frame.setContentPane(viewer);
			
		} catch (CytomineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		frame.setVisible(true);
		
	}
}
