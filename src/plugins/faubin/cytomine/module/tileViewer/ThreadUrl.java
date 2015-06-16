package plugins.faubin.cytomine.module.tileViewer;

import java.awt.image.BufferedImage;
import java.util.Stack;

import be.cytomine.client.Cytomine;
import be.cytomine.client.CytomineException;


public class ThreadUrl extends Thread{

	private Stack<Tile> queue;
	private Stack<Tile>[] out_queue;
	private Cytomine cytomine;
	private int zoom;
	
	public ThreadUrl(Stack<Tile> queue, Stack<Tile>[] out_queue2, int zoom, Cytomine cytomine) {
		this.queue = queue;
		this.out_queue = out_queue2;
		this.cytomine = cytomine;
		this.zoom = zoom;
	}
	
	@Override
	public synchronized void run() {
		while(!queue.empty()){
			//take one tile from the queue
			try{
				Tile tile = queue.pop();
				String url = tile.url;
//				System.out.println(url);
				//download requested image urls from the queue
				BufferedImage downloaded = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
				
				try {
					downloaded = cytomine.downloadPictureAsBufferedImage(url,"");
					
				} catch (CytomineException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//put result in out_queue
				tile.image = downloaded;
				out_queue[zoom].push(tile);
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
	}
	
}
