package plugins.faubin.cytomine.module.main.mvc;

import icy.system.thread.ThreadUtil;
import plugins.faubin.cytomine.module.main.mvc.frame.ProcessingFrame;
import plugins.faubin.cytomine.oldgui.mvc.model.utils.Configuration;
import be.cytomine.client.Cytomine;

public abstract class Model {
	protected Cytomine cytomine;
	
	protected Configuration configuration = Configuration.getConfiguration();
	
	protected ProcessingFrame processFrame;
	
	public Model(Cytomine cytomine) {
		this.cytomine = cytomine;
		
		ThreadUtil.invokeLater(new Runnable() {

			@Override
			public void run() {
				processFrame = new ProcessingFrame();

			}
			
		});
		
	}

	public Cytomine getCytomine() {
		return cytomine;
	}
	
}
