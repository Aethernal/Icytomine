package plugins.faubin.cytomine.module.main.mvc;

import plugins.faubin.cytomine.module.main.IcytomineFrame;
import be.cytomine.client.Cytomine;

public abstract class Controller {
	
	private String name; // module name
	
	public Controller(String name, Cytomine cytomine){
		this.name = name;
	};
	
	public abstract View getView(); // return module view
	public abstract Model getModel(); // return module model
	
	public void applyToFrame(){
		IcytomineFrame.getIcytomineFrame().addModule(this);
	}
	
	public String getName() {
		return name;
	}
	
}
