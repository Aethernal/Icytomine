package plugins.faubin.cytomine.module.tileViewer.utils;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.Timer;

public class Tile {
	
	public String url;
	public int c;
	public int r;
	public BufferedImage image;
	public int time;
	
	public Tile(String url, int c, int r) {
		super();
		this.url = url;
		this.c = c;
		this.r = r;
		this.time = 0;
		
		Timer timer = new Timer(1000, new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				time++;
			}
			
		});
		timer.setRepeats(true);
		timer.start();
		
	}
	
	public void resetTime(){
		time = 0;
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
