package plugins.faubin.cytomine.module.main.mvc;

import plugins.faubin.cytomine.module.main.IcytomineFrame;
import be.cytomine.client.Cytomine;

public abstract class Controller {
	
	private String name; // module name
	
	public Controller(String name, Cytomine cytomine){
		this.name = name;
	};
	
	/**
	 * this abstract function is used to get the view of the module
	 * @return View
	 */
	public abstract View getView(); 
	
	/**
	 * this abstract function is used to get the model of the module
	 * @return Model
	 */
	public abstract Model getModel(); 
	
	/**
	 * this function is used to add the module to the main interface
	 */
	public void applyToFrame(){
		IcytomineFrame.getIcytomineFrame().addModule(this);
	}
	
	/**
	 * the name if  the module
	 * @return String
	 */
	public String getName() {
		return name;
	}
	
}
