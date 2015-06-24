package plugins.faubin.cytomine.module.main.mvc;

import plugins.faubin.cytomine.module.main.mvc.panel.Menu;
import plugins.faubin.cytomine.module.main.mvc.panel.Workspace;


public abstract class View{

	/*
	 * this function is used to get the workspace part of the view
	 */
	public abstract Workspace getWorkSpace();
	/*
	 * this function is used to get the menu part of the view
	 */
	public abstract Menu getMenu();
	
}
