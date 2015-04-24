package plugins.faubin.cytomine.headless.cmd;

import plugins.faubin.cytomine.Config;
import plugins.faubin.cytomine.headless.Console;
import be.cytomine.client.Cytomine;


public class CMDConnect extends CMD{
	
	public CMDConnect(Console console) {
		super(console);
		
		String command = "connect";
		String description = "Allow to connect to cytomine, must be done before all other command";
		String[] arguments = new String[]{"(String) host","(String) public key", "(String) private key"};
		
		super.initialize(command, description, arguments);
		
	}
	
	@Override
	public Object execute(final String[] args){
		
		//generating action with arguments
		CMDAction action = new CMDAction(args) {
			
			@Override
			public Object call(){
				Cytomine cytomine = null;
					try {
						cytomine = new Cytomine(args[0], args[1], args[2]);
						cytomine.getProjects();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println(Config.messages.get("connection_failed"));
						cytomine = null;
					}
				
				return cytomine;
			}
		};
		
		//update and execute command
		super.updateAction(action);
		Object returned = super.execute(args);
		
		//process returned value
		if(returned!=null){
			console.cytomine = (Cytomine) returned;
		}
		
		return null;
	}
	
}
