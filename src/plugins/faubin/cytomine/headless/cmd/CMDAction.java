package plugins.faubin.cytomine.headless.cmd;

import java.util.concurrent.Callable;

public abstract class CMDAction implements Callable<Object>{
	protected String[] args;
	
	public CMDAction(String[] args) {
		super();
		this.args = args;
	}
	

}
