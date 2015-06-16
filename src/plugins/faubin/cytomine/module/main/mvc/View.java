package plugins.faubin.cytomine.module.main.mvc;

import plugins.faubin.cytomine.module.main.mvc.panel.Menu;
import plugins.faubin.cytomine.module.main.mvc.panel.Workspace;


public abstract class View{

	public abstract Workspace getWorkSpace();
	public abstract Menu getMenu();
	
}
